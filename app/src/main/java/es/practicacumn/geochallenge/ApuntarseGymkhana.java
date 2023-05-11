package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;

public class ApuntarseGymkhana extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference ref;
    private ListView listView;
    private List<Gymkhana> gymkhanaList;
    private TextView Nohay;
    private ProgressBar progressBar;
    private EditText buscador;
    private ImageButton buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuntarse_gymkhana);
        gymkhanaList=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        listView=findViewById(R.id.mostraGymkhana);
        Nohay=findViewById(R.id.Sininfo);
        buscador=findViewById(R.id.buscarGymkhana);
        buscar=findViewById(R.id.lupaGK);
        buscar.setOnClickListener(this);

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
            case R.id.lupaGK:
                Toast.makeText(this, "Funciona", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}