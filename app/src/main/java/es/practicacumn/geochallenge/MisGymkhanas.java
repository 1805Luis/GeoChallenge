package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import es.practicacumn.geochallenge.Fragmentos.Frag_Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;

public class MisGymkhanas extends AppCompatActivity {
    private Gymkhana gymkana;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misgymkhanas);
        recogerDatos();
        InizializarFragmento();
    }

    private void InizializarFragmento() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("gymkana", (Serializable) gymkana);
        Frag_Gymkhana fGymkana = new Frag_Gymkhana();
        fGymkana.setArguments(bundle);
        // Crea una transacción de fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerGymkhanas, fGymkana);
        fragmentTransaction.commit();
    }

    private void recogerDatos() {
        Intent intent=getIntent();
        if(intent.hasExtra("gymkana")) {
            gymkana= (Gymkhana) intent.getSerializableExtra("gymkana");
        }
    }
}