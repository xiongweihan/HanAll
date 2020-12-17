package com.example.hanall.widget.leaf;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.hanall.R;
import com.example.hanall.utils.ScreenUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 动画loadingView
 */
public class LeafLoadingView extends View {

    private static final String TAG = "LeafLoadingView";
    //淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    //橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    //中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    //不同振幅之间的振幅差
    private static final int AMPLITUDE_DISPARITY = 5;

    //总进度
    private static final int TOTAL_PROGRESS = 100;
    //叶子飘动一个周期所花的 时间
    private static final long LEAF_FLOAT_TIME = 3000;
    //叶子旋转一周需要的时间
    private static final long LEAF_ROTATE_TIME = 2000;

    //用于控制绘制的进度条距离左/上/下 的距离
    private static final int LEFT_MARGIN = 9;
    //用于控制回值的进度条距离右的距离
    private static final int RIGHT_MARGIN = 25;

    private int mLeftMargin, mRightMargin;
    //中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    //振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    //叶子飘动一个周期所花费的时间
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    //叶子旋转一周需要的时间
    private long mLeafRotateTime = LEAF_ROTATE_TIME;

    private Resources mResources;
    private Bitmap mLeafBitmap;
    private int mLeafWidth, mLeafHeight;

    private Bitmap mOuterBitmap;
    private Rect mOuterSrcRect, mOuterDestRect;
    private int mOuterWidth, mOuterHeight;

    private int mTotalWidth, mTotalHeight;

    private Paint mBitmapPaint, mWhitepaint, mOrangePaint;
    private RectF mWhiteRectF, mOrangeRectF, mArcRectF;

    //当前进度
    private int mProgress;
    //所绘制进度条部分的宽度
    private int mProgressWidth;
    //当前所在的绘制的进度条的位置
    private int mCurrentProgressPosition;
    //弧形的半径
    private int mArcRadius;

    //arc 右上角的X坐标，也是矩形X坐标的起始点
    private int mArcRightLocation;
    //用于产生叶子的信息
    private LeafFactory mLeafFactory;
    //产生出的叶子信息
    private List<Leaf> mLeafUInfos;
    //用于控制随机增加的时间不抱团
    private int mAddTime;


    public LeafLoadingView(Context context) {
        this(context, null);
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mResources = getResources();
        mLeftMargin = ScreenUtil.dip2px(context, LEFT_MARGIN);
        mRightMargin = ScreenUtil.dip2px(context, RIGHT_MARGIN);

        mLeafFloatTime = LEAF_FLOAT_TIME;
        mLeafRotateTime = LEAF_ROTATE_TIME;

        initBitmap();
        initPaint();
        mLeafFactory = new LeafFactory();
        mLeafUInfos = mLeafFactory.generateLeafs();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);

        mWhitepaint = new Paint();
        mWhitepaint.setAntiAlias(true);
        mWhitepaint.setColor(WHITE_COLOR);

