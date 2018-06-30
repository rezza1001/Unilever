package g.rezza.moch.unileverapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_CROP = 2;

    private CircleImageView imvw_profile_00;
    private ImageView       imvw_back_00;
    private EditText        edtx_name_00;
    private EditText        edtx_contact_00;
    private EditText        edtx_address_00;
    private EditText        edtx_city_00;
    private EditText        edtx_phone_00;
    private EditText        edtx_email_00;
    private EditText        edtx_term_00;
    private EditText        edtx_username_00;
    private EditText        edtx_password_00;
    private EditText        edtx_cpassword_00;
    private EditText        edtx_npwp_00;
    private EditText        edtx_npwp_address_00;
    private EditText        edtx_nik_00;
    private Button          bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        initListener();
    }

    private void initUI(){
        imvw_profile_00     = (CircleImageView) findViewById(R.id.imvw_profile_00);
        edtx_name_00        = (EditText)    findViewById(R.id.edtx_name_00);
        edtx_contact_00     = (EditText)    findViewById(R.id.edtx_contact_00);
        edtx_address_00     = (EditText)    findViewById(R.id.edtx_address_00);
        edtx_city_00        = (EditText)    findViewById(R.id.edtx_city_00);
        edtx_phone_00       = (EditText)    findViewById(R.id.edtx_phone_00);
        edtx_email_00       = (EditText)    findViewById(R.id.edtx_email_00);
        edtx_term_00        = (EditText)    findViewById(R.id.edtx_term_00);
        edtx_username_00    = (EditText)    findViewById(R.id.edtx_username_00);
        edtx_password_00    = (EditText)    findViewById(R.id.edtx_password_00);
        edtx_cpassword_00   = (EditText)    findViewById(R.id.edtx_cpassword_00);
        edtx_npwp_00        = (EditText)    findViewById(R.id.edtx_npwp_00);
        edtx_nik_00         = (EditText)    findViewById(R.id.edtx_nik_00);
        edtx_npwp_address_00= (EditText)    findViewById(R.id.edtx_npwp_address_00);
        bbtn_action_00      = (Button)      findViewById(R.id.bbtn_action_00);
        imvw_back_00        = (ImageView)   findViewById(R.id.imvw_back_00);
    }

    private void initListener(){
        imvw_profile_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGalery();
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getImageFromGalery(){
        Intent imageGallery = new Intent(Intent.ACTION_PICK);
        imageGallery.setType("image/*");
        startActivityForResult(imageGallery, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imvw_profile_00.setImageBitmap(selectedImage);

                File f = new File(imageUri.getPath());

                Log.d(TAG,f.getPath()+" | "+ f.getName());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(RegisterActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void sendData(){
        String name     = edtx_name_00.getText().toString();
        String contact  = edtx_contact_00.getText().toString();
        String address  = edtx_address_00.getText().toString();
        String city     = edtx_city_00.getText().toString();
        String phone    = edtx_phone_00.getText().toString();
        String email    = edtx_email_00.getText().toString();
        String term     = edtx_term_00.getText().toString();
        String username = edtx_username_00.getText().toString();
        String password = edtx_password_00.getText().toString();
        String confirm  = edtx_cpassword_00.getText().toString();
        String npwp     = edtx_npwp_00.getText().toString();
        String nik      = edtx_nik_00.getText().toString();
        String npwp_address      = edtx_npwp_address_00.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.name)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (contact.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.contact)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (address.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.address)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (city.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.city)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (phone.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.phone)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.email)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (npwp.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.npwp)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (username.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.username)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.isEmpty()){
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.password)+ " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.equals(confirm)){
            Toast.makeText(RegisterActivity.this, "Invalid " +getResources().getString(R.string.confirm_password), Toast.LENGTH_SHORT).show();
            return;
        }

        PostManager post = new PostManager(this);
        post.setApiUrl("authenticate/register");
        JSONObject send = new JSONObject();

        try {
            send.put("request_type","4");
            JSONObject data = new JSONObject();

            JSONObject user_info = new JSONObject();
            user_info.put("username",username);
            user_info.put("password", password);
            user_info.put("user_group", "Outlet");
            user_info.put("updated_by", "Mobile Apps Android");
            data.put("user_info",user_info);

            JSONObject outlet_info = new JSONObject();
            outlet_info.put("outlet_name",name);
            outlet_info.put("outlet_contact",contact);
            outlet_info.put("outlet_address",address);
            outlet_info.put("outlet_city",city);
            outlet_info.put("outlet_phone",phone);
            outlet_info.put("outlet_email",email);
            outlet_info.put("outlet_npwp",npwp);
            outlet_info.put("outlet_photo",""); //?
            outlet_info.put("outlet_term_of_payment",term);
            outlet_info.put("outlet_credit_limit","3000000"); //?
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
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
