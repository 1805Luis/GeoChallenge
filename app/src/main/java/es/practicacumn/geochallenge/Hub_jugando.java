package es.practicacumn.geochallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import es.practicacumn.geochallenge.Service.GymkhanaService;

public class Hub_jugando extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_jugando);
        detenerServicio();
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

}