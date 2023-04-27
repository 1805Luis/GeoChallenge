package es.practicacumn.geochallenge.Fragmentos;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Adaptadores.AdaptadorConsejos;
import es.practicacumn.geochallenge.DetallesConsejos;
import es.practicacumn.geochallenge.Model.Consejos.Consejos;
import es.practicacumn.geochallenge.R;

public class Frag_Consejos extends Fragment {
    private List<Consejos> consejos;
    private ListView listView;
    private int tipo;
    private FrameLayout frameLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_consejos, container, false);
        listView=v.findViewById(R.id.consejos);
        frameLayout=v.findViewById(R.id.Consejo);
        Bundle bundle =getArguments();
        if(bundle != null){
            consejos= (List<Consejos>) bundle.getSerializable("consejos");
            tipo=bundle.getInt("tipo");
        }
        if(tipo==1){
            Drawable drawable = getResources().getDrawable(R.drawable.primerosauxilios);
            drawable.setAlpha(120);
            frameLayout.setBackground(drawable);

        } else if (tipo==2) {
            Drawable drawable1 = getResources().getDrawable(R.drawable.supervivencia);
            drawable1.setAlpha(175);
            frameLayout.setBackground(drawable1);
        }
        AdaptadorConsejos adaptadorConsejos=new AdaptadorConsejos(getActivity(), consejos);
        listView.setAdapter(adaptadorConsejos);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Consejos obj=(Consejos) adapterView.getItemAtPosition(i);
                Intent paso = new Intent(getActivity(), DetallesConsejos.class);
                paso.putExtra("objeto",(Serializable) obj);
                startActivity(paso);
            }
        });
        return v;
    }
}