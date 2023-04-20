package es.practicacumn.geochallenge.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import es.practicacumn.geochallenge.CrearGymkhana;
import es.practicacumn.geochallenge.LugarGymkhana;
import es.practicacumn.geochallenge.R;

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
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.crearGY:
                Intent intent=new Intent(getContext(), LugarGymkhana.class);
                startActivity(intent);
                break;
            case R.id.auxilios:
                Toast.makeText(getContext(), "Listar Consejos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.participar:
                Toast.makeText(getContext(), "Participar Gymkhana", Toast.LENGTH_SHORT).show();
                break;
            case R.id.supervivencia:
                Toast.makeText(getContext(), "Listar Supervivencia", Toast.LENGTH_SHORT).show();
        }
    }
}