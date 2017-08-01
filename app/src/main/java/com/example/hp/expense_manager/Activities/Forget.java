package com.example.hp.expense_manager.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hp on 15-Jul-17.
 */

class Forget  extends BaseActivity {
    String user_name, ques, ans;
    TextView securityques;
    EditText securityans, pass, cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        securityques = (TextView) findViewById(R.id.security_Question);
        securityans = (EditText) findViewById(R.id.security_Answer);
        pass = (EditText) findViewById(R.id.pass);
        cpass = (EditText) findViewById(R.id.cpass);
        user_name = getIntent().getStringExtra("username");
        Button update = (Button) findViewById(R.id.update);

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("username", user_name);
        VolleyHelper.postRequestVolley(Forget.this, URL_API.Forget, hashMap, RequestCodes.Forget, false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isvalid = true;
                if (securityans.getText().toString().matches("")||pass.getText().toString().matches("")||cpass.getText().toString().matches("")) {
                    isvalid = false;
                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                }
                if (isvalid) {
                    if (securityans.getText().toString().equalsIgnoreCase(ans)) {
                        if (pass.getText().toString().equalsIgnoreCase(cpass.getText().toString())) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("username", user_name);
                            hashMap.put("password", pass.getText().toString());
                            VolleyHelper.postRequestVolley(Forget.this, URL_API.Update, hashMap, RequestCodes.Update, false);
                        } else
                            Toast.makeText(getApplicationContext(), "password and confirm password did not match", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "security answer did not matched", Toast.LENGTH_SHORT).show();

                }
            }
        });
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
        if (requestCode == 12) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    ques = jsonObject.getString("security_question");
                    ans = jsonObject.getString("security_answer");
                    securityques.setText(ques);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 13) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

