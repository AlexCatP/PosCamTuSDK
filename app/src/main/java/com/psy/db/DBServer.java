package com.psy.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.psy.model.User;


/**
 *
 * @author ppssyyy
 *
 */
public class DBServer {

	private DBHelper dbHelper;

	public DBServer(Context context)

	{

		this.dbHelper = new DBHelper(context);
	}

	// //////////////USER/////////////////////
	public void addUser(User user) {
		SQLiteDatabase localSQLiteDatabase = this.dbHelper
				.getWritableDatabase();

		Object[] arrayOfObject = new Object[3];
		arrayOfObject[0] = user.getUserID();
		arrayOfObject[1] = user.getLoginName();
		arrayOfObject[2] = user.getPassword();

		localSQLiteDatabase.execSQL("insert into user(user_id,login_name,password) values(?,?,?)",
				arrayOfObject);
		localSQLiteDatabase.close();
	}

	public ArrayList<User> findUser() {
		ArrayList<User> user = new ArrayList<>();
		SQLiteDatabase localSQLiteDatabase = this.dbHelper
				.getWritableDatabase();
		Cursor localCursor = localSQLiteDatabase.rawQuery("select *  from user", null);

		while (localCursor.moveToNext()) {
			User temp = new User();
			temp.setUserID(localCursor.getInt(localCursor
					.getColumnIndex("user_id")));
			temp.setLoginName(localCursor.getString(localCursor
					.getColumnIndex("login_name")));
			temp.setPassword(localCursor.getString(localCursor
					.getColumnIndex("password")));
			user.add(temp);
		}
		localSQLiteDatabase.close();
		return user;
	}

	public void updateUser(String username, String password) {
		SQLiteDatabase localSQLiteDatabase = this.dbHelper
				.getWritableDatabase();
		localSQLiteDatabase.execSQL("update user set login_name='" + username + "',password='" + password+"'");
		localSQLiteDatabase.close();
	}

	public void updateUser(int userId,String username, String password) {
		SQLiteDatabase localSQLiteDatabase = this.dbHelper
				.getWritableDatabase();
		localSQLiteDatabase.execSQL("update user set user_id="+userId+" ,login_name='" + username + "',password='" + password+"'");
		localSQLiteDatabase.close();
	}


	/**
	 * delTable
	 */
	public void delTable() {
		SQLiteDatabase localSQLiteDatabase = this.dbHelper
				.getWritableDatabase();
		localSQLiteDatabase.execSQL("delete from  user");
		localSQLiteDatabase.close();
	}

}
