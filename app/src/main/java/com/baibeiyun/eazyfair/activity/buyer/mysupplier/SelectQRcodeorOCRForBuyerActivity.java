package com.baibeiyun.eazyfair.activity.buyer.mysupplier;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.CardBean;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.HttpUtil;
import com.google.gson.Gson;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SelectQRcodeorOCRForBuyerActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private Button qr_bt, mingpian_bt;
    private ProgressBar ll_progress;

    private String picPath;//图片存储路径
    private String sdPath;//SD卡的路径
    private final int TAKEPHOTO_CODE = 2;
    private static byte[] bytes;
    public static final String action = "namecard.scan";
    private static String extension;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    public static SelectQRcodeorOCRForBuyerActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_for_buyer);
        activity = this;
        initYuyan();
        initview();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        select();
        if (listforlanguage != null) {
            String tag = language.getTag();
            if (tag.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);

            } else if (tag.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }

    // 查询数据库的方法
    private ArrayList<Language> select() {
        listforlanguage.clear();
        LanguageDao languageDao = new LanguageDao(this);
        Cursor cursor = languageDao.selectById("easyfair");
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            language = new Language(id, tag);
            listforlanguage.add(language);
        }
        cursor.close();
        return listforlanguage;

    }


    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        qr_bt = (Button) findViewById(R.id.qr_bt);
        mingpian_bt = (Button) findViewById(R.id.mingpian_bt);
        ll_progress = (ProgressBar) findViewById(R.id.ll_progress);

        fanhui_rl.setOnClickListener(SelectQRcodeorOCRForBuyerActivity.this);
        qr_bt.setOnClickListener(SelectQRcodeorOCRForBuyerActivity.this);
        mingpian_bt.setOnClickListener(SelectQRcodeorOCRForBuyerActivity.this);
    }

    private void initData() {
        sdPath = Environment.getExternalStorageDirectory().getPath();
        picPath = sdPath + "/" + "temp.png";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.qr_bt:
                Intent intent = new Intent(SelectQRcodeorOCRForBuyerActivity.this, CaptureActivity.class);
                intent.putExtra("tag",2);
                startActivityForResult(intent, 0);
                break;
            //扫描名片
            case R.id.mingpian_bt:
                takePhoto(mingpian_bt);
                break;
        }
    }

    //重写onActivityResult方法 获取从CaptureRequest返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKEPHOTO_CODE) {
                FileInputStream inputStream = null;
                try {
                    //把图片转化为字节流
                    inputStream = new FileInputStream(picPath);
                    bytes = HttpUtil.Inputstream2byte(inputStream);
                    if (!(bytes.length > (1000 * 1024 * 5))) {
                        new MyAsynTask().execute();
                    } else {
//                        Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, "图片太大!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.pic_too_big, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                Intent intent = new Intent(SelectQRcodeorOCRForBuyerActivity.this, EditCardForQRcodeActivity.class);
                if (result != null && !result.isEmpty()) {
                    intent.putExtra("result", result);
                    startActivity(intent);
                    finish();
                } else {
//                    Toast.makeText(this, "请重新扫描！", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, R.string.please_cxsm, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void takePhoto(View view) {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(picPath);
        Uri uri = Uri.fromFile(file);
        //为拍摄的图片指定一个存储的路径
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent2, TAKEPHOTO_CODE);
    }

    class MyAsynTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            ll_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return startScan();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                ll_progress.setVisibility(View.GONE);
                handleResult(result);
            }
        }
    }

    /**
     * 处理服务器返回的结果
     *
     * @param result
     */
    private Map<String, Object> map = new ArrayMap<>();

    private void handleResult(String result) {
        Gson gson = new Gson();
        CardBean cardBean = gson.fromJson(result, CardBean.class);
        String status = cardBean.getStatus();
        if ("OK".equals(status)) {
            String deserve = "";
            CardBean.DataBean data = cardBean.getData();
            List<CardBean.DataBean.FBean> f = data.getF();
            if (f != null && f.size() > 0) {
                for (int i = 0; i < f.size(); i++) {
                    CardBean.DataBean.FBean fBean = f.get(i);
                    String key = fBean.getN();//姓名
                    Object vause = fBean.getV();
                    if (vause instanceof List) {
                        List vause1 = (List) vause;
                        if (vause1.size() > 0) {
                            for (int k = 0; k < vause1.size(); k++) {
                                deserve += (String) vause1.get(k);
                            }
                            Log.d("stringBuilder", deserve);
                            map.put(key, deserve);
                        }
                    } else if (vause instanceof String) {
                        if ("".equals(vause)) {
                            map.put(key, "");
                        } else {
                            map.put(key, vause);
                        }
                    }

                }
            }
            Object name = map.get("name");//姓名
            Object company = map.get("company");//公司
            Object department = map.get("department");//部门
            Object jobtitle = map.get("jobtitle");//职位
            Object tel_main = map.get("tel_main");//电话
            Object tel_mobile = map.get("tel_mobile");//手机
            Object tel_home = map.get("tel_home");//家庭电话
            Object tel_inter = map.get("tel_inter");//直拨电话
            Object fax = map.get("fax");//传真
            Object pager = map.get("pager");//传呼机
            Object web = map.get("web");//网址
            Object email = map.get("email");//邮箱
            Object address = map.get("address");//地址
            Object postcode = map.get("postcode");//邮编
            Object icq = map.get("ICQ");//QQ或其他的社交软件

//            Log.d("handleResult", "handleResult: " + name + "\n" + company + "\n" + department + "\n" + jobtitle + "\n" + tel_main + "\n" + tel_mobile + "\n" + tel_home + "\n" +
//                    tel_inter + "\n" + fax + "\n" + pager + "\n" + web + "\n" + email + "\n" + address + "\n" + postcode + "\n" + icq);
            Intent intent = new Intent(getApplicationContext(), EditCardForOCRBuyerActvity.class);
            intent.putExtra("name", (String) name);
            intent.putExtra("company", (String) company);
            intent.putExtra("department", (String) department);
            intent.putExtra("jobtitle", (String) jobtitle);
            intent.putExtra("tel_main", (String) tel_main);
            intent.putExtra("tel_mobile", (String) tel_mobile);

            intent.putExtra("tel_home", (String) tel_home);
            intent.putExtra("tel_inter", (String) tel_inter);
            intent.putExtra("fax", (String) fax);
            intent.putExtra("pager", (String) pager);
            intent.putExtra("web", (String) web);

            intent.putExtra("email", (String) email);
            intent.putExtra("address", (String) address);
            intent.putExtra("postcode", (String) postcode);
            intent.putExtra("icq", (String) icq);


            intent.putExtra("url", picPath);
            startActivity(intent);


        } else if ("-90".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.no_permissions, Toast.LENGTH_SHORT).show();
        } else if ("-91".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.credit_low, Toast.LENGTH_SHORT).show();
        } else if ("-92".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.frozen, Toast.LENGTH_SHORT).show();
        } else if ("-98".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.time_limit, Toast.LENGTH_SHORT).show();
        } else if ("-99".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.verification_false, Toast.LENGTH_SHORT).show();
        } else if ("-100".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.usename_false, Toast.LENGTH_SHORT).show();
        } else if ("-101".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.upload_fail, Toast.LENGTH_SHORT).show();
        } else if ("-102".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.upload_to_large, Toast.LENGTH_SHORT).show();
        } else if ("-106".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.request_false, Toast.LENGTH_SHORT).show();
        } else if ("-110".equals(status)) {
            Toast.makeText(SelectQRcodeorOCRForBuyerActivity.this, R.string.recognition_false, Toast.LENGTH_SHORT).show();
        }
    }

    public static String startScan() {
        String xml = HttpUtil.getSendXML(action, "2", "0", extension);
        return HttpUtil.send(xml, bytes);
    }
}
