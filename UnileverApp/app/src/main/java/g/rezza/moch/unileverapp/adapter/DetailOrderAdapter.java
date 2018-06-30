package g.rezza.moch.unileverapp.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.lib.Parse;

/**
 * Created by rezza on 28/02/18.
 */

public class DetailOrderAdapter extends RelativeLayout {
    private static final String TAG = "DetailOrderAdapter";

    private TextView txvw_title_00;
    private TextView txvw_price_00;
    private TextView txvw_qty_00;
    private TextView txvw_total_00;

    private String price    = "0";
    private String qty      = "0";

    public DetailOrderAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_detail_order, this, true);

        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        txvw_price_00   = (TextView) findViewById(R.id.txvw_price_00);
        txvw_qty_00     = (TextView) findViewById(R.id.txvw_qty_00);
        txvw_total_00   = (TextView) findViewById(R.id.txvw_total_00);
    }

    public void create( String title, String price,int qty){
        this.price = price;
        this.qty   = qty +"";
        txvw_title_00.setText(title);
        txvw_price_00.setText("Rp. "+ Parse.toCurrnecy(price));
        txvw_qty_00.setText(qty+"X ");

        double total = Double.parseDouble(price) * qty;
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total));

    }

    public String getPrice(){
        return price;
    }

    public String getQty() {
        return qty;
    }

    private OnCancelListener mListener;
    public void setOnCancelListener(OnCancelListener onCancelListener){
        mListener = onCancelListener;
    }
    public interface OnCancelListener{
        public void onCancel(String id, View v);
    }
}
