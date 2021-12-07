package net.vyl.thz.notbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.vyl.thz.notbook.models.Notas;
import net.vyl.thz.notbook.models.Tareas;

import java.util.ArrayList;
import java.util.List;

public class DaoTareas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoTareas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(Tareas tareas){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_TAREAS[1], tareas.getTitulo());
        cv.put(DB.COLUMS_TABLE_TAREAS[2], tareas.getDescripcion());
        cv.put(DB.COLUMS_TABLE_TAREAS[3], tareas.getContenido());
        cv.put(DB.COLUMS_TABLE_TAREAS[4], tareas.getFecha_modificacion());
        cv.put(DB.COLUMS_TABLE_TAREAS[5], tareas.getFecha_cumplimiento());
        cv.put(DB.COLUMS_TABLE_TAREAS[6], tareas.getCompletada());
        return ad.insert(db.TABLE_TAREAS_NAME, null, cv);
    }

    public List<Tareas> getAll(){
        List<Tareas> lst = new ArrayList<Tareas>();
        Cursor cursor = ad.query(DB.TABLE_TAREAS_NAME, DB.COLUMS_TABLE_TAREAS, null, null, null, null, DB.COLUMS_TABLE_TAREAS[5] + " DESC");
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new Tareas(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getLong(6)));
            }
        }
        return lst;
    }

    public Tareas getOneByID(long id){
        Tareas tareas = null;
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_TAREAS_NAME + " where " + DB.COLUMS_TABLE_TAREAS[0] + "=?;", new String[]{String.valueOf(id)});

        if(cursor!= null){
            if(cursor.moveToFirst()){
                tareas = new Tareas(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
            }
        }
        return tareas;
    }

    public  boolean update(Tareas tareas){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_TAREAS[1], tareas.getTitulo());
        cv.put(DB.COLUMS_TABLE_TAREAS[2], tareas.getDescripcion());
        cv.put(DB.COLUMS_TABLE_TAREAS[3], tareas.getContenido());
        cv.put(DB.COLUMS_TABLE_TAREAS[4], tareas.getFecha_modificacion());
        cv.put(DB.COLUMS_TABLE_TAREAS[5], tareas.getFecha_cumplimiento());
        cv.put(DB.COLUMS_TABLE_TAREAS[6], tareas.getCompletada());
        return ad.update(DB.TABLE_TAREAS_NAME, cv,DB.COLUMS_TABLE_TAREAS[0] + "=?",
                new String[]{String.valueOf(tareas.getIdTarea())}) > 0;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_TAREAS_NAME,DB.COLUMS_TABLE_TAREAS[0] + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public List<Tareas> getAllByName(String nombre){
        /*return ad.rawQuery("select * from " + DB.TABLE_TAREAS_NAME + " where " + DB.COLUMS_TABLE_TAREAS[1] + " like '%"+ nombre + "%' OR "
                + DB.COLUMS_TABLE_TAREAS[2] + " like '%" + nombre + "%'",null );
                */

        List<Tareas> lst = new ArrayList<>();

        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_TAREAS_NAME + " where " + DB.COLUMS_TABLE_TAREAS[1] +
                " like '%"+ nombre + "%' OR " + DB.COLUMS_TABLE_TAREAS[2] + " like '%" + nombre + "%' order by " +
                DB.COLUMS_TABLE_TAREAS[5] + " DESC", null);

        if(cursor.getCount()>0){

            while(cursor.moveToNext()){
                lst.add(new Tareas(cursor.getLong(0),cursor.getString(1), cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5),
                        cursor.getLong(6)));
            }
        }
        return lst;
    }
}
