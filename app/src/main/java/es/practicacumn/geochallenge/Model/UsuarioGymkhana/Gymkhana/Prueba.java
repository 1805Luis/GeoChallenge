package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import java.io.Serializable;

public class Prueba implements Serializable {
    private int Orden;
    private double Latitud;
    private double Longitud;
    private String Descripccion;

    public Prueba(int orden, double latitud, double longitud, String descripccion) {
        Orden = orden;
        Latitud = latitud;
        Longitud = longitud;
        Descripccion = descripccion;
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
}
