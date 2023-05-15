package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;

import es.practicacumn.geochallenge.Interfaces.RetrofitWeather;
import es.practicacumn.geochallenge.Interfaces.WeatherAPI;
import es.practicacumn.geochallenge.Model.Comun;
import es.practicacumn.geochallenge.Model.TiempoMeteorologico.Main;
import es.practicacumn.geochallenge.Model.TiempoMeteorologico.WeatherData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiempoMeteorologico extends AppCompatActivity implements View.OnClickListener {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat, lon;
    private EditText et;
    private TextView Ciudad_Pais, info, maxima, minima, actual, sensacion, viento, humedad, amanecer, atardecer, presion, direccion;
    private ImageView iconoTiempo, iconoMaxima, iconoMinima;
    private ImageButton Buscar, MiUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempometeorologico);
        InicializarIU();
        getIniLocalizacion();
        getLocalizacion();
    }

    private void InicializarIU() {
        et = findViewById(R.id.et);
        Ciudad_Pais = findViewById(R.id.Ciudad_Pais);
        iconoTiempo = findViewById(R.id.IconoTiempo);
        info = findViewById(R.id.descripcion);
        actual = findViewById(R.id.Temperatura);
        maxima = findViewById(R.id.Maxima);
        minima = findViewById(R.id.Minima);
        iconoMaxima = findViewById(R.id.IconoMaxima);
        iconoMinima = findViewById(R.id.IconoMinima);
        sensacion = findViewById(R.id.sensacion);
        viento = findViewById(R.id.Viento);
        humedad = findViewById(R.id.Humedad);
        amanecer = findViewById(R.id.amanecer);
        atardecer = findViewById(R.id.anochecer);
        presion = findViewById(R.id.presion);
        direccion = findViewById(R.id.direccionV);
        Buscar = findViewById(R.id.QueCiudad);
        Buscar.setOnClickListener(this);
        MiUbicacion = findViewById(R.id.EstoyAqui);
        MiUbicacion.setOnClickListener(this);

    }

    private void getLocalizacion() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    TiempoCoordenada(lat, lon);
                }
            }
        };
        int permiso = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 180, locationListener);
    }

    private void getIniLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
    private void TiempoCoordenada(Double lat,Double lon){
        WeatherAPI myapi= RetrofitWeather.getInstance();
        Call<WeatherData> examplecall = myapi.getweather(lat,lon, Comun.clave_Secundaria,Comun.units,Comun.lang);
        examplecall.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.code()==404){
                    Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful())){
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                }
                actualizarIU(response);

            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarIU(Response<WeatherData> response){
        WeatherData mydata=response.body();
        Main main = mydata.getMain();

        Picasso.get()
                .load("https://openweathermap.org/img/wn/"+mydata.getWeather().get(0).getIcon()+"@2x.png")
                .into(iconoTiempo);

        Picasso.get()
                .load("https://cdn-icons-png.flaticon.com/512/1312/1312332.png")
                .into(iconoMaxima);

        Picasso.get()
                .load("https://cdn-icons-png.flaticon.com/512/1312/1312331.png")
                .into(iconoMinima);


        Ciudad_Pais.setText(String.valueOf(mydata.getName()+" , "+mydata.getSys().getCountry()));
        info.setText(String.valueOf(mydata.getWeather().get(0).getDescription()));
        actual.setText(String.valueOf(Comun.cast(main.getTemp())+"°C"));
        sensacion.setText(String.valueOf("Sensacion de "+Comun.cast(main.getFeelsLike())+"°C"));
        maxima.setText(String.valueOf(Comun.cast(main.getTempMax())+"°C"));
        minima.setText(String.valueOf(Comun.cast(main.getTempMin())+"°C"));
        viento.setText(String.valueOf(mydata.getWind().getSpeed()+" m/s"));
        humedad.setText(String.valueOf(main.getHumidity()+"%"));
        amanecer.setText(String.valueOf(Comun.conversorUnixaHoras(mydata.getSys().getSunrise())));
        atardecer.setText(String.valueOf(Comun.conversorUnixaHoras(mydata.getSys().getSunset())));
        presion.setText(String.valueOf(main.getPressure()+" hpa"));
        direccion.setText(String.valueOf(Comun.direccionViento(mydata.getWind().getDeg())) +"( "+ mydata.getWind().getDeg() +" º)");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.QueCiudad:
                QueTiempoHará();
                break;
            case R.id.EstoyAqui:
                getLocalizacion();
                break;
        }
    }

    private void QueTiempoHará() {
        Geocoder geocoder = new Geocoder(TiempoMeteorologico.this);
        try {
        ArrayList<Address> addresses= (ArrayList<Address>) geocoder.getFromLocationName(et.getText().toString(),1);
        WeatherAPI myapi= RetrofitWeather.getInstance();
        Call<WeatherData> examplecall = myapi.getweather(addresses.get(0).getLatitude(),addresses.get(0).getLongitude(), Comun.clave_Secundaria,Comun.units,Comun.lang);
        examplecall.enqueue(new Callback<WeatherData>() {

            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.code()==404){
                    Toast.makeText(getApplicationContext(), "Porfavor introduzca una ciudad valida", Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful())){
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                }
                actualizarIU(response);
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}