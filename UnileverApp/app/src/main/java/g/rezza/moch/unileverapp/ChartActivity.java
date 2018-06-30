package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.adapter.ProductOrderAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.MyOrderHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class ChartActivity extends AppCompatActivity {

    private static final String TAG = "ChartActivity";
    private ImageView imvw_back_00;
    private ListView                    lsvw_order_00;
    private ProductOrderAdapter         adapter;
    private ArrayList<MyOrderHolder>    list = new ArrayList<>();
    private Button                      bbtn_action_00;
    private TextView                    txvw_total_00;
    private TextView                    txvw_empty_00;
    private TextView                    txvw_counter_00;
    private RelativeLayout              rvly_bottom_00;
    private TextView                    txvw_notif_00;
    private Button                      bbtn_close_00;
    private RelativeLayout              rvly_notif_00;
    private Double  mCredit = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initLayout();
        initListener();
        initData();
        checkLimit();
    }

    private void initLayout(){
        imvw_back_00        = (ImageView) findViewById(R.id.imvw_back_00);
        lsvw_order_00       = (ListView) findViewById(R.id.lsvw_order_00);
        adapter             = new ProductOrderAdapter(this,list);
        bbtn_action_00      = (Button) findViewById(R.id.bbtn_action_00);
        txvw_total_00       = (TextView) findViewById(R.id.txvw_total_00);
        txvw_empty_00       = (TextView) findViewById(R.id.txvw_empty_00);
        txvw_counter_00     = (TextView) findViewById(R.id.txvw_counter_00);
        rvly_bottom_00      = (RelativeLayout) findViewById(R.id.rvly_bottom_00);
        rvly_notif_00       = (RelativeLayout) findViewById(R.id.rvly_notif_00);
        txvw_notif_00       = (TextView) findViewById(R.id.txvw_notif_00);
        bbtn_close_00       = (Button) findViewById(R.id.bbtn_close_00);

        lsvw_order_00.setAdapter(adapter);
        txvw_empty_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        adapter.setOOnChangeQtyListener(new ProductOrderAdapter.OnChangeQtyListener() {
            @Override
            public void OnSelect(MyOrderHolder product, int position) {
                ChartDB chartDB = new ChartDB();
                chartDB.getProduct(ChartActivity.this, product.product);
                chartDB.qty = product.qty;
                chartDB.update(ChartActivity.this, " "+ ChartDB.FIELD_PRODUCT_ID+" = '"+product.product+"'");
                processTotal();
            }
        });

        adapter.setAfterDeleteListener(new ProductOrderAdapter.afterDeleteListener() {
            @Override
            public void afterChange(MyOrderHolder event, int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                processTotal();
            }
        });

        rvly_notif_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        bbtn_close_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvly_notif_00.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
       this.finish();
    }

    private void initData(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(ChartActivity.this);
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(ChartActivity.this,chart.product_id );
            MyOrderHolder holder = new MyOrderHolder();
            holder.product      = chart.product_id;
            holder.description  = productDB.name;
            holder.img_url      = productDB.photo_link+""+productDB.photo;
            holder.qty          = chart.qty;
            holder.price        = productDB.selling_price;
            holder.max_qty      = 100;
            list.add(holder);

        }
        txvw_counter_00.setText(getResources().getString(R.string.cart)+" ("+list.size()+")");
        adapter.notifyDataSetChanged();
        processTotal();
    }

    private void processTotal(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(ChartActivity.this);
        if (charts.size() == 0){
            txvw_empty_00.setVisibility(View.VISIBLE);
            rvly_bottom_00.setVisibility(View.GONE);
        }
        else {
            txvw_empty_00.setVisibility(View.GONE);
        }
        long total = 0;
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(ChartActivity.this,chart.product_id );
            long price = chart.qty * productDB.selling_price;
            total = total + price;
        }
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total+""));
        txvw_total_00.setTag(total+"");
        txvw_counter_00.setText(getResources().getString(R.string.cart)+" ("+list.size()+")");
    }



    private String getDetialOrder(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(ChartActivity.this);
        JSONArray product_info = new JSONArray();
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(ChartActivity.this,chart.product_id );
            JSONObject product_item  = new JSONObject();
            try {
                product_item.put("product_id", productDB.id);
                product_item.put("product_qty", chart.qty);
                product_item.put("product_pricelist", productDB.pricelist);
                product_item.put("product_discount", productDB.discount);
                product_item.put("product_selling_price", productDB.selling_price);
                product_info.put(product_item);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return product_info.toString();
    }

    private void checkLimit(){
        OutletDB outletDB = new OutletDB();
        outletDB.getMine(this);
        PostManager post = new PostManager(this);
        post.setApiUrl("/Order/credit_limit_check/outletId/"+outletDB.outlet_id);
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    try {
                        txvw_notif_00.setText("Total pembelian anda sudah melebihi batas kredit "+
                                obj.getString("total_credit_limit"));
                        mCredit = Double.parseDouble(obj.getString("total_credit_limit"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ChartActivity.this, message, Toast.LENGTH_SHORT).show();
                    ChartActivity.this.finish();
                }
            }
        });
    }

    private void sendData(){
        double total = Double.parseDouble(txvw_total_00.getTag().toString());
//        mCredit = 2000000.0;
        if (mCredit <  total){
            rvly_notif_00.setVisibility(View.VISIBLE);
            return;
        }

        PostManager post = new PostManager(this);
        post.setApiUrl("/Order/discount_check/order_subtotal/"+txvw_total_00.getTag().toString());
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                String discount = "0";
                if (code == ErrorCode.OK){
                    try {
                         discount = obj.getString("discount_calc");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ChartActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                OrderDB orderDB = new OrderDB();
                orderDB.clearData(ChartActivity.this);
                orderDB.order_disc = Double.parseDouble(discount);
                orderDB.order_pay_type = "-";
                orderDB.order_detail = getDetialOrder();
                orderDB.insert(ChartActivity.this);

                Intent intent = new Intent(ChartActivity.this, PaymentActivity.class);
                intent.putExtra("ORDER_ID", "-99");
                startActivity(intent);
                ChartActivity.this.finish();
            }
        });
    }

}
