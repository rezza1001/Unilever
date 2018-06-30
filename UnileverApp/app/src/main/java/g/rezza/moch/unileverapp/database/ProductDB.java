package g.rezza.moch.unileverapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.connection.Database;
import g.rezza.moch.unileverapp.holder.KeyValueHolder;
import g.rezza.moch.unileverapp.holder.MenuGridHolder;

public class ProductDB {

    public String category_l1 = "";
    public String category_l2 = "";
    public String category_l3 = "";
    public String id = "";
    public String brand_id = "";
    public String brand = "";
    public String sku = "";
    public String name = "";
    public long pricelist = 0;
    public long selling_price = 0;
    public double discount = 0;
    public String photo_link = "";
    public String photo = "";

    public static final String TAG   = "ProductDB";
    public static final String TABLE_NAME       = "PRODUCT";

    public static final String FIELD_CATEGORY_L1    = "category_level_1";
    public static final String FIELD_CATEGORY_L2    = "category_level_2";
    public static final String FIELD_CATEGORY_L3    = "category_level_3";
    public static final String FIELD_ID             = "id";
    public static final String FIELD_BRAND          = "brand";
    public static final String FIELD_BRAND_ID     = "brand_id";
    public static final String FIELD_SKU            = "sku";
    public static final String FIELD_NAME           = "name";
    public static final String FIELD_PRICELIST      = "pricelist";
    public static final String FIELD_SELLING_PRICE  = "selling_price";
    public static final String FIELD_DISCOUNT       = "discount";
    public static final String FIELD_PATH           = "photo_link";
    public static final String FIELD_PHOTO          = "photo";


    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + FIELD_ID    + " varchar(50) NOT NULL," +
                " " + FIELD_CATEGORY_L1    + " varchar(100) NULL," +
                " " + FIELD_CATEGORY_L2    + " varchar(100) NULL," +
                " " + FIELD_CATEGORY_L3    + " varchar(100) NULL," +
                " " + FIELD_BRAND    + " varchar(100) NULL," +
                " " + FIELD_BRAND_ID    + " varchar(50) NULL," +
                " " + FIELD_SKU    + " varchar(50) NULL," +
                " " + FIELD_NAME    + " varchar(255) NULL," +
                " " + FIELD_PRICELIST    + " varchar(30) NULL," +
                " " + FIELD_SELLING_PRICE    + " varchar(30) NULL," +
                " " + FIELD_DISCOUNT    + " varchar(30) NULL," +
                " " + FIELD_PATH    + " varchar(255) NULL," +
                " " + FIELD_PHOTO    + " varchar(255) NULL," +
                "  PRIMARY KEY (" + FIELD_ID +"))";
        return create;
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_CATEGORY_L1, category_l1);
        contentValues.put(FIELD_CATEGORY_L2, category_l2);
        contentValues.put(FIELD_CATEGORY_L3, category_l3);
        contentValues.put(FIELD_BRAND, brand);
        contentValues.put(FIELD_BRAND_ID, brand_id);
        contentValues.put(FIELD_SKU, sku);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_PRICELIST, pricelist);
        contentValues.put(FIELD_SELLING_PRICE, selling_price);
        contentValues.put(FIELD_DISCOUNT, discount);
        contentValues.put(FIELD_PATH, photo_link);
        contentValues.put(FIELD_PHOTO, photo);
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
            pDB.delete(TABLE_NAME, " "+ FIELD_ID+" = "+ id);
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

    public void getProduct(Context context, String id){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME+ " WHERE "+ FIELD_ID +"='"+id+"'", null);
        try {
            while (res.moveToNext()){
                this.id              = res.getString(res.getColumnIndex(FIELD_ID));
                this.category_l1     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L1));
                this.category_l2     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L2));
                this.category_l3     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L3));
                this.brand           = res.getString(res.getColumnIndex(FIELD_BRAND));
                this.brand_id      = res.getString(res.getColumnIndex(FIELD_BRAND_ID));
                this.sku             = res.getString(res.getColumnIndex(FIELD_SKU));
                this.name            = res.getString(res.getColumnIndex(FIELD_NAME));
                this.pricelist       = Long.parseLong(res.getString(res.getColumnIndex(FIELD_PRICELIST)));
                this.selling_price   = Long.parseLong(res.getString(res.getColumnIndex(FIELD_SELLING_PRICE)));
                this.discount        =res.getDouble(res.getColumnIndex(FIELD_DISCOUNT));
                this.photo_link      = res.getString(res.getColumnIndex(FIELD_PATH));
                this.photo           = res.getString(res.getColumnIndex(FIELD_PHOTO));
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

    public ArrayList<ProductDB> getProducts(Context context){
        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME, null);
        ArrayList<ProductDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                ProductDB prod = new ProductDB();
                prod.id              = res.getString(res.getColumnIndex(FIELD_ID));
                prod.category_l1     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L1));
                prod.category_l2     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L2));
                prod.category_l3     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L3));
                prod.brand           = res.getString(res.getColumnIndex(FIELD_BRAND));
                prod.brand_id      = res.getString(res.getColumnIndex(FIELD_BRAND_ID));
                prod.sku             = res.getString(res.getColumnIndex(FIELD_SKU));
                prod.name            = res.getString(res.getColumnIndex(FIELD_NAME));
                prod.pricelist       = Long.parseLong(res.getString(res.getColumnIndex(FIELD_PRICELIST)));
                prod.selling_price   = Long.parseLong(res.getString(res.getColumnIndex(FIELD_SELLING_PRICE)));
                prod.discount        =res.getDouble(res.getColumnIndex(FIELD_DISCOUNT));
                prod.photo_link      = res.getString(res.getColumnIndex(FIELD_PATH));
                prod.photo           = res.getString(res.getColumnIndex(FIELD_PHOTO));
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

    public ArrayList<ProductDB> getProdcutsByBrand(Context context, String brand){

        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " WHERE "+ FIELD_BRAND_ID +"='"+brand+"'", null);
        ArrayList<ProductDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                ProductDB prod = new ProductDB();
                prod.id              = res.getString(res.getColumnIndex(FIELD_ID));
                prod.category_l1     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L1));
                prod.category_l2     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L2));
                prod.category_l3     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L3));
                prod.brand           = res.getString(res.getColumnIndex(FIELD_BRAND));
                prod.brand_id      = res.getString(res.getColumnIndex(FIELD_BRAND_ID));
                prod.sku             = res.getString(res.getColumnIndex(FIELD_SKU));
                prod.name            = res.getString(res.getColumnIndex(FIELD_NAME));
                prod.pricelist       = Long.parseLong(res.getString(res.getColumnIndex(FIELD_PRICELIST)));
                prod.selling_price   = Long.parseLong(res.getString(res.getColumnIndex(FIELD_SELLING_PRICE)));
                prod.discount        =res.getDouble(res.getColumnIndex(FIELD_DISCOUNT));
                prod.photo_link      = res.getString(res.getColumnIndex(FIELD_PATH));
                prod.photo           = res.getString(res.getColumnIndex(FIELD_PHOTO));
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

    public ArrayList<ProductDB> getProdcutsByBrandSortPrice(Context context, String brand,String sort){

        Database pDB = new Database(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME + " WHERE "+ FIELD_BRAND_ID +"='"+brand+"'"
                +" ORDER BY cast("+FIELD_SELLING_PRICE+" as unsigned) "+ sort, null);
        ArrayList<ProductDB> list = new ArrayList<>();
        try {
            while (res.moveToNext()){
                ProductDB prod = new ProductDB();
                prod.id              = res.getString(res.getColumnIndex(FIELD_ID));
                prod.category_l1     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L1));
                prod.category_l2     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L2));
                prod.category_l3     = res.getString(res.getColumnIndex(FIELD_CATEGORY_L3));
                prod.brand           = res.getString(res.getColumnIndex(FIELD_BRAND));
                prod.brand_id      = res.getString(res.getColumnIndex(FIELD_BRAND_ID));
                prod.sku             = res.getString(res.getColumnIndex(FIELD_SKU));
                prod.name            = res.getString(res.getColumnIndex(FIELD_NAME));
                prod.pricelist       = Long.parseLong(res.getString(res.getColumnIndex(FIELD_PRICELIST)));
                prod.selling_price   = Long.parseLong(res.getString(res.getColumnIndex(FIELD_SELLING_PRICE)));
                prod.discount        =res.getDouble(res.getColumnIndex(FIELD_DISCOUNT));
                prod.photo_link      = res.getString(res.getColumnIndex(FIELD_PATH));
                prod.photo           = res.getString(res.getColumnIndex(FIELD_PHOTO));
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
