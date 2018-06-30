package g.rezza.moch.unileverapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import g.rezza.moch.unileverapp.connection.Database;

public class BrandDB {

    public String id        = "";
    public String name      = "";
    public String image     = "";
    public String status    = "";


    public static final String TAG   = "BrandDB";
    public static final String TABLE_NAME       = "BRAND";

    public static final String FIELD_ID             = "id";
    public static final String FIELD_NAME           = "name";
    public static final String FIELD_IMAGE          = "image";
    public static final String FIELD_STATUS         = "status";



    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " varchar(50) NOT NULL," +
                " " + FIELD_NAME    + " varchar(50) NULL," +
                " " + FIELD_IMAGE    + " varchar(256) NULL," +
                " " + FIELD_STATUS    + " varchar(50) NULL," +
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_IMAGE, image);
        contentValues.put(FIELD_STATUS, status);

        return contentValues;
    }

    public void clearData(Context context){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME);
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    public void deleteData(Context context, String id){
        try {
            Database pDB = new Database(context);
            pDB.delete(TABLE_NAME, " "+ FIELD_ID+" ='"+ id+"'");
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    public boolean insert(Context context){
        Log.d(TAG, "Insert Data ");
        boolean x = false;
        try {
            Database pDB = new Database(context);
            x = pDB.insert(TABLE_NAME, createContentValues());
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        return x;
    }

    public int update(Context context, String where){
        Log.d(TAG, "Update Data ");
        int x = 0;
        try {
            Database pDB = new Database(context);
            x = pDB.update(TABLE_NAME, createContentValues(), where);
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return x;
    }

    public void getByID(Context context, String id){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ FIELD_ID +"='"+id+"'", null);
        try {
            while (res.moveToNext()){
                this.id     = res.getString(res.getColumnIndex(FIELD_ID));
                this.name   = res.getString(res.getColumnIndex(FIELD_NAME));
                this.image  = res.getString(res.getColumnIndex(FIELD_IMAGE));
                this.status = res.getString(res.getColumnIndex(FIELD_STATUS));
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }

    }

    public ArrayList<BrandDB> get(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME , null);
        ArrayList<BrandDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                BrandDB prod = new BrandDB();
                prod.id         = res.getString(res.getColumnIndex(FIELD_ID));
                prod.name       = res.getString(res.getColumnIndex(FIELD_NAME));
                prod.image      = res.getString(res.getColumnIndex(FIELD_IMAGE));
                prod.status      = res.getString(res.getColumnIndex(FIELD_STATUS));
                list.add(prod);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        finally {
            res.close();
            pDB.close();
        }
        return list;

    }
}
