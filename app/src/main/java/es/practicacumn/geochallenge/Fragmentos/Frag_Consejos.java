package es.practicacumn.geochallenge.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import es.practicacumn.geochallenge.Adaptadores.AdaptadorConsejos;
import es.practicacumn.geochallenge.Model.Consejos.Consejos;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.R;

public class Frag_Consejos extends Fragment {
    private List<Consejos> consejos;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_consejos, container, false);
        listView=v.findViewById(R.id.consejos);
        Bundle bundle =getArguments();
        if(bundle != null){
            consejos= (List<Consejos>) bundle.getSerializable("pruebas");
        }
        AdaptadorConsejos adaptadorConsejos=new AdaptadorConsejos(getActivity(), consejos);
        listView.setAdapter(adaptadorConsejos);

        return v;
    }
}