package com.wxh.common4mvp.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wxh.common4mvp.R;
import com.wxh.common4mvp.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignatureView extends View {


    private Paint linePaint;// 画笔

    private ArrayList<Path> lines;// 写字的笔迹，支持多笔画
    private int lineCount;// 记录笔画数目

    private final int DEFAULT_LINE_WIDTH = 10;// 默认笔画宽度

    private int lineColor = Color.BLACK;// 默认字迹颜色（黑色）
    private float lineWidth = DEFAULT_LINE_WIDTH;// 笔画宽度

    public SignatureView(Context context) {
        super(context);
        initLinePaint();
        lines = new ArrayList<>();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.SignatureView);
            parseTyepdArray(tArray);
        }
        initLinePaint();
        lines = new ArrayList<>();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.SignatureView, defStyleAttr, 0);
            parseTyepdArray(tArray);
        }
        initLinePaint();
        lines = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lines != null && lines.size() > 0) {
            for (Path path : lines)
                canvas.drawPath(path, linePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 考虑到这个view会出现在lib工程里，因此使用if else
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下这个屏幕
            Path path = new Path();
            path.moveTo(event.getX(), event.getY());
            lines.add(path);
            lineCount = lines.size();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 在屏幕上移动
            lines.get(lineCount - 1).lineTo(event.getX(), event.getY());
            invalidate();
        } else {

        }
        return true;
    }

    /**
     * 解析类型数组
     *
     * @param tArray
     * @file Framework:com.flueky.android.view.SignView.java
     * @author flueky zuokefei0217@163.com
     * @time 2016年12月19日 下午5:15:25
     */
    private void parseTyepdArray(TypedArray tArray) {
        lineColor = tArray.getColor(R.styleable.SignatureView_sign_lineColor, Color.BLACK);
        lineWidth = tArray.getDimension(R.styleable.SignatureView_sign_lineWidth, DEFAULT_LINE_WIDTH);
    }

    private void initLinePaint() {
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setPathEffect(new CornerPathEffect(50));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);//防锯齿
        linePaint.setDither(true);//防抖动
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        linePaint.setColor(lineColor);
    }

    /**
     * @param lineWidth the lintWidth to set
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        linePaint.setStrokeWidth(lineWidth);
    }

    /**
     * 保存view视图的bitmap信息
     *
     * @return
     * @file Framework:com.flueky.android.view.SignView.java
     * @author flueky zuokefei0217@163.com
     * @time 2016年12月19日 下午7:00:01
     */
    public Bitmap getImage() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        /**
         * 绘制背景
         */
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        draw(canvas);// 绘制view视图的内容
        return bitmap;
    }

    /**
     * 将图像保存到文件
     *
     * @param filePath
     * @return 返回false表示保存失败
     * @file Framework:com.flueky.android.view.SignView.java
     * @author flueky zuokefei0217@163.com
     * @time 2016年12月19日 下午7:00:49
     */
    public boolean saveImageToFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtils.showToast("文件创建失败");
                return false;
            }
            FileOutputStream fos = new FileOutputStream(file);
            getImage().compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void clearPath() {
        lines.removeAll(lines);
        invalidate();
    }
}
