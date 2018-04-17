/** 
 * TuSdkDemo
 * DemoListHeader.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午2:13:03 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.view;

import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import org.lasque.tusdk.Group.GroupHeader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.xdu.poscam.R;

/**
 * 范例列表分组视图
 * 
 * @author Clear
 */
public class DemoListHeader extends TuSdkCellRelativeLayout<GroupHeader>
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return R.layout.demo_view_list_header;
	}

	public DemoListHeader(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public DemoListHeader(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public DemoListHeader(Context context)
	{
		super(context);
	}

	/** 标题视图 */
	private TextView mTitleView;

	@Override
	public void loadView()
	{
		super.loadView();
		// 标题视图
		mTitleView = this.getViewById(R.id.lsq_titleLabel);
	}

	@Override
	protected void bindModel()
	{
		if (this.getModel() == null || this.getModel().titleResId == 0) return;
		mTitleView.setText(this.getModel().titleResId);
	}
}