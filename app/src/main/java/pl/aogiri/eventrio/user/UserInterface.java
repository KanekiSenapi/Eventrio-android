package pl.aogiri.eventrio.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserInterface {


    @GET("users/{uid}")
    Call<User> getUser(@Path("uid") String uid);

    @GET("users/fb/{fbid}")
    Call<User> getUserByFbid(@Path("fbid") String fbid);

    @POST("users/fb/create")
    Call<User> createUserByFb(@Body Object userFB);


}