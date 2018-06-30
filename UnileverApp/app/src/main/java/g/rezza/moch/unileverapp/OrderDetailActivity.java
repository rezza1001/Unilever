package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import g.rezza.moch.unileverapp.component.ListOrder;
import g.rezza.moch.unileverapp.component.TextKeyValue;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.holder.OrderPayment;
import g.rezza.moch.unileverapp.lib.ErrorCode;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView       imvw_back_00;
    private RelativeLayout  rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;


    private TextKeyValue txkv_order_00;
    private TextKeyValue txkv_date_order_00;
    private TextKeyValue txkv_status_00;
    private TextKeyValue txkv_term_00;
    private TextKeyValue txkv_payment_00;
    private TextKeyValue txkv_note_00;
    private ListOrder    lsvw_order_00;
    private OutletDB     outletDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initLayout();
        requestData();
    }

    private void initLayout() {
        imvw_back_00    = (ImageView) findViewById(R.id.imvw_back_00);
        rvly_cart_00    = (RelativeLayout) findViewById(R.id.rvly_cart_00);
        rvly_notif_00   = (RelativeLayout) findViewById(R.id.rvly_notif_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);

        txkv_order_00       = (TextKeyValue) findViewById(R.id.txkv_order_00);
        txkv_date_order_00  = (TextKeyValue) findViewById(R.id.txkv_date_order_00);
        txkv_status_00      = (TextKeyValue) findViewById(R.id.txkv_status_00);
        txkv_term_00        = (TextKeyValue) findViewById(R.id.txkv_term_00);
        txkv_payment_00     = (TextKeyValue) findViewById(R.id.txkv_payment_00);
        txkv_note_00        = (TextKeyValue) findViewById(R.id.txkv_note_00);
        lsvw_order_00       = (ListOrder) findViewById(R.id.lsvw_order_00);

        rvly_cart_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
        txvw_title_00.setText(getResources().getString(R.string.detail_order));

        txkv_order_00.setKey(getResources().getString(R.string.order_no));
        txkv_date_order_00.setKey(getResources().getString(R.string.order_date));
        txkv_status_00.setKey("Status");
        txkv_status_00.setVisibility(View.GONE);
        txkv_term_00.setKey(getResources().getString(R.string.expired_date));
        txkv_payment_00.setKey(getResources().getString(R.string.payment_method));
        txkv_note_00.setKey(getResources().getString(R.string.note));

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void requestData(){
        PostManager post = new PostManager(this);
        post.setApiUrl("Order/detail/orderId/"+getIntent().getStringExtra("ORDER_ID"));
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    JSONArray data = null;
                    try {
                        data = obj.getJSONArray("data");
                        JSONArray product_info = new JSONArray();
                        for (int i=0; i<data.length(); i++){
                            JSONObject product_item  = new JSONObject();
                            JSONObject detail = data.getJSONObject(i);
                            try {
                                product_item.put("product", detail.getString("product_name"));
                                product_item.put("product_qty", detail.getString("product_qty"));
                                product_item.put("product_pricelist", detail.getString("product_pricelist"));
                                product_item.put("product_discount", detail.getString("product_discount"));
                                product_item.put("product_selling_price", detail.getString("product_selling_price"));
                                product_info.put(product_item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        createDetail(product_info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(OrderDetailActivity.this,message,Toast.LENGTH_LONG).show();
                    OrderDetailActivity.this.finish();
                }
            }
        });
    }

    private void createDetail(JSONArray data_detail) {
        outletDB = new OutletDB();
        outletDB.getMine(this);

        txkv_order_00.setValue(getIntent().getStringExtra("ORDER_ID"));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        try {
            calendar.setTime(format1.parse(getIntent().getStringExtra("ORDER_DATE")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txkv_date_order_00.setValue(format2.format(calendar.getTime()));
        txkv_term_00.setValue("7 Hari");

        txkv_payment_00.setValue(getIntent().getStringExtra("PAYMENT"));
        txkv_note_00.setValue(getIntent().getStringExtra("NOTE"));

        try {
            ArrayList<OrderPayment> orders = new ArrayList<>();
            for (int i=0; i<data_detail.length(); i++){
                JSONObject data     = data_detail.getJSONObject(i);

                OrderPayment order  = new OrderPayment();
                order.product       = data.getString("product");
                order.description   = data.getString("product");
                order.qty           = data.getInt("product_qty");
                order.price         = data.getLong("product_selling_price");
                orders.add(order);
            }

            double discount = 0.0;
            try {
                discount = Double.parseDouble(getIntent().getStringExtra("DISCOUNT"));
            }catch (Exception e){
            }
            lsvw_order_00.create(orders );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
