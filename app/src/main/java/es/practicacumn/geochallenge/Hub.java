package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import es.practicacumn.geochallenge.Fragmentos.Frag_Hub;
import es.practicacumn.geochallenge.Fragmentos.Frag_Usuario;

public class Hub extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        String[]consejos= getResources().getStringArray(R.array.Consejo_del_dia);
        Random random = new Random();
        int indice = random.nextInt(consejos.length);

        drawerLayout=findViewById(R.id.drawerlayout1);
        navigationView=findViewById(R.id.navigationview1);
        toolbar=findViewById(R.id.toolbar1);
        toolbar.setTitle(consejos[indice]);

        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().add(R.id.containerHub,new Frag_Hub()).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.VolveraCasa:
                        lanzarFragmentos(1);
                        break;
                    case R.id.DatosUsuario:
                        lanzarFragmentos(2);
                        break;
                    case R.id.CerraSesion:
                        signOut();
                        break;
                    case R.id.BorrarCuenta:
                        borrarDatos();
                        break;
                        
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void borrarDatos() {

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String UserId=mAuth.getUid();
        mDatabase.child("Usuario").child(UserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Hub.this, "Datos eliminados de la base de datos", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(Hub.this, "Ha surgido un error", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseUser user=mAuth.getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Hub.this, "Usuario eliminado con exito", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(Hub.this, "Ha surgido un error", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void lanzarFragmentos(int i) {
        switch (i){
            case 1:
                Frag_Hub fragHub=new Frag_Hub();
                iniciarTransición(fragHub);
                break;
            case 2:
                Frag_Usuario fragUsuario=new Frag_Usuario();
                iniciarTransición(fragUsuario);
                break;
        }

    }

    private void iniciarTransición(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerHub, frag);
        fragmentTransaction.commit();
    }
}