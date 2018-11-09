package cn.skyui.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.MotionEvent;
import android.view.View;

public class UiUtil {

    public static void addClickEffect(View... views) {
        for (final View view : views) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    PropertyValuesHolder scaleX;
                    PropertyValuesHolder scaleY;
                    PropertyValuesHolder alpha;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f,
                                    0.93f);
                            scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f,
                                    0.93f);
                            alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0.8f);

                            ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY).setDuration(150).start();
                            break;

                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.93f,
                                    1f);
                            scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.93f,
                                    1f);
                            alpha = PropertyValuesHolder.ofFloat("alpha", 0.8f, 1f);

                            ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY).setDuration(200).start();
                            break;

                    }
                    return false;
                }
            });
        }
    }


    /**
     * 将传入的View加上点击效果 home tab使用
     *
     * @param views
     */
    public static void addBottomBarClickEffect(View... views) {
        for (final View view : views) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    PropertyValuesHolder scaleX;
                    PropertyValuesHolder scaleY;
                    PropertyValuesHolder alpha;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
                            scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
                            alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0.8f);
                            ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY).setDuration(200).start();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            ObjectAnimator animator;
                            scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1.2f);
                            scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1.2f);
                            alpha = PropertyValuesHolder.ofFloat("alpha", 0.8f, 1f);
                            animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY);
                            animator.setDuration(200);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.2f, 1.0f);
                                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.2f, 1.0f);
                                    ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).setDuration(100).start();
                                }
                            });
                            animator.start();
                            break;
                        default: break;
                    }
                    return false;
                }
            });
        }
    }

    private static long lastClickTime = 0L;
    //这里设置的时间间隔是800ms

    public static boolean isFastClick(){
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(0 < timeD && timeD < 800){
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
