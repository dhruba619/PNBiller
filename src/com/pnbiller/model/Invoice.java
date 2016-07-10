/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pnbiller.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author dhrubajyotib
 */
public class Invoice implements Serializable {

    private final String ENTERPRISE_NAME = "V's Restaurant";
    private final String ADDRESS = "Hinjewadi";
    private int invoiceId;
    private Customer customer;
    private List<Order> orderList;
    private double totalAmount;
    private double discount;
    private double serviceTax;
    private double netPayableAmount;
    private String tableNumner;

    public String getTableNumner() {
        return tableNumner;
    }

    public void setTableNumner(String tableNumner) {
        this.tableNumner = tableNumner;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public double getNetPayableAmount() {
        return netPayableAmount;
    }

    public void setNetPayableAmount(double netPayableAmount) {
        this.netPayableAmount = netPayableAmount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
