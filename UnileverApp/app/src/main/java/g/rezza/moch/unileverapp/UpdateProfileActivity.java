package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class UpdateProfileActivity extends AppCompatActivity {


    private ImageView       imvw_back_00;
    private RelativeLayout  rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;
    private EditText        edtx_contact_00;
    private EditText        edtx_address_00;
    private EditText        edtx_city_00;
    private EditText        edtx_phone_00;
    private EditText        edtx_email_00;
    private EditText        edtx_npwp_00;
    private EditText        edtx_npwp_address_00;
    private EditText        edtx_nik_00;
    private Button          bbtn_action_00;

    private OutletDB        outletDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        outletDB = new OutletDB();
        outletDB.getMine(this);

        initLayout();
        initListener();
        initData();
    }

    private void initLayout(){
        imvw_back_00        = (ImageView)   findViewById(R.id.imvw_back_00);
        rvly_notif_00       = (RelativeLayout)   findViewById(R.id.rvly_notif_00);
        rvly_cart_00        = (RelativeLayout) findViewById(R.id.rvly_cart_00);
        txvw_title_00       = (TextView)    findViewById(R.id.txvw_title_00);
        edtx_contact_00     = (EditText)    findViewById(R.id.edtx_contact_00);
        edtx_address_00     = (EditText)    findViewById(R.id.edtx_address_00);
        edtx_city_00        = (EditText)    findViewById(R.id.edtx_city_00);
        edtx_phone_00       = (EditText)    findViewById(R.id.edtx_phone_00);
        edtx_email_00       = (EditText)    findViewById(R.id.edtx_email_00);
        edtx_npwp_00        = (EditText)    findViewById(R.id.edtx_npwp_00);
        edtx_nik_00         = (EditText)    findViewById(R.id.edtx_nik_00);
        edtx_npwp_address_00= (EditText)    findViewById(R.id.edtx_npwp_address_00);
        bbtn_action_00      = (Button)      findViewById(R.id.bbtn_action_00);

        rvly_notif_00.setVisibility(View.GONE);
        rvly_cart_00.setVisibility(View.GONE);
        txvw_title_00.setText(getResources().getString(R.string.change_password));
        edtx_email_00.setVisibility(View.GONE);
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

    private void initData(){
        edtx_contact_00.setText(Parse.getString(outletDB.outlet_contact));
        edtx_address_00.setText(Parse.getString(outletDB.outlet_address));
        edtx_city_00.setText(Parse.getString(outletDB.outlet_city));
        edtx_phone_00.setText(Parse.getString(outletDB.outlet_phone));
        edtx_email_00.setText(Parse.getString(outletDB.outlet_email));
        edtx_npwp_00.setText(Parse.getString(outletDB.outlet_npwp));
        edtx_nik_00.setText(Parse.getString(outletDB.outlet_nik));
        edtx_npwp_address_00.setText(Parse.getString(outletDB.outlet_npwp_addr));
    }

    private void save(){
        String contact  = edtx_contact_00.getText().toString();
        String address  = edtx_address_00.getText().toString();
        String city     = edtx_city_00.getText().toString();
        String phone    = edtx_phone_00.getText().toString();
        String email    = edtx_email_00.getText().toString();
        String npwp     = edtx_npwp_00.getText().toString();
        String nik      = edtx_nik_00.getText().toString();
        String npwp_address      = edtx_npwp_address_00.getText().toString();

        if (contact.isEmpty()){
            Toast.makeText(UpdateProfileActivity.this, getResources().getString(R.string.contact)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (address.isEmpty()){
            Toast.makeText(UpdateProfileActivity.this, getResources().getString(R.string.address)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (city.isEmpty()){
            Toast.makeText(UpdateProfileActivity.this, getResources().getString(R.string.city)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (phone.isEmpty()){
            Toast.makeText(UpdateProfileActivity.this, getResources().getString(R.string.phone)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.isEmpty()){
            Toast.makeText(UpdateProfileActivity.this, getResources().getString(R.string.email)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (npwp.isEmpty()){
            Toast.makeText(UpdateProfileActivity.this, getResources().getString(R.string.npwp)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        outletDB.outlet_contact = contact;
        outletDB.outlet_address = address;
        outletDB.outlet_city    = city;
        outletDB.outlet_phone   = phone;
        outletDB.outlet_email   = email;
        outletDB.outlet_npwp    = npwp;
        outletDB.outlet_npwp_addr = npwp_address;
        outletDB.outlet_nik     = nik;

        PostManager post = new PostManager(this);
        post.setApiUrl("Profile/update_profile");
        JSONObject send = new JSONObject();

        try {
            send.put("request_type","17");
            JSONObject data = new JSONObject();

            JSONObject user_info = new JSONObject();
            user_info.put("username",outletDB.username);
            user_info.put("outlet_id", outletDB.outlet_id);
            data.put("user_info",user_info);

            JSONObject outlet_info = new JSONObject();
            outlet_info.put("outlet_contact",contact);
            outlet_info.put("outlet_address",address);
            outlet_info.put("outlet_city",city);
            outlet_info.put("outlet_phone",phone);
            outlet_info.put("outlet_email",email);
            outlet_info.put("outlet_npwp",npwp);
            outlet_info.put("outlet_photo",""); //?
            outlet_info.put("outlet_nik",nik); //?
            outlet_info.put("outlet_npwp",npwp); //?
            outlet_info.put("outlet_npwp_address",npwp_address); //?
            data.put("outlet_info", outlet_info);

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
                    outletDB.clearData(UpdateProfileActivity.this);
                    outletDB.insert(UpdateProfileActivity.this);
                    Toast.makeText(UpdateProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(UpdateProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
