/**
 * EntryActivity.java
 *程序主界面
 * @author 	Peng Shiyao
 */
package org.lasque.tusdk;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;
import org.lasque.tusdk.psy.api.DefineCameraBase;
import org.lasque.tusdk.psy.api.DefineCameraBaseFragment;
import org.lasque.tusdk.psy.suite.EditAlbumMultipleComponent;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.psy.my.MyActivityManager;
import com.psy.util.Common;

import java.util.Timer;
import java.util.TimerTask;

import cn.xdu.poscam.R;


public class EntryActivity extends TuFragmentActivity
{
	/** 布局ID */
	public static final int layoutId = R.layout.entry_activity;

	MyActivityManager man;

	public EntryActivity()
	{

	}

	/** 初始化控制器 */
	@Override
	protected void initActivity()
	{
		super.initActivity();
		this.setRootView(layoutId, 0);

		man = MyActivityManager.getInstance();
		man.pushOneActivity(EntryActivity.this);

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
				System.exit(0);

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
				System.exit(0);
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/** 相机按钮容器 */
	private View mCameraButtonView;

	/** 编辑器按钮容器 */
	private View mEditorButtonView;


	/**
	 * 初始化视图
	 */
	@Override
	protected void initView()
	{
		super.initView();


		TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
		TuSdk.checkFilterManager(mFilterManagerDelegate);

		mCameraButtonView = this.getViewById(R.id.lsq_entry_camera);
		mEditorButtonView = this.getViewById(R.id.lsq_entry_editor);

		mCameraButtonView.setOnClickListener(mButtonClickListener);
		mEditorButtonView.setOnClickListener(mButtonClickListener);
	}

	/** 滤镜管理器委托 */
	private FilterManagerDelegate mFilterManagerDelegate = new FilterManagerDelegate()
	{
		@Override
		public void onFilterManagerInited(FilterManager manager)
		{
			TuSdk.messageHub().showSuccess(EntryActivity.this, R.string.lsq_inited);
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
				DefineCameraBaseFragment.bmp1 = null;
				Common.bitmap = null;
				Common.fragParamName = null;
				Common.fragParam = null;
			}
			else if (v == mEditorButtonView)
			{
				showAlbumEditorComponent();
			}

		}
	};

	/** 打开相机组件 */
	private void showCameraComponent()
	{
		 new DefineCameraBase().showSample(this);
	}


	/** 打开多功能编辑组件 */
	private void showAlbumEditorComponent()
	{
		new EditAlbumMultipleComponent().showSample(this);
	}


}