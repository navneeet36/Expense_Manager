package com.example.hp.expense_manager.pojo;

/**
 * Created by hp on 24-Jul-17.
 */

public class BeanAddUser {
    String adminid;
    String readonly;
    String writeonly;
    String name;
    String contact;

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    String accounts;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String userid;

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getWriteonly() {
        return writeonly;
    }

    public void setWriteonly(String writeonly) {
        this.writeonly = writeonly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
