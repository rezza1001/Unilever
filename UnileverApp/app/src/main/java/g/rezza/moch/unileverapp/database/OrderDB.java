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

public class OrderDB {

    public String   id              = "-99";
    public double   order_disc      = 0;
    public String   order_notes     = "";
    public String   order_pay_type  = "";
    public String   order_detail    = "";
    public String   created_at      = "";
    public String   order_date      = "";
    public String   order_subtotal      = "";
    public String   order_total      = "";
    public String   outlet_credit_limit      = "";


    public static final String TAG   = "OrderDB";
    public static final String TABLE_NAME       = "ORDER_PRODUCT";

    public static final String FIELD_ID             = "id";
    public static final String FIELD_DISCOUNT       = "order_disc";
    public static final String FIELD_NOTE           = "order_notes";
    public static final String FIELD_PAY_TYPE       = "order_pay_type";
    public static final String FIELD_DETAIL         = "order_detail";
    public static final String FIELD_ORDER_DATE     = "order_date";
    public static final String FIELD_SUBTOTAL       = "order_subtotal";
    public static final String FIELD_CREDIT_LIMIT   = "outlet_credit_limit";
    public static final String FIELD_TOTAL          = "order_total";
    public static final String FIELD_CREATED_AT     = "created_at";



    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " varchar(100) default '0'," +
                " " + FIELD_DISCOUNT    + " varchar(50) default '0'," +
                " " + FIELD_NOTE    + " varchar(255) NULL," +
                " " + FIELD_PAY_TYPE    + " varchar(255) NULL," +
                " " + FIELD_ORDER_DATE    + " varchar(255) NULL," +
                " " + FIELD_SUBTOTAL    + " varchar(255) NULL," +
                " " + FIELD_CREDIT_LIMIT    + " varchar(255) NULL," +
                " " + FIELD_TOTAL    + " varchar(255) NULL," +
                " " + FIELD_DETAIL    + " text NULL," +
                " " + FIELD_CREATED_AT    + " datetime  ,"+
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
//        id = System.currentTimeMillis()+"";
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_DISCOUNT, order_disc);
        contentValues.put(FIELD_NOTE, order_notes);
        contentValues.put(FIELD_PAY_TYPE, order_pay_type);
        contentValues.put(FIELD_DETAIL, order_detail);
        contentValues.put(FIELD_SUBTOTAL, order_subtotal);
        contentValues.put(FIELD_CREDIT_LIMIT, outlet_credit_limit);
        contentValues.put(FIELD_ORDER_DATE, order_date);
        contentValues.put(FIELD_TOTAL, order_total);
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


    public ArrayList<OrderDB> getOrders(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " ORDER BY "+FIELD_CREATED_AT +" DESC ", null);
        ArrayList<OrderDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                OrderDB prod = new OrderDB();
                prod.id             = res.getString(res.getColumnIndex(FIELD_ID));
                prod.order_disc     = res.getDouble(res.getColumnIndex(FIELD_DISCOUNT));
                prod.order_notes    = res.getString(res.getColumnIndex(FIELD_NOTE));
                prod.order_pay_type = res.getString(res.getColumnIndex(FIELD_PAY_TYPE));
                prod.order_detail   = res.getString(res.getColumnIndex(FIELD_DETAIL));
                prod.outlet_credit_limit   = res.getString(res.getColumnIndex(FIELD_CREDIT_LIMIT));
                prod.created_at     = res.getString(res.getColumnIndex(FIELD_CREATED_AT));
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

    public void getOrder(Context context, String id){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME +" WHERE "+ FIELD_ID +"='"+id+"'", null);
        try {
            while (res.moveToNext()){
                this.id             = res.getString(res.getColumnIndex(FIELD_ID));
                this.order_disc     = res.getDouble(res.getColumnIndex(FIELD_DISCOUNT));
                this.order_notes    = res.getString(res.getColumnIndex(FIELD_NOTE));
                this.order_pay_type = res.getString(res.getColumnIndex(FIELD_PAY_TYPE));
                this.order_detail   = res.getString(res.getColumnIndex(FIELD_DETAIL));
                this.created_at     = res.getString(res.getColumnIndex(FIELD_CREATED_AT));
                this.order_subtotal = res.getString(res.getColumnIndex(FIELD_SUBTOTAL));
                this.order_total    = res.getString(res.getColumnIndex(FIELD_TOTAL));
                this.order_date     = res.getString(res.getColumnIndex(FIELD_ORDER_DATE));
                this.outlet_credit_limit     = res.getString(res.getColumnIndex(FIELD_CREDIT_LIMIT));
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
