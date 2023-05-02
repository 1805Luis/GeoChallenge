package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;




import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.List;
import java.util.Locale;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.UbicacionGymkhana;

public class LugarGymkhana extends AppCompatActivity implements View.OnClickListener {
    private String Nombre, Lugar, Dificultad, ParticipantesMax, FechaInicio, FechaFin, HoraInicio, HoraFin,Descripcion,UserId;
    private Marker previous,markerinicial; // Variable para guardar el marcador anterior
    private IMapController mapController;
    private MapView map = null;
    private float startX, startY;
    private EditText LatitudP, LongitudP;
    private double LatitudUbicacion, LongitudUbicacion, Latitud,Longitud;
    private Button Busqueda,Siguiente;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugargymkhana);
        RecibirDatos();
        LatitudP = findViewById(R.id.LatitudPrueba);
        LongitudP = findViewById(R.id.LongitudPrueba);
        Busqueda=findViewById(R.id.Buscar);
        Busqueda.setOnClickListener(this);
        Siguiente=findViewById(R.id.ContinuarConPruebas);
        Siguiente.setOnClickListener(this);



        map = findViewById(R.id.MapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(18.0);

        getIniLocalizacion();
        getLocalizacion();

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        previous = null;


        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);


        //Brujula
        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Guarda la posición inicial del evento
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Calcula la distancia entre la posición inicial y la posición final del evento
                        float distance = (float) Math.sqrt(Math.pow(event.getX() - startX, 2) + Math.pow(event.getY() - startY, 2));

                        // Si la distancia es menor que un umbral específico, agrega el marcador
                        if (distance < 50) {
                            // Si hay un marcador anterior, elimínalo del mapa
                            map.getOverlays().remove(markerinicial);
                            if (previous != null) {
                                map.getOverlays().remove(previous);
                                previous = null;
                            }

                            // Obtén las coordenadas del punto clickeado
                            IGeoPoint point = map.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                            IntroduccirDatos(point);

                            // Crea un nuevo marcador en la ubicación del punto clickeado
                            Marker marker = new Marker(map);
                            marker.setPosition((GeoPoint) point);
                            Drawable Picono=getResources().getDrawable(R.drawable.ic_seleccionlugar);
                            marker.setIcon(Picono);
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                            // Agrega el nuevo marcador a la lista de overlays del mapa
                            map.getOverlays().add(marker);

                            // Asigna el nuevo marcador a la variable previousMarker
                            previous = marker;
                        }
                        break;
                }

                return false;
            }
        });
    }

    private void getLocalizacion() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if(previous!=null){
                    previous=null;
                }
                if(markerinicial!=null){
                    map.getOverlays().remove(markerinicial);
                }
                IGeoPoint point=new GeoPoint(location.getLatitude(),location.getLongitude());
                ColocarVariablesDouble(location.getLatitude(),location.getLongitude());
                IntroduccirDatos(point);
                Marker marker = new Marker(map);
                marker.setPosition((GeoPoint) point);
                Drawable Picono=getResources().getDrawable(R.drawable.ic_seleccionlugar);
                marker.setIcon(Picono);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(marker);
                map.getController().setCenter(point);
                markerinicial = marker;

            }
        };
        int permiso=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,5,locationListener);
    }

    private void getIniLocalizacion() {
        int permiso=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso==PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }


    private void IntroduccirDatos(IGeoPoint point1) {
        double latitud = point1.getLatitude();
        double longitud = point1.getLongitude();
        ColocarVariablesDouble(latitud,longitud);
        LatitudP.setText(String.valueOf(String.format("%.4f", latitud)));
        LongitudP.setText(String.valueOf(String.format("%.4f", longitud)));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            Aviso();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void Aviso() {
        AlertDialog.Builder aviso=new AlertDialog.Builder(this);
        aviso.setTitle("Desea retroceder");
        aviso.setMessage("No se guardara su progreso")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),CrearGymkhana.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        aviso.create().show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (map != null) {
            map.onDetach();  // Desvincula la vista del mapa
        }
        finish();  // Finaliza la actividad
    }
    @Override
    protected void onStart() {
        super.onStart();
        map.onResume();  // Reanuda la vista del mapa
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Buscar:
                RealizarBusqueda();
                break;
            case R.id.ContinuarConPruebas:
                UbicarCoordenadas();
                break;
        }
    }


    private void UbicarCoordenadas() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LatitudUbicacion, LongitudUbicacion, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                 Lugar = address.getAddressLine(0);
            } else {
                Toast.makeText(this, "Lugar Desconocido", Toast.LENGTH_SHORT).show();
                Lugar="Ubicacion desconocida";
            }
            EnviarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void RecibirDatos() {
        Bundle entrada = getIntent().getExtras();
        if (!entrada.isEmpty()) {
            Nombre = entrada.getString("NombreGY");
            Dificultad = entrada.getString("DificultadGY");
            ParticipantesMax = entrada.getString("NParticipantes");
            FechaInicio = entrada.getString("InicioFGY");
            HoraInicio = entrada.getString("InicioHGY");
            FechaFin = entrada.getString("FinFGY");
            HoraFin = entrada.getString("FinHGY");
            Descripcion=entrada.getString("Descripcion");
            UserId=entrada.getString("IdUsuario");

        }
    }

    private void EnviarDatos() {
        UbicacionGymkhana lugarPrueba=new UbicacionGymkhana(LatitudUbicacion,LongitudUbicacion);
        Bundle extras = new Bundle();
        extras.putString("NombreGY",Nombre);
        extras.putString("DificultadGY",Dificultad);
        extras.putString("NParticipantes",ParticipantesMax);
        extras.putString("InicioFGY",FechaInicio);
        extras.putString("InicioHGY",HoraInicio);
        extras.putString("FinFGY",FechaFin);
        extras.putString("FinHGY",HoraFin);
        extras.putString("Lugar",Lugar);
        extras.putSerializable("LugarPrueba",lugarPrueba);
        extras.putString("Descripcion",Descripcion);



        Intent intent=new Intent(getApplicationContext(),CrearPruebas.class);
        intent.putExtras(extras);
        startActivity(intent);
    }


    private void RealizarBusqueda() {
        AlertDialog.Builder alerta =new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_personalizada,null);
        alerta.setView(view);
        alerta.setCancelable(false);
        final AlertDialog dialog=alerta.create();
        dialog.show();

        RadioButton coord= view.findViewById(R.id.BuscarCoordenadas);
        RadioButton lugar=view.findViewById(R.id.BuscarLugar);
        LinearLayout linearLayout=view.findViewById(R.id.Coordenadas);
        EditText lat=view.findViewById(R.id.latitud);
        EditText lon=view.findViewById(R.id.longitud);
        EditText ubi=view.findViewById(R.id.Lugar);
        Button buscar=view.findViewById(R.id.Localizar);
        Button cancelar=view.findViewById(R.id.Cancelar);

        coord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayout.setVisibility(View.VISIBLE);
                    ubi.setVisibility(View.GONE);
                }
            }
        });
        lugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayout.setVisibility(View.GONE);
                    ubi.setVisibility(View.VISIBLE);
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lat=lat.getText().toString().trim();
                String Lon=lon.getText().toString().trim();
                String Lugar=ubi.getText().toString().trim();
                if(coord.isChecked()){
                    if (!Lat.isEmpty()&&!Lon.isEmpty()){
                        Latitud= Double.parseDouble(Lat);
                        Longitud= Double.parseDouble(Lon);
                        if(Latitud <=90&& Latitud >=-90){
                            if(Longitud <=180&& Longitud >=-180){
                                ColocarVariablesDouble(Latitud,Longitud);
                                SituarMapa(Latitud,Longitud);
                                dialog.dismiss();
                            }else Toast.makeText(LugarGymkhana.this, "La longitud debe estar entre -180º y 180º", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(LugarGymkhana.this, "La latitud debe estar entre -90º y 90º", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(LugarGymkhana.this, "Debe rellenar los campos latitud y longitud", Toast.LENGTH_SHORT).show();
                } else if (lugar.isChecked()){
                    if(!Lugar.isEmpty()){
                        SituarLugar(Lugar);
                        dialog.dismiss();
                    }else Toast.makeText(LugarGymkhana.this, "Debe poner un lugar a buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        }


    private void ColocarVariablesDouble(double latitud, double longitud) {
        LatitudUbicacion=latitud;
        LongitudUbicacion=longitud;
    }

    private void SituarLugar(String lugar) {
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocationName(lugar,1);
            if(addresses!=null&&!addresses.isEmpty()){
                Address address=addresses.get(0);
                SituarMapa(address.getLatitude(),address.getLongitude());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SituarMapa(double latitude,double longitude){
        map.getOverlays().remove(markerinicial);
        if(previous!=null){
            previous=null;
        }
            IGeoPoint point=new GeoPoint(latitude,longitude);
            IntroduccirDatos(point);
            Marker marker = new Marker(map);
            marker.setPosition((GeoPoint) point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setPosition((GeoPoint) point);
            Drawable Picono=getResources().getDrawable(R.drawable.ic_seleccionlugar);
            marker.setIcon(Picono);
            map.getOverlays().add(marker);
            map.getController().setCenter(point);
            previous = marker;

        }
}
