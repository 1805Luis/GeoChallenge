package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;

public class Mapa extends AppCompatActivity implements View.OnClickListener {
    private String informacion;
    private int ordenPrueba;
    private List<Prueba> listPruebas;
    private boolean ayuda;
    private Marker previous,markerinicial; // Variable para guardar el marcador anterior
    private IMapController mapController;
    private MapView map = null;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Toolbar toolbar1;
    private ImageView atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        toolbar1 =findViewById(R.id.tmap);
        setSupportActionBar(toolbar1);
        atras=findViewById(R.id.regreso);
        atras.setOnClickListener(this);
        map = findViewById(R.id.MapView);
        map.setBuiltInZoomControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(18.0);

        Bundle entrada = getIntent().getExtras();
        if (entrada!=null) {

            informacion = entrada.getString("Descripcion");
            ordenPrueba = entrada.getInt("Orden");
            listPruebas = (List<Prueba>) entrada.getSerializable("Pruebas");
            ayuda=entrada.getBoolean("Ayuda");
        }
        PermisoLocalizacion();
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

    }
    private void PermisoLocalizacion() {
        int permiso= ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
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
                Marker marker = new Marker(map);
                marker.setPosition((GeoPoint) point);
                Drawable Picono=getResources().getDrawable(R.drawable.ic_navegacion);
                marker.setIcon(Picono);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(marker);
                map.getController().setCenter(point);
                markerinicial = marker;
                if(ayuda){
                    Prueba prueba=listPruebas.get(ordenPrueba-1);
                    IGeoPoint point1=new GeoPoint(prueba.getLatitud(),prueba.getLongitud());
                    Marker marker1 = new Marker(map);
                    marker1.setPosition((GeoPoint) point1);
                }
            }
        };
        int permiso=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.regreso:
                VolverAtras();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            VolverAtras();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void VolverAtras() {
        Intent intent1=new Intent(this,HubJugando.class);
        intent1.putExtra("Descripcion",informacion);
        intent1.putExtra("Pruebas",(Serializable) listPruebas);
        intent1.putExtra("Orden",ordenPrueba);
        intent1.putExtra("Ayuda",ayuda);
        startActivity(intent1);
        finish();
    }


}