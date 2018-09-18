package com.wxh.common4mvp.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wxh on 2017/4/6.
 */
public class ImageUtils {
    public static final int REQUESTCODE_IMAGEZOOM = 100;

    /**
     * 把本地图片文件读入内存并进行像素压缩(已处理图片角度的旋转问题)
     *
     * @param imagePath 图片路径
     * @param w         图片最大宽度
     * @param h         图片最大高度
     * @return 压缩后的Bitmap图像文件，用于app展示等
     */
    public static Bitmap compressImageFromFile(String imagePath, float w, float h) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;//只读边，不读内容
        BitmapFactory.decodeFile(imagePath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;

        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (width > height && width > w) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / w);
        } else if (width < height && height > h) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / h);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例(采样率)
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, newOpts);

        // 判断图片角度，如果需要旋转图片
        int degree = readPictureDegree(imagePath);
        if (degree != 0) {
            bitmap = rotateBitmap(bitmap, degree);
        }

        return bitmap;
    }

    /**
     * 把在内存的Bitmap图像文件进行质量压缩并保存到本地
     *
     * @param bmp       待压缩的Bitmap图像文件
     * @param imagePath 图片路径
     * @param options   压缩百分比
     * @return 压缩后的File本地图片文件，用于上传服务器等
     */
    public static File compressImageToFile(Bitmap bmp, String imagePath, int options) {
        File file = new File(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                baos.reset();//重置baos即清空baos
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩50%，把压缩后的数据存放到baos中
            }
//            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里初始100表示不压缩，把压缩后的数据存放到baos中
//            if (size > 0) {
//                while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                    LogUtils.i("baos.toByteArray().length:" + baos.toByteArray().length / 1024);
//                    baos.reset();// 重置baos即清空baos
//                    options = options / 2;// 每次都减少一半
//                    bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//                }
//            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                file = null;
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }

        return file;
    }


    //图片按比例大小压缩方法（根据Bitmap图片压缩）
    public static Bitmap compressImageFromBmp(Bitmap image, float ww, float hh) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return bitmap;
    }


    /**
     * 获取图片的角度
     *
     * @param imagePath 图片路径
     * @return
     */
    public static int readPictureDegree(String imagePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param bitmap  内存中的图片
     * @param degress 待旋转角度
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 把bitmap转换成String
     *
     * @param imagePath 图片路径
     * @return 转化后对应的字符串
     */
    public static String bitmapToString(String imagePath) {

        Bitmap bm = compressImageFromFile(imagePath, 480, 800);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 调用系统图片裁剪功能
     *
     * @param app      当前活动窗口
     * @param inputUri 源图片uri
     * @param x        横向裁剪框比例
     * @param y        纵向裁剪框比例
     */
    public static void startImageZoom(Activity app, Uri inputUri, int x, int y) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        // crop=true 有这句才能出来最后的裁剪页面
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", x);
        intent.putExtra("aspectY", y);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", x);
        intent.putExtra("outputY", y);
        // 返回图片格式
        intent.putExtra("outputFormat", "JPEG");
        // 返回data形式
        intent.putExtra("return-data", true);
        app.startActivityForResult(intent, REQUESTCODE_IMAGEZOOM);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap transImage(String fromFile, int width) {
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        int degree = readPictureDegree(fromFile);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        LogUtils.v("bitmap before:" + "x:" + bitmapWidth + " y:" + bitmapHeight);
        // 缩放图片的尺寸
        float scaleWidth = (float) width / bitmapWidth;
        float scaleHeight = scaleWidth;
        if (degree == 90 || degree == 270) {
            scaleWidth = (float) width / bitmapHeight;
            scaleHeight = scaleWidth;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(degree);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);

        int resizeBitmapWidth = resizeBitmap.getWidth();
        int resizeBitmapHeight = resizeBitmap.getHeight();
        LogUtils.v("bitmap affter:" + "x:" + resizeBitmapWidth + " y:" + resizeBitmapHeight);
//            // save file
//            File myCaptureFile = new File(toFile);
//            FileOutputStream out = new FileOutputStream(myCaptureFile);
//            if(resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)){
//                out.flush();
//                out.close();
//            }
//        if(!bitmap.isRecycled()){
//            bitmap.recycle();//记得释放资源，否则会内存溢出
//        }
//        if(!resizeBitmap.isRecycled()){
//            resizeBitmap.recycle();
//        }

        return resizeBitmap;

    }

    public static Bitmap transImage(Bitmap srcBitmap, int width) {
        Bitmap resizeBitmap = null;
        if (srcBitmap != null) {
            int bitmapWidth = srcBitmap.getWidth();
            int bitmapHeight = srcBitmap.getHeight();
            LogUtils.v("bitmap before:" + "x:" + bitmapWidth + " y:" + bitmapHeight);
            // 缩放图片的尺寸
            float scaleWidth = (float) width / bitmapWidth;
            float scaleHeight = scaleWidth;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 产生缩放后的Bitmap对象
            resizeBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);

            int resizeBitmapWidth = resizeBitmap.getWidth();
            int resizeBitmapHeight = resizeBitmap.getHeight();
            LogUtils.v("bitmap affter:" + "x:" + resizeBitmapWidth + " y:" + resizeBitmapHeight);
        }

//            // save file
//            File myCaptureFile = new File(toFile);
//            FileOutputStream out = new FileOutputStream(myCaptureFile);
//            if(resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)){
//                out.flush();
//                out.close();
//            }
//        if(!bitmap.isRecycled()){
//            bitmap.recycle();//记得释放资源，否则会内存溢出
//        }
//        if(!resizeBitmap.isRecycled()){
//            resizeBitmap.recycle();
//        }

        return resizeBitmap;

    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;


    }
}
