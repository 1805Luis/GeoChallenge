package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;

public class Brujula extends AppCompatActivity implements SensorEventListener {

    private ImageView imageView;
    private float [] mGravity=new float[3];
    private float [] mGeomagnetic=new float[3];
    private float azimuth=0f;
    private float currectAzimuth=0f;
    private SensorManager mSensorManager;
    private String informacion;
    private int ordenPrueba;
    private List<Prueba> listPruebas;
    private boolean ayuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brujula);
        imageView=(ImageView) findViewById(R.id.compass);
        mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        recibirDatos();
    }

    private void recibirDatos() {
        Bundle entrada = getIntent().getExtras();
        if (entrada!=null) {

            informacion = entrada.getString("Descripcion");
            ordenPrueba = entrada.getInt("Orden");
            listPruebas = (List<Prueba>) entrada.getSerializable("Pruebas");
            ayuda=entrada.getBoolean("Ayuda");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha=0.97f;
        synchronized (this){
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                mGravity[0]= alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
                mGravity[1]= alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
                mGravity[2]= alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
            }
            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic[0]= alpha*mGeomagnetic[0] + (1-alpha)*sensorEvent.values[0];
                mGeomagnetic[1]= alpha*mGeomagnetic[1] + (1-alpha)*sensorEvent.values[1];
                mGeomagnetic[2]= alpha*mGeomagnetic[2] + (1-alpha)*sensorEvent.values[2];
            }
            float R[]=new float[9];
            float I[]=new float[9];
            boolean success=SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if(success){

                float orientation[]=new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth=(float) Math.toDegrees(orientation[0]);
                azimuth=(azimuth+360) % 360;

                Animation animation= new RotateAnimation(-currectAzimuth,-azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                currectAzimuth=azimuth;
                animation.setDuration(500);
                animation.setRepeatCount(0);
                animation.setFillAfter(true);

                imageView.startAnimation(animation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            VolverAtras();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void VolverAtras() {
        Intent intent1=new Intent(this,HubJugando.class);
        intent1.putExtra("Descripcion",informacion);
        intent1.putExtra("Pruebas",(Serializable) listPruebas);
        intent1.putExtra("Orden",ordenPrueba);
        intent1.putExtra("Ayuda",ayuda);
        startActivity(intent1);
        finish();
    }
}