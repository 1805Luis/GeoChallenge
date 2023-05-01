package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Usuario;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ParticipaGymkhana {
   private String IdGymkhana;
   private boolean Participa;

    public ParticipaGymkhana() {    }

    public ParticipaGymkhana(String idGymkhana, boolean participa) {
        IdGymkhana = idGymkhana;
        Participa = participa;
    }

    public String getIdGymkhana() {
        return IdGymkhana;
    }

    public void setIdGymkhana(String idGymkhana) {
        IdGymkhana = idGymkhana;
    }

    public boolean isParticipa() {
        return Participa;
    }

    public void setParticipa(boolean participa) {
        Participa = participa;
    }
}
