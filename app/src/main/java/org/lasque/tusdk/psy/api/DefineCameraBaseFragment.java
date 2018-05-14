/**
 * TuSdkDemo
 * SimpleCameraFragment.java
 *
 * @author Clear
 * @Date 2014-11-20 下午1:22:19
 * @Copyright (c) 2014 tusdk.com. All rights reserved.
 */
package org.lasque.tusdk.psy.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.EntryActivity;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface.TuSdkStillCameraListener;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView.TuCameraFilterViewDelegate;
import org.lasque.tusdk.impl.components.camera.TuFocusTouchView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.psy.model.Param;
import com.psy.model.PosLib;
import com.psy.model.YouTuTag;
import com.psy.my.DetailActivity;
import com.psy.my.LoginActivity;
import com.psy.my.MyActivityManager;
import com.psy.my.PhotoProcessActivity;
import com.psy.my.PosLibActivity;
import com.psy.my.PosPicAdapter;
import com.psy.my.RegActivity;
import com.psy.my.UserActivity;
import com.psy.my.ZoomImageView;
import com.psy.util.BitmapUtil;
import com.psy.util.Common;
import com.psy.util.DataConvert;
import com.psy.util.GenericProgressDialog;
import com.psy.util.HttpHelper;
import com.psy.util.URL;
import com.youtu.Youtu;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xdu.poscam.R;


/**
 * 快速相机范例
 *
 * @author Clear
 */

public class DefineCameraBaseFragment extends TuFragment {
    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int flashStatus = 0;
    public static int frontStatus = 0;

    /**
     * 布局ID
     */
    public static final int layoutId = R.layout.define_camera_base_fragment;

    public DefineCameraBaseFragment() {
        this.setRootViewLayoutId(layoutId);
    }

    /**
     * 相机视图
     */
    private RelativeLayout cameraView;


    // 底部栏
    // private RelativeLayout bottomBar;
    /**
     * 拍摄按钮
     */
    private Button captureButton;
    /**
     * 滤镜选择栏
     */
    private TuCameraFilterView filterBar;
    /**
     * 滤镜开关按钮
     */
    private Button filterToggleButton;
    /**
     * 闪光灯按钮列表
     */
    private ArrayList<TextView> mFlashBtns = new ArrayList<TextView>(3);
    /**
     * 相机对象
     */
    private TuSdkStillCameraInterface mCamera;
    /**
     * 默认闪关灯模式
     */
    private CameraFlash mFlashModel = CameraFlash.Off;

    private Activity activity;
    private static final int REQUEST_CODE = 100;
    private String photo_path;


    /**
     * pose图片
     */
    private static ZoomImageView zoomImageView;

    private ImageView my,map;

    private static Bitmap bitmap;
    private static String imgURL;

    public static Bitmap bmp1;

    public static LinearLayout resultll;
    private TextView resultTxt;

    JSONObject respose;
    ArrayList<YouTuTag> tags;
    ArrayList<PosLib> poses;
    MyActivityManager man;
    ArrayList<HashMap<String, Object>> arrHM;
    private ArrayList<HashMap<String, Object>> ArrayListHashMap;
    private HashMap<String, Object> hashMap;
    public static GridView gridView;
    private ImageView flashBtn, switchBtn;

    private Button mBtnSearch;
    private Button mBtnTakePhoto;
    public static int pic_status;
    public static int PIC_ON_CAMERA = 11;
    public static int PIC_FOR_SELECT = 12;
    public static int PIC_FOR_DETAIL = 13;

