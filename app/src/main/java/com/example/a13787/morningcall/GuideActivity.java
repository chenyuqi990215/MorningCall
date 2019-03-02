package com.example.a13787.morningcall;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;

public class GuideActivity extends BaseActivity
{

    private ViewPager mViewPager;
    private int[] mImageIds = new int[]{R.drawable.iv_ydy1, R.drawable.iv_ydy2, R.drawable.iv_ydy3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private int mPaintDis;
    private Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    protected void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red);
        start_btn = (Button) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initData();
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(adapter);
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout()
            {
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPaintDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
                System.out.println("距离：" + mPaintDis);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int leftMargin = (int) (mPaintDis * positionOffset) + position * mPaintDis;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);
            }
            @Override
            public void onPageSelected(int position)
            {
                System.out.println("position:" + position);
                if (position == mImageViewList.size() - 1)
                {
                    start_btn.setVisibility(View.VISIBLE);
                }
                else
                {
                    start_btn.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state)
            {
                System.out.println("state:" + state);
            }
        });
    }
    class GuideAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return mImageViewList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }
    @Override
    protected void initData()
    {
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++)
        {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view);
            ImageView pointView = new ImageView(this);
            pointView.setImageResource(R.drawable.shape_point1);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0)
            {
                params.leftMargin = 20;
            }
            pointView.setLayoutParams(params);
            llContainer.addView(pointView);
        }
    }
    @Override
    protected int initLayout()
    {
        return R.layout.activity_guide;
    }
    @Override
    protected void initListener()
    {

    }
}
