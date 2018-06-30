package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.holder.OrderPayment;
import g.rezza.moch.unileverapp.lib.Parse;

public class ListItemOrder extends RelativeLayout {

    public ListItemOrder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_listitem_order,this,true);
        initLayout();
    }

    private TextView txvw_key_00 ;
    private TextView txvw_value_00 ;
    private TextView txvw_qty_00 ;
    private TextView txvw_total_00 ;
    private void initLayout(){
        txvw_key_00     = (TextView) findViewById(R.id.txvw_key_00);
        txvw_value_00   = (TextView) findViewById(R.id.txvw_value_00);
        txvw_qty_00     = (TextView) findViewById(R.id.txvw_qty_00);
        txvw_total_00   = (TextView) findViewById(R.id.txvw_total_00);
    }

    public void setKey(String key){
        txvw_key_00.setText(key);
    }

    public void setValue(String value){
        txvw_value_00.setText(value.trim());
    }

    public String getValue(){
       return txvw_value_00.getText().toString();
    }

    public void create(OrderPayment data){
        txvw_qty_00.setText(data.qty+" X ");
        txvw_key_00.setText(""+data.description);
        txvw_value_00.setText("Rp. "+Parse.toCurrnecy(data.price));

        double total = data.price * data.qty;
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total));
    }
}
