package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class Gymkhana implements Serializable {
    private String Id;
    private String Nombre;
    private String Lugar;
    private String Dificultad;
    private String Descripcion;
    private String DiaInicio;
    private String DiaFin;
    private String HoraInicio;
    private String HoraFin;
    private int MaxParticipantes;
    private List<Prueba> Pruebas;
    private String Estado;
    private UbicacionGymkhana UbicacionGymkhana;
    private List<String> Idusuarios;
    private String IdCreador;

    public Gymkhana() {

    }

    public Gymkhana(String id, String nombre, String lugar, String dificultad, String descripcion, String diaInicio, String diaFin, String horaInicio, String horaFin, int maxParticipantes, List<Prueba> pruebas, String estado, UbicacionGymkhana ubicacionGymkhana, List<String> usuarios, String idCreador) {
        Id = id;
        Nombre = nombre;
        Lugar = lugar;
        Dificultad = dificultad;
        Descripcion = descripcion;
        DiaInicio = diaInicio;
        DiaFin = diaFin;
        HoraInicio = horaInicio;
        HoraFin = horaFin;
        MaxParticipantes = maxParticipantes;
        Pruebas = pruebas;
        Estado = estado;
        UbicacionGymkhana = ubicacionGymkhana;
        Idusuarios = usuarios;
        IdCreador = idCreador;
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

    public int getMaxParticipantes() {
        return MaxParticipantes;
    }

    public void setMaxParticipantes(int maxParticipantes) {
        MaxParticipantes = maxParticipantes;
    }

    public List<Prueba> getPruebas() {
        return Pruebas;
    }

    public void setPruebas(List<Prueba> pruebas) {
        Pruebas = pruebas;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public UbicacionGymkhana getUbicacionGymkhana() {
        return UbicacionGymkhana;
    }

    public void setUbicacionGymkhana(UbicacionGymkhana ubicacionGymkhana) {
        this.UbicacionGymkhana = ubicacionGymkhana;
    }

    public List<String> getIdusuarios() {
        return Idusuarios;
    }

    public void setIdusuarios(List<String> idusuarios) {
        Idusuarios = idusuarios;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getIdCreador() {
        return IdCreador;
    }

    public void setIdCreador(String idCreador) {
        IdCreador = idCreador;
    }
}
