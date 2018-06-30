package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.msebera.android.httpclient.util.EncodingUtils;
import g.rezza.moch.unileverapp.Static.App;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.lib.Parse;

public class WebViewActivity extends AppCompatActivity {

    private WebView         wbvw_payment_00;
    private ImageView       imvw_back_00;
    private RelativeLayout  rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        wbvw_payment_00 = (WebView) findViewById(R.id.wbvw_payment_00);
        imvw_back_00    = (ImageView) findViewById(R.id.imvw_back_00);
        rvly_cart_00    = (RelativeLayout) findViewById(R.id.rvly_cart_00);
        rvly_notif_00   = (RelativeLayout) findViewById(R.id.rvly_notif_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);

        rvly_cart_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
        txvw_title_00.setText(getResources().getString(R.string.payment_method));

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String transid  = getIntent().getStringExtra("invoice_id");
        String products = getIntent().getStringExtra("products");
        double total    = Double.parseDouble(getIntent().getStringExtra("total"));
        int payment     = getIntent().getIntExtra("payment",36);
        total = Parse.round(total, 2);
        sendToDoku(transid, total, payment,products);

    }

    private void sendToDoku(String transid, double total, int payment, String products){
        OutletDB outletDB = new OutletDB();
        outletDB.getMine(this);
        if (outletDB.outlet_email== null){
            outletDB.outlet_email = "emailtest@gmail.com";
        }
        wbvw_payment_00.getSettings().setLoadsImagesAutomatically(true);
        wbvw_payment_00.getSettings().setJavaScriptEnabled(true);
        wbvw_payment_00.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        StringBuilder sb = new StringBuilder();
        sb.append("submit=1").append("&");
        sb.append("BASKET=").append(products).append("&");
        sb.append("MALLID=").append(App.DOKU_MALLID).append("&");
        sb.append("CHAINMERCHANT=NA").append("&");
        sb.append("CURRENCY=360").append("&");
        sb.append("PURCHASECURRENCY=360").append("&");
        sb.append("AMOUNT=").append(total).append("&");
        sb.append("PURCHASEAMOUNT=").append(total).append("&");
        sb.append("TRANSIDMERCHANT="+transid).append("&");
        sb.append("SHAREDKEY=").append(App.SHAREDKEY).append("&");
        sb.append("PAYMENTCHANNEL=").append(payment).append("&");
        sb.append("WORDS=").append(App.getWords(total,transid)).append("&");
        sb.append("REQUESTDATETIME=").append(App.getSession()).append("&");
        sb.append("SESSIONID=").append(App.getSession()).append("&");
        sb.append("EMAIL=").append(outletDB.outlet_email).append("&");
        sb.append("NAME=").append(outletDB.outlet_name).append("&");
        sb.append("MOBILEPHONE=").append(outletDB.outlet_phone);

        Log.d("WEBVIEW", sb.toString());
        wbvw_payment_00.postUrl(App.DOKU_URL_RECEIVE,
                EncodingUtils.getBytes(sb.toString(), "BASE64"));
    }



    @Override
    public void onBackPressed() {
        this.finish();
    }
}
