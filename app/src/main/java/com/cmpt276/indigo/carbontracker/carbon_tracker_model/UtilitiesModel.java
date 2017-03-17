package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

/**
 * Created by Arya on 2017-03-16.
 */

public class UtilitiesModel implements CarbonFootprintComponent {
    public static final double ELECTRIFY_FOOTPRINT_KG_PER_GWH = 9000;
    public static final double GAS_FOOTPRINT_KG_PER_GWH = 185760; // 51.6 Kg/Gj

    public enum Company{
        BCHYDRO,
        FORTISBC
    }

    private Company companyName;
    private String nickName;
    private long billingPeriodInDays;
    private double monthlyEnergyConsumptionInGWH;
    private double monthlyCO2EmissionsInKg;
    private double dailyEnergyConsumptionInGWH;
    private double dailyCO2EmissionsInKg;
    private boolean isDeleted;

    public UtilitiesModel(Company companyName){
        this.companyName = companyName;
        nickName = new String();
        billingPeriodInDays = 0;
        monthlyEnergyConsumptionInGWH = 0;
        monthlyCO2EmissionsInKg = 0;
    }

    public Company getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Company companyName) {
        this.companyName = companyName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getBillingPeriodInDays() {
        return billingPeriodInDays;
    }

    public void setBillingPeriodInDays(long billingPeriodInDays) {
        this.billingPeriodInDays = billingPeriodInDays;
    }

    public double getMonthlyEnergyConsumptionInGWH() {
        return monthlyEnergyConsumptionInGWH;
    }

    public void setMonthlyEnergyConsumptionInGWH(double monthlyEnergyConsumptionInGWH) {
        this.monthlyEnergyConsumptionInGWH = monthlyEnergyConsumptionInGWH;
    }

    public double getMonthlyCO2EmissionsInKg() {
        return monthlyCO2EmissionsInKg;
    }

    public void setMonthlyCO2EmissionsInKg(double monthlyCO2EmissionsInKg) {
        this.monthlyCO2EmissionsInKg = monthlyCO2EmissionsInKg;
    }

    public double getDailyEnergyConsumptionInGWH() {
        dailyCO2EmissionsInKg = monthlyEnergyConsumptionInGWH/billingPeriodInDays;
        return dailyEnergyConsumptionInGWH;
    }

    public void setDailyEnergyConsumptionInGWH(double dailyEnergyConsumptionInGWH) {
        this.dailyEnergyConsumptionInGWH = dailyEnergyConsumptionInGWH;
    }

    public double getDailyCO2EmissionsInKg() {
        dailyCO2EmissionsInKg = monthlyCO2EmissionsInKg / billingPeriodInDays;
        return dailyCO2EmissionsInKg;
    }

    public void setDailyCO2EmissionsInKg(double dailyCO2EmissionsInKg) {
        this.dailyCO2EmissionsInKg = dailyCO2EmissionsInKg;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void calculateMonthlyEmissions(){
        switch (companyName){
            case BCHYDRO:
                monthlyCO2EmissionsInKg = ELECTRIFY_FOOTPRINT_KG_PER_GWH * monthlyEnergyConsumptionInGWH;
                break;
            case FORTISBC:
                monthlyCO2EmissionsInKg = GAS_FOOTPRINT_KG_PER_GWH * monthlyEnergyConsumptionInGWH;
                break;
            default:
                monthlyCO2EmissionsInKg = 0;
                break;
        }
    }
}
