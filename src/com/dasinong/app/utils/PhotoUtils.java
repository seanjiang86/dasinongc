package com.dasinong.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

public class PhotoUtils {

	public final static String PHOTOS_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dasinong/photos";
	public final static int REQUESTCODE_GALLERY = 201; 
	public final static int REQUESTCODE_CAPTURE = 202;
	public final static int REQUESTCODE_CROP = 203;
	
	
	public static void startPhotoCrop(Context context, Uri uri, int aspectX, int aspectY, int outputX, int outputY, String filePath, String fileName) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		Uri outUri = Uri.fromFile(new File(filePath + File.separator + fileName));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		((Activity)context).startActivityForResult(intent, REQUESTCODE_CROP);
	}
	
	public static void fromCapture(Context context, String filePath, String fileName) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍摄照片
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		Uri uri = Uri.fromFile(new File(filePath + File.separator + fileName));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		((Activity)context).startActivityForResult(intent, REQUESTCODE_CAPTURE);
	}
	
	public static void fromGallery(Context context) {
		Intent intent = new Intent();  
        /* 开启Pictures画面Type设定为image */  
        intent.setType("image/*");  
        /* 使用Intent.ACTION_GET_CONTENT这个Action */  
        intent.setAction(Intent.ACTION_GET_CONTENT);   
        /* 取得相片后返回本画面 */  
        ((Activity)context).startActivityForResult(intent, REQUESTCODE_GALLERY);
	}
	
	public static boolean writeBitmapToPath(Bitmap bitmap, String filePath, String fileName) {
		if (bitmap == null || TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName)) {
			return false;
		}
		FileOutputStream fos = null;
		try {
			File pathFile = new File(filePath);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			
			File file = new File(filePath + File.separator + fileName);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			byte[] array = out.toByteArray();

			fos = new FileOutputStream(file);
			fos.write(array);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(fos != null){
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return degree;
		}
		return degree;
	}
	
	/**
	 * 旋转图片，使图片保持正确的方向。
	 * @param bitmap 原始图片
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
	public static Bitmap rotateBitmap(String path, int degrees) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
//		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		matrix.setRotate(degrees);
		float scaleWidth = ((float) 480) / bitmap.getWidth();
        float scaleHeight = ((float) 480) / bitmap.getHeight();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleWidth);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}
	
	interface PhotoCallBack{
		public void success();
		public void fail();
	}
	
}
