package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.practicacumn.geochallenge.Fragmentos.Frag_Pruebas;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;

public class CrearPruebas extends AppCompatActivity implements View.OnClickListener {
    String Nombre,Lugar,Dificultad,ParticipantesMax,FechaInicio,FechaFin,HoraInicio,HoraFin,Id;
    private List<Prueba> ListaPruebas;
    private EditText EOrden,ELat,ELon,EInfo;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pruebas);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        ListaPruebas= new ArrayList<>();
        EOrden=findViewById(R.id.Orden);
        ELat=findViewById(R.id.Latitud);
        ELon=findViewById(R.id.Longitud);
        EInfo=findViewById(R.id.Descripccion);
        Button enviar = findViewById(R.id.IntroduccirDatos);
        enviar.setOnClickListener(this);
        Button continuar = findViewById(R.id.Terminar);
        continuar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IntroduccirDatos:
                introducirDatos();
                break;
            case R.id.Terminar:
                if(ListaPruebas.size()+1<=2){
                    Toast.makeText(this, "Debe introduccir al menos 2 pistas", Toast.LENGTH_SHORT).show();
                }else{
                    almacenarDatos();
                }
                break;
        }
    }
    private void almacenarDatos() {
        recibirDatos();
        generarId();
        Gymkhana gymkhana= new Gymkhana(Id,Nombre,Lugar,Dificultad,FechaInicio,FechaFin,HoraInicio,HoraFin,ParticipantesMax,ListaPruebas,false);
        mDatabase.child("Gymkhana").child(Id).setValue(gymkhana);
        cambiarActividad(gymkhana);

    }

    private void generarId() {
        UUID uuid = UUID.randomUUID();
        Id = uuid.toString().replaceAll("-", "");
    }

    private void cambiarActividad(Gymkhana gymkana) {
        Intent intent= new Intent(getApplicationContext(),MostraGymkhana.class);
        intent.putExtra("gymkana",gymkana);
        startActivity(intent);

    }

    private void recibirDatos() {

        Bundle extras=getIntent().getExtras();
        if(!extras.isEmpty()) {
            Nombre = extras.getString("NombreGY");
            Lugar = extras.getString("LugarGY");
            Dificultad = extras.getString("DificultadGY");
            ParticipantesMax = extras.getString("NParticipantes");
            FechaInicio = extras.getString("InicioFGY");
            HoraInicio = extras.getString("InicioHGY");
            FechaFin= extras.getString("FinFGY");
            HoraFin=extras.getString("FinHGY");

        }
    }

    private void introducirDatos() {
        String torden = EOrden.getText().toString().trim();
        String TLat = ELat.getText().toString().trim();
        String TLon = ELon.getText().toString().trim();
        String infoPr = EInfo.getText().toString().trim();
        if(!torden.isEmpty()){
            if(EsNumerico(torden)){
                int orden = Integer.parseInt(torden);
                if(orden ==(ListaPruebas.size()+1)){
                    if(!TLat.isEmpty()){
                        double lat = Double.parseDouble(TLat);
                        if(lat <=90&& lat >=-90){
                            if(!TLon.isEmpty()){
                                double lon = Double.parseDouble(TLon);
                                if(lon <=180&& lon >=-180){
                                    if(!infoPr.isEmpty()){
                                        ListaPruebas.add(new Prueba(orden, lat, lon, infoPr));
                                        EOrden.getText().clear();
                                        ELat.getText().clear();
                                        ELon.getText().clear();
                                        EInfo.getText().clear();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("pruebas", (Serializable) ListaPruebas);
                                        Frag_Pruebas fPruebas = new Frag_Pruebas();
                                        fPruebas.setArguments(bundle);
                                        // Crea una transacción de fragmentos
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        //replace elimina el fragmento existente y agrega un nuevo fragmento
                                        fragmentTransaction.replace(R.id.container, fPruebas);
                                        fragmentTransaction.commit();
                                    }else Toast.makeText(this, "La informacion de la pista no puede ser vacía", Toast.LENGTH_SHORT).show();
                                }else Toast.makeText(this, "La longitud debe estar entre -180º y 180º", Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(this, "La longitud no puede ser nula", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(this, "La latitud debe estar entre -90º y 90º", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(this, "La latitud no puede ser nula", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, "El numero de la pista a de ser "+(ListaPruebas.size()+1), Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Se debe introducir un numero", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this, "Introduzca el numero de la pista", Toast.LENGTH_SHORT).show();



    }

    private boolean EsNumerico(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}