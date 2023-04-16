package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidePwd extends AppCompatActivity{
    private Button continuar;
    private EditText email;
    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_pwd);
        email=findViewById(R.id.Correo);
        continuar=findViewById(R.id.Siguiente);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperar();
            }
        });
    }

    private void recuperar() {
        correo=email.getText().toString().trim();
        if (!correo.isEmpty()&&Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            enviarCorreo();
            AlertDialog.Builder alerta= new AlertDialog.Builder(this);
            alerta.setTitle("Recuperacion de contraseña");
            alerta.setMessage("¿Le ha llegado el correo?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Volver a enviar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            enviarCorreo();
                        }
                    });
            alerta.create().show();
        }
    }

    private void enviarCorreo() {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(correo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Enviado con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ha habido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}