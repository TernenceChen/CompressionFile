package org.ternence.compressionfile.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.ternence.compressionfile.R;


public class SunView extends View {

    Paint mPathPaint;
    private int mWidth;
    private int mHeight;
    int mainColor;
    int trackColor;
    private Path mPathPath;
    private Paint mMotionPaint;
    private Path mMotionPath;
    int controlX, controlY;
    float startX, startY;
    float endX, endY;
    private double rX;
    private double rY;
    private int[] mSunrise = new int[2];
    private int[] mSunset = new int[2];
    private Paint mSunPaint;
    private ValueAnimator valueAnimator;
    private float mProgress;
    private Paint mShadePaint;
    private Shader mPathShader;
    private float mCurrentProgress;
    private boolean isDraw = false;
    private DashPathEffect mDashPathEffect;
    private Paint mTextPaint;
    private LinearGradient mBackgroundShader;
    private int sunColor;
    private Paint mSunStrokePaint;
    private float svSunSize;
    private float svTextSize;
    private float textOffset;
    private float svPadding;
    private float svTrackWidth;

    public SunView(Context context) {
        super(context);
        init(null);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final Context context = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SunView);
        mainColor = array.getColor(R.styleable.SunView_svMainColor, 0x67B2FD);
        trackColor = array.getColor(R.styleable.SunView_svTrackColor, 0x67B2FD);
        sunColor = array.getColor(R.styleable.SunView_svSunColor, 0x00D3FE);
        svSunSize = array.getDimension(R.styleable.SunView_svSunRadius, 10);
        svTextSize = array.getDimension(R.styleable.SunView_svTextSize, 18);
        textOffset = array.getDimension(R.styleable.SunView_svTextOffset, 10);
        svPadding = array.getDimension(R.styleable.SunView_svPadding, 10);
        svTrackWidth = array.getDimension(R.styleable.SunView_svTrackWidth, 3);
        array.recycle();

        Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(mainColor);
        pathPaint.setStyle(Paint.Style.FILL);
        mPathPaint = pathPaint;
        mPathPath = new Path();

        Paint shadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadePaint.setColor(Color.parseColor("#B3FFFFFF"));
        shadePaint.setStyle(Paint.Style.FILL);
        mShadePaint = shadePaint;

        Paint motionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        motionPaint.setColor(trackColor);
        motionPaint.setStrokeCap(Paint.Cap.ROUND);
        motionPaint.setStrokeWidth(svTrackWidth);
        motionPaint.setStyle(Paint.Style.STROKE);
        mMotionPaint = motionPaint;
        mMotionPath = new Path();

        Paint sunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunPaint.setColor(sunColor);
        sunPaint.setStyle(Paint.Style.FILL);
        mSunPaint = sunPaint;

        Paint sunStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunStrokePaint.setColor(Color.WHITE);
        sunStrokePaint.setStyle(Paint.Style.FILL);
        mSunStrokePaint = sunStrokePaint;

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(mainColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(svTextSize);
        mTextPaint = textPaint;
        mDashPathEffect = new DashPathEffect(new float[]{6, 12}, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (!isDraw) {
            mWidth = getWidth();
            mHeight = getHeight();
            controlX = mWidth / 2;
            controlY = 0-mHeight/2;
            startX = svPadding;
            startY = mHeight-svPadding;
            endX = mWidth-svPadding;
            endY = mHeight-svPadding;
            rX = svPadding;
            rY = mHeight-svPadding;

            mPathShader = new LinearGradient(mWidth / 2, svPadding, mWidth / 2, endY,
                    mainColor, Color.WHITE, Shader.TileMode.CLAMP);
            mPathPaint.setShader(mPathShader);
            mPathPath.moveTo(startX, startY);
            mPathPath.quadTo(controlX, controlY, endX, endY);

            mMotionPath.moveTo(startX, startY);
            mMotionPath.quadTo(controlX, controlY, endX, endY);
            isDraw = true;
        }

        canvas.drawPath(mPathPath, mPathPaint);
        mMotionPaint.setStyle(Paint.Style.STROKE);
        mMotionPaint.setPathEffect(null);
        canvas.drawPath(mMotionPath, mMotionPaint);

        mShadePaint.setShader(mBackgroundShader);
        canvas.drawRect((float) rX, 0, mWidth, mHeight, mShadePaint);

        mMotionPaint.setPathEffect(mDashPathEffect);
        canvas.drawPath(mMotionPath, mMotionPaint);

        if (mSunrise.length != 0||mSunset.length != 0){
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("日出 "+(mSunrise[0]<10? "0"+mSunrise[0]: mSunrise[0])
                    +":"+(mSunrise[1]<10? "0"+mSunrise[1]: mSunrise[1]), startX+textOffset, startY, mTextPaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("日落 "+(mSunset[0]<10? "0"+mSunset[0]: mSunset[0])
                    +":"+(mSunset[1]<10? "0"+mSunset[1]: mSunset[1]), endX-textOffset, endY, mTextPaint);
        }

        canvas.drawCircle((float) rX, (float)rY, svSunSize*6/5, mSunStrokePaint);
        canvas.drawCircle((float) rX, (float)rY, svSunSize, mSunPaint);

        mMotionPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX, startY, svTrackWidth*2, mMotionPaint);
        canvas.drawCircle(endX, endY, svTrackWidth*2, mMotionPaint);

        canvas.restore();
    }

    private void setProgress(float t){
        mProgress = t;
        rX = startX * Math.pow(1 - t, 2) + 2 * controlX * t * (1 - t) + endX * Math.pow(t, 2);
        rY = startY * Math.pow(1 - t, 2) + 2 * controlY * t * (1 - t) + endY * Math.pow(t, 2);
        invalidate();
    }

    public void setCurrentTime(int hour, int minute){
        if (mSunrise.length != 0||mSunset.length != 0){
            float p0 = mSunrise[0]*60+mSunrise[1];// 起始分钟数
            float p1 = hour*60+minute-p0;// 当前时间总分钟数
            float p2 = mSunset[0]*60+mSunset[1]-p0;// 日落到日出总分钟数
            float progress = p1/p2;
            setProgress(progress);
            motionAnimation();
        }
    }

    public void setSunrise(int hour, int minute){
        mSunrise[0] = hour;
        mSunrise[1] = minute;
    }

    public void setSunset(int hour, int minute){
        mSunset[0] = hour;
        mSunset[1] = minute;
    }

    public void motionAnimation(){
        if (valueAnimator == null){
            mCurrentProgress = 0f;
            final ValueAnimator animator = ValueAnimator.ofFloat(mCurrentProgress, mProgress);
            animator.setDuration((long) (2500*(mProgress-mCurrentProgress)));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Object val = animator.getAnimatedValue();
                    if (val instanceof Float){
                        setProgress((Float) val);
                        if ((Float)val == mProgress){
                            // 保存当前的进度，下一次更新即可以从上次时间运动到当前时间
                            mCurrentProgress = mProgress;
                        }
                    }
                }
            });
            valueAnimator = animator;
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mCurrentProgress, mProgress);
        }
        valueAnimator.start();
    }

}
