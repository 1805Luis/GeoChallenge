package es.practicacumn.geochallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.practicacumn.geochallenge.Model.Comun;

public class CrearGymkhana extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout nombreGKTIL,participantesTIL,inicioFechaTIL,inicioHoraTIL,finFechaTIL,finHoraTIL,descripcionTIL;
    private TextInputEditText Gnombre,GNparticipantes,GinicioFecha,GinicioHora,GfinFecha,GfinHora, Gdescripcion;
    private String GKnombre,GKinicioFecha,GKinicioHora,GKfinFecha, GKfinHora,GKdificultad,GKNcomponentes,Descripcion;
    private TextView GnivelDificultad;
    private RatingBar Gdificultad;
    private Button CrearPruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_gymkhana);
        PermisoLocalizacion();


        nombreGKTIL = findViewById(R.id.NombreGK);
        Gnombre = (TextInputEditText) nombreGKTIL.getEditText();

        descripcionTIL=findViewById(R.id.DescripcionGK);
        Gdescripcion =(TextInputEditText) descripcionTIL.getEditText();

        inicioFechaTIL=findViewById(R.id.FechaInicio);
        GinicioFecha=(TextInputEditText)inicioFechaTIL.getEditText();
        Comun.InicializarFecha(GinicioFecha,CrearGymkhana.this);

        inicioHoraTIL=findViewById(R.id.HoraInicio);
        GinicioHora=(TextInputEditText)inicioHoraTIL.getEditText();
        InicializarHora(GinicioHora);

        finFechaTIL=findViewById(R.id.FechaFin);
        GfinFecha=(TextInputEditText)finFechaTIL.getEditText();
        Comun.InicializarFecha(GfinFecha,CrearGymkhana.this);

        finHoraTIL=findViewById(R.id.HoraFin);
        GfinHora=(TextInputEditText) finHoraTIL.getEditText();
        InicializarHora(GfinHora);

        participantesTIL=findViewById(R.id.ParticipantesMax);
        GNparticipantes=(TextInputEditText) participantesTIL.getEditText();

        GnivelDificultad=findViewById(R.id.NivelDificultad);
        NivelDificultad();


        CrearPruebas=findViewById(R.id.Crear);
        CrearPruebas.setOnClickListener(this);

    }

    private void PermisoLocalizacion() {
        int permiso= ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }

    }



    private void Inicializar() {

        GKnombre=Gnombre.getText().toString().trim();
        GKinicioFecha=GinicioFecha.getText().toString().trim();
        GKfinFecha=GfinFecha.getText().toString().trim();
        GKinicioHora=GinicioHora.getText().toString().trim();
        GKfinHora =GfinHora.getText().toString().trim();
        GKdificultad=GnivelDificultad.getText().toString();
        GKNcomponentes=GNparticipantes.getText().toString().trim();
        Descripcion= Gdescripcion.getText().toString().trim();
    }

    private void NivelDificultad() {
        Gdificultad=findViewById(R.id.EDificultad);
        Gdificultad.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                GnivelDificultad.setText(String.valueOf(v));
                switch ((int) Gdificultad.getRating()){
                    case 0:
                        GnivelDificultad.setText("Sin especificar");
                        break;
                    case 1:
                        GnivelDificultad.setText("Facil");
                        break;
                    case 2:
                        GnivelDificultad.setText("Moderado");
                        break;
                    case 3:
                        GnivelDificultad.setText("Intermedio");
                        break;
                    case 4:
                        GnivelDificultad.setText("Dificil");
                        break;
                    case 5:
                        GnivelDificultad.setText("Extremo");
                        break;
                }

            }
        });
    }

    private void InicializarHora(EditText Mihora) {
        Mihora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime=Calendar.getInstance();
                int hora=currentTime.get(Calendar.HOUR_OF_DAY);
                int minute=currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog=new TimePickerDialog(CrearGymkhana.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHora , int selectedMinutos) {
                       Mihora.setText(String.format("%d:%02d",selectedHora,selectedMinutos));
                    }
                },hora,minute,true);
                timePickerDialog.setTitle("Seleccione una hora");
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Crear:
                Continuar();
                break;
        }
    }




    private void Continuar() {
        Inicializar();
        if(!GKnombre.isEmpty()){
            if(!Descripcion.isEmpty()) {
                if (!GKdificultad.isEmpty()) {
                    if (!GKNcomponentes.isEmpty()) {
                        if(Integer.parseInt(GKNcomponentes)>=4){
                            if (!GKinicioHora.isEmpty() && !GKinicioFecha.isEmpty() && !GKfinHora.isEmpty() && !GKfinFecha.isEmpty()) {
                                if (fechasPasadas(GKinicioFecha, GKinicioHora)) {
                                    if (fechasPasadas(GKfinFecha, GKfinHora)) {
                                        if (esPosterior(GKinicioFecha, GKfinFecha, GKinicioHora, GKfinHora)) {
                                            if(validarHora(GKinicioFecha,GKinicioHora)){
                                                    EnviarDatos();
                                            }else
                                                Toast.makeText(this, "Debes dejar una hora de margen a la hora de crear la gymkhana", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(this, "Las fechas no son posteriores", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(this, "No pude poner la fecha y hora de final en el pasado", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(this, "No pude poner la fecha y hora de inicio en el pasado", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Los campos de inicio y fin no pueden estar vacios", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(this, "El mínimo son 4 participantes ", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(this, "Debe indicar el numero maximo de participantes", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Debe especificar la dificultad", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "Debe incluir una breve descripcion de la gymkhana", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Debe indicar el nombre de la gymkhana", Toast.LENGTH_SHORT).show();
    }




    private boolean validarHora(String fecha, String hora) {
        Calendar calendarHoy = Calendar.getInstance();
        Calendar calendarComparar = Calendar.getInstance();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date fechaHoraHoy = calendarHoy.getTime();
            Date fechaHoraComparar = dateFormat.parse(fecha+" "+hora);

            calendarHoy.setTime(fechaHoraHoy);
            calendarComparar.setTime(fechaHoraComparar);

            // Sumar una hora a la hora actual
            calendarHoy.add(Calendar.HOUR_OF_DAY, 1);

            if(calendarHoy.get(Calendar.YEAR) <= calendarComparar.get(Calendar.YEAR) &&
                    calendarHoy.get(Calendar.MONTH) <= calendarComparar.get(Calendar.MONTH) &&
                    calendarHoy.get(Calendar.DAY_OF_MONTH) <= calendarComparar.get(Calendar.DAY_OF_MONTH) &&
                    calendarHoy.get(Calendar.HOUR_OF_DAY) <= calendarComparar.get(Calendar.HOUR_OF_DAY) &&
                    calendarHoy.get(Calendar.MINUTE) <= calendarComparar.get(Calendar.MINUTE)){
                return true;
            }else{
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }
    private boolean esPosterior(String fechaInicio, String fechaFinal, String horaInicio, String horaFinal) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            // Convertir las cadenas de texto de las fechas y horas a objetos Date
            Date inicio = formato.parse(fechaInicio + " " + horaInicio);
            Date fin = formato.parse(fechaFinal + " " + horaFinal);

            // Comparar las fechas y horas y devolver true si la fecha y hora final es posterior a la fecha y hora de inicio
            return fin.after(inicio);
        } catch (Exception e) {
            // Si hay algún error en la conversión de las fechas y horas, devolver false
            return false;
        }
    }
    private boolean fechasPasadas(String FechaInicio, String HoraInicio) {
        SimpleDateFormat FormatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar FechaHoraActual = Calendar.getInstance();
        try {
            Date FechaHoraInicio=FormatoFecha.parse(FechaInicio+" "+HoraInicio+ ":00");
            return FechaHoraInicio.compareTo(FechaHoraActual.getTime())>0;
        }catch (Exception e){
            return false;
        }

    }
    private void EnviarDatos() {
        Bundle extras = new Bundle();
        extras.putString("NombreGY",GKnombre);
        extras.putString("DificultadGY",GKdificultad);
        extras.putString("NParticipantes",GKNcomponentes);
        extras.putString("InicioFGY",GKinicioFecha);
        extras.putString("InicioHGY",GKinicioHora);
        extras.putString("FinFGY",GKfinFecha);
        extras.putString("FinHGY",GKfinHora);
        extras.putString("Descripcion",Descripcion);

        Intent intent=new Intent(getApplicationContext(),LugarGymkhana.class);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            Crearalerta();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void Crearalerta() {
        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("¿Desea volver atras?");
        alerta.setMessage("Perderá el progreso")
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