package es.practicacumn.geochallenge;

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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

public class LugarGymkhana extends AppCompatActivity {
    private String Nombre,Lugar,Dificultad,ParticipantesMax,FechaInicio,FechaFin,HoraInicio,HoraFin;
    private Marker previous; // Variable para guardar el marcador anterior
    private IMapController mapController;
    private MapView map = null;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private float startX, startY;
    private EditText LatitudP, LongitudP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugargymkhana);

        LatitudP=findViewById(R.id.LatitudPrueba);
        LongitudP =findViewById(R.id.LongitudPrueba);

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        previous=null;

        map = findViewById(R.id.MapView);
        map.setTileSource(TileSourceFactory.HIKEBIKEMAP);
        mapController=map.getController();
        mapController.setZoom(12.0);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);


        //Brujula
        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);


        GeoPoint point = new GeoPoint(45.845557, 26.170010);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);
        map.getController().setCenter(point);
        previous=startMarker;

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
                            if (previous != null) {
                                map.getOverlays().remove(previous);
                                previous = null;
                            }

                            // Obtén las coordenadas del punto clickeado
                            IGeoPoint point1 = map.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                            double latitud = point1.getLatitude();
                            double longitud = point1.getLongitude();
                            LatitudP.setText(String.valueOf(String.format("%.4f",latitud)));
                            LongitudP.setText(String.valueOf(String.format("%.4f",longitud)));

                            // Crea un nuevo marcador en la ubicación del punto clickeado
                            Marker marker = new Marker(map);
                            marker.setPosition((GeoPoint) point1);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            Intent intent=new Intent(getApplicationContext(),Hub.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



}
