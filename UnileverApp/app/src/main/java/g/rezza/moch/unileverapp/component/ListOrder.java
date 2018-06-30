package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.holder.KeyValueHolder;
import g.rezza.moch.unileverapp.holder.OrderPayment;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class ListOrder extends RelativeLayout {

    private ArrayList<OrderPayment> mData = new ArrayList<>();

    public ListOrder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_listorder,this,true);
        initLayout();
    }

    private LinearLayout lnly_body_00;
    private TextView    txvw_total_00;
    private TextView    txvw_disc_00;
    private TextView    txvw_disc_ammount_00;
    private TextView    txvw_subtotal_00;
    private TextView    txvw_ppn_00;
    private TextView    txvw_totalbuy_00;
//    private TextView    txvw_ullage_00;
    private DiscountView dscv_00;
    private void initLayout(){
        lnly_body_00            = (LinearLayout)    findViewById(R.id.lnly_body_00);
        txvw_total_00           = (TextView)        findViewById(R.id.txvw_total_00);
        txvw_disc_00            = (TextView)        findViewById(R.id.txvw_disc_00);
        txvw_disc_ammount_00    = (TextView)        findViewById(R.id.txvw_disc_ammount_00);
        txvw_subtotal_00        = (TextView)        findViewById(R.id.txvw_subtotal_00);
        txvw_ppn_00             = (TextView)        findViewById(R.id.txvw_ppn_00);
        txvw_totalbuy_00        = (TextView)        findViewById(R.id.txvw_totalbuy_00);
//        txvw_ullage_00          = (TextView)        findViewById(R.id.txvw_ullage_00);
        dscv_00                 = (DiscountView)    findViewById(R.id.dscv_00);
    }

    public void create(ArrayList<OrderPayment> data){
        mData = data;
        lnly_body_00.removeAllViews();
        double total = 0;
        for (OrderPayment order: mData){
            ListItemOrder list = new ListItemOrder(getContext(), null);
            list.create(order);
            lnly_body_00.addView(list);
            total = total+ (order.price * order.qty);
        }
        check_discount(total);

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

                txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total));
                txvw_disc_00.setText("Discount 0 %");
                txvw_disc_ammount_00.setText("Rp. 0");
                txvw_subtotal_00.setText("Rp. "+ Parse.toCurrnecy(total) );
                double final_amount = (total - dscv_00.getValue());
                double ppn = 0.1 * final_amount;
                final_amount = final_amount + ppn;

                txvw_ppn_00.setText("Rp. "+Parse.toCurrnecy(ppn) );
                txvw_totalbuy_00.setText("Rp. "+ Parse.toCurrnecy(final_amount));

            }
        });
    }

    public void create(ArrayList<OrderPayment> data, ArrayList<KeyValueHolder> discount){
        mData = data;
        lnly_body_00.removeAllViews();
        double total = 0;
        for (OrderPayment order: mData){
            ListItemOrder list = new ListItemOrder(getContext(), null);
            list.create(order);
            lnly_body_00.addView(list);
            total = total+ (order.price * order.qty);
        }
        dscv_00.create(discount);
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total));
        txvw_disc_00.setText("Discount 0 %");
        txvw_disc_ammount_00.setText("Rp. 0");
        txvw_subtotal_00.setText("Rp. "+ Parse.toCurrnecy(total) );
        double final_amount = (total - dscv_00.getValue());
        double ppn = 0.1 * final_amount;
        final_amount = final_amount + ppn;


        txvw_ppn_00.setText("Rp. "+Parse.toCurrnecy(ppn) );
        txvw_totalbuy_00.setText("Rp. "+ Parse.toCurrnecy(final_amount));
    }
}
