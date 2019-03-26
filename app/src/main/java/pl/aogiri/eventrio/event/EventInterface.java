package pl.aogiri.eventrio.event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventInterface {

    @GET("/api/events")
    Call<List<Event>> listEvents();

    @GET("/api/events/{id}")
    Call<Event> getEvent(@Path("id") String id);

    @GET("/api/events/box")
    Call<List<Event>> listEventsBox(@Query("N") double N, @Query("S") double S, @Query("W") double W, @Query("E") double E, @Query("date") String date);
}
