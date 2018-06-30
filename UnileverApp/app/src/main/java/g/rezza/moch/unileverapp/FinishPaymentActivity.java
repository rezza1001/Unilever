package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.unileverapp.database.OutletDB;

public class FinishPaymentActivity extends AppCompatActivity {

    private Button    bbtn_action_00;
    private ImageView imvw_back_00;
    private RelativeLayout rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;
    private TextView        txvw_thnks_00;

    OutletDB outletDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_payment);

        imvw_back_00    = (ImageView)       findViewById(R.id.imvw_back_00);
        rvly_cart_00    = (RelativeLayout)  findViewById(R.id.rvly_cart_00);
        rvly_notif_00   = (RelativeLayout)  findViewById(R.id.rvly_notif_00);
        txvw_title_00   = (TextView)        findViewById(R.id.txvw_title_00);
        txvw_thnks_00   = (TextView)        findViewById(R.id.txvw_thnks_00);
        bbtn_action_00  = (Button) findViewById(R.id.bbtn_action_00);

        outletDB = new OutletDB();
        outletDB.getMine(this);

        rvly_cart_00.setVisibility(View.GONE);
        imvw_back_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
        txvw_title_00.setText("Pembayaran Selesai");
        txvw_thnks_00.setText("Terima kasih "+outletDB.outlet_name+", anda telah melakukan pembayaran via "+getIntent().getStringExtra("PAYMENT")+" dengan status pembayaran belum dibayar, pesanan anda akan segera diproses.");

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinishPaymentActivity.this.finish();
            }
        });
    }
}
