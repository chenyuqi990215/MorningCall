package com.example.a13787.morningcall;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity
{
    private MyDatabaseHelper dbHelp = new MyDatabaseHelper(this,"Userinfo.db",null,4);  //update on 2019.3.1
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    @Override
    protected void initView()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EditText pass = (EditText) findViewById(R.id.register_password);
        pass.setTypeface(Typeface.DEFAULT);
        pass.setTransformationMethod(new PasswordTransformationMethod());
        pass = (EditText) findViewById(R.id.register_confirm);
        pass.setTypeface(Typeface.DEFAULT);
        pass.setTransformationMethod(new PasswordTransformationMethod());
        final RadioGroup sex = (RadioGroup) findViewById(R.id.register_sex);
        final EditText userName = (EditText) findViewById(R.id.register_username);
        final EditText email = (EditText) findViewById(R.id.register_email);
        final EditText password = (EditText) findViewById(R.id.register_password);
        final EditText check = (EditText) findViewById(R.id.register_confirm);
        final EditText tel = (EditText) findViewById(R.id.register_tel);
        final EditText schoolName = (EditText) findViewById(R.id.register_schoolname);
        final EditText studid = (EditText) findViewById(R.id.register_studid);
        Button btn_register = (Button) findViewById(R.id.email_sign_in_button);
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password1 = password.getText().toString();
                String password2 = check.getText().toString();
                if (!password1.equals(password2))
                {
                    Toast.makeText(RegisterActivity.this,"Error: Password not equal",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ContentValues contentValues = new ContentValues();
                    int sexChoseId = sex.getCheckedRadioButtonId();
                    switch (sexChoseId) {
                        case R.id.mainRegisterRdBtnFemale:
                            contentValues.put("sex", "Female");
                            break;
                        case R.id.mainRegisterRdBtnMale:
                            contentValues.put("sex", "Male");
                            break;
                        default:
                            contentValues.put("sex", "Default");
                            break;
                    }
                    String info;
                    info = userName.getText().toString();
                    contentValues.put("username", info);
                    info = email.getText().toString();
                    contentValues.put("email", info);
                    info = password.getText().toString();
                    contentValues.put("password", info);
                    info = tel.getText().toString();
                    contentValues.put("tel", info);
                    info = schoolName.getText().toString();
                    contentValues.put("schoolname", info);
                    info = studid.getText().toString();
                    contentValues.put("studid", info);
                    String result = dbHelp.addData(contentValues);
                    Toast.makeText(RegisterActivity.this,result,Toast.LENGTH_SHORT).show();
                    if (result.equals("Accepted"))
                    {
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    @Override
    protected int initLayout()
    {
        return R.layout.activity_register;
    }
    @Override
    protected void initListener()
    {

    }
    @Override
    protected void initData()
    {

    }
}
