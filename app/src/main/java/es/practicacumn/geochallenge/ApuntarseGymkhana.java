package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


import es.practicacumn.geochallenge.Adaptadores.AdaptadorGymkhana;
import es.practicacumn.geochallenge.Model.Comun;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;

public class ApuntarseGymkhana extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference ref;
    private ListView listView;
    private List<Gymkhana> gymkhanaList;
    private TextView Nohay;
    private ProgressBar progressBar;
    private FloatingActionButton filtro;
    private TextInputLayout nombreGKTIL,inicioFechaTIL, lugarTIL;
    private TextInputEditText Gnombre,GinicioFecha, Glugar;
    private TextView GnivelDificultad;
    private RatingBar Gdificultad;
    private Button busqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuntarse_gymkhana);
        gymkhanaList=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        listView=findViewById(R.id.mostraGymkhana);
        Nohay=findViewById(R.id.Sininfo);
        filtro=findViewById(R.id.filtrarGymkhanas);
        filtro.setOnClickListener(this);



        ref = FirebaseDatabase.getInstance().getReference().child("Gymkhana");

        ValueEventListener gymkhanaListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymkhanaList.clear();
                if (snapshot.exists()){
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Gymkhana gymkhana=dataSnapshot.getValue(Gymkhana.class);
                    actualizarValores(gymkhana.getDiaInicio(),gymkhana.getHoraInicio(),gymkhana.getId());
                    if(!gymkhana.getEstado().equals("Apunto de empezar")){
                        gymkhanaList.add(gymkhana);
                    }

                }
                if(gymkhanaList.size()>0){
                    listView.setVisibility(View.VISIBLE);
                    AdaptadorGymkhana mi=new AdaptadorGymkhana(getApplicationContext(),gymkhanaList);
                    listView.setAdapter(mi);
                    progressBar.setVisibility(View.INVISIBLE);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Gymkhana obj=(Gymkhana) adapterView.getItemAtPosition(i);
                            Intent paso = new Intent(getApplicationContext(), DetallesGymkhana.class);
                            paso.putExtra("objeto",(Serializable) obj);
                            startActivity(paso);
                        }
                    });
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Nohay.setVisibility(View.VISIBLE);
                }

            }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Nohay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(gymkhanaListener);
    }

    private void actualizarValores(String diaInicio, String horaInicio, String id) {
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date dateTime=null;
        try {
            dateTime = dateFormat.parse(diaInicio + " " + horaInicio);
        }catch (ParseException e){
            e.printStackTrace();
            return;
        }
        Calendar currentCalendar = Calendar.getInstance();
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTime(dateTime);
        // Agregar 30 minutos a la hora de inicio
        selectedCalendar.add(Calendar.MINUTE,30);
        if (selectedCalendar.before(currentCalendar)){
            DatabaseReference GymkhanaRef=FirebaseDatabase.getInstance().getReference("Gymkhana/" + id);
            GymkhanaRef.child("estado").setValue("Apunto de empezar");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.filtrarGymkhanas:
                FiltrarDatos();
                break;
        }
    }

    private void FiltrarDatos() {
        AlertDialog.Builder alerta =new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.buscadorgymkhana,null);
        alerta.setView(view);
        alerta.setCancelable(true);
        final AlertDialog dialog=alerta.create();
        dialog.show();

        nombreGKTIL = view.findViewById(R.id.GKNombre);
        Gnombre = (TextInputEditText) nombreGKTIL.getEditText();

        lugarTIL =view.findViewById(R.id.GkLugar);
        Glugar =(TextInputEditText) lugarTIL.getEditText();

        inicioFechaTIL=view.findViewById(R.id.FechaInicioGk);
        GinicioFecha=(TextInputEditText)inicioFechaTIL.getEditText();
        Comun.InicializarFecha(GinicioFecha,ApuntarseGymkhana.this);
;

        GnivelDificultad=view.findViewById(R.id.GKNivelDificultad);
        NivelDificultad(view);

        busqueda=view.findViewById(R.id.BuscarGK);
        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Nohay.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                RealizarBusqueda(Gnombre,Glugar,GinicioFecha,GnivelDificultad);
                dialog.dismiss();
            }
        });

    }

    private void RealizarBusqueda(TextInputEditText gnombre, TextInputEditText glugar, TextInputEditText ginicioFecha, TextView gnivelDificultad) {
        List<Gymkhana> resultado=new ArrayList<>();
        String nombre=(gnombre.getText().toString().trim()).toLowerCase();
        String lugar=glugar.getText().toString().trim().toLowerCase();
        String fecha=ginicioFecha.getText().toString().trim();
        String nivel=gnivelDificultad.getText().toString().trim();
        if(nivel.equals("Sin especificar")){
            nivel="";
        }

        if(gymkhanaList.size()>0) {
            for (Gymkhana gymkhana : gymkhanaList) {
                if ((nombre.isEmpty() || gymkhana.getNombre().toLowerCase().contains(nombre)) &&
                        (lugar.isEmpty() || gymkhana.getLugar().toLowerCase().contains(lugar))&&
                        (fecha.isEmpty()||gymkhana.getDiaInicio().equals(fecha))&&
                        (nivel.isEmpty()||gymkhana.getDificultad().equals(nivel))) {

                    resultado.add(gymkhana);
                }

                if(resultado.size()>0){
                    AdaptadorGymkhana consulta = new AdaptadorGymkhana(getApplicationContext(), resultado);
                    listView.setAdapter(consulta);
                    progressBar.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    Nohay.setVisibility(View.INVISIBLE);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Nohay.setVisibility(View.VISIBLE);
                }

            }

        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Nohay.setVisibility(View.VISIBLE);
        }

    }

    private void NivelDificultad(View view) {
        Gdificultad=view.findViewById(R.id.GKDificultad);
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

}