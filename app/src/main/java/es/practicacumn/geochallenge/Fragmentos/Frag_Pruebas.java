package es.practicacumn.geochallenge.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Adaptadores.AdaptadorPruebas;
import es.practicacumn.geochallenge.DetallesPrueba;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.R;

public class Frag_Pruebas extends Fragment {
    private ListView listView;
    private List<Prueba> pruebas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_pruebas, container, false);
        listView=v.findViewById(R.id.listView);
        Bundle bundle =getArguments();
        if(bundle != null){
            pruebas= (List<Prueba>) bundle.getSerializable("pruebas");
        }
        AdaptadorPruebas adaptador=new AdaptadorPruebas(getActivity(),pruebas);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Prueba obj=(Prueba) adapterView.getItemAtPosition(i);
                Intent paso = new Intent(getActivity(), DetallesPrueba.class);
                paso.putExtra("objeto",(Serializable) obj);
                startActivity(paso);
            }
        });

        return v;
    }
}