package net.vyl.thz.notbook.models;

import java.io.Serializable;

public class MultimediaTareas implements Serializable {
    private long idMulTarea;
    private long idTarea;
    private String descripcion;
    private String multimedia;

    public MultimediaTareas(long idMulTarea, long idTarea, String descripcion, String multimedia) {
        this.idMulTarea = idMulTarea;
        this.idTarea = idTarea;
        this.descripcion = descripcion;
        this.multimedia = multimedia;
    }

    public long getIdMulTarea() {
        return idMulTarea;
    }

    public void setIdMulTarea(long idMulTarea) {
        this.idMulTarea = idMulTarea;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    @Override
    public String toString() {
        return "MultimediaTareas{" +
                "idMulTarea=" + idMulTarea +
                ", idTarea=" + idTarea +
                '}';
    }
}
