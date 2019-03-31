package pl.aogiri.eventrio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = "http://192.168.1.17:8080/api/";
    public static final String API_USERNAME = "";
    public static final String API_PASSWORD = "";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
    private static Gson gson = gsonBuilder.create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(Credentials.basic(API_USERNAME, API_PASSWORD));
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        return retrofit.create(serviceClass);
    }
}