package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.AgreementActivity;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.entities.AboutInfoBean;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class AboutEasyFairActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private TextView version_tv;//版本号
    private TextView function_introduction_tv;//功能介绍
    private TextView system_notification_tv;//系统通知
    private TextView help_tv;//帮助与反馈
    private TextView check_version_tv;//检测新版本
    private TextView provision_tv;//条款与隐私政策
    private TextView commpany_tv;//某某公司所有
    private MyDialog myDialog;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    //关于的详细信息
    private String customerService;//客服
    private String functionIntro;//功能
    private String privacyPolicy;//版本检查
    private String sysInform;//系统
    private String lastVersion;//最新版本

    private String m_newVerName; //最新版的版本名
    private String m_appNameStr; //下载到本地要给这个APP命的名字

    private Handler m_mainHandler;
    private ProgressDialog m_progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_easy_fair);
        initYuyan();
        initview();
        initDateAboutInfo();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(AboutEasyFairActivity.this);
        if (language != null) {
            String tag = this.language.getTag();
            if (tag.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);

            } else if (tag.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }


    private void initview() {
        myDialog = new MyDialog(this);

        m_mainHandler = new Handler();
        m_progressDlg = new ProgressDialog(this);
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        m_progressDlg.setIndeterminate(false);
        m_appNameStr = "haha.apk";

        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        function_introduction_tv = (TextView) findViewById(R.id.function_introduction_tv);
        system_notification_tv = (TextView) findViewById(R.id.system_notification_tv);
        help_tv = (TextView) findViewById(R.id.help_tv);
        check_version_tv = (TextView) findViewById(R.id.check_version_tv);
        provision_tv = (TextView) findViewById(R.id.provision_tv);
        commpany_tv = (TextView) findViewById(R.id.commpany_tv);
        version_tv = (TextView) findViewById(R.id.version_tv);
        fanhui_rl.setOnClickListener(AboutEasyFairActivity.this);
        function_introduction_tv.setOnClickListener(AboutEasyFairActivity.this);
        system_notification_tv.setOnClickListener(AboutEasyFairActivity.this);
        help_tv.setOnClickListener(AboutEasyFairActivity.this);
        check_version_tv.setOnClickListener(AboutEasyFairActivity.this);
        provision_tv.setOnClickListener(AboutEasyFairActivity.this);
        commpany_tv.setOnClickListener(AboutEasyFairActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.function_introduction_tv: {
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("details", functionIntro);
                intent.setClass(AboutEasyFairActivity.this, AboutDetailsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.system_notification_tv: {
                Intent intent = new Intent();
                intent.putExtra("type", 3);
                intent.putExtra("details", sysInform);
                intent.setClass(AboutEasyFairActivity.this, AboutDetailsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.help_tv: {
                Intent intent = new Intent();
                intent.putExtra("type", 2);
                intent.putExtra("details", customerService);
                intent.setClass(AboutEasyFairActivity.this, AboutDetailsActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.check_version_tv:
                getVerCode(AboutEasyFairActivity.this);
                getVerName(AboutEasyFairActivity.this);
                new checkNewestVersionAsyncTask().execute();
                break;
            case R.id.provision_tv:
                Intent intent = new Intent(AboutEasyFairActivity.this, AgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取关于的信息
     */
    String tag = "About";

    private void initDateAboutInfo() {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "sysAbout/aboutEasyFair";
        JSONObject jsonObject = new JSONObject();
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.doPost(AboutEasyFairActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                Gson gson = new Gson();
                AboutInfoBean aboutInfoBean = gson.fromJson(result + "", AboutInfoBean.class);
                String code = aboutInfoBean.getCode();
                if ("200".equals(code)) {
                    customerService = aboutInfoBean.getCustomerService();
                    functionIntro = aboutInfoBean.getFunctionIntro();
                    lastVersion = aboutInfoBean.getLastVersion();
                    privacyPolicy = aboutInfoBean.getPrivacyPolicy();
                    sysInform = aboutInfoBean.getSysInform();
                    version_tv.setText(lastVersion);
                }
                myDialog.dialogDismiss();
            }

            @Override
            public void onError(VolleyError error) {
                myDialog.dialogDismiss();
            }
        });
    }

    /**
     * 检查是否有新版本
     */
    String tagAbout = "tagAbout";

    private void initDateVersion(final VersionUpdate versionUpdate) {
        String url = BaseUrl.HTTP_URL;
        JSONObject jsonObject = new JSONObject();
        HttpUtils.doPost(AboutEasyFairActivity.this, url, tagAbout, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                versionUpdate.updateSuccess();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    interface VersionUpdate {
        void updateSuccess();
    }

    @Override
    protected void onStop() {
        MyAppclication.getHttpQueues().cancelAll(tag);
        super.onStop();
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    "com.baibeiyun.eazyfair", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.baibeiyun.eazyfair", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }

    class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return !getVerName(AboutEasyFairActivity.this).equals(lastVersion);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                doNewVersionUpdate();
            } else {
                notNewVersionDlgShow();
            }
            super.onPostExecute(aBoolean);
        }

    }

    /**
     * 提示更新新版本
     */
    private void doNewVersionUpdate() {
        String verName = getVerName(AboutEasyFairActivity.this);

        String str = "当前版本：" + verName + "，发现新版本：" + lastVersion + "，是否更新？";
        Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.software_gx).setMessage(str)
                // 设置内容
                .setPositiveButton(R.string.update,// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                m_progressDlg.setTitle(R.string.down);
                                m_progressDlg.setMessage("等待中...");
                                downFile(BaseUrl.HTTP_URL + "apk/easyfair.apk");  //开始下载
                            }
                        })
                .setNegativeButton(R.string.no_update,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 点击"取消"按钮之后退出程序
                                dialog.dismiss();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    /**
     * 提示当前为最新版本
     */
    private void notNewVersionDlgShow() {

        String verName = getVerName(this);
        String str = "当前版本:" + verName + ",/n已是最新版,无需更新!";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")
                .setMessage(str)// 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    private void downFile(final String url) {
        m_progressDlg.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();

                    m_progressDlg.setMax((int) length);//设置进度条的最大值

                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                m_appNameStr);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                m_progressDlg.setProgress(count);
                            }
                        }
                    }
                    assert fileOutputStream != null;
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    down();  //告诉HANDER已经下载完成了，可以安装了
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 告诉HANDER已经下载完成了，可以安装了
     */
    private void down() {
        m_mainHandler.post(new Runnable() {
            public void run() {
                m_progressDlg.cancel();
                update();
            }
        });
    }

    /**
     * 安装程序
     */
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), m_appNameStr)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
