package com.baibeiyun.eazyfair.activity.supplier.offer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.myaccount.BaseDataActivity;
import com.baibeiyun.eazyfair.adapter.EasyQuoteAdapter;
import com.baibeiyun.eazyfair.entities.EasyQuote;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EasyQuoteActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout more_rl;//更多
    private TextView custom_name_tv;//客户名
    private RelativeLayout show_page_ll;//显示页数
    private TextView current_page_tv;//当前页
    private TextView total_page_tv;//总页数
    private ListView offer_list_lv;
    private int id;//得到上一个页面传递过来的客户表id

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;


    private MyCustomer myCustomer;
    private String uniqued_id;

    private ArrayList<EasyQuote> easyQuotes = new ArrayList<>();
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private EasyQuoteAdapter easyQuoteAdapter;
    private Map<Integer, String> map = new HashMap<>();
    private List<CheckBox> mlist = new ArrayList<>();//存放CheckBox
    private Handler mHandler;

    private ArrayList<EasyQuote> easyQuotesNumers = new ArrayList<>();
    private ArrayList<String> listForQuote = new ArrayList<>();//将该map集合加到list集合中

    private int size;//总条数
    private double page;//总的页数
    private int t = 0;//当前页数
    private int k = 0;//从第几条开始查询

    private MyUser myUser;

    private String name, company_name;
    private ArrayList<EasyQuote> easyQuotesforSend = new ArrayList<>();
    private EasyQuote easyQuoteforSend;
    private String tag;//语言
    private MyDialog myDialog;


    private ImageView iv_tolast;
    private ImageView iv_tonext;
    private int from = 0;
    private PopupWindow mPopupWindow;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                myDialog.dialogDismiss();
                sendPDF();
            } else if (msg.what == 1) {
                myDialog.dialogDismiss();
                //去掉末尾的0
                DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                if (page != 0) {
                    total_page_tv.setText(decimalFormat.format(page));
                } else {
                    total_page_tv.setText("1");
                }
                current_page_tv.setText("1");
                getData();
                easyQuoteAdapter = new EasyQuoteAdapter(datas, EasyQuoteActivity.this, map, mlist);
                offer_list_lv.setAdapter(easyQuoteAdapter);
                easyQuoteAdapter.notifyDataSetChanged();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_quote);
        initYuyan();
        initView();
        initData();
        initFloat();
    }

    //数据源
    private void getData() {
        if (datas != null) {
            datas.clear();
        }
        for (EasyQuote easy : easyQuotes) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = easy.getGoods_img1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("_id", easy.get_id());
            data.put("unique_id", easy.getUniqued_id());
            data.put("goods_name", easy.getGoods_name());
            data.put("code", easy.getCode());
            datas.add(data);
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(EasyQuoteActivity.this);
        if (language != null) {
            tag = language.getTag();
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
        myDialog = new MyDialog(this);
        iv_tolast = (ImageView) findViewById(R.id.iv_tolast);
        iv_tonext = (ImageView) findViewById(R.id.iv_tonext);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        more_rl = (RelativeLayout) findViewById(R.id.more_rl);
        custom_name_tv = (TextView) findViewById(R.id.custom_name_tv);
        show_page_ll = (RelativeLayout) findViewById(R.id.show_page_ll);
        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        offer_list_lv = (ListView) findViewById(R.id.offer_list_lv);
//        offer_list_lv.setPullLoadEnable(true);
//        offer_list_lv.setPullRefreshEnable(true);
//        offer_list_lv.setXListViewListener(this);
        fanhui_rl.setOnClickListener(this);
        more_rl.setOnClickListener(this);
        iv_tolast.setOnClickListener(this);
        iv_tonext.setOnClickListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        myCustomer = CursorUtils.selectCustomerById(this, id);
        name = myCustomer.getName();//客户名
        company_name = myCustomer.getCompany_name();//客户的公司名
        myUser = CursorUtils.selectOurInfo(EasyQuoteActivity.this);
        custom_name_tv.setText(myCustomer.getName());
        uniqued_id = myCustomer.getUnique_id();
        if (CursorUtils.selectByUniquedIdforEasyQuotePart(EasyQuoteActivity.this, uniqued_id) != null) {
            easyQuotesNumers = CursorUtils.selectByUniquedIdforEasyQuotePart(EasyQuoteActivity.this, uniqued_id);
            size = easyQuotesNumers.size();//总条数
            page = Math.ceil((double) size / (double) 5);//总页数
            //去掉末尾的0
            DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
            if (page != 0) {
                total_page_tv.setText(decimalFormat.format(page));
            } else {
                total_page_tv.setText("1");
            }
            current_page_tv.setText("1");
            easyQuotes = CursorUtils.selectEasyQuoteByLimitPart(EasyQuoteActivity.this, uniqued_id, 0);
            getData();
            easyQuoteAdapter = new EasyQuoteAdapter(datas, EasyQuoteActivity.this, map, mlist);
            offer_list_lv.setAdapter(easyQuoteAdapter);
            easyQuoteAdapter.notifyDataSetChanged();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myDialog.dialogShow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CursorUtils.selectByUniquedIdforEasyQuotePart(EasyQuoteActivity.this, uniqued_id) != null) {
                    easyQuotesNumers = CursorUtils.selectByUniquedIdforEasyQuotePart(EasyQuoteActivity.this, uniqued_id);
                    size = easyQuotesNumers.size();//总条数
                    page = Math.ceil((double) size / (double) 5);//总页数
//                    //去掉末尾的0
//                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
//                    if (page != 0) {
//                        total_page_tv.setText(decimalFormat.format(page));
//                    } else {
//                        total_page_tv.setText("1");
//                    }
//                    current_page_tv.setText("1");
                    easyQuotes = CursorUtils.selectEasyQuoteByLimitPart(EasyQuoteActivity.this, uniqued_id, 0);
//                    getData();
//                    easyQuoteAdapter = new EasyQuoteAdapter(datas, EasyQuoteActivity.this, map, mlist);
//                    offer_list_lv.setAdapter(easyQuoteAdapter);
//                    easyQuoteAdapter.notifyDataSetChanged();
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }

            }
        }).start();

    }

    private void initFloat() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_jump);
