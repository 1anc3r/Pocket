package me.lancer.pocket.tool.mvp.image.bean;

import android.content.Context;
import android.util.AttributeSet;

public class ImageViewBean extends android.support.v7.widget.AppCompatImageView {
    private OnMeasureListener onMeasureListener;

    public ImageViewBean(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewBean(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //将图片测量的大小回调到onMeasureSize()方法中
        if (onMeasureListener != null) {
            onMeasureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public interface OnMeasureListener {
        public void onMeasureSize(int width, int height);
    }

}
