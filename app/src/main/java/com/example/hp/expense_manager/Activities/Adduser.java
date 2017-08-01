package com.example.hp.expense_manager.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.BeanAccountInfo;
import com.example.hp.expense_manager.pojo.BeanAddUser;
import com.example.hp.expense_manager.utils.RequestCodes;
import com.example.hp.expense_manager.utils.URL_API;
import com.example.hp.expense_manager.utils.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Adduser extends BaseActivity {
    EditText name, contact;
    CheckBox read, write;
    String readonly, writeonly,accounts="All";
    Button btn;
    TextView select;
    ArrayList<BeanAccountInfo> list;
    private ArrayList<Integer> accPos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText) findViewById(R.id.fName);
        select = (TextView) findViewById(R.id.selectaccounts);
        contact = (EditText) findViewById(R.id.contact);
        read = (CheckBox) findViewById(R.id.checkBox);
        write = (CheckBox) findViewById(R.id.checkBox2);
        btn = (Button) findViewById(R.id.btn);
        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(this);

        final String adminid = editor.getString("adminid", "-1 ");
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("adminid", adminid);

                VolleyHelper.postRequestVolley(Adduser.this, URL_API.AddAccount, hashMap, RequestCodes.AddAccount, false);

            }
        });

        //   Log.d("d3df",readonly);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (read.isChecked())
                    readonly = "yes";
                else
                    readonly = "no";

                if (write.isChecked())
                    writeonly = "yes";
                else
                    writeonly = "no";
                for(int j=0;j<list.size();j++)
                {
                    if (accPos.contains(j))
                   accounts= list.get(j).getAccountname()+","+accounts;
                }
                HashMap<String, String> hashMap1 = new HashMap<String, String>();
                BeanAddUser b = new BeanAddUser();
                b.setAdminid(adminid);
                b.setName(name.getText().toString());
                b.setContact(contact.getText().toString());
                b.setReadonly(readonly);
                b.setWriteonly(writeonly);
                b.setAccounts(accounts);


                Gson gson = new Gson();
                hashMap1.put("data", gson.toJson(b));
                VolleyHelper.postRequestVolley(Adduser.this, URL_API.Adduser, hashMap1, RequestCodes.Adduser, false);


            }
        });
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        if (requestCode == 9) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    HashMap<String, String> hashMap1 = new HashMap<String, String>();

                    hashMap1.put("contact", contact.getText().toString());


                    VolleyHelper.postRequestVolley(Adduser.this, URL_API.Userid, hashMap1, RequestCodes.Userid, false);

                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 10) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {
                    BeanAddUser s = new Gson().fromJson(jsonObject.getString("ot"), BeanAddUser.class);

                    Toast.makeText(this, "userid : " + s.getUserid() + " used in signup of user", Toast.LENGTH_LONG).show();


                } else
                    Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 3) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int i = jsonObject.getInt("success");
                if (i == 1) {
                    list = new Gson().fromJson(jsonObject.get("accounts").toString(), new TypeToken<ArrayList<BeanAccountInfo>>() {
                    }.getType());
                    showDialog(list);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminPanel.class);

    }

    void showDialog(final ArrayList<BeanAccountInfo> list) {
        final ArrayList<String> students = new ArrayList<>();
        for (BeanAccountInfo info : list)
            students.add(info.getAccountname());
        new MaterialDialog.Builder(this)
                .title("Select accounts")
                .items(students)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        accPos.clear();
                        accPos.addAll(Arrays.asList(which));
                        return true;
                    }
                })
                .positiveText("done")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //    si=new ArrayList<BeanStudentSemInfo>();
                    }
                })
                .show();
    }
}
