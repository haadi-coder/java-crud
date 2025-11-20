package com.mycompany.hotel.http;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.hotel.db.entity.Hotel;
import com.mycompany.hotel.service.HotelNotFoundException;
import com.mycompany.hotel.service.HotelService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JsonMapper;

public class HotelHttp {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final JsonMapper jsonMapper = new JsonMapper() {
        @Override
        public String toJsonString(Object obj, Type type) {
            return gson.toJson(obj);
        }

        @Override
        public <T> T fromJsonString(String json, Type type) {
            return gson.fromJson(json, type);
        }
    };

    private final HotelService hotelService;
    private final Javalin app;

   public HotelHttp(HotelService hotelService) {
        this.hotelService = hotelService;
        this.app = Javalin.create(config -> config.jsonMapper(jsonMapper)).start(8080);
        configureRoutes();
        configureExceptionHandlers();
        System.out.println("HTTP server started at http://localhost:8080");
    }

    private void configureRoutes() {
        app.get("/hotels", this::getAllHotels);
        app.get("/hotels/{id}", this::getHotelById);
        app.post("/hotels", this::createHotel);
        app.put("/hotels/{id}", this::updateHotel);
        app.delete("/hotels/{id}", this::deleteHotel);
    }

    private void configureExceptionHandlers() {
        app.exception(HotelNotFoundException.class, (exception, ctx) -> {
            ctx.status(404)
               .json(new ErrorResponse(exception.getMessage()));
        });

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500)
               .json(new ErrorResponse("Internal server error"));
        });
    }


    private void getAllHotels(Context ctx) {
        ctx.json(hotelService.findAll());
    }

    private void getHotelById(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Hotel hotel = hotelService.findById(id);
        ctx.json(hotel);
    }

    private void createHotel(Context ctx) {
        CreateHotelRequest request = ctx.bodyAsClass(CreateHotelRequest.class);
        int createdId = hotelService.save(request.title(), request.rating());
        ctx.status(201)
           .json(new IdResponse(createdId));
    }

    private void updateHotel(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        CreateHotelRequest request = ctx.bodyAsClass(CreateHotelRequest.class);

        Hotel hotel = new Hotel(id, request.title(), request.rating());
        hotelService.update(hotel);
        ctx.status(204); // No content
    }

    private void deleteHotel(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        hotelService.deleteById(id);
        ctx.status(204);
    }

    public void stop() {
        app.stop();
    }

    record CreateHotelRequest(String title, int rating) {}
    record IdResponse(int id) {}
    record ErrorResponse(String error) {}
}
