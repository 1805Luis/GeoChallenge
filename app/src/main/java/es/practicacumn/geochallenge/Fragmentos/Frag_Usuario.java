package es.practicacumn.geochallenge.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Usuario;
import es.practicacumn.geochallenge.R;

public class Frag_Usuario extends Fragment {
    private DatabaseReference mDatabase,userReference;
    private TextView Unombre,Uapellido,Utelf,UfechaNac,Usexo,Usangre,Ualtura,Upeso,Ualergia,Uantecedentes;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_usuario, container, false);
        inicializar(v);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        userId=user.getUid();
        userReference=mDatabase.child("Usuario").child(userId);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                if (usuario!=null){
                    Unombre.setText("Nombre: "+usuario.getNombre());
                    Uapellido.setText("Apellidos: "+usuario.getApellido());
                    Utelf.setText("Telefono: "+usuario.getTelefono());
                    UfechaNac.setText(usuario.getFechaNacimiento());
                    Usexo.setText(usuario.getGenero());
                    Usangre.setText(usuario.getGrupoSanguineo());
                    Ualtura.setText(usuario.getAltura()+" cm");
                    Upeso.setText(usuario.getPeso()+" kg");
                    Ualergia.setText(usuario.getAlergias());
                    Uantecedentes.setText(usuario.getAntecedentesMedicos());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }

    private void inicializar(View v) {
        Unombre=v.findViewById(R.id.NombreUser);
        Uapellido=v.findViewById(R.id.ApellidosUser);
        Utelf=v.findViewById(R.id.TelefonoUser);
        UfechaNac=v.findViewById(R.id.FechaNacimientoUser);
        Usexo=v.findViewById(R.id.SexoUser);
        Usangre=v.findViewById(R.id.SangreUser);
        Ualtura=v.findViewById(R.id.AlturaUser);
        Upeso=v.findViewById(R.id.PesoUser);
        Ualergia=v.findViewById(R.id.AlergiasUser);
        Uantecedentes=v.findViewById(R.id.MedicoUser);

    }
}