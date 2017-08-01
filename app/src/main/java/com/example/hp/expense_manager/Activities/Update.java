package com.example.hp.expense_manager.Activities;

/**
 * Created by hp on 26-Jul-17.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.BeanAccountInfo;
import com.example.hp.expense_manager.pojo.BeanEntriesInfo;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
/**
 * Created by hp on 19-Jul-17.
 */

public class Update extends BaseActivity implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;


    Toolbar toolbar;
    EditText addincome, discrption;
    private ArrayList<String> arraySpinner;
    AppCompatSpinner account;
    ArrayList<BeanAccountInfo> list;
    Button submit;
    String s,adminid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addincome = (EditText) findViewById(R.id.income);
        discrption = (EditText) findViewById(R.id.discription);
        account = (AppCompatSpinner) findViewById(R.id.account);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        submit=(Button)findViewById(R.id.submit);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        Intent intent = getIntent();
        s = intent.getStringExtra("entryno");
        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);

        adminid = editor.getString("adminid", "-1 ");

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);

        VolleyHelper.postRequestVolley(Update.this, URL_API.AddAccount, hashMap, RequestCodes.AddAccount, false);
                submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allvalid = true;
                if (addincome.getText().toString().matches("") || discrption.getText().toString().matches("") || btnDatePicker.getText().toString().matches("") || btnTimePicker.getText().toString().matches("") || list.get(account.getSelectedItemPosition()).getAccountname().matches("")) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                }
                if (allvalid) {
                    HashMap<String, String> hashMap1 = new HashMap<String, String>();
                    BeanEntriesInfo b = new BeanEntriesInfo();
                    b.setAmount(addincome.getText().toString());
                    b.setDescription(discrption.getText().toString());
                    b.setDate(btnDatePicker.getText().toString());
                    b.setTime(btnTimePicker.getText().toString());
                    b.setEntryno(s);
                    b.setAccount(list.get(account.getSelectedItemPosition()).getAccountname());
                    Gson gson = new Gson();
                    hashMap1.put("data", gson.toJson(b));
                    VolleyHelper.postRequestVolley(Update.this, URL_API.UpdateEntry, hashMap1, RequestCodes.UpdateEntry, false);


                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            btnTimePicker.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        if (requestCode == 3) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {
                    list = new Gson().fromJson(jsonObject.get("accounts").toString(), new TypeToken<ArrayList<BeanAccountInfo>>() {
                    }.getType());
                    arraySpinner = new ArrayList<>();
                    for (BeanAccountInfo b : list) {
                        arraySpinner.add(b.getAccountname());
                    }
                    MyCustomAdapter adapter = new MyCustomAdapter(getApplicationContext(), R.layout.spinner_row, arraySpinner);
                    account.setAdapter(adapter);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        else if (requestCode==4)
        {
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
        else if (requestCode==15)
        {
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
                               ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminPanel.class);

    }
}


