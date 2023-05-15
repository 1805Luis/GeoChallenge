package es.practicacumn.geochallenge.Fragmentos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.practicacumn.geochallenge.Adaptadores.AdaptadorGymkhana;
import es.practicacumn.geochallenge.GymkhanasApuntadas;
import es.practicacumn.geochallenge.MisGymkhanas;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.R;

public class Frag_GymkhanasApuntadas extends Fragment {
    private ListView listView;
    private List<Gymkhana> gymkhanas;
    private FirebaseUser user;
    private String userId;
    private TextView NoTienes;
    public Frag_GymkhanasApuntadas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_gymkhanas_apuntadas, container, false);
        gymkhanas=new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        listView=v.findViewById(R.id.ApuntadasGymkhnas);
        NoTienes=v.findViewById(R.id.SinParticipacion);
        ExtrarDatos();
        return v;
    }

    private void ExtrarDatos() {
        List<Gymkhana> gymkhanasList = new ArrayList<>();

        DatabaseReference gymkhanaReference= FirebaseDatabase.getInstance().getReference();
        Query query=gymkhanaReference.child("Gymkhana").orderByChild("participantes/"+userId).equalTo(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymkhanasList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Gymkhana gymkhana = dataSnapshot.getValue(Gymkhana.class);
                        gymkhanasList.add(gymkhana);
                    }

                    if (gymkhanasList.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        AdaptadorGymkhana mi = new AdaptadorGymkhana(getActivity(), gymkhanasList);
                        listView.setAdapter(mi);
                        NoTienes.setVisibility(View.INVISIBLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Gymkhana obj = (Gymkhana) adapterView.getItemAtPosition(i);
                                Intent paso = new Intent(getActivity(), GymkhanasApuntadas.class);
                                paso.putExtra("gymkana", (Serializable) obj);
                                startActivity(paso);
                            }
                        });
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Gymkhana gymkhana=(Gymkhana) adapterView.getItemAtPosition(i);

                                AlertDialog.Builder alerta= new AlertDialog.Builder(getContext());
                                alerta.setTitle("Desapuntarse de una gimkhana");
                                alerta.setMessage("¿Desea desapuntarse de esta gymkhana?")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseReference participanteRef = FirebaseDatabase.getInstance().getReference().child("Gymkhana").child(gymkhana.getId()).child("participantes").child(userId);
                                                participanteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            gymkhanasList.remove(gymkhana);
                                                            Toast.makeText(getActivity(), "Se ha desapuntado con exito", Toast.LENGTH_SHORT).show();
                                                            if(gymkhanasList.isEmpty()){
                                                                NoTienes.setVisibility(View.VISIBLE);
                                                                listView.setVisibility(View.INVISIBLE);
                                                            }
                                                        }else {
                                                            Toast.makeText(getActivity(), "Error en la aperacion", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                alerta.create().show();
                                return true;
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error en la base de datos", Toast.LENGTH_SHORT).show();

            }
        });

    }
}