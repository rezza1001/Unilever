package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.lib.ErrorCode;

public class ChangePwdActivity extends AppCompatActivity {

    private ImageView   imvw_back_00;
    private RelativeLayout rvly_cart_00;
    private RelativeLayout rvly_notif_00;
    private TextView    txvw_title_00;
    private Button      bbtn_action_00;
    private EditText    edtx_old_00;
    private EditText    edtx_new_00;
    private EditText    edtx_confirm_00;
    private OutletDB    outlet ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        outlet = new OutletDB();
        outlet.getMine(this);

        initLayout();
        initListener();
    }

    private void initLayout(){
        imvw_back_00        = (ImageView)   findViewById(R.id.imvw_back_00);
        rvly_notif_00       = (RelativeLayout)   findViewById(R.id.rvly_notif_00);
        rvly_cart_00        = (RelativeLayout) findViewById(R.id.rvly_cart_00);
        txvw_title_00       = (TextView)    findViewById(R.id.txvw_title_00);
        bbtn_action_00      = (Button)      findViewById(R.id.bbtn_action_00);
        edtx_old_00         = (EditText)    findViewById(R.id.edtx_old_00);
        edtx_new_00         = (EditText)    findViewById(R.id.edtx_new_00);
        edtx_confirm_00     = (EditText)    findViewById(R.id.edtx_confirm_00);

        rvly_notif_00.setVisibility(View.GONE);
        rvly_cart_00.setVisibility(View.GONE);
        txvw_title_00.setText(getResources().getString(R.string.change_password));
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
                save();
            }
        });
    }

    private void save(){
        String password = edtx_old_00.getText().toString();
        String new_pwd  = edtx_new_00.getText().toString();
        String confirm  = edtx_confirm_00.getText().toString();

        if (password.isEmpty() || !outlet.password.equals(password)){
            Toast.makeText(this,getResources().getString(R.string.alert_empty_password) ,Toast.LENGTH_LONG).show();
            return;
        }
        else if (new_pwd.isEmpty()){
            Toast.makeText(this,getResources().getString(R.string.alert_new_password_rq) ,Toast.LENGTH_LONG).show();
            return;
        }
        else if (confirm.isEmpty()){
            Toast.makeText(this,getResources().getString(R.string.alert_confirm_pwd) ,Toast.LENGTH_LONG).show();
            return;
        }

        PostManager post = new PostManager(this);
        post.setApiUrl("Profile/update_password");
        JSONObject send = new JSONObject();
        try {
            send.put("request_type","4");
            JSONObject data = new JSONObject();
            JSONObject user_info = new JSONObject();
            user_info.put("username",outlet.username);
            user_info.put("outlet_id", outlet.outlet_id);
            user_info.put("password_old", password);
            user_info.put("password_new", new_pwd);
            data.put("user_info", user_info);
            send.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(send);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    Toast.makeText(ChangePwdActivity.this, message, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(ChangePwdActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
