package es.practicacumn.geochallenge.Fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import es.practicacumn.geochallenge.R;

public class Frag_Mapa extends Fragment {
    private Marker previous,markerinicial; // Variable para guardar el marcador anterior
    private IMapController mapController;
    private MapView map = null;
    private float startX, startY;

    private OnMapClickListener mListener;

    public interface OnMapClickListener {
        void onMapClick(double lat, double lng);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnMapClickListener){
            mListener=(OnMapClickListener) context;
        }else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_mapa, container, false);
        getIniLocalizacion();
        map = v.findViewById(R.id.Mapa);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(18.0);
        IGeoPoint point=new GeoPoint(40.3845877,-3.6319141,15);
        Marker marker = new Marker(map);
        marker.setPosition((GeoPoint) point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
        map.getController().setCenter(point);

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
                            double latitud=point.getLatitude();
                            double longitud=point.getLongitude();
                            mListener.onMapClick(latitud,longitud);

                            // Crea un nuevo marcador en la ubicación del punto clickeado
                            Marker marker = new Marker(map);
                            marker.setPosition((GeoPoint) point);
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




        return v;
    }

    private void getIniLocalizacion() {
        int permiso= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }
}