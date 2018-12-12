package pl.aogiri.eventrio.event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventInterface {

    @GET("/api/events")
    Call<List<Event>> listEvents();

    @GET("/api/events/box")
    Call<List<Event>> listEventsBox(@Query("N") double N, @Query("E") double E, @Query("S") double S, @Query("W") double W);

    @GET("/api/events/{id}")
    Call<Event> getEvent(@Path("id") String id);

    //TODO test date box
    @GET("/api/events/box/date")
    Call<List<Event>> listEventsBox(@Query("N") double N, @Query("E") double E, @Query("S") double S, @Query("W") double W, @Query("date") String date);
}
