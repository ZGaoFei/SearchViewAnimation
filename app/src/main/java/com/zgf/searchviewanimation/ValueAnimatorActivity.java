package com.zgf.searchviewanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * ValueAnimator 类的使用
 * ObjectAnimator 类的使用
 * TypeEvaluator 类的使用
 * 自定义 view
 */
public class ValueAnimatorActivity extends AppCompatActivity {
    private ImageView ivValue;
    private MyAnimView myAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animator);
        ivValue = (ImageView) findViewById(R.id.iv_value);
        myAnimView = (MyAnimView) findViewById(R.id.my_anim_view);

        ivValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ValueAnimatorActivity.this, LayoutAnimationsActivity.class));
            }
        });

//        animatorSet();
        setValue();
    }

    /**
     * ValueAnimator
     */
    private void setValue() {
        Log.e("========", "=======");
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(1000);
        valueAnimator.setTarget(ivValue);
        valueAnimator.setRepeatCount(2);// 重复次数
        valueAnimator.setCurrentPlayTime(100);// 延迟播放时间
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);// 播放的模式 RESTART 和 REVERSE
        valueAnimator.start();
    }

    /**
     * ObjectAnimator
     */
    private void setObject() {
        /**
         * alpha 透明度
         * rotation 旋转
         * translationX X轴平移 (textview.getTranslationX();)
         * scaleY 垂直方向缩放
         *
         */
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivValue, "alpha", 0f, 1f, 0f);
        animator.setDuration(3000);
        animator.start();
    }

    /**
     * 组合动画
     */
    private void animatorSet() {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(ivValue, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(ivValue, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(ivValue, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).after(moveIn);
        animSet.setDuration(5000);
        animSet.start();
    }

    /**
     * TypeEvaluator 的使用
     */
    private void evaluator() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(300, 300);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), point1, point2);
        anim.setDuration(5000);
        anim.start();
    }

    private void colorEvaluator() {
        ObjectAnimator anim = ObjectAnimator.ofObject(myAnimView, "color", new ColorEvaluator(),
                "#0000FF", "#FF0000");
        anim.setDuration(5000);
        anim.start();
    }
}
