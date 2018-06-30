package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.Static.App;
import g.rezza.moch.unileverapp.adapter.ProductGridAdapter;
import g.rezza.moch.unileverapp.adapter.ProductListAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.ProductHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProductActivity";

    private ImageView           imvw_back_00;
    private ListView            lsvw_product_00;
    private GridView            grvw_product_00;
    private ProductListAdapter  adapter ;
    private ProductGridAdapter  adapter_grid;
    private ArrayList<ProductHolder> list = new ArrayList<>();
    private ArrayList<ProductHolder> list_filter = new ArrayList<>();
    private LinearLayout        bbtn_list_00;
    private ImageView           imvw_option_00;
    private TextView            txvw_option_00;
    private TextView            txvw_title_00;
    private TextView            txvw_counter_00;
    private RelativeLayout      rvly_cart_counter_00;
    private RelativeLayout      rvly_cart_00;
    private EditText            edtx_search_00;
    private RelativeLayout      rvly_popup_00;
    private LinearLayout        lnly_pricebottom_00;
    private LinearLayout        lnly_pricetop_00;
    private LinearLayout        lnly_news_00;
    private ImageView           imvw_check_01;
    private ImageView           imvw_check_02;
    private ImageView           imvw_check_03;
    private LinearLayout        bbtn_sort_00;

    private String  mSort = "NONE";

    private int option = App.MENU_GRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initLayout();
        initListener();
        retrieveData();
        switchOption();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initLayout(){
        imvw_back_00    = (ImageView) findViewById(R.id.imvw_back_00);
        lsvw_product_00 = (ListView) findViewById(R.id.lsvw_product_00);
        adapter         = new ProductListAdapter(this, list_filter);
        adapter_grid    = new ProductGridAdapter(this, list_filter);
        bbtn_list_00    = (LinearLayout) findViewById(R.id.bbtn_list_00);
        imvw_option_00  = (ImageView) findViewById(R.id.imvw_option_00);
        txvw_option_00  = (TextView) findViewById(R.id.txvw_option_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        grvw_product_00 = (GridView) findViewById(R.id.grvw_product_00);
        txvw_counter_00     = (TextView)    findViewById(R.id.txvw_counter_00);
        rvly_cart_counter_00= (RelativeLayout) findViewById(R.id.rvly_cart_counter_00);
        rvly_cart_00        = (RelativeLayout) findViewById(R.id.rvly_cart_00);
        edtx_search_00      = (EditText)    findViewById(R.id.edtx_search_00);
        rvly_popup_00       = (RelativeLayout)  findViewById(R.id.rvly_popup_00);

        bbtn_sort_00        = (LinearLayout)    findViewById(R.id.bbtn_sort_00) ;
        lnly_news_00        = (LinearLayout)    findViewById(R.id.lnly_news_00) ;
        lnly_pricetop_00    = (LinearLayout)    findViewById(R.id.lnly_pricetop_00) ;
        lnly_pricebottom_00 = (LinearLayout)    findViewById(R.id.lnly_pricebottom_00) ;
        imvw_check_01       = (ImageView) findViewById(R.id.imvw_check_01);
        imvw_check_02       = (ImageView) findViewById(R.id.imvw_check_02);
        imvw_check_03       = (ImageView) findViewById(R.id.imvw_check_03);

        lsvw_product_00.setAdapter(adapter);
        grvw_product_00.setAdapter(adapter_grid);
        rvly_cart_counter_00.bringToFront();

        txvw_title_00.requestFocus();
        rvly_popup_00.setVisibility(View.GONE);
    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn_list_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (option == App.MENU_LIST){
                    option = App.MENU_GRID;
                }
                else {
                    option = App.MENU_LIST;
                }
                switchOption();
            }
        });

        adapter_grid.setOnSelectedListener(new ProductGridAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ProductHolder event, int position) {
                Intent intent = new Intent(ProductActivity.this, DetailProductActivity.class);
                intent.putExtra("PRODUCTID", event.product_id);
                startActivityForResult(intent, 1);
                ProductActivity.this.finish();
            }
        });

        adapter.setOnSelectedListener(new ProductListAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ProductHolder event, int position) {
                Intent intent = new Intent(ProductActivity.this, DetailProductActivity.class);
                intent.putExtra("PRODUCTID", event.product_id);
                startActivityForResult(intent, 1);
                ProductActivity.this.finish();
            }
        });

        rvly_cart_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, ChartActivity.class);
                startActivity(intent);
                ProductActivity.this.finish();
            }
        });

        edtx_search_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterData(editable.toString());
            }
        });

        rvly_popup_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvly_popup_00.setVisibility(View.GONE);
            }
        });

        lnly_news_00.setOnClickListener(this);
        lnly_pricebottom_00.setOnClickListener(this);
        lnly_pricetop_00.setOnClickListener(this);
        bbtn_sort_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvly_popup_00.setVisibility(View.VISIBLE);
            }
        });

    }

    private void switchOption(){
        if (option == App.MENU_LIST){
            grvw_product_00.setVisibility(View.GONE);
            lsvw_product_00.setVisibility(View.VISIBLE);
            txvw_option_00.setText("TAMPILAN");
            imvw_option_00.setImageResource(R.drawable.ic_grid);
        }
        else {
            grvw_product_00.setVisibility(View.VISIBLE);
            lsvw_product_00.setVisibility(View.GONE);
            txvw_option_00.setText("TAMPILAN");
            imvw_option_00.setImageResource(R.drawable.ic_list);
        }
    }

    private void retrieveData(){
        PostManager post = new PostManager(this);
        post.setApiUrl("product/inquiry");
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {

                if (code == ErrorCode.OK){
                    try {
                        ProductDB DB = new ProductDB();
                        DB.clearData(ProductActivity.this);
                        JSONArray data = obj.getJSONArray("data");
                        for (int i=0; i<data.length(); i++){
                            JSONObject prod = data.getJSONObject(i);
                            ProductDB productDB = new ProductDB();
                            productDB.id            = prod.getString("product_id");
                            productDB.category_l1   = prod.getString("category_level_1");
                            productDB.category_l2   = prod.getString("category_level_2");
                            productDB.category_l3   = prod.getString("category_level_3");
                            productDB.brand         = prod.getString("brand_name");
                            productDB.brand_id      = prod.getString("brand_id");
                            productDB.sku           = prod.getString("product_sku");
                            productDB.name          = prod.getString("product_name");
                            productDB.pricelist     = prod.getLong("product_pricelist");
                            productDB.selling_price = prod.getLong("selling_price");
                            productDB.discount      = prod.getDouble("product_discount");
                            productDB.photo_link    = prod.getString("product_photo_link");
                            productDB.photo         = prod.getString("product_photo");
                            productDB.insert(ProductActivity.this);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initData();
            }
        });
    }

    private void initData(){
        ChartDB chartDBCounter = new ChartDB();
        ArrayList<ChartDB> carts = chartDBCounter.getProducts(this);
        txvw_counter_00.setText(""+carts.size());

        txvw_title_00.setText(getIntent().getStringExtra("BRAND"));

        getData("");

    }

    private void getData(String sort){
        list.clear();
        ProductDB productDB = new ProductDB();
        ArrayList<ProductDB> products = new ArrayList<>();
        if (sort.isEmpty()){
            products = productDB.getProdcutsByBrand(ProductActivity.this, getIntent().getStringExtra("BRAND_ID"));

        }
        else {
            String status = "DESC";
            if (sort.equals("PRICE_BOTTOM")){
                status = "ASC";
            }
            products = productDB.getProdcutsByBrandSortPrice(ProductActivity.this, getIntent().getStringExtra("BRAND_ID"),status);
        }
        for(ProductDB prod: products){
            ProductHolder holder = new ProductHolder();
            holder.product_id    = prod.id;
            holder.description   = prod.name;
            holder.product       = prod.brand;
            holder.img_url       = prod.photo_link+"/"+prod.photo;
            holder.price         = prod.selling_price;

            ChartDB chartDB = new ChartDB();
            chartDB.getProduct(ProductActivity.this,holder.product_id);
            if (chartDB.product_id.isEmpty()){
                holder.inChart = 0;
            }
            else {
                holder.inChart = 1;
            }

            list.add(holder);
        }
        filterData("");
    }

    private void filterData(String s){
        list_filter.clear();
        if (s.isEmpty()){
            for (ProductHolder product: list){
                list_filter.add(product);
            }
        }
        else {
            for (ProductHolder product: list){
                if (product.description.toLowerCase().contains(s.toLowerCase())){
                    list_filter.add(product);
                }

            }
        }
        adapter.notifyDataSetChanged();
        adapter_grid.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switchSort(view);
    }

    private void switchSort(View view){
        imvw_check_01.setImageResource(R.drawable.ic_check_disable);
        imvw_check_02.setImageResource(R.drawable.ic_check_disable);
        imvw_check_03.setImageResource(R.drawable.ic_check_disable);
        if (view == lnly_news_00){
            mSort = "";
            imvw_check_01.setImageResource(R.drawable.ic_check_enable);
        }
        else if (view == lnly_pricebottom_00){
            mSort = "PRICE_BOTTOM";
            imvw_check_03.setImageResource(R.drawable.ic_check_enable);
        }
        else if (view == lnly_pricetop_00){
            mSort = "PRICE_TOP";
            imvw_check_02.setImageResource(R.drawable.ic_check_enable);
        }
        getData(mSort);
        handler.sendEmptyMessageDelayed(1,200);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 1){
                rvly_popup_00.setVisibility(View.GONE);
            }
            return false;
        }
    });
}
