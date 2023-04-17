package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.practicacumn.geochallenge.Model.Comun;

public class CrearGymkhana extends AppCompatActivity implements View.OnClickListener {
    private EditText Gnombre,Glugar,GinicioFecha,GinicioHora,GfinFecha,GfinHora,GNparticipantes;
    private String GKnombre,GKlugar,GKinicioFecha,GKinicioHora,GKfinFecha, GKfinHora,GKdificultad,GKNcomponentes;
    private TextView GnivelDificultad;
    private RatingBar Gdificultad;
    private Button CrearPruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_gymkhana);
        Gnombre=findViewById(R.id.NombreGK);
        Glugar=findViewById(R.id.LugarGk);
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
    private void Inicializar() {
        GKnombre=Gnombre.getText().toString().trim();
        GKlugar=Glugar.getText().toString().trim();
        GKinicioFecha=GinicioFecha.getText().toString().trim();
        GKfinFecha=GfinFecha.getText().toString().trim();
        GKinicioHora=GinicioHora.getText().toString().trim();
        GKfinHora =GfinHora.getText().toString().trim();
        GKdificultad=GnivelDificultad.getText().toString();
        GKNcomponentes=GNparticipantes.getText().toString().trim();
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
            if (!GKlugar.isEmpty()){
                if(!GKdificultad.isEmpty()){
                    if(!GKNcomponentes.isEmpty()){
                        if(!GKinicioHora.isEmpty()&&!GKinicioFecha.isEmpty()&&!GKfinHora.isEmpty()&&!GKfinFecha.isEmpty()){
                            if(fechasPasadas(GKinicioFecha,GKinicioHora)){
                                if(fechasPasadas(GKfinFecha, GKfinHora)){
                                   EnviarDatos();
                                }else Toast.makeText(this, "No pude poner la fecha y hora de final en el pasado", Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(this, "No pude poner la fecha y hora de inicio en el pasado", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(this, "Los campos de inicio y fin no pueden estar vacios", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(this, "Debe indicar el numero maximo de participantes", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, "Debe especificar la dificultad", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Debe especificar en lugar de comienzo", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this, "Debe indicar el nombre de la gymkhana", Toast.LENGTH_SHORT).show();
    }

    private void EnviarDatos() {
        Bundle extras = new Bundle();
        extras.putString("NombreGY",GKnombre);
        extras.putString("LugarGY",GKlugar);
        extras.putString("DificultadGY",GKdificultad);
        extras.putString("NParticipantes",GKNcomponentes);
        extras.putString("InicioFGY",GKinicioFecha);
        extras.putString("InicioHGY",GKinicioHora);
        extras.putString("FinFGY",GKfinFecha);
        extras.putString("FinHGY",GKfinHora);




        Intent intent=new Intent(getApplicationContext(),CrearPruebas.class);
        intent.putExtras(extras);
        startActivity(intent);
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
}