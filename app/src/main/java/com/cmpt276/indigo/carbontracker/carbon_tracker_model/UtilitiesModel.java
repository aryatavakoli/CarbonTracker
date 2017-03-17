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
    private double totalEnergyConsumptionInGWH;
    private double totalCO2EmissionsInKg;
    private double dailyEnergyConsumptionInGWH;
    private double dailyCO2EmissionsInKg;
    private boolean isDeleted;

    public UtilitiesModel(Company companyName){
        this.companyName = companyName;
        nickName = new String();
        billingPeriodInDays = 0;
        totalEnergyConsumptionInGWH = 0;
        totalCO2EmissionsInKg = 0;
        dailyEnergyConsumptionInGWH = 0;
        dailyCO2EmissionsInKg = 0;
        isDeleted = false;
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

    public double getTotalEnergyConsumptionInGWH() {
        return totalEnergyConsumptionInGWH;
    }

    public void setTotalEnergyConsumptionInGWH(double totalEnergyConsumptionInGWH) {
        this.totalEnergyConsumptionInGWH = totalEnergyConsumptionInGWH;
    }

    public double getTotalCO2EmissionsInKg() {
        return totalCO2EmissionsInKg;
    }

    public void setTotalCO2EmissionsInKg(double totalCO2EmissionsInKg) {
        this.totalCO2EmissionsInKg = totalCO2EmissionsInKg;
    }

    public double getDailyEnergyConsumptionInGWH() {
        dailyCO2EmissionsInKg = totalEnergyConsumptionInGWH /billingPeriodInDays;
        return dailyEnergyConsumptionInGWH;
    }

    public void setDailyEnergyConsumptionInGWH(double dailyEnergyConsumptionInGWH) {
        this.dailyEnergyConsumptionInGWH = dailyEnergyConsumptionInGWH;
    }

    public double getDailyCO2EmissionsInKg() {
        dailyCO2EmissionsInKg = totalCO2EmissionsInKg / billingPeriodInDays;
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

    public void calculateTotalEmissions(){
        switch (companyName){
            case BCHYDRO:
                totalCO2EmissionsInKg = ELECTRIFY_FOOTPRINT_KG_PER_GWH * totalEnergyConsumptionInGWH;
                break;
            case FORTISBC:
                totalCO2EmissionsInKg = GAS_FOOTPRINT_KG_PER_GWH * totalEnergyConsumptionInGWH;
                break;
            default:
                totalCO2EmissionsInKg = 0;
                break;
        }
    }
}
