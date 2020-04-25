package com.isaev.ee.tablemodule.tables;

import org.javamoney.moneta.Money;

import java.time.LocalDate;

public class RevenueRecognition {

    private int contractId;
    private LocalDate dateRecognition;
    private Money amount;

    public RevenueRecognition(int contractId, LocalDate dateRecognition, Money amount) {
        this.contractId = contractId;
        this.dateRecognition = dateRecognition;
        this.amount = amount;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public LocalDate getDateRecognition() {
        return dateRecognition;
    }

    public void setDateRecognition(LocalDate dateRecognition) {
        this.dateRecognition = dateRecognition;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }
}
