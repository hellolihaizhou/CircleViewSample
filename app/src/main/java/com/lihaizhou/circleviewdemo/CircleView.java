package com.lihaizhou.circleviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public class CircleView extends View{
    private float mViewWidth;
    private float mViewHeight;
    private Paint bigCirclePaint;
    private Paint smallCirclePaint;
    private float currentValue = 0;
    private float[] pos;
    private float[] tan;
    private Bitmap mBall;//小球
    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        pos = new float[2];
        tan = new float[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mBall = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ball);

        bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(Color.RED);
        bigCirclePaint.setStyle(Paint.Style.STROKE);

        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(Color.RED);
        smallCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        invalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth/2,mViewHeight/2);
        Path path = new Path();
        path.addCircle(0,0,200, Path.Direction.CW);//这里的CW代表外边的大圆路径是顺时针，这样小圆会按照顺时针转动，若改为CCW则为逆时针
        PathMeasure pathMeasure = new PathMeasure(path,false);
        currentValue+=0.005;
        if(currentValue < 1)
        {
            if (currentValue>0.6&&currentValue<0.9) //在顶部时，绘制的细一些，效果上看像是放慢了速度
            {
                currentValue+=0.0001;
            }
            else
            {
                currentValue+=0.01;
            }
        }
        else
        {

            currentValue = 0;
        }
        pathMeasure.getPosTan(pathMeasure.getLength()*currentValue,pos,tan);// 获取当前位置的坐标以及趋势,pos,tan务必初始化
        //canvas.drawCircle(pos[0],pos[1],20,smallCirclePaint); //这里是用画笔绘制，下面一行用的是图片
        canvas.drawBitmap(mBall,pos[0] - mBall.getWidth()/2,pos[1]- mBall.getHeight() / 2,smallCirclePaint);// 将图片绘制中心调整到与当前点重合
        canvas.drawPath(path,bigCirclePaint);
        invalidate();//invalidate的执行会触发onDraw,放在这里的目的是循环绘制
    }
}
