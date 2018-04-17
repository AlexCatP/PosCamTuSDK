package com.psy.my;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.psy.model.PosLib;
import com.psy.util.Common;
import com.psy.util.HttpHelper;
import com.psy.util.URL;

import org.json.JSONException;
import org.lasque.tusdk.psy.api.DefineCameraBaseFragment;
import org.lasque.tusdk.psy.api.DefineCameraBase;

import java.util.ArrayList;
import java.util.HashMap;

import cn.xdu.poscam.R;

public class PosLibActivity extends Activity implements View.OnClickListener {
    private GridView gridView;


    private ArrayList<HashMap<String, Object>> ArrayListHashMap;
    private HashMap<String, Object> hashMap;
    private LinearLayout loading, mainContent, frontll, notFrontll;
    private ArrayList<HashMap<String, Object>> ah;
    private Button boy, girl, gruop, couple, kid, boyself, girlself,hotSelf,hot;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    loadFail(msg.obj.toString());
                    break;
                case 1:
                    ArrayList<HashMap<String, Object>> ah =
                            (ArrayList<HashMap<String, Object>>) msg.obj;
                    loadData(ah);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(PosLibActivity.this);
        setContentView(R.layout.pos_lib);
        initView();
    }

    public void initView() {
        loading = (LinearLayout) findViewById(R.id.loading);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);
        frontll = (LinearLayout) findViewById(R.id.frontll);
        notFrontll = (LinearLayout) findViewById(R.id.notFrontll);
        boy = (Button) findViewById(R.id.boy);
        girl = (Button) findViewById(R.id.girl);
        couple = (Button) findViewById(R.id.couple);
        gruop = (Button) findViewById(R.id.group);
        kid = (Button) findViewById(R.id.kid);
        girlself = (Button) findViewById(R.id.girlSelf);
        boyself = (Button) findViewById(R.id.boySelf);
        hotSelf = (Button)findViewById(R.id.hotlSelf);
        hot = (Button) findViewById(R.id.hot);

        boy.setOnClickListener(this);
        girl.setOnClickListener(this);
        couple.setOnClickListener(this);
        gruop.setOnClickListener(this);
        kid.setOnClickListener(this);
        girlself.setOnClickListener(this);
        boyself.setOnClickListener(this);
        hotSelf.setOnClickListener(this);
        hot.setOnClickListener(this);


        if (DefineCameraBaseFragment.frontStatus == 1) {
            frontll.setVisibility(View.VISIBLE);
            notFrontll.setVisibility(View.GONE);

        } else {
            frontll.setVisibility(View.GONE);
            notFrontll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loading.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (DefineCameraBaseFragment.frontStatus == 1) {
            initData(R.id.hotlSelf);
        } else {
            initData(R.id.hot);
        }

    }

    private void loadFail(String str) {
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.GONE);
        Common.display(PosLibActivity.this, str);
    }

    private void initData(final int id) {
        new Thread() {
            @Override
            public void run() {
                try {
                    ah = new ArrayList<>();
                    switch (id) {
                        case R.id.hot:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_TYPE_NOT12,null));
                            break;
                        case R.id.hotlSelf:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_TYPE12,null));
                            break;
                        case R.id.girlSelf:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=1"));
                            break;

                        case R.id.boySelf:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=2"));
                            break;

                        case R.id.girl:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=3"));
                            break;
                        case R.id.boy:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=4"));
                            break;
                        case R.id.couple:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=5"));
                            break;
                        case R.id.group:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=6"));
                            break;
                        case R.id.kid:
                            ah = HttpHelper.AnalysisPosInfo(HttpHelper.sendGet(
                                    URL.FIND_POS_BY_ID_TID, "tid=7"));
                            break;
                    }


                    if (ah != null) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = ah;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "未加载到图片 :(";
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }


            }
        }.start();
    }

    private void loadData(final ArrayList<HashMap<String, Object>> posLists) {
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        //在handler里面加载
        gridView = (GridView) findViewById(R.id.gview);
        getData(posLists);
        //Collections.reverse(ArrayListHashMap);
        PosPicAdapter adapter = new PosPicAdapter(PosLibActivity.this, ArrayListHashMap,
                R.layout.gird_item, new String[]{"pospic"},
                new int[]{R.id.gvImg});
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }

    private void getData(ArrayList<HashMap<String, Object>> posLists) {
        ArrayListHashMap = new ArrayList<>();
        for (int i = 0; i < posLists.size(); i++) {// list
            hashMap = new HashMap<>();
            hashMap.put("userid", posLists.get(i).get("userid"));
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


    public static ArrayList<PosLib> getPoses(ArrayList<HashMap<String, Object>> ah) {
        PosLib posLib = null;
        ArrayList<PosLib> poes = new ArrayList<>();
        //System.out.println(ah);
        for (int i = 0; i < ah.size(); i++) {
            posLib = new PosLib();
            posLib.setUserId((int) ah.get(i).get("userid"));
            posLib.setPosContent(ah.get(i).get("poscontent").toString());
            posLib.setPosId((int) ah.get(i).get("posid"));
            posLib.setPosName(ah.get(i).get("posname").toString());
            posLib.setTags(ah.get(i).get("tags").toString());
            posLib.setPosUrl(ah.get(i).get("pos_pic_url").toString());
            posLib.setTypeId((int) ah.get(i).get("typeid"));
            posLib.setPosPb((int) ah.get(i).get("pospb"));
            poes.add(posLib);
        }
        return poes;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hot:
                setBarStyle(hot);
                initData(R.id.hot);
                break;
            case R.id.hotlSelf:
                setBarStyle(hotSelf);
                initData(R.id.hotlSelf);
                break;
            case R.id.girl:
                setBarStyle(girl);
                initData(R.id.girl);
                break;
            case R.id.boy:
                setBarStyle(boy);
                initData(R.id.boy);
                break;
            case R.id.couple:
                setBarStyle(couple);
                initData(R.id.couple);
                break;
            case R.id.group:
                setBarStyle(gruop);
                initData(R.id.group);
                break;
            case R.id.kid:
                setBarStyle(kid);
                initData(R.id.kid);
                break;
            case R.id.girlSelf:
                setBarStyle(girlself);
                initData(R.id.girlSelf);
                break;
            case R.id.boySelf:
                setBarStyle(boyself);
                initData(R.id.boySelf);
                break;
        }

    }

    private void setBarStyle(Button selectedBtn) {
        Button[] buttons = {hot,hotSelf,girlself, boyself, girl, boy,
                couple, gruop, kid};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackgroundColor(getResources().getColor(R.color.white));
            buttons[i].setTextColor(getResources().getColor(R.color.textGray));
            if (buttons[i].equals(selectedBtn)) {
                if (i==0 || i==1)
                    buttons[i].setTextColor(getResources().getColor(R.color.shanZhaRed));
                else
                buttons[i].setTextColor(getResources().getColor(R.color.black));
                buttons[i].setBackgroundColor(getResources().getColor(R.color.bgColor));
            }
        }

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            new DefineCameraBase().showSample(this);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
