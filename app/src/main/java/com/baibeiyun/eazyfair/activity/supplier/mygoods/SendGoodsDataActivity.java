package com.baibeiyun.eazyfair.activity.supplier.mygoods;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.OtherUtils;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class SendGoodsDataActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RadioButton bluetooth_rb, email_rb, excel_rb, pdf_rb;
    private Button send_bt;//发送
    private RadioGroup send_rg, document_rg;
    private int sendFormate, a;
    private int fileFormate;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    private String[] split1, split2, split3;
    private MyCommodity myCommodity;
    private String self_defined;
    //蓝牙发送相关
    private BluetoothAdapter btAdapter;
    private MyUser myUser;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private LinearLayout title_bar_ll;
    private int tag;//判断当前是供应商还是采购商
    private String tag1;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_goods_data);
        initYuyan();
        initview();

    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(SendGoodsDataActivity.this);
        if (language != null) {
            tag1 = this.language.getTag();
            if (tag1.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);

            } else if (tag1.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }


    private void initview() {
        myUser = CursorUtils.selectOurInfo(SendGoodsDataActivity.this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        tag = intent.getIntExtra("tag", 0);
         lang = intent.getStringExtra("lang");
        myCommodity = CursorUtils.selectById(SendGoodsDataActivity.this, id);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        self_defined = myCommodity.getDiy();
        byte[] product_imgs1 = this.myCommodity.getProduct_imgs1();
        byte[] product_imgs2 = this.myCommodity.getProduct_imgs2();
        byte[] product_imgs3 = this.myCommodity.getProduct_imgs3();
        byte[] product_imgs4 = this.myCommodity.getProduct_imgs4();
        bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        if (self_defined != null) {
            split1 = self_defined.split("\\|");
            split2 = new String[split1.length];
            split3 = new String[split1.length];
            int length = split1.length;
            for (int i = 0; i < length; i++) {
                a = split1[i].indexOf(":");
                split2[i] = split1[i].substring(0, a);
                split3[i] = split1[i].substring(a + 1, split1[i].length());
            }
        }

        title_bar_ll = (LinearLayout) findViewById(R.id.title_bar_ll);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        bluetooth_rb = (RadioButton) findViewById(R.id.bluetooth_rb);
        email_rb = (RadioButton) findViewById(R.id.email_rb);
        excel_rb = (RadioButton) findViewById(R.id.excel_rb);
        pdf_rb = (RadioButton) findViewById(R.id.pdf_rb);
        send_rg = (RadioGroup) findViewById(R.id.send_rg);
        document_rg = (RadioGroup) findViewById(R.id.document_rg);
        send_bt = (Button) findViewById(R.id.send_bt);
        fanhui_rl.setOnClickListener(SendGoodsDataActivity.this);
        send_bt.setOnClickListener(SendGoodsDataActivity.this);

        send_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //蓝牙发送
                if (i == bluetooth_rb.getId()) {
                    sendFormate = 0;
                }
                //email发送
                else if (i == email_rb.getId()) {
                    sendFormate = 1;
                }
            }
        });
        document_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //选择excel
                if (i == excel_rb.getId()) {
                    fileFormate = 0;
                }
                //选择pdf
                else if (i == pdf_rb.getId()) {
                    fileFormate = 1;
                }
            }
        });

        //蓝色
        int color_blue = getResources().getColor(R.color.blue);
        //绿色
        int color_green = getResources().getColor(R.color.green_color);
        if (tag == 1) {
            title_bar_ll.setBackgroundColor(color_blue);
            Drawable blue = getResources().getDrawable(R.drawable.solid_blue);
            send_bt.setBackground(blue);
        } else if (tag == 2) {
            title_bar_ll.setBackgroundColor(color_green);
            Drawable green = getResources().getDrawable(R.drawable.solid_green);
            send_bt.setBackground(green);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.send_bt:
                switch (fileFormate) {
                    //excel
                    case 0:
                        writeContentToExcel("sdcard/EasyFair/GoodsDetail.xls");
                        break;
                    //pdf
                    case 1:
                        try {
                            writeContentToPDF();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                switch (sendFormate) {
                    //蓝牙
                    case 0:
                        sendBluetooth();
                        break;
                    //email
                    case 1:
                        sendEmail();
                        break;
                }
                break;
        }
    }

    //emai发送
    private void sendEmail() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        // 附件
        // 邮件发送的类型（带附件的发送）
        File file = null;
        if (fileFormate == 0) {
            file = new File("sdcard/EasyFair/GoodsDetail.xls");
        } else if (fileFormate == 1) {
            file = new File("sdcard/EasyFair/GoodsDetail.pdf");
        }
        intent.setType("*/*");
        // 附件
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        /**将可以以蓝牙方式发送文件的应用进行过滤，然后只剩下email*/
        PackageManager pm = getPackageManager();
        //得到所有可以以蓝牙方式发送文件的应用
        List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
        String packageName = null;
        String className = null;
        if (appsList.size() > 0) {
            // 选择蓝牙
            boolean found = false;
            //便利所有应用的包名  如何和蓝牙的包名一样的话  就设置蓝牙启动
            for (ResolveInfo info : appsList) {
                packageName = info.activityInfo.packageName;
                if (packageName.contains("mail") || packageName.contains("gm")) {
                    className = info.activityInfo.name;
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(SendGoodsDataActivity.this, R.string.the_phone_not_have_email, Toast.LENGTH_SHORT).show();
            }
        }
        // 设置启动蓝牙intent
        intent.setClassName(packageName, className);
        startActivity(intent);
    }

    //蓝牙发送
    private void sendBluetooth() {
        if (btAdapter != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            // 附件
            // 邮件发送的类型（带附件的发送）
            File file = null;
            if (fileFormate == 0) {
                file = new File("sdcard/EasyFair/GoodsDetail.xls");
            } else if (fileFormate == 1) {
                file = new File("sdcard/EasyFair/GoodsDetail.pdf");
            }
            intent.setType("*/*");
            // 附件
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            /**将可以以蓝牙方式发送文件的应用进行过滤，然后只剩下蓝牙*/
            PackageManager pm = getPackageManager();
            //得到所有可以以蓝牙方式发送文件的应用
            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
            String packageName = null;
            String className = null;
            if (appsList.size() > 0) {
                // 选择蓝牙
                boolean found = false;
                //便利所有应用的包名  如何和蓝牙的包名一样的话  就设置蓝牙启动
                for (ResolveInfo info : appsList) {
                    packageName = info.activityInfo.packageName;
                    if (packageName.contains("bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;// 找到，结束查找
                    }
                }
                if (!found) {
                    Toast.makeText(SendGoodsDataActivity.this, R.string.the_phone_not_have_bluetooth, Toast.LENGTH_SHORT).show();
                }
            }
            if (className != null) {
                // 设置启动蓝牙intent
                intent.setClassName(packageName, className);
                startActivity(intent);
            } else {
                Toast.makeText(SendGoodsDataActivity.this, R.string.unable_to_call_bluetooth, Toast.LENGTH_SHORT).show();
            }


        }
    }


    //将从数据库得到的数据转成excel格式
    private void writeContentToExcel(String fileName) {
        try {
            Number n = null;
            DateTime d = null;
            File tempFile = new File(fileName);
            //创建工作薄
            WritableWorkbook workbook = null;
            workbook = Workbook.createWorkbook(tempFile);
            WritableSheet sheet = workbook.createSheet(myCommodity.getSerial_number(), 0);
            // 预定义的一些字体和格式， 字形、大小、加粗、倾斜、下划线、颜色
            // 头文件
            WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
            headerFormat.setAlignment(Alignment.CENTRE);//设置居中
            headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            headerFormat.setWrap(false);//设置自动换行
            // 标题
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleFormat.setAlignment(Alignment.CENTRE);
            titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleFormat.setWrap(false);
            // 内容
            WritableFont detFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat detFormat = new WritableCellFormat(detFont);
            detFormat.setAlignment(Alignment.CENTRE);
            detFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            detFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            detFormat.setWrap(false);
            // number 格式
            NumberFormat nf = new NumberFormat("0.000");
            WritableCellFormat priceFormat = new WritableCellFormat(nf);
            priceFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            priceFormat.setAlignment(Alignment.CENTRE);
            priceFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            priceFormat.setWrap(false);
            // 日期
            DateFormat df = new DateFormat("yyyy-MM-dd");
            WritableCellFormat dateFormat = new WritableCellFormat(df);
            dateFormat.setAlignment(Alignment.CENTRE);
            dateFormat.setAlignment(Alignment.CENTRE);
            dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            dateFormat.setWrap(false);
            Label l = null;
            // 创建单元格

            //中文版
            if (lang.equals("ch")) {
                //第一行
                l = new Label(0, 0, "商品详情单", headerFormat);

                sheet.addCell(l);
                sheet.mergeCells(0, 0, 8, 0);//合并单元格

                //第二行
                if (tag == 1) {
                    l = new Label(0, 1, OtherUtils.SUPPLIERNAME, titleFormat);
                } else if (tag == 2) {
                    l = new Label(0, 1, OtherUtils.BUYERNAME, titleFormat);
                }
                sheet.addCell(l);

                l = new Label(1, 1, myUser.getCh_contact(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 1, 3, 1);
                //公司名
                if (tag == 1) {
                    l = new Label(4, 1, OtherUtils.SUPPLIERCOMPANY, titleFormat);
                } else if (tag == 2) {
                    l = new Label(4, 1, OtherUtils.BUYERCOMPANY, titleFormat);
                }
                sheet.addCell(l);
                l = new Label(5, 1, myUser.getCh_company_name(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 1, 8, 1);

                //第三行
                l = new Label(0, 2, OtherUtils.GOODSPIC, titleFormat);
                sheet.addCell(l);

                Date now = new Date();
                SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss");
                String format = da.format(now);
                //图片1
                OtherUtils.savePNG_After(bitmap1, "sdcard/EasyFair/" + format + "1" + ".png");
                File file = new File("sdcard/EasyFair/" + format + "1" + ".png");
                //前两位是起始格，后两位是图片占多少个格，并非是位置
                WritableImage image = new WritableImage(1, 2, 2, 1, file);
                sheet.addImage(image);
                //图片2
                OtherUtils.savePNG_After(bitmap2, "sdcard/EasyFair/" + format + "2" + ".png");
                File file2 = new File("sdcard/EasyFair/" + format + "2" + ".png");
                WritableImage image2 = new WritableImage(3, 2, 2, 1, file2);
                sheet.addImage(image2);
                //图片3
                OtherUtils.savePNG_After(bitmap3, "sdcard/EasyFair/" + format + "3" + ".png");
                File file3 = new File("sdcard/EasyFair/" + format + "3" + ".png");
                WritableImage image3 = new WritableImage(5, 2, 2, 1, file3);
                sheet.addImage(image3);
                //图片4
                OtherUtils.savePNG_After(bitmap4, "sdcard/EasyFair/" + format + "4" + ".png");
                File file4 = new File("sdcard/EasyFair/" + format + "4" + ".png");
                WritableImage image4 = new WritableImage(7, 2, 2, 1, file4);
                sheet.addImage(image4);

                //第四行
                l = new Label(0, 3, OtherUtils.GOODS_PARAMETER, titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(0, 3, 8, 3);

                //第五行
                l = new Label(0, 4, OtherUtils.GOODS_NAME, titleFormat);
                sheet.addCell(l);
                l = new Label(1, 4, myCommodity.getName(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 4, 3, 4);
                l = new Label(4, 4, OtherUtils.GOODSPRICE, titleFormat);
                sheet.addCell(l);
                l = new Label(5, 4, String.valueOf(myCommodity.getPrice()), priceFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 4, 8, 4);

                //第六行
                l = new Label(0, 5, OtherUtils.ART_NO, titleFormat);
                sheet.addCell(l);
                l = new Label(1, 5, myCommodity.getSerial_number(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 5, 3, 5);
                l = new Label(4, 5, OtherUtils.GOODSMATERIAL, titleFormat);
                sheet.addCell(l);
                l = new Label(5, 5, myCommodity.getMaterial(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 5, 8, 5);

                //第七行
                l = new Label(0, 6, OtherUtils.MOQ, titleFormat);
                sheet.addCell(l);
                l = new Label(1, 6, myCommodity.getMoq() + "\t" + myCommodity.getUnit(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 6, 3, 6);
                l = new Label(4, 6, OtherUtils.GOODSCOLOR, titleFormat);
                sheet.addCell(l);
                l = new Label(5, 6, myCommodity.getColor(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 6, 8, 6);

                //第八行
                l = new Label(0, 7, OtherUtils.PRICECLAUSE, titleFormat);
                sheet.addCell(l);
                l = new Label(1, 7, myCommodity.getPrice_clause() + "\t\t" + myCommodity.getPrice_clause_diy(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 7, 3, 7);
                l = new Label(4, 7, OtherUtils.CURRENCY, titleFormat);
                sheet.addCell(l);
                l = new Label(5, 7, myCommodity.getCurrency_variety(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 7, 8, 7);

                //第九行
                l = new Label(0, 8, OtherUtils.INTRODUCTION, titleFormat);
                sheet.addCell(l);
                l = new Label(1, 8, myCommodity.getIntroduction(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 8, 3, 8);
                l = new Label(4, 8, OtherUtils.REMARK, titleFormat);
                sheet.addCell(l);
                l = new Label(5, 8, myCommodity.getRemark(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 8, 8, 8);

                //第十至十二行
                l = new Label(0, 9, OtherUtils.OUTPAC, titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(0, 9, 0, 11);

                l = new Label(1, 9, OtherUtils.NUMBER, priceFormat);
                sheet.addCell(l);
                l = new Label(2, 9, myCommodity.getOutbox_number(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(2, 9, 3, 9);

                l = new Label(1, 10, OtherUtils.SIZE, detFormat);
                sheet.addCell(l);
                l = new Label(2, 10, myCommodity.getOutbox_size(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(2, 10, 3, 10);
                l = new Label(1, 11, OtherUtils.WEIGHT, detFormat);
                sheet.addCell(l);
                l = new Label(2, 11, myCommodity.getOutbox_weight() + "\t\t" + myCommodity.getOutbox_weight_unit(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(2, 11, 3, 11);

                l = new Label(4, 9, OtherUtils.INNER_PAC, titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(4, 9, 4, 11);

                l = new Label(5, 9, OtherUtils.NUMBER, priceFormat);
                sheet.addCell(l);

                l = new Label(6, 9, myCommodity.getCenterbox_number(), titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(6, 9, 8, 6);

                l = new Label(5, 10, OtherUtils.SIZE, titleFormat);
                sheet.addCell(l);

                l = new Label(6, 10, myCommodity.getCenterbox_size(), titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(6, 10, 8, 6);

                l = new Label(5, 11, OtherUtils.WEIGHT, titleFormat);
                sheet.addCell(l);

                l = new Label(6, 11, myCommodity.getCenterbox_weight() + "\t" + myCommodity.getCenterbox_weight_unit(), titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(6, 11, 8, 11);

                //英文版
            } else if (lang.equals("en")) {
                //第一行
                l = new Label(0, 0, "Product details single", headerFormat);

                sheet.addCell(l);
                sheet.mergeCells(0, 0, 8, 0);//合并单元格

                //第二行
                if (tag == 1) {
                    l = new Label(0, 1, "Supplier name", titleFormat);
                } else if (tag == 2) {
                    l = new Label(0, 1, "Buyer name", titleFormat);
                }
                sheet.addCell(l);

                l = new Label(1, 1, myUser.getCh_contact(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 1, 3, 1);
                //公司名
                if (tag == 1) {
                    l = new Label(4, 1, "Supplier company name", titleFormat);
                } else if (tag == 2) {
                    l = new Label(4, 1, "Buyer Company Name", titleFormat);
                }
                sheet.addCell(l);
                l = new Label(5, 1, myUser.getCh_company_name(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 1, 8, 1);

                //第三行
                l = new Label(0, 2, "Product Image", titleFormat);
                sheet.addCell(l);

                Date now = new Date();
                SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss");
                String format = da.format(now);
                //图片1
                OtherUtils.savePNG_After(bitmap1, "sdcard/EasyFair/" + format + "1" + ".png");
                File file = new File("sdcard/EasyFair/" + format + "1" + ".png");
                //前两位是起始格，后两位是图片占多少个格，并非是位置
                WritableImage image = new WritableImage(1, 2, 2, 1, file);
                sheet.addImage(image);
                //图片2
                OtherUtils.savePNG_After(bitmap2, "sdcard/EasyFair/" + format + "2" + ".png");
                File file2 = new File("sdcard/EasyFair/" + format + "2" + ".png");
                WritableImage image2 = new WritableImage(3, 2, 2, 1, file2);
                sheet.addImage(image2);
                //图片3
                OtherUtils.savePNG_After(bitmap3, "sdcard/EasyFair/" + format + "3" + ".png");
                File file3 = new File("sdcard/EasyFair/" + format + "3" + ".png");
                WritableImage image3 = new WritableImage(5, 2, 2, 1, file3);
                sheet.addImage(image3);
                //图片4
                OtherUtils.savePNG_After(bitmap4, "sdcard/EasyFair/" + format + "4" + ".png");
                File file4 = new File("sdcard/EasyFair/" + format + "4" + ".png");
                WritableImage image4 = new WritableImage(7, 2, 2, 1, file4);
                sheet.addImage(image4);

                //第四行
                l = new Label(0, 3, "Product parameters", titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(0, 3, 8, 3);

                //第五行
                l = new Label(0, 4, "Product name", titleFormat);
                sheet.addCell(l);
                l = new Label(1, 4, myCommodity.getName(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 4, 3, 4);
                l = new Label(4, 4, "Product price", titleFormat);
                sheet.addCell(l);
                l = new Label(5, 4, String.valueOf(myCommodity.getPrice()), priceFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 4, 8, 4);

                //第六行
                l = new Label(0, 5, "Item number", titleFormat);
                sheet.addCell(l);
                l = new Label(1, 5, myCommodity.getSerial_number(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 5, 3, 5);
                l = new Label(4, 5, "Material", titleFormat);
                sheet.addCell(l);
                l = new Label(5, 5, myCommodity.getMaterial(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 5, 8, 5);

                //第七行
                l = new Label(0, 6, "MOQ", titleFormat);
                sheet.addCell(l);
                l = new Label(1, 6, myCommodity.getMoq() + "\t" + myCommodity.getUnit(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 6, 3, 6);
                l = new Label(4, 6, "Color", titleFormat);
                sheet.addCell(l);
                l = new Label(5, 6, myCommodity.getColor(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 6, 8, 6);

                //第八行
                l = new Label(0, 7, "Pricing term", titleFormat);
                sheet.addCell(l);
                l = new Label(1, 7, myCommodity.getPrice_clause() + "\t\t" + myCommodity.getPrice_clause_diy(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 7, 3, 7);
                l = new Label(4, 7, "Currency", titleFormat);
                sheet.addCell(l);
                l = new Label(5, 7, myCommodity.getCurrency_variety(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 7, 8, 7);

                //第九行
                l = new Label(0, 8, "Introduction", titleFormat);
                sheet.addCell(l);
                l = new Label(1, 8, myCommodity.getIntroduction(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(1, 8, 3, 8);
                l = new Label(4, 8, "Remarks", titleFormat);
                sheet.addCell(l);
                l = new Label(5, 8, myCommodity.getRemark(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(5, 8, 8, 8);

                //第十至十二行
                l = new Label(0, 9, "Outer packing", titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(0, 9, 0, 11);

                l = new Label(1, 9, "Number", priceFormat);
                sheet.addCell(l);
                l = new Label(2, 9, myCommodity.getOutbox_number(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(2, 9, 3, 9);

                l = new Label(1, 10, "Size", detFormat);
                sheet.addCell(l);
                l = new Label(2, 10, myCommodity.getOutbox_size(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(2, 10, 3, 10);
                l = new Label(1, 11, "Weight", detFormat);
                sheet.addCell(l);
                l = new Label(2, 11, myCommodity.getOutbox_weight() + "\t\t" + myCommodity.getOutbox_weight_unit(), detFormat);
                sheet.addCell(l);
                sheet.mergeCells(2, 11, 3, 11);

                l = new Label(4, 9, "Box packaging", titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(4, 9, 4, 11);

                l = new Label(5, 9, "Number", priceFormat);
                sheet.addCell(l);

                l = new Label(6, 9, myCommodity.getCenterbox_number(), titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(6, 9, 8, 6);

                l = new Label(5, 10, "Size", titleFormat);
                sheet.addCell(l);

                l = new Label(6, 10, myCommodity.getCenterbox_size(), titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(6, 10, 8, 6);

                l = new Label(5, 11, "Weight", titleFormat);
                sheet.addCell(l);

                l = new Label(6, 11, myCommodity.getCenterbox_weight() + "\t" + myCommodity.getCenterbox_weight_unit(), titleFormat);
                sheet.addCell(l);
                sheet.mergeCells(6, 11, 8, 11);
            }


            int a = 0;
            int h = 12;//行数-自定义从第十二行开始
            if (self_defined != null) {
                //自定义标题导入表格
                if (split1.length > 1) {
                    for (int i = 0; i <= (split1.length - 1) * 4; i = i + 4) {
                        if (i % 4 == 0) {
                            if (i % 8 == 0) {
                                l = new Label(0, h + a, split2[i / 4], titleFormat);

                            } else if ((i / 4) % 2 == 1) {
                                l = new Label(4, h + a, split2[i / 4], titleFormat);
                                a++;
                            } else if (i == 4) {
                                l = new Label(4, h, split2[1], titleFormat);
                                a++;
                            }
                        } else {
                            l = new Label(0, h, split2[0], titleFormat);
                        }
                        sheet.addCell(l);
                    }
                } else if (split1.length == 1) {
                    l = new Label(0, h, split2[0], titleFormat);
                    sheet.addCell(l);
                }
                if (split1.length % 2 == 1) {
                    l = new Label(4, h + a, "", titleFormat);
                    sheet.addCell(l);
                }
                //自定义内容导入表格
                a = 0;
                if (split1.length > 1) {
                    for (int i = 0; i <= (split1.length - 1) * 4; i = i + 4) {
                        if (i % 4 == 0) {
                            if (i % 8 == 0) {
                                l = new Label(1, h + a, split3[i / 4], detFormat);
                                sheet.mergeCells(1, h + a, 3, h + a);
                            } else if ((i / 4) % 2 == 1) {
                                l = new Label(5, h + a, split3[i / 4], detFormat);
                                sheet.mergeCells(5, h + a, 8, h + a);
                                a++;
                            } else if (i == 4) {
                                l = new Label(5, 9, split3[1], detFormat);
                                sheet.mergeCells(5, h, 8, h);
                                a++;
                            }
                        } else {
                            l = new Label(1, 9, split3[0], detFormat);
                            sheet.mergeCells(1, h, 3, h);
                        }
                        sheet.addCell(l);
                    }
                } else if (split1.length == 1) {
                    l = new Label(1, h, split3[0], titleFormat);
                    sheet.addCell(l);
                    sheet.mergeCells(1, h, 3, h);
                }
                if (split1.length % 2 == 1) {
                    l = new Label(5, h + a, "", titleFormat);
                    sheet.addCell(l);
                    sheet.mergeCells(5, h + a, 8, h + a);
                }
            }

            sheet.setRowView(0, 800, false); //设置行高
            sheet.setRowView(1, 800, false); //设置行高
            sheet.setRowView(2, 2000, false); //设置行高
            sheet.setRowView(3, 800, false); //设置行高
            sheet.setRowView(4, 800, false); //设置行高
            sheet.setRowView(5, 800, false); //设置行高
            sheet.setRowView(6, 800, false); //设置行高
            sheet.setRowView(7, 800, false); //设置行高
            sheet.setRowView(8, 800, false); //设置行高
            sheet.setRowView(9, 800, false); //设置行高
            sheet.setRowView(10, 800, false); //设置行高
            sheet.setRowView(11, 800, false); //设置行高
            for (int i = 0; i <= a; i++) {
                sheet.setRowView(12 + i, 800, false); //设置行高
            }
            workbook.write();
            workbook.close();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }


    //将从数据库得到的数据转成pdf格式
    private void writeContentToPDF() throws Exception {
        Document document = new Document();
        File file = new File("sdcard/EasyFair/GoodsDetail.pdf");
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 10, Font.NORMAL);
        Font font1 = new Font(bfChinese, 20, Font.NORMAL);

        if (lang.equals("ch")) {

            /***** 第一行 ******/
            List<String> mList7 = new ArrayList<>();
            mList7.add(OtherUtils.GOODSDETAIL);
            createTable(mList7, document, font1);
            /***** 第二行 ******/
            List<String> mList6 = new ArrayList<>();
            if (tag == 1) {
                mList6.add(OtherUtils.SUPPLIERNAME);
                mList6.add(myUser.getCh_contact());
                mList6.add(OtherUtils.SUPPLIERCOMPANY);
                mList6.add(myUser.getCh_company_name());
                createTable(mList6, document, font);
            } else if (tag == 2) {
                mList6.add(OtherUtils.BUYERNAME);
                mList6.add(myUser.getCh_contact());
                mList6.add(OtherUtils.BUYERCOMPANY);
                mList6.add(myUser.getCh_company_name());
                createTable(mList6, document, font);
            }
            /***** 第三行 ******/
            PdfPTable table3 = new PdfPTable(5);
            table3.setWidths(new float[]{25f, 31.33f, 31.33f, 31.33f, 31.33f});
            // 第一列
            Paragraph paragraph7 = new Paragraph(OtherUtils.GOODSPIC, font);
            PdfPCell cell6 = new PdfPCell(paragraph7);
            cell6.setUseBorderPadding(true);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
            cell6.setMinimumHeight(100);// 设置表格的最小高度
            table3.addCell(cell6);

            // 第二列：添加图片
            List<Image> list = new ArrayList<>();

            byte[] product_imgs1 = myCommodity.getProduct_imgs1();
            byte[] product_imgs2 = myCommodity.getProduct_imgs2();
            byte[] product_imgs3 = myCommodity.getProduct_imgs3();
            byte[] product_imgs4 = myCommodity.getProduct_imgs4();
            list.add(Image.getInstance(product_imgs1));
            list.add(Image.getInstance(product_imgs2));
            list.add(Image.getInstance(product_imgs3));
            list.add(Image.getInstance(product_imgs4));

            createImageTable(list, document, table3);
            table3.completeRow();
            document.add(table3);
            /***** 第四行 ******/
            List<String> mList1 = new ArrayList<>();
            mList1.add(OtherUtils.GOODS_PARAMETER);
            createTable(mList1, document, font);
            /***** 第五行 ******/
            List<String> mList = new ArrayList<>();
            mList.add(OtherUtils.GOODS_NAME);
            mList.add(myCommodity.getName());
            mList.add(OtherUtils.GOODSPRICE);
            mList.add(String.valueOf(myCommodity.getPrice()));
            createTable(mList, document, font);
            /***** 第六行 ******/
            List<String> mList2 = new ArrayList<>();
            mList2.add(OtherUtils.ART_NO);
            mList2.add(myCommodity.getSerial_number());
            mList2.add(OtherUtils.GOODSMATERIAL);
            mList2.add(myCommodity.getMaterial());
            createTable(mList2, document, font);
            /***** 第七行 ******/
            List<String> mList3 = new ArrayList<>();
            mList3.add(OtherUtils.MOQ);
            mList3.add(myCommodity.getMoq() + "\t" + myCommodity.getUnit());
            mList3.add(OtherUtils.GOODSCOLOR);
            mList3.add(myCommodity.getColor());
            createTable(mList3, document, font);
            /***** 第八行 ******/
            List<String> mList4 = new ArrayList<>();
            mList4.add(OtherUtils.PRICECLAUSE);
            mList4.add(myCommodity.getPrice_clause() + "\t" + myCommodity.getPrice_clause_diy());
            mList4.add(OtherUtils.CURRENCY);
            mList4.add(myCommodity.getCurrency_variety());
            createTable(mList4, document, font);
            /***** 第九行 ******/
            List<String> mList5 = new ArrayList<>();
            mList5.add(OtherUtils.INTRODUCTION);
            mList5.add(myCommodity.getIntroduction());
            mList5.add(OtherUtils.REMARK);
            mList5.add(myCommodity.getRemark());
            createTable(mList5, document, font);

            /***** 第十行 ******/
            List<String> mList8 = new ArrayList<>();
            mList8.add(OtherUtils.OUTPAC);
            mList8.add(OtherUtils.NUMBER);
            mList8.add(myCommodity.getOutbox_number());
            mList8.add(OtherUtils.SIZE);
            mList8.add(myCommodity.getOutbox_size());
            mList8.add(OtherUtils.WEIGHT);
            mList8.add(myCommodity.getOutbox_weight() + "\t" + myCommodity.getOutbox_weight_unit());
            createTable(mList8, document, font);
            /***** 第十一行 ******/
            List<String> mList9 = new ArrayList<>();
            mList9.add(OtherUtils.INNER_PAC);
            mList9.add(OtherUtils.NUMBER);
            mList9.add(myCommodity.getCenterbox_number());
            mList9.add(OtherUtils.SIZE);
            mList9.add(myCommodity.getCenterbox_size());
            mList9.add(OtherUtils.WEIGHT);
            mList9.add(myCommodity.getCenterbox_weight() + "\t" + myCommodity.getCenterbox_weight_unit());
            createTable(mList9, document, font);
        } else if (lang.equals("en")) {


            /***** 第一行 ******/
            List<String> mList7 = new ArrayList<>();
            mList7.add("Product details single");
            createTable(mList7, document, font1);
            /***** 第二行 ******/
            List<String> mList6 = new ArrayList<>();
            mList6.add("Name");
            mList6.add(myUser.getCh_contact());
            mList6.add("Commpany Name");
            mList6.add(myUser.getCh_company_name());
            createTable(mList6, document, font);

            /***** 第三行 ******/
            PdfPTable table3 = new PdfPTable(5);
            table3.setWidths(new float[]{25f, 31.33f, 31.33f, 31.33f, 31.33f});
            // 第一列
            Paragraph paragraph7 = new Paragraph("Image", font);
            PdfPCell cell6 = new PdfPCell(paragraph7);
            cell6.setUseBorderPadding(true);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
            cell6.setMinimumHeight(100);// 设置表格的最小高度
            table3.addCell(cell6);

            // 第二列：添加图片
            List<Image> list = new ArrayList<>();

            byte[] product_imgs1 = myCommodity.getProduct_imgs1();
            byte[] product_imgs2 = myCommodity.getProduct_imgs2();
            byte[] product_imgs3 = myCommodity.getProduct_imgs3();
            byte[] product_imgs4 = myCommodity.getProduct_imgs4();
            list.add(Image.getInstance(product_imgs1));
            list.add(Image.getInstance(product_imgs2));
            list.add(Image.getInstance(product_imgs3));
            list.add(Image.getInstance(product_imgs4));

            createImageTable(list, document, table3);
            table3.completeRow();
            document.add(table3);
            /***** 第四行 ******/
            List<String> mList1 = new ArrayList<>();
            mList1.add("Parameter");
            createTable(mList1, document, font);
            /***** 第五行 ******/
            List<String> mList = new ArrayList<>();
            mList.add("Product name");
            mList.add(myCommodity.getName());
            mList.add("Commodity price");
            mList.add(String.valueOf(myCommodity.getPrice()));
            createTable(mList, document, font);
            /***** 第六行 ******/
            List<String> mList2 = new ArrayList<>();
            mList2.add("Item number");
            mList2.add(myCommodity.getSerial_number());
            mList2.add("Material");
            mList2.add(myCommodity.getMaterial());
            createTable(mList2, document, font);
            /***** 第七行 ******/
            List<String> mList3 = new ArrayList<>();
            mList3.add("MOQ");
            mList3.add(myCommodity.getMoq() + "\t" + myCommodity.getUnit());
            mList3.add("Color");
            mList3.add(myCommodity.getColor());
            createTable(mList3, document, font);
            /***** 第八行 ******/
            List<String> mList4 = new ArrayList<>();
            mList4.add("Price");
            mList4.add(myCommodity.getPrice_clause() + "\t" + myCommodity.getPrice_clause_diy());
            mList4.add("Currency");
            mList4.add(myCommodity.getCurrency_variety());
            createTable(mList4, document, font);
            /***** 第九行 ******/
            List<String> mList5 = new ArrayList<>();
            mList5.add("Introduction");
            mList5.add(myCommodity.getIntroduction());
            mList5.add("Remarks");
            mList5.add(myCommodity.getRemark());
            createTable(mList5, document, font);

            /***** 第十行 ******/
            List<String> mList8 = new ArrayList<>();
            mList8.add("Outer packing");
            mList8.add("Number");
            mList8.add(myCommodity.getOutbox_number());
            mList8.add("Size");
            mList8.add(myCommodity.getOutbox_size());
            mList8.add("Weight");
            mList8.add(myCommodity.getOutbox_weight() + "\t" + myCommodity.getOutbox_weight_unit());
            createTable(mList8, document, font);
            /***** 第十一行 ******/
            List<String> mList9 = new ArrayList<>();
            mList9.add("Box packaging");
            mList9.add("Number");
            mList9.add(myCommodity.getCenterbox_number());
            mList9.add("Size");
            mList9.add(myCommodity.getCenterbox_size());
            mList9.add("Weight");
            mList9.add(myCommodity.getCenterbox_weight() + "\t" + myCommodity.getCenterbox_weight_unit());
            createTable(mList9, document, font);
        }


        String self_defined = myCommodity.getDiy();
        if (self_defined != null && !self_defined.isEmpty()) {
            StringBuilder buffer = new StringBuilder();
            String[] split = self_defined.split("\\|");//分行 每行两条自定义的内容
            for (int i = 0; i < split.length; i++) {
                if (i % 2 == 0) {
                    buffer.append(split[i]).append("*");
                } else {
                    buffer.append(split[i]).append("|");
                }
            }
            String[] split7 = buffer.toString().split("\\|");
            for (String s : split7) {
                //分条 将每行的两条内容分开
                String[] split4 = s.split("\\*");
                if (split4.length == 2) {//这一行刚好有两条自定已的内容
                    //得到两条自定已的内容
                    String s1 = split4[0];
                    String s2 = split4[1];
                    //将第一条自定义内容分割
                    String[] split5 = s1.split(":");
                    //将第二条自定义内容分割
                    String[] split6 = s2.split(":");
                    /***** 自定义 ******/
                    List<String> mCustomList1 = new ArrayList<>();
                    mCustomList1.add(split5[0]);
                    mCustomList1.add(split5[1]);
                    mCustomList1.add(split6[0]);
                    mCustomList1.add(split6[1]);
                    createTable(mCustomList1, document, font);
                } else if (split4.length == 1) {//这一行自定已的内容只有一条
                    String s1 = split4[0];
                    String[] split5 = s1.split(":");
                    List<String> mCustomList2 = new ArrayList<>();
                    mCustomList2.add(split5[0]);
                    mCustomList2.add(split5[1]);
                    mCustomList2.add("");
                    mCustomList2.add("");
                    createTable(mCustomList2, document, font);
                }
            }
        }
        document.close();
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
                cell.setMinimumHeight(40);// 设置表格的最小高度
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
                // cell.setBorderWidth(1);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell.setMinimumHeight(40);// 设置表格的最小高度
                table.addCell(cell);
            }

        }
    }


}
