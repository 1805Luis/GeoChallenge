package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
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
    private Button acceder,registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Auth=FirebaseAuth.getInstance();
        acceder=findViewById(R.id.irLogin);
        acceder.setOnClickListener(this);
        registrarse=findViewById(R.id.soyNuevo);
        registrarse.setOnClickListener(this);

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.irLogin:
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.soyNuevo:
                Intent intent1=new Intent(MainActivity.this,CrearCuenta.class);
                startActivity(intent1);
                finish();
                break;

        }
    }
}
