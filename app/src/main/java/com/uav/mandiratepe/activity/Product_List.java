package com.uav.mandiratepe.activity;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.mandiratepe.MRPService.LogoutService;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.adpater.Product_Adapter;
import com.uav.mandiratepe.adpater.TextTextAdapter;
import com.uav.mandiratepe.bo.CustomerBO;
import com.uav.mandiratepe.bo.ProductBO;
import com.uav.mandiratepe.constant.ApplicationConstant;
import com.uav.mandiratepe.override.ExpandableHeightListView;
import com.uav.mandiratepe.permission.PermissionHandler;
import com.uav.mandiratepe.permission.Session;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.DataAdapterVO;
import com.uav.mandiratepe.vo.ProductVO;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;
import com.uav.mandiratepe.webview.AppWebViewClients;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.uav.mandiratepe.util.Utility.hidekeynbord;

public class Product_List extends Base_Activity  implements View.OnClickListener, TextWatcher,View.OnFocusChangeListener{

    public static TextView categaryamount,categaryitem,totalamount;

    static Integer totalamounttext=0;

    static Animation animation ;
    static ArrayList<Integer> totalprice;
    static int position;

    public static JSONArray category;
    static JSONObject category_item;
    public static int selecttabid;
    public static Map<String,ProductVO> orderItemList= new HashMap<>();

    static Map<Integer, List<ProductVO>> categoryProductMap = null;

    private EditText phone_pin_first_edittext,phone_pin_second_edittext,phone_pin_third_edittext,phone_pin_forth_edittext;
    TextView resendotpbtn;
    Button otpverifybtn;
    CountDownTimer mobiletime = null;
    CountDownTimer emailtime = null;


