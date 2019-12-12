package com.uav.mandiratepe.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.activity.Product_List;
import com.uav.mandiratepe.constant.ApplicationConstant;
import com.uav.mandiratepe.webview.AppWebViewClients;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    static int errorColor;
    static  Context contextact;
    static  String erroract;


    public static SpannableStringBuilder getErrorSpannableString(Context context, String msg){
        int version = Build.VERSION.SDK_INT;
        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(context, R.color.errorColor);
        } else {
            errorColor = context.getResources().getColor(R.color.errorColor);
        }

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(msg);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, msg.length(), 0);

        return spannableStringBuilder;
    }

    public  static String imagetostring (Uri mImageUri, Context context){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        Bitmap bmp= grabImage(mImageUri,context);
        bmp= Utility.scaleDown(bmp, 1100, true);
        if(bmp.getWidth()>bmp.getHeight()){
            Matrix matrix =new Matrix();
            matrix.postRotate(90);
            bmp= Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        }

        /*Bitmap  bmp1= Bitmap.createScaledBitmap(
                bitmap, 320, 500, false);*/
        bmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes =outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    public static String BitMapToString(Bitmap bmp){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        bmp= Utility.scaleDown(bmp, 1100, true);
        if(bmp.getWidth()>bmp.getHeight()){
            Matrix matrix =new Matrix();
            matrix.postRotate(90);
            bmp= Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        }

        bmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes =outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }




    /// manoj shakya
    public static File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/HUNDI/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }


    public static Bitmap grabImage(Uri mImageUri, Context context)
    {
        //context.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = context.getContentResolver();
        Bitmap bitmap = null;
        try
        {
            bitmap  = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }









    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    //15-1-2019


    public static Bitmap ByteArrayToBitmap(byte[] byteArray){
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }




    public static String currentDateFormat(){

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS");
        String currentTimeStamp=dateFormat.format(new Date());
        return  currentTimeStamp;

    }

    public static File storeCameraPhotoInSDCard(Bitmap bitmap , String currentdate){
        //  File outputFile = new File(Environment.getExternalStorageDirectory(),"photo_"+currentdate);
        File direct = new File(Environment.getExternalStorageDirectory() + "/ROF");
        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()+"/"+"ROF/");
            wallpaperDirectory.mkdirs();
        }
        File file = new File(new File(Environment.getExternalStorageDirectory()+"/"+"ROF/"), "photo_"+currentdate+".JPEG");
        /*if (file.exists()) {
            file.delete();
        }*/
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }




    public static String validatePattern(String value, String key ){
        try{
            JSONObject valid = new JSONObject(key);
            if(value == null){
                return  valid.getString("msg");
            }
            Pattern ptrn = Pattern.compile(valid.getString("pattern"));
            Matcher matcher = ptrn.matcher(value);
            String errorMsg=null;

            if(! matcher.matches()){
                errorMsg= valid.getString("msg");
            }
            return errorMsg;
        }catch(Exception e){

            Log.w("error",e);
            return   ApplicationConstant.SOMETHINGWRONG;
        }
    }


    public static boolean validepanno(Pattern pattern,String value){

        Matcher matcher = pattern.matcher(value);
        // Check if pattern matches
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


    public static Bitmap getImageFileFromSDCared(File filename){

        Bitmap bitmap=null;
        try {
            FileInputStream fis=new FileInputStream(filename);
            bitmap= BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }



   /* public  static String getImageByPincode(Context context, Bitmap bitmap){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < textBlocks.size(); index++) {
            //extract scanned text blocks here
            TextBlock item = textBlocks.valueAt(index);
            stringBuilder.append(item.getValue());
            stringBuilder.append("\n");
        }
        String value=stringBuilder.toString().replaceAll(" ", "");
        String replaceString=value.toString().replaceAll("\n", "");

        int i=0;
        String nos = "";
        List<String> digitsArray = new ArrayList<String>();
        while(replaceString.length()>i){
            Character ch = replaceString.charAt(i);
            if(Character.isDigit(ch)){
                nos += ch;
            }else if(nos!=""){
                digitsArray.add(nos);
                nos ="";
            }
            i++;
        }

        String pincode=null;
        for(String p : digitsArray){
            if(p.length()==6){
                pincode=p;
                break;
            }
        }
        return pincode;


    }
*/


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }





    public static void  alertDialog(Context context, String title , String msg , String buttonname){

        AlertDialog alertDialog;

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });
        alertDialog.show();

    }

    public static void  exceptionAlertDialog(Context context,String title ,String msg , String buttonname,String error){

        contextact=context;
        erroract=error;

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                sendLogFile(contextact,erroract);

            }
        });
        alertDialog.show();

    }
    private static void sendLogFile(Context context ,String error) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"manojshakya1207@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error reported from MyAPP");
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached."+error); // do this so some email clients don't complain about empty body.
        context.startActivity(intent);
    }



    public static Drawable GetImage(Context c, String ImageName) {
        Drawable drawable;

        try {
            String[] imagename=ImageName.split("\\.");
            drawable= c.getResources().getDrawable(c.getResources().getIdentifier(imagename[0], "drawable", c.getPackageName()));
        }catch (Exception e){
            drawable=null;
        }
        return drawable;
    }


    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public static JSONArray getQueryarray(String query) {
        JSONArray jsonArray=new JSONArray();
        try {
            String[] params = query.split("&");
            for (String param : params) {
                JSONObject object=new JSONObject();
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                object.put(name,value);
                jsonArray.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static void showSingleButtonDialog(final Context var1, String error, String var2 , final boolean activityfinish ) {
        final Dialog var3 = new Dialog(var1);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        var3.setContentView(var1.getResources().getIdentifier("singlebuttondialog", "layout", var1.getPackageName()));
        var3.setCanceledOnTouchOutside(false);
        TextView var4 = (TextView)var3.findViewById(var1.getResources().getIdentifier("dialog_one_tv_title", "id", var1.getPackageName()));
        var4.setText(error);
        TextView var6 = (TextView)var3.findViewById(var1.getResources().getIdentifier("dialog_one_tv_text", "id", var1.getPackageName()));

        var6.setText(var2);

        Button var5 = (Button)var3.findViewById(var1.getResources().getIdentifier("dialog_one_btn", "id", var1.getPackageName()));
        var5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

                Activity activity = (Activity) var1;
                if(activityfinish){

                    activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    activity.finish();
                }
            }
        });
        var3.show();
    }

    public static Date convertString2Date(String dtValue, String format){
        Date dt = new Date();
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
            dt =  simpleDateFormat.parse(dtValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static Date StringToDateWithLenient(String dtValue, String format){

        Date dt = new Date();
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
            simpleDateFormat.setLenient(false);
            dt =  simpleDateFormat.parse(dtValue);

        } catch (ParseException e) {
            e.printStackTrace();
            dt=null;
        }

        return dt;
    }

    public static String convertDate2String(Date dtValue , String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(dtValue);
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }


    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MotionEvent ontouchevent(){
        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        // List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        // Dispatch touch event to view
        return motionEvent;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static  String urlDecode(String code){

        String name= null;
        try{
            name= URLDecoder.decode(code, "UTF-8");
        }catch (Exception e){
            return null;
        }
        return name;

    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    public static Integer getVersioncode(Context context){
        Integer version =null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;
            Log.w("versioncode", String.valueOf(version));
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return version;
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    public static String maskString(String strText, int start, int end, char maskChar)
            throws Exception{

        if(strText == null || strText.equals(""))
            return "";

        if(start < 0)
            start = 0;

        if( end > strText.length() )
            end = strText.length();

        if(start > end)
            throw new Exception("End index cannot be greater than start index");

        int maskLength = end - start;

        if(maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for(int i = 0; i < maskLength; i++){
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }


    public static void hidekeynbord(Activity context){
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }


    public static String hindinameutf8(String value){
        String[] utfArr=value.split(" ");
        String utlText="";
        for(int i=0; i<utfArr.length; i++){
            if(utlText!="") utlText +=" ";
            utlText += String.valueOf(Html.fromHtml(Utility.utf8(utfArr[i])));
        }

        return utlText;
    }

    public static String utf8(String value){
        String text = null;
        String result = "";
        try{
            text =  value;
            String[] arr = text.split("u");
            for(int i = 1; i < arr.length; i++){
                int hexVal = Integer.parseInt(arr[i], 16);
                result += (char)hexVal;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }



    public static void webviewDialog(Context context){
        final Dialog var3 = new Dialog(context);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        var3.setContentView(R.layout.dialog_webview);
        var3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //var3.setCanceledOnTouchOutside(true);

        WebView webView=var3.findViewById(R.id.webView);
        ProgressBar progressBar=var3.findViewById(R.id.progressbar);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);


        webView.setInitialScale(1);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMinimumFontSize(16);
        webView.setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        webView.setWebViewClient(new AppWebViewClients(progressBar) );
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("WebView", consoleMessage.message());
                return true;
            }
        });

        webView.loadUrl("http://mandiratepe.com/faqApp.html"); //receiptUrl

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(var3.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        var3.show();
        var3.getWindow().setAttributes(lp);

    }





}


