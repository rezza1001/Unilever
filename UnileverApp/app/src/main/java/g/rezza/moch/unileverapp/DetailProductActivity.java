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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.lib.Parse;

public class DetailProductActivity extends AppCompatActivity {
    private static final  String TAG = "DetailProductActivity";

    private ImageView   imvw_back_00;
    private ImageView   imvw_image_00;
    private TextView    txvw_title_00;
    private TextView    txvw_name_00;
    private TextView    txvw_price_00;
    private TextView    txvw_brandname_00;
    private TextView    txvw_category_00;
    private ProductDB   productDB;
    private Button      bbtn_action_00;
    private TextView    txvw_counter_00;
    private RelativeLayout rvly_cart_counter_00;
    private RelativeLayout rvly_cart_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout(){
        imvw_back_00        = (ImageView)   findViewById(R.id.imvw_back_00);
        imvw_image_00       = (ImageView)   findViewById(R.id.imvw_image_00);
        txvw_title_00       = (TextView)    findViewById(R.id.txvw_title_00);
        txvw_name_00        = (TextView)    findViewById(R.id.txvw_name_00);
        txvw_price_00       = (TextView)    findViewById(R.id.txvw_price_00);
        txvw_brandname_00   = (TextView)    findViewById(R.id.txvw_brandname_00);
        txvw_category_00    = (TextView)    findViewById(R.id.txvw_category_00);
        bbtn_action_00      = (Button)      findViewById(R.id.bbtn_action_00);
        txvw_counter_00     = (TextView)    findViewById(R.id.txvw_counter_00);
        rvly_cart_counter_00= (RelativeLayout) findViewById(R.id.rvly_cart_counter_00);
        rvly_cart_00        = (RelativeLayout) findViewById(R.id.rvly_cart_00);

        txvw_name_00.bringToFront();
        rvly_cart_counter_00.bringToFront();
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
                ChartDB chartDB = new ChartDB();
                chartDB.getProduct(DetailProductActivity.this, productDB.id);
                Log.d(TAG, "chartDB "+ chartDB.product_id);
                if (chartDB.product_id.isEmpty()){
                    chartDB.product_id = productDB.id;
                    chartDB.insert(DetailProductActivity.this);
                }
                Intent intent = new Intent(DetailProductActivity.this, ChartActivity.class);
                intent.putExtra("PRODUCTID", productDB.id);
                DetailProductActivity.this.startActivity(intent);
                DetailProductActivity.this.finish();
            }
        });
        rvly_cart_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProductActivity.this, ChartActivity.class);
                startActivity(intent);
                DetailProductActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("BRAND_ID", productDB.brand_id);
        intent.putExtra("BRAND", productDB.brand);
        this.startActivity(intent);
        this.finish();
    }

    private void initData(){
        productDB = new ProductDB();
        productDB.getProduct(this, getIntent().getStringExtra("PRODUCTID"));
        txvw_title_00.setText(productDB.name);
        txvw_name_00.setText(productDB.name);
        txvw_brandname_00.setText(productDB.brand);
        txvw_category_00.setText(productDB.category_l1);
        Glide.with(this).load(productDB.photo_link+""+ productDB.photo).into(imvw_image_00);
        txvw_price_00.setText("Rp. "+ Parse.toCurrnecy(productDB.selling_price));

        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> carts = chartDB.getProducts(this);
        txvw_counter_00.setText(""+carts.size());
    }
}
