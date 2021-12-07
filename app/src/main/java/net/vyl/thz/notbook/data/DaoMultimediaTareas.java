package net.vyl.thz.notbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.vyl.thz.notbook.models.MultimediaTareas;

import java.util.ArrayList;
import java.util.List;

public class DaoMultimediaTareas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoMultimediaTareas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(MultimediaTareas multimedia){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_MULTIMEDIATAREAS[1], multimedia.getIdTarea());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIATAREAS[2], multimedia.getDescripcion());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIATAREAS[3], multimedia.getMultimedia());
        return ad.insert(db.TABLE_MULTIMEDIATAREAS_NAME, null, cv);
    }

    public List<MultimediaTareas> getAllByIDNota(long id){
        List<MultimediaTareas> lst = new ArrayList<MultimediaTareas>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_MULTIMEDIATAREAS_NAME + " where " + DB.COLUMS_TABLE_MULTIMEDIATAREAS[1] + "=?;", new String[]{String.valueOf(id)});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new MultimediaTareas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3)));
            }
        }
        return lst;
    }

    public MultimediaTareas getOneByID(long id){
        Cursor cursor = null;
        MultimediaTareas multimedia = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_MULTIMEDIATAREAS_NAME + " where " + DB.COLUMS_TABLE_MULTIMEDIATAREAS[0] + "=?;", new String[]{String.valueOf(id)});

        if(cursor!= null){
            if(cursor.moveToFirst()){
                multimedia = new MultimediaTareas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3));
            }
        }
        return multimedia;
    }

    public  boolean update(MultimediaTareas multimedia){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_MULTIMEDIATAREAS[1], multimedia.getIdTarea());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIATAREAS[2], multimedia.getDescripcion());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIATAREAS[3], multimedia.getMultimedia());
        return ad.update(DB.TABLE_MULTIMEDIATAREAS_NAME, cv,DB.COLUMS_TABLE_MULTIMEDIATAREAS[0]+ "=?", new String[]{String.valueOf(multimedia.getIdMulTarea())}) > 0;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_MULTIMEDIATAREAS_NAME, DB.COLUMS_TABLE_MULTIMEDIATAREAS[0]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAllTarea(long id){
        return ad.delete(DB.TABLE_MULTIMEDIATAREAS_NAME,DB.COLUMS_TABLE_MULTIMEDIATAREAS[1]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
