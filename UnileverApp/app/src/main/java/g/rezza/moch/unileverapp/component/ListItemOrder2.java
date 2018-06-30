package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.ChartActivity;
import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.adapter.DetailOrderAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.KeyValueHolder;
import g.rezza.moch.unileverapp.holder.MyOrderHolder;
import g.rezza.moch.unileverapp.holder.OrderPayment;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class ListItemOrder2 extends RelativeLayout {

    private OrderDB orderDB;

    public ListItemOrder2(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_listitem_order_2,this,true);
        initLayout();
    }


    private LinearLayout lnly_body_00;
    private TextView     txvw_total_00;
//    private TextView     txvw_ullage_00;
    private TextView     txvw_ppn_00;
    private TextView     txvw_totalpay_00;
    private DiscountView dscv_00;
    private void initLayout(){
        lnly_body_00    = (LinearLayout) findViewById(R.id.lnly_body_00);
        txvw_total_00   = (TextView) findViewById(R.id.txvw_total_00);
//        txvw_ullage_00  = (TextView) findViewById(R.id.txvw_ullage_00);
        txvw_ppn_00     = (TextView) findViewById(R.id.txvw_ppn_00);
        txvw_totalpay_00= (TextView) findViewById(R.id.txvw_totalpay_00);
        dscv_00         = (DiscountView) findViewById(R.id.dscv_00);

    }


    public void create(String orderid){
        double total = 0 ;

        lnly_body_00.removeAllViews();
        orderDB = new OrderDB();
        orderDB.getOrder(getContext(), orderid);
        JSONArray data_detail = null;
        try {
            data_detail = new JSONArray(orderDB.order_detail);
            for (int i=0; i<data_detail.length(); i++){
                JSONObject data     = data_detail.getJSONObject(i);
                ProductDB productDB = new ProductDB();
                productDB.getProduct(getContext(),data.getString("product_id"));
                int qty = Integer.parseInt(data.getString("product_qty"));

                DetailOrderAdapter detail = new DetailOrderAdapter(getContext(), null);
                detail.create(productDB.name, productDB.selling_price+"", qty);
                lnly_body_00.addView(detail);

                double price = Double.parseDouble(productDB.selling_price+"") ;
                total = total+ (price * qty);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total));
        txvw_total_00.setTag(total);
        check_discount(total);

    }

    private void calculate(double total){
//        txvw_ullage_00.setText("- Rp. 0");

        double ullage   = dscv_00.getValue();
        double ppn      = 0;
        double total_pay= 0;

        total_pay = (total - ullage);
        Log.d("LIST ORDER", "Total before PPN "+ total_pay);
        Log.d("LIST ORDER", "Total Discount "+ ullage);
        ppn = (0.1 * total_pay);
        total_pay = total_pay + ppn;

//        txvw_ullage_00.setText("- Rp. "+ Parse.toCurrnecy(ullage));
        txvw_ppn_00.setText("Rp. "+ Parse.toCurrnecy(ppn));
        txvw_totalpay_00.setText("Rp. "+ Parse.toCurrnecy(total_pay));
        txvw_totalpay_00.setTag(total_pay);
    }

    public String getSubTotal(){
       return txvw_total_00.getTag().toString();
    }

    public String getTotal(){
        return txvw_totalpay_00.getTag().toString();
    }

    private void check_discount(final double total){
        PostManager post = new PostManager(getContext());
        post.setApiUrl("Order/discount_check/orderSubTotal/"+total);
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    try {
                        JSONArray data = obj.getJSONArray("data");
                        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
                        for (int i=0; i<data.length(); i++){
                            String disc = data.getJSONObject(i).getString("discount_calc");
                            String discount_name = data.getJSONObject(i).getString("discount_name");
                            kvs.add(new KeyValueHolder(discount_name, disc));
                        }
                        dscv_00.create(kvs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

                calculate(total);
            }
        });
    }
}
