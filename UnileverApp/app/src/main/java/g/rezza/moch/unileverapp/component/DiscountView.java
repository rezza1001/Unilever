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

public class DiscountView extends RelativeLayout {

    private LinearLayout    lnly_1;
    private LinearLayout    lnly_2;
    private LinearLayout    lnly_3;
    private TextView        txvw_disc_1;
    private TextView        txvw_disc_2;
    private TextView        txvw_disc_3;
    private TextView        txvw_key_1;
    private TextView        txvw_key_2;
    private TextView        txvw_key_3;
    private ArrayList<LinearLayout> mLayout = new ArrayList<>();
    private ArrayList<TextView> mValue = new ArrayList<>();
    private ArrayList<TextView> mKey = new ArrayList<>();

    private Double total = 0.0;
    public DiscountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_discount,this,true);
        initLayout();
    }

    private void initLayout(){
        lnly_1     = (LinearLayout) findViewById(R.id.lnly_1);
        lnly_2     = (LinearLayout) findViewById(R.id.lnly_2);
        lnly_3     = (LinearLayout) findViewById(R.id.lnly_3);
        txvw_disc_1     = (TextView) findViewById(R.id.txvw_disc_1);
        txvw_disc_2     = (TextView) findViewById(R.id.txvw_disc_2);
        txvw_disc_3     = (TextView) findViewById(R.id.txvw_disc_3);
        txvw_key_1     = (TextView) findViewById(R.id.txvw_key_1);
        txvw_key_2     = (TextView) findViewById(R.id.txvw_key_2);
        txvw_key_3     = (TextView) findViewById(R.id.txvw_key_3);

        lnly_1.setVisibility(GONE);
        lnly_2.setVisibility(GONE);
        lnly_3.setVisibility(GONE);

        mLayout.add(lnly_1);
        mLayout.add(lnly_3);
        mLayout.add(lnly_3);

        mValue.add(txvw_disc_1);
        mValue.add(txvw_disc_2);
        mValue.add(txvw_disc_3);

        mKey.add(txvw_key_1);
        mKey.add(txvw_key_2);
        mKey.add(txvw_key_3);
    }


    public Double getValue(){
       return total;
    }

    public void create(ArrayList<KeyValueHolder> pKvs){
       for (int i=0; i<pKvs.size(); i++){
           mLayout.get(i).setVisibility(VISIBLE);
           mKey.get(i).setText(pKvs.get(i).key);
           mValue.get(i).setText("- Rp. "+Parse.toCurrnecy(pKvs.get(i).value));
           total = total + Double.parseDouble(pKvs.get(i).value);
       }
    }
}
