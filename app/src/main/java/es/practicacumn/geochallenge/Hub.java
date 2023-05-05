package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
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
import es.practicacumn.geochallenge.Fragmentos.Frag_MisGymkhanas;
import es.practicacumn.geochallenge.Fragmentos.Frag_Usuario;
import es.practicacumn.geochallenge.Service.GymkhanaService;

public class Hub extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static final int REQUEST_CODE_NOTIFICATION = 1;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        pedirPermisos();
        String[]consejos= getResources().getStringArray(R.array.Consejo_del_dia);
        Random random = new Random();
        int indice = random.nextInt(consejos.length);

        drawerLayout=findViewById(R.id.drawerlayout1);
        navigationView=findViewById(R.id.navigationview1);
        toolbar=findViewById(R.id.toolbar1);
        toolbar.setTitle(consejos[indice]);

        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
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
                    case R.id.MisGymkhanas:
                        lanzarFragmentos(3);
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

        Intent intent =new Intent(this, GymkhanaService.class);
        startService(intent);

        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("LanzarAlerta")){
                    String idGymkhana=intent.getStringExtra("IdGymkhana");
                    InicioGymkhana(idGymkhana);
                }

            }
        };

    }

    private void InicioGymkhana(String idGymkhana) {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("LA GYMKHANA A COMENZADO");
        alerta.setMessage("¡Únete ahora!")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),Hub_jugando.class);
                        intent.putExtra("IdGymkhana",idGymkhana);
                        startActivity(intent);
                        finish();


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alerta.create().show();
    }

    private void pedirPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
            return;
        }
    }




    private void borrarDatos() {

        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("¿Desea eliminar su cuenta?");
        alerta.setMessage("Todos sus datos serán eliminados")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else Toast.makeText(Hub.this, "Ha surgido un error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alerta.create().show();
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
            case 3:
                Frag_MisGymkhanas fragMisGymkhanas=new Frag_MisGymkhanas();
                iniciarTransición(fragMisGymkhanas);
                break;

        }

    }

    private void iniciarTransición(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerHub, frag);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SalirApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void SalirApp() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("¿Desea salir de la aplicación?");
        alerta.setMessage("¿Está seguro de que desea salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alerta.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter("LanzarAlerta");
        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}