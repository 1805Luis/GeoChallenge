package es.practicacumn.geochallenge.Model.Consejos;

import java.io.Serializable;

public class Consejos implements Serializable {
    int Id;
    String Orden;
    String Descripccion;

    public Consejos(int id, String orden, String descripccion) {
        Id = id;
        Orden = orden;
        Descripccion = descripccion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getOrden() {
        return Orden;
    }

    public void setOrden(String orden) {
        Orden = orden;
    }

    public String getDescripccion() {
        return Descripccion;
    }

    public void setDescripccion(String descripccion) {
        Descripccion = descripccion;
    }
}
