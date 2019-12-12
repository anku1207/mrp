package com.uav.mandiratepe.volley;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.activity.MandiClosed;
import com.uav.mandiratepe.activity.Product_List;
import com.uav.mandiratepe.activity.SplashScreen;
import com.uav.mandiratepe.bo.ContactUsBO;
import com.uav.mandiratepe.bo.LocationBO;
import com.uav.mandiratepe.constant.GlobalApplication;
import com.uav.mandiratepe.permission.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import static com.uav.mandiratepe.util.Utility.showSingleButtonDialog;

public class GetAppLatestVersion extends AsyncTask<String, String, JSONObject> {

    private ProgressDialog progressDialog;
    private String currentVersion, latestVersion;
    private GetAppLatestVersionInterface getAppLatestVersionInterface;



    private Context mctx;

    public GetAppLatestVersion( Context context ,GetAppLatestVersionInterface getAppLatestVersionInterface){
        this.mctx = context;
        this.getAppLatestVersionInterface=getAppLatestVersionInterface;
    }

    private  void getCurrentVersion(){
        PackageManager pm = mctx.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(mctx.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        this.currentVersion= pInfo.versionName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        this.getCurrentVersion();
        try {
            //It retrieves the latest version by scraping the content of current version from play store at runtime

          /*  latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.uav.mandiratepe")
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .timeout(120000)
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();*/






        }catch (Exception e){
            latestVersion=null;
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if(latestVersion!=null) {
            if (!currentVersion.equalsIgnoreCase(latestVersion)){
                if(!((Activity)mctx).isFinishing()){//This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                    showForceUpdateDialog();
                }
            }
        }
        getAppLatestVersionInterface.Continue(true);

    }

    private void showForceUpdateDialog(){
        VolleyUtils.makeJsonObjectRequest(mctx, ContactUsBO.updateVersion(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                JSONObject data=response.getJSONObject("dataList");

                Log.w("verifylocation",response.toString());

                if(response.getString("status").equals("fail")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    showSingleButtonDialog(mctx,"Error !",sb.toString(),true);
                }else {

                    final Dialog var3 = new Dialog(mctx);
                    var3.requestWindowFeature(1);
                    var3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    var3.setContentView(R.layout.singlebuttondialog);
                    var3.setCanceledOnTouchOutside(false);
                    TextView title = (TextView)var3.findViewById(R.id.dialog_one_tv_title);
                    title.setText("");
                    TextView msg = (TextView)var3.findViewById(R.id.dialog_one_tv_text);
                    msg.setText(data.getString("updateVersion"));
                    Button update = (Button)var3.findViewById(R.id.dialog_one_btn);
                    update.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View var) {
                            ((Activity)mctx).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                    ("market://details?id="+ mctx.getPackageName() )));
                            ((Activity) mctx).finish();
                            var3.dismiss();
                        }
                    });
                    var3.show();
                }
            }
        });


    }
}


