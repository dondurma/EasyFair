package com.baibeiyun.eazyfair.activity.supplier.mygoods;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.dao.MyCommodityDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.MyDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jxl.Sheet;
import jxl.Workbook;

public class NewGoodsActivityone extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    //公司货号 商品名称 商品单位 商品材质
    private EditText input_company_number_et;
    private EditText input_goods_name_et;
    private EditText input_goods_unit_et;
    private EditText input_goods_material;
    private EditText goods_number_et;//最少起订量
    private EditText goods_weight_et;//商品重量
    private TextView goods_weight_unit_tv;
    private LinearLayout goods_weight_unit_ll;
    private Button next_bt;//下一步

    //当前页面得到用户输入的数据
    private String commpanynumber, goodsname, goodsmaterial, goodsunit, moq, goodsweight, goodsweightunit;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    public static NewGoodsActivityone activityone;
    private String path;


    private ArrayList<MyCommodity> myCommodities;

    private List<String> dropList = new ArrayList<>();//popuWindow中list
    private PopupWindow popupWindow;
    private Handler mhandler;
    private MyDialog myDialog;

    private TextView more_tv;
    private LinearLayout other_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods_activityone);
        activityone = this;
        initYuyan();
        initview();
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        if (path != null) {
            myDialog.dialogShow();
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (path != null) {
                        boolean b = fileIsExists();
                        if (b) {
                            excelInData();
                            saved();
                            delFile(path);
                            myDialog.dialogDismiss();
                        } else {
                            Toast.makeText(NewGoodsActivityone.this, R.string.no_file_try_agein, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, 1500);
        }


    }


    //判断当前指定路径中的指定某个文件是否存在的方法
    private boolean fileIsExists() {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(NewGoodsActivityone.this);
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
        mhandler = new Handler();
        myDialog = new MyDialog(NewGoodsActivityone.this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_company_number_et = (EditText) findViewById(R.id.input_company_number_et);
        input_goods_name_et = (EditText) findViewById(R.id.input_goods_name_et);
        input_goods_material = (EditText) findViewById(R.id.input_goods_material);
        input_goods_unit_et = (EditText) findViewById(R.id.input_goods_unit_et);
        goods_number_et = (EditText) findViewById(R.id.goods_number_et);
        goods_weight_et = (EditText) findViewById(R.id.goods_weight_et);
        goods_weight_unit_tv = (TextView) findViewById(R.id.goods_weight_unit_tv);
        goods_weight_unit_ll = (LinearLayout) findViewById(R.id.goods_weight_unit_ll);
        next_bt = (Button) findViewById(R.id.next_bt);
        more_tv = (TextView) findViewById(R.id.more_tv);
        more_tv.setOnClickListener(this);
        other_ll = (LinearLayout) findViewById(R.id.other_ll);
        goods_weight_unit_ll.setOnClickListener(NewGoodsActivityone.this);
        fanhui_rl.setOnClickListener(NewGoodsActivityone.this);
        next_bt.setOnClickListener(NewGoodsActivityone.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //下一步
            case R.id.next_bt:
                if (!input_goods_name_et.getText().toString().trim().equals("") && !input_company_number_et.getText().toString().trim().equals("")) {
                    getData();
                    Intent intent = new Intent(NewGoodsActivityone.this, NewGoodsActivitytwo.class);
                    intent.putExtra("commpanynumber", commpanynumber);
                    intent.putExtra("goodsname", goodsname);
                    intent.putExtra("goodsmaterial", goodsmaterial);
                    intent.putExtra("goodsunit", goodsunit);

                    intent.putExtra("moq", moq);
                    intent.putExtra("goodsweight", goodsweight);
                    intent.putExtra("goodsweightunit", goodsweightunit);

                    startActivity(intent);
                } else {
                    Toast.makeText(NewGoodsActivityone.this, "商品名和货号不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.goods_weight_unit_ll:
                showPopuWindow();
                break;
            case R.id.more_tv:
                other_ll.setVisibility(View.VISIBLE);
                more_tv.setVisibility(View.GONE);
                break;
        }
    }

    //得到用户输入的数据
    private void getData() {
        commpanynumber = input_company_number_et.getText().toString().trim();
        goodsname = input_goods_name_et.getText().toString().trim();
        goodsmaterial = input_goods_material.getText().toString().trim();
        goodsunit = input_goods_unit_et.getText().toString().trim();
        moq = goods_number_et.getText().toString().trim();
        goodsweight = goods_weight_et.getText().toString().trim();
        goodsweightunit = goods_weight_unit_tv.getText().toString().trim();
    }

    private void showPopuWindow() {
        dropList.clear();
        initDate();
        View mV = LayoutInflater.from(NewGoodsActivityone.this).inflate(R.layout.listview_popupwindow, null);
        mV.setBackgroundColor(Color.WHITE);
        popupWindow = new PopupWindow(mV, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(mV);
        final ListView listview = (ListView) mV.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(NewGoodsActivityone.this, android.R.layout.simple_list_item_1, dropList);
        listview.setAdapter(adapter);
        popupWindow.setFocusable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = dropList.get(position);
                goods_weight_unit_tv.setText(s);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(NewGoodsActivityone.this, 0.5f);//0.0-1.0
        popupWindow.showAtLocation(goods_weight_unit_ll, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(NewGoodsActivityone.this, 1f);
            }
        });
    }

    private void initDate() {
        dropList.add("mg");
        dropList.add("g");
        dropList.add("kg");
        dropList.add("t");
    }

    // 设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
    //所有的导入Excel的方法

    private static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    //导入Excel的方法
    private ArrayList<MyCommodity> excelInData() {
        Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
        byte[] bytes_no = BitmapUtils.compressBmpFromBmp(bitmap_no);
        myCommodities = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            for (int i = 2; i < sheet.getRows(); i++) {
                MyCommodity myCommodity = new MyCommodity();
                myCommodity.set_id(Integer.valueOf(getStringRandom(3)));
                myCommodity.setSerial_number(sheet.getCell(0, i).getContents());
                myCommodity.setName(sheet.getCell(1, i).getContents());
                myCommodity.setPrice(Double.parseDouble(sheet.getCell(2, i).getContents()));
                myCommodity.setMaterial(sheet.getCell(3, i).getContents());
                myCommodity.setColor(sheet.getCell(4, i).getContents());
                myCommodity.setMoq(sheet.getCell(5, i).getContents());
                myCommodity.setGoods_weight(sheet.getCell(6, i).getContents());
                myCommodity.setGoods_weight_unit(sheet.getCell(7, i).getContents());
                myCommodity.setOutbox_number(sheet.getCell(8, i).getContents());
                myCommodity.setOutbox_size(sheet.getCell(9, i).getContents());
                myCommodity.setOutbox_weight(sheet.getCell(10, i).getContents());
                myCommodity.setOutbox_weight_unit(sheet.getCell(11, i).getContents());
                myCommodity.setCenterbox_number(sheet.getCell(12, i).getContents());
                myCommodity.setCenterbox_size(sheet.getCell(13, i).getContents());
                myCommodity.setCenterbox_weight(sheet.getCell(14, i).getContents());
                myCommodity.setCenterbox_weight_unit(sheet.getCell(15, i).getContents());
                myCommodity.setUnit(sheet.getCell(16, i).getContents());
                myCommodity.setCurrency_variety(sheet.getCell(17, i).getContents());
                myCommodity.setPrice_clause(sheet.getCell(18, i).getContents());
                myCommodity.setPrice_clause_diy(sheet.getCell(19, i).getContents());
                myCommodity.setGoods_type(0);//供应商
                myCommodity.setCreate_time(DateUtil.getDate());
                myCommodity.setProduct_imgs1(bytes_no);
                myCommodity.setProduct_imgs2(bytes_no);
                myCommodity.setProduct_imgs3(bytes_no);
                myCommodity.setProduct_imgs4(bytes_no);

                myCommodities.add(myCommodity);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast2(NewGoodsActivityone.this, R.string.wfsb);
        } finally {
            assert workbook != null;
            workbook.close();
        }
        return myCommodities;
    }

    private void saved() {
        int size = myCommodities.size();
        for (int i = 0; i < size; i++) {
            MyCommodity myCommodity = myCommodities.get(i);
            MyCommodityDao myCommodityDao = new MyCommodityDao(NewGoodsActivityone.this);
            myCommodityDao.insertDataforAll(myCommodity);
            finish();
        }
        ToastUtil.showToast2(NewGoodsActivityone.this, R.string.Import_sjcg);
    }

    //删除文件
    public static void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }


}
