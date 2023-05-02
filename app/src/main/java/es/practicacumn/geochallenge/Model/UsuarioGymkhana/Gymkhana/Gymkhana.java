package es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Usuario.Usuario;

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
    private boolean Terminado;
    private UbicacionGymkhana UbicacionGymkhana;
    private List<Usuario> Usuarios;
    private String IdCreador;

    public Gymkhana() {

    }

    public Gymkhana(String id, String nombre, String lugar, String dificultad, String descripcion, String diaInicio, String diaFin, String horaInicio, String horaFin, int maxParticipantes, List<Prueba> pruebas, boolean terminado, UbicacionGymkhana ubicacionGymkhana, List<Usuario> usuarios, String idCreador) {
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
        Terminado = terminado;
        UbicacionGymkhana = ubicacionGymkhana;
        Usuarios = usuarios;
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
        return UbicacionGymkhana;
    }

    public void setUbicacionGymkhana(UbicacionGymkhana ubicacionGymkhana) {
        this.UbicacionGymkhana = ubicacionGymkhana;
    }

    public List<Usuario> getUsuarios() {
        return Usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        Usuarios = usuarios;
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
