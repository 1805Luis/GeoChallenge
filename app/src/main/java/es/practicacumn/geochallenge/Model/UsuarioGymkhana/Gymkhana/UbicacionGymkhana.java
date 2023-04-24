package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import java.io.Serializable;

public class UbicacionGymkhana implements Serializable {
    double latitud;
    double longitud;

    public UbicacionGymkhana(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
