package com.common.util;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.common.adapter.AnimationListenerAdapter;

public class AccordionAnimation {

	public static final int WRAP_CONTENT_HEIGHT = -1;
	private boolean progressFlag = false;
	private View view;
	
	public AccordionAnimation(final View view) {
		
		this.view = view;
		
	}
	
	public void accordion(final int startHeight, final int targetHeight, int duration) {

		final int startCorrectedHeight = (startHeight == WRAP_CONTENT_HEIGHT) ? wrapContentHeight(view) : startHeight;
		final int targetCorrectedHeight = (targetHeight == WRAP_CONTENT_HEIGHT) ? wrapContentHeight(view) : targetHeight;

		if(view.getHeight() == targetCorrectedHeight || progressFlag) {
			
			return;
			
		}
		
		progressFlag = true;
		
		Animation animation = new Animation() {
			
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				
		        int newHeight = (int)(startCorrectedHeight + (targetCorrectedHeight - startCorrectedHeight)*interpolatedTime);
		        view.getLayoutParams().height = newHeight;
		        view.requestLayout();
				
			}
			
			@Override
			public boolean willChangeBounds() {
				return true;
			}
			
		};
		animation.setDuration(duration);
		view.startAnimation(animation);
		sleep(duration);
		
	}



	public void accordion(final int startHeight, final int targetHeight, int duration, AnimationListenerAdapter animationListenerAdapter) {

		final int startCorrectedHeight = (startHeight == WRAP_CONTENT_HEIGHT) ? wrapContentHeight(view) : startHeight;
		final int targetCorrectedHeight = (targetHeight == WRAP_CONTENT_HEIGHT) ? wrapContentHeight(view) : targetHeight;

		if(view.getHeight() == targetCorrectedHeight || progressFlag) {

			return;

		}

		progressFlag = true;

		Animation animation = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {

				int newHeight = (int)(startCorrectedHeight + (targetCorrectedHeight - startCorrectedHeight)*interpolatedTime);
				view.getLayoutParams().height = newHeight;
				view.requestLayout();

			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}

		};
		animation.setDuration(duration);
		animation.setAnimationListener(animationListenerAdapter);
		view.startAnimation(animation);
		sleep(duration);

	}


	private void sleep(int duration) {
		
		new Handler().postDelayed(new Runnable() {
			
		    public void run() {
		    
		    	progressFlag = false;
		    	
		    }
		    
		}, duration);
		
	}
	
	private int wrapContentHeight(View view) {
		
		view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return view.getMeasuredHeight();
		
	}
	
}
/*** Example
	AccordionAnimation accordionAnimation = new AccordionAnimation(view);
	
	int startHeight = 0;
	int targetHeight = AccordionAnimation.WRAP_CONTENT_HEIGHT;	// This means the height originally have.
	int duration = 500;
	
	accordionAnimation.accordion(startHeight, targetHeight, duration);
***/