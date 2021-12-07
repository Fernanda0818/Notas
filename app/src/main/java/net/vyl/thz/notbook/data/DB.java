package net.vyl.thz.notbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME = "DBNotBook";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NOTAS_NAME = "notas";
    public static final String[] COLUMS_TABLE_NOTAS = {"idnota", "titulo", "descripcion", "contenido", "fecha_modificacion"};
    private static final String SCRIPT_TABLE_NOTAS = "create table if not exists " + TABLE_NOTAS_NAME + " (" +
            COLUMS_TABLE_NOTAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_NOTAS[1] + " text not null," +
            COLUMS_TABLE_NOTAS[2] + " text(250)," +
            COLUMS_TABLE_NOTAS[3] + " text," +
            COLUMS_TABLE_NOTAS[4] + " text not null" +
            ");";
    public static final String TABLE_MULTIMEDIANOTAS_NAME = "multimedianotas";
    public static final String[] COLUMS_TABLE_MULTIMEDIANOTAS = {"idmulnota", "idnota", "descripcion", "multimedia"};
    private static final String SCRIPT_TABLE_MULTIMEDIANOTAS = "create table if not exists multimedianotas (" +
            COLUMS_TABLE_MULTIMEDIANOTAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_MULTIMEDIANOTAS[1] + " integer not null," +
            COLUMS_TABLE_MULTIMEDIANOTAS[2] + " text," +
            COLUMS_TABLE_MULTIMEDIANOTAS[3] + " text not null," +
            "foreign key (" + COLUMS_TABLE_MULTIMEDIANOTAS[1] + ") references " + TABLE_NOTAS_NAME + "(" + COLUMS_TABLE_NOTAS[0] + ")" +
            " on delete cascade on update cascade);";
    public static final String TABLE_AUDIONOTAS_NAME = "audionotas";
    public static final String[] COLUMS_TABLE_AUDIONOTAS = {"idaudnota", "idnota", "audio"};
    private static final String SCRIPT_TABLE_AUDIONOTAS = "create table if not exists audionotas (" +
            COLUMS_TABLE_AUDIONOTAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_AUDIONOTAS[1] + " integer not null," +
            COLUMS_TABLE_AUDIONOTAS[2] + " text not null," +
            "foreign key (" + COLUMS_TABLE_AUDIONOTAS[1] + ") references " + TABLE_NOTAS_NAME + "(" + COLUMS_TABLE_NOTAS[1] + ")" +
            " on delete cascade on update cascade);";
    public static final String TABLE_TAREAS_NAME = "tareas";
    public static final String[] COLUMS_TABLE_TAREAS = {"idtarea", "titulo", "descripcion", "contenido",
            "fecha_modificacion", "fecha_cumplimiento", "completada"};
    private static final String SCRIPT_TABLE_TAREAS = "create table if not exists tareas (" +
            COLUMS_TABLE_TAREAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_TAREAS[1] + " text not null," +
            COLUMS_TABLE_TAREAS[2] + " text(250)," +
            COLUMS_TABLE_TAREAS[3] + " text," +
            COLUMS_TABLE_TAREAS[4] + " text not null," +
            COLUMS_TABLE_TAREAS[5] + " text not null," +
            COLUMS_TABLE_TAREAS[6] + " integer not null" +
            ");";
    public static final String TABLE_MULTIMEDIATAREAS_NAME = "multimediatareas";
    public static final String[] COLUMS_TABLE_MULTIMEDIATAREAS = {"idmultarea", "idtarea", "descripcion", "multimedia"};
    private static final String SCRIPT_TABLE_MULTIMEDIATAREAS = "create table if not exists multimediatareas (" +
            COLUMS_TABLE_MULTIMEDIATAREAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_MULTIMEDIATAREAS[1] + " integer not null," +
            COLUMS_TABLE_MULTIMEDIATAREAS[2] + " text," +
            COLUMS_TABLE_MULTIMEDIATAREAS[3] + " text not null," +
            "foreign key (" + COLUMS_TABLE_MULTIMEDIATAREAS[1] + ") references " + TABLE_TAREAS_NAME + "(" + COLUMS_TABLE_TAREAS[0] + ")" +
            " on delete cascade on update cascade);";
    public static final String TABLE_AUDIOTAREAS_NAME = "audiotareas";
    public static final String[] COLUMS_TABLE_AUDIOTAREAS = {"idaudtarea", "idtarea", "audio"};
    private static final String SCRIPT_TABLE_AUDIOTAREAS = "create table if not exists audiotareas (" +
            COLUMS_TABLE_AUDIOTAREAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_AUDIOTAREAS[1] + " integer not null," +
            COLUMS_TABLE_AUDIOTAREAS[2] + " text not null," +
            "foreign key (" + COLUMS_TABLE_AUDIOTAREAS[1] + ") references " + TABLE_TAREAS_NAME + "(" + COLUMS_TABLE_TAREAS[1] + ")" +
            " on delete cascade on update cascade);";
    public static final String TABLE_RECORDATORIOSTAREAS_NAME = "recordatoriostareas";
    public static final String[] COLUMS_TABLE_RECORDATORIOSTAREAS = {"idrectarea", "idtarea", "recordatorio"};
    private static final String SCRIPT_TABLE_RECORDATORIOSTAREAS = "create table if not exists recordatoriostareas (" +
            COLUMS_TABLE_RECORDATORIOSTAREAS[0] + " integer primary key autoincrement," +
            COLUMS_TABLE_RECORDATORIOSTAREAS[1] + " integer not null," +
            COLUMS_TABLE_RECORDATORIOSTAREAS[2] + " text not null," +
            "foreign key (" + COLUMS_TABLE_RECORDATORIOSTAREAS[1] + ") references " + TABLE_TAREAS_NAME + "(" + COLUMS_TABLE_TAREAS[1] + ")" +
            " on delete cascade on update cascade);";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCRIPT_TABLE_NOTAS);
        sqLiteDatabase.execSQL(SCRIPT_TABLE_MULTIMEDIANOTAS);
        sqLiteDatabase.execSQL(SCRIPT_TABLE_AUDIONOTAS);
        sqLiteDatabase.execSQL(SCRIPT_TABLE_TAREAS);
        sqLiteDatabase.execSQL(SCRIPT_TABLE_MULTIMEDIATAREAS);
        sqLiteDatabase.execSQL(SCRIPT_TABLE_AUDIOTAREAS);
        sqLiteDatabase.execSQL(SCRIPT_TABLE_RECORDATORIOSTAREAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
