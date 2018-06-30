package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.unileverapp.R;

public class TextKeyValue extends RelativeLayout {

    public TextKeyValue(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_text_keyvalue,this,true);
        initLayout();
    }

    private TextView txvw_key_00 ;
    private TextView txvw_value_00 ;
    private void initLayout(){
        txvw_key_00     = (TextView) findViewById(R.id.txvw_key_00);
        txvw_value_00   = (TextView) findViewById(R.id.txvw_value_00);
    }

    public void setMarginDiv(int x){
        LayoutParams lp = new LayoutParams(x,LayoutParams.WRAP_CONTENT);
        txvw_key_00.setLayoutParams(lp);
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
}
