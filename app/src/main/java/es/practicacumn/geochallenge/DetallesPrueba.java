package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;

public class DetallesPrueba extends AppCompatActivity {
    TextView Orden,Coord,Info;
    ImageView Codigo;
    String imagenUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_prueba);
        Orden = findViewById(R.id.OrdenDePrueba);
        Coord=findViewById(R.id.CoordenadasPrueba);
        Info=findViewById(R.id.InformacionPrueba);
        Codigo=findViewById(R.id.CodigoQR);
        Prueba obj=(Prueba) getIntent().getExtras().getSerializable("objeto");
        imagenUrl= obj.getCodigoQr();
        Picasso.get()
                .load(imagenUrl)
                .into(Codigo);
        Orden.setText("Esta es la pista: "+obj.getOrden());
        Coord.setText("Las coordenadas de la pista son ( "+String.format("%.4f"+obj.getLatitud())+" , "+String.format("%.4f"+obj.getLongitud())+")");
        Info.setText("La pista dice lo siguiente: "+obj.getDescripccion());
    }
}