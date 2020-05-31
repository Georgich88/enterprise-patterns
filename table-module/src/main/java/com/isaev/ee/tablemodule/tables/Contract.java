package com.isaev.ee.tablemodule.tables;

import org.javamoney.moneta.Money;

import java.time.LocalDate;

public class Contract {

    private int id;
    private int productId;
    private LocalDate dateSigned;
    private Money revenue;

    public Contract(int contractId, int productId, LocalDate dateSigned, Money revenue) {
        this.id = contractId;
        this.productId = productId;
        this.dateSigned = dateSigned;
        this.revenue = revenue;
    }

    public int getId() {
        return id;
    }

    public void setId(int contractId) {
        this.id = contractId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(LocalDate dateSigned) {
        this.dateSigned = dateSigned;
    }

    public Money getRevenue() {
        return revenue;
    }

    public void setRevenue(Money revenue) {
        this.revenue = revenue;
    }
}
