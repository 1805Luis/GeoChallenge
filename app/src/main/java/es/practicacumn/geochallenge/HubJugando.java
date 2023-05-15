package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.practicacumn.geochallenge.Fragmentos.Frag_Brujula;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.Service.GymkhanaService;

public class HubJugando extends AppCompatActivity implements View.OnClickListener {
    private Button Auxilios,Mapa, Supervivencia,LeerQR;
    private TextView InformacionActual,Latitud,Longitud;
    private String idGymkhana,informacion,Lat,Lon,fecha,hora;
    private int ordenPrueba;
    private List<Prueba> listPruebas;
    private FirebaseDatabase GymkhanasRef;
    private int request_code =100;
    private FloatingActionButton pista;
    private RelativeLayout Coor;
    private FrameLayout brujula;
    private boolean ayuda;
    private Gymkhana gymkhana;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_jugando);
        verificarPermisos();
        listPruebas= new ArrayList<>();
        Coor=findViewById(R.id.RCoordenadas);
        Latitud=findViewById(R.id.Latitud);
        Longitud=findViewById(R.id.Longitud);
        brujula=findViewById(R.id.containerBrujula);
        Supervivencia =findViewById(R.id.Supervivencia);
        Supervivencia.setOnClickListener(this);
        Auxilios =findViewById(R.id.vital);
        Auxilios.setOnClickListener(this);
        Mapa=findViewById(R.id.mapa);
        Mapa.setOnClickListener(this);
        LeerQR=findViewById(R.id.leerQR);
        LeerQR.setOnClickListener(this);
        InformacionActual=findViewById(R.id.pista);
        pista=findViewById(R.id.obtenerPista);
        pista.setOnClickListener(this);
        detenerServicio();
        recibirDatos();
        lanzarBrujula();
        ayuda=false;
        diseño();
        destruirServicio();


    }

    private void destruirServicio() {
        if(idGymkhana!=null){
            DatabaseReference gymkhanaRef = FirebaseDatabase.getInstance().getReference("Gymkhana/"+idGymkhana);
            gymkhanaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        gymkhana = dataSnapshot.getValue(Gymkhana.class);
                        fecha=gymkhana.getDiaFin();
                        hora=gymkhana.getHoraFin();
                        destructor(fecha,hora);

                    } else {
                        Toast.makeText(HubJugando.this, "No se ha podido obtener", Toast.LENGTH_SHORT).show();                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HubJugando.this, "Error en la descarga de datos", Toast.LENGTH_SHORT).show();                }
            });

        }
    }

    private void destructor(String fecha, String hora) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            // Parsear la fecha y hora de cierre
            Date fechaHoraCierre = formato.parse(fecha + " " + hora);

            // Obtener la fecha y hora actual
            Date fechaHoraActual = new Date();

            // Calcular el tiempo restante en milisegundos hasta el cierre
            long tiempoRestante = fechaHoraCierre.getTime() - fechaHoraActual.getTime();

            if (tiempoRestante > 0) {
                // Programar el cierre de la actividad en el tiempo especificado
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref=database.getReference("Gymkhana/" + idGymkhana);
                        ref.child("estado").setValue("Finalizado");
                        Intent intent=new Intent(HubJugando.this,Hub.class);
                        startActivity(intent);
                        finish();
                    }
                }, tiempoRestante);
            } else {
                // La fecha y hora de cierre ya ha pasado, cerrar la actividad inmediatamente
                Intent intent=new Intent(HubJugando.this,Hub.class);
                startActivity(intent);
                finish();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void recibirDatos() {
        Bundle entrada = getIntent().getExtras();
        if (entrada!=null) {
            idGymkhana=entrada.getString("IdGymkhana");
            informacion = entrada.getString("Descripcion");
            ordenPrueba = entrada.getInt("Orden");
            listPruebas = (List<Prueba>) entrada.getSerializable("Pruebas");
            ayuda=entrada.getBoolean("Ayuda");

            InformacionActual.setText(informacion);
            if(ayuda){
                Ayuda();
            }
        }
    }

    private void diseño() {
        if(!ayuda){
            Coor.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams) brujula.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW,InformacionActual.getId());
            brujula.setLayoutParams(layoutParams);
        }else{
            RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams) brujula.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW,Coor.getId());
            Coor.setVisibility(View.VISIBLE);
            brujula.setLayoutParams(layoutParams);
        }
    }

    private void lanzarBrujula() {
        Frag_Brujula frag_brujula=new Frag_Brujula();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerBrujula, frag_brujula);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int PermisoCamara=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        if(PermisoCamara==PackageManager.PERMISSION_GRANTED){
        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA},request_code);
        }
    }

    private void detenerServicio(){
        Intent intent=new Intent(this, GymkhanaService.class);
        stopService(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SalirGymkhana();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void SalirGymkhana() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("¿Desea salir de la Gymkhana?");
        alerta.setMessage("¿Está seguro de que desea salir de la Gymkhana?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),Hub.class);
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Supervivencia:
                Intent intent=new Intent(getApplicationContext(),ConsejosS.class);
                intent.putExtra("Voy",1);
                startActivity(intent);
                break;
            case R.id.mapa:
                Intent intent1=new Intent(this, Mapa.class);
                intent1.putExtra("Descripcion",informacion);
                intent1.putExtra("Pruebas",(Serializable) listPruebas);
                intent1.putExtra("Orden",ordenPrueba);
                intent1.putExtra("Ayuda",ayuda);
                startActivity(intent1);
                break;
            case R.id.vital:
                Intent intent2=new Intent(getApplicationContext(),ConsejosP.class);
                intent2.putExtra("Voy",1);
                startActivity(intent2);
                break;
            case R.id.leerQR:
                LeerQR();
                break;
            case R.id.obtenerPista:
                Ayuda();
                break;
                
        }
    }

    private void Ayuda() {
        if(listPruebas.size()>0) {
            ayuda = true;
            double latitud = listPruebas.get(ordenPrueba - 1).getLatitud();
            double longitud = listPruebas.get(ordenPrueba - 1).getLongitud();
            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            Lat = decimalFormat.format(latitud);
            Lon = decimalFormat.format(longitud);
            Latitud.setText(Lat);
            Longitud.setText(Lon);
            diseño();
        }else{
            Toast.makeText(this, "No se ha leido ningún codigo QR", Toast.LENGTH_SHORT).show();
        }
    }


    private void LeerQR() {
        IntentIntegrator integrator=new IntentIntegrator(HubJugando.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            String datos=result.getContents();

            String patronQR = "^Orden de la prueba: \\d+ Id_Gymkhana: .+ Informacion: .+$";

            // Crear un objeto Pattern y Matcher para realizar la validación
            Pattern pattern = Pattern.compile(patronQR);
            Matcher matcher = pattern.matcher(datos);
        
            if(matcher.matches()){
                ayuda=false;
                diseño();
                //Extraer el orden de la prueba
                String orden = datos.split("Orden de la prueba: ")[1].split(" ")[0];
                ordenPrueba= Integer.parseInt(orden);

                // Extraer el ID de la Gymkhana
                idGymkhana = datos.split("Id_Gymkhana: ")[1].split(" ")[0];
                leerPruebas();

                // Extraer la información
                informacion = datos.split("Informacion: ")[1];

                InformacionActual.setText(informacion);

            }else{
                Toast.makeText(this, "El código QR no tiene la estructura válida", Toast.LENGTH_SHORT).show();
            }

        }

    private void leerPruebas() {
        GymkhanasRef= FirebaseDatabase.getInstance();
        DatabaseReference ref= GymkhanasRef.getReference("Gymkhana/"+idGymkhana+"/pruebas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPruebas.clear();
                if (snapshot.exists()) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Prueba prueba=dataSnapshot.getValue(Prueba.class);
                            listPruebas.add(prueba);
                        }
                    }else{
                    Toast.makeText(HubJugando.this, "No existen pruebas para esta gymkhana", Toast.LENGTH_SHORT).show();
                }
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HubJugando.this, "Error en la base de datos", Toast.LENGTH_SHORT).show();

            }
        });


    }
}