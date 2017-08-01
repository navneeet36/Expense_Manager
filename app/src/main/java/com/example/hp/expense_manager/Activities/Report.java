package com.example.hp.expense_manager.Activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.hp.expense_manager.R;
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

class Report extends BaseActivity implements View.OnClickListener{
    Toolbar toolbar;
    TextView income, expense, total;
    private int mYear, mMonth, mDay, mHour, mMinute;

    Button choose, btnDatePicker,btnDatePicker1,btnDatePicker3,btn;
    ArrayList<BeanEntriesInfo> list1 = new ArrayList<>();
    ArrayList<BeanEntriesInfo> list2 = new ArrayList<>();
    private String[] array,a;
    Integer pos;
     String adminid,role;
  long in = 0;
  long   outp = 0;
    long totl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        income = (TextView) findViewById(R.id.Income);
        expense = (TextView) findViewById(R.id.Expense);
        total = (TextView) findViewById(R.id.Total);
        choose = (Button) findViewById(R.id.choose);
        btnDatePicker = (Button) findViewById(R.id.date);
        btnDatePicker.setOnClickListener(this);
        btnDatePicker1 = (Button) findViewById(R.id.date1);
        btnDatePicker1.setOnClickListener(this);
        btnDatePicker3 = (Button) findViewById(R.id.but);
        btnDatePicker3.setOnClickListener(this);
        btn = (Button) findViewById(R.id.ok);
        btn.setOnClickListener(this);
        btnDatePicker1.setVisibility(View.INVISIBLE);
        btnDatePicker3.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        btnDatePicker.setVisibility(View.INVISIBLE);

        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);
         adminid = editor.getString("adminid", "-1 ");
        role=editor.getString("role","-1");
        String useraccounts = editor.getString("accounts", "-1");
        a = useraccounts.split(",");

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);


        VolleyHelper.postRequestVolley(Report.this, URL_API.Entries, hashMap, RequestCodes.Entries, false);
        this.array = new String[]{
                "till today", " by day", "by peroid"
        };

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(array);


            }
        });




            }


    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);

        if (requestCode == 7) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {


                    list1 = new Gson().fromJson(jsonObject.get("faculty_list").toString(), new TypeToken<ArrayList<BeanEntriesInfo>>() {
                    }.getType());

                    for (BeanEntriesInfo b : list1) {
                        if(role.equalsIgnoreCase("admin")) {
                            if (b.getIncoming().equals("yes")) {
                                in = Long.parseLong(b.getAmount()) + in;
                            } else if (b.getIncoming().equals("no")) {
                                outp = (Long.parseLong(b.getAmount())) + outp;
                            }
                        }
                        else
                        {

                                for (int j = 0; j < a.length; j++) {
                                    if (a[j].equalsIgnoreCase(b.getAccount()))
                                    {
                                        if (b.getIncoming().equals("yes")) {
                                            in = Long.parseLong(b.getAmount()) + in;
                                        } else if (b.getIncoming().equals("no")) {
                                            outp = (Long.parseLong(b.getAmount())) + outp;
                                        }
                                    }

                                }}
                    }
                    totl = in - outp;
                    total.setText(String.valueOf(totl));
                    income.setText(String.valueOf(in));
                    expense.setText(String.valueOf(outp));
              //      Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == 8) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {


                    list2 = new Gson().fromJson(jsonObject.get("entyry_date").toString(), new TypeToken<ArrayList<BeanEntriesInfo>>() {
                    }.getType());


                //    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    void showDialog(final String[] array) {


        new MaterialDialog.Builder(this)
                .title("Select Category ")
                .items(array)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == -1) return true;
                        pos = which;
                        long in = 0;
                        long outp = 0;
                        long totl;
                        if(pos==0) {
                            btnDatePicker1.setVisibility(View.INVISIBLE);
                            btnDatePicker3.setVisibility(View.INVISIBLE);
                            btn.setVisibility(View.INVISIBLE);
                            btnDatePicker.setVisibility(View.INVISIBLE);

                            for (BeanEntriesInfo b : list1) {
                                if (role.equalsIgnoreCase("admin"))
                                {
                                    if (b.getIncoming().equals("yes")) {
                                        in = Long.parseLong(b.getAmount()) + in;
                                    } else if (b.getIncoming().equals("no")) {
                                        outp = (Long.parseLong(b.getAmount())) + outp;
                                    }
                                }
                                else {
                                    for (int j = 0; j < a.length; j++) {
                                        if (a[j].equalsIgnoreCase(b.getAccount())) {
                                            if (b.getIncoming().equals("yes")) {
                                                in = Long.parseLong(b.getAmount()) + in;
                                            } else if (b.getIncoming().equals("no")) {
                                                outp = (Long.parseLong(b.getAmount())) + outp;
                                            }
                                        }
                                    }
                                }

                            }
                           totl = in - outp;
                            total.setText(String.valueOf(totl));
                            income.setText(String.valueOf(in));
                            expense.setText(String.valueOf(outp));
                        }
                        if(pos==1) {
                            btnDatePicker1.setVisibility(View.INVISIBLE);
                            btnDatePicker3.setVisibility(View.INVISIBLE);
                            btn.setVisibility(View.INVISIBLE);
                            btnDatePicker.setVisibility(View.VISIBLE);

                             Snackbar.make(findViewById(R.id.main_frame),"please select date first", Snackbar.LENGTH_LONG).show();}

                        if(pos==2) {
                            btnDatePicker.setVisibility(View.INVISIBLE);
                            btnDatePicker1.setVisibility(View.VISIBLE);
                            btnDatePicker3.setVisibility(View.VISIBLE);
                            btn.setVisibility(View.VISIBLE);
                            Snackbar.make(findViewById(R.id.main_frame),"please select date first", Snackbar.LENGTH_LONG).show();

                        for (BeanEntriesInfo b : list2) {
                                {
                                    if(role.equalsIgnoreCase("admin")) {
                                        if (b.getIncoming().equals("yes")) {
                                            in = Long.parseLong(b.getAmount()) + in;
                                        } else if (b.getIncoming().equals("no")) {
                                            outp = (Long.parseLong(b.getAmount())) + outp;
                                        }
                                    }
                                    else
                                    {
                                        for (int j = 0; j < a.length; j++) {
                                            if (a[j].equalsIgnoreCase(b.getAccount())) {
                                                if (b.getIncoming().equals("yes")) {
                                                    in = Long.parseLong(b.getAmount()) + in;
                                                } else if (b.getIncoming().equals("no")) {
                                                    outp = (Long.parseLong(b.getAmount())) + outp;
                                                }
                                            }}}
                                }
                            }
                            totl = in - outp;
                            total.setText(String.valueOf(totl));
                            income.setText(String.valueOf(in));
                            expense.setText(String.valueOf(outp));
                        }

                        return true;

                    }
                })

                .show();

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
                            if(dayOfMonth<10&&(monthOfYear+1)<10)
                            btnDatePicker.setText(year + "-" + "0"+(monthOfYear + 1) + "-" + "0"+dayOfMonth);
                            else if(dayOfMonth<10)
                                btnDatePicker.setText(year + "-" + (monthOfYear + 1) + "-" + "0"+dayOfMonth);
                            else if((monthOfYear+1)<10)
                                btnDatePicker.setText(year + "-" +"0"+(monthOfYear + 1) + "-" + dayOfMonth);
                            else
                                btnDatePicker.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            long in = 0;
                            long outp = 0;
                            long totl;
                            for (BeanEntriesInfo b : list1) {
                                if (role.equalsIgnoreCase("admin")) {
                                    if (b.getDate().equals(btnDatePicker.getText().toString())) {
                                        if (b.getIncoming().equals("yes")) {
                                            in = Long.parseLong(b.getAmount()) + in;
                                        } else if (b.getIncoming().equals("no")) {
                                            outp = (Long.parseLong(b.getAmount())) + outp;
                                        }
                                    }
                                }
                                else
                                {
                                    for (int j = 0; j < a.length; j++) {
                                        if (a[j].equalsIgnoreCase(b.getAccount())) {
                                            if (b.getDate().equals(btnDatePicker.getText().toString())) {
                                                if (b.getIncoming().equals("yes")) {
                                                    in = Long.parseLong(b.getAmount()) + in;
                                                } else if (b.getIncoming().equals("no")) {
                                                    outp = (Long.parseLong(b.getAmount())) + outp;
                                                }
                                            }

                                        }}}
                            }
                            totl = in - outp;
                            total.setText(String.valueOf(totl));
                            income.setText(String.valueOf(in));
                            expense.setText(String.valueOf(outp));


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == btnDatePicker1) {

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

                            btnDatePicker1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            HashMap<String, String> hashMap1 = new HashMap<String, String>();
                            hashMap1.put("adminid", adminid);
                            hashMap1.put("from",btnDatePicker3.getText().toString());
                            hashMap1.put("to",btnDatePicker1.getText().toString());

                            VolleyHelper.postRequestVolley(Report.this, URL_API.Entriesacctodate, hashMap1, RequestCodes.Entriesacctodate, false);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == btnDatePicker3) {

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

                            btnDatePicker3.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == btn) {
            long in = 0;
            long outp = 0;
            long totl;
            for (BeanEntriesInfo b : list2) {
                {
                    if(role.equalsIgnoreCase("admin")) {
                        if (b.getIncoming().equals("yes")) {

                            in = Long.parseLong(b.getAmount()) + in;
                        } else if (b.getIncoming().equals("no")) {
                            outp = (Long.parseLong(b.getAmount())) + outp;
                        }
                    }
                    else
                    {
                        for (int j = 0; j < a.length; j++) {
                            if (a[j].equalsIgnoreCase(b.getAccount())) {
                                if (b.getIncoming().equals("yes")) {

                                    in = Long.parseLong(b.getAmount()) + in;
                                } else if (b.getIncoming().equals("no")) {
                                    outp = (Long.parseLong(b.getAmount())) + outp;
                                }
                            }}}
                }
            }
            totl = in - outp;
            total.setText(String.valueOf(totl));
            income.setText(String.valueOf(in));
            expense.setText(String.valueOf(outp));

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
}
