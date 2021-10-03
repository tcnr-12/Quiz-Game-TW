package tw.tcnrcloud110.quiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Q0100_WeatherServer {
    @GET("data/2.5/weather?")
    Call<Q0100_WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("lang") String lang, @Query("APPID") String app_id);
}