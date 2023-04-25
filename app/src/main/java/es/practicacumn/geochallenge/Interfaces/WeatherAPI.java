package es.practicacumn.geochallenge.Interfaces;

import es.practicacumn.geochallenge.Model.TiempoMeteorologico.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?")
    Call<WeatherData> getweather(@Query("lat") Double lat,
                                 @Query("lon") Double lon,
                                 @Query("appid") String apikey,
                                 @Query("units") String units,
                                 @Query("lang") String lan);
}