//        //跳转
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jump(EasyQuoteActivity.this);
//            }
//        });
        //添加商品
        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EasyQuoteActivity.this, AddEasyQuoteGoodsActivity.class);
                intent.putExtra("unique_id", uniqued_id);
                startActivity(intent);
            }
        });

        //上一页
//        FloatingActionButton last_page_fb = (FloatingActionButton) findViewById(R.id.last_page_fb);
//        last_page_fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        show_page_ll.setVisibility(View.GONE);
//                        t--;
//                        if (t >= 0) {
//                            k = 5 * t;
//                            selectEasyQuote();
//                            easyQuoteAdapter.notifyDataSetChanged();
//                            int p = t + 1;
//                            current_page_tv.setText(p + "");
////                            onLoad();
//                        } else {
//                            ToastUtil.showToast2(EasyQuoteActivity.this, R.string.one_page);
//                            t = 0;
////                            onLoad();
//                        }
//                    }
//                }, 1000);
//                show_page_ll.setVisibility(View.VISIBLE);

//            }
//        });

        //下一页
//        FloatingActionButton next_page_fb = (FloatingActionButton) findViewById(R.id.next_page_fb);
//        next_page_fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        show_page_ll.setVisibility(View.GONE);
//                        t++;
//                        if (t < page) {
//                            k = 5 * t;
//                            selectEasyQuote();
//                            easyQuoteAdapter.notifyDataSetChanged();
//                            int p = t + 1;
//                            current_page_tv.setText(p + "");
////                            onLoad();
//                        } else {
//                            ToastUtil.showToast2(EasyQuoteActivity.this, R.string.last_page);
//                            t = (int) page;
////                            onLoad();
//                        }
//                    }
//                }, 1000);
//                show_page_ll.setVisibility(View.VISIBLE);

