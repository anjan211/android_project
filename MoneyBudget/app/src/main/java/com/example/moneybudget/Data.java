package com.example.moneybudget;

public class Data {

    String item, date, id, notes, payment, currency,ItemNday,ItemNweek,ItemNmonth;
    int amount, month, week;

    public Data() {
    }

    public Data(String item, String date, String id, String notes, String payment, String currency, String itemNday, String itemNweek, String itemNmonth, int amount, int month, int week) {
        this.item = item;
        this.date = date;
        this.id = id;
        this.notes = notes;
        this.payment = payment;
        this.currency = currency;
        ItemNday = itemNday;
        ItemNweek = itemNweek;
        ItemNmonth = itemNmonth;
        this.amount = amount;
        this.month = month;
        this.week = week;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItemNday() {
        return ItemNday;
    }

    public void setItemNday(String itemNday) {
        ItemNday = itemNday;
    }

    public String getItemNweek() {
        return ItemNweek;
    }

    public void setItemNweek(String itemNweek) {
        ItemNweek = itemNweek;
    }

    public String getItemNmonth() {
        return ItemNmonth;
    }

    public void setItemNmonth(String itemNmonth) {
        ItemNmonth = itemNmonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}

