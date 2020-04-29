package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class PwdEditText extends AppCompatEditText {
    private Paint sidePaint, backPaint, textPaint;
    private Context mC;
    private int spzceX, spzceY;
    private int Wide;
    private int yiInput, weiInput, backColor, textColor;
    private String mText;
    private int textLength;
    private int screenWidth, dp_15;
    private List<RectF> rectFS;

    public PwdEditText(Context context) {
        super(context);
        mC = context;
        init();
    }

    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mC = context;
        init();
    }

    public PwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mC = context;
        init();
    }

    /**
     * 输入监听
     */
    public interface OnTextChangeListeven {
        void onTextChange(String pwd);
    }

    private OnTextChangeListeven onTextChangeListeven;

    public void setOnTextChangeListeven(OnTextChangeListeven onTextChangeListeven) {
        this.onTextChangeListeven = onTextChangeListeven;
    }

    public void clearText() {
        setText("");
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
    }

    private void init() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(mC);
        dp_15 = (int) mC.getResources().getDimension(R.dimen.dp_15);
        setTextColor(0xFF333333);
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        spzceX = dp2px(4);
//        spzceY = dp2px(1);
        Wide = Math.round((screenWidth - dp_15 * 2-21) / 6);
        yiInput = 0xFFEDEDED;
        weiInput = 0xFFEDEDED;
        backColor = 0xFFFFFFFF;
        textColor = 0xFF333333;
        sidePaint = new Paint();
        backPaint = new Paint();
        textPaint = new Paint();
        rectFS = new ArrayList<>();

        mText = "";
        textLength = 6;

        this.setBackgroundDrawable(null);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mText == null) {
            return;
        }
        //如果字数不超过用户设置的总字数，就赋值给成员变量mText；
        // 如果字数大于用户设置的总字数，就只保留用户设置的那几位数字，并把光标制动到最后，让用户可以删除；
        if (text.toString().length() <= textLength) {
            mText = text.toString();
        } else {
            setText(mText);
            setSelection(getText().toString().length());  //光标制动到最后
            //setText(mText)之后键盘会还原，再次把键盘设置为数字键盘；
            setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }
        if (onTextChangeListeven != null) onTextChangeListeven.onTextChange(mText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                heightSize = widthSize / textLength;
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //边框画笔
        sidePaint.setAntiAlias(true);//消除锯齿
        sidePaint.setStrokeWidth(3);//设置画笔的宽度
        sidePaint.setStyle(Paint.Style.STROKE);//设置绘制轮廓
        sidePaint.setColor(weiInput);
        //背景色画笔
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(backColor);
        //文字的画笔
        textPaint.setTextSize(18);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        for (int i = 0; i < textLength; i++) {
            //区分已输入和未输入的边框颜色
            if (mText.length() >= i) {
                sidePaint.setColor(yiInput);
            } else {
                sidePaint.setColor(weiInput);
            }
            //RectF的参数(left,  top,  right,  bottom); 画出每个矩形框并设置间距，间距其实是增加左边框距离，缩小上下右边框距离；
            RectF rect = new RectF(i * Wide + spzceX, spzceY, i * Wide + Wide - spzceX, Wide - spzceY); //四个值，分别代表4条线，距离起点位置的线
            canvas.drawRoundRect(rect, 9, 9, backPaint); //绘制背景色
            canvas.drawRoundRect(rect, 9, 9, sidePaint); //绘制边框；
            rectFS.add(rect);
        }
        //画密码圆点
        for (int j = 0; j < mText.length(); j++) {
            canvas.drawCircle(rectFS.get(j).centerX(), rectFS.get(j).centerY(), dp2px(5), textPaint);
        }
    }

    private int dp2px(float dpValue) {
        float scale = mC.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
