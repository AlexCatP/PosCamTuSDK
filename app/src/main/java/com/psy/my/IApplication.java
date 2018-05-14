package com.psy.my;

/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import android.app.Application;
import android.graphics.Bitmap;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.psy.util.Common;
import com.psy.util.MyImageLoader;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkApplication;

import cn.finalteam.toolsfinal.StorageUtils;
import cn.xdu.poscam.R;

import java.io.File;

import cn.finalteam.galleryfinal.*;
import cn.finalteam.galleryfinal.ImageLoader;


/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/18 下午1:45
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         ************************* TuSDK 集成三部曲 *************************
         *
         * 1. 在官网注册开发者账户
         *
         * 2. 下载SDK和示例代码
         *
         * 3. 创建应用，获取appkey，导出资源包
         *
         ************************* TuSDK 集成三部曲 *************************
         *
         * 关于TuSDK体积 (约2M大小)
         *
         * Android 编译知识：
         * APK文件包含了Java代码，JNI库和资源文件；
         * JNI库包含arm64-v8a,armeabi等不同CPU的编译结果的集合，这些都会编译进 APK 文件；
         * 在安装应用时，系统会自动选择最合适的JNI版本，其他版本不会占用空间；
         * 参考TuSDK Demo的APK 大小，除去资源和JNI库，SDK本身的大小约2M；
         *
         * 开发文档:http://tusdk.com/doc
         */

        // 设置资源类，当 Application id 与 Package Name 不相同时，必须手动调用该方法, 且在 init 之前执行。
        // TuSdk.setResourcePackageClazz(org.lasque.tusdkdemo.R.class);

        // 自定义 .so 文件路径，在 init 之前调用
        // NativeLibraryHelper.shared().mapLibrary(NativeLibType.LIB_CORE, "libtusdk-library.so 文件路径");
        // NativeLibraryHelper.shared().mapLibrary(NativeLibType.LIB_IMAGE, "libtusdk-image.so 文件路径");

        // 设置输出状态，建议在接入阶段开启该选项，以便定位问题。
       // this.setEnableLog(true);
        /**
         *  初始化SDK，应用密钥是您的应用在 TuSDK 的唯一标识符。每个应用的包名(Bundle Identifier)、密钥、资源包(滤镜、贴纸等)三者需要匹配，否则将会报错。
         *
         *  @param appkey 应用秘钥 (请前往 http://tusdk.com 申请秘钥)
         */
       // this.initPreLoader(this.getApplicationContext(), "420d1c1a1f522759-00-4nfzp1");


         TuSdk.enableDebugLog(true);
         TuSdk.init(this.getApplicationContext(), "420d1c1a1f522759-00-4nfzp1");



        /**
         *  指定开发模式,需要与lsq_tusdk_configs.json中masters.key匹配， 如果找不到devType将默认读取master字段
         *  如果一个应用对应多个包名，则可以使用这种方式来进行集成调试。
         */
        // this.initPreLoader(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1", "debug");

        // 如果不想继承TuSdkApplication，直接在自定义Application.onCreate()方法中调用以下方法
        // 初始化全局变量
        // TuSdk.enableDebugLog(true);
        // 开发ID (请前往 http://tusdk.com 获取您的APP 开发秘钥)
        // TuSdk.init(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1");
        // 需要指定开发模式 需要与lsq_tusdk_configs.json中masters.key匹配， 如果找不到devType将默认读取master字段
        // TuSdk.init(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1", "debug");




        //建议在application中配置

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
                Common.IMG_CACHE_PATH);

        //设置主题
        //ThemeConfig theme = ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getApplicationContext().getResources().getColor(R.color.shanZhaRed))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(this, new MyImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setTakePhotoFolder(cacheDir)
                //.setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);

        //universal-image-loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.head) //
                .showImageOnFail(R.drawable.head) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存位置
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)//
                .diskCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()//
                .build();//
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

    }
}
