package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button tengocredenciales,soyNuevo,olvidePwd;
    private EditText email,contrasenia;
    private String correo,pw;
    private SignInButton AccesoGoogle;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mAuth = FirebaseAuth.getInstance();
        email =findViewById(R.id.Email);
        contrasenia =findViewById(R.id.Password);
        tengocredenciales =findViewById(R.id.Acceder);
        tengocredenciales.setOnClickListener(this);
        soyNuevo=findViewById(R.id.CrearCuenta);
        soyNuevo.setOnClickListener(this);
        olvidePwd=findViewById(R.id.OlvidePwd);
        olvidePwd.setOnClickListener(this);
        AccesoGoogle=findViewById(R.id.AccederGoogle);

    }

    @Override
   public void onClick(View view) {
        switch (view.getId()){
            case R.id.Acceder:
                Toast.makeText(this, "Acceder", Toast.LENGTH_SHORT).show();
                break;
            case R.id.CrearCuenta:
                Toast.makeText(this, "Crear Cuenta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.OlvidePwd:
                Toast.makeText(this, "Olvidé la contraseña", Toast.LENGTH_SHORT).show();
                break;
            case R.id.AccederGoogle:
                Toast.makeText(this, "Acceder con Google", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}