package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyInquiry;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
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

public class SendInquiryGoodsDataActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private RadioGroup send_rg;
    private RadioButton bluetooth_rb;
    private RadioButton email_rb;

    private RadioGroup document_rg;
    private RadioButton excel_input_rb;
    private RadioButton excel_rb;
    private RadioButton pdf_rb;

    private Button send_bt;
    private int sendFormate;
    private int fileFormate;

    //国际化相关
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private ArrayList<String> inquiry_id;//得到传递过来的id的集合

    private ArrayList<MyInquiry> myInquiries = new ArrayList<>();
    private MyInquiry myInquiry;

    private MyUser myUser;

    private String self_defined;
    private String[] split1, split2, split3;
    private int a;

    private BluetoothAdapter btAdapter;

    private String name, companyname;
    private String tag;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_send_one);
        initYuyan();
        initView();
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (inquiry_id != null) {
            inquiry_id.clear();
            inquiry_id = null;
        }
        if (myInquiries != null) {
            myInquiries.clear();
            myInquiries = null;
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(this);
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
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        send_rg = (RadioGroup) findViewById(R.id.send_rg);
        bluetooth_rb = (RadioButton) findViewById(R.id.bluetooth_rb);
        email_rb = (RadioButton) findViewById(R.id.email_rb);
        document_rg = (RadioGroup) findViewById(R.id.document_rg);
        excel_rb = (RadioButton) findViewById(R.id.excel_rb);
        excel_input_rb = (RadioButton) findViewById(R.id.excel_input_rb);
        pdf_rb = (RadioButton) findViewById(R.id.pdf_rb);
        send_bt = (Button) findViewById(R.id.send_bt);
        send_bt.setOnClickListener(SendInquiryGoodsDataActivity.this);
        fanhui_rl.setOnClickListener(SendInquiryGoodsDataActivity.this);
    }

    private void initData() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        inquiry_id = (ArrayList<String>) getIntent().getSerializableExtra("listForBuyer");
        name = getIntent().getStringExtra("name");
        companyname = getIntent().getStringExtra("companyname");
        lang = getIntent().getStringExtra("lang");
        //根据获得的多个id来查询询价表中的数据
        myInquiries = CursorUtils.selectInquiryByIds(this, inquiry_id);
        myUser = CursorUtils.selectOurInfo(this);

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
                //选择供导入的excel
                else if (i == excel_input_rb.getId()) {
                    fileFormate = 2;
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //发送
            case R.id.send_bt:
                switch (fileFormate) {
                    //excel
                    case 0:
                        writeContentToExcel("sdcard/EasyFair/xunjiadan.xls");
                        break;
                    //pdf
                    case 1:
                        try {
                            writeContentToPDF();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    //供导入的EXCEL
                    case 2:
                        excelOutforInput(myInquiries);
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


    //将数据库中的数据转成EXCEL
    public void writeContentToExcel(String fileName) {
        try {
            Number n = null;
            DateTime d = null;
            File tempFile = new File(fileName);
            //创建工作薄
            WritableWorkbook workbook = null;
            workbook = Workbook.createWorkbook(tempFile);
            int size = myInquiries.size();
            for (int j = 0; j < size; j++) {
                myInquiry = myInquiries.get(j);
                WritableSheet sheet = workbook.createSheet(myInquiry.getSerial_number(), j);
                createExcel(workbook, sheet, j);
            }
            workbook.write();
            workbook.close();


        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }

    private void createExcel(WritableWorkbook workbook, WritableSheet sheet, int k) throws WriteException, IOException {
        String j = String.valueOf(k);
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

        byte[] product_imgs1 = myInquiry.getProduct_imgs1();
        byte[] product_imgs2 = myInquiry.getProduct_imgs2();
        byte[] product_imgs3 = myInquiry.getProduct_imgs3();
        byte[] product_imgs4 = myInquiry.getProduct_imgs4();
        Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);

        Label l = null;
        // 创建单元格
        if (lang.equals("ch")) {
            //第一行
            l = new Label(0, 0, OtherUtils.INQUIRYLIST, headerFormat);
            sheet.addCell(l);
            sheet.mergeCells(0, 0, 8, 0);//合并单元格
            //第二行
            l = new Label(0, 1, OtherUtils.SUPPLIERNAME, titleFormat);
            sheet.addCell(l);

            l = new Label(1, 1, name, detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 1, 3, 1);
            //公司名
            l = new Label(4, 1, OtherUtils.SUPPLIERCOMPANY, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 1, companyname, detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 1, 8, 1);

            //第三行
            l = new Label(0, 2, OtherUtils.INQUIRY_TYPE, titleFormat);
            sheet.addCell(l);
            String type = null;
            if (myInquiry.getInquiry_type() != null) {
                String quote_type = myInquiry.getInquiry_type();
                switch (quote_type) {
                    case "1":
                        type = "已询价";
                        break;
                    case "2":
                        type = "预留询价";
                        break;
                    case "3":
                        type = "留样询价";
                        break;
                    case "":
                        type = "无";
                        break;
                }
            } else {
                type = "无";
            }

            l = new Label(1, 2, type, titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 2, 3, 2);

            l = new Label(4, 2, OtherUtils.INQUIRY_TIME, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 2, myInquiry.getInquiry_time(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 2, 8, 2);

            //第四行
            l = new Label(0, 3, OtherUtils.GOODSPIC, titleFormat);
            sheet.addCell(l);

            Date now = new Date();
            SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = da.format(now);
            //图片1
            OtherUtils.savePNG_After(bitmap1, "sdcard/EasyFair/" + format + "1" + ".png");
            File file = new File("sdcard/EasyFair/" + format + "1" + ".png");
            //前两位是起始格，后两位是图片占多少个格，并非是位置
            WritableImage image = new WritableImage(1, 3, 2, 1, file);
            sheet.addImage(image);
            //图片2
            OtherUtils.savePNG_After(bitmap2, "sdcard/EasyFair/" + format + "2" + ".png");
            File file2 = new File("sdcard/EasyFair/" + format + "2" + ".png");
            WritableImage image2 = new WritableImage(3, 3, 2, 1, file2);
            sheet.addImage(image2);
            //图片3
            OtherUtils.savePNG_After(bitmap3, "sdcard/EasyFair/" + format + "3" + ".png");
            File file3 = new File("sdcard/EasyFair/" + format + "3" + ".png");
            WritableImage image3 = new WritableImage(5, 3, 2, 1, file3);
            sheet.addImage(image3);
            //图片4
            OtherUtils.savePNG_After(bitmap4, "sdcard/EasyFair/" + format + "4" + ".png");
            File file4 = new File("sdcard/EasyFair/" + format + "4" + ".png");
            WritableImage image4 = new WritableImage(7, 3, 2, 1, file4);
            sheet.addImage(image4);

            //第五行
            l = new Label(0, 4, OtherUtils.GOODS_PARAMETER, titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(0, 4, 8, 4);

            //第六行
            l = new Label(0, 5, OtherUtils.GOODS_NAME, titleFormat);
            sheet.addCell(l);
            l = new Label(1, 5, myInquiry.getGoods_name(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 5, 3, 5);
            l = new Label(4, 5, OtherUtils.GOODSPRICE, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 5, String.valueOf(myInquiry.getPrice()), priceFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 5, 8, 5);

            //第七行
            l = new Label(0, 6, OtherUtils.ART_NO, titleFormat);
            sheet.addCell(l);
            l = new Label(1, 6, myInquiry.getSerial_number(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 6, 3, 6);
            l = new Label(4, 6, OtherUtils.GOODSMATERIAL, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 6, myInquiry.getMaterial(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 6, 8, 6);

            //第八行
            l = new Label(0, 7, OtherUtils.MOQ, titleFormat);
            sheet.addCell(l);
            l = new Label(1, 7, myInquiry.getMoq() + "\t" + myInquiry.getGoods_unit(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 7, 3, 7);
            l = new Label(4, 7, OtherUtils.GOODSCOLOR, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 7, myInquiry.getColor(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 7, 8, 7);

            //第九行
            l = new Label(0, 8, OtherUtils.PRICECLAUSE, titleFormat);
            sheet.addCell(l);
            l = new Label(1, 8, myInquiry.getPrice_clause() + "\t\t" + myInquiry.getPrice_clause_diy(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 8, 3, 8);
            l = new Label(4, 8, OtherUtils.CURRENCY, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 8, myInquiry.getCurrency_varitety(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 8, 8, 8);

            //第十行
            l = new Label(0, 9, OtherUtils.INTRODUCTION, titleFormat);
            sheet.addCell(l);
            l = new Label(1, 9, myInquiry.getIntroduction(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 9, 3, 9);
            l = new Label(4, 9, OtherUtils.REMARK, titleFormat);
            sheet.addCell(l);
            l = new Label(5, 9, myInquiry.getRemark(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 9, 8, 9);

            //第十一至十三行
            l = new Label(0, 10, OtherUtils.OUTPAC, titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(0, 10, 0, 12);

            l = new Label(1, 10, OtherUtils.NUMBER, priceFormat);
            sheet.addCell(l);
            l = new Label(2, 10, myInquiry.getOutbox_number(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(2, 10, 3, 10);

            l = new Label(1, 11, OtherUtils.SIZE, detFormat);
            sheet.addCell(l);
            l = new Label(2, 11, myInquiry.getOutbox_size(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(2, 11, 3, 11);
            l = new Label(1, 12, OtherUtils.WEIGHT, detFormat);
            sheet.addCell(l);
            l = new Label(2, 12, myInquiry.getOutbox_weight() + "\t\t" + myInquiry.getOutbox_weight_unit(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(2, 12, 3, 12);

            l = new Label(4, 10, OtherUtils.INNER_PAC, titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(4, 10, 4, 12);

            l = new Label(5, 10, OtherUtils.NUMBER, priceFormat);
            sheet.addCell(l);

            l = new Label(6, 10, myInquiry.getCenterbox_number(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(6, 10, 8, 7);

            l = new Label(5, 11, OtherUtils.SIZE, titleFormat);
            sheet.addCell(l);

            l = new Label(6, 11, myInquiry.getCenterbox_size(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(6, 11, 8, 7);

            l = new Label(5, 12, OtherUtils.WEIGHT, titleFormat);
            sheet.addCell(l);

            l = new Label(6, 12, myInquiry.getCenterbox_weight() + "\t" + myInquiry.getCenterbox_weight_unit(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(6, 12, 8, 12);
        } else if (lang.equals("en")) {
            //第一行
            l = new Label(0, 0, "Commodity inquiry form", headerFormat);
            sheet.addCell(l);
            sheet.mergeCells(0, 0, 8, 0);//合并单元格
            //第二行
            l = new Label(0, 1, "Supplier name", titleFormat);
            sheet.addCell(l);

            l = new Label(1, 1, name, detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 1, 3, 1);
            //公司名
            l = new Label(4, 1, "Supplier company name", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 1, companyname, detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 1, 8, 1);

            //第三行
            l = new Label(0, 2, "Inquiry type", titleFormat);
            sheet.addCell(l);
            String type = null;
            if (myInquiry.getInquiry_type() != null) {
                String quote_type = myInquiry.getInquiry_type();
                switch (quote_type) {
                    case "1":
                        type = "Has been inquiry";
                        break;
                    case "2":
                        type = "Reserved inquiry";
                        break;
                    case "3":
                        type = "Sample inquiry";
                        break;
                    case "":
                        type = "no";
                        break;
                }
            } else {
                type = "no";
            }

            l = new Label(1, 2, type, titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 2, 3, 2);

            l = new Label(4, 2, "Inquiry time", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 2, myInquiry.getInquiry_time(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 2, 8, 2);

            //第四行
            l = new Label(0, 3, "Product Image", titleFormat);
            sheet.addCell(l);

            Date now = new Date();
            SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = da.format(now);
            //图片1
            OtherUtils.savePNG_After(bitmap1, "sdcard/EasyFair/" + format + "1" + ".png");
            File file = new File("sdcard/EasyFair/" + format + "1" + ".png");
            //前两位是起始格，后两位是图片占多少个格，并非是位置
            WritableImage image = new WritableImage(1, 3, 2, 1, file);
            sheet.addImage(image);
            //图片2
            OtherUtils.savePNG_After(bitmap2, "sdcard/EasyFair/" + format + "2" + ".png");
            File file2 = new File("sdcard/EasyFair/" + format + "2" + ".png");
            WritableImage image2 = new WritableImage(3, 3, 2, 1, file2);
            sheet.addImage(image2);
            //图片3
            OtherUtils.savePNG_After(bitmap3, "sdcard/EasyFair/" + format + "3" + ".png");
            File file3 = new File("sdcard/EasyFair/" + format + "3" + ".png");
            WritableImage image3 = new WritableImage(5, 3, 2, 1, file3);
            sheet.addImage(image3);
            //图片4
            OtherUtils.savePNG_After(bitmap4, "sdcard/EasyFair/" + format + "4" + ".png");
            File file4 = new File("sdcard/EasyFair/" + format + "4" + ".png");
            WritableImage image4 = new WritableImage(7, 3, 2, 1, file4);
            sheet.addImage(image4);

            //第五行
            l = new Label(0, 4, "Product parameters", titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(0, 4, 8, 4);

            //第六行
            l = new Label(0, 5, "product name", titleFormat);
            sheet.addCell(l);
            l = new Label(1, 5, myInquiry.getGoods_name(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 5, 3, 5);
            l = new Label(4, 5, "Product Image", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 5, String.valueOf(myInquiry.getPrice()), priceFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 5, 8, 5);

            //第七行
            l = new Label(0, 6, "Product code", titleFormat);
            sheet.addCell(l);
            l = new Label(1, 6, myInquiry.getSerial_number(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 6, 3, 6);
            l = new Label(4, 6, "Material", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 6, myInquiry.getMaterial(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 6, 8, 6);

            //第八行
            l = new Label(0, 7, "MOQ", titleFormat);
            sheet.addCell(l);
            l = new Label(1, 7, myInquiry.getMoq() + "\t" + myInquiry.getGoods_unit(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 7, 3, 7);
            l = new Label(4, 7, "Color", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 7, myInquiry.getColor(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 7, 8, 7);

            //第九行
            l = new Label(0, 8, "pricing term", titleFormat);
            sheet.addCell(l);
            l = new Label(1, 8, myInquiry.getPrice_clause() + "\t\t" + myInquiry.getPrice_clause_diy(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 8, 3, 8);
            l = new Label(4, 8, "Type of currency", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 8, myInquiry.getCurrency_varitety(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 8, 8, 8);

            //第十行
            l = new Label(0, 9, "Introduction", titleFormat);
            sheet.addCell(l);
            l = new Label(1, 9, myInquiry.getIntroduction(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(1, 9, 3, 9);
            l = new Label(4, 9, "Remarks", titleFormat);
            sheet.addCell(l);
            l = new Label(5, 9, myInquiry.getRemark(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(5, 9, 8, 9);

            //第十一至十三行
            l = new Label(0, 10, "Outer packing", titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(0, 10, 0, 12);

            l = new Label(1, 10, "Number", priceFormat);
            sheet.addCell(l);
            l = new Label(2, 10, myInquiry.getOutbox_number(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(2, 10, 3, 10);

            l = new Label(1, 11, "Size", detFormat);
            sheet.addCell(l);
            l = new Label(2, 11, myInquiry.getOutbox_size(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(2, 11, 3, 11);
            l = new Label(1, 12, "Weight", detFormat);
            sheet.addCell(l);
            l = new Label(2, 12, myInquiry.getOutbox_weight() + "\t\t" + myInquiry.getOutbox_weight_unit(), detFormat);
            sheet.addCell(l);
            sheet.mergeCells(2, 12, 3, 12);

            l = new Label(4, 10, "In the box packaging", titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(4, 10, 4, 12);

            l = new Label(5, 10, "Number", priceFormat);
            sheet.addCell(l);

            l = new Label(6, 10, myInquiry.getCenterbox_number(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(6, 10, 8, 7);

            l = new Label(5, 11, "Size", titleFormat);
            sheet.addCell(l);

            l = new Label(6, 11, myInquiry.getCenterbox_size(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(6, 11, 8, 7);

            l = new Label(5, 12, "Weight", titleFormat);
            sheet.addCell(l);

            l = new Label(6, 12, myInquiry.getCenterbox_weight() + "\t" + myInquiry.getCenterbox_weight_unit(), titleFormat);
            sheet.addCell(l);
            sheet.mergeCells(6, 12, 8, 12);
        }


        String self_defined = myInquiry.getSelf_defined();
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

        int a = 0;
        int h = 13;//行数-自定义从第十四行开始
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
        sheet.setRowView(2, 800, false); //设置行高
        sheet.setRowView(3, 2000, false); //设置行高
        sheet.setRowView(4, 800, false); //设置行高
        sheet.setRowView(5, 800, false); //设置行高
        sheet.setRowView(6, 800, false); //设置行高
        sheet.setRowView(7, 800, false); //设置行高
        sheet.setRowView(8, 800, false); //设置行高
        sheet.setRowView(9, 800, false); //设置行高
        sheet.setRowView(10, 800, false); //设置行高
        sheet.setRowView(11, 800, false); //设置行高
        sheet.setRowView(12, 800, false); //设置行高
        for (int i = 0; i <= a; i++) {
            sheet.setRowView(13 + i, 800, false); //设置行高
        }

    }


    //pdf部分
    //将从数据库得到的数据转成pdf格式
    private void writeContentToPDF() throws Exception {
        Document document = new Document();
        File file = new File("sdcard/EasyFair/xunjiadan.pdf");
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 10, Font.NORMAL);
        int size = myInquiries.size();
        for (int i = 0; i < size; i++) {
            myInquiry = myInquiries.get(i);
            createPdf(document, font);
        }
        document.close();
    }

    private void createPdf(Document document, Font font) {
        try {

            if (lang.equals("ch")) {
                /***** 第一行 ******/
                PdfPTable pdfTable = new PdfPTable(1);
                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                Font font1 = new Font(bfChinese, 20, Font.NORMAL);
                Paragraph paragraph = new Paragraph("商品询价单", font1);
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
                mList6.add(companyname);
                mList6.add("姓名");
                mList6.add(name);
                createTable(mList6, document, font);
                /***** 第三行 ******/
                List<String> mList7 = new ArrayList<>();
                mList7.add("询价类型");
                String inquiry_type = myInquiry.getInquiry_type();
                String type = null;
                switch (inquiry_type) {
                    case "1":
                        type = "已询价";
                        break;
                    case "2":
                        type = "预留询价";
                        break;
                    case "3":
                        type = "留样询价";
                        break;
                    case "":
                        type = "无";
                        break;
                }
                mList7.add(type);
                mList7.add("询价时间");
                mList7.add(myInquiry.getInquiry_time());
                createTable(mList7, document, font);
                /***** 第四行 ******/
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
                byte[] product_imgs1 = myInquiry.getProduct_imgs1();
                byte[] product_imgs2 = myInquiry.getProduct_imgs2();
                byte[] product_imgs3 = myInquiry.getProduct_imgs3();
                byte[] product_imgs4 = myInquiry.getProduct_imgs4();

                list.add(Image.getInstance(product_imgs1));
                list.add(Image.getInstance(product_imgs2));
                list.add(Image.getInstance(product_imgs3));
                list.add(Image.getInstance(product_imgs4));

                createImageTable(list, document, table3);
                table3.completeRow();
                document.add(table3);
                /***** 第五行 ******/
                List<String> mList1 = new ArrayList<>();
                mList1.add("产品参数详情");
                createTable(mList1, document, font);
                /***** 第六行 ******/
                List<String> mList = new ArrayList<>();
                mList.add("商品名称");
                mList.add(myInquiry.getGoods_name());
                mList.add("商品价格");
                mList.add(String.valueOf(myInquiry.getPrice()));
                createTable(mList, document, font);
                /***** 第七行 ******/
                List<String> mList2 = new ArrayList<>();
                mList2.add("公司货号");
                mList2.add(myInquiry.getSerial_number());
                mList2.add("商品材质");
                mList2.add(myInquiry.getMaterial());
                createTable(mList2, document, font);
                /***** 第八行 ******/
                List<String> mList3 = new ArrayList<>();
                mList3.add("商品单位");
                mList3.add(myInquiry.getGoods_unit());
                mList3.add("商品颜色");
                mList3.add(myInquiry.getColor());
                createTable(mList3, document, font);
                /***** 第九行 ******/
                List<String> mList4 = new ArrayList<>();
                mList4.add("价格条款");
                mList4.add(myInquiry.getPrice_clause());
                mList4.add("货币类型");
                mList4.add(myInquiry.getCurrency_varitety());
                createTable(mList4, document, font);
                /***** 第十行 ******/
                List<String> mList5 = new ArrayList<>();
                mList5.add("商品介绍");
                mList5.add(myInquiry.getIntroduction());
                mList5.add("商品备注");
                mList5.add(myInquiry.getRemark());
                createTable(mList5, document, font);
                /***** 第十一行 ******/
                List<String> mList8 = new ArrayList<>();
                mList8.add(OtherUtils.OUTPAC);
                mList8.add(OtherUtils.NUMBER);
                mList8.add(myInquiry.getOutbox_number());
                mList8.add(OtherUtils.SIZE);
                mList8.add(myInquiry.getOutbox_size());
                mList8.add(OtherUtils.WEIGHT);
                mList8.add(myInquiry.getOutbox_weight() + "\t" + myInquiry.getOutbox_weight_unit());
                createTable(mList8, document, font);
                /***** 第十二行 ******/
                List<String> mList9 = new ArrayList<>();
                mList9.add(OtherUtils.INNER_PAC);
                mList9.add(OtherUtils.NUMBER);
                mList9.add(myInquiry.getCenterbox_number());
                mList9.add(OtherUtils.SIZE);
                mList9.add(myInquiry.getCenterbox_size());
                mList9.add(OtherUtils.WEIGHT);
                mList9.add(myInquiry.getCenterbox_weight() + "\t" + myInquiry.getCenterbox_weight_unit());
                createTable(mList9, document, font);
            } else if (lang.equals("en")) {
                /***** 第一行 ******/
                PdfPTable pdfTable = new PdfPTable(1);
                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                Font font1 = new Font(bfChinese, 20, Font.NORMAL);
                Paragraph paragraph = new Paragraph("Commodity inquiry form", font1);
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
                mList6.add("Company name");
                mList6.add(companyname);
                mList6.add("Name");
                mList6.add(name);
                createTable(mList6, document, font);
                /***** 第三行 ******/
                List<String> mList7 = new ArrayList<>();
                mList7.add("Inquiry type");
                String inquiry_type = myInquiry.getInquiry_type();
                String type = null;
                switch (inquiry_type) {
                    case "1":
                        type = "Has been inquiry";
                        break;
                    case "2":
                        type = "Reserved inquiry";
                        break;
                    case "3":
                        type = "Sample inquiry";
                        break;
                    case "":
                        type = "no";
                        break;
                }
                mList7.add(type);
                mList7.add("Inquiry time");
                mList7.add(myInquiry.getInquiry_time());
                createTable(mList7, document, font);
                /***** 第四行 ******/
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
                byte[] product_imgs1 = myInquiry.getProduct_imgs1();
                byte[] product_imgs2 = myInquiry.getProduct_imgs2();
                byte[] product_imgs3 = myInquiry.getProduct_imgs3();
                byte[] product_imgs4 = myInquiry.getProduct_imgs4();

                list.add(Image.getInstance(product_imgs1));
                list.add(Image.getInstance(product_imgs2));
                list.add(Image.getInstance(product_imgs3));
                list.add(Image.getInstance(product_imgs4));

                createImageTable(list, document, table3);
                table3.completeRow();
                document.add(table3);
                /***** 第五行 ******/
                List<String> mList1 = new ArrayList<>();
                mList1.add("Product Parameter Details");
                createTable(mList1, document, font);
                /***** 第六行 ******/
                List<String> mList = new ArrayList<>();
                mList.add("Product name");
                mList.add(myInquiry.getGoods_name());
                mList.add("Commodity price");
                mList.add(String.valueOf(myInquiry.getPrice()));
                createTable(mList, document, font);
                /***** 第七行 ******/
                List<String> mList2 = new ArrayList<>();
                mList2.add("Company number");
                mList2.add(myInquiry.getSerial_number());
                mList2.add("Material");
                mList2.add(myInquiry.getMaterial());
                createTable(mList2, document, font);
                /***** 第八行 ******/
                List<String> mList3 = new ArrayList<>();
                mList3.add("Commodity units");
                mList3.add(myInquiry.getGoods_unit());
                mList3.add("Product's Colour");
                mList3.add(myInquiry.getColor());
                createTable(mList3, document, font);
                /***** 第九行 ******/
                List<String> mList4 = new ArrayList<>();
                mList4.add("Pricing term");
                mList4.add(myInquiry.getPrice_clause());
                mList4.add("Type of currency");
                mList4.add(myInquiry.getCurrency_varitety());
                createTable(mList4, document, font);
                /***** 第十行 ******/
                List<String> mList5 = new ArrayList<>();
                mList5.add("Product desciption");
                mList5.add(myInquiry.getIntroduction());
                mList5.add("Product Notes");
                mList5.add(myInquiry.getRemark());
                createTable(mList5, document, font);
                /***** 第十一行 ******/
                List<String> mList8 = new ArrayList<>();
                mList8.add("Outer packing");
                mList8.add(OtherUtils.NUMBER);
                mList8.add(myInquiry.getOutbox_number());
                mList8.add("Size");
                mList8.add(myInquiry.getOutbox_size());
                mList8.add("Weight");
                mList8.add(myInquiry.getOutbox_weight() + "\t" + myInquiry.getOutbox_weight_unit());
                createTable(mList8, document, font);
                /***** 第十二行 ******/
                List<String> mList9 = new ArrayList<>();
                mList9.add("In the box packaging");
                mList9.add("Number");
                mList9.add(myInquiry.getCenterbox_number());
                mList9.add("Size");
                mList9.add(myInquiry.getCenterbox_size());
                mList9.add("Weight");
                mList9.add(myInquiry.getCenterbox_weight() + "\t" + myInquiry.getCenterbox_weight_unit());
                createTable(mList9, document, font);
            }


            /***** 第十三行开始 ******/
            String self_defined = myInquiry.getSelf_defined();
            if (self_defined != null && !self_defined.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
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
                // cell.setBorderWidth(1);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell.setMinimumHeight(50);// 设置表格的最小高度
                table.addCell(cell);
            }

        }
    }


    //供导入的excel
    // 将数据转成excel格式（供导入用）
    public void excelOutforInput(ArrayList<MyInquiry> myInquiries) {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(new File("sdcard/EasyFair/xunjia.xls"));
            //通过excel对象创建一个选项卡
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            Label la = new Label(0, 0, "商品表id");
            sheet.addCell(la);
            la = new Label(1, 0, "商品货号");
            sheet.addCell(la);
            la = new Label(2, 0, "商品名称");
            sheet.addCell(la);
            la = new Label(3, 0, "商品单位");
            sheet.addCell(la);
            la = new Label(4, 0, "商品材质");
            sheet.addCell(la);
            la = new Label(5, 0, "商品颜色");
            sheet.addCell(la);
            la = new Label(6, 0, "商品价格");
            sheet.addCell(la);
            la = new Label(7, 0, "货币类型");
            sheet.addCell(la);
            la = new Label(8, 0, "价格条款");
            sheet.addCell(la);
            la = new Label(9, 0, "价格条款自定义");
            sheet.addCell(la);
            la = new Label(10, 0, "备注");
            sheet.addCell(la);
            la = new Label(11, 0, "商品简介");
            sheet.addCell(la);
            la = new Label(12, 0, "自定义输入部分");
            sheet.addCell(la);
            la = new Label(13, 0, "商品重量");
            sheet.addCell(la);
            la = new Label(14, 0, "重量单位");
            sheet.addCell(la);
            la = new Label(15, 0, "外箱数量");
            sheet.addCell(la);
            la = new Label(16, 0, "外箱尺寸");
            sheet.addCell(la);
            la = new Label(17, 0, "外箱重量");
            sheet.addCell(la);
            la = new Label(18, 0, "外箱重量单位");
            sheet.addCell(la);
            la = new Label(19, 0, "中盒数量");
            sheet.addCell(la);
            la = new Label(20, 0, "中盒尺寸");
            sheet.addCell(la);
            la = new Label(21, 0, "中盒重量");
            sheet.addCell(la);
            la = new Label(22, 0, "中盒重量单位");
            sheet.addCell(la);
            la = new Label(23, 0, "公司名称");
            sheet.addCell(la);
            la = new Label(24, 0, "姓名");
            sheet.addCell(la);
            la = new Label(25, 0, "唯一识别id");
            sheet.addCell(la);
            la = new Label(26, 0, "最少起订量");
            sheet.addCell(la);


            for (int i = 0; i < myInquiries.size(); i++) {
                MyInquiry myInquiry = myInquiries.get(i);
                // 创建一个单元格对象 ：列 行 值
                Label la1 = new Label(0, i + 1, String.valueOf(myInquiry.getCommodity_id()));//商品表id
                Label la2 = new Label(1, i + 1, myInquiry.getSerial_number());//商品货号
                Label la3 = new Label(2, i + 1, myInquiry.getGoods_name());//商品名
                Label la4 = new Label(3, i + 1, myInquiry.getGoods_unit());//商品单位
                Label la5 = new Label(4, i + 1, myInquiry.getMaterial());//商品材质
                Label la6 = new Label(5, i + 1, myInquiry.getColor());//商品颜色
                Label la7 = new Label(6, i + 1, String.valueOf(myInquiry.getPrice()));//商品价格
                Label la8 = new Label(7, i + 1, myInquiry.getCurrency_varitety());//货币类型
                Label la9 = new Label(8, i + 1, myInquiry.getPrice_clause());//价格条款
                Label la10 = new Label(9, i + 1, myInquiry.getPrice_clause_diy());//价格条款自定义
                Label la11 = new Label(10, i + 1, myInquiry.getRemark());//备注
                Label la12 = new Label(11, i + 1, myInquiry.getIntroduction());//商品简介
                Label la13 = new Label(12, i + 1, myInquiry.getSelf_defined());//自定义输入部分
                Label la14 = new Label(13, i + 1, myInquiry.getGoods_weight());//商品重量
                Label la15 = new Label(14, i + 1, myInquiry.getGoods_weight_unit());//重量单位
                Label la16 = new Label(15, i + 1, myInquiry.getOutbox_number());//外箱数量
                Label la17 = new Label(16, i + 1, myInquiry.getOutbox_size());//外箱尺寸
                Label la18 = new Label(17, i + 1, myInquiry.getOutbox_weight());//外箱重量
                Label la19 = new Label(18, i + 1, myInquiry.getOutbox_weight_unit());//外箱重量单位
                Label la20 = new Label(19, i + 1, myInquiry.getCenterbox_number());//中盒数量
                Label la21 = new Label(20, i + 1, myInquiry.getCenterbox_size());//中盒尺寸
                Label la22 = new Label(21, i + 1, myInquiry.getCenterbox_weight());//中盒重量
                Label la23 = new Label(22, i + 1, myInquiry.getCenterbox_weight_unit());//中盒重量单位
                Label la24 = new Label(23, i + 1, myUser.getCh_company_name());//自己的公司名
                Label la25 = new Label(24, i + 1, myUser.getCh_contact());//自己的名字
                Label la26 = new Label(25, i + 1, myUser.getUnique_id());//自己的唯一id
                Label la27 = new Label(26, i + 1, myInquiry.getMoq());//最少起订量

                sheet.addCell(la1);
                sheet.addCell(la2);
                sheet.addCell(la3);
                sheet.addCell(la4);
                sheet.addCell(la5);
                sheet.addCell(la6);
                sheet.addCell(la7);
                sheet.addCell(la8);
                sheet.addCell(la9);
                sheet.addCell(la10);
                sheet.addCell(la11);
                sheet.addCell(la12);
                sheet.addCell(la13);
                sheet.addCell(la14);
                sheet.addCell(la15);
                sheet.addCell(la16);
                sheet.addCell(la17);
                sheet.addCell(la18);
                sheet.addCell(la19);
                sheet.addCell(la20);
                sheet.addCell(la21);
                sheet.addCell(la22);
                sheet.addCell(la23);
                sheet.addCell(la24);
                sheet.addCell(la25);
                sheet.addCell(la26);
                sheet.addCell(la27);

            }
            workbook.write();
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


    //蓝牙发送
    public void sendBluetooth() {
        if (btAdapter != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            // 附件
            // 发送的类型（带附件的发送）
            File file = null;
            if (fileFormate == 0) {
                file = new File("sdcard/EasyFair/xunjiadan.xls");
            } else if (fileFormate == 1) {
                file = new File("sdcard/EasyFair/xunjiadan.pdf");
            } else if (fileFormate == 2) {
                file = new File("sdcard/EasyFair/xunjia.xls");
            } else {
                ToastUtil.showToast2(SendInquiryGoodsDataActivity.this, R.string.select_fsfs);
                return;
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
                    if (packageName.equals("com.android.bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;// 找到，结束查找
                    }
                }
                if (!found) {
                    Toast.makeText(SendInquiryGoodsDataActivity.this, R.string.the_phone_not_have_bluetooth, Toast.LENGTH_SHORT).show();
                }
            }
            // 设置启动蓝牙intent
            intent.setClassName(packageName, className);
            startActivity(intent);
        }
    }

    //emai发送
    public void sendEmail() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        // 附件
        // 邮件发送的类型（带附件的发送）
        File file = null;
        if (fileFormate == 0) {
            file = new File("sdcard/EasyFair/xunjiadan.xls");
        } else if (fileFormate == 1) {
            file = new File("sdcard/EasyFair/xunjiadan.pdf");
        } else if (fileFormate == 2) {
            file = new File("sdcard/EasyFair/xunjia.xls");
        } else {
            ToastUtil.showToast2(SendInquiryGoodsDataActivity.this, R.string.select_fsfs);
            return;
        }
        intent.setType("*/*");
        // 附件
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        /**将可以以email方式发送文件的应用进行过滤，然后只剩下email*/
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
                Toast.makeText(SendInquiryGoodsDataActivity.this, R.string.the_phone_not_have_email, Toast.LENGTH_SHORT).show();
            }
        }
        // 设置启动蓝牙intent
        intent.setClassName(packageName, className);
        startActivity(intent);

    }


}
