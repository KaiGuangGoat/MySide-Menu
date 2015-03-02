package com.example.sidemenu_test.widget;

import java.util.List;

import com.example.sidemenu_test.animation.Rotate2DAnimation;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

public class SideMenuView extends ViewGroup{
	private static final String TAG = "sideMenuView";
	
	private int childHeight;
	private int childWidth;
	private int mHeight;//��ͼ�ĸ߶�
	
	private boolean showSideMenu = true;
	private Handler handler;
	private SelectSideMenuListener listener;
	
	public SideMenuView(Context context) {
		super(context);
		handler = new Handler(getContext().getMainLooper());
	}

	public SideMenuView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public SideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		handler = new Handler(getContext().getMainLooper());
//		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SideMenuView, defStyleAttr, 0);
//		
//		a.recycle();
	}
	
	
	public void setSelectSideMenuListener(SelectSideMenuListener listener){
		this.listener = listener;
	}
	
	private long ANIM_DURATION = 175;
	private Rotate2DAnimation getAnimation(int fromDegree,int toDegree,int y){
		Rotate2DAnimation animation = new Rotate2DAnimation(fromDegree, toDegree, 0.0f, y, 0.0f, false);
		animation.setFillAfter(true);
		animation.setDuration(ANIM_DURATION);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		return animation;
	}
	

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childTop = 0;
		for(int i=0;i<getChildCount();i++){
			if(i == 0){
				childHeight = getChildAt(i).getMeasuredHeight();
				childWidth = getChildAt(i).getMeasuredWidth();
			}
			final View childView = getChildAt(i);
			final int childHeight = childView.getMeasuredHeight();
			if(childView.getVisibility() == View.VISIBLE){
//				
				final int topTemp = childTop;
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						childView.layout(0, topTemp, childView.getMeasuredWidth(), topTemp + childHeight);
						childView.startAnimation(getAnimation(90, 0, childView.getHeight()/2));
					}
				}, (long)delay(i));
			}
			
			childTop += childHeight;
		}
		mHeight = childTop;
		
	}
	
	private void animationChilds(final View childView,int position){
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Rotate2DAnimation animation;
				if(showSideMenu){
					animation = getAnimation(90, 0, childView.getHeight()/2);
				}else{
					animation = getAnimation(0, 90, childView.getHeight()/2);
				}
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						childView.clearAnimation();
						if(!showSideMenu){
							childView.setVisibility(INVISIBLE);
						}else{
							childView.setVisibility(VISIBLE);
						}
					}
				});
				childView.startAnimation(animation);
			}
		}, (long)delay(position));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for(int i=0;i<getChildCount();i++){
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	
	
	private float lastY;
	private float lastX;
	private long downTimeStamp;
	private int scrollYLast;//��һ���ƶ�ƫ�Ƶ���
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			lastY = event.getY();
			scrollYLast = getScrollY();
			downTimeStamp = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			if(mHeight <= getHeight()){
				return false;
			}
			int deltaY = (int) (lastY - event.getY());
			if(deltaY>0){//���ϻ���
				if(getScrollY() >= mHeight - getHeight()){
					break;
				}
			}else{//���»���
				if(getScrollY() <= 0){ 
					break;
				}
			}
			scrollTo(0, deltaY+scrollYLast);
			
			break;
		case MotionEvent.ACTION_UP:
			if(Math.abs(lastX - event.getX())<10 && Math.abs(lastY - event.getY())<10)
				if(System.currentTimeMillis() - downTimeStamp < 300){
					if(listener != null){
						listener.onSelect(getPosition(getScrollY()+event.getY()));
					}
					hideSideMenu();
				}
			break;
		}
		return true;
	} 
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
	
	private int getPosition(float y){
		if(y<childHeight){
			return 1;
		}
		if(y%childHeight == 0){
			return (int) (y/childHeight);
		}
		if(y%childHeight >0){
			return (int)y/childHeight + 1;
		}
		return 0;
	}
	
	private double delay(int position){
		float weight = (float)position/(float)getChildCount();
		return 3 * ANIM_DURATION * weight;
	}
	
	public void showSideMenu(){
		showSideMenu = true;
		for(int i=0;i<getChildCount();i++){
			animationChilds(getChildAt(i), i+1);
		}
	}
	
	public void hideSideMenu(){
		showSideMenu = false;
		for(int i=0;i<getChildCount();i++){
			animationChilds(getChildAt(i), i+1);
		}
	}
	
	public boolean isShowSideMenu(){
		return showSideMenu;
	}
	
	public int getChildWidth(){
		return childWidth;
	}
	
	public void setLayoutParams(int width){
		getLayoutParams().width = width;
	}
	
	public interface SelectSideMenuListener{
		public void onSelect(int position);
	}

}
