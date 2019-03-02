package com.example.a13787.morningcall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 13787 on 2019/2/28.
 */

public class MyDatabaseHelper  extends SQLiteOpenHelper
{
    private static final String CREATE_USERINFO = "create table userinfo ("
            + "username char(100) unique, "
            + "password char(100) not null, "
            + "email char(100) primary key, "
            + "schoolname char(100) not null, "
            + "studid char(20) not null, "
            + "tel char(20) not null, "
            + "sex char(10) not null, "
            + "headportrait blob)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USERINFO);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists userinfo");
        onCreate(db);
    }
    private Boolean checkUsername(String username)
    {
        final String Pattern = "[a-zA-Z]\\w{6,16}";
        return username.matches(Pattern);
    }
    private Cursor queryDataByUsername(String username)
    {
        String sqlquery = "select * from userinfo where username = '" + username + "'";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sqlquery,null);
    }
    private Cursor queryDataByEmail(String email)
    {
        String sqlquery = "select * from userinfo where email = '" + email + "'";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sqlquery,null);
    }
    public String queryPasswordByEmail(String email)
    {
        if (!checkEmail(email))
            return "Error: Email invalid";
        Cursor cursor = queryDataByEmail(email);
        if (cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex("password"));
        else return "Error: Email not exist";
    }
    private Boolean checkDuplicateUsername(String username)
    {
        Cursor cursor = queryDataByUsername(username);
        return !cursor.moveToFirst();
    }
    private Boolean checkDuplicateEmail(String email)
    {
        Cursor cursor = queryDataByEmail(email);
        return !cursor.moveToFirst();
    }
    private Boolean checkPassword(String password)
    {
        final String Pattern="^(?:\\d+|[a-zA-Z]+|[!@#$%^&*]+)$";
        return password.matches(Pattern);
    }
    private Boolean checkEmail(String email)
    {
        final String Pattern="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return email.matches(Pattern);
    }
    private Boolean checkTel(String tel)
    {
        final String Pattern="^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        return tel.matches(Pattern);
    }
    private Boolean checkStudid(String studid)
    {
        final String Pattern="^[0-9]*[1-9][0-9]*$";
        return studid.matches(Pattern);
    }
    private Boolean checkSex(String sex)
    {
        return (sex.equalsIgnoreCase("male") || sex.equalsIgnoreCase("female"));
    }
    private String checkContent(ContentValues contentValues)
    {
        if (!checkDuplicateUsername(contentValues.getAsString("username")))
            return "Error: User duplicate";
        if (!checkDuplicateEmail(contentValues.getAsString("username")))
            return "Error: Email duplicate";
        if (!checkUsername(contentValues.getAsString("username")))
            return "Error: Username invalid";
        if (!checkEmail(contentValues.getAsString("email")))
            return "Error: Email invalid";
        if (!checkPassword(contentValues.getAsString("password")))
            return "Error: Password invalid";
        if (!checkTel(contentValues.getAsString("tel")))
            return "Error: Telphone invalid";
        if (!checkStudid(contentValues.getAsString("studid")))
            return "Error: Student id invalid";
        if (!checkSex(contentValues.getAsString("sex")))
            return "Error: Sex invalid";
        return "Accepted";
    }
    public String addData(ContentValues contentValues)
    {
        String result = checkContent(contentValues);
        if (!result.equalsIgnoreCase("Accepted"))
            return result;
        SQLiteDatabase db = getWritableDatabase();
        db.insert("userinfo",null,contentValues);
        return result;
    }
}
