package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Perfil extends AppCompatActivity implements View.OnClickListener {
    private Spinner sexo,sangre;
    private Calendar calendar;
    private FirebaseAuth mAuth;
    private EditText nombre,apellido,tlf,cumple,altura,peso,antecedentes,alergias;
    private String Unombre,Uapellido,Utlf,Usexo,Usangre,Uantecedentes,Ualergias,UserId,Ucumple,Upeso,Ualtura;
    private Button enviar;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        UserId=mAuth.getUid();
/*
        mDatabase.child("Usuario").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Intent intent= new Intent(PPerfil.this,PBusquedaGK.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PPerfil.this, "Error en la conexion en la base", Toast.LENGTH_SHORT).show();
            }
        });
        */
        nombre = findViewById(R.id.Nombre);
        apellido = findViewById(R.id.Apellidos);
        tlf = findViewById(R.id.Telefono);
        cumple=findViewById(R.id.FechaNacimiento);
        Comun.InicializarFecha(cumple,Perfil.this);
        inizializarSexo();
        inizializarSangre();
        altura = findViewById(R.id.Altura);
        peso = findViewById(R.id.Peso);
        antecedentes = findViewById(R.id.Medico);
        alergias = findViewById(R.id.Alergias);

        enviar=findViewById(R.id.DatosUsuario);
        enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DatosUsuario:
                Listo();
                break;

        }
    }
    private void inizializarSexo() {
        sexo=findViewById(R.id.Sexo_spinner);
        ArrayAdapter<CharSequence> adapterSexo= ArrayAdapter.createFromResource(this,R.array.sexo_array, android.R.layout.simple_spinner_item);
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapterSexo);
    }
    private void inizializarSangre() {
        sangre=findViewById(R.id.Sangre_spinner);
        ArrayAdapter<CharSequence> adapterSangre = ArrayAdapter.createFromResource(this,R.array.sangre_array, android.R.layout.simple_spinner_item);
        adapterSangre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sangre.setAdapter(adapterSangre);
    }
    public void Listo(){
        Inizializar();
        if(!Unombre.isEmpty()){
            if(!Uapellido.isEmpty()){
                if(!Utlf.isEmpty()&& Patterns.PHONE.matcher(Utlf).matches()){
                    if(!Ucumple.isEmpty()){
                        if(!Usexo.equals("Seleccione uno")){
                            if(!Usangre.equals("Seleccione uno")){
                                if(!Ualtura.isEmpty()){
                                    if(!Upeso.isEmpty()){
                                        if(!Uantecedentes.isEmpty()){
                                            if(!Ualergias.isEmpty()){
                                                subirDatos();
                                            }else{
                                                Ualergias="No tiene alergias";
                                                subirDatos();
                                            }
                                        }else{
                                            Uantecedentes="No tiene antecedentes medicos relevante";
                                            if(!Ualergias.isEmpty()){
                                                subirDatos();
                                            }else{
                                                Ualergias="No tiene alergias";
                                                subirDatos();
                                            }
                                        }
                                    }else Toast.makeText(this, "Introduzca un peso en kg y termine de rellenar el formulario", Toast.LENGTH_SHORT).show();
                                }else Toast.makeText(this, "Introduzca un altura en cm y termine de rellenar el formulario" , Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(this, "Seleccione un valor (sangre) y termine de rellenar el formulario"  , Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(this, "Seleccione un valor (sexo) y termine de rellenar el formulario"     , Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(this, "Introduzca su fecha de nacimiento y termine de rellenar el formulario" , Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, "Debe rellenar el campo del telefono y termine de rellenar el formulario" , Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Debe rellenar el campo del apellido y termine de rellenar el formulario", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this, "Debe rellenar el campo del nombre y termine de rellenar el formulario", Toast.LENGTH_SHORT).show();

    }

    private void subirDatos() {
        Usuario usuario= new Usuario(Unombre,Uapellido,Utlf,Ucumple,Usexo,Usangre,Ualtura,Upeso,Uantecedentes,Ualergias);
        mDatabase.child("Usuario").child(UserId).setValue(usuario);
        Toast.makeText(this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(getApplicationContext(),Hub.class);
        startActivity(intent);
        finish();


    }

    private void Inizializar() {
        Unombre=nombre.getText().toString().trim();
        Uapellido=apellido.getText().toString().trim();
        Utlf=tlf.getText().toString().trim();
        Ucumple=cumple.getText().toString().trim();
        Usexo=sexo.getSelectedItem().toString();
        Usangre=sangre.getSelectedItem().toString();
        Ualtura=altura.getText().toString().trim();
        Upeso=peso.getText().toString().trim();
        Uantecedentes=antecedentes.getText().toString().trim();
        Ualergias=alergias.getText().toString().trim();

    }
}
