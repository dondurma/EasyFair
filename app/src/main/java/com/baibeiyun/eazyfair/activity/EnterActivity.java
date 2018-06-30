package com.baibeiyun.eazyfair.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.business_opportunity.BusinessActivity;
import com.baibeiyun.eazyfair.activity.buyer.MyBuyerActivity;
import com.baibeiyun.eazyfair.activity.buyer.mygoods.NewGoodsOneForBuyerActivity;
import com.baibeiyun.eazyfair.activity.myaccount.AccountActivity;
import com.baibeiyun.eazyfair.activity.myaccount.BaseDataActivity;
import com.baibeiyun.eazyfair.activity.myaccount.SystemSettingsActivity;
import com.baibeiyun.eazyfair.activity.supplier.MySupplierActivity;
import com.baibeiyun.eazyfair.activity.supplier.mygoods.NewGoodsActivityone;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MessageBean;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EnterActivity extends BaseActivity implements View.OnClickListener {
    private Button supplier_bt, buyer_bt, business_bt;
    //我的账户
    private RelativeLayout my_account_rl;
    //消息
    private FrameLayout news_rl;
    private MyUser myUser;
    private ImageView redpoint_iv;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private String s;
    private String item;

    //获取咨询消息列表
    private String tag = "consult";
    private MyDialog myDialog;
    private int pageNo = 1;
    private int pageSize = 10;

    private ArrayList<Map<String, Object>> datas = new ArrayList<>();

    private String tag1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        initYuyan();
        initview();
//        initData();
        initOpenExcel();

    }

    private void initOpenExcel() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            String dataString = intent.getDataString();
            String[] split = dataString.split("\\///");
            s = split[1];
            showAlertDialog();
        }
    }


    private void initYuyan() {
        // 获得res资源对象
        resources = getResources();
        // 获得设置对象
        config = resources.getConfiguration();
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(this);
        if (language != null) {
            tag1 = this.language.getTag();
            if ("en".equals(tag1)) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);
            } else if ("ch".equals(tag1)) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }

    private void initData() {
        myUser = CursorUtils.selectInfo(EnterActivity.this);
        if (this.myUser == null || this.myUser.getCh_company_name() == null && this.myUser.getCh_contact() == null) {
            showDialog(EnterActivity.this, R.string.save_info);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        myUser = CursorUtils.selectInfo(EnterActivity.this);
//        if (this.myUser == null || this.myUser.getCh_company_name() == null && this.myUser.getCh_contact() == null) {
//            showDialog(EnterActivity.this, R.string.save_info);
//        }
//        getInfoData();
    }

    private void initview() {
        myDialog = new MyDialog(EnterActivity.this);
        redpoint_iv = (ImageView) findViewById(R.id.redpoint_iv);
        my_account_rl = (RelativeLayout) findViewById(R.id.my_account_rl);
        news_rl = (FrameLayout) findViewById(R.id.news_rl);
        supplier_bt = (Button) findViewById(R.id.supplier_bt);
        buyer_bt = (Button) findViewById(R.id.buyer_bt);
        business_bt = (Button) findViewById(R.id.business_bt);
        my_account_rl.setOnClickListener(EnterActivity.this);
        news_rl.setOnClickListener(EnterActivity.this);
        supplier_bt.setOnClickListener(EnterActivity.this);
        buyer_bt.setOnClickListener(EnterActivity.this);
        business_bt.setOnClickListener(EnterActivity.this);
//        getInfoData();

    }

    private void getInfoData() {
        getInformationData(
                new InformationActivity.MessageSuccess() {
                    @Override
                    public void success() {
                        List<String> mlist = new ArrayList<>();
                        mlist.clear();
                        for (int i = 0; i < datas.size(); i++) {
                            Map<String, Object> stringObjectMap = datas.get(i);
                            String hasReadState = (String) stringObjectMap.get("hasReadState");
                            if ("0".equals(hasReadState)) {
                                mlist.add(hasReadState);
                            }
                        }
                        if (mlist != null && mlist.size() > 0) {
                            redpoint_iv.setVisibility(View.VISIBLE);
                        }
                    }
                }

        );
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //我的供应商
            case R.id.supplier_bt:
                intent = new Intent(EnterActivity.this, MySupplierActivity.class);
                startActivity(intent);
                break;
            //我的采购商
            case R.id.buyer_bt:
                intent = new Intent(EnterActivity.this, MyBuyerActivity.class);
                startActivity(intent);
                break;
            //商机
            case R.id.business_bt:
                intent = new Intent(EnterActivity.this, BusinessActivity.class);
                startActivity(intent);
                break;
            //我的账户
            case R.id.my_account_rl:
                intent = new Intent(EnterActivity.this, AccountActivity.class);
                startActivity(intent);
                break;
            //消息
            case R.id.news_rl:
                intent = new Intent(EnterActivity.this, InformationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void showDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.click_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EnterActivity.this, BaseDataActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();

            }
        });
        builder.setNegativeButton(R.string.to_see, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EnterActivity.this);
        final String[] items = {"商品模板数据（供应商）", "商品模板数据（采购商）", "报价数据", "询价数据", "数据库"};
        builder.setTitle("导入到");
        //第二个参数是设置默认选中哪一项-1代表默认都不选
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item = items[which];
            }
        });
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = null;
                switch (item) {
                    case "商品模板数据（供应商）":
                        intent = new Intent(EnterActivity.this, NewGoodsActivityone.class);
                        intent.putExtra("path", s);
                        break;
                    case "商品模板数据（采购商）":
                        intent = new Intent(EnterActivity.this, NewGoodsOneForBuyerActivity.class);
                        intent.putExtra("path", s);
                        break;
                    case "报价数据":
                        intent = new Intent(EnterActivity.this, MyBuyerActivity.class);
                        intent.putExtra("path", s);
                        break;
                    case "询价数据":
                        intent = new Intent(EnterActivity.this, MySupplierActivity.class);
                        intent.putExtra("path", s);
                        break;
                    case "数据库":
                        intent = new Intent(EnterActivity.this, SystemSettingsActivity.class);
                        intent.putExtra("path", s);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
                dialogInterface.dismiss();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }


    /**
     * 获取资讯列表的方法
     */
    private void getInformationData(final InformationActivity.MessageSuccess messageSuccess) {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "sysUserMessage/getMessageByType";
        JSONObject jsonObject = new JSONObject();
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
            jsonObject.put("type", 0);
            jsonObject.put("pageNo", pageNo);
            jsonObject.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(EnterActivity.this, url, tag, jsonObject, new VolleyCallBack(EnterActivity.this, VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                Gson gson = new Gson();
                MessageBean messageBean = gson.fromJson(result + "", MessageBean.class);
                String code = messageBean.getCode();

                if ("200".equals(code)) {
                    datas.clear();
                    List<MessageBean.MessageListBean> messageList = messageBean.getMessageList();
                    if (messageList != null && messageList.size() > 0) {
                        for (int i = 0; i < messageList.size(); i++) {
                            Map<String, Object> map = new ArrayMap<>();
                            MessageBean.MessageListBean messageListBean = messageList.get(i);
                            //当前状态
                            String hasReadState = messageListBean.getHasReadState();
                            map.put("hasReadState", hasReadState);
                            datas.add(map);
                        }
                        messageSuccess.success();
                    }
                }
                myDialog.dialogDismiss();
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }


}
