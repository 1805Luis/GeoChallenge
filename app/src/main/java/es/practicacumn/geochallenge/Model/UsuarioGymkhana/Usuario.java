package es.practicacumn.geochallenge.Model.UsuarioGymkhana;

public class Usuario {

    private String Nombre;
    private String Apellido;
    private String Telefono;
    private String FechaNacimiento;
    private String Genero;
    private String GrupoSanguineo;
    private String Altura;
    private String Peso;
    private String AntecedentesMedicos;
    private String Alergias;

    public Usuario() {    }

    public Usuario(String nombre, String apellido, String telefono, String fechaNacimiento, String genero, String grupoSanguineo, String altura, String peso, String antecedentesMedicos, String alergias) {
        this.Nombre = nombre;
        this.Apellido = apellido;
        this.Telefono = telefono;
        this.FechaNacimiento = fechaNacimiento;
        this.Genero = genero;
        this.GrupoSanguineo = grupoSanguineo;
        this.Altura = altura;
        this.Peso = peso;
        this.AntecedentesMedicos = antecedentesMedicos;
        this.Alergias = alergias;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        this.Apellido = apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.FechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        this.Genero = genero;
    }

    public String getGrupoSanguineo() {
        return GrupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.GrupoSanguineo = grupoSanguineo;
    }

    public String getAltura() {
        return Altura;
    }

    public void setAltura(String altura) {
        this.Altura = altura;
    }

    public String getPeso() {
        return Peso;
    }

    public void setPeso(String peso) {
        this.Peso = peso;
    }

    public String getAntecedentesMedicos() {
        return AntecedentesMedicos;
    }

    public void setAntecedentesMedicos(String antecedentesMedicos) {
        this.AntecedentesMedicos = antecedentesMedicos;
    }

    public String getAlergias() {
        return Alergias;
    }

    public void setAlergias(String alergias) {
        this.Alergias = alergias;
    }
}
