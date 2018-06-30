package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import g.rezza.moch.unileverapp.component.TextKeyValue;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.fragment.home.InvoiceFragment;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class InvoiceDetailActivity extends AppCompatActivity {

    private ImageView       imvw_back_00;
    private RelativeLayout  rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;

    private TextKeyValue txkv_order_00;
    private TextKeyValue txkv_invoice_00;
    private TextKeyValue txkv_invdate_00;
    private TextKeyValue txkv_invduedate_00;
    private TextKeyValue txkv_date_order_00;
    private TextKeyValue txkv_status_00;
    private TextKeyValue txkv_discount_00;
    private TextKeyValue txkv_total_00;
    private TextKeyValue txkv_payment_00;
    private TextKeyValue txkv_note_00;

    private Button      bbtn_action_00;
    private HashMap<String, Integer> MAP_PAYMENT = new HashMap<>();

    private OutletDB outletDB;
    private String product = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        initLayout();
        initListener();
        requestData();
    }

    private void initLayout() {
        imvw_back_00    = (ImageView)       findViewById(R.id.imvw_back_00);
        rvly_cart_00    = (RelativeLayout)  findViewById(R.id.rvly_cart_00);
        rvly_notif_00   = (RelativeLayout)  findViewById(R.id.rvly_notif_00);
        txvw_title_00   = (TextView)        findViewById(R.id.txvw_title_00);

        txkv_order_00       = (TextKeyValue)    findViewById(R.id.txkv_order_00);
        txkv_invoice_00     = (TextKeyValue)    findViewById(R.id.txkv_invoice_00);
        txkv_invdate_00     = (TextKeyValue)    findViewById(R.id.txkv_invdate_00);
        txkv_invduedate_00  = (TextKeyValue)    findViewById(R.id.txkv_invduedate_00);
        txkv_date_order_00  = (TextKeyValue)    findViewById(R.id.txkv_date_order_00);
        txkv_status_00      = (TextKeyValue)    findViewById(R.id.txkv_status_00);
        txkv_discount_00    = (TextKeyValue)    findViewById(R.id.txkv_discount_00);
        txkv_total_00       = (TextKeyValue)    findViewById(R.id.txkv_total_00);
        txkv_payment_00     = (TextKeyValue)    findViewById(R.id.txkv_payment_00);
        txkv_note_00        = (TextKeyValue)    findViewById(R.id.txkv_note_00);
        bbtn_action_00      = (Button)          findViewById(R.id.bbtn_action_00);

        rvly_cart_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
        txvw_title_00.setText(getResources().getString(R.string.invoice));

        txkv_order_00.setKey(getResources().getString(R.string.order_no));
        txkv_invoice_00.setKey(getResources().getString(R.string.invoice_no));
        txkv_invdate_00.setKey(getResources().getString(R.string.invoice_date));
        txkv_invduedate_00.setKey(getResources().getString(R.string.due_date));
        txkv_date_order_00.setKey(getResources().getString(R.string.order_date));
        txkv_discount_00.setKey(getResources().getString(R.string.total_ullage));
        txkv_total_00.setKey(getResources().getString(R.string.total_payment));
        txkv_payment_00.setKey(getResources().getString(R.string.payment_method));
        txkv_note_00.setKey(getResources().getString(R.string.note));
        txkv_status_00.setKey("Status");
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

                String cc = getResources().getString(R.string.credit_card);
                String cc1 = "Credit Card";
                String tf = "Transfer Bank";
                String tf1 = "Bank Transfer";
                MAP_PAYMENT.put(cc,15);
                MAP_PAYMENT.put(cc1,15);
                MAP_PAYMENT.put(tf,36);
                MAP_PAYMENT.put(tf1,36);
                if (       txkv_payment_00.getValue().equals(cc)
                        || txkv_payment_00.getValue().equals(cc1)
                        || txkv_payment_00.getValue().equals(tf)
                        || txkv_payment_00.getValue().equals(tf1)){
                    Intent intent = new Intent(InvoiceDetailActivity.this, WebViewActivity.class);
                    intent.putExtra("payment",MAP_PAYMENT.get(txkv_payment_00.getValue()));
                    intent.putExtra("invoice_id",txkv_invoice_00.getValue());
                    intent.putExtra("total",txkv_total_00.getTag().toString());
                    intent.putExtra("products",product);

                    startActivity(intent);
                    InvoiceDetailActivity.this.finish();
                }
                else {
                    Intent intent = new Intent(InvoiceDetailActivity.this, FinishPaymentActivity.class);
                    intent.putExtra("PAYMENT",txkv_payment_00.getValue());
                    startActivity(intent);
                    InvoiceDetailActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void requestData(){
        outletDB = new OutletDB();
        outletDB.getMine(this);
        Log.e("TAGRZ","Invoice/detail/outletId/"+outletDB.outlet_id+"/invoiceId/"+getIntent().getStringExtra("INVOICE"));
        PostManager post = new PostManager(this);
        post.setApiUrl("Invoice/detail/outletId/"+outletDB.outlet_id+"/invoiceId/"+getIntent().getStringExtra("INVOICE")+"/orderId/"+getIntent().getStringExtra("ORDERID"));
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    JSONArray data = null;
                    try {
                        data = obj.getJSONArray("data");
                        JSONObject detail = data.getJSONObject(0);
                        txkv_invoice_00.setValue(detail.getString("invoice_id"));

                        Calendar calendar2 = Calendar.getInstance();
                        SimpleDateFormat fm_time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat fm_time2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                        try {
                            calendar2.setTime(fm_time1.parse(detail.getString("order_date")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        txkv_date_order_00.setValue(fm_time2.format(calendar2.getTime()));

                        Calendar calendar = Calendar.getInstance();
                        Calendar inv_cal = Calendar.getInstance();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
                        try {
                            calendar.setTime(format1.parse(detail.getString("invoice_due_date")));
                            inv_cal.setTime(format1.parse(detail.getString("invoice_date")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        txkv_invduedate_00.setValue(format2.format(calendar.getTime()));
                        txkv_invdate_00.setValue(format2.format(inv_cal.getTime()));

                        txkv_order_00.setValue(detail.getString("order_id"));
                        txkv_discount_00.setValue("Rp. " +Parse.toCurrnecy(detail.getString("order_discount")));
                        txkv_payment_00.setValue(detail.getString("order_payment_type"));
                        txkv_note_00.setValue(detail.getString("order_note"));
                        txkv_status_00.setValue(detail.getString("order_status"));
                        txkv_total_00.setValue("Rp. " +Parse.toCurrnecy(detail.getString("total")));
                        txkv_total_00.setTag(detail.getString("total"));


                        JSONArray products = obj.getJSONArray("product");
                        StringBuilder sbProduct = new StringBuilder();
                        for (int i=0; i<products.length(); i++){
                            sbProduct.append(products.getJSONObject(i).getString("product_id"));
                            double price = Double.parseDouble(products.getJSONObject(i).getString("product_selling_price"));
                            int qty      = products.getJSONObject(i).getInt("product_qty");
                            double total = price * qty;

                            sbProduct.append(products.getJSONObject(i).getString("product_id")).append(",");
                            sbProduct.append(price).append(",");
                            sbProduct.append(qty).append(",");
                            sbProduct.append(total);
                            if (i < (products.length()-1)){
                                sbProduct.append(";");
                            }

                        }
                        product = sbProduct.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(InvoiceDetailActivity.this,message,Toast.LENGTH_LONG).show();
                    InvoiceDetailActivity.this.finish();
                }
            }
        });
    }
}