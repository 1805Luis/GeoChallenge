package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import es.practicacumn.geochallenge.Fragmentos.Frag_Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;

public class MostraGymkhana extends AppCompatActivity implements View.OnClickListener {
    private Gymkhana gymkana;
    private Button crearGK;
    private DatabaseReference mDatabase;
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
                mDatabase.child("Gymkhana").child(gymkana.getId()).setValue(gymkana);
                Toast.makeText(this, "Creada con exito", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Hub.class);
                startActivity(intent);
                finish();
        }
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
        }
    }
}