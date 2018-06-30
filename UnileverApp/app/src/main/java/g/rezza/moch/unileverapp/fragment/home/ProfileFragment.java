package g.rezza.moch.unileverapp.fragment.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.unileverapp.ChangePwdActivity;
import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.SignInActivity;
import g.rezza.moch.unileverapp.UpdateProfileActivity;
import g.rezza.moch.unileverapp.component.TextViewProfile;
import g.rezza.moch.unileverapp.connection.ImageUploader;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.holder.KeyValueHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.LoadingScreen;
import g.rezza.moch.unileverapp.lib.Parse;

/**
 * Created by rezza on 09/02/18.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int RESULT_LOAD_IMG = 64;

    private TextView txvw_name_00;
    private TextView txvw_change_00;
    private CircleImageView imvw_account_00;
    private TextViewProfile txpf_outletid_00;
    private TextViewProfile txpf_username_00;
    private TextViewProfile txpf_o_address_00;
    private TextViewProfile txpf_o_city_00;
    private TextViewProfile txpf_o_phone_00;
    private TextViewProfile txpf_o_email_00;
    private TextViewProfile txpf_o_npwp_00;
    private TextViewProfile txpf_o_npwpaddr_00;
    private TextViewProfile txpf_o_nik_00;
    private RelativeLayout  rvly_logout_00;
    private RelativeLayout  rvly_changepwd_00;
    private RelativeLayout  rvly_foto_00;
    private LoadingScreen   loadingScreen;
    private OutletDB outletDB;

    public static Fragment newInstance() {
        Fragment frag   = new ProfileFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.fragment_home_profile, container, false);
        txvw_name_00        = view.findViewById(R.id.txvw_name_00);
        txvw_change_00      = view.findViewById(R.id.txvw_change_00);
        imvw_account_00     = view.findViewById(R.id.imvw_account_00);
        txpf_outletid_00    = view.findViewById(R.id.txpf_outletid_00);
        txpf_username_00    = view.findViewById(R.id.txpf_username_00);
        txpf_o_address_00   = view.findViewById(R.id.txpf_o_address_00);
        txpf_o_city_00      = view.findViewById(R.id.txpf_o_city_00);
        txpf_o_phone_00     = view.findViewById(R.id.txpf_o_phone_00);
        txpf_o_email_00     = view.findViewById(R.id.txpf_o_email_00);
        txpf_o_npwp_00      = view.findViewById(R.id.txpf_o_npwp_00);
        txpf_o_npwpaddr_00  = view.findViewById(R.id.txpf_o_npwpaddr_00);
        txpf_o_nik_00       = view.findViewById(R.id.txpf_o_nik_00);
        rvly_logout_00      = view.findViewById(R.id.rvly_logout_00);
        rvly_changepwd_00   = view.findViewById(R.id.rvly_changepwd_00);
        rvly_foto_00        = view.findViewById(R.id.rvly_foto_00);
        loadingScreen       = new LoadingScreen(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txpf_outletid_00.setTitle("Outlet ID");
        txpf_username_00.setTitle(getResources().getString(R.string.username));
        txpf_o_address_00.setTitle(getResources().getString(R.string.address));
        txpf_o_city_00.setTitle(getResources().getString(R.string.city));
        txpf_o_phone_00.setTitle(getResources().getString(R.string.phone));
        txpf_o_email_00.setTitle(getResources().getString(R.string.email));
        txpf_o_npwp_00.setTitle(getResources().getString(R.string.npwp));
        txpf_o_npwpaddr_00.setTitle(getResources().getString(R.string.npwp_address));
        txpf_o_nik_00.setTitle(getResources().getString(R.string.nik));

        txpf_o_phone_00.setImage(R.drawable.ic_phone_android);
        txpf_o_email_00.setImage(R.drawable.ic_mail_outline);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"ONRESUME");
        initData();
    }

    private void initData() {
        outletDB = new OutletDB();
        outletDB.getMine(getActivity());

        txvw_name_00.setText(outletDB.outlet_name);
        txpf_outletid_00.setValue(outletDB.outlet_id);
        txpf_username_00.setValue(outletDB.username);
        txpf_o_address_00.setValue(Parse.getString(outletDB.outlet_address));
        txpf_o_city_00.setValue(Parse.getString(outletDB.outlet_city));
        txpf_o_phone_00.setValue(Parse.getString(outletDB.outlet_phone));
        txpf_o_email_00.setValue(Parse.getString(outletDB.outlet_email));
        txpf_o_email_00.setValue(Parse.getString(outletDB.outlet_email));
        txpf_o_npwp_00.setValue(Parse.getString(outletDB.outlet_npwp));
        txpf_o_npwpaddr_00.setValue(Parse.getString(outletDB.outlet_npwp_addr));
        txpf_o_nik_00.setValue(Parse.getString(outletDB.outlet_nik));

        String url_foto = Parse.getString(outletDB.url_foto);
        Log.d(TAG,"URL FOTO "+ outletDB.url_foto);
        if (!url_foto.isEmpty()){
            String path = "http://202.154.3.188/commerce/unilever-middleware/uploads/"+outletDB.outlet_id+"/";
            Glide.with(getActivity()).load(path+"/"+url_foto).into(imvw_account_00);
        }
    }

    private void initListener(){
        rvly_logout_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingScreen.setMessage("Please Wait...");
                loadingScreen.show();
                handler.sendEmptyMessageDelayed(-99, 1000);
            }
        });

        rvly_changepwd_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePwdActivity.class);
                startActivity(intent);
            }
        });

        txvw_change_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        rvly_foto_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPemission();
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case -99:
                    logout();
                    break;
            }
            return false;
        }
    });

    private void logout(){
        loadingScreen.dimiss();
        OutletDB outletDB = new OutletDB();
        outletDB.clearData(getActivity());

        ChartDB chartDB = new ChartDB();
        chartDB.clearData(getActivity());

        OrderDB orderDB = new OrderDB();
        orderDB.clearData(getActivity());

        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);

        getActivity().finish();
    }

    private void getImageFromGalery(){
        Intent imageGallery = new Intent(Intent.ACTION_PICK);
        imageGallery.setType("image/*");
        startActivityForResult(imageGallery, RESULT_LOAD_IMG);
    }


    /*
     *  PERMISSION GALLERY
     */

    private void requestPemission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        if(!hasPermissions(getActivity(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        else {
            getImageFromGalery();
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getImageFromGalery();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            try {
                final Uri imageUri = data.getData();

                ImageUploader imageUploader = new ImageUploader(imageUri, getActivity());
                File file = new File(imageUploader.getPath(imageUri));
                long length = file.length() / 1024;
                imageUploader.addParam(new KeyValueHolder("outlet_id",outletDB.outlet_id));

                long max = 2000;
                Log.d(TAG, "File size "+ length +" "+(length > max));
                if (length < max){ // MAX 2MB
                    imageUploader.execute("profile_photo");
                }
                else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.file_image_maximum), Toast.LENGTH_LONG).show();
                }
                imageUploader.setOnUploadListener(new ImageUploader.UploadListener() {
                    @Override
                    public void onFinishUpload(int code, String message, JSONObject response) {
                        if (code == ErrorCode.OK){
                            Log.d(TAG,response.toString());
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            try {
                                String url = response.getString("outlet_photo");
                                String path = "http://202.154.3.188/commerce/unilever-middleware/uploads/"+outletDB.outlet_id+"/";
                                Glide.with(getActivity()).load(path+"/"+url).into(imvw_account_00);
                                outletDB.url_foto = url;
                                outletDB.clearData(getActivity());
                                outletDB.insert(getActivity());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
