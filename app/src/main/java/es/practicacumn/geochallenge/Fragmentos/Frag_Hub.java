package es.practicacumn.geochallenge.Fragmentos;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import es.practicacumn.geochallenge.ApuntarseGymkhana;
import es.practicacumn.geochallenge.ConsejosP;
import es.practicacumn.geochallenge.ConsejosS;
import es.practicacumn.geochallenge.CrearGymkhana;

import es.practicacumn.geochallenge.DetallesGymkhana;
import es.practicacumn.geochallenge.GymkhanasApuntadas;
import es.practicacumn.geochallenge.HubJugando;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.R;


public class Frag_Hub extends Fragment implements View.OnClickListener {
    private Button Crear,Auxilios,Participar,Supervivencia,Jugar;
    private List<Gymkhana> gymkhanaList;
    private FirebaseUser user;
    private String userId;

    public Frag_Hub() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_hub, container, false);
        gymkhanaList=new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        Crear=v.findViewById(R.id.crearGY);
        Crear.setOnClickListener(this);
        Auxilios=v.findViewById(R.id.auxilios);
        Auxilios.setOnClickListener(this);
        Participar=v.findViewById(R.id.participar);
        Participar.setOnClickListener(this);
        Supervivencia=v.findViewById(R.id.supervivencia);
        Supervivencia.setOnClickListener(this);
        Jugar=v.findViewById(R.id.jugar);
        Jugar.setOnClickListener(this);
        lanzarTiempo();
        participar();
        return v;
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
                Intent intent=new Intent(getContext(), CrearGymkhana.class);
                startActivity(intent);
                break;

            case R.id.auxilios:
                Intent intent1=new Intent(getContext(), ConsejosP.class);
                startActivity(intent1);
                break;

            case R.id.participar:
                Intent intent2=new Intent(getContext(), ApuntarseGymkhana.class);
                startActivity(intent2);
                break;

            case R.id.supervivencia:
                Intent intent3=new Intent(getContext(), ConsejosS.class);
                startActivity(intent3);
                break;
            case R.id.jugar:
                Intent intent4=new Intent(getContext(), HubJugando.class);
                startActivity(intent4);
                break;
        }
    }


    private void participar() {
        DatabaseReference gymkhanaReference= FirebaseDatabase.getInstance().getReference();
        Query query=gymkhanaReference.child("Gymkhana").orderByChild("participantes/"+userId).equalTo(false);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymkhanaList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Gymkhana gymkhana = dataSnapshot.getValue(Gymkhana.class);
                        if(gymkhana.getEstado().equals("En progreso")){
                            gymkhanaList.add(gymkhana);
                        }

                    }

                    if (gymkhanaList.size() > 0) {
                        Jugar.setVisibility(View.VISIBLE);
                    }else{
                        Jugar.setVisibility(View.GONE);
                    }
                }else{
                    Jugar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}