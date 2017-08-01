package com.example.hp.expense_manager.pojo;

/**
 * Created by hp on 19-Jul-17.
 */

public class AdminItem {
    String title;
    Class action;
    int icon;

    public AdminItem(String title, Class action, int icon) {
        this.title = title;
        this.icon = icon;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getAction() {
        return action;
    }
}

