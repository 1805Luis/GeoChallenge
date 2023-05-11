package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.regex.Pattern;

public class CrearCuenta extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout emailTIL,pwdTIL,VpwdTIL;
    private TextInputEditText email,pwd, Vpwd;
    private String Email,Pwd, VPwd;
    private Button continuar,VIP;
    private FirebaseAuth Auth;
    private int tamanio=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        Auth=FirebaseAuth.getInstance();
        emailTIL=findViewById(R.id.EmailCC);
        email=(TextInputEditText)emailTIL.getEditText();
        pwdTIL=findViewById(R.id.PasswordCC);
        pwd=(TextInputEditText)pwdTIL.getEditText();
        VpwdTIL =findViewById(R.id.ConfirmationPwdCC);
        Vpwd=(TextInputEditText)VpwdTIL.getEditText();
        continuar=findViewById(R.id.Continuar);
        continuar.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Continuar:
                comprobarDatos();
                break;

        }
    }





    private void comprobarDatos() {
        Email=email.getText().toString().trim();
        Pwd=pwd.getText().toString().trim();
        VPwd = Vpwd.getText().toString().trim();
        if(!Email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            if(!Pwd.isEmpty()&&!VPwd.isEmpty()){
                if (Pwd.length()>=tamanio){
                    if(comprobarContrasenia(Pwd)){
                        if(Pwd.equals(VPwd)){
                            Auth.fetchSignInMethodsForEmail(Email).addOnCompleteListener(task -> {

                                if (task.isSuccessful()) {
                                    List<String> signInMethods = task.getResult().getSignInMethods();

                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        CrearAlerta();
                                    } else {
                                        craerCuenta();
                                    }
                                } else {
                                    Toast.makeText(this, "Error al verificar cuenta", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else Toast.makeText(this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(this, "Las contraseña debe incluir numeros, letras, caracteres especiales", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, "Introduzca una contraseña con "+tamanio+" o mas caracteres(numeros, letras, caracteres especiales)", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Las contraseñas no pueden estar vacías", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this, "EL campo no debe estar vacío o introduzca un correo valido ", Toast.LENGTH_SHORT).show();
    }

    private void CrearAlerta() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("Ya esta registrado");
        alerta.setMessage("¿Qué desea hacer?")
                .setCancelable(false)
                .setPositiveButton("Volver al inicio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Recuperar Contraseña", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),OlvidePwd.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alerta.create().show();
    }

    private void craerCuenta() {
        Auth.createUserWithEmailAndPassword(Email,Pwd).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CrearCuenta.this, "Cuenta Creada con exito", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = Auth.getCurrentUser();
                            user.sendEmailVerification();
                            Intent intent = new Intent(getApplicationContext(), VerificarCuenta.class);
                            intent.putExtra("email",Email);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(CrearCuenta.this, "No se pudo crear la cuenta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean comprobarContrasenia(String pwd) {
        boolean seguro=false;
        Pattern patronSeguridad= Pattern.compile(
                "^" +
                        "(?=.*[0-9])"+           // al menos 1 numero
                        "(?=.*[a-z])"+           // al menos 1 letra minuscula
                        "(?=.*[A-Z])"+           // al menos 1 letra mayuscula
                        "(?=.*[@#$%&+=/!¡¿?])"+  // al menos 1 caracter especial
                        "(?=\\S+$)"+             // no espacios em blaco
                        ".{"+tamanio+",}"+       // longitud minima de la contraseña
                        "$"
        );

        if(patronSeguridad.matcher(pwd).matches()){
            seguro= true;
        }

        return seguro;
    }


}