//            }
//        });
    }


    public enum LocationforBottom {
        BOTTOM
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //更多
            case R.id.more_rl:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                if (myUser != null) {
                    getGoodsData();//得到所有选中的id的集合
                    if (listForQuote.size() != 0) {


                        from = LocationforBottom.BOTTOM.ordinal();
                        //调用此方法，menu不会顶置
                        initPopupWindowforBottom();

//                        myDialog.dialogShow();
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                //根据多个code来查询简易报价中的商品
//                                easyQuotesforSend = CursorUtils.selectEasyQuoteByIds(EasyQuoteActivity.this, listForQuote);
//                                try {
//                                    writeContentToPDF();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                Message message = new Message();
//                                message.what = 0;
//                                handler.sendMessage(message);
//                            }
//                        }).start();


                    } else {
                        Alerter.create(EasyQuoteActivity.this)
                                .setText(R.string.please_xzyfsdsp)
                                .setDuration(800)
                                .show();
                    }

                } else {
                    showDialog(EasyQuoteActivity.this, R.string.save_info);
                }
                break;
            //上一页
            case R.id.iv_tolast:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_page_ll.setVisibility(View.GONE);
                        t--;
                        if (t >= 0) {
                            k = 5 * t;
                            selectEasyQuote();
                            easyQuoteAdapter.notifyDataSetChanged();
                            int p = t + 1;
                            current_page_tv.setText(p + "");
//                            onLoad();
                        } else {
                            ToastUtil.showToast2(EasyQuoteActivity.this, R.string.one_page);
                            t = 0;
//                            onLoad();
                        }
                    }
                }, 1000);
                show_page_ll.setVisibility(View.VISIBLE);

                break;
            //下一页
            case R.id.iv_tonext:

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_page_ll.setVisibility(View.GONE);
                        t++;
                        if (t < page) {
                            k = 5 * t;
                            selectEasyQuote();
                            easyQuoteAdapter.notifyDataSetChanged();
                            int p = t + 1;
                            current_page_tv.setText(p + "");
//                            onLoad();
                        } else {
                            ToastUtil.showToast2(EasyQuoteActivity.this, R.string.last_page);
                            t = (int) page;
//                            onLoad();
                        }
                    }
                }, 1000);
                show_page_ll.setVisibility(View.VISIBLE);


                break;


        }
    }

    //上一页
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                t--;
//                if (t >= 0) {
//                    k = 5 * t;
//                    selectEasyQuote();
//                    easyQuoteAdapter.notifyDataSetChanged();
//                    int p = t + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(EasyQuoteActivity.this, R.string.one_page);
//                    t = 0;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//
//    }

    //下一页
//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                t++;
//                if (t < page) {
//                    k = 5 * t;
//                    selectEasyQuote();
//                    easyQuoteAdapter.notifyDataSetChanged();
//                    int p = t + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(EasyQuoteActivity.this, R.string.last_page);
//                    t = (int) page;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//    }


    private void selectEasyQuote() {
        if (easyQuotes != null) {
            easyQuotes.clear();
        }
        easyQuotes = CursorUtils.selectEasyQuoteByLimitPart(EasyQuoteActivity.this, uniqued_id, k);
        getData();
    }


    //发送的popupwindow
    private void initPopupWindowforBottom() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_share, null);
        if (LocationforBottom.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }

        //动画效果
        if (LocationforBottom.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (LocationforBottom.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_goods_detail, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(EasyQuoteActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        LinearLayout msg_ch_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_ch_ll);
        LinearLayout msg_en_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_en_ll);
        LinearLayout cancel_ll = (LinearLayout) popupWindowView.findViewById(R.id.cancel_ll);
        //发送
        //中文版
        msg_ch_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dialogShow();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //根据多个code来查询简易报价中的商品
                        easyQuotesforSend = CursorUtils.selectEasyQuoteByIds(EasyQuoteActivity.this, listForQuote);
                        try {
                            writeContentToPDF(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }).start();


                mPopupWindow.dismiss();
            }
        });
        //英文版
        msg_en_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dialogShow();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //根据多个code来查询简易报价中的商品
                        easyQuotesforSend = CursorUtils.selectEasyQuoteByIds(EasyQuoteActivity.this, listForQuote);
                        try {
                            writeContentToPDF(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }).start();


                mPopupWindow.dismiss();
            }
        });
        //取消
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }


    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(EasyQuoteActivity.this, 1f);
        }

    }


