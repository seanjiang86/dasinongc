package com.dasinong.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public final class GraphicUtils {
	private final static String TAG = "GraphicUtils";
	public static int mScreenWidth;
	public static int mScreenHeight;

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	* 获取状态栏高度
	* 
	* @return
	*/
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}
	
	public static Bitmap createRoundImage(Context context, Bitmap originalImage, Bitmap mask) {
		RectF clipRect = new RectF();
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Bitmap roundBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(roundBitmap);
		canvas.drawBitmap(originalImage, 0, 0, null);
		clipRect.set(0, 0, width, height);
		canvas.drawBitmap(mask, null, clipRect, null);
		return roundBitmap;
	}

	public static Bitmap createRoundImage(Context context, Bitmap originalImage, int dp) {
		final int CONNER = dip2px(context, dp);
		RectF clipRect = new RectF();
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Bitmap roundBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(roundBitmap);
		Path path = new Path();
		clipRect.set(0, 0, width, height);
		path.addRoundRect(clipRect, CONNER, CONNER, Path.Direction.CCW);
		canvas.clipPath(path);
		canvas.drawBitmap(originalImage, null, clipRect, null);
		return roundBitmap;
	}

	public static Bitmap createReflectedImage(Context context, Bitmap originalImage, Bitmap mask) {
		final int reflectionGap = 1;
		final int CONNER = dip2px(context, 5);
		Path path = new Path();
		RectF clipRect = new RectF();

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap
				.createBitmap(originalImage, 0, height * 3 / 4, width, height / 4, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 4), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(originalImage, 0, 0, null);
		clipRect.set(0, 0, width, height);
		canvas.drawBitmap(mask, null, clipRect, null);

		canvas.save();
		clipRect.set(0, height, width, (height + height / 4 + CONNER) + reflectionGap);
		path.addRoundRect(clipRect, CONNER, CONNER, Path.Direction.CCW);
		canvas.clipPath(path);

		Paint point = new Paint();
		point.setColor(Color.LTGRAY);
		canvas.drawRect(clipRect, point);
		canvas.restore();

		canvas.save();
		clipRect.set(1, height + 1, width - 1, (height + height / 4 + CONNER) + reflectionGap);
		path.reset();
		path.addRoundRect(clipRect, CONNER, CONNER, Path.Direction.CCW);
		canvas.clipPath(path);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		canvas.restore();

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, height, 0, bitmapWithReflection.getHeight() + reflectionGap,
				0x50000000, 0x00000000, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	public final static int LOCATION_LEFT_TOP = 0;
	public final static int LOCATION_BOTTOM_RIGHT = 1;

	public static final Point getViewPosition(View view, int type) {
		int[] locations = new int[2];
		view.getLocationInWindow(locations);
		Point point = new Point(locations[0], locations[1]);
		if (type == LOCATION_LEFT_TOP)
			return point;
		if (type == LOCATION_BOTTOM_RIGHT) {
			int width = view.getWidth();
			int height = view.getHeight();
			point.x += width;
			point.y += height;
			return point;
		}
		return new Point(0, 0);
	}

	public static Bitmap getBitmapByBitmap(Bitmap backBitmap, Bitmap mask, int x, int y) {
		Bitmap output = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawBitmap(mask, x, y, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(backBitmap, rect, rect, paint);

		return output;
	}

	public final static boolean pointInView(Point point, View view) {
		Point left_top = GraphicUtils.getViewPosition(view, GraphicUtils.LOCATION_LEFT_TOP);
		Point bottom_right = GraphicUtils.getViewPosition(view, GraphicUtils.LOCATION_BOTTOM_RIGHT);
		return (point.x >= left_top.x && point.x <= bottom_right.x && point.y >= left_top.y && point.y <= bottom_right.y);
	}

	// 图形配比
	public static Bitmap getImageScale(Bitmap bitmap, int width, int height) {
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();
		float scaleWidth = ((float) width) / srcWidth;
		float scaleHeight = ((float) height) / srcHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap destbmp = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, matrix, true);
		return destbmp;
	}

	/**
	 * 创建缩略图
	 * 
	 * @Title: getImageThumbnail
	 * @Description: TODO
	 * @param context
	 *            上下文
	 * @param bitmap
	 *            原Bitmap
	 * @param width
	 *            缩略图的宽度
	 * @param height
	 *            缩略图的高度
	 * @return 返回缩略图 Bitmap
	 */
	public final static Bitmap getImageThumbnail(Context context, Bitmap bitmap, int width, int height) {
		Bitmap resultBmp = null;
		int widthMax = GraphicUtils.dip2px(context, width);
		int heightMax = GraphicUtils.dip2px(context, height);

		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		if (newWidth <= widthMax && newHeight <= heightMax) {
			return bitmap;
		}
		float scaleWidth = ((float) widthMax) / newWidth;
		float scaleHeight = ((float) heightMax) / newHeight;
		float scale = Math.min(scaleWidth, scaleHeight);

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		resultBmp = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight, matrix, true);

		if (bitmap != null) {
			bitmap.recycle();
		}
		return resultBmp;
	}


	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;// <= 1 ? 2 : roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public final static int AT_TOP = 0;
	public final static int AT_BOTTOM = 1;
	public final static int OTHER = 2;

	public static final int PULL_TO_REFRESH = 3;
	public static final int RELEASE_TO_REFRESH = 4;
	public static final int REFRESHING = 5;
	public static final int FRESH_OTHER = 6;

	/**
	 * @Title: toGrayscale
	 * @Description: 灰度图
	 * @param bmpOriginal
	 * @return Bitmap
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);

		return bmpGrayscale;
	}

	/**
	 * @Title: getColor
	 * @Description: 得到RGB颜色值
	 * @param color
	 * @return int
	 */
	public static int getColor(int color) {
		return Color.rgb(color >> 16, (color & 0x00ffff) >> 8, color & 0x0000ff);
	}


	public static Bitmap getViewBitmap(View view) {
//		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST),MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST));
//		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}
	
	public interface SaveImageCallBack {
		void before();
		void after(Boolean result);
	}
	
}
