package net.vyl.thz.notbook.models;

public class Notas {
    private long idNota;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String fecha_modificacion;

    public Notas(long idNota, String titulo, String descripcion, String contenido, String fecha_modificacion) {
        this.idNota = idNota;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.contenido = contenido;
        this.fecha_modificacion = fecha_modificacion;
    }

    public long getIdNota() {
        return idNota;
    }

    public void setIdNota(long idNota) {
        this.idNota = idNota;
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

    @Override
    public String toString() {
        return "Notas{" +
                "idNota=" + idNota +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
