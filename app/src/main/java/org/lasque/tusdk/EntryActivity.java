/**
 * TuSdkDemo
 * EntryActivity.java
 *
 * @author 		Clear
 * @Date 		2014-11-15 下午4:30:52 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * @link 		开发文档:http://tusdk.com/docs/android/api/
 */
package org.lasque.tusdk;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;
import org.lasque.tusdk.psy.api.DefineCameraBase;
import org.lasque.tusdk.psy.suite.EditMultipleComponent;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.psy.my.MyActivityManager;
import com.psy.util.Common;

import java.util.Timer;
import java.util.TimerTask;

import cn.xdu.poscam.R;

/**
 * @author Clear
 */
public class EntryActivity extends TuFragmentActivity
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_entry_activity;

	MyActivityManager man;

	public EntryActivity()
	{
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
		 * 开发文档:http://tusdk.com/doc
		 *
		 * 请参见TuApplication类中的SDK初始化代码。
		 */
	}

	/** 初始化控制器 */
	@Override
	protected void initActivity()
	{
		super.initActivity();
		this.setRootView(layoutId, 0);

		man = MyActivityManager.getInstance();
		man.pushOneActivity(EntryActivity.this);

		// 设置应用退出信息ID 一旦设置将触发连续点击两次退出应用事件
		//this.setAppExitInfoId(R.string.lsq_exit_info);
	}

	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit = new Timer();

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						isExit = false;
						hasTask = true;
					}
				};
				tExit.schedule(task, 2000);

			} else {

				man.finishAllActivity();

			}
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				if (!hasTask) {
				}
			} else {

				man.finishAllActivity();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/** 相机按钮容器 */
	private View mCameraButtonView;

	/** 编辑器按钮容器 */
	private View mEditorButtonView;

	/** 组件列表按钮容器 */
	private View mComponentListButtonView;

	/**
	 * 初始化视图
	 */
	@Override
	protected void initView()
	{
		super.initView();
		// sdk统计代码，请不要加入您的应用
		//StatisticsManger.appendComponent(ComponentActType.sdkComponent);

		// 异步方式初始化滤镜管理器 (注意：如果需要一开启应用马上执行SDK组件，需要做该检测，否则可以忽略检测)
		// 需要等待滤镜管理器初始化完成，才能使用所有功能
		TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
		TuSdk.checkFilterManager(mFilterManagerDelegate);

		mCameraButtonView = this.getViewById(R.id.lsq_entry_camera);
		mEditorButtonView = this.getViewById(R.id.lsq_entry_editor);
		mComponentListButtonView = this.getViewById(R.id.lsq_entry_list);

		mCameraButtonView.setOnClickListener(mButtonClickListener);
		mEditorButtonView.setOnClickListener(mButtonClickListener);
		mComponentListButtonView.setOnClickListener(mButtonClickListener);

//		if (Common.isInit)
//			showCameraComponent();
	}

	/** 滤镜管理器委托 */
	private FilterManagerDelegate mFilterManagerDelegate = new FilterManagerDelegate()
	{
		@Override
		public void onFilterManagerInited(FilterManager manager)
		{
			TuSdk.messageHub().showSuccess(EntryActivity.this, R.string.lsq_inited);
			showCameraComponent();
			//Common.isInit = true;
		}
	};

	/** 按钮点击事件处理 */
	private View.OnClickListener mButtonClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{

			if (v == mCameraButtonView)
			{
				showCameraComponent();
			}
			else if (v == mEditorButtonView)
			{
				showEditorComponent();
			}

		}
	};

	/** 打开相机组件 */
	private void showCameraComponent()
	{
		 new DefineCameraBase().showSample(this);
	}

	/** 打开多功能编辑组件 */
	private void showEditorComponent()
	{
		new EditMultipleComponent().showSample(this);
	}


}