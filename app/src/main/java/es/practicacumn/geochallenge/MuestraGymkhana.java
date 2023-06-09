package es.practicacumn.geochallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.practicacumn.geochallenge.Fragmentos.Frag_Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.Service.GenerarPdf;

public class MuestraGymkhana extends AppCompatActivity implements View.OnClickListener {
    private Gymkhana gymkana;
    private Button crearGK;
    private DatabaseReference mDatabase;
    private List<Prueba> pruebaList=new ArrayList<>();
    private String Nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_gymkhana);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        recogerDatos();
        InizializarFragmento();
        crearGK=findViewById(R.id.crear);
        crearGK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.crear:
                CrearAlerta();
                break;

        }
    }

    private void CrearAlerta() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("La gymkhana ha sido creada");
        alerta.setMessage("¿Desea descargar los QR de las pistas?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Gymkhana").child(gymkana.getId()).setValue(gymkana);
                        LanzarServicio();
                        Intent intent=new Intent(getApplicationContext(),Hub.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Gymkhana").child(gymkana.getId()).setValue(gymkana);
                        Intent intent=new Intent(getApplicationContext(),Hub.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alerta.create().show();
    }

    private void LanzarServicio() {
        Intent intent=new Intent(this, GenerarPdf.class);
        intent.putExtra("Pruebas",(Serializable) pruebaList);
        intent.putExtra("Nombre",Nombre);
        startService(intent);
    }
    private void InizializarFragmento() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("gymkana", (Serializable) gymkana);
        Frag_Gymkhana fGymkana = new Frag_Gymkhana();
        fGymkana.setArguments(bundle);
        // Crea una transacción de fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerGK, fGymkana);
        fragmentTransaction.commit();
    }

    private void recogerDatos() {
        Intent intent=getIntent();
        if(intent.hasExtra("gymkana")) {
            gymkana= (Gymkhana) intent.getSerializableExtra("gymkana");
            for(Prueba prueba: gymkana.getPruebas()){
                pruebaList.add(prueba);
            }
            Nombre=gymkana.getNombre();
        }
    }
}