package es.practicacumn.geochallenge.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.R;
public class Frag_Gymkhana extends Fragment {
    private TextView nombre,ubicacion,dificultad,participantes,inicio,fin,id;
    private Gymkhana gymkhana;
    private List<Prueba> pruebas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle =getArguments();
        if(bundle != null){
            gymkhana= (Gymkhana) bundle.getSerializable("gymkana");
        }
        View v= inflater.inflate(R.layout.fragment_gymkhana, container, false);
        inicializarDatos(v);
        nombre.setText("Nombre de la Gymkana: "+gymkhana.getNombre());
        ubicacion.setText("La gymkana se desarrollara en "+gymkhana.getLugar());
        dificultad.setText("La gymkana tiene una dificultad de tipo: "+gymkhana.getDificultad());
        inicio.setText("La gymkana comenzará a las "+gymkhana.getHoraInicio()+" el día "+gymkhana.getDiaInicio());
        fin.setText("La gymkana finalizará a las "+gymkhana.getHoraFin()+" el día "+gymkhana.getDiaFin());
        participantes.setText("Estará formada por un maximo de "+gymkhana.getMaxParticipantes());
        mostrarPruebas();
        return v;
    }

    private void mostrarPruebas() {
        pruebas=gymkhana.getPruebas();
        Bundle extra=new Bundle();
        extra.putSerializable("pruebas",(Serializable) pruebas);
        Frag_Pruebas FPruebas=new Frag_Pruebas();
        FPruebas.setArguments(extra);
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerPruebas,FPruebas);
        fragmentTransaction.commit();
    }

    private void inicializarDatos(View v) {
        nombre=v.findViewById(R.id.NombreG);
        ubicacion=v.findViewById(R.id.LugarG);
        dificultad=v.findViewById(R.id.DificultadG);
        participantes=v.findViewById(R.id.ParticipantesG);
        inicio=v.findViewById(R.id.InicioG);
        fin=v.findViewById(R.id.FinG);
    }


/*
    private void recibirDatos() {
        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            Nombre = extras.getString("NombreGY");
            Lugar = extras.getString("LugarGY");
            Dificultad = extras.getString("DificultadGY");
            ParticipantesMax = extras.getString("NParticipantes");
            FechaInicio = extras.getString("InicioFGY");
            HoraInicio = extras.getString("InicioHGY");
            FechaFin = extras.getString("FinFGY");
            HoraFin = extras.getString("FinHGY");
        }
    }*/
}