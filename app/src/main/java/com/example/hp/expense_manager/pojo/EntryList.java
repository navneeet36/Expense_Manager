package com.example.hp.expense_manager.pojo;

/**
 * Created by hp on 22-Jul-17.
 */

public class EntryList {
    String date;
    String amount;
    String account;
    String incoming;
    String outgoing;
    String description;
    String time;
    String entryno;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;

    public String getEntryno() {
        return entryno;
    }

    public void setEntryno(String entryno) {
        this.entryno = entryno;
    }

    public EntryList(String date, String amount, String account, String incoming, String outgoing, String description, String time,String entryno,String username) {
        this.date = date;
        this.amount = amount;
        this.account = account;
        this.incoming = incoming;
        this.outgoing = outgoing;
        this.description = description;
        this.time = time;
        this.entryno=entryno;
        this.username=username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming;
    }

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
