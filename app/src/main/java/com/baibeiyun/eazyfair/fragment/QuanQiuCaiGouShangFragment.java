package com.baibeiyun.eazyfair.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.business_opportunity.BusinessTheWordSupplierActivity;
import com.baibeiyun.eazyfair.adapter.BusinessAdapter;
import com.baibeiyun.eazyfair.entities.BussinessGY;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class QuanQiuCaiGouShangFragment extends Fragment {
    private View view;
    private int pageNo = 1;
    private int pageSize = 10;
    private BusinessAdapter adapter;
    private List<BussinessGY.BusinessOpportunityListBean> list = new ArrayList<>();
    private PullToRefreshListView pullToRefreshListView;
    private int fragmentTag;
    //全球供应商信息
    private String tag = "QuanQiuCaiGouShangFragment";
    private String firstIndustryType;
    private String secondIndustryType;
    private String thirdIndustryType;
    private String fourthIndustryType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quanhangyecaigoushang_layout, null);
        initView(view);
        list.clear();
        if (2 == fragmentTag) {
            initDate(2);
        } else if (1 == fragmentTag) {
            initDate(1);
        }
        return view;
    }

    private void initView(View view) {
        fragmentTag = getFragmentManager().findFragmentByTag("22").getArguments().getInt("tag");
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                // 设置最近刷新时间
                String updateTime = DateUtils.formatDateTime(
                        getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(updateTime);
                pageNo--;
                if (pageNo > 0) {
                    initDate(fragmentTag);
                } else {
                    pageNo = 1;
                    handler.sendEmptyMessage(0);
                    ToastUtil.showToast(getContext(), "暂无数据刷新！");
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                pageNo++;
                initDate(fragmentTag);
            }
        });
        setPullToRefreshLable();
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BussinessGY.BusinessOpportunityListBean itemAtPosition = (BussinessGY.BusinessOpportunityListBean) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("introduction", itemAtPosition.getProductIntroduction());//商品介绍
                intent.putExtra("productName", itemAtPosition.getProductName());//名称
                intent.putExtra("industryType", itemAtPosition.getFourthIndustryType());//类型
                intent.putExtra("priceRange", itemAtPosition.getPriceRange());//价格
                intent.putExtra("pubDate", itemAtPosition.getPubDate());//时间
                intent.putExtra("sendMode", itemAtPosition.getSendMode());//发送方式
                intent.setClass(getContext(), BusinessTheWordSupplierActivity.class);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    pullToRefreshListView.onRefreshComplete();
                    break;
            }
        }
    };


    private void initDate(int type) {
        String url = BaseUrl.HTTP_URL + "businessOpportunity/getAllBusinessOpportunities";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        String industryType = edit.getString(SharedprefenceStore.INDUSTRYTYPE, "");
        if (!industryType.isEmpty()) {
            String[] industry = industryType.split("\\|");
            int length = industry.length;
            if (length == 4) {
                firstIndustryType = industry[0];
                secondIndustryType = industry[1];
                thirdIndustryType = industry[2];
                fourthIndustryType = industry[3];
            } else if (length == 3) {
                firstIndustryType = industry[0];
                secondIndustryType = industry[1];
                thirdIndustryType = industry[2];
                fourthIndustryType = "";
            }
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
            jsonObject.put("type", type);
            jsonObject.put("pageNo", pageNo);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("firstIndustryType", firstIndustryType);
            jsonObject.put("secondIndustryType", secondIndustryType);
            jsonObject.put("thirdIndustryType", thirdIndustryType);
            jsonObject.put("fourthIndustryType", fourthIndustryType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(getContext(), url, tag, jsonObject, new VolleyCallBack(getActivity().getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                Gson gson = new Gson();
                BussinessGY bussinessGY = gson.fromJson(result + "", BussinessGY.class);
                String code = bussinessGY.getCode();
                if ("200".equals(code)) {
                    List<BussinessGY.BusinessOpportunityListBean> businessOpportunityList = bussinessGY.getBusinessOpportunityList();
                    if (businessOpportunityList != null && businessOpportunityList.size() > 0) {
                        for (int i = 0; i < businessOpportunityList.size(); i++) {
                            BussinessGY.BusinessOpportunityListBean businessOpportunityListBean = businessOpportunityList.get(i);
                            list.add(businessOpportunityListBean);
                        }
                    } else {
                        pageNo--;
                        ToastUtil.showToast(getContext(), "已经是最后一页了！");
                    }
                    adapter = new BusinessAdapter(getContext(), list);
                    adapter.notifyDataSetChanged();
                    pullToRefreshListView.setAdapter(adapter);
                    pullToRefreshListView.onRefreshComplete();
                }
            }

            @Override
            public void onError(VolleyError error) {
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }


    private void setPullToRefreshLable() {
        // 1:第一种设置 (个人推荐第一种)
        ILoadingLayout startLoading = pullToRefreshListView.getLoadingLayoutProxy(true, false);
        startLoading.setPullLabel("下拉刷新");// 刚下拉时显示的提示
        startLoading.setRefreshingLabel("正在刷新中...");// 刷新时显示的提示
        startLoading.setReleaseLabel("释放即可刷新");// 下拉达到一定距离时显示的提示
        ILoadingLayout endLoading = pullToRefreshListView.getLoadingLayoutProxy(false, true);
        endLoading.setPullLabel("上拉加载更多");// 刚上拉时显示的提示
        endLoading.setRefreshingLabel("拼命加载中...");// 加载时的提示
        endLoading.setReleaseLabel("释放即可加载更多");// 上拉达到一定距离时显示的提示
    }

}
