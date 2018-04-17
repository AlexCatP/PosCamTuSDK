/** 
 * TuSdkDemo
 * DefineCameraSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:40:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.psy.api;

import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdk.Base;
import org.lasque.tusdk.Group.GroupType;

import android.app.Activity;
import android.os.Bundle;

import com.psy.util.Common;

import cn.xdu.poscam.R;

/**
 * 自定义相机范例
 * 
 * @author Clear
 */
public class DefineCameraBase extends Base
{
	/**
	 * 自定义相机范例
	 */
	public DefineCameraBase()
	{
		super(GroupType.APISample, R.string.sample_api_CameraBase);
	}

	/**
	 * 显示范例
	 * 
	 * @param activity
	 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;

		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);
		DefineCameraBaseFragment defineCameraBaseFragment = new DefineCameraBaseFragment();
		Bundle args = new Bundle();
		args.putString(Common.fragParamName,Common.fragParam);
		defineCameraBaseFragment.setArguments(args);

		this.componentHelper.presentModalNavigationActivity(
				defineCameraBaseFragment, true);
	}
}
