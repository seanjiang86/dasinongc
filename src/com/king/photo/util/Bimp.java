package com.king.photo.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Bimp {
	public static int max = 0;

	public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>(); // 选择的图片的临时列表

//	public static Bitmap revitionImageSize(String path) throws IOException {
//		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeStream(in, null, options);
//		in.close();
//		int i = 0;
//		Bitmap bitmap = null;
//		while (true) {
//			if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
//				in = new BufferedInputStream(new FileInputStream(new File(path)));
//				options.inSampleSize = (int) Math.pow(2.0D, i);
//				options.inJustDecodeBounds = false;
//				bitmap = BitmapFactory.decodeStream(in, null, options);
//				break;
//			}
//			i += 1;
//		}
//		return bitmap;
//	}
	
	public static Bitmap revitionImageSize(String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((800) * temp);
		float scaleWidth = ((float) 800) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}
}
