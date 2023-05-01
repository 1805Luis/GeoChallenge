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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import es.practicacumn.geochallenge.ApuntarseGymkhana;
import es.practicacumn.geochallenge.ConsejosP;
import es.practicacumn.geochallenge.ConsejosS;
import es.practicacumn.geochallenge.CrearGymkhana;
import es.practicacumn.geochallenge.R;
import es.practicacumn.geochallenge.TiempoMeteorologico;

public class Frag_Hub extends Fragment implements View.OnClickListener {
    private Button Crear,Auxilios,Participar,Supervivencia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_hub, container, false);
        Crear=v.findViewById(R.id.crearGY);
        Crear.setOnClickListener(this);
        Auxilios=v.findViewById(R.id.auxilios);
        Auxilios.setOnClickListener(this);
        Participar=v.findViewById(R.id.participar);
        Participar.setOnClickListener(this);
        Supervivencia=v.findViewById(R.id.supervivencia);
        Supervivencia.setOnClickListener(this);

        //lanzarTiempo();
        return v;
    }

    private void lanzarTiempo() {
        Frag_TiempoMet frag_tiempoMet=new Frag_TiempoMet();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ElTiempo, frag_tiempoMet);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.crearGY:
                Intent intent=new Intent(getContext(), CrearGymkhana.class);
                startActivity(intent);
                break;
            case R.id.auxilios:
                Intent intent1=new Intent(getContext(), ConsejosP.class);
                startActivity(intent1);
                break;
            case R.id.participar:
                Intent intent2=new Intent(getContext(), ApuntarseGymkhana.class);
                startActivity(intent2);

                break;
            case R.id.supervivencia:
                Intent intent4=new Intent(getContext(), ConsejosS.class);
                startActivity(intent4);
                break;
        }
    }
}