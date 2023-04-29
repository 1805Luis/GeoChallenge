package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
@IgnoreExtraProperties
public class Prueba implements Serializable {
    private int Orden;
    private double Latitud;
    private double Longitud;
    private String Descripccion;
    private String CodigoQr;

    public Prueba() {  }

    public Prueba(int orden, double latitud, double longitud, String descripccion, String codigoQr) {
        Orden = orden;
        Latitud = latitud;
        Longitud = longitud;
        Descripccion = descripccion;
        CodigoQr = codigoQr;
    }

    public int getOrden() {
        return Orden;
    }

    public void setOrden(int orden) {
        Orden = orden;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public String getDescripccion() {
        return Descripccion;
    }

    public void setDescripccion(String descripccion) {
        Descripccion = descripccion;
    }

    public String getCodigoQr() {
        return CodigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        CodigoQr = codigoQr;
    }
}
