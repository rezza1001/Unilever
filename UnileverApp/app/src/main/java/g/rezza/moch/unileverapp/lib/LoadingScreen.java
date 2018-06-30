package g.rezza.moch.unileverapp.lib;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Rezza on 8/22/17.
 */

public class LoadingScreen {
private Context mContext;
    ProgressDialog progressDialog = null;

    public LoadingScreen(Context pContext){
        mContext = pContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("please wait ...");
        progressDialog.setCancelable(false);
    }
    public void show() {
        progressDialog.show();
    }

    public void setMessage(String pMessage){
        progressDialog.setMessage(pMessage);
    }

    public void setTitle(String pTitle){
        progressDialog.setTitle(pTitle);
    }

    public void dimiss(){
        progressDialog.dismiss();
    }


}
