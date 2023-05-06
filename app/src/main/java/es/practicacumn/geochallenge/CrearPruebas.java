package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import es.practicacumn.geochallenge.Fragmentos.Frag_Mapa;
import es.practicacumn.geochallenge.Fragmentos.Frag_Pruebas;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.UbicacionGymkhana;

public class CrearPruebas extends AppCompatActivity implements View.OnClickListener,Frag_Mapa.OnMapClickListener {
    private String Nombre,Lugar,Dificultad,ParticipantesMax,FechaInicio,FechaFin,HoraInicio,HoraFin,Id,Porden,PLat,PLon,Pinfo,Descripcion,UserId;
    private double lat,lon;
    private int orden;
    private List<Prueba> ListaPruebas;
    private UbicacionGymkhana ubicacionGymkhana;
    private EditText EOrden,ELat,ELon,EInfo;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseUser user;
    private Button enviar,continuar;
    private ProgressBar progressBar;
    private TextView informacion;
    private RelativeLayout carga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pruebas);
        user= FirebaseAuth.getInstance().getCurrentUser();
        UserId=user.getUid();
        recibirDatos();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        ListaPruebas= new ArrayList<>();
        EOrden=findViewById(R.id.Orden);
        ELat=findViewById(R.id.Latitud);
        LanzarMapa(ELat);
        ELon=findViewById(R.id.Longitud);
        LanzarMapa(ELon);
        EInfo=findViewById(R.id.Descripccion);
        enviar = findViewById(R.id.IntroduccirDatos);
        enviar.setOnClickListener(this);
        continuar = findViewById(R.id.Terminar);
        continuar.setOnClickListener(this);
        carga=findViewById(R.id.CargarPruebas);
        informacion=findViewById(R.id.textoplano);
        progressBar=findViewById(R.id.HacerPrueba);
        generarId();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IntroduccirDatos:
                introducirDatos();
                break;
            case R.id.Terminar:
                if(ListaPruebas.size()+1<=2){
                    Toast.makeText(this, "Debe introduccir al menos 2 pistas", Toast.LENGTH_SHORT).show();
                }else{
                    almacenarDatos();
                }
                break;
        }
    }
    private void almacenarDatos() {

        Gymkhana gymkhana= new Gymkhana(Id,Nombre,Lugar,Dificultad,Descripcion,FechaInicio,FechaFin,HoraInicio,HoraFin,Integer.parseInt(ParticipantesMax),ListaPruebas,false,ubicacionGymkhana,null,UserId);

        cambiarActividad(gymkhana);

    }

    private void generarId() {
        UUID uuid = UUID.randomUUID();
        Id = uuid.toString().replaceAll("-", "");
    }

    private void cambiarActividad(Gymkhana gymkana) {
        Intent intent= new Intent(getApplicationContext(),MostraGymkhana.class);
        intent.putExtra("gymkana",gymkana);
        startActivity(intent);

    }

    private void recibirDatos() {
        Bundle extras=getIntent().getExtras();
        if(!extras.isEmpty()) {
            Nombre = extras.getString("NombreGY");
            Lugar = extras.getString("Lugar");
            Dificultad = extras.getString("DificultadGY");
            ParticipantesMax = extras.getString("NParticipantes");
            FechaInicio = extras.getString("InicioFGY");
            HoraInicio = extras.getString("InicioHGY");
            FechaFin= extras.getString("FinFGY");
            HoraFin=extras.getString("FinHGY");
            ubicacionGymkhana= (UbicacionGymkhana) extras.getSerializable("LugarPrueba");
            Descripcion=extras.getString("Descripcion");




        }
    }

    private void introducirDatos() {
        enviar.setEnabled(false);
        carga.setVisibility(View.VISIBLE);
        informacion.setText("VALIDANDO DATOS.....");
        progressBar.setProgress(2);
        Fragment fragment= getSupportFragmentManager().findFragmentById(R.id.container);;

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.container)).commit();
        }

        Porden = EOrden.getText().toString().trim();
        PLat = ELat.getText().toString().trim();
        PLon = ELon.getText().toString().trim();
        Pinfo = EInfo.getText().toString().trim();
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        if(!Porden.isEmpty()){
            if(esNumerico(Porden)){
                orden = Integer.parseInt(Porden);
                if(orden ==(ListaPruebas.size()+1)){
                    if(!PLat.isEmpty()){
                        lat = Double.parseDouble(PLat);
                        if(lat <=90&& lat >=-90){
                            if(!PLon.isEmpty()){
                                lon = Double.parseDouble(PLon);
                                if(lon <=180&& lon >=-180){
                                    if(!Pinfo.isEmpty()){
                                        informacion.setText("DATOS VALIDOS.....");
                                        progressBar.setProgress(25);
                                        generarCodigoQR(barcodeEncoder);
                                    }else{
                                        Toast.makeText(this, "La informacion de la pista no puede ser vacía", Toast.LENGTH_SHORT).show();
                                        Normalidad();
                                    }
                                }else{
                                    Toast.makeText(this, "La longitud debe estar entre -180º y 180º", Toast.LENGTH_SHORT).show();
                                    Normalidad();
                                }
                            }else{
                                Toast.makeText(this, "La longitud no puede ser nula", Toast.LENGTH_SHORT).show();
                                Normalidad();
                            }
                        }else{
                            Toast.makeText(this, "La latitud debe estar entre -90º y 90º", Toast.LENGTH_SHORT).show();
                            Normalidad();
                        }
                    }else{
                        Toast.makeText(this, "La latitud no puede ser nula", Toast.LENGTH_SHORT).show();
                        Normalidad();
                    }
                }else{
                    Toast.makeText(this, "El numero de la pista ha de ser "+(ListaPruebas.size()+1), Toast.LENGTH_SHORT).show();
                    Normalidad();
                }
            }else{
                Toast.makeText(this, "Se debe introducir un numero", Toast.LENGTH_SHORT).show();
                Normalidad();
            }
        }else{
            Toast.makeText(this, "Introduzca el numero de la pista", Toast.LENGTH_SHORT).show();
            Normalidad();
        }

    }

    private void Normalidad() {
        carga.setVisibility(View.INVISIBLE);
        enviar.setEnabled(true);
    }

    private void generarCodigoQR(BarcodeEncoder barcodeEncoder) {
        informacion.setText("GENERANDO CODIGO QR.....");
        try {

            Bitmap bitmap = barcodeEncoder.encodeBitmap("Orden de la prueba: " + orden + " Id_Gymkhana: " + Id + " Informacion: " + Pinfo, BarcodeFormat.QR_CODE, 750, 750);
            String nombre="Prueba"+orden;
            informacion.setText("CODIGO QR GENERADO.....");
            progressBar.setProgress(50);
            subirCodigoQR(bitmap,nombre);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void subirCodigoQR(Bitmap bitmap, String nombreArchivo) {
        informacion.setText("SUBIENDO ARCHIVO.....");

        //El id es el de la gymkhana
        storageRef = FirebaseStorage.getInstance().getReference().child("codigosQR").child(Id);

        // Convertir el código QR a un array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        // Subir el código QR a Storage con un nombre de archivo único
        StorageReference codigoQRRef = storageRef.child(nombreArchivo);
        codigoQRRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    // El código QR se subió exitosamente
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        informacion.setText("QR SUBIDO CON EXITO.....");
                        progressBar.setProgress(75);
                        codigoQRRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Se obtuvo la URL de descarga del código QR
                                String codigoUrl = uri.toString();
                                ListaPruebas.add(new Prueba(orden, lat, lon, Pinfo,codigoUrl));
                                EOrden.getText().clear();
                                ELat.getText().clear();
                                ELon.getText().clear();
                                EInfo.getText().clear();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("pruebas", (Serializable) ListaPruebas);
                                Frag_Pruebas fPruebas = new Frag_Pruebas();
                                fPruebas.setArguments(bundle);
                                informacion.setText("MOSTRANDO PISTA.....");
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fPruebas);
                                fragmentTransaction.commit();

                                progressBar.setProgress(100);
                                carga.setVisibility(View.INVISIBLE);
                                enviar.setEnabled(true);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(CrearPruebas.this, "Error al subir el codigo", Toast.LENGTH_SHORT).show();                    }
                });
    }


    private boolean esNumerico(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            CrearAlerta();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void CrearAlerta() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("¿Desea volver atras?");
        alerta.setMessage("Perderá el progreso")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ruta= "/codigosQR/"+Id;
                        EliminarStorage(ruta);
                        Intent intent=new Intent(getApplicationContext(),CrearGymkhana.class);
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
    private void EliminarStorage(String ruta) {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference carpeta =storage.getReference().child(ruta);
        carpeta.listAll()
                .addOnSuccessListener(listResult ->{
                    for (StorageReference item: listResult.getItems()){
                        item.delete();
                    }
                    for (StorageReference subcarpeta: listResult.getPrefixes()){
                        EliminarStorage(subcarpeta.getPath());
                    }
                    carpeta.delete();
                });
    }

    private void LanzarMapa(EditText Ubicacion) {
        Ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frag_Mapa fragMapa=new Frag_Mapa();
                Bundle bundle = new Bundle();
                bundle.putSerializable("pruebas", (Serializable) ListaPruebas);
                bundle.putSerializable("Origen",(Serializable) ubicacionGymkhana);
                fragMapa.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragMapa).commit();
            }
        });
    }


    @Override
    public void onMapClick(double Lat, double Lon) {
        ELat.setText(String.valueOf(Lat));
        ELon.setText(String.valueOf(Lon));
    }
}