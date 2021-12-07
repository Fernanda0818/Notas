package net.vyl.thz.notbook.models;

import java.io.Serializable;

public class AudioNotas implements Serializable {
    private long idAudNota;
    private long idNota;
    private String audio;

    public AudioNotas(long idAudNota, long idNota, String audio) {
        this.idAudNota = idAudNota;
        this.idNota = idNota;
        this.audio = audio;
    }

    public long getIdAudNota() {
        return idAudNota;
    }

    public void setIdAudNota(long idAudNota) {
        this.idAudNota = idAudNota;
    }

    public long getIdNota() {
        return idNota;
    }

    public void setIdNota(long idNota) {
        this.idNota = idNota;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "AudioNotas{" +
                "idAudNota=" + idAudNota +
                ", idNota=" + idNota +
                '}';
    }
}
