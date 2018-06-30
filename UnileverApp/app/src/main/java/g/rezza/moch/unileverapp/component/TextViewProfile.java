package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.unileverapp.R;


/**
 * Created by rezza on 02/01/18.
 */

public class TextViewProfile extends RelativeLayout {

    private TextView txvw_title_email_00;
    private TextView txvw_email_00;
    private CircleImageView imvw_account_00;

    public TextViewProfile(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_texview_standard, this, true);
        createLayout();
    }

    private void createLayout(){
        txvw_title_email_00 = (TextView)        findViewById(R.id.txvw_title_email_00);
        txvw_email_00       = (TextView)        findViewById(R.id.txvw_email_00);
        imvw_account_00     = (CircleImageView) findViewById(R.id.circleImageView);
    }

    public void setImage(int resource){
        imvw_account_00.setImageResource(resource);
    }

    public void setTitle(String title){
        txvw_title_email_00.setText(title);
    }
    public void setValue(String value){
        txvw_email_00.setText(value);
    }
    public String getValue(){
        return txvw_email_00.getText().toString();
    }

}
