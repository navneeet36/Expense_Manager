package com.example.hp.expense_manager.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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

public class AddIncome extends BaseActivity implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;


    Toolbar toolbar;
    EditText addincome, discrption;
    private ArrayList<String> arraySpinner;
    AppCompatSpinner account;
    ArrayList<BeanAccountInfo> list;
    private String[] a;
    Button submit;
    TextView addaccount;
    String s, adminid,role,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addincome);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addincome = (EditText) findViewById(R.id.income);
        addaccount = (TextView) findViewById(R.id.addaccount);
        discrption = (EditText) findViewById(R.id.discription);
        account = (AppCompatSpinner) findViewById(R.id.account);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        submit = (Button) findViewById(R.id.submit);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);

        adminid = editor.getString("adminid", "-1 ");
        String useraccounts=editor.getString("accounts","-1");
         a=useraccounts.split(",");
        a[a.length-1]="";

        role=editor.getString("role","-1");
        username=editor.getString("username","-1");
if(role.equalsIgnoreCase("user"))
{
addaccount.setVisibility(View.INVISIBLE);
    MyCustomAdapter1 adapter = new MyCustomAdapter1(getApplicationContext(), R.layout.spinner_row, a);
    account.setAdapter(adapter);
}
if(role.equalsIgnoreCase("admin"))
{
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);

        VolleyHelper.postRequestVolley(AddIncome.this, URL_API.AddAccount, hashMap, RequestCodes.AddAccount, false);
}
        addaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(AddIncome.this)
                        .title("Add Account")
                        .content("")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .positiveText("ok")

                        .input("Enter Account Name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                s = input.toString();
                               if(s.length()>0) {
                                   HashMap<String, String> hashMap2 = new HashMap<String, String>();
                                   BeanAccountInfo b1 = new BeanAccountInfo();
                                   b1.setAdminid(adminid);
                                   b1.setAccountname(s);
                                   Gson gson = new Gson();
                                   hashMap2.put("data", gson.toJson(b1));
                                   VolleyHelper.postRequestVolley(AddIncome.this, URL_API.AddNewAccount, hashMap2, RequestCodes.AddNewAccount, false);
                               }
                            }
                        })

                        .show();


            }


        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allvalid = true;
                if(role.equalsIgnoreCase("admin"))
                {
                if (addincome.getText().toString().matches("")  || btnDatePicker.getText().toString().matches("") || btnTimePicker.getText().toString().matches("") || list.get(account.getSelectedItemPosition()).getAccountname().matches("")) {
                    allvalid = false;
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                }}
                else
                {
                    if (addincome.getText().toString().matches("")  || btnDatePicker.getText().toString().matches("") || btnTimePicker.getText().toString().matches("") || a[account.getSelectedItemPosition()].matches("")) {
                        allvalid = false;
                        Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();

                    }}
                if(addincome.getText().toString().length()>10)
                {
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "you have entered incorrect value in add income", Toast.LENGTH_SHORT).show();
                }

                if (allvalid) {
                    HashMap<String, String> hashMap1 = new HashMap<String, String>();
                    BeanEntriesInfo b = new BeanEntriesInfo();
                    b.setAdminid(adminid);
                    b.setAmount(addincome.getText().toString());
                    b.setDescription(discrption.getText().toString());
                    b.setDate(btnDatePicker.getText().toString());
                    b.setTime(btnTimePicker.getText().toString());
                    b.setIncoming("yes");
                    b.setOutgoing("no");
                    b.setUsername(username);
                    if(role.equalsIgnoreCase("admin"))
                    b.setAccount(list.get(account.getSelectedItemPosition()).getAccountname());
                    else
                        b.setAccount(a[account.getSelectedItemPosition()]);
                    Gson gson = new Gson();
                    hashMap1.put("data", gson.toJson(b));
                    VolleyHelper.postRequestVolley(AddIncome.this, URL_API.AddIncome, hashMap1, RequestCodes.AddIncome, false);


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

                            btnDatePicker.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

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
        } else if (requestCode == 4) {
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
        } else if (requestCode == 6) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("adminid", adminid);

                    VolleyHelper.postRequestVolley(AddIncome.this, URL_API.AddAccount, hashMap, RequestCodes.AddAccount, false);

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
        if(role.equalsIgnoreCase("user"))
            startActivity(UserPage.class);
        else

            startActivity(AdminPanel.class);

    }

    public class MyCustomAdapter1 extends ArrayAdapter<String> {

        public MyCustomAdapter1(Context context, int textViewResourceId,
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
