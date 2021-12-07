package net.vyl.thz.notbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.vyl.thz.notbook.models.AudioTareas;

import java.util.ArrayList;
import java.util.List;

public class DaoAudioTareas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoAudioTareas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(AudioTareas audio){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_AUDIOTAREAS[1], audio.getIdTarea());
        cv.put(DB.COLUMS_TABLE_AUDIOTAREAS[2], audio.getAudio());
        return ad.insert(db.TABLE_AUDIOTAREAS_NAME, null, cv);
    }

    public List<AudioTareas> getAllByIDTarea(long id){
        List<AudioTareas> lst = new ArrayList<AudioTareas>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_AUDIOTAREAS_NAME + " where " + DB.COLUMS_TABLE_AUDIOTAREAS[1] + "=?;", new String[]{String.valueOf(id)});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new AudioTareas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2)));
            }
        }
        return lst;
    }

    public AudioTareas getOneByID(long id){
        Cursor cursor = null;
        AudioTareas audio = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_AUDIOTAREAS_NAME + " where " + DB.COLUMS_TABLE_AUDIOTAREAS[0] + "=?;", new String[]{String.valueOf(id)});

        if(cursor!= null){
            if(cursor.moveToFirst()){
                audio = new AudioTareas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2));
            }
        }
        return audio;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_AUDIOTAREAS_NAME, DB.COLUMS_TABLE_AUDIOTAREAS[0]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAllNota(long id){
        return ad.delete(DB.TABLE_AUDIOTAREAS_NAME, DB.COLUMS_TABLE_AUDIOTAREAS[1]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
