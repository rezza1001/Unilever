package g.rezza.moch.unileverapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import g.rezza.moch.unileverapp.connection.Database;

/**
 * Created by rezza on 27/01/18.
 */

public class OutletDB {

    public String user_id = "";
    public String outlet_id;
    public String username;
    public String password;
    public int    mine;
    public String outlet_name;
    public String outlet_contact;
    public String outlet_address;
    public String outlet_city;
    public String outlet_phone;
    public String outlet_email;
    public String outlet_photo;
    public String outlet_term = "-1";
    public String outlet_limit;
    public String outlet_npwp;
    public String outlet_npwp_addr;
    public String outlet_nik;
    public String url_foto;

    public static final String TAG   = "OutletDB";

    public static final String TABLE_NAME       = "OUTLET";

    public static final String FIELD_USER_ID    = "user_id";
    public static final String FIELD_O_ID       = "outlet_id";
    public static final String FIELD_USERNAME   = "username";
    public static final String FIELD_MINE       = "mine";
    public static final String FIELD_O_NAME     = "outlet_name";
    public static final String FIELD_O_CONTACT  = "outlet_contact";
    public static final String FIELD_O_ADDRESS  = "outlet_address";
    public static final String FIELD_O_CITY     = "outlet_city";
    public static final String FIELD_O_PHONE    = "outlet_phone";
    public static final String FIELD_O_EMAIL    = "outlet_email";
    public static final String FIELD_O_PHOTO    = "outlet_photo";
    public static final String FIELD_O_TERM     = "outlet_term_of_payment";
    public static final String FIELD_O_LIMIT    = "outlet_credit_limit";
    public static final String FIELD_O_NPWP       = "outlet_npwp";
    public static final String FIELD_O_NPWP_ADDR  = "outlet_npwp_address";
    public static final String FIELD_O_NIK        = "outlet_nik";
    public static final String FIELD_O_FOTO    = "outlet_nik_foto";
    public static final String FIELD_PASSWORD    = "password";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_USER_ID    + " varchar(50) NOT NULL," +
                " " + FIELD_O_ID + " varchar(50) NOT NULL," +
                " " + FIELD_USERNAME + " varchar(100) NOT NULL," +
                " " + FIELD_O_NAME + " varchar(200) NULL," +
                " " + FIELD_MINE  + " int default 0," +
                " " + FIELD_O_CONTACT  + " varchar(100) NULL," +
                " " + FIELD_O_ADDRESS  + " varchar(255) NULL," +
                " " + FIELD_O_CITY  + " varchar(255) NULL," +
                " " + FIELD_O_PHONE  + " varchar(30) NULL," +
                " " + FIELD_O_EMAIL  + " varchar(100) NULL," +
                " " + FIELD_O_PHOTO  + " varchar(100) NULL," +
                " " + FIELD_O_TERM  + " varchar(10) NULL," +
                " " + FIELD_O_LIMIT  + " varchar(30) NULL," +
                " " + FIELD_O_NPWP  + " varchar(30) NULL," +
                " " + FIELD_O_NPWP_ADDR  + " varchar(255) NULL," +
                " " + FIELD_O_NIK + " varchar(50) NULL," +
                " " + FIELD_O_FOTO  + " varchar(255) NULL," +
                " " + FIELD_PASSWORD  + " varchar(30) NULL," +
                "  PRIMARY KEY (" + FIELD_USER_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_USER_ID, user_id);
        contentValues.put(FIELD_O_ID, outlet_id);
        contentValues.put(FIELD_USERNAME, username);
        contentValues.put(FIELD_MINE, mine);
        contentValues.put(FIELD_O_NAME, outlet_name);
        contentValues.put(FIELD_O_ADDRESS, outlet_address);
        contentValues.put(FIELD_O_CITY, outlet_city);
        contentValues.put(FIELD_O_PHONE, outlet_phone);
        contentValues.put(FIELD_O_EMAIL, outlet_email);
        contentValues.put(FIELD_O_PHOTO, outlet_photo);
        contentValues.put(FIELD_O_TERM, outlet_term);
        contentValues.put(FIELD_O_LIMIT, outlet_limit);
        contentValues.put(FIELD_O_CONTACT, outlet_contact);
        contentValues.put(FIELD_O_NPWP, outlet_npwp);
        contentValues.put(FIELD_O_NPWP_ADDR, outlet_npwp_addr);
        contentValues.put(FIELD_O_NIK, outlet_nik);
        contentValues.put(FIELD_O_FOTO, url_foto);
        contentValues.put(FIELD_PASSWORD, password);
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
            pDB.delete(TABLE_NAME, " "+ FIELD_USER_ID+" = "+ id);
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

    public void getMine(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ FIELD_MINE +"=1 ", null);
        try {
            while (res.moveToNext()){
                this.user_id        = res.getString(res.getColumnIndex(FIELD_USER_ID));
                this.outlet_id      = res.getString(res.getColumnIndex(FIELD_O_ID));
                this.username       = res.getString(res.getColumnIndex(FIELD_USERNAME));
                this.mine           = res.getInt(res.getColumnIndex(FIELD_MINE));
                this.outlet_name    = res.getString(res.getColumnIndex(FIELD_O_NAME));
                this.outlet_address = res.getString(res.getColumnIndex(FIELD_O_ADDRESS));
                this.outlet_city    = res.getString(res.getColumnIndex(FIELD_O_CITY));
                this.outlet_phone   = res.getString(res.getColumnIndex(FIELD_O_PHONE));
                this.outlet_email   = res.getString(res.getColumnIndex(FIELD_O_EMAIL));
                this.outlet_photo   = res.getString(res.getColumnIndex(FIELD_O_PHOTO));
                this.outlet_term    = res.getString(res.getColumnIndex(FIELD_O_TERM));
                this.outlet_limit   = res.getString(res.getColumnIndex(FIELD_O_LIMIT));
                this.outlet_contact   = res.getString(res.getColumnIndex(FIELD_O_CONTACT));
                this.outlet_npwp    = res.getString(res.getColumnIndex(FIELD_O_NPWP));
                this.outlet_npwp_addr   = res.getString(res.getColumnIndex(FIELD_O_NPWP_ADDR));
                this.outlet_nik     = res.getString(res.getColumnIndex(FIELD_O_NIK));
                this.url_foto     = res.getString(res.getColumnIndex(FIELD_O_FOTO));

                this.password   = res.getString(res.getColumnIndex(FIELD_PASSWORD));
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
}
