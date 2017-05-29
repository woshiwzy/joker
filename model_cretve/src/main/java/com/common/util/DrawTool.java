package com.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * draw tool
 * 
 * @author zouxinxin
 * 
 */
public class DrawTool {

	
	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	/*
	 * 缩放图片使之适配屏幕
	 */
	public static Drawable scaleDrable2FitScreen(Context context, Drawable drawable) {
		Point p = Tool.getDisplayMetrics(context);

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		// rate
		float sx = ((float) p.x / width);
		// float sy = ((float) p.y / height);
		// set rate
		matrix.postScale(sx, sx);// 等比例放大
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图

		try {

			Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);// 强制加在图片有可能出现内存溢出,
			if (null != oldbmp && !oldbmp.isRecycled()) {
				oldbmp.recycle();
			}
			return new BitmapDrawable(context.getResources(), newbmp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Drawable zoomDrawable(Drawable drawable, int w, int h, Resources r) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// drawable to bitmap
		Bitmap oldbmp = drawableToBitmap(drawable);
		// create matrix
		Matrix matrix = new Matrix();
		// rate
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		// set rate
		matrix.postScale(sx, sy);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		if (null != oldbmp && !oldbmp.isRecycled()) {
			oldbmp.recycle();
		}
		return new BitmapDrawable(r, newbmp);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	public static Drawable drawablePrintText(Drawable drawable, String text) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		Paint paint = new Paint();
//		paint.setTextSize(10);
		paint.setColor(Color.WHITE);
		drawable.draw(canvas);
		float mst = paint.measureText(text);
		canvas.drawText(text, w / 2 - mst / 2, h / 2, paint);
		BitmapDrawable bdb = new BitmapDrawable(bitmap);
		return bdb;
	}

	public static Bitmap rotateBitmap(Bitmap bm, float orientationDegree) {
		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		try {
			Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
			return bm1;
		} catch (OutOfMemoryError ex) {
		}
		return null;
	}

	public static Drawable drawablePrintText(Drawable drawable, String text, float degrees) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		drawable.draw(canvas);
		float mst = paint.measureText(text);
		canvas.drawText(text, w / 2 - mst / 2, h / 2, paint);
		Bitmap bretmap = rotateBitmap(bitmap, degrees);
		if (null == bretmap) {
			BitmapDrawable bdb = new BitmapDrawable(bitmap);
			return bdb;
		} else {
			BitmapDrawable bdb = new BitmapDrawable(bretmap);
			return bdb;
		}

	}
	  //图片圆角处理
    public static Bitmap getRoundedBitmap(Bitmap mBitmap,int roundPx) {
            //创建新的位图
            Bitmap bgBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Config.ARGB_8888);
            //把创建的位图作为画板
            Canvas mCanvas = new Canvas(bgBitmap);
            
            Paint mPaint = new Paint();
            Rect mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            RectF mRectF = new RectF(mRect);
            //设置圆角半径为20
            mPaint.setAntiAlias(true);
            //先绘制圆角矩形
            mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);
            
            //设置图像的叠加模式
            mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //绘制图像
            mCanvas.drawBitmap(mBitmap, mRect, mRect, mPaint);
            
            return bgBitmap;
    }
}
