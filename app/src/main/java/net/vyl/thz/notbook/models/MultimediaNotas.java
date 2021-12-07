package net.vyl.thz.notbook.models;

import java.io.Serializable;

public class MultimediaNotas implements Serializable {
    private long idMulNota;
    private long idNota;
    private String descripcion;
    private String multimedia;

    public MultimediaNotas(long idMulNota, long idNota, String descripcion, String multimedia) {
        this.idMulNota = idMulNota;
        this.idNota = idNota;
        this.descripcion = descripcion;
        this.multimedia = multimedia;
    }

    public long getIdMulNota() {
        return idMulNota;
    }

    public void setIdMulNota(long idMulNota) {
        this.idMulNota = idMulNota;
    }

    public long getIdNota() {
        return idNota;
    }

    public void setIdNota(long idNota) {
        this.idNota = idNota;
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
        return "MultimediaNotas{" +
                "idMulNota=" + idMulNota +
                ", idNota=" + idNota +
                '}';
    }
}