    String imgPath, detailImgPath, analysis, analysis1;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

//                if (analysis!=null && analysis.equals("analysis1"))
//                {
//
//                    tags = new ArrayList<>();
//                    YouTuTag youTuTag4 = new YouTuTag();
//                    youTuTag4.setTagConfidence(60);
//                    youTuTag4.setTagName("奥斯卡");
//                    tags.add(youTuTag4);
//
//                    YouTuTag youTuTag1 = new YouTuTag();
//                    youTuTag1.setTagConfidence(50);
//                    youTuTag1.setTagName("合影");
//                    tags.add(youTuTag1);
//
//                    YouTuTag youTuTag2 = new YouTuTag();
//                    youTuTag2.setTagConfidence(40);
//                    youTuTag2.setTagName("演出");
//                    tags.add(youTuTag2);
//
//                    YouTuTag youTuTag3 = new YouTuTag();
//                    youTuTag3.setTagConfidence(30);
//                    youTuTag3.setTagName("晚会");
//                    tags.add(youTuTag3);
//
//                    YouTuTag youTuTag = new YouTuTag();
//                    youTuTag.setTagConfidence(20);
//                    youTuTag.setTagName("灯");
//                    tags.add(youTuTag);
//
//
//                    YouTuTag youTuTag5 = new YouTuTag();
//                    youTuTag5.setTagConfidence(5);
//                    youTuTag5.setTagName("演员");
//                    tags.add(youTuTag5);
//
//                }else{
//                    tags = HttpHelper.getTags((JSONObject) msg.obj);
//                }

                tags = HttpHelper.getTags((JSONObject) msg.obj);

                System.out.println("tags=" + tags);
                if (tags.size() == 0) {
                    dismissProgressDialog(activity);
                    Toast.makeText(activity, "图片格式错误或图片破损",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (tags.get(0).getTagName().equals("error")) {
                        dismissProgressDialog(activity);
                        Toast.makeText(activity, "图片格式错误或图片破损",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dismissProgressDialog(activity);
                        poses = new ArrayList<>();
                        Collections.sort(tags);
                        //现在能找到新图片所有的特征向量了，通过json传给服务器
                        //然后服务器返回json结果
                        try {
                            Param param = new Param();
                            param.setTag_id(DataConvert.getParam2(DataConvert.toTagMap(), tags));
                            param.setWeight(DataConvert.getParam3(tags));
                            HashMap<Integer, Double> param3 =
                                    DataConvert.getMergedParam(param.getTag_id(), param.getWeight());

                            String json = postData(DataConvert.toJsonArray(param3));
                            //System.out.println("----param=" + DataConvert.toJsonArray(param3)+" json="+json);
                            arrHM = HttpHelper.AnalysisPosInfo2(json);

                            if (arrHM != null) {
                                Collections.reverse(arrHM);
                                //System.out.println("===" + arrHM);
                                loadData(arrHM);


                                //poses = PosLibActivity.getPoses(arrHM);
                                String temp = "";
                                for (int i = 0; i < tags.size(); i++) {
                                    temp += (tags.get(i).getTagName() + "、");
                                }
                                resultTxt.setText("为您找到适合" + "\""
                                        + temp.substring(0, temp.length() - 1) + "\"场景的pose");
                                if (gridView.getVisibility() == View.VISIBLE
                                        && resultll.getVisibility() == View.VISIBLE) {
                                    gridView.setVisibility(View.GONE);
                                    resultll.setVisibility(View.GONE);
                                } else {
                                    gridView.setVisibility(View.VISIBLE);
                                    resultll.setVisibility(View.VISIBLE);
                                }


                                if (imgURL != null) {
                                    setZoomImg(imgURL);
                                }
                            } else {
                                Common.display(activity, "分析失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Common.display(activity, "服务器错误，请稍后再试");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Common.display(activity, "服务器错误，请稍后再试");
                        }


                    }
                }

            }

            if (msg.what == -1) {
                loadFail(msg.obj.toString());
            }

        }
    };

    public String postData(String param) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("json", param);
        return HttpHelper.postData(URL.GET_RESULT, paramHM, null);
    }


    private void loadFail(String str) {
        dismissProgressDialog(activity);
        Common.display(activity, str);
    }


