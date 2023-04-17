package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import java.io.Serializable;
import java.util.List;

public class Gymkhana implements Serializable {
    private String Nombre;
    private String Lugar;
    private String Dificultad;
    private String DiaInicio;
    private String DiaFin;
    private String HoraInicio;
    private String HoraFinal;
    private String MaxParticipantes;
    private List<Prueba> Pruebas;
    private boolean Terminado;

    public Gymkhana(String nombre, String lugar, String dificultad, String diaInicio, String diaFin, String horaInicio, String horaFinal, String maxParticipantes, List<Prueba> pruebas, boolean terminado) {
        Nombre = nombre;
        Lugar = lugar;
        Dificultad = dificultad;
        DiaInicio = diaInicio;
        DiaFin = diaFin;
        HoraInicio = horaInicio;
        HoraFinal = horaFinal;
        MaxParticipantes = maxParticipantes;
        Pruebas = pruebas;
        Terminado = terminado;
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

    public String getHoraFinal() {
        return HoraFinal;
    }

    public void setHoraFinal(String horaFinal) {
        HoraFinal = horaFinal;
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
}
