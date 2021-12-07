package net.vyl.thz.notbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.vyl.thz.notbook.models.MultimediaNotas;

import java.util.ArrayList;
import java.util.List;

public class DaoMultimediaNotas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoMultimediaNotas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(MultimediaNotas multimedia){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_MULTIMEDIANOTAS[1], multimedia.getIdNota());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIANOTAS[2], multimedia.getDescripcion());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIANOTAS[3], multimedia.getMultimedia());
        return ad.insert(db.TABLE_MULTIMEDIANOTAS_NAME, null, cv);
    }

    public List<MultimediaNotas> getAllByIDNota(long id){
        List<MultimediaNotas> lst = new ArrayList<MultimediaNotas>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_MULTIMEDIANOTAS_NAME + " where " + DB.COLUMS_TABLE_MULTIMEDIANOTAS[1] + "=?;", new String[]{String.valueOf(id)});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new MultimediaNotas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3)));
            }
        }
        return lst;
    }

    public MultimediaNotas getOneByID(long id){
        Cursor cursor = null;
        MultimediaNotas multimedia = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_MULTIMEDIANOTAS_NAME + " where " + DB.COLUMS_TABLE_MULTIMEDIANOTAS[0] + "=?;", new String[]{String.valueOf(id)});

        if(cursor!= null){
            if(cursor.moveToFirst()){
                multimedia = new MultimediaNotas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3));
            }
        }
        return multimedia;
    }

    public  boolean update(MultimediaNotas multimedia){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_MULTIMEDIANOTAS[1], multimedia.getIdNota());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIANOTAS[2], multimedia.getDescripcion());
        cv.put(DB.COLUMS_TABLE_MULTIMEDIANOTAS[3], multimedia.getMultimedia());
        return ad.update(DB.TABLE_MULTIMEDIANOTAS_NAME, cv,DB.COLUMS_TABLE_MULTIMEDIANOTAS[0]+ "=?", new String[]{String.valueOf(multimedia.getIdMulNota())}) > 0;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_MULTIMEDIANOTAS_NAME, DB.COLUMS_TABLE_MULTIMEDIANOTAS[0]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAllNota(long id){
        return ad.delete(DB.TABLE_MULTIMEDIANOTAS_NAME,DB.COLUMS_TABLE_MULTIMEDIANOTAS[1]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
