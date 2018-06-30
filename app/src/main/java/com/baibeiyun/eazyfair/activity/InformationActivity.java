package com.baibeiyun.eazyfair.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.InformationAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MessageBean;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.baibeiyun.eazyfair.view.XListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class InformationActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private RelativeLayout fanhui_rl;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private XListView zixun_lv;
    private InformationAdapter informationAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private int pageNo = 1;
    private int pageSize = 10;
    private MyDialog myDialog;
    private String tag = "consult";//获取咨询消息列表
    private String tag2 = "delete_info";
    private Handler mHandler;

    private Map<Integer, Integer> map = new HashMap<>();
    private List<CheckBox> mlist = new ArrayList<>();//存放CheckBox
    private List<Integer> listfortips = new ArrayList<>();//将该map集合加到list集合中

    private RelativeLayout delete_rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initYuyan();
        initView();
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(InformationActivity.this);
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


    private void initView() {
        mHandler = new Handler();
        delete_rl = (RelativeLayout) findViewById(R.id.delete_rl);
        delete_rl.setOnClickListener(this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(InformationActivity.this);
        zixun_lv = (XListView) findViewById(R.id.zixun_lv);
        myDialog = new MyDialog(InformationActivity.this);
        zixun_lv.setPullLoadEnable(true);//设置允许上拉加载
        zixun_lv.setPullRefreshEnable(true);//设置允许下拉刷新
        zixun_lv.setXListViewListener(this);
        getInformationData(
                new MessageSuccess() {
                    @Override
                    public void success() {
                        informationAdapter = new InformationAdapter(datas, InformationActivity.this, map, mlist);
                        zixun_lv.setAdapter(informationAdapter);
                    }
                });
        zixun_lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                        if (map == null) return;
                        String acceptTime = map.get("acceptTime").toString();
                        String content_tv = map.get("content_tv").toString();
                        String createTime = map.get("createTime").toString();
                        String hasReadState = map.get("hasReadState").toString();
                        String messageId = map.get("messageId").toString();
                        String readTime = map.get("readTime").toString();
                        String title_tv = map.get("title_tv").toString();
                        String type = map.get("type").toString();

                        Intent intent = new Intent();
                        intent.putExtra("acceptTime", acceptTime);
                        intent.putExtra("content_tv", content_tv);
                        intent.putExtra("createTime", createTime);
                        intent.putExtra("hasReadState", hasReadState);
                        intent.putExtra("messageId", messageId);
                        intent.putExtra("readTime", readTime);
                        intent.putExtra("title_tv", title_tv);
                        intent.putExtra("type", type);
                        intent.setClass(InformationActivity.this, MessageDetailsActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });


        zixun_lv.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < datas.size(); i++) {
                            informationAdapter.getIsvisibleMap().put(i, CheckBox.VISIBLE);
                        }
                        informationAdapter.notifyDataSetChanged();
                        delete_rl.setVisibility(View.VISIBLE);
                        return true;
                    }
                });


    }

    //获取资讯列表的方法
    private void getInformationData(final MessageSuccess messageSuccess) {
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
        HttpUtils.doPost(InformationActivity.this, url, tag, jsonObject,
                new VolleyCallBack(InformationActivity.this, VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Gson gson = new Gson();
                        MessageBean messageBean = gson.fromJson(result + "", MessageBean.class);
                        String code = messageBean.getCode();
                        if ("200".equals(code)) {
                            List<MessageBean.MessageListBean> messageList = messageBean.getMessageList();
                            if (messageList != null && messageList.size() > 0) {
                                for (int i = 0; i < messageList.size(); i++) {
                                    Map<String, Object> map = new ArrayMap<>();
                                    MessageBean.MessageListBean messageListBean = messageList.get(i);

                                    long acceptTime = messageListBean.getAcceptTime();//接收时间
                                    String content = messageListBean.getContent();//内容
                                    long createTime = messageListBean.getCreateTime();//创建时间
                                    String hasReadState = messageListBean.getHasReadState();//当前状态
                                    int messageId = messageListBean.getMessageId();//ID
                                    long readTime = messageListBean.getReadTime();//阅读时间
                                    String title = messageListBean.getTitle();//标题
                                    String type = messageListBean.getType();//信息类型

                                    map.put("acceptTime", acceptTime);
                                    map.put("content_tv", content);
                                    map.put("createTime", createTime);
                                    map.put("hasReadState", hasReadState);
                                    map.put("messageId", messageId);
                                    map.put("readTime", readTime);
                                    map.put("title_tv", title);
                                    map.put("type", type);
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


    //下拉刷新
    @Override
    public void onRefresh() {
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        pageNo--;
                        if (pageNo == 0) {
                            pageNo = 1;
                            ToastUtil.showToast2(InformationActivity.this, R.string.allreadly_top);
                            onLoad();
                        } else {
                            datas.clear();
                            getInformationData(
                                    new MessageSuccess() {
                                        @Override
                                        public void success() {
                                            informationAdapter = new InformationAdapter(datas, InformationActivity.this, map, mlist);
                                            zixun_lv.setAdapter(informationAdapter);
                                        }
                                    });
                        }

                    }
                }, 1000);


    }

    //上拉加载
    @Override
    public void onLoadMore() {
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        pageNo++;
                        getInformationData(
                                new MessageSuccess() {
                                    @Override
                                    public void success() {
                                        informationAdapter = new InformationAdapter(datas, InformationActivity.this, map, mlist);
                                        zixun_lv.setAdapter(informationAdapter);
                                    }
                                });
                        onLoad();

                    }
                }, 1000);

    }

    interface MessageSuccess {
        void success();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            //删除
            case R.id.delete_rl:
                getGoodsData();
                if (listfortips != null && listfortips.size() > 0) {
                    deleteInfo();

                } else {
                    ToastUtil.showToast2(InformationActivity.this, R.string.please_yscdtm);
                }

                break;


        }
    }

    public void getGoodsData() {
        if (map == null) return;
        Set<Integer> integers = map.keySet();
        for (Integer key : integers) {
            Integer s = map.get(key);
            listfortips.add(s);
        }
    }

    //停止刷新
    private void onLoad() {
        zixun_lv.stopRefresh();
        zixun_lv.stopLoadMore();
    }

    //删除的接口
    private void deleteInfo() {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "sysUserMessage/deleteMessage";
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        try {
            jsonObject.put("token", token);
            for (int i = 0; i < listfortips.size(); i++) {
                jsonArray.put(listfortips.get(i));
            }
            jsonObject.put("idList", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(InformationActivity.this, url, tag2, jsonObject,
                new VolleyCallBack(InformationActivity.this, VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if ("200".equals(result.getString("code"))) {
                                datas.clear();
                                myDialog.dialogDismiss();
                                getInformationData(new MessageSuccess() {
                                    @Override
                                    public void success() {
                                        informationAdapter = new InformationAdapter(datas, InformationActivity.this, map, mlist);
                                        zixun_lv.setAdapter(informationAdapter);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {


                    }
                });


    }


}
