package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.dao.EasyInquiryDao;
import com.baibeiyun.eazyfair.entities.EasyInquiry;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CreameBitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.view.MyDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cn.hzw.graffiti.GraffitiActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddEasyInquiryGoodsActivity extends BaseActivityforBuyer implements View.OnClickListener {

    private RelativeLayout fanhui_rl;
    private EditText input_goods_name_et;
    private Button upload_image_iv;
    private ImageView image1_iv, image2_iv, image3_iv, image4_iv;
    private EditText content_et;
    private Button confirm_bt;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private int from = 0;
    private PopupWindow mPopupWindow;

    private List<ImageView> imageViewList = new ArrayList<>();//存放显示图片的四个ImageView
    private final int REQUEST_IMAGE = 2;
    private final int maxNum = 4;//最多显示四张图片
    private ArrayList<String> mSelectPath;//所选的图片的地址
    private List<Bitmap> bitmapList = new ArrayList<>();//存放所选的图片
    private List<String> finalSelectPaths = new ArrayList<>();//存放最终所选择的图片的地址
    public static final int REQ_CODE_GRAFFITI = 101;
    //得到当前输入的数据
    private String allPictureUrl;
    private String uniqued_id;
    private MyDialog myDialog;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==4) {
                myDialog.dialogDismiss();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_easy_inquiry_goods);
        initYuyan();
        initView();
        initData();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(AddEasyInquiryGoodsActivity.this);
        if (language != null) {
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

    private void initView() {
        myDialog=new MyDialog(this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_goods_name_et = (EditText) findViewById(R.id.input_goods_name_et);
        upload_image_iv = (Button) findViewById(R.id.upload_image_iv);
        image1_iv = (ImageView) findViewById(R.id.image1_iv);
        image2_iv = (ImageView) findViewById(R.id.image2_iv);
        image3_iv = (ImageView) findViewById(R.id.image3_iv);
        image4_iv = (ImageView) findViewById(R.id.image4_iv);
        content_et = (EditText) findViewById(R.id.content_et);
        confirm_bt = (Button) findViewById(R.id.confirm_bt);
        fanhui_rl.setOnClickListener(this);
        upload_image_iv.setOnClickListener(this);
        confirm_bt.setOnClickListener(this);
        imageViewList.add(image1_iv);
        imageViewList.add(image2_iv);
        imageViewList.add(image3_iv);
        imageViewList.add(image4_iv);

    }

    private void initData() {
        Intent intent = getIntent();
        uniqued_id = intent.getStringExtra("unique_id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.upload_image_iv:
                from = Location.BOTTOM.ordinal();
                initPopupWindow();
                break;
            case R.id.confirm_bt:
                myDialog.dialogShow();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saved();

                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);


                    }
                }).start();

                break;
        }
    }


    private void saved() {
        EasyInquiry easyInquiry = new EasyInquiry();
        Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
        byte[] bytes_no = BitmapUtils.compressBmpFromBmp(bitmap_no);
        if (allPictureUrl != null) {
            //  2016/12/7 计算出有几个“|”线
            int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
            //一张图片
            if (i == 0) {
                Bitmap bitmap = ImageFactoryandOther.getSmallBitmap(allPictureUrl);
                //将bitmap转换为二进制流
                byte[] bytes = BitmapUtils.compressBmpFromBmp(bitmap);
                easyInquiry.setGoods_img1(bytes);
                easyInquiry.setGoods_img2(bytes_no);
                easyInquiry.setGoods_img3(bytes_no);
                easyInquiry.setGoods_img4(bytes_no);
                //两张图片
            } else if (i == 1) {
                String[] split = allPictureUrl.split("\\|");
                Bitmap bitmap1 = ImageFactoryandOther.getSmallBitmap(split[0]);
                Bitmap bitmap2 = ImageFactoryandOther.getSmallBitmap(split[1]);
                byte[] bytes1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                byte[] bytes2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                easyInquiry.setGoods_img1(bytes1);
                easyInquiry.setGoods_img2(bytes2);
                easyInquiry.setGoods_img3(bytes_no);
                easyInquiry.setGoods_img4(bytes_no);
            } else if (i == 2) {
                String[] split = allPictureUrl.split("\\|");
                Bitmap bitmap1 = ImageFactoryandOther.getSmallBitmap(split[0]);
                Bitmap bitmap2 = ImageFactoryandOther.getSmallBitmap(split[1]);
                Bitmap bitmap3 = ImageFactoryandOther.getSmallBitmap(split[2]);
                byte[] bytes1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                byte[] bytes2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                byte[] bytes3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                easyInquiry.setGoods_img1(bytes1);
                easyInquiry.setGoods_img2(bytes2);
                easyInquiry.setGoods_img3(bytes3);
                easyInquiry.setGoods_img4(bytes_no);
            } else if (i == 3) {
                String[] split = allPictureUrl.split("\\|");
                Bitmap bitmap1 = ImageFactoryandOther.getSmallBitmap(split[0]);
                Bitmap bitmap2 = ImageFactoryandOther.getSmallBitmap(split[1]);
                Bitmap bitmap3 = ImageFactoryandOther.getSmallBitmap(split[2]);
                Bitmap bitmap4 = ImageFactoryandOther.getSmallBitmap(split[3]);
                byte[] bytes1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                byte[] bytes2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                byte[] bytes3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                byte[] bytes4 = BitmapUtils.compressBmpFromBmp(bitmap4);
                easyInquiry.setGoods_img1(bytes1);
                easyInquiry.setGoods_img2(bytes2);
                easyInquiry.setGoods_img3(bytes3);
                easyInquiry.setGoods_img4(bytes4);
            }
        } else {
            easyInquiry.setGoods_img1(bytes_no);
            easyInquiry.setGoods_img2(bytes_no);
            easyInquiry.setGoods_img3(bytes_no);
            easyInquiry.setGoods_img4(bytes_no);
        }
        easyInquiry.setGoods_name(input_goods_name_et.getText().toString().trim());
        easyInquiry.setContent(content_et.getText().toString().trim());
        easyInquiry.setUniqued_id(uniqued_id);
        String s = UUID.randomUUID().toString();
        easyInquiry.setCode(s);
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(this);
        easyInquiryDao.insertData(easyInquiry);
    }

    //上传图片的PopupWindow弹出的方向
    private enum Location {
        BOTTOM
    }

    //上传图片弹出的popupWindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_upload, null);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_goods_activitythree, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(AddEasyInquiryGoodsActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        TextView photo = (TextView) popupWindowView.findViewById(R.id.photo_tv);
        TextView cancel = (TextView) popupWindowView.findViewById(R.id.cancel_tv);
        //选择照片
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapList.clear();
                selectGallery();
                mPopupWindow.dismiss();
            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    // 设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(AddEasyInquiryGoodsActivity.this, 1f);
        }
    }

    //拍照和选择相册的方法
    private void selectGallery() {
        Intent intent = new Intent(AddEasyInquiryGoodsActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片,显示
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量 4张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式,选取多张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                String tag = data.getStringExtra("Tag");
                if (mSelectPath.size() == 1 && tag.equals("2")) {
                    // 涂鸦参数
                    GraffitiActivity.GraffitiParams params = new GraffitiActivity.GraffitiParams();
                    // 图片路径
                    params.mImagePath = mSelectPath.get(0);

                    GraffitiActivity.startActivityForResult(AddEasyInquiryGoodsActivity.this, params, REQ_CODE_GRAFFITI);
                } else if (mSelectPath.size() >= 1 && tag.equals("1")) {
                    if (mSelectPath.size() > 4) {
                        mSelectPath.remove(0);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
                        //加入最后选择的集合
                        finalSelectPaths.add(p);
                        sb.append(p + "|");
                        String picstring = sb.toString();
                        if (!picstring.isEmpty()) {
                            allPictureUrl = picstring.substring(0, picstring.length() - 1);
                        }
                        //加载缩略图
                        Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(p, 200, 240);
                        bitmapList.add(bitmap);
                    }
                    int size = bitmapList.size();
                    for (int i = 0; i < size; i++) {
                        imageViewList.get(i).setImageBitmap(bitmapList.get(i));
                    }
                }

            }
        } else if (requestCode == REQ_CODE_GRAFFITI) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                if (mSelectPath == null || mSelectPath.size() == 0) {
                    return;
                } else if (mSelectPath.size() == 1) {
                    Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(path, 200, 240);
                    image1_iv.setImageBitmap(bitmap);
                    allPictureUrl = path;
                } else {
                    Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(path, 200, 240);
                    image1_iv.setImageBitmap(bitmap);
                    allPictureUrl = allPictureUrl + "|" + path;

                }
            } else if (resultCode == GraffitiActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
