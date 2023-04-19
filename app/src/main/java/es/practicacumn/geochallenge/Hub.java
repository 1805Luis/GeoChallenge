package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import es.practicacumn.geochallenge.Fragmentos.Frag_Hub;
import es.practicacumn.geochallenge.Fragmentos.Frag_Usuario;


public class Hub extends AppCompatActivity  {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        drawerLayout = findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        InicioHub();
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.VolveraCasa:
                        InicioHub();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.DatosUsuario:
                        MostrarUsuario();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.CerraSesion:
                        CerrarSesion();
                        break;

                }
                return true;
            }
        });

    }

    private void MostrarUsuario() {
        Frag_Usuario fragUsuario=new Frag_Usuario();
        lanzarFragment(fragUsuario);
    }

    private void CerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void InicioHub() {
        Frag_Hub fragHub=new Frag_Hub();
        lanzarFragment(fragHub);
    }

    private void lanzarFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //replace elimina el fragmento existente y agrega un nuevo fragmento
        fragmentTransaction.replace(R.id.containerHub, frag);
        fragmentTransaction.commit();
    }

}