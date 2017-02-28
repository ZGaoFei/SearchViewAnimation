package com.zgf.searchviewanimation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    private ImageView ivTop;
    private TextView tvNext;

    private int totalDy;
    private int width;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        width = px2dip(getWindowManager().getDefaultDisplay().getWidth());
        width = getWindowManager().getDefaultDisplay().getWidth();

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        textView = (TextView) findViewById(R.id.et_search_input);

//        ivTop = (ImageView) findViewById(R.id.iv_top);
        tvNext = (TextView) findViewById(R.id.tv_main);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ValueAnimatorActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(setData(), this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                int densityDy = px2dip(totalDy);

                Log.e("=totalDy=====densityDy=", "========" + totalDy + "===" + densityDy + "=====" + width);

                if (densityDy < 500) {
                    setLayout(densityDy);
                } else {
                    setLayout(500);
                }
            }
        });

    }

    private void setLayout(int densityDy) {
        double v = (double) densityDy / 500;

        frameLayout.setPivotX(width);
        frameLayout.setPivotY(0);

        frameLayout.setScaleX(1 - (float) v);

        double v1 = v > 0.066 ? 0.066 : v;
        double v2 = v > 0.54 ? 0.54 : v;
        double v3 = v > 0.95 ? 0.95 : v;

        textView.setBackgroundColor(Color.rgb(255 - (int) (v1 * 255), 255 - (int) (255 * v2), 255 - (int) (255 * v3)));

    }

    private void setCircle() {
        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        Rect rect = new Rect(0, 0, frameLayout.getWidth(), frameLayout.getHeight());

        drawable.setBounds(rect);
    }

    /**
     * 一个坐标点，以某个点为缩放中心，缩放指定倍数，求这个坐标点在缩放后的新坐标值。
     * @param targetPointX 坐标点的X
     * @param targetPointY 坐标点的Y
     * @param scaleCenterX 缩放中心的X
     * @param scaleCenterY 缩放中心的Y
     * @param scale 缩放倍数
     * @return 坐标点的新坐标
     */
    protected PointF scaleByPoint(float targetPointX, float targetPointY, float scaleCenterX, float scaleCenterY, float scale){
        Matrix matrix = new Matrix();
        // 将Matrix移到到当前圆所在的位置，
        // 然后再以某个点为中心进行缩放
        matrix.preTranslate(targetPointX,targetPointY);
        matrix.postScale(scale,scale,scaleCenterX,scaleCenterY);
        float[] values = new float[9];
        matrix.getValues(values);
        return new PointF(values[Matrix.MTRANS_X],values[Matrix.MTRANS_Y]);
    }

    private void startAni(int w) {
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        layoutParams.width = width - w;
        frameLayout.setLayoutParams(layoutParams);
    }

    /**
     * 自造数据
     *
     * @return
     */
    private List<String> setData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("item" + i);
        }
        return list;
    }

    /**
     * 获取px值
     *
     * @param pxValue
     * @return
     */
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<String> list;
        private Context context;

        public MyAdapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv, null);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            public MyViewHolder(View itemView) {
                super(itemView);

                textView = (TextView) itemView.findViewById(R.id.tv_item);
            }
        }
    }
}
