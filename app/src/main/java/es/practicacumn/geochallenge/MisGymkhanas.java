package es.practicacumn.geochallenge;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import es.practicacumn.geochallenge.Fragmentos.Frag_Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.Service.GenerarPdf;


public class MisGymkhanas extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 100;
    private Gymkhana gymkana;
    private List<Prueba> pruebaList=new ArrayList<>();
    private String Nombre;
    private FloatingActionButton generarpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misgymkhanas);
        Permisos();
        recogerDatos();
        InizializarFragmento();
        generarpdf=findViewById(R.id.generar);
        generarpdf.setOnClickListener(this);


    }



    private void InizializarFragmento() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("gymkana", (Serializable) gymkana);
        Frag_Gymkhana fGymkana = new Frag_Gymkhana();
        fGymkana.setArguments(bundle);
        // Crea una transacción de fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerGymkhanas, fGymkana);
        fragmentTransaction.commit();
    }

    private void recogerDatos() {
        Intent intent=getIntent();
        if(intent.hasExtra("gymkana")) {
            gymkana= (Gymkhana) intent.getSerializableExtra("gymkana");
            for(Prueba prueba: gymkana.getPruebas()){
                pruebaList.add(prueba);
            }
            Nombre=gymkana.getNombre();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.generar:
                Snackbar.make(view,"Descargar Codigos QR",Snackbar.LENGTH_LONG)
                               .setAction("Si", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LanzarServicio();
                                    }
                                })
                        .show();
                break;

        }
    }

    private void LanzarServicio() {
        Intent intent=new Intent(this, GenerarPdf.class);
        intent.putExtra("Pruebas",(Serializable) pruebaList);
        intent.putExtra("Nombre",Nombre);
        startService(intent);
    }

    private void Permisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
    }
}