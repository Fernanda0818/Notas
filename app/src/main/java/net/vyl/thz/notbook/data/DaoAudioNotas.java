package net.vyl.thz.notbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.vyl.thz.notbook.models.AudioNotas;

import java.util.ArrayList;
import java.util.List;

public class DaoAudioNotas {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoAudioNotas(Context context){
        this.context = context;
        db = new DB(context);
        ad = db.getWritableDatabase();
    }

    public long insert(AudioNotas audio){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLE_AUDIONOTAS[1], audio.getIdNota());
        cv.put(DB.COLUMS_TABLE_AUDIONOTAS[2], audio.getAudio());
        return ad.insert(db.TABLE_AUDIONOTAS_NAME, null, cv);
    }

    public List<AudioNotas> getAllByIDNota(long id){
        List<AudioNotas> lst = new ArrayList<AudioNotas>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_AUDIONOTAS_NAME + " where " + DB.COLUMS_TABLE_AUDIONOTAS[1] + "=?;", new String[]{String.valueOf(id)});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                lst.add(new AudioNotas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2)));
            }
        }
        return lst;
    }

    public AudioNotas getOneByID(long id){
        Cursor cursor = null;
        AudioNotas audio = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_AUDIONOTAS_NAME + " where " + DB.COLUMS_TABLE_AUDIONOTAS[0] + "=?;", new String[]{String.valueOf(id)});

        if(cursor!= null){
            if(cursor.moveToFirst()){
                audio = new AudioNotas(cursor.getLong(0), cursor.getLong(1), cursor.getString(2));
            }
        }
        return audio;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_AUDIONOTAS_NAME, DB.COLUMS_TABLE_AUDIONOTAS[0]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAllNota(long id){
        return ad.delete(DB.TABLE_AUDIONOTAS_NAME, DB.COLUMS_TABLE_AUDIONOTAS[1]+ "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
