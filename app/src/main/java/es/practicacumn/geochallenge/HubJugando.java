package es.practicacumn.geochallenge;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.practicacumn.geochallenge.Service.GymkhanaService;

public class HubJugando extends AppCompatActivity implements View.OnClickListener {
    private Button Vital,Mapa,Brujula,LeerQR;
    private TextView InformacionActual,id,orden;
    private int request_code =100;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_jugando);
        verificarPermisos();
        Brujula=findViewById(R.id.brujula);
        Brujula.setOnClickListener(this);
        Vital=findViewById(R.id.vital);
        Vital.setOnClickListener(this);
        Mapa=findViewById(R.id.mapa);
        Mapa.setOnClickListener(this);
        LeerQR=findViewById(R.id.leerQR);
        LeerQR.setOnClickListener(this);
        orden=findViewById(R.id.ordenh);
        id=findViewById(R.id.idh);
        InformacionActual=findViewById(R.id.pista);
        detenerServicio();
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
            case R.id.brujula:
                Intent intent=new Intent(this, Brujula.class);
                startActivity(intent);
                break;
            case R.id.mapa:
                Intent intent1=new Intent(this, Mapa.class);
                startActivity(intent1);
                break;
            case R.id.vital:
                Elige();
                break;
            case R.id.leerQR:
                LeerQR();
                break;
                
        }
    }

    private void LeerQR() {
        IntentIntegrator integrator=new IntentIntegrator(HubJugando.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Lector");
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
                String ordenPrueba = datos.split("Orden de la prueba: ")[1].split(" ")[0];

                // Extraer el ID de la Gymkhana
                String idGymkhana = datos.split("Id_Gymkhana: ")[1].split(" ")[0];

                // Extraer la información
                String informacion = datos.split("Informacion: ")[1];

                orden.setText(ordenPrueba);
                id.setText(idGymkhana);
                InformacionActual.setText(informacion);
            }else{
                Toast.makeText(this, "El código QR no tiene la estructura válida", Toast.LENGTH_SHORT).show();
            }

            
        }


    private void Elige() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("Tipos de consejos");
        alerta.setMessage("¿Con cual desea interactuar?")
                .setCancelable(false)
                .setPositiveButton("Consejos Supervivencia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),ConsejosS.class);
                        intent.putExtra("Voy",1);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Primeros Auxilios", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(),ConsejosP.class);
                        intent.putExtra("Voy",1);
                        startActivity(intent);
                    }
                });
        alerta.create().show();
    }
}