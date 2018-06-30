package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.unileverapp.component.ListItemOrder2;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView       imvw_back_00;
    private RelativeLayout  rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;
    private LinearLayout    lnly_cod_00;
    private LinearLayout    lnly_giro_00;
    private LinearLayout    lnly_transfer_00;
    private LinearLayout    lnly_credit_00;
    private LinearLayout    lnly_cash_00;
    private ImageView       imvw_check_01;
    private ImageView       imvw_check_02;
    private ImageView       imvw_check_03;
    private ImageView       imvw_check_04;
    private ImageView       imvw_check_05;
    private Button          bbtn_action_00;
    private String          mPayment = "";
    private ListItemOrder2  ls_orders_00;
    private EditText        edtx_note_00;

    private OrderDB         myorder = new OrderDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initLayout();
        initListener();

        myorder.getOrder(this, getIntent().getStringExtra("ORDER_ID"));
        bbtn_action_00.setTag("0");
    }

    private void initLayout() {
        imvw_back_00    = (ImageView)       findViewById(R.id.imvw_back_00);
        rvly_cart_00    = (RelativeLayout)  findViewById(R.id.rvly_cart_00);
        rvly_notif_00   = (RelativeLayout)  findViewById(R.id.rvly_notif_00);
        txvw_title_00   = (TextView)        findViewById(R.id.txvw_title_00);

        lnly_cod_00     = (LinearLayout)    findViewById(R.id.lnly_cod_00);
        lnly_giro_00    = (LinearLayout)    findViewById(R.id.lnly_giro_00);
        lnly_transfer_00 = (LinearLayout)   findViewById(R.id.lnly_transfer_00);
        lnly_credit_00  = (LinearLayout)    findViewById(R.id.lnly_credit_00);
        lnly_cash_00    = (LinearLayout)    findViewById(R.id.lnly_cash_00);

        imvw_check_01   = (ImageView)       findViewById(R.id.imvw_check_01);
        imvw_check_02   = (ImageView)       findViewById(R.id.imvw_check_02);
        imvw_check_03   = (ImageView)       findViewById(R.id.imvw_check_03);
        imvw_check_04   = (ImageView)       findViewById(R.id.imvw_check_04);
        imvw_check_05   = (ImageView)       findViewById(R.id.imvw_check_05);

        bbtn_action_00  = (Button)          findViewById(R.id.bbtn_action_00);

        ls_orders_00    = (ListItemOrder2)  findViewById(R.id.ls_orders_00);

        edtx_note_00    = (EditText)        findViewById(R.id.edtx_note_00);

        rvly_cart_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
        imvw_back_00.setVisibility(View.VISIBLE);
        txvw_title_00.setText(getResources().getString(R.string.continue_order));

        ls_orders_00.create(getIntent().getStringExtra("ORDER_ID"));
//        lnly_transfer_00.setVisibility(View.GONE);
//        lnly_credit_00.setVisibility(View.GONE);
        if (Double.parseDouble(ls_orders_00.getSubTotal()) < 500000 ){
            lnly_giro_00.setVisibility(View.GONE);
            lnly_transfer_00.setVisibility(View.GONE);
            lnly_credit_00.setVisibility(View.GONE);
            lnly_cash_00.setVisibility(View.GONE);
        }

    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lnly_cod_00.setOnClickListener(this);
        lnly_giro_00.setOnClickListener(this);
        lnly_transfer_00.setOnClickListener(this);
        lnly_credit_00.setOnClickListener(this);
        lnly_cash_00.setOnClickListener(this);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bbtn_action_00.getTag().equals("0")){
                    Toast.makeText(PaymentActivity.this, getResources().getString(R.string.please_chose_payment), Toast.LENGTH_SHORT).show();
                }
                else {
                    sendData();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentActivity.this, ChartActivity.class);
        PaymentActivity.this.startActivity(intent);
        PaymentActivity.this.finish();
    }

    @Override
    public void onClick(View view) {
        switchPayment(view);
    }

    private void switchPayment(View view){
        imvw_check_01.setImageResource(R.drawable.ic_check_disable);
        imvw_check_02.setImageResource(R.drawable.ic_check_disable);
        imvw_check_03.setImageResource(R.drawable.ic_check_disable);
        imvw_check_04.setImageResource(R.drawable.ic_check_disable);
        imvw_check_05.setImageResource(R.drawable.ic_check_disable);


        if (view == lnly_cod_00){
            mPayment = getResources().getString(R.string.cod);
            imvw_check_01.setImageResource(R.drawable.ic_check_enable);
        }
        else if (view == lnly_giro_00){
            mPayment = "Giro";
            imvw_check_02.setImageResource(R.drawable.ic_check_enable);
        }
        else if (view == lnly_transfer_00){
            mPayment = "Transfer Bank";
            imvw_check_03.setImageResource(R.drawable.ic_check_enable);
        }
        else if (view == lnly_credit_00){
            mPayment = getResources().getString(R.string.credit_card);
            imvw_check_04.setImageResource(R.drawable.ic_check_enable);
        }
        else if (view == lnly_cash_00){
            mPayment = getResources().getString(R.string.cash_payment);
            imvw_check_05.setImageResource(R.drawable.ic_check_enable);
        }
        bbtn_action_00.setTag("1");
        bbtn_action_00.setBackgroundResource(R.drawable.button_signin);
    }

    private void sendData(){
        myorder.order_pay_type = mPayment;
        myorder.order_notes    = edtx_note_00.getText().toString();

        PostManager post = new PostManager(PaymentActivity.this);
        post.setApiUrl("order/save");
        JSONObject send = new JSONObject();
        try {
            OutletDB outletDB = new OutletDB();
            outletDB.getMine(PaymentActivity.this);

            send.put("request_type","7");
            JSONObject data = new JSONObject();

            JSONObject order_info = new JSONObject();
            order_info.put("username", outletDB.username);
            order_info.put("outlet_id", outletDB.outlet_id);
            order_info.put("order_disc", myorder.order_disc);
            order_info.put("order_pay_type", myorder.order_pay_type);
            order_info.put("order_notes", myorder.order_notes);
            order_info.put("order_subtotal", ls_orders_00.getSubTotal());
            order_info.put("order_total", ls_orders_00.getTotal());
            data.put("order_info",order_info);

            JSONArray product_info = new JSONArray(myorder.order_detail);
            data.put("order_detail", product_info);

            send.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(send);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    OrderDB orderDB = new OrderDB();
                    orderDB.deleteData(PaymentActivity.this,"-99");
                    orderDB.order_detail    = myorder.order_detail;
                    orderDB.order_pay_type  = myorder.order_pay_type;
                    orderDB.order_notes     = myorder.order_notes;
                    try {
                        JSONObject data         = obj.getJSONObject("data");
                        orderDB.id              = data.getString("order_id");
                        orderDB.outlet_credit_limit  = data.getString("outlet_credit_limit");
                        orderDB.order_total     = data.getString("order_total");
                        orderDB.order_subtotal  = data.getString("order_subtotal");
                        orderDB.order_disc      = data.getDouble("order_disc");
                        orderDB.order_notes     = data.getString("order_notes");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    orderDB.insert(PaymentActivity.this);

                    ChartDB chartDB = new ChartDB();
                    chartDB.clearData(PaymentActivity.this);

                    Intent intent = new Intent(PaymentActivity.this, SummaryOrderActivity.class);
                    intent.putExtra("ORDER_ID", orderDB.id);
                    startActivityForResult(intent, 1);
                    PaymentActivity.this.finish();

                    Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
