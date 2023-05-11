package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth Auth;
    private Button tengocredenciales;
    private TextInputLayout emailTIL,pwdTIL;
    private TextInputEditText email,pwd;
    private SignInButton AccesoGoogle;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private String correo, contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Auth = FirebaseAuth.getInstance();

        emailTIL=findViewById(R.id.EmailL);
        email =(TextInputEditText)emailTIL.getEditText();

        pwdTIL=findViewById(R.id.PasswordL);
        pwd=(TextInputEditText)pwdTIL.getEditText();

        tengocredenciales =findViewById(R.id.Acceder);
        tengocredenciales.setOnClickListener(this);

        AccesoGoogle=findViewById(R.id.AccederGoogle);
        AccesoGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Acceder:
                Acceder();
                break;

            case R.id.AccederGoogle:
                iniciarGoogle();
                signIn();
                break;

        }
    }

    private void Acceder(){
        correo=email.getText().toString().trim();
        contrasenia =pwd.getText().toString().trim();
        if(!correo.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(correo).matches() && !contrasenia.isEmpty()){
            Auth.signInWithEmailAndPassword(correo, contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent= new Intent(Login.this,Hub.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(Login.this, "Ha introducido mal las credenciales o no esta registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else Toast.makeText(this, "Rellene los campos pertinentes para acceder", Toast.LENGTH_SHORT).show();

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
    protected void onStart() {
        super.onStart();
        FirebaseUser user = Auth.getCurrentUser();
        updateUI(user);
    }
    private void updateUI(FirebaseUser user) {
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(Login.this, Hub.class);
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