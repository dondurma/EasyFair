package com.baibeiyun.eazyfair.activity.business_opportunity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforSJ;
import com.baibeiyun.eazyfair.adapter.ExhibitionInfoAdapter;
import com.baibeiyun.eazyfair.entities.ExhibitionInfoBean;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.baibeiyun.eazyfair.view.XListView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExhibitionInfoActivity extends BaseActivityforSJ implements View.OnClickListener, XListView.IXListViewListener {
    private XListView exhibition_info_xlv;
    private ExhibitionInfoAdapter exhibitionInfoAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private RelativeLayout fanhui_rl;
    private MyDialog myDialog;
    private int pageNo, pageSize;
    //展品信息的接口调用
    private String tag = "ExHibition";

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private List<ExhibitionInfoBean.ExhibitionListBean> exhibitionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_info);
        initYuyan();
        myDialog = new MyDialog(this);
        init();
        pageNo = 1;
        pageSize = 10;
        initDateExHibition(
                new ExhibitionInfo() {
                    @Override
                    public void success() {
                        exhibitionInfoAdapter = new ExhibitionInfoAdapter(datas, ExhibitionInfoActivity.this);
                        exhibition_info_xlv.setAdapter(exhibitionInfoAdapter);
                        myDialog.dialogDismiss();
                    }

                });

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(this);
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


    private void init() {
        exhibition_info_xlv = (XListView) findViewById(R.id.exhibition_info_xlv);
        exhibition_info_xlv.setPullLoadEnable(true);//设置允许上拉加载
        exhibition_info_xlv.setPullRefreshEnable(true);//设置允许下拉刷新
        exhibition_info_xlv.setXListViewListener(ExhibitionInfoActivity.this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(ExhibitionInfoActivity.this);

        exhibition_info_xlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> itemAtPosition = (Map<String, Object>) adapterView.getItemAtPosition(i);
                if (itemAtPosition != null) {
                    Intent intent = new Intent(ExhibitionInfoActivity.this, ExhibitionDetailActivity.class);
                    String title = (String) itemAtPosition.get("title_tv");
                    String endTime = (String) itemAtPosition.get("time_tv");
                    String startTime = (String) itemAtPosition.get("startTime");
                    String address = (String) itemAtPosition.get("address");
                    String chargeStandard = (String) itemAtPosition.get("chargeStandard");
                    String city = (String) itemAtPosition.get("city");
                    String contact = (String) itemAtPosition.get("contact");
                    String introduction = (String) itemAtPosition.get("introduction");
                    String organiser = (String) itemAtPosition.get("organiser");
                    String pavilionName = (String) itemAtPosition.get("pavilionName");
                    String range = (String) itemAtPosition.get("range");
                    String state = (String) itemAtPosition.get("state");
                    String telephone = (String) itemAtPosition.get("telephone");
                    String type = (String) itemAtPosition.get("type");

                    intent.putExtra("title", title);
                    intent.putExtra("endTime", endTime);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("address", address);
                    intent.putExtra("chargeStandard", chargeStandard);
                    intent.putExtra("city", city);
                    intent.putExtra("contact", contact);
                    intent.putExtra("introduction", introduction);
                    intent.putExtra("organiser", organiser);
                    intent.putExtra("pavilionName", pavilionName);
                    intent.putExtra("range", range);
                    intent.putExtra("state", state);
                    intent.putExtra("telephone", telephone);
                    intent.putExtra("type", type);

                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
        }
    }

    //下拉刷新的方法
    @Override
    public void onRefresh() {
        pageNo--;
        if (pageNo == 0) {
            pageNo = 1;
            ToastUtil.showToast(ExhibitionInfoActivity.this, "暂无数据更新！");
            onLoad();
        } else {
            datas.clear();
            initDateExHibition(new ExhibitionInfo() {
                @Override
                public void success() {
                    exhibitionInfoAdapter.notifyDataSetChanged();
                    myDialog.dialogDismiss();
                    onLoad();
                }

            });
        }
    }

    //上拉加载的方法
    @Override
    public void onLoadMore() {
        pageNo++;
        initDateExHibition(new ExhibitionInfo() {
            @Override
            public void success() {
                exhibitionInfoAdapter.notifyDataSetChanged();
                onLoad();
                myDialog.dialogDismiss();
            }

        });
    }


    private void initDateExHibition(final ExhibitionInfo exhibitionInfo) {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "sysExhibition/getExhibitionList";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
            jsonObject.put("pageNo", pageNo);
            jsonObject.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(ExhibitionInfoActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if ("200".equals(result.getString("code"))) {
                        Gson gson = new Gson();
                        ExhibitionInfoBean exhibitionInfoBean = gson.fromJson(result + "", ExhibitionInfoBean.class);
                        exhibitionList = exhibitionInfoBean.getExhibitionList();
                        if (exhibitionList != null && exhibitionList.size() > 0) {
                            for (int i = 0; i < exhibitionList.size(); i++) {
                                ExhibitionInfoBean.ExhibitionListBean exhibitionListBean = exhibitionList.get(i);
                                String title = exhibitionListBean.getTitle();
                                long startTime = exhibitionListBean.getStartTime();
                                long endTime = exhibitionListBean.getEndTime();

                                String address = exhibitionListBean.getAddress();
                                String chargeStandard = exhibitionListBean.getChargeStandard();
                                String city = exhibitionListBean.getCity();
                                String contact = exhibitionListBean.getContact();
                                String introduction = exhibitionListBean.getIntroduction();
                                String organiser = exhibitionListBean.getOrganiser();
                                String pavilionName = exhibitionListBean.getPavilionName();
                                String range = exhibitionListBean.getRange();
                                String state = exhibitionListBean.getState();
                                String telephone = exhibitionListBean.getTelephone();
                                String type = exhibitionListBean.getType();
                                int id = exhibitionListBean.getId();
                                Map<String, Object> map = new HashMap<>();
                                if (title == null) {
                                    map.put("title_tv", "");
                                } else {
                                    map.put("title_tv", title);
                                }
                                map.put("time_tv", timeFormat(endTime));
                                map.put("startTime", timeFormat(startTime));
                                map.put("address", address);
                                map.put("chargeStandard", chargeStandard);
                                map.put("city", city);
                                map.put("contact", contact);
                                map.put("introduction", introduction);
                                map.put("organiser", organiser);
                                map.put("pavilionName", pavilionName);
                                map.put("range", range);
                                map.put("state", state);
                                map.put("telephone", telephone);
                                map.put("type", type);
                                map.put("id", id);
                                datas.add(map);
                            }

                        } else {
                            if (pageNo != 1) {
                                pageNo--;
                                Toast.makeText(ExhibitionInfoActivity.this, R.string.no_more_data, Toast.LENGTH_SHORT).show();
                            }
                        }
                        exhibitionInfo.success();
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


    private String timeFormat(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return format.format(date);
    }

    private interface ExhibitionInfo {
        void success();

    }

    /**
     * 停止刷新
     */
    private void onLoad() {
        exhibition_info_xlv.stopRefresh();
        exhibition_info_xlv.stopLoadMore();
    }
}
