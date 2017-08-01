package com.example.hp.expense_manager.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.BeanLoginInfo;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

public class SignUpActivity extends BaseActivity {
    EditText  userid,username, email, pass, cpass, securityans;
    Button signup;
    RadioButton rb1, rb2;
    private String[] arraySpinner;
    AppCompatSpinner securityques;
    Toolbar toolbar;
    String role;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rb1 = (RadioButton) findViewById(R.id.rbuser);
        rb2 = (RadioButton) findViewById(R.id.rbadmin);
        userid = (EditText) findViewById(R.id.userid);
        username = (EditText) findViewById(R.id.fName);
        securityques=(AppCompatSpinner)findViewById(R.id.security_Question);
        securityans = (EditText) findViewById(R.id.security_Answer);
        email = (EditText) findViewById(R.id.Email);
        cpass = (EditText) findViewById(R.id.cpass);
        signup = (Button) findViewById(R.id.btn_signup);
        pass = (EditText) findViewById(R.id.pass);
        this.arraySpinner = new String[]{
                "Please Select Security Question", " Which is your favourite colour ?", "Who is your best friend ?",
                "Which is your favourite sport ?", "What is your maiden name ?"
        };
        MyCustomAdapter adapter = new MyCustomAdapter(this, R.layout.spinner_row, arraySpinner);
        securityques.setAdapter(adapter);
        //   securityques.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allvalid = true;
                if (username.getText().toString().matches("") || securityques.getSelectedItemPosition() == 0 || securityans.getText().toString().matches("") || email.getText().toString().matches("") || cpass.getText().toString().matches("") || pass.getText().toString().matches("")) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                }

                if (!isValidEmail(email.getText())) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Check your email", Toast.LENGTH_SHORT).show();
                }

                if (!pass.getText().toString().equals(cpass.getText().toString())) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Password not matched", Toast.LENGTH_SHORT).show();
                }
                if (rb1.isChecked()) {

                    role = rb1.getText().toString();
                    user=(userid.getText().toString());
                //    userid.setVisibility(View.VISIBLE);


                } else {

                    role = rb2.getText().toString();
                    user=("0");
              //      userid.setVisibility(View.GONE);


                }

                if (allvalid) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    BeanLoginInfo b = new BeanLoginInfo();
                    b.setUsername(username.getText().toString());
                    b.setRole(role);
                    b.setPassword(pass.getText().toString());
                    b.setEmailid(email.getText().toString());
                    b.setSecurityans(securityans.getText().toString());
                    b.setSecurityques(arraySpinner[securityques.getSelectedItemPosition()]);
                    b.setUserid(user);
                    Gson gson = new Gson();
                    hashMap.put("data", gson.toJson(b));
                                   VolleyHelper.postRequestVolley(SignUpActivity.this,URL_API.SIGN_UP,hashMap,RequestCodes.SIGN_UP,false);
                }
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, LOGIN.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(LOGIN.class);

    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        showDialog();
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        dismissDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        dismissDialog();
        if (requestCode == RequestCodes.SIGN_UP) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, false, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, true, parent);
        }


        public View getCustomView(int position, boolean white, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label = (TextView) row.findViewById(android.R.id.text1);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position));

            return row;
        }
    }

}
