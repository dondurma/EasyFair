package com.baibeiyun.eazyfair.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    //将一个bitmap转化为byte[]
    public static byte[] compressBmpFromBmp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//option表示压缩至x% 如果是100就表示不压缩
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100 && options > 10) {//防止压缩的options小于0
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        return baos.toByteArray();
    }

    //将一个bitmap转化为byte[] 无压缩
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //根据图片路径得到图片的bitmap对象
    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        Bitmap bitmap;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率
        newOpts.inPreferredConfig = Config.RGB_565;//该模式是默认的,可不设
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    //将byte[] 转换为bitmap
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            } catch (OutOfMemoryError e) {

            }
            return bitmap;
        } else {
            return null;
        }
    }

    //按照像素大小来缩放图片
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
//        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        Bitmap bitmap;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > pixelW) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / pixelW);
        } else if (w < h && h > pixelH) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / pixelH);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
        return bitmap;
    }


    /**
     * @param path    原图路径
     * @param offsetX 截取开始点在X轴偏移量
     * @param offsetY 截取开始点在Y轴偏移量
     * @param targetW 截取多宽（像素）
     * @param targetH 截取多高（像素）
     * @return
     */
    public static Bitmap matrixScale(String path, int offsetX, int offsetY, int targetW, int targetH) {
        // 构建原始位图
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        // 获取原始宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算宽高缩放比例，targetW，targetH即期待缩放完成后位图的宽高
        float scaleW = (float) targetW / width;
        float scaleH = (float) targetH / height;
        // 将缩放比例放进矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);
        bitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, width, height, matrix, false);
        return bitmap;
    }


    /*
    * @param path 图片路径
    * @param quality 质量 0-100,100表示原图
    * @return
    */
    public static Bitmap losslessScale(String path, int quality) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                baos.toByteArray(), 0, baos.toByteArray().length);
        return compressedBitmap;
    }


}
