package test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by yahier on 17/8/15.
 * 每个段落的首行凸出显示的TextView，当前使用在银联实名认证和支付宝实名认证页面.
 * 做这个Text纯属 是没事找事的。不过做的过程中 有一些乐趣。
 */

public class ProminentTextView extends android.support.v7.widget.AppCompatTextView {

    private float spaceWidth;

    public MyText(Context context) {
        super(context);
    }

    public MyText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = getPaint();
        spaceWidth = mPaint.measureText("1.");//假如提示文字超过9条就不秒了哟。
        mPaint.setColor(getCurrentTextColor());
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float baseline = fm.descent - fm.ascent;

        float y = baseline;  //由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
        final String txt = getText().toString();
        final String[] passages = txt.split("\n");

        for (String passage : passages) {
            float x = 0;
            //文本自动换行
            final String[] texts = autoSplit(passage, mPaint, getWidth());
            for (int i = 0; i < texts.length; i++) {
                String text = texts[i];
                if (i > 0) {
                    x = spaceWidth;
                }
                canvas.drawText(text, x, y, mPaint);
                y += baseline + getLineSpacingExtra();
            }
        }


    }


    private String[] autoSplit(String content, Paint p, float width) {
        final int contentLength = content.length();
        final float textWidth = p.measureText(content);

        if (textWidth <= width) {
            return new String[]{content};
        }
        final float textLines = textWidth / width;
        final float spaceLines = textLines * spaceWidth / width;
        int lines = (int) Math.ceil(textLines + spaceLines);

        int start = 0, end = 1;
        int line = 0;
        String[] lineTexts = new String[lines];
        while (start < contentLength) {
            if (p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                end = end - 1;//超出宽度就退回一个字
                if (line > 0)
                    end = end - 1;//这种做法 不是非常可靠
                lineTexts[line++] = content.substring(start, end);
                start = end;
            }
            if (end == contentLength) { //不足一行的文本
                lineTexts[line] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }

}
