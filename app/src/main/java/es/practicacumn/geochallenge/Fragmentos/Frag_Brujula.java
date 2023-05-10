package es.practicacumn.geochallenge.Fragmentos;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import es.practicacumn.geochallenge.R;

public class Frag_Brujula extends Fragment implements SensorEventListener {
    private ImageView imageView;
    private float [] mGravity=new float[3];
    private float [] mGeomagnetic=new float[3];
    private float azimuth=0f;
    private float currectAzimuth=0f;
    private SensorManager mSensorManager;

    public Frag_Brujula() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_frag__brujula, container, false);
        imageView=(ImageView) v.findViewById(R.id.compass);
        mSensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
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
}