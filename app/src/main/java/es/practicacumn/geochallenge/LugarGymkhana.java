package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

public class LugarGymkhana extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private String Nombre,Lugar,Dificultad,ParticipantesMax,FechaInicio,FechaFin,HoraInicio,HoraFin;
    private Marker previous = null; // Variable para guardar el marcador anterior
    private Marker lastMarkerPosition = null;
    private IMapController mapController;
    private MapView map = null;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private GestureDetector gestureDetector;
    float lastX,lastY,oldDist;
    int mode,NONE=0,DRAG=1,ZOOM=2;
    boolean isAddingMarker=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugargymkhana);

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.MapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
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

        //Desplazar el marcador
        /*startMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
               // Se llama cuando se empieza a arrastrar el marcador
            }
            @Override
            public void onMarkerDrag(Marker marker) {
                // Se llama cuando se está arrastrando el marcador
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Se llama cuando se suelta el marcador
                // Obtiene la nueva ubicación del marcador
                GeoPoint newPosition = marker.getPosition();
                // Actualiza la posición del marcador en el mapa
                marker.setPosition(newPosition);
                // Invalida el MapView para que se reflejen los cambios
                map.invalidate();
            }


        });
        startMarker.setDraggable(true);
        map.getOverlays().add(startMarker);*/
        map.setMultiTouchControls(true);

        // Configurar el detector de gestos
        gestureDetector = new GestureDetector(this, this);
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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (isAddingMarker) {
                    GeoPoint geoPoint = (GeoPoint) map.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                    if (previous != null) {
                        map.getOverlays().remove(previous);
                    }
                    Marker marker = new Marker(map);
                    marker.setPosition(geoPoint);
                    map.getOverlays().add(marker);
                    map.invalidate();
                    previous = marker;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                isAddingMarker=false;
                lastX = event.getX();
                lastY = event.getY();
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode==DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                    int deltaX = (int) (lastX - x);
                    int deltaY = (int) (lastY - y);
                    map.scrollBy(deltaX, deltaY);
                    lastX = x;
                    lastY = y;
                    isAddingMarker=true;
                } else if (mode==ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        float scale = newDist / oldDist;
                        int zoomLevel = map.getZoomLevel();
                        if (scale > 1) {
                            zoomLevel += 1;
                        } else {
                            zoomLevel -= 1;
                        }
                        map.getController().setZoom(zoomLevel);
                        oldDist = newDist;

                    }

                    isAddingMarker=true;
                }
                    break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isAddingMarker=false;
                // Iniciar el gesto de pellizco
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //Finalizar el gesto de pellizco
                mode=NONE;

                isAddingMarker=true;
                break;

        }
        return true;
    }
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