    SpotsDialog spotsDialog;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar =(android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.inflateMenu(R.menu.menu_home__item);
        customMenuIconChangeColor(toolbar);



        categoryProductMap = new HashMap<Integer, List<ProductVO>>();
        // sms read permission
        if(!PermissionHandler.checkpermissionmessage(Product_List.this))



        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {




                if (menuItem.getItemId()==R.id.history){

                    spotsDialog.show();
                    SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
                    if(!sharedPreferences.contains(Session.CACHE_CUSTOMER_DATA)){
                        createNewCustomer("order");
                    }else {
                        startActivity(new Intent(Product_List.this,Order_History.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                    spotsDialog.dismiss();

                }else if(menuItem.getItemId()==R.id.contactus){
                    startActivity(new Intent(Product_List.this,ContactUs.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }else if(menuItem.getItemId()==R.id.help){
                    Utility.webviewDialog(Product_List.this);
                }

                return false;
            }
        });


        orderItemList= new HashMap<>();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        animation = AnimationUtils.loadAnimation(this, R.anim.animation);



        spotsDialog = new SpotsDialog(this, R.style.CustomDialogloading);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        totalamount=findViewById(R.id.totalamount);

        totalprice=new ArrayList<>();
        categaryitem=findViewById(R.id.categary);
        categaryamount=findViewById(R.id.categaryamount);
        LinearLayout proceedlayout=findViewById(R.id.proceedlayout);
        LinearLayout amountbycategoreybtn=findViewById(R.id.amountbycategoreybtn);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor( R.color.colorPrimary));

        Product_List.categaryitem.setText(getApplication().getString(R.string.Rs)+" : "+0);
        proceedlayout.setOnClickListener(this);
        amountbycategoreybtn.setOnClickListener(this);


        setTabLayoutCategorey();


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        if(!Session.check_Exists_key(this,Session.CACHE_INFODIALOG)){
            Utility.webviewDialog(Product_List.this);
            Session.set_Data_Sharedprefence(this,Session.CACHE_INFODIALOG,"1");
        }

    }

    private void customMenuIconChangeColor(Toolbar toolbar){
        for(int i = 0; i < toolbar.getMenu().size(); i++){
            Drawable drawable = toolbar.getMenu().getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }


    public void setTabLayoutCategorey(){
        VolleyUtils.makeJsonObjectRequest(this, ProductBO.getCategoryList(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.getString("status").equals("fail")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    Utility.showSingleButtonDialog(Product_List.this,null,sb.toString(),false);
                }else {
                    category=response.getJSONArray("dataList");

                    //load tab first time all itme in list

                    mViewPager.setOffscreenPageLimit(category.length());

                    for(int i=0 ; i<category.length();i++){
                        JSONObject jsonObject =category.getJSONObject(i);
                        totalprice.add(0);
                        tabLayout.addTab(tabLayout.newTab().setText(jsonObject.getString("categoryName")));

                    }

                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                }
            }
        });
    }



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home__item, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Toast.makeText(this, "sdfdsfdaf", Toast.LENGTH_SHORT).show();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast.makeText(this, "sdfdsfdaf", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onClick(View view) {



        switch (view.getId()){
            case R.id.proceedlayout:

                spotsDialog.show();

                SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
                if(saveItemListInSession()!=0){
                    if(!sharedPreferences.contains(Session.CACHE_CUSTOMER_DATA)){
                        createNewCustomer("proceed");
                    }else {
                        startActivity(new Intent(Product_List.this,Check_Out.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();

                    }
                }else {
                    Utility.showSingleButtonDialog(Product_List.this,null,"please select a Product",false);
                }
                spotsDialog.dismiss();

                break;
            case R.id.amountbycategoreybtn:

                if(saveItemListInSession()!=0){
                    Dialog var3 = new Dialog(Product_List.this);
                    var3.requestWindowFeature(1);
                    var3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    var3.setContentView(R.layout.show_amount_by_categorey);
                   // var3.setCanceledOnTouchOutside(false);
                    var3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                    ExpandableHeightListView expandableHeightListView =var3.findViewById(R.id.listview);
                    TextTextAdapter myAdapter=new TextTextAdapter(this, getItemList(), R.layout.sidebyside_test_listview);
                    expandableHeightListView.setAdapter(myAdapter);
                    expandableHeightListView.setExpanded(true);


                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(var3.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                   /* expandableHeightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(Product_List.this, ""+i, Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    var3.show();
                    var3.getWindow().setAttributes(lp);
                }else {
                    Utility.showSingleButtonDialog(Product_List.this,null,"please select a Product",false);
                }
                break;
        }
    }

    public ArrayList<DataAdapterVO> getItemList(){
        ArrayList<DataAdapterVO> datalist = new ArrayList<>();

            ArrayList<ProductVO> productVOS =Session.getItemList(Product_List.this);
            HashMap<Integer,String> categoryList=new HashMap<>();
            HashMap<Integer,Double> categoryTotal=new HashMap<>();
            for(ProductVO productVO : productVOS){
                categoryList.put(productVO.getCategoryId(),productVO.getCategoryName());
                Double rate=productVO.getQty()*productVO.getMop();
                if(categoryTotal.containsKey(productVO.getCategoryId())){
                    Double categorytotal=categoryTotal.get(productVO.getCategoryId());
                    categoryTotal.put(productVO.getCategoryId(),categorytotal+rate);
                }else {
                    categoryTotal.put(productVO.getCategoryId(),rate);
                }

            }

            Double totalamount=0.0;
            for (Map.Entry<Integer, Double> entry : categoryTotal.entrySet()) {
                Integer key = entry.getKey();
                Double value = entry.getValue();

                totalamount +=value;
                datalist.add(new DataAdapterVO(categoryList.get(key),String.valueOf(value.intValue())));
            }
           datalist.add(new DataAdapterVO("Total",""+totalamount.intValue()));


            return datalist;
    }

    public static int computeOrderProductAmt(Map<String,ProductVO> orderItemList){
        int sum=0;
        for(int i = 0; i < orderItemList.size (); ++i){
            ProductVO value = (new ArrayList<ProductVO>(orderItemList.values())).get(i);
            if(value.getQty()!=null && value.getQty()!=0){
                sum +=value.getQty()*value.getMop();
            }

        }
        return sum;
    }
    public int saveItemListInSession(){

        List<ProductVO> preorderlist=new ArrayList<>();

        int totalamt=0;
        for(int i = 0; i < orderItemList.size (); ++i){

            ProductVO productVO = (new ArrayList<ProductVO>(orderItemList.values())).get(i);
            if(productVO.getQty()!=null && productVO.getQty()!=0){
                preorderlist.add(productVO);
                totalamt +=productVO.getQty()*productVO.getMop();
            }
        }
        String selectitemstringarry = Utility.toJson(preorderlist);
        Session.set_Data_Sharedprefence(Product_List.this,Session.CACHE_ORDER_LIST,selectitemstringarry);
        return totalamt;
    }


    public void createNewCustomer(final String type){

            final Dialog var3 = new Dialog(Product_List.this);
            var3.requestWindowFeature(1);
            var3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            var3.setContentView(R.layout.activity_login_by_mobile_number);

            var3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            var3.setCanceledOnTouchOutside(false);

            Button login=var3.findViewById(R.id.login);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(var3.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean inputvalid=true;

                    EditText mobileno= var3.findViewById(R.id.mobileno);

                    if(mobileno.getText().toString().equals("")){

                        mobileno.setError("this filed is required");
                        inputvalid=false;
                    }

                    if (!mobileno.getText().toString().equals("") &&  Utility.validatePattern(mobileno.getText().toString().trim(), ApplicationConstant.MOBILENO_VALIDATION)!=null){
                        mobileno.setError(Utility.validatePattern(mobileno.getText().toString().trim(),ApplicationConstant.MOBILENO_VALIDATION));
                        inputvalid=false;
                    }
                    if(inputvalid)  loginByMobile(mobileno.getText().toString(),var3,type);
                }
            });
            var3.show();
            var3.getWindow().setAttributes(lp);

    }




    public  void  loginByMobile(String mobilenumber, final Dialog dialog, final String type){
        VolleyUtils.makeJsonObjectRequest(this, CustomerBO.getCustomerByMobileNumber(mobilenumber), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                dialog.dismiss();
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                Log.w("customerlogin",response.toString());

                dialog.dismiss();
                if(!response.getString("status").equals("success")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    Utility.showSingleButtonDialog(Product_List.this,null,sb.toString(),false);

                }else {
                    JSONObject object = response.getJSONObject("dataList");
                    JSONObject customerjosn=object.getJSONObject("customer");

                    showOtpDialog(customerjosn.getString("mobileNumber"),object.getInt("expireTime"),object.has("otp")?object.getString("otp"):null,type);

                }
            }
        });
    }


    public void showOtpDialog(final String mobileno, int expireTime, String otp , final String type){

       /* mobiletime = null;
        emailtime = null;*/



        Dialog var3 = new Dialog(Product_List.this);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        var3.setContentView(R.layout.design_verify__otp);
        var3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        var3.setCanceledOnTouchOutside(false);

        phone_pin_first_edittext=var3.findViewById(R.id.phone_pin_first_edittext);
        phone_pin_second_edittext=var3.findViewById(R.id.phone_pin_second_edittext);
        phone_pin_third_edittext=var3.findViewById(R.id.phone_pin_third_edittext);
        phone_pin_forth_edittext=var3.findViewById(R.id.phone_pin_forth_edittext);
        TextView showmobileno=var3.findViewById(R.id.showmobileno);
        resendotpbtn=var3.findViewById(R.id.resendotpbtn);



        otpverifybtn=var3.findViewById(R.id.otpverifybtn);
        otpverifybtn.setTag(R.string.mobileno,mobileno);
        otpverifybtn.setTag(R.string.type,type);

        cancelTimer("mobile");

        if(otp !=null)autofileotp(otp);
        startTimer(new Long(expireTime),"mobileotp");

        setPINListeners();

        try {
            showmobileno.setText("OTP has sent on "+Utility.maskString(mobileno,3,7,'*'));
        } catch (Exception e) {
            e.printStackTrace();
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(var3.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        resendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidekeynbord(Product_List.this);

                resendotpbtn.setVisibility(View.GONE);
                resendotpfun(mobileno);
            }
        });


        otpverifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidekeynbord(Product_List.this);

                if(!mobileotpfiledcheck()) {
                    Utility.showSingleButtonDialog(Product_List.this,null," OTP is Empty",false);
                }else {
                    otpverify(mobileno,type);
                }
            }
        });
        var3.show();
        var3.getWindow().setAttributes(lp);

    }

    public  void autofileotp(String mobileotp){

        String otp=mobileotp.substring(0,4);
        cancelTimer("mobile");
        char[] array = otp.toCharArray();
        for(int i = 0; i < array.length; i++) {
            if(i==0){
                phone_pin_first_edittext.setText(String.valueOf(array[i]));
            }else if(i==1){
                phone_pin_second_edittext.setText(String.valueOf(array[i]));
            }else if(i==2){
                phone_pin_third_edittext.setText(String.valueOf(array[i]));
            }else if(i==3){
                phone_pin_forth_edittext.setText(String.valueOf(array[i]));
            }
        }
    }



    public void otpverify(final String mobileno ,final String type){

        VolleyUtils.makeJsonObjectRequest(this,CustomerBO.verifiyOTP(mobileno,getmobileotp(),Session.getSessionByKey(Product_List.this,Session.CACHE_TOKENID)) , new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                Log.w("otp resp",response.toString());

                if(!response.getString("status").equals("success")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    Utility.showSingleButtonDialog(Product_List.this,null,sb.toString(),false);

                }else{

                    JSONObject object = response.getJSONObject("dataList");
                    JSONObject customerjosn=object.getJSONObject("customer");
                    Session.set_Data_Sharedprefence(Product_List.this,Session.CACHE_CUSTOMER_DATA,customerjosn.toString());

                    if(type.equals("proceed")){
                        startActivity(new Intent(Product_List.this,Check_Out.class));
                    }else if(type.equals("order")){
                        startActivity(new Intent(Product_List.this,Order_History.class));
                    }
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();

                }
            }
        });

    }



    public void resendotpfun(final String mobileno){

        VolleyUtils.makeJsonObjectRequest(this,CustomerBO.resendOTP(mobileno) , new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                Log.w("otp response",response.toString());


                if(!response.getString("status").equals("success")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    Utility.showSingleButtonDialog(Product_List.this,null,sb.toString(),false);

                }else{
                    setempty();
                    JSONObject object = response.getJSONObject("dataList");
                    startTimer(new Long (object.getInt("expireTime")),"mobileotp");
                    phone_pin_first_edittext.requestFocus();

                }
            }
        });

    }

    public String getmobileotp(){
        return   phone_pin_first_edittext.getText().toString()+phone_pin_second_edittext.getText().toString()+phone_pin_third_edittext.getText()+
                phone_pin_forth_edittext.getText().toString();
    }



    //start timer function
    void startTimer(Long timeperiod,final String type) {
        CountDownTimer counttimetype=null;

        counttimetype=new CountDownTimer(timeperiod*1000, 1000) {
            public void onTick(long millisUntilFinished) {

              String text=(millisUntilFinished/1000)+" sec";

                otpverifybtn.setText(Html.fromHtml("<span>Verify</span><br><span>"+text+"</span>"));

            }
            public void onFinish() {
                if(type.equals("mobileotp")){
                    /*otpverifybtn.setTag("resend");
                    otpverifybtn.setText("Resend");*/

                    resendotpbtn.setVisibility(View.VISIBLE);
                    otpverifybtn.setText("verify");

                }else if(type.equals("emailotp")){

                    /*otpverifybtn.setTag("resend");
                    otpverifybtn.setText("Resend");*/
                    resendotpbtn.setVisibility(View.VISIBLE);
                    otpverifybtn.setText("verify");

                }
            }
        };
        if(type.equals("mobileotp")){
            mobiletime= counttimetype;
            counttimetype.start();
        } else if(type.equals("emailotp")){
            emailtime= counttimetype;
            counttimetype.start();
        }

    }
    //cancel timer
    void cancelTimer(String type) {
        if(type.equals("mobile")){
            if(mobiletime!=null)
                mobiletime.cancel();
            resendotpbtn.setVisibility(View.GONE);
           /* otpverifybtn.setTag("verify");
            otpverifybtn.setText("Verify");*/
        }else if(type.equals("email")){
            if(emailtime!=null)
                emailtime.cancel();
           /* otpverifybtn.setTag("verify");
            otpverifybtn.setText("verify");*/
            resendotpbtn.setVisibility(View.GONE);
        }

    }

    private void setPINListeners() {
        phone_pin_first_edittext.addTextChangedListener(this);
        phone_pin_second_edittext.addTextChangedListener(this);
        phone_pin_third_edittext.addTextChangedListener(this);
        phone_pin_forth_edittext.addTextChangedListener(this);

        phone_pin_first_edittext.setOnFocusChangeListener(this);
        phone_pin_second_edittext.setOnFocusChangeListener(this);
        phone_pin_third_edittext.setOnFocusChangeListener(this);
        phone_pin_forth_edittext.setOnFocusChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if (phone_pin_first_edittext.getText().hashCode() == s.hashCode()){
            if(!phone_pin_first_edittext.getText().toString().equals("")){
                phone_pin_second_edittext.requestFocus();
            }

        }else if(phone_pin_second_edittext.getText().hashCode() == s.hashCode()){
            if(!phone_pin_second_edittext.getText().toString().equals("")){
                phone_pin_third_edittext.requestFocus();
            }else {
                phone_pin_first_edittext.requestFocus();
            }
        }else if(phone_pin_third_edittext.getText().hashCode() == s.hashCode()){
            if(!phone_pin_third_edittext.getText().toString().equals("")){
                phone_pin_forth_edittext.requestFocus();
            }else {
                phone_pin_second_edittext.requestFocus();
            }
        }else if(phone_pin_forth_edittext.getText().hashCode() == s.hashCode()){
            if(!phone_pin_forth_edittext.getText().toString().equals("")){
                if(mobileotpfiledcheck()){
                    otpverify(otpverifybtn.getTag(R.string.mobileno).toString(),otpverifybtn.getTag(R.string.type).toString());
                }
            }else {
                phone_pin_third_edittext.requestFocus();
            }
        }
    }

    public boolean mobileotpfiledcheck(){
        if( phone_pin_first_edittext.getText().toString().equals("") ||phone_pin_second_edittext.getText().toString().equals("") || phone_pin_third_edittext.getText().equals("") ||
                phone_pin_forth_edittext.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    public void setempty(){
        phone_pin_first_edittext.setText("");
        phone_pin_second_edittext.setText("");
        phone_pin_third_edittext.setText("");
        phone_pin_forth_edittext.setText("");

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        final int id = view.getId();
        switch (id) {
            case R.id.phone_pin_first_edittext:
                if (hasFocus) {

                    phone_pin_first_edittext.setSelection(phone_pin_first_edittext.getText().length());
                    //showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.phone_pin_second_edittext:
                if (hasFocus) {
                    phone_pin_second_edittext.setSelection(phone_pin_second_edittext.getText().length());
                }
                break;

            case R.id.phone_pin_third_edittext:
                if (hasFocus) {
                    phone_pin_third_edittext.setSelection(phone_pin_third_edittext.getText().length());
                }
                break;
            case R.id.phone_pin_forth_edittext:
                if (hasFocus) {
                    phone_pin_forth_edittext.setSelection(phone_pin_forth_edittext.getText().length());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
       /* long diffMs =new Date().getTime()- Long.parseLong(Session.getSessionByKey(Product_List.this,Session.CACHE_APPSESSION));
        long diffSec = diffMs / 1000;
        long min = diffSec / 60;
        long sec = diffSec % 60;

        Toast.makeText(this, ""+sec, Toast.LENGTH_SHORT).show();
        if(sec>60){
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);
            orderItemList=new LinkedHashMap<>();
            Product_List.categaryitem.setText(getApplication().getString(R.string.Rs)+" : "+0);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        Log.w("stage","onPause");
        /*Date date =new Date();
        Session.set_Data_Sharedprefence(Product_List.this,Session.CACHE_APPSESSION,String.valueOf(date.getTime()));*/
    }



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                String otp=message.substring(0,4);
                cancelTimer("mobile");
                char[] array = otp.toCharArray();
                for(int i = 0; i < array.length; i++) {
                    if(i==0){
                        phone_pin_first_edittext.setText(String.valueOf(array[i]));
                    }else if(i==1){
                        phone_pin_second_edittext.setText(String.valueOf(array[i]));
                    }else if(i==2){
                        phone_pin_third_edittext.setText(String.valueOf(array[i]));
                    }else if(i==3){
                        phone_pin_forth_edittext.setText(String.valueOf(array[i]));
                    }
                }
                // phonepinverifybtn.performClick();
            }
        }
    };



    /**
     * A placeholder fragment containing a simple view.
     */
        public static class PlaceholderFragment extends Fragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        TextView add,subtraction,value,amount;

        TextView textView,errormsg;
        RecyclerView recyclerView;
       // SwipeRefreshLayout pullToRefresh;

        ImageView imagebatchlock;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public  Fragment newInstance(int sectionNumber) {
            Fragment fragment;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);


           /* PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;*/


            switch(sectionNumber) {

               /* case 0:
                    fragment=new Vegetables();
                    fragment.setArguments(args);
                    return  fragment;
*/


                default: fragment=new PlaceholderFragment();
                    fragment.setArguments(args);
                    return  fragment;
            }
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home__item, container, false);
            return rootView;
        }


        @Override
        public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(rootView, savedInstanceState);


            imagebatchlock=rootView.findViewById(R.id.imagebatchlock);
            errormsg=rootView.findViewById(R.id.errormsg);

            recyclerView=rootView.findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(true);
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(verticalLayoutManager);




           // pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);

            getitemlist();


          /*  pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                int Refreshcounter = 1; //Counting how many times user have refreshed the layout

                @Override
                public void onRefresh() {

                    try {

                         *//* String proceedamt[] = Product_List.totalamount.getText().toString().split("\\r?\\n");
                         String amountcate[] = Product_List.amountbycategary.getText().toString().split("\\r?\\n");*//*
                        Product_List.totalamount.setText(String.valueOf(Integer.parseInt( Product_List.totalamount.getText().toString())-Integer.parseInt(Product_List.categaryamount.getText().toString())));
                        Product_List.categaryitem.setText(category.getJSONObject(Product_List.selecttabid).getString("categoryName"));
                        Product_List.categaryamount.setText("0");
                        getitemlist();

                    }catch (Exception e){

                    }


                }
            });*/
        }


        public void getitemlist(){
            try {


                JSONObject jsonObject =category.getJSONObject(getArguments().getInt(ARG_SECTION_NUMBER));
                List<ProductVO> productVOs = categoryProductMap.get(jsonObject.getInt("categoryId"));
                if(productVOs!=null){
                    Product_List.categaryitem.setText(getContext().getString(R.string.Rs)+" : "+String.valueOf(Product_List.computeOrderProductAmt(Product_List.orderItemList)));

                    //pullToRefresh.setRefreshing(false);
                    Product_Adapter product_adapter=new Product_Adapter(getContext(),productVOs ,R.layout.item_design,recyclerView);
                    recyclerView.setAdapter(product_adapter);
                    return;
                }



                    VolleyUtils.makeJsonObjectRequest(getContext(), ProductBO.getTodayProductList(jsonObject.getInt("categoryId"), Session.getMandiId(getContext()),Session.getSessionByKey(getContext(),Session.CACHE_ORDERID)), new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                    }
                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;

                        if(!response.getString("status").equals("success")){
                            JSONArray error =response.getJSONArray("error");
                            imagebatchlock.setVisibility(View.VISIBLE);
                            errormsg.setVisibility(View.VISIBLE);

                            StringBuilder sb = new StringBuilder();
                            for(int i=0; i<error.length(); i++){
                                sb.append(error.get(i)).append("\n");
                            }

                            errormsg.setText(sb.toString());




                        }else {

                            JSONObject object=response.getJSONObject("dataList");
                            List<ProductVO> product_lists =new ArrayList<>();

                            JSONArray jsonArray =object.getJSONArray("productList");

                            int categoryId=0;
                            for(int i=0; i<jsonArray.length();i++ ){
                                JSONObject object1=jsonArray.getJSONObject(i);
                                ProductVO productVO =new ProductVO();

                                productVO.setProductName(object1.getString("productName"));
                                productVO.setProductID(object1.getInt("productId"));
                                productVO.setThumbnailImagePath(object1.getString("thumbnailImagePath"));
                                productVO.setMop(object1.getDouble("mop"));
                                productVO.setUnitTypeName(object1.getString("unitTypeName"));
                                productVO.setCategoryId(object1.getInt("categoryId"));
                                productVO.setCategoryName(object1.getString("categoryName"));
                                productVO.setMaximumQty(object1.getInt("maximumQty"));

                                productVO.setQty(object1.getInt("qty"));
                                productVO.setAvgweight(Double.parseDouble(object1.get("avgweight").toString()));


                                if(object1.has("nutritionImage")){
                                    productVO.setNutritionImage(object1.getString("nutritionImage"));
                                }


                                if(object1.has("utfCode")){
                                    productVO.setUtfCode(object1.getString("utfCode"));
                                }

                                if(Integer.valueOf(productVO.getQty())>0){
                                    productVO.setTotal(String.valueOf(productVO.getQty()*productVO.getMop().intValue()));
                                    Product_List.orderItemList.put(productVO.getCategoryId()+"-"+productVO.getProductID(),productVO);
                                }
                                product_lists.add(productVO);
                                categoryId = object1.getInt("categoryId");
                            }
                            categoryProductMap.put(categoryId, product_lists);
                            Product_List.categaryitem.setText(getContext().getString(R.string.Rs)+" : "+String.valueOf(Product_List.computeOrderProductAmt(Product_List.orderItemList)));

                            //pullToRefresh.setRefreshing(false);
                            Product_Adapter product_adapter=new Product_Adapter(getContext(),product_lists ,R.layout.item_design,recyclerView);
                            recyclerView.setAdapter(product_adapter);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            PlaceholderFragment placeholderFragment=new PlaceholderFragment();
            return placeholderFragment.newInstance(position);
        }
        @Override
        public int getCount() {
            return category.length();
        }
    }


}
