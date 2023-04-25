package es.practicacumn.geochallenge.Interfaces;

import es.practicacumn.geochallenge.Model.Comun;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWeather {

    private static Retrofit retrofit=new Retrofit.Builder()
            .baseUrl(Comun.Base_URL_Weather)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static WeatherAPI service= retrofit.create(WeatherAPI.class);
    public static WeatherAPI getInstance(){
        if(service==null)service=retrofit.create(WeatherAPI.class);
        return service;
    }
}
