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

public class ChartDB {

    public String product_id = "";
    public int qty           = 0;
    public String created_at = "";


    public static final String TAG   = "ChartDB";
    public static final String TABLE_NAME       = "CHART";

    public static final String FIELD_PRODUCT_ID     = "product_id";
    public static final String FIELD_QTY            = "qty";
    public static final String FIELD_CREATED_AT     = "created_at";



    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_PRODUCT_ID    + " varchar(50) NOT NULL," +
                " " + FIELD_QTY    + " varchar(10) NULL," +
                " " + FIELD_CREATED_AT    + " datetime NULL," +
                "  PRIMARY KEY (" + FIELD_PRODUCT_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_PRODUCT_ID, product_id);
        contentValues.put(FIELD_QTY, qty);
        contentValues.put(FIELD_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

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
            pDB.delete(TABLE_NAME, " "+ FIELD_PRODUCT_ID+" ='"+ id+"'");
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

    public void getProduct(Context context, String id){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ FIELD_PRODUCT_ID +"='"+id+"'", null);
        try {
            while (res.moveToNext()){
                this.product_id = res.getString(res.getColumnIndex(FIELD_PRODUCT_ID));
                this.qty        = res.getInt(res.getColumnIndex(FIELD_QTY));
                this.created_at = res.getString(res.getColumnIndex(FIELD_CREATED_AT));
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

    public ArrayList<ChartDB> getProducts(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " ORDER BY "+FIELD_CREATED_AT, null);
        ArrayList<ChartDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                ChartDB prod = new ChartDB();
                prod.product_id = res.getString(res.getColumnIndex(FIELD_PRODUCT_ID));
                prod.qty        = res.getInt(res.getColumnIndex(FIELD_QTY));
                prod.created_at = res.getString(res.getColumnIndex(FIELD_CREATED_AT));
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
