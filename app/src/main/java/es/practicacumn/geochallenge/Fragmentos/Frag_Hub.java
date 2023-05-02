package es.practicacumn.geochallenge.Fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.practicacumn.geochallenge.ApuntarseGymkhana;
import es.practicacumn.geochallenge.ConsejosP;
import es.practicacumn.geochallenge.ConsejosS;
import es.practicacumn.geochallenge.CrearGymkhana;
import es.practicacumn.geochallenge.DetallesGymkhana;
import es.practicacumn.geochallenge.MainActivity;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Usuario.Usuario;
import es.practicacumn.geochallenge.R;
import es.practicacumn.geochallenge.TiempoMeteorologico;

public class Frag_Hub extends Fragment implements View.OnClickListener {
    private Button Crear,Auxilios,Participar,Supervivencia;
    private DatabaseReference mDatabase,GymkhanaRef,UserRef;
    private FirebaseAuth mAuth;
    private Usuario user;
    private String UserId;
    private List<Usuario> usuarioList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_hub, container, false);
        Crear=v.findViewById(R.id.crearGY);
        Crear.setOnClickListener(this);
        Auxilios=v.findViewById(R.id.auxilios);
        Auxilios.setOnClickListener(this);
        Participar=v.findViewById(R.id.participar);
        Participar.setOnClickListener(this);
        Supervivencia=v.findViewById(R.id.supervivencia);
        Supervivencia.setOnClickListener(this);
        ObtenerUsuario();
        //lanzarTiempo();
        return v;
    }

    private void ObtenerUsuario() {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        UserId=mAuth.getUid();
        mDatabase.child("Usuario").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    GenericTypeIndicator<Usuario> usuario=new GenericTypeIndicator<Usuario>() {};
                    user=snapshot.getValue(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void lanzarTiempo() {
        Frag_TiempoMet frag_tiempoMet=new Frag_TiempoMet();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ElTiempo, frag_tiempoMet);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.crearGY:
                Bundle extras = new Bundle();
                extras.putString("IdUsuario",UserId);
                Intent intent=new Intent(getContext(), CrearGymkhana.class);
                intent.putExtras(extras);
                startActivity(intent);
                break;

            case R.id.auxilios:
                Intent intent1=new Intent(getContext(), ConsejosP.class);
                startActivity(intent1);
                break;

            case R.id.participar:
                Participa();
                break;

            case R.id.supervivencia:
                Intent intent4=new Intent(getContext(), ConsejosS.class);
                startActivity(intent4);
                break;
        }
    }

    private void Participa() {
        if(user.getParticipaGymkhana().isParticipa()){
            AlertDialog.Builder alerta= new AlertDialog.Builder(getActivity());
            alerta.setTitle("Ya participa en una gymkhna");
            alerta.setMessage("¿Que desea hacer?")
                    .setCancelable(false)
                    .setPositiveButton("Desapuntarse y apuntarse a otra", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GymkhanaRef=FirebaseDatabase.getInstance().getReference();
                            usuarioList=new ArrayList<>();
                            GymkhanaRef.child("Gymkhana").child(user.getParticipaGymkhana().getIdGymkhana()).child("participantes").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                            Usuario usuario=dataSnapshot.getValue(Usuario.class);
                                            usuarioList.add(usuario);
                                        }
                                    }
                                    int i =0;
                                    boolean encontrado=false;
                                    while (i<usuarioList.size()&&!encontrado){
                                        if(usuarioList.get(i).equals(UserId)){
                                            usuarioList.remove(i);
                                            encontrado=true;
                                        }else{
                                            i++;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Error al descargar los datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                            GymkhanaRef.child("Gymkhana").child(user.getParticipaGymkhana().getIdGymkhana()).child("participantes").setValue(usuarioList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Cambios realizados con exito", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //Cambiar Datos Usuario
                            UserRef=FirebaseDatabase.getInstance().getReference("Usuario/"+UserId);
                            String mensaje="No participa";
                            Map<String,Object> participa=new HashMap<>();
                            participa.put("idGymkhana",mensaje);
                            participa.put("participa",false);
                            UserRef.child("participaGymkhana").updateChildren(participa);
                            Intent intent=new Intent(getContext(), ApuntarseGymkhana.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
            alerta.create().show();

        }else{
            Intent intent=new Intent(getContext(), ApuntarseGymkhana.class);
            startActivity(intent);
        }

    }
}