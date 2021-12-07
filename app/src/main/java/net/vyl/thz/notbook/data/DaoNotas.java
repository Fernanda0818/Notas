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

public class DaoNotas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoNotas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(Notas notas){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_NOTAS[1], notas.getTitulo());
        cv.put(DB.COLUMS_TABLE_NOTAS[2], notas.getDescripcion());
        cv.put(DB.COLUMS_TABLE_NOTAS[3], notas.getContenido());
        cv.put(DB.COLUMS_TABLE_NOTAS[4], notas.getFecha_modificacion().toString());
        return ad.insert(db.TABLE_NOTAS_NAME, null, cv);
    }

    public List<Notas> getAll(){
        List<Notas> lst = new ArrayList<Notas>();
        Cursor cursor = ad.query(DB.TABLE_NOTAS_NAME, DB.COLUMS_TABLE_NOTAS, null, null, null, null, DB.COLUMS_TABLE_NOTAS[4] + " DESC");
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new Notas(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }
        return lst;
    }

    public Notas getOneByID(long id){
        Notas nota = null;
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_NOTAS_NAME + " where " + DB.COLUMS_TABLE_NOTAS[0] + "=?", new String[]{String.valueOf(id)});

        if(cursor != null){
            Log.d("Prueba", "getOneByID: Entró al update " + cursor.getCount());
            if(cursor.moveToFirst()){
                Log.d("Prueba", "getOneByID: Está al inicio");
                nota = new Notas(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }
        }
        return nota;
    }

    public  boolean update(Notas nota){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_NOTAS[1], nota.getTitulo());
        cv.put(DB.COLUMS_TABLE_NOTAS[2], nota.getDescripcion());
        cv.put(DB.COLUMS_TABLE_NOTAS[3], nota.getContenido());
        cv.put(DB.COLUMS_TABLE_NOTAS[4], nota.getFecha_modificacion());
        return ad.update(DB.TABLE_NOTAS_NAME, cv,DB.COLUMS_TABLE_NOTAS[0] + "=?", new String[]{String.valueOf(nota.getIdNota())}) > 0;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_NOTAS_NAME,DB.COLUMS_TABLE_NOTAS[0] + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<Notas> getAllByName(String nombre){
        Log.d("Prueba", "getAllByName: " + nombre);
        List<Notas> lst = new ArrayList<>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_NOTAS_NAME + " where " + DB.COLUMS_TABLE_NOTAS[1] + " like '%"+ nombre + "%' OR " + DB.COLUMS_TABLE_NOTAS[2] + " like '%" + nombre + "%' order by " + DB.COLUMS_TABLE_NOTAS[4] + " DESC", null);
        Log.d("Prueba", "getAllByName: " + cursor.getCount());
        if(cursor.getCount()>0){
            Log.d("Prueba", "getAllByName: Count > 0");
            while(cursor.moveToNext()){
                lst.add(new Notas(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }
        return lst;
    }
}