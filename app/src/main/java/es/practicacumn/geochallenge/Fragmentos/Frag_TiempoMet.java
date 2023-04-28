package es.practicacumn.geochallenge.Fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import es.practicacumn.geochallenge.Interfaces.RetrofitWeather;
import es.practicacumn.geochallenge.Interfaces.WeatherAPI;
import es.practicacumn.geochallenge.Model.Comun;
import es.practicacumn.geochallenge.Model.TiempoMeteorologico.Main;
import es.practicacumn.geochallenge.Model.TiempoMeteorologico.WeatherData;
import es.practicacumn.geochallenge.R;
import es.practicacumn.geochallenge.TiempoMeteorologico;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Frag_TiempoMet extends Fragment implements LocationListener{
    private LocationManager locationManager;
    private TextView Temperatura,Maxima,Minima,coordenadas;
    private ImageView tiempo;
    private FrameLayout frameLayout;
    double Latitud,Longitud;

    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tiempo_met, container, false);
        InicializarIU(v);
        return v;
    }



    private void actualizarIU(Response<WeatherData> response){
            WeatherData mydata=response.body();
            Main main = mydata.getMain();

            Picasso.get()
                    .load("https://openweathermap.org/img/wn/"+mydata.getWeather().get(0).getIcon()+"@2x.png")
                    .into(tiempo);

            Temperatura.setText(String.valueOf(Comun.cast(main.getTemp())+"°C"));
            Maxima.setText(String.valueOf(Comun.cast(main.getTempMax())+"°C"));
            Minima.setText(String.valueOf(Comun.cast(main.getTempMin())+"°C"));
        }

    private void InicializarIU(View v) {
        tiempo = v.findViewById(R.id.ImagenTiempo);
        Temperatura = v.findViewById(R.id.Temp);
        Maxima = v.findViewById(R.id.tempMax);
        Minima = v.findViewById(R.id.tempMin);
        frameLayout=v.findViewById(R.id.ActividadTiempo);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TiempoMeteorologico.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitud = location.getLatitude();
        Longitud = location.getLongitude();
        MiTiempo(Latitud,Longitud);

    }

    private void MiTiempo(double latitude, double longitude) {
        WeatherAPI myapi= RetrofitWeather.getInstance();
        Call<WeatherData> examplecall = myapi.getweather(latitude,longitude, Comun.apikey_Weather,Comun.units,Comun.lang);
        examplecall.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.code()==404){
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful())){
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_LONG).show();
                }
                actualizarIU(response);

            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}