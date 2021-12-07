package net.vyl.thz.notbook.models;

public class RecordatoriosTareas {
    private long idRecTarea;
    private long idTarea;
    private String recordatorio;

    public RecordatoriosTareas(long idRecTarea, long idTarea, String recordatorio) {
        this.idRecTarea = idRecTarea;
        this.idTarea = idTarea;
        this.recordatorio = recordatorio;
    }

    public long getIdRecTarea() {
        return idRecTarea;
    }

    public void setIdRecTarea(long idRecTarea) {
        this.idRecTarea = idRecTarea;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public String getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }

    @Override
    public String toString() {
        return "RecordatoriosTareas{" +
                "idRecTarea=" + idRecTarea +
                ", idTarea=" + idTarea +
                '}';
    }
}
