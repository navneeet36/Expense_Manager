package com.example.hp.expense_manager.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.BeanAddUser;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;
import com.example.hp.expense_manager.pojo.BeanLoginInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LOGIN extends BaseActivity {
    Toolbar toolbar;
    String user_name;
    String pass_word,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView forget = (TextView) findViewById(R.id.forgetpass);

        final EditText username = (EditText) findViewById(R.id.editText);
        final EditText password = (EditText) findViewById(R.id.editText1);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isvalid = true;
                if (username.getText().toString().matches("")) {
                    isvalid = false;
                    Toast.makeText(getApplicationContext(), "Please fill username", Toast.LENGTH_SHORT).show();
                }
                if (isvalid) {
                    user_name = username.getText().toString();
                    Intent intent = new Intent(LOGIN.this, Forget.class);
                    intent.putExtra("username", user_name);
                    startActivity(intent);
                }
            }
        });
        Button stulogin = (Button) findViewById(R.id.stuLogin);
        stulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = username.getText().toString();
                pass_word = password.getText().toString();

                if (username.equals(null) && username.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Fill All The Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("username", user_name);
                hashMap.put("password", pass_word);

                VolleyHelper.postRequestVolley(LOGIN.this, URL_API.LOGIN_API, hashMap, RequestCodes.LOGIN, false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signup:
                startActivity(SignUpActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        showDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        dismissDialog();
        if (requestCode == 1) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    BeanLoginInfo b = new Gson().fromJson(jsonObject.getString("ot"), BeanLoginInfo.class);
                /*                jsonObject.getJSONObject("ot").*/
                    editor.putString("username", user_name).commit();
                    editor.putString("password", pass_word).commit();
                    editor.putString("role",b.getRole()).commit();
                    editor.putString("loginInfo", jsonObject.getString("ot")).commit();
                    userid=b.getUserid();
                    if (b.getRole().equalsIgnoreCase("admin")) {
                        editor.putString("adminid", b.getAdminid()).commit();
                        Intent in = new Intent(this, AdminPanel.class);
                        in.putExtra("adminid", b.getAdminid());
                        startActivity(in);
                    } else if (b.getRole().equalsIgnoreCase("user")) {
                        //  editor.putString("roll_no", b.getRollNo()).commit();
                        HashMap<String, String> hashMap1 = new HashMap<String, String>();
                        hashMap1.put("userid", b.getUserid());

                        VolleyHelper.postRequestVolley(LOGIN.this, URL_API.LOGINUser, hashMap1, RequestCodes.LOGINUser, false);
   }


                    finish();
                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode==11)
        {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
               //     Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    BeanAddUser b1 = new Gson().fromJson(jsonObject.getString("ot"), BeanAddUser.class);
                /*                jsonObject.getJSONObject("ot").*/
                    editor.putString("adminid", b1.getAdminid()).commit();
                    editor.putString("userid", userid).commit();
                    editor.putString("readonly", b1.getReadonly()).commit();
                    editor.putString("writeonly", b1.getWriteonly()).commit();
                    editor.putString("accounts",b1.getAccounts()).commit();

                    Intent intent = new Intent(this, UserPage.class);
                    intent.putExtra("userid", b1.getUserid());
                    intent.putExtra("adminid",b1.getAdminid());
                    startActivity(intent);
                finish();
            } else
            Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
                }


    }
}

