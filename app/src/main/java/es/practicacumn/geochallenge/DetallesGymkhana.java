package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetallesGymkhana extends AppCompatActivity implements View.OnClickListener {
 private TextView Nombre,Informacion,Inicio,Fin,NumeroPruebas,NumeroParticipantes,Dificultad,Plazas;
 private Button participar;
 private DatabaseReference mDatabase,GymkhanaRef;
 private FirebaseAuth mAuth;
 private String UserId,GymkhanaID;
 private int PlazasDisponibles,participantesMaximos,participantesActuales;
 private List<String> usuarioList;
 private Gymkhana obj;
 private List<Gymkhana> gymkhanasList;
 private boolean puede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_gymkhana);
        usuarioList=new ArrayList<>();
        gymkhanasList=new ArrayList<>();
        obj=(Gymkhana) getIntent().getExtras().getSerializable("objeto");
        GymkhanaID=obj.getId();
        participantesMaximos= obj.getMaxParticipantes();
        Incializar();

        Nombre.setText("Nombre: "+obj.getNombre());
        Informacion.setText(obj.getDescripcion());
        Inicio.setText("Inicio: "+obj.getDiaInicio()+", "+obj.getHoraInicio());
        Fin.setText("Fin: "+obj.getDiaFin()+", "+obj.getHoraFin());
        NumeroPruebas.setText("Estará compuesta por "+obj.getPruebas().size()+" pruebas");
        NumeroParticipantes.setText("Estará compuesta por "+participantesMaximos+" participantes");
        Dificultad.setText("Dificultad: "+obj.getDificultad());

        GymkhanaRef=FirebaseDatabase.getInstance().getReference();
        PlazasDisponibles(obj);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        UserId=mAuth.getUid();
        Disponibilidad();


    }

    private void Disponibilidad() {
        DatabaseReference gymkhanaReference=FirebaseDatabase.getInstance().getReference();
        Query query=gymkhanaReference.child("Gymkhana").orderByChild("participantes/"+UserId).equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymkhanasList.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Gymkhana gymkhana = dataSnapshot.getValue(Gymkhana.class);
                        gymkhanasList.add(gymkhana);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void PlazasDisponibles(Gymkhana obj) {
        GymkhanaRef.child("Gymkhana").child(GymkhanaID).child("participantes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String claveParticipante = snapshot.getKey();
                        usuarioList.add(claveParticipante);
                    }
                }
                if(usuarioList.isEmpty()){
                    Plazas.setText("Plazas Disponibles "+ obj.getMaxParticipantes());
                }else if(usuarioList.size()>0){
                    participantesMaximos= obj.getMaxParticipantes();
                    participantesActuales=usuarioList.size();
                   int plazasDisponibles=participantesMaximos-participantesActuales;
                    Plazas.setText("Plazas Disponibles: "+plazasDisponibles);
                    variableGlobal(plazasDisponibles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetallesGymkhana.this, "Error al descargar los datos", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void variableGlobal(int plazasDisponibles) {
        PlazasDisponibles=plazasDisponibles;
    }

    private void Incializar() {
        Nombre=findViewById(R.id.TituloGymkhana);
        Informacion=findViewById(R.id.DescripcionGymkhana);
        Inicio=findViewById(R.id.FechaInicioGymkhana);
        Fin=findViewById(R.id.FechaFinGymkhana);
        NumeroPruebas=findViewById(R.id.NumeroPruebas);
        NumeroParticipantes=findViewById(R.id.NumeroParticipantes);
        Dificultad=findViewById(R.id.DificultadGymkhana);
        Plazas=findViewById(R.id.PlazasDisponibles);
        participar=findViewById(R.id.Participar);
        participar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Participar:
                PuedeParticipar();
                break;
        }
    }

    private void PuedeParticipar(){
        if(gymkhanasList.size()>0){
            int i=0;
            puede=true;
            while (i<gymkhanasList.size() && puede) {
                Gymkhana gymkhana = gymkhanasList.get(i);
                if (!TienesTiempo(gymkhana.getDiaInicio(),gymkhana.getDiaFin(),gymkhana.getHoraInicio(),gymkhana.getHoraFin(),obj.getDiaInicio(),obj.getHoraInicio(),obj.getDiaFin(),obj.getHoraFin())){
                    puede = false;
                }
                i++;
            }

            if(puede){
                Participar();
            }else{
                Toast.makeText(DetallesGymkhana.this, "No puede participar ya que te coincide con otra", Toast.LENGTH_SHORT).show();
            }

        }else{
            Participar();
        }
    }

    private boolean TienesTiempo(String diaInicioGymkhanas, String diaFinGymkhanas, String horaInicioGymkhanas, String horaFinGymkhanas,
                                 String diaInicio, String horaInicio, String diaFin, String horaFin) {

        DateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatHora = new SimpleDateFormat("HH:mm");

        try {

            //Fechas y horas de cualquier gymkhana
            Date FechaInicioGs= formatFecha.parse(diaInicioGymkhanas);
            Date FechaFinGs   = formatFecha.parse(diaFinGymkhanas);
            Date HoraInicioGs = formatHora.parse(horaInicioGymkhanas);
            Date HoraFinGs    = formatHora.parse(horaFinGymkhanas);

            //Fechas y horas de esta gymkhana
            Date FechaInicio = formatFecha.parse(diaInicio);
            Date HoraInicio = formatHora.parse(horaInicio);
            Date FechaFin = formatFecha.parse(diaFin);
            Date HoraFin = formatHora.parse(horaFin);

            Calendar cal = Calendar.getInstance();
            cal.setTime(HoraInicio);
            cal.add(Calendar.MINUTE, 30);
            HoraInicio = cal.getTime();

            Calendar calfin = Calendar.getInstance();
            calfin.setTime(HoraFin);
            calfin.add(Calendar.MINUTE, 30);
            HoraFin = calfin.getTime();


            if(FechaInicioGs.equals(FechaInicio) && FechaFinGs.equals(FechaFin)){
                if(HoraInicioGs.after(HoraFin)||HoraFinGs.before(HoraInicio)){
                    return true;
                }
            } else if (FechaInicioGs.after(FechaFin)||FechaFinGs.before(FechaInicio)) {
                return true;
            }

            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }


    private void Participar() {
        if(usuarioList.isEmpty()){
            apuntarseGymkhana();
        } else if(participantesMaximos>=(usuarioList.size()+1)){
            apuntarseGymkhana();
        }else
            Toast.makeText(this, "No hay plazas disponibles", Toast.LENGTH_SHORT).show();
    }

    private void apuntarseGymkhana() {
        GymkhanaRef = FirebaseDatabase.getInstance().getReference("Gymkhana/" + GymkhanaID);
        GymkhanaRef.child("participantes").child(UserId).setValue(true);
        Intent intent = new Intent(this, Hub.class);
        startActivity(intent);
        finish();
    }
}