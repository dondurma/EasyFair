package com.baibeiyun.eazyfair.activity.supplier;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.supplier.mycustomer.CustomListActivity;
import com.baibeiyun.eazyfair.activity.supplier.mygoods.MyGoodsActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.OfferActivity;
import com.baibeiyun.eazyfair.dao.MyQuoteDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.RoundImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;

public class MySupplierActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    //我的商品 我的顾客 报价
    private Button my_commodity_bt, my_customer_bt, my_price_bt;
    private RoundImageView logo;
    private TextView company_name;
    private MyUser myUser;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private ArrayList<MyQuote> myQuotes;
    private String path;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_supplier);
        initYuyan();
        init();
        myUser = CursorUtils.selectOurInfo(MySupplierActivity.this);
        if (myUser != null) {
            if (this.myUser.getCh_portrait() == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                logo.setImageBitmap(bitmap);
            } else {
                byte[] ch_portrait = myUser.getCh_portrait();
                Bitmap bitmap = BitmapUtils.Bytes2Bimap(ch_portrait);
                logo.setImageBitmap(bitmap);//公司logo
            }
            company_name.setText(myUser.getCh_company_name());
        }
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
        mHandler = new Handler();
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        if (path != null) {
            boolean b = fileIsExists();
            if (b) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        excelIn();
                        saved();
                        delFile(path);
                    }
                }, 1000);

            } else {
                Toast.makeText(MySupplierActivity.this, R.string.no_file_try_agein, Toast.LENGTH_SHORT).show();
            }
        }
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        my_commodity_bt = (Button) findViewById(R.id.my_commodity_bt);
        my_customer_bt = (Button) findViewById(R.id.my_customer_bt);
        my_price_bt = (Button) findViewById(R.id.my_price_bt);
        logo = (RoundImageView) findViewById(R.id.logo);
        company_name = (TextView) findViewById(R.id.company_name);
        fanhui_rl.setOnClickListener(MySupplierActivity.this);
        my_commodity_bt.setOnClickListener(MySupplierActivity.this);
        my_customer_bt.setOnClickListener(MySupplierActivity.this);
        my_price_bt.setOnClickListener(MySupplierActivity.this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.my_commodity_bt:
                intent = new Intent(MySupplierActivity.this, MyGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.my_customer_bt:
                intent = new Intent(MySupplierActivity.this, CustomListActivity.class);
                startActivity(intent);
                break;
            case R.id.my_price_bt:
                intent = new Intent(MySupplierActivity.this, OfferActivity.class);
                startActivity(intent);
                break;
        }
    }


    //判断当前指定路径中的指定某个文件是否存在的方法
    public boolean fileIsExists() {
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

    // 2016/12/8 导入Excel的方法
    public ArrayList<MyQuote> excelIn() {
        Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
        byte[] bytes_no = BitmapUtils.compressBmpFromBmp(bitmap_no);
        myQuotes = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(path));
            //获得第一个选项卡
            Sheet sheet = workbook.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++) {
                MyQuote myQuote = new MyQuote();
                myQuote.set_id(ImageFactoryandOther.getStringRandom(6));//询价id
                myQuote.setCommodity_id(Integer.valueOf(sheet.getCell(0, i).getContents()));//商品表id
                myQuote.setSerial_number(sheet.getCell(1, i).getContents());//商品货号
                myQuote.setGoods_name(sheet.getCell(2, i).getContents());//商品名称
                myQuote.setGoods_unit(sheet.getCell(3, i).getContents());//商品单位
                myQuote.setMaterial(sheet.getCell(4, i).getContents());//商品材质
                myQuote.setColor(sheet.getCell(5, i).getContents());//商品颜色
                myQuote.setPrice(Double.parseDouble(sheet.getCell(6, i).getContents()));//商品价格
                myQuote.setCurrency_varitety(sheet.getCell(7, i).getContents());//货币类型
                myQuote.setPrice_clause(sheet.getCell(8, i).getContents());//价格条款
                myQuote.setPrice_clause_diy(sheet.getCell(9, i).getContents());//价格条款自定义
                myQuote.setRemark(sheet.getCell(10, i).getContents());//备注
                myQuote.setIntroduction(sheet.getCell(11, i).getContents());//简介
                myQuote.setSelf_defined(sheet.getCell(12, i).getContents());//自定义
                myQuote.setGoods_weight(sheet.getCell(13, i).getContents());//重量
                myQuote.setGoods_weight_unit(sheet.getCell(14, i).getContents());//重量单位
                myQuote.setOutbox_number(sheet.getCell(15, i).getContents());//外箱数量
                myQuote.setOutbox_size(sheet.getCell(16, i).getContents());//外箱尺寸
                myQuote.setOutbox_weight(sheet.getCell(17, i).getContents());//外箱重量
                myQuote.setOutbox_weight_unit(sheet.getCell(18, i).getContents());//外箱重量单位
                myQuote.setCenterbox_number(sheet.getCell(19, i).getContents());//中盒数量
                myQuote.setCenterbox_size(sheet.getCell(20, i).getContents());//中盒尺寸
                myQuote.setCenterbox_weight(sheet.getCell(21, i).getContents());//中盒重量
                myQuote.setCenterbox_weight_unit(sheet.getCell(22, i).getContents());//中盒重量单位
                myQuote.setBuyer_companyname(sheet.getCell(23, i).getContents());//公司名
                myQuote.setBuyer_name(sheet.getCell(24, i).getContents());//采购商姓名
                myQuote.setUniqued_id(sheet.getCell(25, i).getContents());//采购商唯一id
                myQuote.setMoq(sheet.getCell(26, i).getContents());//最少起订量

                myQuote.setProduct_imgs1(bytes_no);
                myQuote.setProduct_imgs2(bytes_no);
                myQuote.setProduct_imgs3(bytes_no);
                myQuote.setProduct_imgs4(bytes_no);
                myQuotes.add(myQuote);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast2(MySupplierActivity.this, R.string.wfsb);
        } finally {
            assert workbook != null;
            workbook.close();
        }
        return myQuotes;

    }

    private void saved() {
        int size = myQuotes.size();
        for (int i = 0; i < size; i++) {
            MyQuote myQuote = myQuotes.get(i);
            MyQuoteDao myQuoteDao = new MyQuoteDao(MySupplierActivity.this);
            myQuoteDao.addData(myQuote);
            finish();
        }
        Toast.makeText(MySupplierActivity.this, R.string.Import_sjcg, Toast.LENGTH_SHORT).show();
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
