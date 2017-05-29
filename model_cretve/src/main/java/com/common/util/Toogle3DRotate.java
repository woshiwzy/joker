package com.common.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class Toogle3DRotate {

    public static final int animationDuration=300;
	
	/**
	 * 2层视图3d切换
	 * @param contianer视图容器
	 * @param cover 遮盖的视图
	 * @param bottom被遮盖视图
	 * @param isCover 如果切换顶部传true，否则传false
	 */
	public static void toogle3d(View contianer,View cover,View bottom ,boolean isCover){
		 if(isCover){
			 applyRotation( contianer, cover, bottom,0,  0, 90) ;
		 }else{
			 applyRotation( contianer, cover, bottom,1,  0, -90); 
		 }
	}
	
	/**
	 * 
	 * @param contianer容器
	 * @param cover 顶部覆盖的视图
	 * @param bottom 底部被遮盖的视图
	 * @param position 视图所在容器的位置
	 * @param start
	 * @param end
	 */
	private static void applyRotation(View contianer,View cover,View bottom,int position, float start, float end) {
        final float centerX = contianer.getWidth() / 2.0f;
        final float centerY = contianer.getHeight() / 2.0f;
        final MyRotate3dAnimation rotation =new MyRotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(animationDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextViewAnimateListener( contianer, cover ,bottom,position));
        contianer.startAnimation(rotation);
    }
    
    private static  final class DisplayNextViewAnimateListener implements Animation.AnimationListener {
        private final int mPosition;
        private final View viewContainer;
        private final View viewCover;
        private final View viewBottom;
        private DisplayNextViewAnimateListener(View container,View cover,View bottom,int position) {
            this.mPosition = position;
            this.viewContainer=container;
            this.viewCover=cover;
            this.viewBottom=bottom;
        }
        public void onAnimationStart(Animation animation) {}
        public void onAnimationEnd(Animation animation) {
        	viewContainer.post(new SwapViews( viewContainer, viewCover, viewBottom,mPosition));
        }
        public void onAnimationRepeat(Animation animation) {}
    }
    private static final class SwapViews implements Runnable {
        private final int mPosition;
        private final View viewContainer;
        private final View viewCover;
        private final View viewBottom;
        public SwapViews(View container,View cover,View bottom,int position) {
            this.mPosition = position;
            this.viewContainer=container;
            this.viewCover=cover;
            this.viewBottom=bottom;
        }

        public void run() {
            final float centerX = viewContainer.getWidth() / 2.0f;
            final float centerY = viewContainer.getHeight() / 2.0f;
            MyRotate3dAnimation rotation;
            if (mPosition ==0) {
            	viewCover.setVisibility(View.GONE);
            	viewBottom.setVisibility(View.VISIBLE);
                rotation = new MyRotate3dAnimation(-90, 0, centerX, centerY, 310.0f, false);
            } else {
            	viewCover.setVisibility(View.VISIBLE);
            	viewBottom.setVisibility(View.GONE);
                rotation = new MyRotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            }
            rotation.setDuration(animationDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            viewContainer.startAnimation(rotation);
        }
    }
    
    
    private static class MyRotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private final float mCenterX;
        private final float mCenterY;
        private final float mDepthZ;
        private final boolean mReverse;
        private Camera mCamera;

        /**
         * Creates a new 3D rotation on the Y axis. The rotation is defined by its
         * start angle and its end angle. Both angles are in degrees. The rotation
         * is performed around a center point on the 2D space, definied by a pair
         * of X and Y coordinates, called centerX and centerY. When the animation
         * starts, a translation on the Z axis (depth) is performed. The length
         * of the translation can be specified, as well as whether the translation
         * should be reversed in time.
         *
         * @param fromDegrees the start angle of the 3D rotation
         * @param toDegrees the end angle of the 3D rotation
         * @param centerX the X center of the 3D rotation
         * @param centerY the Y center of the 3D rotation
         * @param reverse true if the translation should be reversed, false otherwise
         */
        public MyRotate3dAnimation(float fromDegrees, float toDegrees,
                float centerX, float centerY, float depthZ, boolean reverse) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterX = centerX;
            mCenterY = centerY;
            mDepthZ = depthZ;
            mReverse = reverse;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mReverse) {
                camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
            } else {
                camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
            }
            camera.rotateY(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
        
    }

}
