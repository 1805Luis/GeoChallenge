package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;

public class DetallesPrueba extends AppCompatActivity {
    TextView Orden,Coord,Info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_prueba);
        Orden = findViewById(R.id.OrdenDePrueba);
        Coord=findViewById(R.id.CoordenadasPrueba);
        Info=findViewById(R.id.InformacionPrueba);
        Prueba obj=(Prueba) getIntent().getExtras().getSerializable("objeto");
        Orden.setText("Esta es la pista: "+obj.getOrden());
        Coord.setText("Las coordenadas de la pista son ( "+obj.getLatitud()+" , "+obj.getLongitud()+")");
        Info.setText("La pista dice lo siguiente: "+obj.getDescripccion());
    }
}