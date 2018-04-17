package com.psy.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.psy.model.PosLib;
import com.psy.model.User;
import com.psy.util.Common;


import android.content.Context;

public class DataAccess {
	public void initDatabase(Context context) {
		DBServer dbServer = new DBServer(context);
		User user = new User();
		user.setUserID(-1);
		user.setLoginName("");
		user.setPassword("");
		dbServer.addUser(user);
	}
}
