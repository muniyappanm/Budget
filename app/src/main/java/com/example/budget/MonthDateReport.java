package com.example.budget;

public class MonthDateReport {
    String month;
    int expense;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public MonthDateReport(String month, int expense) {
        this.month = month;
        this.expense = expense;
    }
}
