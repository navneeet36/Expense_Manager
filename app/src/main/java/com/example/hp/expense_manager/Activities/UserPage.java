package com.example.hp.expense_manager.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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



class UserPage extends BaseActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    int columns = 2;
    TextView income, expense, total, user;
    ArrayList<BeanEntriesInfo> list1 = new ArrayList<>();

    long in = 0;
    long outp = 0;
    long totl;
    String adminid, readonly, writeonly, userid;
    private String[] a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        income = (TextView) findViewById(R.id.Income);
        expense = (TextView) findViewById(R.id.Expense);
        total = (TextView) findViewById(R.id.Total);
        user = (TextView) findViewById(R.id.userid);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new GridLayoutManager(this, columns);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);
        adminid = editor.getString("adminid", "-1 ");
        readonly = editor.getString("readonly", "-1");
        writeonly = editor.getString("writeonly", "-1");
        userid = editor.getString("userid", "-1");
        String useraccounts = editor.getString("accounts", "-1");
        a = useraccounts.split(",");
        user.setText(userid);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);

        System.out.println(readonly);
        VolleyHelper.postRequestVolley(UserPage.this, URL_API.Entries, hashMap, RequestCodes.Entries, false);


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
        if (writeonly.equalsIgnoreCase("yes")) {
            arrayList.add(new AdminItem("Add Income", AddIncome.class, R.drawable.ic_action_new));
            //if (accesses.getGuestMgmt() == 1)
            arrayList.add(new AdminItem("Add Expense", AddExpense.class, R.drawable.ic_action_new));
        }
        if (readonly.equalsIgnoreCase("yes")) {
            // if (accesses.getTableMgmt() == 1)
            arrayList.add(new AdminItem("Entries", Entries.class, R.drawable.ic_create_white_24dp));
            // if (accesses.getMenuMgmt() == 1)
            arrayList.add(new AdminItem("Report", Report.class, R.drawable.ic_create_white_24dp));
        }

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
                    totl = in - outp;
                    total.setText("$"+String.valueOf(totl));
                    income.setText("$"+String.valueOf(in));
                    expense.setText("$"+String.valueOf(outp));
                    //    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(LOGIN.class);

    }

}