    @Override
    protected void loadView(ViewGroup view) {

        // 相机视图
        cameraView = this.getViewById(R.id.cameraView);

        // 拍摄按钮
        captureButton = this.getViewById(R.id.captureButton);
        captureButton.setOnClickListener(mClickListener);

        // 滤镜开关按钮
        filterToggleButton = this.getViewById(R.id.filterButton);
        filterToggleButton.setOnClickListener(mClickListener);
        // 滤镜选择栏
        filterBar = this.getViewById(R.id.lsq_group_filter_view);
        // 设置控制器
        filterBar.setActivity(this.getActivity());
        // 绑定选择委托
        filterBar.setDelegate(mFilterBarDelegate);

        // 设置默认是否显示
        filterBar.setDefaultShowState(false);
        // 显示滤镜标题视图
        filterBar.setDisplaySubtitles(true);

        // 滤镜选择栏 设置SDK内置滤镜
        filterBar.loadFilters();

        flashBtn = this.getViewById(R.id.flash_view);

        resultll = this.getViewById(R.id.resultll);
        resultTxt = this.getViewById(R.id.resultTxt);
        mBtnSearch = this.getViewById(R.id.search);
        mBtnTakePhoto = this.getViewById(R.id.takephoto);
        my = this.getViewById(R.id.my);
        map = this.getViewById(R.id.map);
        switchBtn = this.getViewById(R.id.camera_flip_view);
        zoomImageView = this.getViewById(R.id.zoom_image_view);
        gridView = this.getViewById(R.id.gview);


        flashBtn.setOnClickListener(mClickListener);
        mBtnSearch.setOnClickListener(mClickListener);
        mBtnTakePhoto.setOnClickListener(mClickListener);
        my.setOnClickListener(mClickListener);
        switchBtn.setOnClickListener(mClickListener);
        map.setOnClickListener(mClickListener);


        // 设置是否显示前后摄像头切换按钮
        this.showViewIn(switchBtn, CameraHelper.cameraCounts() > 1);


    }

