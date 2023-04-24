package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Usuario;

public class Gymkhana implements Serializable {
    private String Id;
    private String Nombre;
    private String Lugar;
    private String Dificultad;
    private String DiaInicio;
    private String DiaFin;
    private String HoraInicio;
    private String HoraFin;
    private String MaxParticipantes;
    private List<Prueba> Pruebas;
    private boolean Terminado;
    private UbicacionGymkhana ubicacionGymkhana;
    private List<Usuario> usuarios;

    public Gymkhana(String id, String nombre, String lugar, String dificultad, String diaInicio, String diaFin, String horaInicio, String horaFin, String maxParticipantes, List<Prueba> pruebas, boolean terminado, UbicacionGymkhana ubicacionGymkhana, List<Usuario> usuarios) {
        Id = id;
        Nombre = nombre;
        Lugar = lugar;
        Dificultad = dificultad;
        DiaInicio = diaInicio;
        DiaFin = diaFin;
        HoraInicio = horaInicio;
        HoraFin = horaFin;
        MaxParticipantes = maxParticipantes;
        Pruebas = pruebas;
        Terminado = terminado;
        this.ubicacionGymkhana = ubicacionGymkhana;
        this.usuarios = usuarios;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getDificultad() {
        return Dificultad;
    }

    public void setDificultad(String dificultad) {
        Dificultad = dificultad;
    }

    public String getDiaInicio() {
        return DiaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        DiaInicio = diaInicio;
    }

    public String getDiaFin() {
        return DiaFin;
    }

    public void setDiaFin(String diaFin) {
        DiaFin = diaFin;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraFin() {
        return HoraFin;
    }

    public void setHoraFin(String horaFin) {
        HoraFin = horaFin;
    }

    public String getMaxParticipantes() {
        return MaxParticipantes;
    }

    public void setMaxParticipantes(String maxParticipantes) {
        MaxParticipantes = maxParticipantes;
    }

    public List<Prueba> getPruebas() {
        return Pruebas;
    }

    public void setPruebas(List<Prueba> pruebas) {
        Pruebas = pruebas;
    }

    public boolean isTerminado() {
        return Terminado;
    }

    public void setTerminado(boolean terminado) {
        Terminado = terminado;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public UbicacionGymkhana getUbicacionGymkhana() {
        return ubicacionGymkhana;
    }

    public void setUbicacionGymkhana(UbicacionGymkhana ubicacionGymkhana) {
        this.ubicacionGymkhana = ubicacionGymkhana;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
