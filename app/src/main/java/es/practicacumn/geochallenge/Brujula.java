package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

public class Brujula extends AppCompatActivity {

    private ImageView imageView;
    private float [] mGravity=new float[3];
    private float [] mGeomagnetic=new float[3];
    private float azimuth=0f;
    private float currectAzimuth=0f;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brujula);
    }
}