//    //停止刷新
//    private void onLoad() {
//        offer_list_lv.stopRefresh();
//        offer_list_lv.stopLoadMore();
//    }

    //跳转至第几页
    private void jump(final Context context) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.jump_to_layout_dialog, null);
        dialog.setContentView(view);
        final EditText editText = (EditText) view.findViewById(R.id.jump_et);
        Button jump_dialog_sure = (Button) view.findViewById(R.id.jump_dialog_sure);
        Button jump_dialog_cancel = (Button) view.findViewById(R.id.jump_dialog_cancel);
        //解决不能弹出输入法的问题
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        jump_dialog_sure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String trim = editText.getText().toString().trim();
                        Integer integer = Integer.valueOf(trim);
                        t = integer - 1;
                        if (integer <= page && integer != 0) {
                            k = (integer - 1) * 5;
                            selectEasyQuote();
                            easyQuoteAdapter.notifyDataSetChanged();
                            current_page_tv.setText(trim);
                            dialog.dismiss();
                        } else {
                            ToastUtil.showToast2(context, R.string.input_page_error);
                            editText.setText("");
                        }
                    }
                });
        jump_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }

    private void showDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.click_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EasyQuoteActivity.this, BaseDataActivity.class);
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


    //将map集合加到list集合中的方法
    public void getGoodsData() {
        if (map == null) return;
        Set<Integer> integers = map.keySet();
        for (Integer key : integers) {
            String s = map.get(key);
            listForQuote.add(s);
        }
    }

    //将从数据库得到的数据转成pdf格式
    private void writeContentToPDF(int type) throws Exception {
        Document document = new Document();
        File file = new File("sdcard/EasyFair/EasyQuote.pdf");
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 10, Font.NORMAL);
        int size = easyQuotesforSend.size();
        for (int i = 0; i < size; i++) {
            easyQuoteforSend = easyQuotesforSend.get(i);
            createPdf(document, font, type);
        }

        document.close();
    }

    //生成pdf
    private void createPdf(Document document, Font font, int type) {
        try {

            if (type == 0) {
                /***** 第一行 ******/
                PdfPTable pdfTable = new PdfPTable(1);
                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                Font font1 = new Font(bfChinese, 20, Font.NORMAL);
                Paragraph paragraph = new Paragraph("简易报价单", font1);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setPaddingBottom(5f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderWidth(0);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.WHITE);// 设置表格外线的颜色
                cell.setMinimumHeight(60);// 设置表格的最小高度
                pdfTable.addCell(cell);
                pdfTable.completeRow();
                document.add(pdfTable);
                /***** 第二行 ******/
                List<String> mList6 = new ArrayList<>();
                mList6.add("公司名称");
                mList6.add(company_name);
                mList6.add("姓名");
                mList6.add(name);
                createTable(mList6, document, font);

                /***** 第三行 ******/
                PdfPTable table3 = new PdfPTable(5);
                table3.setWidths(new float[]{25f, 31.33f, 31.33f, 31.33f, 31.33f});
                // 第一列
                Paragraph paragraph7 = new Paragraph("产品图片", font);
                PdfPCell cell6 = new PdfPCell(paragraph7);
                cell6.setUseBorderPadding(true);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell6.setMinimumHeight(100);// 设置表格的最小高度
                table3.addCell(cell6);

                // 第二列：添加图片
                List<Image> list = new ArrayList<>();
                //图片的路径
                byte[] product_imgs1 = easyQuoteforSend.getGoods_img1();
                byte[] product_imgs2 = easyQuoteforSend.getGoods_img2();
                byte[] product_imgs3 = easyQuoteforSend.getGoods_img3();
                byte[] product_imgs4 = easyQuoteforSend.getGoods_img4();

                list.add(Image.getInstance(product_imgs1));
                list.add(Image.getInstance(product_imgs2));
                list.add(Image.getInstance(product_imgs3));
                list.add(Image.getInstance(product_imgs4));

                createImageTable(list, document, table3);
                table3.completeRow();
                document.add(table3);
                /***** 第四行 ******/
                List<String> mList1 = new ArrayList<>();
                mList1.add("产品参数详情");
                createTable(mList1, document, font);
                /***** 第五行******/
                List<String> mList = new ArrayList<>();
                mList.add("商品名称");
                mList.add(easyQuoteforSend.getGoods_name());

                createTable(mList, document, font);
                /***** 第六行 ******/
                List<String> mList2 = new ArrayList<>();
                mList2.add("详情");

                createTable(mList2, document, font);
                /***** 第七行 ******/
                List<String> mList3 = new ArrayList<>();

                mList3.add(easyQuoteforSend.getContent());
                createTable(mList3, document, font);

            } else if (type == 1) {
                /***** 第一行 ******/
                PdfPTable pdfTable = new PdfPTable(1);
                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                Font font1 = new Font(bfChinese, 20, Font.NORMAL);
                Paragraph paragraph = new Paragraph("Simple quotation", font1);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setPaddingBottom(5f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderWidth(0);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.WHITE);// 设置表格外线的颜色
                cell.setMinimumHeight(60);// 设置表格的最小高度
                pdfTable.addCell(cell);
                pdfTable.completeRow();
                document.add(pdfTable);
                /***** 第二行 ******/
                List<String> mList6 = new ArrayList<>();
                mList6.add("Company Name");
                mList6.add(company_name);
                mList6.add("Name");
                mList6.add(name);
                createTable(mList6, document, font);

                /***** 第三行 ******/
                PdfPTable table3 = new PdfPTable(5);
                table3.setWidths(new float[]{25f, 31.33f, 31.33f, 31.33f, 31.33f});
                // 第一列
                Paragraph paragraph7 = new Paragraph("Product Image", font);
                PdfPCell cell6 = new PdfPCell(paragraph7);
                cell6.setUseBorderPadding(true);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell6.setMinimumHeight(100);// 设置表格的最小高度
                table3.addCell(cell6);

                // 第二列：添加图片
                List<Image> list = new ArrayList<>();
                //图片的路径
                byte[] product_imgs1 = easyQuoteforSend.getGoods_img1();
                byte[] product_imgs2 = easyQuoteforSend.getGoods_img2();
                byte[] product_imgs3 = easyQuoteforSend.getGoods_img3();
                byte[] product_imgs4 = easyQuoteforSend.getGoods_img4();

                list.add(Image.getInstance(product_imgs1));
                list.add(Image.getInstance(product_imgs2));
                list.add(Image.getInstance(product_imgs3));
                list.add(Image.getInstance(product_imgs4));

                createImageTable(list, document, table3);
                table3.completeRow();
                document.add(table3);
                /***** 第四行 ******/
                List<String> mList1 = new ArrayList<>();
                mList1.add("Product Parameter Details");
                createTable(mList1, document, font);
                /***** 第五行******/
                List<String> mList = new ArrayList<>();
                mList.add("Product Name");
                mList.add(easyQuoteforSend.getGoods_name());

                createTable(mList, document, font);
                /***** 第六行 ******/
                List<String> mList2 = new ArrayList<>();
                mList2.add("Detail");

                createTable(mList2, document, font);
                /***** 第七行 ******/
                List<String> mList3 = new ArrayList<>();

                mList3.add(easyQuoteforSend.getContent());
                createTable(mList3, document, font);


            }


        } catch (Exception ignored) {
        }
    }

    private void createTable(List<String> mList, Document document, Font font) throws Exception {
        if (mList != null && mList.size() > 0) {
            int size = mList.size();
            PdfPTable table = new PdfPTable(size);
            if (size == 1) {
                table.setWidths(new float[]{25f});
            } else if (size == 2) {
                table.setWidths(new float[]{25f, 125f});
            } else if (size == 7) {
                table.setWidths(new float[]{20f, 10f, 15f, 10f, 20f, 10f, 15f});
            } else {
                table.setWidths(new float[]{25f, 50f, 25f, 50f});
            }

            for (int i = 0; i < size; i++) {
                Paragraph paragraph = new Paragraph(mList.get(i), font);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setUseBorderPadding(true);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // cell.setBorderWidth(1);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell.setMinimumHeight(25);// 设置表格的最小高度
                table.addCell(cell);
            }
            table.completeRow();
            document.add(table);
        }

    }

    private void createImageTable(List<Image> list, Document document, PdfPTable table) throws Exception {
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                PdfPCell cell = new PdfPCell();
                cell.setImage(list.get(i));
                cell.setUseBorderPadding(true);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell.setMinimumHeight(50);// 设置表格的最小高度
                table.addCell(cell);
            }

        }
    }

    //发送
    private void sendPDF() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        File file = new File("sdcard/EasyFair/EasyQuote.pdf");
        intent.setType("*/*");
        // 附件
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
    }


}
