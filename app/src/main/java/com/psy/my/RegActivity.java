package com.psy.my;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psy.db.DBServer;
import com.psy.model.User;
import com.psy.util.Common;
import com.psy.util.EncodeAndDecode;
import com.psy.util.HttpHelper;
import com.psy.util.URL;

import java.util.HashMap;

import cn.xdu.poscam.R;

public class RegActivity extends Activity implements OnClickListener {
    private EditText edUserName, edPhone, edPassword1, edPassword2, edVerify;
    private Button btnReg, btnGetVerifyNo, btnGetVerifyNoGray;
    private TextView msgName, msgPhone, msgPw1, msgPw2, msgVerify;
    private LinearLayout docGray, docRed;
    private ImageView imgBack;
    private String verify = "";
    private String iniPhone = "";
    private TimeCount time;
    private String md5pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(RegActivity.this);
        this.setContentView(R.layout.reg);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        edPhone = (EditText) findViewById(R.id.userPhone);
        edPassword1 = (EditText) findViewById(R.id.pw1);
        edPassword2 = (EditText) findViewById(R.id.pw2);
        edUserName = (EditText) findViewById(R.id.userName);
        btnReg = (Button) findViewById(R.id.btnReg);
        // edVerify = (EditText) findViewById(R.id.verify);
        btnReg = (Button) findViewById(R.id.btnReg);
        // btnGetVerifyNo = (Button) findViewById(R.id.btnGetVerityNo);
        // btnGetVerifyNoGray = (Button) findViewById(R.id.btnGetVerityNoGray);

        msgName = (TextView) findViewById(R.id.msgName);
        msgPw1 = (TextView) findViewById(R.id.msgPwd1);
        msgPw2 = (TextView) findViewById(R.id.msgPwd2);
        msgPhone = (TextView) findViewById(R.id.msgPhone);
        // msgVerify = (TextView) findViewById(R.id.msgVerify);

        docGray = (LinearLayout) findViewById(R.id.docGray);
        docRed = (LinearLayout) findViewById(R.id.docRed);

        imgBack = (ImageView) findViewById(R.id.btnBack);

        imgBack.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        docGray.setOnClickListener(this);
        docRed.setOnClickListener(this);
        // btnGetVerifyNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == docGray) {
            docGray.setVisibility(View.GONE);
            docRed.setVisibility(View.VISIBLE);
        }
        if (v == docRed) {
            docGray.setVisibility(View.VISIBLE);
            docRed.setVisibility(View.GONE);
        }
        if (v == imgBack) {
            Intent intent = new Intent();
            finish();
            intent.setClass(RegActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if (v == btnReg) {

            boolean isConnectWS = true;
//            if(!iniPhone.equals(edPhone.getText().toString().trim()))
//            {
//                msgVerify.setText("该手机号没有获取验证码");
//                time.cancel();
//                btnGetVerifyNo.setText("发送验证码");
//                btnGetVerifyNo.setClickable(true);
//                isConnectWS = false;
//
//            }else
//                msgPhone.setText("");

            if (!edPassword1.getText().toString().matches("[a-zA-Z\\d+]{6,16}")) {
                msgPw1.setText("密码应该为6-16位字母或数字组合");
                isConnectWS = false;
            } else
                msgPw1.setText("");
            if (!edPassword1.getText().toString().equals(edPassword2.getText().toString())) {
                msgPw2.setText("与上次输入密码不同");
                isConnectWS = false;
            } else
                msgPw2.setText("");

            if (!edUserName.getText().toString().matches("[a-zA-Z\\d+]{6,16}")) {
                msgName.setText("用户名应该为6-16位字母或数字组合");
                isConnectWS = false;
            } else
                msgName.setText("");
            if (!edPhone.getText().toString().matches("1[1-9]{1}[0-9]{9}")) {
                msgPhone.setText("手机号码格式错误");
                isConnectWS = false;
            } else
                msgPhone.setText("");
            int flag = Common.isNetworkAvailable(RegActivity.this);
            if (flag == 0) {
                Common.display(RegActivity.this, "请开启手机网络");
                isConnectWS = false;
            }

            if (docGray.getVisibility() == View.VISIBLE) {
                Common.display(getApplicationContext(), "请阅读并同意注册协议");
                isConnectWS = false;
            }

            if (!isConnectWS)
                return;

            md5pw = EncodeAndDecode.getMD5Str(edPassword2.getText().toString());

            try {
                String jsonStr = postData();
                if (HttpHelper.getCode(jsonStr) == 100) {
                    DBServer dbServer = new DBServer(this);
                    dbServer.updateUser(edUserName.getText().toString(), md5pw);
                    Common.display(RegActivity.this, "注册成功");
                    Intent intent1 = new Intent();
                    finish();
                    intent1.setClass(RegActivity.this, LoginActivity.class);
                    startActivity(intent1);
                } else if (HttpHelper.getCode(jsonStr) == 101) {
                    Common.display(RegActivity.this, "该用户名已注册");
                } else if (HttpHelper.getCode(jsonStr) == 102) {
                    Common.display(RegActivity.this, "该手机号已注册");
                } else {
                    Common.display(RegActivity.this, "注册失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Common.display(RegActivity.this, "服务器出错，请稍后再试");
            }

        }
    }


    public String postData() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("username", edUserName.getText().toString());
        paramHM.put("userphone", edPhone.getText().toString());
        paramHM.put("password", md5pw);
        paramHM.put("userpb", "0");
        paramHM.put("taskpic_url", "115.159.124.204:8089/head.png");
        return HttpHelper.postData(URL.REG_URL, paramHM, null);
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btnGetVerifyNo.setVisibility(View.VISIBLE);
            btnGetVerifyNoGray.setVisibility(View.GONE);
            btnGetVerifyNo.setText("获取验证码");
            btnGetVerifyNo.setClickable(true);
            msgVerify.setText("");
            time.cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btnGetVerifyNo.setVisibility(View.GONE);
            btnGetVerifyNoGray.setVisibility(View.VISIBLE);
            btnGetVerifyNoGray.setClickable(false);
            btnGetVerifyNoGray.setText(millisUntilFinished / 1000 + "秒");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            finish();
            intent.setClass(RegActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