    @Override
    protected void viewDidLoad(ViewGroup view) {
        // 创建相机对象
        if (frontStatus == 1)
            mCamera = TuSdk.camera(this.getActivity(), CameraFacing.Front, this.cameraView);
        else
            mCamera = TuSdk.camera(this.getActivity(), CameraFacing.Back, this.cameraView);
        // 相机对象事件监听
        mCamera.setCameraListener(mCameraListener);

        // 可选：设置输出图片分辨率
        // 注意：因为移动设备内存问题，可能会限制部分机型使用最高分辨率
        // 请使用 TuSdkGPU.getGpuType().getSize() 查看当前设备所能够进行处理的图片尺寸
        // 默认使用 1920 * 1440分辨率
        // mCamera.setOutputSize(new TuSdkSize(5312, 2988));

        // 可选，设置相机手动聚焦
        mCamera.adapter().setFocusTouchView(TuFocusTouchView.getLayoutId());
        // 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
        mCamera.setEnableFaceDetection(true);
        // 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
        // mCamera.setDisableMirrorFrontFacing(true);
        // 启动相机
        mCamera.startCameraCapture();


        switch (flashStatus) {
            case 0:
                mFlashModel = CameraFlash.Off;
                flashBtn.setImageResource(R.drawable.camera_flash_off);
                break;
            case 1:
                mFlashModel = CameraFlash.On;
                flashBtn.setImageResource(R.drawable.camera_flash_on);
                break;
            case 2:
                mFlashModel = CameraFlash.Auto;
                flashBtn.setImageResource(R.drawable.camera_flash_auto);
                break;
            default:
                break;
        }

        this.setFlashModel(mFlashModel);


        if (getArguments() != null) {
            zoomImageView = this.getViewById(R.id.zoom_image_view);
            //System.out.println("getArguments()="+getArguments());
            if (zoomImageView != null) {
                imgPath = getArguments().getString("image_path");
                detailImgPath = getArguments().getString("detail_image_path");
                analysis = getArguments().getString("extra");
            }
        }


        if (imgPath != null) {
            setZoomImg(imgPath);
        }
        if (detailImgPath != null) setZoomImg(detailImgPath);

        if (analysis != null) {
            if (analysis.equals("analysis") || analysis.equals("analysis1"))
                imgAnalysis();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.isFragmentPause() && mCamera != null) {
            mCamera.startCameraCapture();
        }

        if (sharedPreferences == null) {
            sharedPreferences = this.getActivity().getSharedPreferences("poscam_sp", this.getActivity().MODE_PRIVATE);
        }
        editor = sharedPreferences.edit();
        flashStatus = sharedPreferences.getInt("flash", 0);
        frontStatus = sharedPreferences.getInt("front", 0);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        activity = this.getActivity();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) mCamera.stopCameraCapture();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCamera != null) {
            mCamera.destroy();
            mCamera = null;
        }
        editor.putInt("flash", flashStatus);
        editor.putInt("front", frontStatus);
        editor.commit();


    }

    /**
     * 滤镜选择栏委托
     */
    private TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterViewDelegate() {
        /**
         * @param view
         *            滤镜分组视图
         * @param itemData
         *            滤镜分组元素
         * @param canCapture
         *            是否允许拍摄
         * @return 是否允许继续执行
         */
        @Override
        public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture) {
            // 直接拍照
            if (canCapture) {
                handleCaptureAction();
                return true;
            }

            switch (itemData.type) {
                case TypeFilter:
                    // 设置滤镜
                    return handleSwitchFilter(itemData.filterOption);
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow) {

        }
    };

    /**
     * 按钮点击事件
     */
    private OnClickListener mClickListener = new OnSafeClickListener() {
        @Override
        public void onSafeClick(View v) {
            switch (v.getId()) {
//                // 取消
//                case R.id.cancelButton:
//                    handleCancelAction();
//                    break;
                case R.id.map:
                    Common.display(getActivity(),"景点定位功能正在开发中...");
                    break;
                // 闪光灯
                case R.id.flash_view:
                    handleFlashAction();
                    break;
                // 切换摄像头
                case R.id.camera_flip_view:
                    handleSwitchCameraAction();
                    break;
                // 拍摄
                case R.id.captureButton:
                    handleCaptureAction();
                    break;
                // 滤镜开关切换按钮
                case R.id.filterButton:
                    handleToggleFilterAction();
                    break;
                case R.id.search: // 智能分析
                    mBtnSearch.setSelected(true);
                    mBtnTakePhoto.setSelected(false);
                    pic_status = PIC_ON_CAMERA;
                    editor.commit();

                    //配置功能
                    FunctionConfig functionConfig = new FunctionConfig.Builder()
                            .setEnableCrop(true)
                            .setEnableRotate(true)
                            .setCropSquare(true)
                            .setEnablePreview(true)
                            .setEnableEdit(false)//编辑功能
                            .setEnableCrop(false)//裁剪功能
                            .setEnableCamera(false)//相机功能
                            .build();

                    GalleryFinal.openGallerySingle(REQUEST_CODE, functionConfig, mOnHanlderResultCallback);

                    break;

                case R.id.takephoto: // pose库
                    mBtnTakePhoto.setSelected(true);
                    mBtnSearch.setSelected(false);
                    Intent intent = new Intent();
                    pic_status = PIC_FOR_SELECT;
                    dismissProgressDialog(activity);
                    activity.finish();
                    intent.setClass(activity, PosLibActivity.class);
                    startActivity(intent);

                    editor.putInt("flash", flashStatus);
                    editor.putInt("front", frontStatus);
                    editor.commit();

                    break;

                case R.id.my:
                    Intent intent1 = new Intent();
                    activity.finish();
                    intent1.setClass(activity, LoginActivity.class);
                    startActivity(intent1);

                    editor.putInt("flash", flashStatus);
                    editor.putInt("front", frontStatus);
                    editor.commit();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 取消动作
     */
    private void handleCancelAction() {
        this.dismissActivityWithAnim();
    }

    /**
     * 滤镜开关切换按钮
     */
    protected void handleToggleFilterAction() {
        filterBar.showGroupView();
    }

    /**
     * 闪光灯动作
     */
    private void handleFlashAction() {
        switch (flashStatus) {
            case 0:
                mFlashModel = CameraFlash.On;
                flashBtn.setImageResource(R.drawable.camera_flash_on);
                flashStatus = 1;
                break;
            case 1:
                mFlashModel = CameraFlash.Auto;
                flashBtn.setImageResource(R.drawable.camera_flash_auto);
                flashStatus = 2;
                break;
            case 2:
                mFlashModel = CameraFlash.Off;
                flashBtn.setImageResource(R.drawable.camera_flash_off);
                flashStatus = 0;
                break;
            default:
                break;
        }


        this.setFlashModel(mFlashModel);
    }

    /**
     * 设置闪光灯模式
     */
    private void setFlashModel(CameraFlash flashMode) {
        mFlashModel = flashMode;
        if (mCamera != null) {
            mCamera.setFlashMode(flashMode);
        }
    }

    /**
     * 切换摄像头
     */
    private void handleSwitchCameraAction() {
        if (frontStatus == 1)
            frontStatus = 2;
        else frontStatus = 1;

        if (mCamera != null) {
            mCamera.rotateCamera();
        }
    }

    /**
     * 拍照
     */
    private void handleCaptureAction() {
        if (mCamera != null) {
            mCamera.captureImage();
        }
    }

    /**
     * 处理滤镜切换
     *
     * @param opt
     * @return 是否允许切换
     */
    private boolean handleSwitchFilter(FilterOption opt) {
        if (mCamera == null) return false;

        String code = FilterLocalPackage.NormalFilterCode;
        if (opt != null) {
            code = opt.code;
        }

        mCamera.switchFilter(code);
        return true;
    }

    /**
     * 相机监听委托
     */
    private TuSdkStillCameraListener mCameraListener = new TuSdkStillCameraListener() {
        /**
         * 相机状态改变 (如需操作UI线程， 请检查当前线程是否为主线程)
         *
         * @param camera
         *            相机对象
         * @param state
         *            相机运行状态
         */
        @Override
        public void onStillCameraStateChanged(TuSdkStillCameraInterface camera, CameraState state) {
            if (state != CameraState.StateStarted) return;

            if (camera.canSupportFlash()) {
                camera.setFlashMode(mFlashModel);
                showViewIn(flashBtn, true);
            } else {
                showViewIn(flashBtn, false);
            }
        }

        /**
         * 获取拍摄图片 (如需操作UI线程， 请检查当前线程是否为主线程)
         *
         * @param camera
         *            相机对象
         * @param result
         *            Sdk执行结果
         */
        @Override
        public void onStillCameraTakedPicture(TuSdkStillCameraInterface camera, final TuSdkResult result) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    test(result);
                }
            });
        }
    };


    private void loadData(final ArrayList<HashMap<String, Object>> posLists) {
        getData(posLists);
        Collections.reverse(ArrayListHashMap);
        PosPicAdapter adapter = new PosPicAdapter(activity, ArrayListHashMap,
                R.layout.gird_item, new String[]{"pospic"},
                new int[]{R.id.gvImg});

        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }


    private void getData(ArrayList<HashMap<String, Object>> posLists) {
        ArrayListHashMap = new ArrayList<>();
        for (int i = 0; i < posLists.size(); i++) {// list
            hashMap = new HashMap<>();
            hashMap.put("posid", posLists.get(i).get("posid"));
            hashMap.put("typeid", posLists.get(i).get("typeid"));
            hashMap.put("tags", posLists.get(i).get("tags"));
            hashMap.put("pospb", posLists.get(i).get("pospb"));
            hashMap.put("posname", posLists.get(i).get("posname"));
            hashMap.put("pospic", posLists.get(i).get("pos_pic_url"));
            hashMap.put("poscontent", posLists.get(i).get("poscontent"));
            ArrayListHashMap.add(hashMap);
        }

    }

    public static void setZoomImg(String imgPath) {
        if (imgPath != null) {
            System.out.println("imgPath=" + imgPath);
            bitmap = BitmapFactory.decodeFile(imgPath);
            if (bitmap != null) {
                zoomImageView.setImageBitmap(bitmap);
                zoomImageView.setAlpha((float) 0.5);
                if (zoomImageView.getVisibility() == View.VISIBLE ||
                        gridView.getVisibility() == View.VISIBLE) {
                    zoomImageView.setVisibility(View.GONE);
                } else {
                    zoomImageView.setVisibility(View.VISIBLE);
                }

                imgURL = imgPath;
            }
        }
    }

    /**
     * 测试方法
     */
    private void test(TuSdkResult result) {
        result.logInfo();

        Bitmap image = result.image; //得到结果

        Intent inten = new Intent();
        inten.setClass(this.getActivity(), PhotoProcessActivity.class);
        Common.bitmap = image;
        startActivity(inten);
        this.getActivity().finish();

    }


    private AlertDialog mAlertDialog;

    private void dismissProgressDialog(final Activity activity) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing()
                        && !activity.isFinishing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        });
    }

    private void showProgressDialog(final String msg, final Activity activity) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mAlertDialog == null) {
                    mAlertDialog = new GenericProgressDialog(
                            activity);
                }
                mAlertDialog.setMessage(msg);
                ((GenericProgressDialog) mAlertDialog)
                        .setProgressVisiable(true);
                mAlertDialog.setCancelable(false);
                mAlertDialog.setOnCancelListener(null);
                mAlertDialog.show();
                mAlertDialog.setCanceledOnTouchOutside(false);
            }
        });


    }

    GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.v("onHanlderSuccess", "reqeustCode: " + reqeustCode + "  resultList.size" + resultList.size());
            for (PhotoInfo info : resultList) {
                switch (reqeustCode) {
                    case REQUEST_CODE:
                        photo_path = info.getPhotoPath();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                if (photo_path == null) {
                                    Toast.makeText(activity, "图片路径为空", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    // 数据返回
                                    bmp1 = BitmapUtil.decodeSampledBitmapFromFile(
                                            photo_path, 500, 500, activity);
                                    Youtu faceYoutu = new Youtu(Common.APP_ID,
                                            Common.SECRET_ID, Common.SECRET_KEY,
                                            Youtu.API_YOUTU_END_POINT);
                                    try {
                                        showProgressDialog("分析中", activity);
                                        respose = faceYoutu.ImageTag(bmp1);
                                    } catch (KeyManagementException e) {
                                        // TODO Auto-generated catch block
                                        dismissProgressDialog(activity);
                                        e.printStackTrace();
                                    } catch (NoSuchAlgorithmException e) {
                                        // TODO Auto-generated catch block
                                        dismissProgressDialog(activity);
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        dismissProgressDialog(activity);
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        dismissProgressDialog(activity);
                                        e.printStackTrace();
                                    }
                                    Message msg = handler.obtainMessage();
                                    int flag = Common.isNetworkAvailable(activity);
                                    if (flag == 0) {
                                        msg.what = -1;
                                        msg.obj = "请开启手机网络";
                                    } else {
                                        msg.what = 1;
                                        msg.obj = respose;
                                    }

                                    handler.sendMessage(msg);
                                    System.out.println(respose + "");
                                    // bmp1.recycle();
                                }
                                Looper.loop();
                            }
                        }).start();


                        break;

                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(activity, "requestCode: " + requestCode + "  " + errorMsg, Toast.LENGTH_LONG).show();

        }
    };

    public void imgAnalysis() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if (Common.bitmap != null) {
                    Youtu faceYoutu = new Youtu(Common.APP_ID,
                            Common.SECRET_ID, Common.SECRET_KEY,
                            Youtu.API_YOUTU_END_POINT);
                    try {

                        showProgressDialog("分析中", activity);
                        respose = faceYoutu.ImageTag(Common.bitmap);
                    } catch (KeyManagementException e) {
                        // TODO Auto-generated catch block
                        dismissProgressDialog(activity);
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        dismissProgressDialog(activity);
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        dismissProgressDialog(activity);
                        e.printStackTrace();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        dismissProgressDialog(activity);
                        e.printStackTrace();
                    }
                    Message msg = handler.obtainMessage();
                    int flag = Common.isNetworkAvailable(activity);
                    if (flag == 0) {
                        msg.what = -1;
                        msg.obj = "请开启手机网络";
                    } else {
                        msg.what = 1;
                        msg.obj = respose;
                    }
                    handler.sendMessage(msg);
                    System.out.println(respose + "");
                }
                Looper.loop();

            }
        }).start();
    }

    @Override
    public boolean onBackPressed() {
        DefineCameraBaseFragment.bmp1 = null;
        Common.bitmap = null;
        Common.fragParamName = null;
        Common.fragParam = null;
        Intent intent = new Intent();
        intent.setClass(getActivity(), EntryActivity.class);
        getActivity().finish();
        startActivity(intent);
        return true;
    }


}