        mOrangePaint = new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);
    }

    private void initBitmap() {
        mLeafBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf)).getBitmap();
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        mOuterBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf_kuang)).getBitmap();
        mOuterWidth = mOuterBitmap.getWidth();
        mOuterHeight = mOuterBitmap.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin;
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2;

        mOuterSrcRect = new Rect(0, 0, mOuterWidth, mOuterHeight);
        mOuterDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);

        mWhiteRectF = new RectF(mLeftMargin + mCurrentProgressPosition, mLeftMargin,
                mTotalWidth - mRightMargin, mTotalHeight - mLeftMargin);

        mOrangeRectF = new RectF(mLeftMargin + mArcRadius, mLeftMargin,
                mCurrentProgressPosition, mTotalHeight - mLeftMargin);

        mArcRectF = new RectF(mLeftMargin, mLeftMargin, mLeftMargin + 2 * mArcRadius,
                mTotalHeight - mLeftMargin);

        mArcRightLocation = mLeftMargin + mArcRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度条和叶子
        //之所以把叶子放在进度条里绘制，主要是层级原因
        drawableProgressAndleaf(canvas);
        canvas.drawBitmap(mOuterBitmap, mOuterSrcRect, mOuterDestRect, mBitmapPaint);

        postInvalidate();
    }

    private void drawableProgressAndleaf(Canvas canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0;
        }
        //mProgressposition 为进度条的宽度，根据当前进度计算出进度条的位置
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS;

        //即当前位置在途中所示1范围内
        if (mCurrentProgressPosition < mArcRadius) {
            Log.e(TAG, "mProgress==" + mProgress + "//--mcurrentProgressPosition ==" + mCurrentProgressPosition
                    + "//mArcRadius == " + mArcRadius);

            //1绘制白色arc
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitepaint);

            //2,绘制白色矩形
            mWhiteRectF.left = mArcRightLocation;
            canvas.drawRect(mWhiteRectF, mWhitepaint);

            //3绘制叶子
            drawLeafs(canvas);

            //3,绘制棕色Arc
            //单边角度
            int angle = (int) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition)
                    / (float) mArcRadius));

            //起始位置
            int startAngle = 180 - angle;
            //扫过的角度
            int sweepAngle = 2 * angle;
            Log.e(TAG, "startAngle==" + startAngle);
            canvas.drawArc(mArcRectF, startAngle, sweepAngle, false, mOrangePaint);

        } else {
            Log.e(TAG, "mProgress==" + mProgress + "//--mcurrentProgressPosition ==" + mCurrentProgressPosition
                    + "//mArcRadius == " + mArcRadius);

            //步骤：1绘制white Rect ,2 绘制 Orange Arc ,3绘制orange RECT
            //让这个层级进行绘制能让叶子刚觉融入了棕色进度条中

            //1,绘制white RECT
            mWhiteRectF.left = mCurrentProgressPosition;
            canvas.drawRect(mWhiteRectF, mWhitepaint);
            //绘制叶子
            drawLeafs(canvas);
            //2绘制Orange ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);
            //绘制orange Rect
            mOrangeRectF.left = mArcRightLocation;
            mOrangeRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangeRectF, mOrangePaint);
        }

    }

    /**
     * 绘制叶子
     *
     * @param canvas
     */
    private void drawLeafs(Canvas canvas) {
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < mLeafUInfos.size(); i++) {
            Leaf leaf = mLeafUInfos.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                //绘制叶子--根据叶子的类型和当前时间得出叶子的(X,Y)
                getLeafLocation(leaf, currentTime);
                //根据时间计算旋转角度
                canvas.save();
                //通过Matrix 控制叶子的旋转
                Matrix matrix = new Matrix();
                float transX = mLeftMargin + leaf.x;
                float transY = mLeftMargin + leaf.y;
                Log.e(TAG, "leaf.x == " + leaf.x + "//leaf.y ==" + leaf.y);
                matrix.postTranslate(transX, transY);

                //通过时间关联旋转角度，则可以直接通过修正LEAF_RATOTE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime)
                        / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);
                //根据叶子旋转方向确认叶子旋转角度
                int rotate = leaf.rotateAngle == 0 ? angle + leaf.rotateAngle : -angle + leaf.rotateAngle;

                matrix.postRotate(rotate, transX + mLeafWidth / 2, transY + mLeafHeight / 2);

                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();
            } else {
                continue;
            }
        }


    }

    private void getLeafLocation(Leaf leaf, long currentTime) {

        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }

        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (mProgressWidth - mProgressWidth * fraction);
        leaf.y = getLocationY(leaf);
    }

    /**
     * 通过叶子信息获取当前叶子的Y值
     */
    private float getLocationY(Leaf leaf) {
        float w = (float) ((float) 2 / Math.PI / mProgressWidth);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                //小振幅 == 中等振幅 - 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;

            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                //小振幅 = 中等振幅 + 振幅差；
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        Log.e(TAG, "getLocationY: a== " + a + "//www===" + w + "//leaf.x" + leaf.x);

        return (int) (a * Math.sin(w * leaf.x)) + mArcRadius * 2 / 3;
    }

    private enum StartType {
        LITTLE, MIDDLE, BIG
    }

    /**
     * 叶子对象，用来记录叶子的主要数据
     */
    private class Leaf {
        //在绘制部分的位置
        float x, y;
        //控制叶子飘动的幅度
        StartType type;
        //旋转角度
        int rotateAngle;
        //旋转方向--0 代表顺时针，100代表逆时针
        int rotateDirection;
        //起始时间（ms）
        long startTime;
    }

    private class LeafFactory {
        private static final int MAX_LEAFS = 8;
        Random random = new Random();

        //生成一个叶子信息
        public Leaf generateLeaf() {

            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            //随时类型一 随机振幅
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
                default:
                    break;
            }
            leaf.type = type;
            //随机起始的旋转角度
            leaf.rotateAngle = random.nextInt(360);
            //随机旋转方向(顺时针或逆时针)
            leaf.rotateDirection = random.nextInt(2);
            //为了产生交错的感觉，让开始的时间有一定的随机性
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            mAddTime += random.nextInt((int) (mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }

        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        private List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
    }

    /**
     * 设置中等振幅
     */
    public void setMiddleAmplitude(int amplitude) {
        this.mMiddleAmplitude = amplitude;
    }

    /**
     * 设置振幅差
     */
    public void setAmplitudeDisparity(int disparity) {
        this.mAmplitudeDisparity = disparity;
    }

    /**
     * 获取中等振幅
     */
    public int getMiddleAmplitude() {
        return mMiddleAmplitude;
    }

    /**
     * 获取振幅差
     */
    public int getAmplitudeDisparity() {
        return mAmplitudeDisparity;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setmProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置叶子旋转一周所花的时间
     */
    public void setLeafFloatTime(long time) {
        this.mLeafFloatTime = time;
    }

    public void setLeafRotateTime(long time){
        this.mLeafRotateTime = time;
    }

    /**
     * 获取叶子飘完一个周期所花的时间
     */
    public long getLeafFloatTime() {
        mLeafFloatTime = mLeafFloatTime == 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        return mLeafFloatTime;
    }

    /**
     * 获取叶子旋转一周所花的时间
     *
     * @return
     */
    public long getLeafRotateTime() {
        mLeafRotateTime = mLeafRotateTime == 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        return mLeafRotateTime;
    }
}
