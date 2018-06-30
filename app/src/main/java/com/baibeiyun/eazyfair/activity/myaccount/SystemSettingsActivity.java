package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SystemSettingsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private LinearLayout language_selection_ll;//语言选择
    private RelativeLayout account_protected_rl;//账号保护
    private LinearLayout pwd_modify_ll;//密码修改
    private RelativeLayout news_attention_modify_rl;//消息提醒修改
    private LinearLayout data_backup_tv;//数据备份
    private LinearLayout goods_mould_tv;//商品模板
//    private LinearLayout change_skin_tv;//更换皮肤

    private TextView langueTv;
    private TextView defautlTv;
    private TextView isOffTv;
    private TextView remindTv;
    private int from = 0;
    private PopupWindow mPopupWindow;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    private String protect = "0";
    private String message = "0";
    private String languageTag = "0";

    private List<String> lstFile = new ArrayList<>();  //结果 List
    private String path;
    private String tag = "System";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        initYuyan();
        initview();
        initSetData();
        inputbackData();
    }

    //导入备份数据库的方法
    private void inputbackData() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        if (path != null) {
//            copyFile(path, "/data/user/0/com.baibeiyun.eazyfair/databases/eazyfair.db", 2);
            copyFile(path, "/data/user/0/com.baibeiyun.eazyfair/databases/eazyfair.txt", 2);
        }
    }

    private void initSetData() {
        if (language.getTag().equals("ch")) {
            langueTv.setText("中文");
            defautlTv.setText("默认");

        } else {
            langueTv.setText("English");
            defautlTv.setText("");
        }


    }

    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        language_selection_ll = (LinearLayout) findViewById(R.id.language_selection_ll);
        account_protected_rl = (RelativeLayout) findViewById(R.id.account_protected_rl);
        pwd_modify_ll = (LinearLayout) findViewById(R.id.pwd_modify_ll);
        news_attention_modify_rl = (RelativeLayout) findViewById(R.id.news_attention_modify_rl);
        data_backup_tv = (LinearLayout) findViewById(R.id.data_backup_tv);
        goods_mould_tv = (LinearLayout) findViewById(R.id.goods_mould_tv);
