package com.baibeiyun.eazyfair.activity.buyer;

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
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.SelectInquiryPriceActivity;
import com.baibeiyun.eazyfair.activity.buyer.mygoods.MyGoodsForBuyerActivity;
import com.baibeiyun.eazyfair.activity.buyer.mysupplier.MySupplierListActivity;
import com.baibeiyun.eazyfair.dao.MyInquiryDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyInquiry;
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

public class MyBuyerActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RoundImageView logo;//公司logo
    private TextView commpany_name_tv;//公司名
    private Button my_goods_bt;//我的商品
    private Button my_supplier_bt;//我的供应商
    private Button my_xunjia_bt;//询价
    private MyUser myUser;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private ArrayList<MyInquiry> myInquiries;
    private String path;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buyer);
        initYuyan();
        myUser = CursorUtils.selectOurInfo(this);
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myInquiries != null) {
            myInquiries.clear();
            myInquiries = null;
        }
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MyBuyerActivity.this);
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
                Toast.makeText(MyBuyerActivity.this, R.string.no_file_try_agein, Toast.LENGTH_SHORT).show();
            }
        }
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        logo = (RoundImageView) findViewById(R.id.logo);
        commpany_name_tv = (TextView) findViewById(R.id.commpany_name_tv);
        my_goods_bt = (Button) findViewById(R.id.my_goods_bt);
        my_supplier_bt = (Button) findViewById(R.id.my_supplier_bt);
        my_xunjia_bt = (Button) findViewById(R.id.my_xunjia_bt);
        fanhui_rl.setOnClickListener(MyBuyerActivity.this);
        my_goods_bt.setOnClickListener(MyBuyerActivity.this);
        my_supplier_bt.setOnClickListener(MyBuyerActivity.this);
        my_xunjia_bt.setOnClickListener(MyBuyerActivity.this);
        if (myUser != null) {
            if (myUser.getCh_portrait() == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                logo.setImageBitmap(bitmap);
            } else {
                byte[] ch_portrait = myUser.getCh_portrait();
                Bitmap bitmap = BitmapUtils.Bytes2Bimap(ch_portrait);
                logo.setImageBitmap(bitmap);//公司logo
            }
            commpany_name_tv.setText(myUser.getCh_company_name());//公司名
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //我的商品
            case R.id.my_goods_bt:
                intent = new Intent(MyBuyerActivity.this, MyGoodsForBuyerActivity.class);
                startActivity(intent);
                break;
            //我的供应商
            case R.id.my_supplier_bt:
                intent = new Intent(MyBuyerActivity.this, MySupplierListActivity.class);
                startActivity(intent);
                break;
            //询价
            case R.id.my_xunjia_bt:
                intent = new Intent(MyBuyerActivity.this, SelectInquiryPriceActivity.class);
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

    //导入excel的方法
    private ArrayList<MyInquiry> excelIn() {
        Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
        byte[] bytes_no = BitmapUtils.compressBmpFromBmp(bitmap_no);
        //由于是导入 所以要返回一个集合
        myInquiries = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(path));
            //获得第一个选项卡
            Sheet sheet = workbook.getSheet(0);
            //for循环一行一行的读值 sheet.getRows()获取数据中有多少条数据 多少行
            for (int i = 1; i < sheet.getRows(); i++) {
                MyInquiry myInquiry = new MyInquiry();
                myInquiry.set_id(ImageFactoryandOther.getStringRandom(6));//询价id
                myInquiry.setCommodity_id(Integer.valueOf(sheet.getCell(0, i).getContents()));//商品表id
                myInquiry.setSerial_number(sheet.getCell(1, i).getContents());//商品货号
                myInquiry.setGoods_name(sheet.getCell(2, i).getContents());//商品名称
                myInquiry.setGoods_unit(sheet.getCell(3, i).getContents());//商品单位
                myInquiry.setMaterial(sheet.getCell(4, i).getContents());//商品材质
                myInquiry.setColor(sheet.getCell(5, i).getContents());//商品颜色
                myInquiry.setPrice(Double.parseDouble(sheet.getCell(6, i).getContents()));//商品价格
                myInquiry.setCurrency_varitety(sheet.getCell(7, i).getContents());//货币类型
                myInquiry.setPrice_clause(sheet.getCell(8, i).getContents());//价格条款
                myInquiry.setPrice_clause_diy(sheet.getCell(9, i).getContents());//价格条款自定义
                myInquiry.setRemark(sheet.getCell(10, i).getContents());//备注
                myInquiry.setIntroduction(sheet.getCell(11, i).getContents());//简介
                myInquiry.setSelf_defined(sheet.getCell(12, i).getContents());//自定义
                myInquiry.setGoods_weight(sheet.getCell(13, i).getContents());//重量
                myInquiry.setGoods_weight_unit(sheet.getCell(14, i).getContents());//重量单位
                myInquiry.setOutbox_number(sheet.getCell(15, i).getContents());//外箱数量
                myInquiry.setOutbox_size(sheet.getCell(16, i).getContents());//外箱尺寸
                myInquiry.setOutbox_weight(sheet.getCell(17, i).getContents());//外箱重量
                myInquiry.setOutbox_weight_unit(sheet.getCell(18, i).getContents());//外箱重量单位
                myInquiry.setCenterbox_number(sheet.getCell(19, i).getContents());//中盒数量
                myInquiry.setCenterbox_size(sheet.getCell(20, i).getContents());//中盒尺寸
                myInquiry.setCenterbox_weight(sheet.getCell(21, i).getContents());//中盒重量
                myInquiry.setCenterbox_weight_unit(sheet.getCell(22, i).getContents());//中盒重量单位
                myInquiry.setSupplier_companyname(sheet.getCell(23, i).getContents());//公司名
                myInquiry.setSupplier_name(sheet.getCell(24, i).getContents());//供应商姓名
                myInquiry.setUniqued_id(sheet.getCell(25, i).getContents());//供应商唯一id
                myInquiry.setMoq(sheet.getCell(26, i).getContents());//最少起订量

                myInquiry.setProduct_imgs1(bytes_no);
                myInquiry.setProduct_imgs2(bytes_no);
                myInquiry.setProduct_imgs3(bytes_no);
                myInquiry.setProduct_imgs4(bytes_no);

                myInquiries.add(myInquiry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast2(MyBuyerActivity.this, R.string.wfsb);
        } finally {
            assert workbook != null;
            workbook.close();

        }
        return myInquiries;
    }


    private void saved() {
        for (int i = 0; i < myInquiries.size(); i++) {
            MyInquiry myInquiry = myInquiries.get(i);
            MyInquiryDao myInquiryDao = new MyInquiryDao(MyBuyerActivity.this);
            //执行该方法层中添加的方法
            myInquiryDao.addData(myInquiry);
        }
        Toast.makeText(MyBuyerActivity.this, R.string.Import_sjcg, Toast.LENGTH_SHORT).show();
        finish();
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
