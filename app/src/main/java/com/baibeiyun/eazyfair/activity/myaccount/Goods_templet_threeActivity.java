package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Goods_templet_threeActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private LinearLayout huobi_select_ll;//货币类型
    private TextView huobi_select_tv;//货币
    private LinearLayout jiage_tiaokuan_ll;//价格条款
    private TextView jiage_tiaokuan_tv;//价格条款
    private EditText jiage_tiaokuan_ed;//价格条款自定义
    private Button save_bt;

    private EditText goods_unit_et;


    private LayoutInflater ml;
    //PopupWindow定义的五种常见货币
    private List<String> mList = new ArrayList<>();


    //接受来自twoActivity的数据
    private Intent intent;

    private PopupWindow popupWindow;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    //得到上一个页面传递的值
    private String goodsNumber, goodsName, goodsPrice, goodsMaterial, goodsColor, goodsShuLiang, goodsWeight, goodsWeightUnit;
    private String outboxAmout, outboxSize, outboxWeight, outboxWeightUnit, centerboxAmout, centerboxSize, centerboxWeight, centerWeightUnit;
    //得到当前用户输入的数据
    private String goodsunit, huobi, tiaokuan, tiaokuan_diy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_templet_three);
        initYuyan();
        initview();
        getIntentData();
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

    private void getIntentData() {
        Intent intent = getIntent();
        goodsNumber = intent.getStringExtra("goodsNumber");
        goodsName = intent.getStringExtra("goodsName");

        goodsPrice = intent.getStringExtra("goodsPrice");
        goodsMaterial = intent.getStringExtra("goodsMaterial");
        goodsColor = intent.getStringExtra("goodsColor");
        goodsShuLiang = intent.getStringExtra("goodsShuLiang");
        goodsWeight = intent.getStringExtra("goodsWeight");
        goodsWeightUnit = intent.getStringExtra("goodsWeightUnit");

        outboxAmout = intent.getStringExtra("outboxAmout");
        outboxSize = intent.getStringExtra("outboxSize");
        outboxWeight = intent.getStringExtra("outboxWeight");
        outboxWeightUnit = intent.getStringExtra("outboxWeightUnit");

        centerboxAmout = intent.getStringExtra("centerboxAmout");
        centerboxSize = intent.getStringExtra("centerboxSize");

        centerboxWeight = intent.getStringExtra("centerboxWeight");
        centerWeightUnit = intent.getStringExtra("centerWeightUnit");

    }

    //得到当前用户输入的数据
    private void getData() {
        goodsunit = goods_unit_et.getText().toString().trim();
        huobi = huobi_select_tv.getText().toString().trim();
        tiaokuan = jiage_tiaokuan_tv.getText().toString().trim();
        tiaokuan_diy = jiage_tiaokuan_ed.getText().toString().trim();
    }

    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        huobi_select_ll = (LinearLayout) findViewById(R.id.huobi_select_ll);
        huobi_select_tv = (TextView) findViewById(R.id.huobi_select_tv);
        jiage_tiaokuan_ll = (LinearLayout) findViewById(R.id.jiage_tiaokuan_ll);
        jiage_tiaokuan_tv = (TextView) findViewById(R.id.jiage_tiaokuan_tv);
        jiage_tiaokuan_ed = (EditText) findViewById(R.id.jiage_tiaokuan_ed);
        goods_unit_et = (EditText) findViewById(R.id.goods_unit_et);
        save_bt = (Button) findViewById(R.id.save_bt);
        fanhui_rl.setOnClickListener(this);
        save_bt.setOnClickListener(this);
        huobi_select_ll.setOnClickListener(this);
        jiage_tiaokuan_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.huobi_select_ll:
                showPopuWindow(huobi_select_ll, 1);
                break;
            case R.id.jiage_tiaokuan_ll:
                showPopuWindow(jiage_tiaokuan_ll, 2);
                break;
            case R.id.save_bt:
                Goods_templet_oneActivity.activity.finish();
                Goods_templet_twoActivity.activity.finish();
                getData();
                excelOut();
                break;

        }
    }


    private void showPopuWindow(final View v, int status) {
        mList.clear();
        initDate(status);
        ml = LayoutInflater.from(Goods_templet_threeActivity.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);

        WindowManager wm = Goods_templet_threeActivity.this.getWindowManager();
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        ListView listview = (ListView) view.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(Goods_templet_threeActivity.this, android.R.layout.simple_list_item_1, mList);
        listview.setAdapter(adapter);
        popupWindow.setFocusable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = mList.get(position);
                switch (v.getId()) {
                    case R.id.huobi_select_ll:
                        huobi_select_tv.setText(s);
                        break;
                    case R.id.jiage_tiaokuan_ll:
                        jiage_tiaokuan_tv.setText(s);
                        if (!"EXW".equals(s)) {
                            jiage_tiaokuan_ed.setVisibility(View.VISIBLE);
                        } else {
                            jiage_tiaokuan_ed.setVisibility(View.GONE);
                            jiage_tiaokuan_ed.setText("");
                        }
                        break;
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(Goods_templet_threeActivity.this, 0.5f);//0.0-1.0
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(Goods_templet_threeActivity.this, 1f);
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

    private void initDate(int status) {
        switch (status) {
            case 1:
                if (language.getTag().equals("ch")) {
                    mList.add("美元");
                    mList.add("欧元");
                    mList.add("英镑");
                    mList.add("日元");
                    mList.add("人民币");
                } else if (language.getTag().equals("en")) {
                    mList.add("dollar");
                    mList.add("Euro");
                    mList.add("Pound");
                    mList.add("Yen");
                    mList.add("RMB");
                }
                break;
            case 2:
                mList.add("EXW");
                mList.add("FOB");
                mList.add("CFR");
                mList.add("CIF");
                break;
        }
    }

    //上传图片的PopupWindow弹出的方向
    public enum Location {
        BOTTOM
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(Goods_templet_threeActivity.this, 1f);
        }
    }

    //将数据转换成excel格式
    private void excelOut() {

        WritableWorkbook workbook = null;
        try {
            WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
            //头文件
            headerFormat.setAlignment(Alignment.CENTRE);//设置居中
            headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            headerFormat.setWrap(true);//设置自动换行
            CellView cellView = new CellView();
            cellView.setAutosize(true); //设置自动大小

            // 标题
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleFormat.setAlignment(Alignment.CENTRE);
            titleFormat.setBackground(Colour.YELLOW);
            titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleFormat.setWrap(true);

            //内容1
            WritableFont detFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat detFormat = new WritableCellFormat(detFont);
            detFormat.setAlignment(Alignment.CENTRE);
            detFormat.setBackground(Colour.GRAY_25);
            detFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            detFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            detFormat.setWrap(true);

            //内容2
//            WritableFont detFont2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat detFormat2 = new WritableCellFormat(detFont);
            detFormat2.setAlignment(Alignment.CENTRE);
            detFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
            detFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
            detFormat2.setWrap(true);


            workbook = Workbook.createWorkbook(new File("sdcard/EasyFair/GoodsTemplet.xls"));
            //通过excel对象创建一个选项卡
            WritableSheet sheet = workbook.createSheet("sheet", 0);
            String date = DateUtil.getDate();
            Label la = new Label(0, 0, "《EazyFair》商品模板" + "  " + date+"     （注：商品价格、最少起订量、商品重量、外箱包装、中盒包装中的数量、重量等项一律为数字，每条商品之间不能有空行！）", headerFormat);
            sheet.setRowView(0, 800, false); //设置行高
            sheet.mergeCells(0, 0, 19, 0);
            sheet.addCell(la);

            la = new Label(0, 1, "商品货号", titleFormat);
            sheet.addCell(la);

            la = new Label(1, 1, "商品名称", titleFormat);
            sheet.addCell(la);

            la = new Label(2, 1, "商品价格", titleFormat);
            sheet.addCell(la);


            la = new Label(3, 1, "商品材质", titleFormat);
            sheet.addCell(la);

            la = new Label(4, 1, "商品颜色", titleFormat);
            sheet.addCell(la);

            la = new Label(5, 1, "最少起订量", titleFormat);
            sheet.addCell(la);

            la = new Label(6, 1, "商品重量", titleFormat);
            sheet.addCell(la);

            la = new Label(7, 1, "重量单位", titleFormat);
            sheet.addCell(la);

            la = new Label(8, 1, "外箱包装数量", titleFormat);
            sheet.addCell(la);

            la = new Label(9, 1, "外箱包装尺寸", titleFormat);
            sheet.addCell(la);

            la = new Label(10, 1, "外箱包装重量", titleFormat);
            sheet.addCell(la);

            la = new Label(11, 1, "外箱包装重量单位", titleFormat);
            sheet.addCell(la);

            la = new Label(12, 1, "中盒包装数量", titleFormat);
            sheet.addCell(la);

            la = new Label(13, 1, "中盒包装尺寸", titleFormat);
            sheet.addCell(la);

            la = new Label(14, 1, "中盒包装重量", titleFormat);
            sheet.addCell(la);

            la = new Label(15, 1, "中盒包装重量单位", titleFormat);
            sheet.addCell(la);

            la = new Label(16, 1, "商品单位", titleFormat);
            sheet.addCell(la);

            la = new Label(17, 1, "货币类型", titleFormat);
            sheet.addCell(la);

            la = new Label(18, 1, "价格条款", titleFormat);
            sheet.addCell(la);

            la = new Label(19, 1, "价格条款自定义", titleFormat);
            sheet.addCell(la);

            // 商品货号
            la = new Label(0, 2, goodsNumber, detFormat);
            sheet.addCell(la);

            //商品名
            la = new Label(1, 2, goodsName, detFormat);
            sheet.addCell(la);

            //商品价格
            la = new Label(2, 2, goodsPrice, detFormat);
            sheet.addCell(la);

            //商品材质
            la = new Label(3, 2, goodsMaterial, detFormat);
            sheet.addCell(la);

            //商品颜色
            la = new Label(4, 2, goodsColor, detFormat);
            sheet.addCell(la);

            //商品数量
            la = new Label(5, 2, goodsShuLiang, detFormat);
            sheet.addCell(la);

            //商品重量
            la = new Label(6, 2, goodsWeight, detFormat);
            sheet.addCell(la);

            //商品重量单位
            la = new Label(7, 2, goodsWeightUnit, detFormat);
            sheet.addCell(la);

            //外箱数量
            la = new Label(8, 2, outboxAmout, detFormat);
            sheet.addCell(la);

            //外箱尺寸
            la = new Label(9, 2, outboxSize, detFormat);
            sheet.addCell(la);

            //外箱重量
            la = new Label(10, 2, outboxWeight, detFormat);
            sheet.addCell(la);

            //外箱重量单位
            la = new Label(11, 2, outboxWeightUnit, detFormat);
            sheet.addCell(la);

            //中盒数量
            la = new Label(12, 2, centerboxAmout, detFormat);
            sheet.addCell(la);

            //中盒尺寸
            la = new Label(13, 2, centerboxSize, detFormat);
            sheet.addCell(la);

            //中盒重量
            la = new Label(14, 2, centerboxWeight, detFormat);
            sheet.addCell(la);

            //中盒重量单位
            la = new Label(15, 2, centerWeightUnit, detFormat);
            sheet.addCell(la);

            //商品单位
            la = new Label(16, 2, goodsunit, detFormat);
            sheet.addCell(la);

            //货币类型
            la = new Label(17, 2, huobi, detFormat);
            sheet.addCell(la);

            //价格条款
            la = new Label(18, 2, tiaokuan, detFormat);
            sheet.addCell(la);

            //价格条款自定义
            la = new Label(19, 2, tiaokuan_diy, detFormat);
            sheet.addCell(la);


            //行
            for (int i = 3; i < 100; i++) {
                //列
                for (int j = 0; j < 20; j++) {
                    if (i % 2 == 0) {
                        la = new Label(j, i, "", detFormat);
                    } else {
                        la = new Label(j, i, "", detFormat2);
                    }
                    sheet.addCell(la);
                }
            }
            //设置列宽
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 15);
            sheet.setColumnView(2, 15);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 15);
            sheet.setColumnView(5, 15);
            sheet.setColumnView(6, 15);
            sheet.setColumnView(7, 15);
            sheet.setColumnView(8, 20);
            sheet.setColumnView(9, 20);
            sheet.setColumnView(10, 20);
            sheet.setColumnView(11, 30);

            sheet.setColumnView(12, 20);
            sheet.setColumnView(13, 20);
            sheet.setColumnView(14, 20);
            sheet.setColumnView(15, 30);
            sheet.setColumnView(16, 15);
            sheet.setColumnView(17, 15);
            sheet.setColumnView(18, 15);
            sheet.setColumnView(19, 20);
            workbook.write();
            showDialog(Goods_templet_threeActivity.this,R.string.templet_wc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert workbook != null;
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void sendData() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        File file = new File("sdcard/EasyFair/GoodsTemplet.xls");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/octet-stream");
        Intent.createChooser(intent, "Choose Email Client");
        startActivity(intent);
        finish();
    }

    private void showDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendData();
                dialogInterface.dismiss();

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


}
