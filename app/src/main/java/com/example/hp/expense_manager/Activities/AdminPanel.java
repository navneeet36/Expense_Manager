package com.example.hp.expense_manager.Activities;

/**
 * Created by hp on 15-Jul-17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.hp.expense_manager.Adapter.AdminAdapter;
import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.AdminItem;
import com.example.hp.expense_manager.pojo.BeanEntriesInfo;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminPanel extends BaseActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    int columns = 2;
    TextView income, expense, total;
    ArrayList<BeanEntriesInfo> list1 = new ArrayList<>();
    FloatingActionButton fab;

    long in = 0;
    long outp = 0;
    long totl;
    String adminid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        income = (TextView) findViewById(R.id.Income);
        expense = (TextView) findViewById(R.id.Expense);
        total = (TextView) findViewById(R.id.Total);
       fab = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new GridLayoutManager(this, columns);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);
        adminid = editor.getString("adminid", "-1 ");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);


        VolleyHelper.postRequestVolley(AdminPanel.this, URL_API.Entries, hashMap, RequestCodes.Entries, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AdminPanel.this, Adduser.class);
                in.putExtra("adminid", adminid);
                startActivity(in);
            }
        });


        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int screen_width = recyclerView.getWidth();
                float dptopx = getResources().getDimension(R.dimen.column);
                columns = (int) (screen_width / dptopx);
                if (linearLayoutManager != null)
                    linearLayoutManager.setSpanCount(columns);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        ArrayList<AdminItem> arrayList = new ArrayList<>();
        //  if (accesses.getOrderMgmt() == 1)
        arrayList.add(new AdminItem("Add Income", AddIncome.class, R.drawable.ic_action_new));
        //if (accesses.getGuestMgmt() == 1)
        arrayList.add(new AdminItem("Add Expense", AddExpense.class, R.drawable.ic_action_new));
        // if (accesses.getTableMgmt() == 1)
        arrayList.add(new AdminItem("Entries", Entries.class, R.drawable.ic_create_white_24dp));
        // if (accesses.getMenuMgmt() == 1)
        arrayList.add(new AdminItem("Report", Report.class, R.drawable.ic_create_white_24dp));


        AdminAdapter adminadapter = new AdminAdapter(this, arrayList);
        recyclerView.setAdapter(adminadapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  addcredits.setText("Credits: Loading..");
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
                        if (b.getIncoming().equals("yes")) {
                            in = Long.parseLong(b.getAmount()) + in;
                        } else if (b.getIncoming().equals("no")) {
                            outp = (Long.parseLong(b.getAmount())) + outp;
                        }
                    }
                    totl = in - outp;
                    total.setText("$"+String.valueOf(totl));
                    income.setText("$"+String.valueOf(in));
                    expense.setText("$"+String.valueOf(outp));
                    //     Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
                //else
//                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   getMenuInflater().inflate(R.menu.dashboard_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(LOGIN.class);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
