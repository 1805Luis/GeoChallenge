package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;

public class GymkhanasApuntadas extends AppCompatActivity{
    private TextView Nombre,Informacion,Inicio,Fin,NumeroPruebas,NumeroParticipantes,Dificultad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymkhanas_apuntadas);
        Incializar();
        Gymkhana gymkhana =(Gymkhana) getIntent().getExtras().getSerializable("gymkana");

        Nombre.setText("Nombre: "+ gymkhana.getNombre());
        Informacion.setText(gymkhana.getDescripcion());
        Inicio.setText("Inicio: "+ gymkhana.getDiaInicio()+", "+ gymkhana.getHoraInicio());
        Fin.setText("Fin: "+ gymkhana.getDiaFin()+", "+ gymkhana.getHoraFin());
        NumeroPruebas.setText("Estará compuesta por "+ gymkhana.getPruebas().size()+" pruebas");
        NumeroParticipantes.setText("Estará compuesta por "+ gymkhana.getMaxParticipantes()+" participantes");
        Dificultad.setText("Dificultad: "+ gymkhana.getDificultad());
    }

    private void Incializar() {
        Nombre=findViewById(R.id.GymkhanaTitulo);
        Informacion=findViewById(R.id.GymkhanaDescripcion);
        Inicio=findViewById(R.id.GymkhanaFechaInicio);
        Fin=findViewById(R.id.GymkhanaFechaFin);
        NumeroPruebas=findViewById(R.id.GymkhanaNumeroPruebas);
        NumeroParticipantes=findViewById(R.id.GymkhanaParticipantes);
        Dificultad=findViewById(R.id.GymkhanaDificultad);
    }

}