package net.vyl.thz.notbook.models;

import java.io.Serializable;

public class AudioTareas implements Serializable {
    private long idAudTarea;
    private long idTarea;
    private String audio;

    public AudioTareas(long idAudTarea, long idTarea, String audio) {
        this.idAudTarea = idAudTarea;
        this.idTarea = idTarea;
        this.audio = audio;
    }

    public long getIdAudTarea() {
        return idAudTarea;
    }

    public void setIdAudTarea(long idAudTarea) {
        this.idAudTarea = idAudTarea;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "AudioTareas{" +
                "idAudTarea=" + idAudTarea +
                ", idTarea=" + idTarea +
                '}';
    }
}
