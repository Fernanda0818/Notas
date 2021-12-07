package net.vyl.thz.notbook.models;

public class Tareas {
    private long idTarea;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String fecha_modificacion;
    private String fecha_cumplimiento;
    private long completada;

    public Tareas(long idTarea, String titulo, String descripcion, String contenido, String fecha_modificacion, String fecha_cumplimiento, long completada) {
        this.idTarea = idTarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.contenido = contenido;
        this.fecha_modificacion = fecha_modificacion;
        this.fecha_cumplimiento = fecha_cumplimiento;
        this.completada = completada;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(String fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public String getFecha_cumplimiento() {
        return fecha_cumplimiento;
    }

    public void setFecha_cumplimiento(String fecha_cumplimiento) {
        this.fecha_cumplimiento = fecha_cumplimiento;
    }

    public long getCompletada() {
        return completada;
    }

    public void setCompletada(int completada) {
        this.completada = completada;
    }

    @Override
    public String toString() {
        return "Tareas{" +
                "idTarea=" + idTarea +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
