package huawei.com.yuanview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *
 */
@SuppressLint("AppCompatCustomView")
public class RoundProgressView extends ImageView {

    private  int circleWidth = 500; // 默认圆的直径
    private Path path;  // 绘制圆的路径
    private RectF rectF;  // 圆角矩形
    private RectF oval;  //圆的外界矩形
    float[] foo = {14,14,14,14,14,14,14,14}; // 绘制圆角矩形的4个角的弧度
    private Paint paint; // 自定义圆的画笔
    private Paint paint2; // 自定义圆的画笔
    private PathDashPathEffect pathDashPathEffect; //虚线路径
    private boolean finalAnim = false;  // 是否开启最终的动画效果

    // 自定义圆的默认颜色
    private int[] DEFAULT_ROUND_COLOR = {Color.parseColor("#cccccc"),Color.parseColor("#cccccc")};

    // 最终动画的颜色设置
    private int[] ANIM_ROUND_COLOR_FINALLY = {Color.parseColor("#407EFC"),Color.parseColor("#CE7FF1"),Color.parseColor("#3C7EFC")};
    private float[] ROUND_STAR_ARG_FINALLY = {0.0f,0.6f,1}; //改变动画在圆上动态的范围270-360

    private int[] ANIM_ROUND_COLOR = {};  //在圆内分布的颜色  与下面的成员变量必须对应
    private float[] ROUND_STAR_ARG = {}; //改变动画在圆上动态的范围

    private float animStartDeg = 0;  //动画开启的起始角度
    private float endDegree = 0; //设置圈的最终角度
    private boolean startAnim = false; // 是否开启动画
    private Handler handler = new Handler();
    private Context mContext;
    private SweepGradient sweepGradient; // 自定义圆的设置
    private SweepGradient sweepGradient1; // 动画的渐变设置
    private String text;  //圈内文本显示
    private int timeInterval = 0;  // 每一帧对应的时间间隔
    private Matrix matrix;  // 默认圆矩阵
    private Matrix matrix1; // 动画矩阵
    private Paint tvPaint;
    private int raduio = 180;
    public RoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        circleWidth = dip2px(mContext,200);
        tvPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//绘制中间文字Paint
        initCircleView();
    }

    /**
     * 初始化 圆
     */
    private void initCircleView(){

        rectF = new RectF(0,0,dip2px(mContext,2),dip2px(mContext,13));  //绘制单元细条矩形
        oval = new RectF(circleWidth/2- raduio,circleWidth/2 - raduio,circleWidth/2+raduio,circleWidth/2+raduio);  // 圆的外接矩形
        paint = new Paint(); // 默认圆的画笔
        paint2 = new Paint(); // 默认圆的画笔
        path = new Path();  // 单元细条路径
        path.addRoundRect(rectF,foo,Path.Direction.CW);
        pathDashPathEffect = new PathDashPathEffect(path, dip2px(mContext,6),dip2px(mContext,0),PathDashPathEffect.Style.ROTATE); //虚线
        matrix = new Matrix();
        sweepGradient = new SweepGradient(circleWidth / 2,circleWidth / 2, DEFAULT_ROUND_COLOR, null);//界面出现首次绘制圆的颜色，默认无渐变色
    }

    /**
     * 动画所需的参数
     */
    private void drawAnimView(){
        sweepGradient1 = new SweepGradient(circleWidth / 2,circleWidth / 2, ANIM_ROUND_COLOR,ROUND_STAR_ARG);
        matrix1 = new Matrix();
        matrix1.setRotate(-89+animStartDeg,circleWidth / 2,circleWidth / 2);
        sweepGradient1.setLocalMatrix(matrix1);
        paint.setShader(sweepGradient1);
    }


    /**
     * 动画所需的参数
     */
    private void drawfinallyAnimView(){
        paint2.setARGB(255,100,100,100);
        paint2.setShader(null);
        paint2.setStrokeWidth(dip2px(mContext,13));
        paint2.setAntiAlias(true);
        paint2.setPathEffect(pathDashPathEffect);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setShader(sweepGradient1);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //设置中间文字
        tvPaint.setStrokeWidth(2);
        tvPaint.setTextSize(100);
        tvPaint.setColor(Color.RED);
        text = (int)(animStartDeg / 3.6) + "";
        canvas.drawText(text + "%", circleWidth / 2-dip2px(mContext,25), circleWidth / 2+dip2px(mContext,10), tvPaint);
        // 设置圆的画笔
        paint.setARGB(255,100,100,100);
        paint.setShader(null);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(pathDashPathEffect);



        //默认圆的效果
        matrix.setRotate(-89,circleWidth / 2,circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
        paint.setShader(sweepGradient);
        canvas.drawArc(oval,-89,360,false,paint);

        if (startAnim){
            drawAnimView();
            if (finalAnim){
                drawfinallyAnimView();
                canvas.drawArc(oval,-89,animStartDeg,false,paint2);
            }else{
                canvas.drawArc(oval,-89,360,false,paint);
            }
        }
        super.onDraw(canvas);
    }


    /**
     * @param ints 外界传入加载动画的色值 eg:{Color.parseColor("#cccccc"),Color.parseColor("#cccccc"),Color.parseColor("#417EFC")}
     * @param floats 外界传入对应色值的角度 eg:{0,0.75F,1.0F}
     */
    public void setRoundColor(int[] ints,float[] floats){
        ANIM_ROUND_COLOR = ints.clone();
        ROUND_STAR_ARG = floats.clone();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int speSize = MeasureSpec.getSize(heightMeasureSpec);
        int speMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), speSize);
    }


    /**
     * 是否开启加载动画
     */
    public void startAnimation(){
        startAnim = true;
        handler.postDelayed(run,100);
    }


    Runnable run = new Runnable() {
        @Override
        public void run() {
            animStartDeg+=3;
            if (animStartDeg>360){
                animStartDeg = 0;
            }
            handler.postDelayed(run,16);
            invalidate();
        }
    };


    /**
     * 停止加载动画
     */
    public void stopAnimation(){
        startAnim = false;
        animStartDeg = 0;
        handler.removeCallbacks(run);
        invalidate();
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * @param degree 外部传入最终动画的角度
     */
    public void updataDegree(float degree){
        if (degree>360){
            endDegree = 360;
        }else{
            this.endDegree = degree;
        }
    }

    private LoadingAnimRunable runable = new LoadingAnimRunable();

    /**
     * @param time  延时多少时间开始
     * @param interval 每一帧对应的时间间隔
     */
    public void startAnimFromDegree(long time ,int interval){
        setRoundColor(ANIM_ROUND_COLOR_FINALLY,ROUND_STAR_ARG_FINALLY);
        animStartDeg = 0;
        timeInterval = interval;
        handler.postDelayed(runable,time);
    }


    /**
     * 最终动画的实现设置
     */
    private class LoadingAnimRunable implements Runnable {
        @Override
        public void run() {
            if (animStartDeg < endDegree){
                animStartDeg+=3;
                invalidate();
                startAnim = true;
                finalAnim = true;
                handler.postDelayed(this,timeInterval);
            }else{
                animStartDeg= 0;
                startAnim = false;
                finalAnim = false;
                handler.removeCallbacks(runable);
            }
        }
    }
}
