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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.practicacumn.geochallenge.Model.Comun;

public class CrearGymkhana extends AppCompatActivity implements View.OnClickListener {
    private EditText Gnombre,GinicioFecha,GinicioHora,GfinFecha,GfinHora,GNparticipantes,GDescripcion;
    private String GKnombre,GKinicioFecha,GKinicioHora,GKfinFecha, GKfinHora,GKdificultad,GKNcomponentes,Descripcion,UserId;
    private TextView GnivelDificultad;
    private RatingBar Gdificultad;
    private Button CrearPruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_gymkhana);
        PermisoLocalizacion();
        recibirDatos();
        Gnombre=findViewById(R.id.NombreGK);
        GDescripcion=findViewById(R.id.DescripcionGK);
        GinicioFecha=findViewById(R.id.FechaInicio);
        Comun.InicializarFecha(GinicioFecha,CrearGymkhana.this);
        GfinFecha=findViewById(R.id.FechaFin);
        Comun.InicializarFecha(GfinFecha,CrearGymkhana.this);
        GinicioHora=findViewById(R.id.HoraInicio);
        InicializarHora(GinicioHora);
        GfinHora=findViewById(R.id.HoraFin);
        InicializarHora(GfinHora);
        GnivelDificultad=findViewById(R.id.NivelDificultad);
        NivelDificultad();
        GNparticipantes=findViewById(R.id.ParticipantesMax);
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

    private void recibirDatos() {
        if(isTaskRoot()){
            Bundle entrada = getIntent().getExtras();
            if (!entrada.isEmpty()) {
                UserId = entrada.getString("IdUsuario");
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
        Descripcion=GDescripcion.getText().toString().trim();
    }

    private void NivelDificultad() {
        Gdificultad=findViewById(R.id.EDificultad);
        Gdificultad.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                GnivelDificultad.setText(String.valueOf(v));
                switch ((int) Gdificultad.getRating()){
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
        boolean resultado = false;

        try {
            Calendar hoy = Calendar.getInstance();
            Date fechaActualSolo = hoy.getTime();
            Date horaActualSolo = new Date(hoy.get(Calendar.HOUR_OF_DAY), hoy.get(Calendar.MINUTE), 0);

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date fechaHora = formato.parse(fecha + " " + hora);


            // Comparar si la fecha es la misma que la de hoy
            if (fechaHora.compareTo(fechaActualSolo) == 0) {
                // Obtener la hora actual más 1 hora
                hoy.add(Calendar.HOUR_OF_DAY, 1);
                Date horaActualMas1 = new Date(hoy.get(Calendar.HOUR_OF_DAY), hoy.get(Calendar.MINUTE), 0);

                // Comparar si la hora actual más 1 hora es menor o igual a la hora pasada como parámetro
                if (horaActualMas1.compareTo(horaActualSolo) <= 0) {
                    resultado = true;
                }
            }else{
                resultado=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultado;
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