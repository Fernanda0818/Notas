package net.vyl.thz.notbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.vyl.thz.notbook.models.RecordatoriosTareas;

import java.util.ArrayList;
import java.util.List;

public class DaoRecordatoriosTareas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoRecordatoriosTareas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(RecordatoriosTareas recordatorios){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_RECORDATORIOSTAREAS[1], recordatorios.getIdTarea());
        cv.put(DB.COLUMS_TABLE_RECORDATORIOSTAREAS[2], recordatorios.getRecordatorio());
        return ad.insert(db.TABLE_RECORDATORIOSTAREAS_NAME, null, cv);
    }

    public List<RecordatoriosTareas> getAllByIDTarea(long id){
        List<RecordatoriosTareas> lst = new ArrayList<RecordatoriosTareas>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_RECORDATORIOSTAREAS_NAME + " where " + DB.COLUMS_TABLE_RECORDATORIOSTAREAS[1] + "=?;", new String[]{String.valueOf(id)});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new RecordatoriosTareas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2)));
            }
        }
        return lst;
    }

    public RecordatoriosTareas getOneByID(long id){
        Cursor cursor = null;
        RecordatoriosTareas recordatorios = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_RECORDATORIOSTAREAS_NAME + " where " + DB.COLUMS_TABLE_RECORDATORIOSTAREAS[0] + "=?;", new String[]{String.valueOf(id)});

        if(cursor!= null){
            if(cursor.moveToFirst()){
                recordatorios = new RecordatoriosTareas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2));
            }
        }
        return recordatorios;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_RECORDATORIOSTAREAS_NAME, DB.COLUMS_TABLE_RECORDATORIOSTAREAS[0]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAllTarea(long id){
        return ad.delete(DB.TABLE_RECORDATORIOSTAREAS_NAME, DB.COLUMS_TABLE_RECORDATORIOSTAREAS[1]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