//        change_skin_tv = (LinearLayout) findViewById(R.id.change_skin_tv);
        fanhui_rl.setOnClickListener(SystemSettingsActivity.this);
        goods_mould_tv.setOnClickListener(SystemSettingsActivity.this);
        data_backup_tv.setOnClickListener(this);
        pwd_modify_ll.setOnClickListener(this);
        language_selection_ll.setOnClickListener(this);
        account_protected_rl.setOnClickListener(this);
        news_attention_modify_rl.setOnClickListener(this);
        langueTv = (TextView) findViewById(R.id.langue);
        defautlTv = (TextView) findViewById(R.id.defautl);
        isOffTv = (TextView) findViewById(R.id.isOff);
        remindTv = (TextView) findViewById(R.id.remind);
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


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            //切换语言
            case R.id.language_selection_ll:
                from = Location.BOTTOM.ordinal();
                initPopupWindow();
                break;
            case R.id.account_protected_rl:
                if (SystemSettingsActivity.this.getString(R.string.not_kq).equals(isOffTv.getText())) {
                    isOffTv.setText(SystemSettingsActivity.this.getString(R.string.kq));
                    protect = "0";
                } else if (SystemSettingsActivity.this.getString(R.string.kq).equals(isOffTv.getText())) {
                    isOffTv.setText(SystemSettingsActivity.this.getString(R.string.not_kq));
                    protect = "1";
                }
                initDateSyetem();
                break;
            case R.id.pwd_modify_ll:
                startActivity(new Intent(SystemSettingsActivity.this, EditPassWordActivity.class));
                break;
            case R.id.news_attention_modify_rl:
                if (SystemSettingsActivity.this.getString(R.string.remind).equals(remindTv.getText())) {
                    remindTv.setText(SystemSettingsActivity.this.getString(R.string.unremind));
                    message = "0";
                } else if (SystemSettingsActivity.this.getString(R.string.unremind).equals(remindTv.getText())) {
                    remindTv.setText(SystemSettingsActivity.this.getString(R.string.remind));
                    message = "1";
                }
                initDateSyetem();
                break;
            //备份数据库数据
            case R.id.data_backup_tv:
                from = Location.BOTTOM.ordinal();
                initPopupWindow2();
                break;
            case R.id.goods_mould_tv:
                intent = new Intent(SystemSettingsActivity.this, Goods_templet_oneActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void initDateSyetem() {
        select();
        if ("ch".equals(language.getTag())) {
            languageTag = "0";
        } else {
            languageTag = "1";
        }
        String url = BaseUrl.HTTP_URL + "user/systemSet";
        JSONObject jsonObject = new JSONObject();
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
            jsonObject.put("language", languageTag);
            jsonObject.put("accountProtectionState", protect);
            jsonObject.put("messageReminderState", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(SystemSettingsActivity.this, url, this.tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        MyAppclication.getHttpQueues().cancelAll(tag);
        super.onStop();
    }

    //上传图片的PopupWindow弹出的方向
    public enum Location {
        BOTTOM
    }

    //中英文切换弹出的popupWindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_select_language, null);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_system_settings, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(SystemSettingsActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        TextView chinese_tv = (TextView) popupWindowView.findViewById(R.id.chinese_tv);
        TextView english_tv = (TextView) popupWindowView.findViewById(R.id.english_tv);
        TextView cancel_tv = (TextView) popupWindowView.findViewById(R.id.cancel_tv);
        //中文
        chinese_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                onCreate(null);
                updateforCh();
                initDateSyetem();
                langueTv.setText("中文");
                defautlTv.setText("默认");
                ToastUtil.showToast2(SystemSettingsActivity.this, R.string.switch_success);
                defautlTv.setVisibility(View.VISIBLE);

                mPopupWindow.dismiss();
            }
        });
        //英文
        english_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);
                onCreate(null);
                updateforEn();
                initDateSyetem();
                defautlTv.setVisibility(View.GONE);
                langueTv.setText("English");
                ToastUtil.showToast2(SystemSettingsActivity.this, R.string.switch_success);
                mPopupWindow.dismiss();
            }
        });
        //取消
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPopupWindow.dismiss();
            }
        });


    }

    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(SystemSettingsActivity.this, 1f);
        }
    }


    // 修改的方法
    private void updateforCh() {
        Language language = new Language("easyfair", "ch");
        LanguageDao languageDao = new LanguageDao(this);
        int rows = languageDao.updateData(language);
        if (rows == 0) {
            Toast.makeText(getApplicationContext(), R.string.modify_failed, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.modify_success, Toast.LENGTH_LONG).show();
        }

    }

    // 修改的方法
    private void updateforEn() {
        Language language = new Language("easyfair", "en");
        LanguageDao languageDao = new LanguageDao(this);
        int rows = languageDao.updateData(language);
        if (rows == 0) {
            Toast.makeText(getApplicationContext(), R.string.modify_failed, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.modify_success, Toast.LENGTH_LONG).show();
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

    //备份数据库弹出的popupWindow
    private void initPopupWindow2() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_back_up_data, null);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_system_settings, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(SystemSettingsActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        TextView export_data_tv = (TextView) popupWindowView.findViewById(R.id.export_data_tv);
        TextView cancel_tv = (TextView) popupWindowView.findViewById(R.id.cancel_tv);
        //导出备份数据
        export_data_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                copyFile("/data/user/0/com.baibeiyun.eazyfair/databases/eazyfair.txt", Environment.getExternalStorageDirectory() + File.separator + "EFbackups/eazyfair.txt", 1);
                copyFile("/data/user/0/com.baibeiyun.eazyfair/databases/eazyfair.txt", Environment.getExternalStorageDirectory() + File.separator +"EASYFAIR"+getCurrentData()+".txt", 1);
                mPopupWindow.dismiss();
            }
        });

        //取消
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    private void sendData() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator +"EASYFAIR"+getCurrentData()+".txt");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/octet-stream");
        Intent.createChooser(intent, "Choose Email Client");
        startActivity(intent);
    }

    /**
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath, int status) {
        MyDialog myDialog = null;
        myDialog = new MyDialog(SystemSettingsActivity.this);
        myDialog.dialogShow();
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                if (status == 1) {
                    ToastUtil.showToast2(SystemSettingsActivity.this, R.string.export_success);
                } else if (status == 2) {
                    ToastUtil.showToast2(SystemSettingsActivity.this, R.string.import_success);
                }
                myDialog.dialogDismiss();
            }
            if (status == 1) {
                sendData();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    public void GetFiles(String Path, String Extension, boolean IsIterative) {  //搜索目录，扩展名，是否进入子文件夹
        File[] files = new File(Path).listFiles();
        for (File f : files) {
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension))  //判断扩展名
                    lstFile.add(f.getPath());
                if (!IsIterative)
                    break;
            } else if (f.isDirectory() && !f.getPath().contains("/."))  //忽略点文件（隐藏文件/文件夹）
                GetFiles(f.getPath(), Extension, IsIterative);
        }

    }

    //删除文件
    public static void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }


    private String getCurrentData() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String format = df.format(new Date());
        return format;
    }


}
