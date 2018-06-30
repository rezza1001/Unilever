package g.rezza.moch.unileverapp.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import g.rezza.moch.unileverapp.database.BrandDB;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;

/**
 * Created by rezza on 11/01/18.
 */

public class Database extends SQLiteOpenHelper {

    public static final String TAG = "RZ Database";

    public static final String DATABASE_NAME = "UNILEVER.db";
    private static final int DB_VERSION      = 10;

    private HashMap hp;
    public Database(Context contex) {
        super(contex, DATABASE_NAME , null, DB_VERSION);
        Log.d(TAG,"ACCESS DB ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate New Database ");
        db.execSQL("DROP TABLE IF EXISTS "+ OutletDB.TABLE_NAME);
        db.execSQL(new OutletDB().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ ProductDB.TABLE_NAME);
        db.execSQL(new ProductDB().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ ChartDB.TABLE_NAME);
        db.execSQL(new ChartDB().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ OrderDB.TABLE_NAME);
        db.execSQL(new OrderDB().getCreateTable());
        db.execSQL("DROP TABLE IF EXISTS "+ BrandDB.TABLE_NAME);
        db.execSQL(new BrandDB().getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"VERSION  "+ oldVersion + " <> "+ newVersion);
        switch (newVersion){
            case 10:
                onCreate(db);
                break;
        }

    }

    public boolean insert (String pTable, ContentValues pContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(pTable, null, pContent);
        return true;
    }

    public int update (String pTable, ContentValues pContent, String where) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(pTable,pContent,where,null);
    }

    public void updateColumn (String pTable, String query, String pWhere) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"UPDATE "+pTable+ " SET " +query+ " WHERE "+ pWhere);
        db.execSQL("UPDATE "+pTable+ " SET " +query+ " WHERE "+ pWhere);
    }

    public boolean delete(String pTable, String where) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"DELETE  FROM "+pTable+ " WHERE "+ where);
        db.execSQL("DELETE  FROM "+pTable+ " WHERE "+ where);
//        db.delete(pTable,where,null);
        return true;
    }


    public boolean delete(String pTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"DELETE  FROM "+pTable);
        db.execSQL("DELETE  FROM "+pTable);
//        db.delete(pTable,where,null);
        return true;
    }
}