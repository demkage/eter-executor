package com.eter.executor.domain;

import java.io.Serializable;

/**
 * Created by rusifer on 5/21/17.
 */
public class SaleResult implements Serializable{
    private int dayOfWeek;
    private int dayOfMonth;
    private double sales;

    public SaleResult() {
    }

    public SaleResult(int dayOfWeek, int dayOfMonth, double sales) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.sales = sales;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }
}
