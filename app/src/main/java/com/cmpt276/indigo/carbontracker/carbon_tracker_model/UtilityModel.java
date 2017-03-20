package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

/**
 * Created by Arya on 2017-03-16.
 */

public class UtilityModel implements CarbonFootprintComponent {
    public static final double ELECTRIFY_FOOTPRINT_KG_PER_GWH = 9000;
    public static final double GAS_FOOTPRINT_KG_PER_GWH = 185760; // 51.6 Kg/Gj

    public enum Company{
        BCHYDRO,
        FORTISBC
    }

    private long id;
    private Company companyName;
    private String name;
    private long billingPeriodInDay;
    private double totalEnergyConsumptionInGWH;
    private double totalCO2EmissionsInKg;
    private double dailyEnergyConsumptionInGWH;
    private double dailyCO2EmissionsInKg;
    private double totalEmissionsPerOccupant;
    private double totalEnergyConsumptionPerOccupant;
    private int numberOfOccupants;
    private boolean isDeleted;

    public UtilityModel(Company companyName){
        this.companyName = companyName;
        name = new String();
        billingPeriodInDay = 0;
        totalEnergyConsumptionInGWH = 0;
        totalCO2EmissionsInKg = 0;
        dailyEnergyConsumptionInGWH = 0;
        dailyCO2EmissionsInKg = 0;
        totalEmissionsPerOccupant = 0;
        totalEnergyConsumptionPerOccupant = 0;
        isDeleted = false;
    }

    public UtilityModel(long id, Company company, String name, long billingPeriodInDay, double totalEnergyConsumptionInGWH, double totalCO2EmissionsInKg, double dailyEnergyConsumptionInGWH, double dailyCO2EmissionsInKg, int numberOfOccupants, boolean isDeleted){
        this.id = id;
        this.companyName = company;
        this.name = name;
        this.billingPeriodInDay = billingPeriodInDay;
        this.totalCO2EmissionsInKg = totalCO2EmissionsInKg;
        this.totalEnergyConsumptionInGWH =totalEnergyConsumptionInGWH;
        this.dailyEnergyConsumptionInGWH = dailyEnergyConsumptionInGWH;
        this.dailyCO2EmissionsInKg = dailyCO2EmissionsInKg;
        this.numberOfOccupants = numberOfOccupants;
        this.isDeleted = isDeleted;
    }

    public Company getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Company companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBillingPeriodInDays() {
        return billingPeriodInDay;
    }

    public void setBillingPeriodInDays(long billingPeriodInDays) {
        this.billingPeriodInDay = billingPeriodInDays;
    }

    public double getTotalEnergyConsumptionInGWH() {
        return totalEnergyConsumptionInGWH;
    }

    public void setTotalEnergyConsumptionInGWH(double totalEnergyConsumptionInGWH) {
        this.totalEnergyConsumptionInGWH = totalEnergyConsumptionInGWH;
    }

    public double getTotalCO2EmissionsInKg() {
        calculateTotalEmissions();
        return totalCO2EmissionsInKg;
    }

    public void setTotalCO2EmissionsInKg(double totalCO2EmissionsInKg) {
        this.totalCO2EmissionsInKg = totalCO2EmissionsInKg;
    }

    public double getDailyEnergyConsumptionInGWH() {
        dailyCO2EmissionsInKg = totalEnergyConsumptionInGWH /billingPeriodInDay;
        return dailyEnergyConsumptionInGWH;
    }

    public void setDailyEnergyConsumptionInGWH(double dailyEnergyConsumptionInGWH) {
        this.dailyEnergyConsumptionInGWH = dailyEnergyConsumptionInGWH;
    }

    public double getDailyCO2EmissionsInKg() {
        dailyCO2EmissionsInKg = totalCO2EmissionsInKg / billingPeriodInDay;
        return dailyCO2EmissionsInKg;
    }

    public void setDailyCO2EmissionsInKg(double dailyCO2EmissionsInKg) {
        this.dailyCO2EmissionsInKg = dailyCO2EmissionsInKg;
    }

    public boolean getIsDeleted() {
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

    public void calculateTotalEmissionsPerOccupant(){
        totalEmissionsPerOccupant = totalEmissionsPerOccupant/numberOfOccupants;
    }

    public void calculateTotalEnergyConsumptionPerOccupant(){
        totalEnergyConsumptionPerOccupant = dailyCO2EmissionsInKg/numberOfOccupants;
    }

    public double getTotalEmissionsPerOccupant() {
        return totalEmissionsPerOccupant;
    }

    public void setTotalEmissionsPerOccupant(double totalEmissionsPerOccupant) {
        this.totalEmissionsPerOccupant = totalEmissionsPerOccupant;
    }

    public double getTotalEnergyConsumptionPerOccupant() {
        return totalEnergyConsumptionPerOccupant;
    }

    public void setTotalEnergyConsumptionPerOccupant(double totalEnergyConsumptionPerOccupant) {
        this.totalEnergyConsumptionPerOccupant = totalEnergyConsumptionPerOccupant;
    }

    public int getNumberOfOccupants() {
        return numberOfOccupants;
    }

    public void setNumberOfOccupants(int numberOfOccupants) {
        this.numberOfOccupants = numberOfOccupants;


    }

    @Override
    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        if (this == other){
            return true;
        }
        if (!(other instanceof UtilityModel)){
            return false;
        }
        UtilityModel o = (UtilityModel) other;
        // a deleted object should never be compared to other components
        if(o.isDeleted || this.isDeleted){
            return false;
        }
        return this.name.equals(o.name);
    }

    @Override
    public String toString() {
        return "UtilityModel{" +
                "companyName=" + companyName +
                ", name='" + name + '\'' +
                ", billingPeriodInDays=" + billingPeriodInDay +
                ", totalEnergyConsumptionInGWH=" + totalEnergyConsumptionInGWH +
                ", totalCO2EmissionsInKg=" + totalCO2EmissionsInKg +
                ", dailyEnergyConsumptionInGWH=" + dailyEnergyConsumptionInGWH +
                ", dailyCO2EmissionsInKg=" + dailyCO2EmissionsInKg +
                ", numberOfOccupants=" + numberOfOccupants +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
