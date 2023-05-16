package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;
import java.util.regex.Pattern;

public class CrearCuenta extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout emailTIL,pwdTIL,VpwdTIL;
    private TextInputEditText email,pwd, Vpwd;
    private String Email,Pwd, VPwd;
    private TextView tengoCredenciales;
    private Button continuar,VIP;
    private FirebaseAuth Auth;
    private static int tamanio=6;
    private SignInButton RegistraseGoogle;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";

    private static final Pattern Contrasenia=
            Pattern.compile("^" +
                    "(?=.*[0-9])"+           // al menos 1 numero
                    "(?=.*[a-z])"+           // al menos 1 letra minuscula
                    "(?=.*[A-Z])"+           // al menos 1 letra mayuscula
                    "(?=.*[@#$%&^+=/!¡¿?])"+  // al menos 1 caracter especial
                    "(?=\\S+$)"+             // no espacios em blaco
                    ".{"+tamanio+",}"+       // longitud minima de la contraseña
                    "$" );

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
        RegistraseGoogle=findViewById(R.id.RegistroGoogle);
        RegistraseGoogle.setOnClickListener(this);

        tengoCredenciales=findViewById(R.id.accederLogin);
        tengoCredenciales.setOnClickListener(this);
        SpannableString content = new SpannableString("Tengo cuenta");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tengoCredenciales.setText(content);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Continuar:
                comprobarDatos();
                break;
            case R.id.RegistroGoogle:
                iniciarGoogle();
                signIn();
                break;
            case R.id.accederLogin:
                Intent intent=new Intent(this,Login.class);
                startActivity(intent);
                finish();
                break;

        }
    }
    private boolean validarEmail(){
        Email=email.getText().toString().trim();

        if(Email.isEmpty()){
            emailTIL.setError("El campo no puede estar vacío");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            emailTIL.setError("Introduzca un email valido");
            return false;
        }else{
            emailTIL.setError(null);
            return true;
        }
    }

    private boolean validarContrasenia(){
        Pwd=pwd.getText().toString().trim();

        if(Pwd.isEmpty()){
            pwdTIL.setError("La contraseña no puede estar vacío");
            return false;
        } else if (!Contrasenia.matcher(Pwd).matches()) {
            pwdTIL.setError("Contraseña no valida");
            return false;
        }else{
            pwdTIL.setError(null);
            return true;
        }
    }

    private boolean validarContraseniasIguales(){
        VPwd = Vpwd.getText().toString().trim();

        if(VPwd.isEmpty()){
            VpwdTIL.setError("La contraseña no puede estar vacío");
            return false;
        } else if (!Pwd.equals(VPwd)) {
            VpwdTIL.setError("Las contraseñas han de ser iguales");
            return false;
        } else{
            pwdTIL.setError(null);
            return true;
        }
    }





    private void comprobarDatos() {
        if(validarEmail()){
            if(validarContrasenia()){
                if(validarContraseniasIguales()){
                    Auth.fetchSignInMethodsForEmail(Email).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            List<String> signInMethods = task.getResult().getSignInMethods();

                            //Usuario Registrado
                            if (signInMethods != null && !signInMethods.isEmpty()) {
                                CrearAlerta();
                            } else {
                                craerCuenta();
                            }
                        } else {
                            Toast.makeText(this, "Error al verificar cuenta", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }

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

    private void iniciarGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent=new Intent(CrearCuenta.this,Perfil.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(CrearCuenta.this, Hub.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
