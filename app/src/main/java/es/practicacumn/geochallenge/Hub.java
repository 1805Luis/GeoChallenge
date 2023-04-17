package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Hub extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button Crear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Crear=findViewById(R.id.CrearGymkhana);
        Crear.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navegation_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.DatosUsuario){
            Toast.makeText(this, "Te mostrare tus datos", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.CerraSesion){
            CerrarSesion();
        }
        return true;
    }

    private void CerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.CrearGymkhana:

        }
    }
}