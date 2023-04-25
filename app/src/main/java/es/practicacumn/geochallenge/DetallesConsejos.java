package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.practicacumn.geochallenge.Model.Consejos.Consejos;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;

public class DetallesConsejos extends AppCompatActivity {

    private TextView Titulo,Descripccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_consejos);
        Titulo=findViewById(R.id.TituloConsejo);
        Descripccion=findViewById(R.id.DescripccionConsejo);
        Consejos consejo=(Consejos) getIntent().getExtras().getSerializable("objeto");
        Titulo.setText(consejo.getOrden());
        Descripccion.setText(consejo.getDescripccion());

    }
}