package es.practicacumn.geochallenge.Fragmentos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.practicacumn.geochallenge.Adaptadores.AdaptadorGymkhana;
import es.practicacumn.geochallenge.MisGymkhanas;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.R;

public class Frag_MisGymkhanas extends Fragment {

    private ListView listView;
    private List<Gymkhana> gymkhanas;
    private DatabaseReference GymkhanasRef;
    private FirebaseUser user;
    private String userId;
    private TextView NoTienes;
    private ProgressBar progressBar;
    public Frag_MisGymkhanas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_mis_gymkhanas, container, false);
        gymkhanas=new ArrayList<>();
        listView=v.findViewById(R.id.misGymkhnas);
        NoTienes=v.findViewById(R.id.Sininfo);
        progressBar=v.findViewById(R.id.progressBar2);
        ExtrarDatos();
        return v;
    }
    private void ExtrarDatos() {
        progressBar.setVisibility(View.VISIBLE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        GymkhanasRef= FirebaseDatabase.getInstance().getReference("Gymkhana");
        Query query=GymkhanasRef.orderByChild("idCreador").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymkhanas.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Gymkhana gymkhana = dataSnapshot.getValue(Gymkhana.class);
                        gymkhanas.add(gymkhana);
                    }
                }
                if (gymkhanas.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    AdaptadorGymkhana mi = new AdaptadorGymkhana(getActivity(), gymkhanas);
                    listView.setAdapter(mi);
                    progressBar.setVisibility(View.INVISIBLE);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Gymkhana obj=(Gymkhana) adapterView.getItemAtPosition(i);
                            Intent paso = new Intent(getActivity(), MisGymkhanas.class);
                            paso.putExtra("gymkana",(Serializable) obj);
                            startActivity(paso);
                        }
                    });
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Gymkhana gymkhana=(Gymkhana) adapterView.getItemAtPosition(i);

                            AlertDialog.Builder alerta= new AlertDialog.Builder(getContext());
                            alerta.setTitle("Importante");

                            TextView titleText = new TextView(getActivity());
                            titleText.setText("Importante");
                            titleText.setTypeface(Typeface.DEFAULT_BOLD);
                            titleText.setTextSize(25);
                            titleText.setGravity(Gravity.CENTER);

                            alerta.setCustomTitle(titleText);
                            alerta.setMessage("¿Desea eliminar esta gymkhana?")
                                    .setCancelable(false)
                                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String ruta= "/codigosQR/"+gymkhana.getId();
                                            EliminarStorage(ruta);
                                            ElimarGymkhana(gymkhana.getId());
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
                }else{
                    NoTienes.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                NoTienes.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void ElimarGymkhana(String id) {
        DatabaseReference Gymkhana=FirebaseDatabase.getInstance().getReference("Gymkhana");
        DatabaseReference gymkhana=Gymkhana.child(id);
        gymkhana.removeValue();
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


}