package com.psy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.psy.model.PosLib;
import com.psy.model.User;

import java.io.File;

public class Common {
	
	public static final String APP_ID = "1007259";
	public static final String SECRET_ID = "AKIDBH8PhKWqB7ZtpDFBjVNHIrt5iZp6stJo";
	public static final String SECRET_KEY = "3kRpuIdcQlXuH0LBlAXLcJ8NOyxtWkdG";
	public static Bitmap bitmap;
	public static final String [] SELF_POS_TYPE ={"女神自拍", "潮男自拍"};
	public static final String [] POS_TYPE ={"美女","帅哥","情侣","集体","小孩"};
	public static final String IMG_CACHE_PATH = "/PoseCamera/";
	public static String local_pic_path = Environment.getExternalStorageDirectory()+"/Android/data/cn.xdu.poscam/cache/imageCache/";
	public static User user;
	public static int userId;
	public static PosLib pos;

	public static String fragParamName;
	public static String fragParam;

	public static boolean isInit;

   public static int type2int(String type){
	   int typeInt = 1;
	   if (type.equals("女神自拍")){
		   typeInt = 1;
	   }else if(type.equals("潮男自拍")){
		   typeInt = 2;
	   }else if(type.equals("美女")){
		   typeInt = 3;
	   }else if(type.equals("帅哥")){
		   typeInt = 4;
	   }else if(type.equals("情侣")){
		   typeInt = 5;
	   }else if(type.equals("集体")){
		   typeInt = 6;
	   }else if(type.equals("小孩")){
		   typeInt = 7;
	   }
	   return typeInt;
   }

	public static void display(Context context , String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

//	public static String getSimpleDraweePicPath(Uri uri){
//			FileBinaryResource resource = (FileBinaryResource)
//					Fresco.getImagePipelineFactory().getMainDiskStorageCache()
//							.getResource(new SimpleCacheKey(uri.toString()));
//		if (resource!=null) {
//			File file = resource.getFile();
//			if (file!=null)
//				return file.getPath();
//			else return null;
//		}
//		else return null;
//
//	}


}
