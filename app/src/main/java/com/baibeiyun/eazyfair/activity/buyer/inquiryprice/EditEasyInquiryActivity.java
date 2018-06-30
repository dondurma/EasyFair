package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.baibeiyun.eazyfair.utils.OtherUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.hzw.graffiti.GraffitiActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class EditEasyInquiryActivity extends BaseActivityforBuyer implements View.OnClickListener {

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private RelativeLayout fanhui_rl;
    private Button upload_image_iv;
    private EditText input_goods_name_et;
    private ImageView image1_iv, image2_iv, image3_iv, image4_iv;
    private EditText content_et;
    private Button confirm_bt;
    private int id;//当前商品的id

    private List<ImageView> imageViewList = new ArrayList<>();//存放显示图片的四个ImageView
    private final int REQUEST_IMAGE = 2;
    private final int maxNum = 4;//最多显示四张图片
    private ArrayList<String> mSelectPath;//所选的图片的地址
    private List<Bitmap> bitmapList = new ArrayList<>();//存放所选的图片
    private List<String> finalSelectPaths = new ArrayList<>();//存放最终所选择的图片的地址
    private String allPictureUrl;//所有图片的URL拼接


    public static final int REQ_CODE_GRAFFITI = 101;
    public static final int REQ_CODE_GRAFFITI_EDIT_ONE = 102;
    public static final int REQ_CODE_GRAFFITI_EDIT_TWO = 103;
    public static final int REQ_CODE_GRAFFITI_EDIT_THREE = 104;
    public static final int REQ_CODE_GRAFFITI_EDIT_FOUR = 105;

    private int from = 0;
    private PopupWindow popupWindow;
    private EasyInquiry easyInquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_easy_inquiry);
        initYuyan();
        initView();
        initData();

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(EditEasyInquiryActivity.this);
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
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_goods_name_et = (EditText) findViewById(R.id.input_goods_name_et);
        upload_image_iv = (Button) findViewById(R.id.upload_image_iv);
        image1_iv = (ImageView) findViewById(R.id.image1_iv);
        image2_iv = (ImageView) findViewById(R.id.image2_iv);
        image3_iv = (ImageView) findViewById(R.id.image3_iv);
        image4_iv = (ImageView) findViewById(R.id.image4_iv);
        content_et = (EditText) findViewById(R.id.content_et);
        confirm_bt = (Button) findViewById(R.id.confirm_bt);
        imageViewList.add(image1_iv);
        imageViewList.add(image2_iv);
        imageViewList.add(image3_iv);
        imageViewList.add(image4_iv);

        fanhui_rl.setOnClickListener(this);
        upload_image_iv.setOnClickListener(this);
        image1_iv.setOnClickListener(this);
        image2_iv.setOnClickListener(this);
        image3_iv.setOnClickListener(this);
        image4_iv.setOnClickListener(this);
        confirm_bt.setOnClickListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        easyInquiry = CursorUtils.selectEasyInquiryById(this, id);
        byte[] product_imgs1 = easyInquiry.getGoods_img1();
        byte[] product_imgs2 = easyInquiry.getGoods_img2();
        byte[] product_imgs3 = easyInquiry.getGoods_img3();
        byte[] product_imgs4 = easyInquiry.getGoods_img4();
        Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        image1_iv.setImageBitmap(bitmap1);
        image2_iv.setImageBitmap(bitmap2);
        image3_iv.setImageBitmap(bitmap3);
        image4_iv.setImageBitmap(bitmap4);
        imageViewList.add(image1_iv);
        imageViewList.add(image2_iv);
        imageViewList.add(image3_iv);
        imageViewList.add(image4_iv);
        String content = easyInquiry.getContent();
        content_et.setText(content);
        input_goods_name_et.setText(easyInquiry.getGoods_name());

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
            case R.id.image1_iv:
                byte[] product_imgs1 = easyInquiry.getGoods_img1();
                Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
                String path1 = OtherUtils.saveBitmap(this, bitmap1);
                GraffitiActivity.GraffitiParams params1 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params1.mImagePath = path1;
                GraffitiActivity.startActivityForResult(EditEasyInquiryActivity.this, params1, REQ_CODE_GRAFFITI_EDIT_ONE);
                break;
            case R.id.image2_iv:
                byte[] product_imgs2 = easyInquiry.getGoods_img2();
                Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
                String path2 = OtherUtils.saveBitmap(this, bitmap2);
                GraffitiActivity.GraffitiParams params2 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params2.mImagePath = path2;
                GraffitiActivity.startActivityForResult(EditEasyInquiryActivity.this, params2, REQ_CODE_GRAFFITI_EDIT_TWO);

                break;
            case R.id.image3_iv:
                byte[] product_imgs3 = easyInquiry.getGoods_img3();
                Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
                String path3 = OtherUtils.saveBitmap(this, bitmap3);
                GraffitiActivity.GraffitiParams params3 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params3.mImagePath = path3;
                GraffitiActivity.startActivityForResult(EditEasyInquiryActivity.this, params3, REQ_CODE_GRAFFITI_EDIT_THREE);
                break;
            case R.id.image4_iv:
                byte[] product_imgs4 = easyInquiry.getGoods_img4();
                Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
                String path4 = OtherUtils.saveBitmap(this, bitmap4);
                GraffitiActivity.GraffitiParams params4 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params4.mImagePath = path4;
                GraffitiActivity.startActivityForResult(EditEasyInquiryActivity.this, params4, REQ_CODE_GRAFFITI_EDIT_FOUR);
                break;
            //保存
            case R.id.confirm_bt:
                updata();
                break;
        }
    }


    // 设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //上传图片的PopupWindow弹出的方向
    private enum Location {
        BOTTOM
    }

    //上传图片弹出的popupWindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_upload, null);
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow = new PopupWindow(popupWindowView, android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            popupWindow = new PopupWindow(popupWindowView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_goods_activitythree, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(EditEasyInquiryActivity.this, 0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });
        TextView photo = (TextView) popupWindowView.findViewById(R.id.photo_tv);
        TextView cancel = (TextView) popupWindowView.findViewById(R.id.cancel_tv);
        //相册
        photo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bitmapList.clear();
                        selectGallery();
                        popupWindow.dismiss();
                    }
                });

        //取消
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(EditEasyInquiryActivity.this, 1f);
        }
    }

    //拍照和选择相册的方法
    private void selectGallery() {
        Intent intent = new Intent(EditEasyInquiryActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片,显示
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量 30张
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

                    GraffitiActivity.startActivityForResult(EditEasyInquiryActivity.this, params, REQ_CODE_GRAFFITI);
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
            //点击第一张图片
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_ONE) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);

                if (allPictureUrl == null) {
                    allPictureUrl = path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //一张图片
                    if (i == 0) {
                        allPictureUrl = path;
                        //两张图片
                    } else if (i == 1) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = path + "|" + split[1];
                        //三张图片
                    } else if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = path + "|" + split[1] + "|" + split[2];
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = path + "|" + split[1] + "|" + split[2] + "|" + split[3];
                    }
                }

                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image1_iv.setImageBitmap(bm);
            }
            //点击第二张图片
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_TWO) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (allPictureUrl == null) {
                    allPictureUrl = "|" + path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //一张图片
                    if (i == 0) {
                        allPictureUrl = allPictureUrl + "|" + path;
                        //两张图片
                    } else if (i == 1) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + path;
                        //三张图片
                    } else if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + path + "|" + split[2];
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + path + "|" + split[2] + "|" + split[3];
                    }
                }

                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image2_iv.setImageBitmap(bm);
            }
            //三张图片
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_THREE) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);

                if (allPictureUrl == null) {
                    allPictureUrl = "|" + "|" + path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //两张图片
                    if (i == 1) {
                        allPictureUrl = allPictureUrl + "|" + path;
                        //三张图片
                    } else if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + path;
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + path + "|" + split[3];
                    }
                }

                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image3_iv.setImageBitmap(bm);
            }
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_FOUR) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (allPictureUrl == null) {
                    allPictureUrl = "|" + "|" + "|" + path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //三张图片
                    if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + split[2] + path;
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + split[2] + "|" + path;
                    }
                }

                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image4_iv.setImageBitmap(bm);
            }
        }
    }


    private void updata() {
        //获得修改后的数据
        String content = content_et.getText().toString().trim();
        String goods_name = input_goods_name_et.getText().toString().trim();
        byte[] product_imgs1 = new byte[0];
        byte[] product_imgs2 = new byte[0];
        byte[] product_imgs3 = new byte[0];
        byte[] product_imgs4 = new byte[0];

        if (allPictureUrl == null) {
            product_imgs1 = easyInquiry.getGoods_img1();
            product_imgs2 = easyInquiry.getGoods_img2();
            product_imgs3 = easyInquiry.getGoods_img3();
            product_imgs4 = easyInquiry.getGoods_img4();
        } else {
            // 2016/12/7 计算出有几个“|”线
            int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
            //一张图片
            if (i == 0) {
                //将路径转换为bitmap
                Bitmap bitmap = BitmapUtils.compressImageFromFile(allPictureUrl);
                //将bitmap转换为二进制流
                product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap);
                product_imgs2 = easyInquiry.getGoods_img2();
                product_imgs3 = easyInquiry.getGoods_img3();
                product_imgs4 = easyInquiry.getGoods_img4();
                //两张图片
            } else if (i == 1) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = easyInquiry.getGoods_img1();
                }
                Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                product_imgs3 = easyInquiry.getGoods_img3();
                product_imgs4 = easyInquiry.getGoods_img4();

            } else if (i == 2) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = easyInquiry.getGoods_img1();
                }
                if (!split[1].equals("")) {
                    Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                    product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                } else {
                    product_imgs2 = easyInquiry.getGoods_img2();
                }
                Bitmap bitmap3 = BitmapUtils.compressImageFromFile(split[2]);
                product_imgs3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                product_imgs4 = easyInquiry.getGoods_img4();
            } else if (i == 3) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = easyInquiry.getGoods_img1();
                }
                if (!split[1].equals("")) {
                    Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                    product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                } else {
                    product_imgs2 = easyInquiry.getGoods_img2();
                }
                if (!split[2].equals("")) {
                    Bitmap bitmap3 = BitmapUtils.compressImageFromFile(split[2]);
                    product_imgs3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                } else {
                    product_imgs3 = easyInquiry.getGoods_img3();
                }
                Bitmap bitmap4 = BitmapUtils.compressImageFromFile(split[3]);
                product_imgs4 = BitmapUtils.compressBmpFromBmp(bitmap4);
            }
        }
        String uniqued_id = easyInquiry.getUniqued_id();
        easyInquiry = new EasyInquiry(id, product_imgs1, product_imgs2, product_imgs3, product_imgs4, goods_name, content, uniqued_id);
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(EditEasyInquiryActivity.this);
        int row = easyInquiryDao.upData(easyInquiry);
        if (row == 0) {
            Toast.makeText(EditEasyInquiryActivity.this, R.string.modify_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditEasyInquiryActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
