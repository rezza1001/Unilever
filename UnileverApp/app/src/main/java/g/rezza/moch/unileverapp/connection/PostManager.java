package g.rezza.moch.unileverapp.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import g.rezza.moch.unileverapp.SignInActivity;
import g.rezza.moch.unileverapp.holder.KeyValueHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.LoadingScreen;

/**
 * Created by Rezza on 8/22/17.
 */

public class PostManager extends AsyncTask<String, Void, String> {

    private static final String TAG = "PostManager";
    private static final String SPARATOR = "~R3224~";

//    private String url              = "http://202.154.3.188/commerce/sandbox-unilever-middleware/core-services";
    private String url              = "http://202.154.3.188/commerce/unilever-middleware/core-services";
    private String apiUrl           = "";
    private JSONObject mData        = new JSONObject();
    private Context context;
    private LoadingScreen loading ;

    private boolean showLoading = true;

    public PostManager(Context mContext){
        loading = new LoadingScreen(mContext);
        context = mContext;
    }

    public void showloading(boolean show){
        if (!show){
            showLoading = false;
        }
    }

    public void setApiUrl(String url) {
        this.apiUrl = url;
    }

    public void setData(JSONObject jo){
        mData = jo;
    }

    public void setData(ArrayList<KeyValueHolder> pHolders){
        for(KeyValueHolder holder : pHolders){
            try {
                mData.put(holder.key, holder.value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPreExecute() {
        if (showLoading){
            loading.show();
        }
        super.onPreExecute();
    }

    protected String doInBackground(String... arg0) {
        StringBuilder sbResponse = new StringBuilder();
        try {
            String type = arg0[0];
            URL url = new URL(this.url+"/"+ apiUrl); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(type); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            if (type.equals("POST")){
                httpURLConnection.setDoOutput(true);
            }
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.setRequestProperty("Accept", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();
            if (type.equals("POST")){
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                Log.d(TAG,type+ " "+apiUrl+ " : "+ mData.toString());
                wr.writeBytes(mData.toString());
                wr.flush();
                wr.close();
            }


            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG,"RESPONSE CODE : "+ responseCode);
            sbResponse.append(responseCode).append(SPARATOR);
            BufferedReader in;
            if (responseCode == ErrorCode.CODE_UNPROCESSABLE_ENTITY){
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }
            else if (responseCode == ErrorCode.NOT_FOUND){
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }
            else {
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            sbResponse.append(response.toString());
            return sbResponse.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " Error "+e.getMessage());
            sbResponse.append("Request time out");
            return sbResponse.toString();
        }

    }




    protected void onPostExecute(String presults) {
        loading.dimiss();
        try {
            String results = presults.split(SPARATOR)[1];
            int code    =  Integer.parseInt(presults.split(SPARATOR)[0]);
            if (results!=null) {
                Log.d(TAG,"TEXT RESPONSE "+ results.toString() +" | CODE : "+ code);
            }
            if (mReceiveListener != null){
                if (results != null){
                    if (results.equals("Request time out")){
                        Toast.makeText(context,"Request time out", Toast.LENGTH_SHORT).show();
                        mReceiveListener.onReceive(null, ErrorCode.TIME_OUT, "Time Out");
                    }
                    try {
                        JSONObject jo = new JSONObject(results);
                        int response_code   = jo.getInt("response_code");
                        String message      = jo.getString("response_message");
                        mReceiveListener.onReceive(jo, response_code,message);
//                        if (body_error_code == ErrorCode.ACCESS_TOKEN){
//                            goToLogin();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mReceiveListener.onReceive(null, ErrorCode.UNDIFINED_ERROR, "Undefined");
                    }

                }
            }
        }catch (Exception e){
            mReceiveListener.onReceive(null, ErrorCode.UNDIFINED_ERROR,"Undefined");
        }


    }

    private onReceiveListener mReceiveListener;
    public void setOnReceiveListener(onReceiveListener mReceiveListener){
        this.mReceiveListener = mReceiveListener;
    }
    public interface onReceiveListener{
        public void onReceive(JSONObject obj, int code, String message);
    }

    private void goToLogin(){

        Intent intent = new Intent(context, SignInActivity.class);
        Activity activity = (Activity)context;
        activity.startActivity(intent);
        activity.finish();
    }
}
