package pl.aogiri.eventrio.user;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserInterface {

    @POST("/api/user")
    Call<String> createAccount(@Body Map<String, String> body);

    @GET("/api/user/{uid}")
    Call<User> getUser(@Path("uid") String uid);

    @GET("/api/user/fb/{fid}")
    Call<User> getUserByFbid(@Path("fid") String fid);


}