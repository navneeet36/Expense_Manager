package com.example.hp.expense_manager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.hp.expense_manager.Adapter.EntryListAdapter;
import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.BeanAccountInfo;
import com.example.hp.expense_manager.pojo.BeanEntriesInfo;
import com.example.hp.expense_manager.pojo.EntryList;
import com.example.hp.expense_manager.utils.DividerItemDecoration;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hp on 19-Jul-17.
 */

public class Entries extends BaseActivity implements EntryListAdapter.OnItemClickListener {

    Toolbar toolbar;
    private ArrayList<String> arraySpinner;
    AppCompatSpinner acctoaccount, acctoentries;
    ArrayList<BeanAccountInfo> list;
    RecyclerView recyclerView;
    private String[] arraySpinner2;
    LinearLayoutManager linearLayoutManager;
    ArrayList<BeanEntriesInfo> list1 = new ArrayList<>();
    EntryListAdapter listAdapter;
    private String[] a;
    Button btn;

    TextView entre;
    String adminid, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        acctoaccount = (AppCompatSpinner) findViewById(R.id.acctoaccount);
        acctoentries = (AppCompatSpinner) findViewById(R.id.acctoentries);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn = (Button) findViewById(R.id.ok);
        entre = (TextView) findViewById(R.id.entre);
        setSupportActionBar(toolbar);
        // SwipeRefreshLayout mSwipe = (SwipeRefreshLayout).findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        entre.setText("All Entries :");
        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);
        adminid = editor.getString("adminid", "-1 ");
        role = editor.getString("role", "-1");
        String useraccounts = editor.getString("accounts", "-1");
        a = useraccounts.split(",");

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);
        VolleyHelper.postRequestVolley(Entries.this, URL_API.Entries, hashMap, RequestCodes.Entries, false);
        if (role.equalsIgnoreCase("admin")) {
            HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("adminid", adminid);
            VolleyHelper.postRequestVolley(Entries.this, URL_API.AddAccount, hashMap2, RequestCodes.AddAccount, false);
        }
        if(role.equalsIgnoreCase("user"))
        {

            MyCustomAdapter2 adapter = new MyCustomAdapter2(getApplicationContext(), R.layout.spinner_row, a);
            acctoaccount.setAdapter(adapter);
        }


        this.arraySpinner2 = new String[]{
                "All", "OnlyIncome", "OnlyExpense"
        };
        MyCustomAdapter2 adapter = new MyCustomAdapter2(this, R.layout.spinner_row, arraySpinner2);
        acctoentries.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entre.setText("Entries Acc. to Filter : ");
                if(role.equalsIgnoreCase("admin")) {
                    ArrayList<EntryList> arrayList = new ArrayList<>();

                    for (BeanEntriesInfo b : list1) {

                        if (arraySpinner2[acctoentries.getSelectedItemPosition()].equalsIgnoreCase("All")) {
                            if (acctoaccount.getSelectedItemPosition() < list.size()) {
                                if (b.getAccount().equalsIgnoreCase(list.get(acctoaccount.getSelectedItemPosition()).getAccountname())) {
                                    arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                }
                            }
                            if (acctoaccount.getSelectedItemPosition() == list.size()) {
                                arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                            }
                        }
                        if (arraySpinner2[acctoentries.getSelectedItemPosition()].equalsIgnoreCase("OnlyIncome")) {
                            if (b.getIncoming().equalsIgnoreCase("yes")) {
                                if (acctoaccount.getSelectedItemPosition() < list.size()) {
                                    if (b.getAccount().equalsIgnoreCase(list.get(acctoaccount.getSelectedItemPosition()).getAccountname()))
                                        arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                }
                                if (acctoaccount.getSelectedItemPosition() == list.size()) {
                                    arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                }
                            }
                        }
                        if (arraySpinner2[acctoentries.getSelectedItemPosition()].equalsIgnoreCase("OnlyExpense")) {

                            if (b.getOutgoing().equalsIgnoreCase("yes")) {
                                if (acctoaccount.getSelectedItemPosition() < list.size()) {
                                    if (b.getAccount().equalsIgnoreCase(list.get(acctoaccount.getSelectedItemPosition()).getAccountname()))
                                        arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                }
                                if (acctoaccount.getSelectedItemPosition() == list.size()) {
                                    arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                }
                            }
                        }
                    }

                    listAdapter = new EntryListAdapter(getApplication(), arrayList);
                    recyclerView.setAdapter(listAdapter);
                }
                else
                {
                    ArrayList<EntryList> arrayList = new ArrayList<>();

                    for (BeanEntriesInfo b : list1) {
                        for (int j = 0; j < a.length; j++) {
                            if (a[j].equalsIgnoreCase(b.getAccount())) {

                                if (arraySpinner2[acctoentries.getSelectedItemPosition()].equalsIgnoreCase("All")) {
                                    if (acctoaccount.getSelectedItemPosition() < a.length - 1) {
                                       if (b.getAccount().equalsIgnoreCase(a[acctoaccount.getSelectedItemPosition()])) {
                                            arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                        }
                                    }
                                    if (acctoaccount.getSelectedItemPosition() == a.length - 1) {
                                        arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                    }
                                }
                                if (arraySpinner2[acctoentries.getSelectedItemPosition()].equalsIgnoreCase("OnlyIncome")) {
                                    if (b.getIncoming().equalsIgnoreCase("yes")) {
                                        if (acctoaccount.getSelectedItemPosition() < a.length - 1) {
                                          if (b.getAccount().equalsIgnoreCase(a[acctoaccount.getSelectedItemPosition()]))
                                                arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                        }
                                        if (acctoaccount.getSelectedItemPosition() == a.length - 1) {
                                            arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                        }
                                    }
                                }
                                if (arraySpinner2[acctoentries.getSelectedItemPosition()].equalsIgnoreCase("OnlyExpense")) {

                                    if (b.getOutgoing().equalsIgnoreCase("yes")) {
                                        if (acctoaccount.getSelectedItemPosition() < a.length - 1) {
                                            if (b.getAccount().equalsIgnoreCase(a[acctoaccount.getSelectedItemPosition()]))
                                                arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                       }
                                        if (acctoaccount.getSelectedItemPosition() == a.length - 1) {
                                            arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                                        }
                                    }
                                }
                            }

                            listAdapter = new EntryListAdapter(getApplication(), arrayList);
                            recyclerView.setAdapter(listAdapter);

                        }
                    }}
            }
        });

    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);

    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);

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
                    ArrayList<EntryList> arrayList = new ArrayList<>();
                    if (role.equalsIgnoreCase("user")) {

                        for (BeanEntriesInfo b : list1) {
                            for (int j = 0; j < a.length; j++) {
                                if (a[j].equalsIgnoreCase(b.getAccount()))
                                    arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));
                            }
                        }
                    } else
                        for (BeanEntriesInfo b : list1) {

                            arrayList.add(new EntryList(b.getDate(), b.getAmount(), b.getAccount(), b.getIncoming(), b.getOutgoing(), b.getDescription(), b.getTime(), b.getEntryno(),b.getUsername()));

                        }

                    listAdapter = new EntryListAdapter(this, arrayList);
                    recyclerView.setAdapter(listAdapter);
                    listAdapter.setOnItemClickListener(this);

                    //      Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 3) {
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
                    arraySpinner.add(list.size(), "All");
                    Entries.MyCustomAdapter adapter = new Entries.MyCustomAdapter(getApplicationContext(), R.layout.spinner_row, arraySpinner);
                    acctoaccount.setAdapter(adapter);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        } else if (requestCode == 14) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {

                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEventItemClick(EntryList item) {
        Intent intent = new Intent(Entries.this, Update.class);
        intent.putExtra("entryno", item.getEntryno());
        startActivity(intent);

    }

    //delete
    @Override
    public void EventItemClick(EntryList item) {
        //     mainActivity.setFragment(UpdateStudent.newInstance(item.getRollno()));
        HashMap<String, String> hashMap3 = new HashMap<String, String>();
        hashMap3.put("entryno", item.getEntryno());

        VolleyHelper.postRequestVolley(Entries.this, URL_API.Delete, hashMap3, RequestCodes.Delete, false);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adminid", adminid);
        VolleyHelper.postRequestVolley(Entries.this, URL_API.Entries, hashMap, RequestCodes.Entries, false);


    }

    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
        }

    }

    public class MyCustomAdapter2 extends ArrayAdapter<String> {

        public MyCustomAdapter2(Context context, int textViewResourceId,
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (role.equalsIgnoreCase("user"))
            startActivity(UserPage.class);
        else

            startActivity(AdminPanel.class);

    }

}