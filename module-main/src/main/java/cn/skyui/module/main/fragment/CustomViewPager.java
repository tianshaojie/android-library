package cn.skyui.module.main.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    /**
     * 是否可以滑动，默认true可滑动
     */
    private boolean isCanScroll = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isCanScroll && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isCanScroll && super.onInterceptTouchEvent(event);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    /**
     * 根据isCanScroll属性，设置页面切换时的滑动翻页效果
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, isCanScroll);
    }

    /**
     * 是否可以滑动
     * @param isCanScroll 默认true可滑动
     */
    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}