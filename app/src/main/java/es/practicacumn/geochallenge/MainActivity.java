package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth Auth;
    private Button tengocredenciales,soyNuevo,olvidePwd,accesoVIP;
    private EditText email,contrasenia;
    private String correo, pwd;
    private SignInButton AccesoGoogle;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Auth = FirebaseAuth.getInstance();
        email =findViewById(R.id.Email);
        contrasenia =findViewById(R.id.Password);
        tengocredenciales =findViewById(R.id.Acceder);
        tengocredenciales.setOnClickListener(this);
        soyNuevo=findViewById(R.id.CrearCuenta);
        soyNuevo.setOnClickListener(this);
        olvidePwd=findViewById(R.id.OlvidePwd);
        olvidePwd.setOnClickListener(this);
        AccesoGoogle=findViewById(R.id.AccederGoogle);
        AccesoGoogle.setOnClickListener(this);
        accesoVIP=findViewById(R.id.AccesoSinLogin);
        accesoVIP.setOnClickListener(this);
    }

    @Override
   public void onClick(View view) {
        switch (view.getId()){
            case R.id.Acceder:
                Acceder();
                break;

            case R.id.CrearCuenta:
                Intent intent= new Intent(getApplicationContext(),CrearCuenta.class);
                startActivity(intent);
                break;

            case R.id.OlvidePwd:
                Intent intent1=new Intent(getApplicationContext(),OlvidePwd.class);
                startActivity(intent1);
                break;

            case R.id.AccederGoogle:
                iniciarGoogle();
                signIn();
                break;

            case R.id.AccesoSinLogin:
                Intent intent4= new Intent(getApplicationContext(),Hub.class);
                startActivity(intent4);

        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = Auth.getCurrentUser();
        updateUI(user);
    }
    private void updateUI(FirebaseUser user) {
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(MainActivity.this, Hub.class);
            startActivity(intent);
            finish();
        }

    }


    private void iniciarGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
                            Intent intent=new Intent(MainActivity.this,Perfil.class);
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void Acceder(){
        correo=email.getText().toString().trim();
        pwd =contrasenia.getText().toString().trim();
        if(!correo.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(correo).matches() && !pwd.isEmpty()){
            Auth.signInWithEmailAndPassword(correo, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent= new Intent(MainActivity.this,Hub.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(MainActivity.this, "Ha introducido mal las credenciales o no esta registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else Toast.makeText(this, "Rellene los campos pertinentes para acceder", Toast.LENGTH_SHORT).show();

    }


}
