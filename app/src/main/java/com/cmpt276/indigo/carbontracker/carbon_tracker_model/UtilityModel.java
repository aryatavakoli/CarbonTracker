package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

/**
 * Created by Arya on 2017-03-16.
 */

public class UtilityModel implements CarbonFootprintComponent {
    public static final double ELECTRIFY_FOOTPRINT_KG_PER_GJ = 2.5; //9000 Kg/GwH
    public static final double GAS_FOOTPRINT_KG_PER_GJ = 51.6; // 51.6 Kg/Gj
    public static final int CONVERTOGIGAJOULES = 3600;

    public enum Company{
        BCHYDRO,
        FORTISBC
    }

    private long id;
    private Company companyName;
    private String name;
    private int billingPeriodInDays;
    private double totalEnergyConsumptionInGJ;
    private double totalCO2EmissionsInKg;
    private double dailyEnergyConsumptionInGJ;
    private double dailyCO2EmissionsInKg;
    private double totalEmissionsPerOccupant;
    private double totalEnergyConsumptionPerOccupant;
    private int numberOfOccupants;
    private boolean isDeleted;

    public UtilityModel(Company companyName){
        this.companyName = companyName;
        name = new String();
        billingPeriodInDays = 0;
        totalEnergyConsumptionInGJ = 0;
        totalCO2EmissionsInKg = 0;
        dailyEnergyConsumptionInGJ = 0;
        dailyCO2EmissionsInKg = 0;
        totalEmissionsPerOccupant = 0;
        totalEnergyConsumptionPerOccupant = 0;
        numberOfOccupants = 0;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return billingPeriodInDays;
    }

    public void setBillingPeriodInDays(int billingPeriodInDays) {
        this.billingPeriodInDays = billingPeriodInDays;
    }

    public double getTotalEnergyConsumptionInGJ() {
        return totalEnergyConsumptionInGJ;
    }

    public void setTotalEnergyConsumptionInGJ(double totalEnergyConsumptionInGJ) {
        //convert to GJ
        switch (companyName) {
            //dont need to do anything
            case FORTISBC:
                this.totalEnergyConsumptionInGJ = totalEnergyConsumptionInGJ;
                break;
            //convert to GJ
            case BCHYDRO:
                this.totalEnergyConsumptionInGJ = totalEnergyConsumptionInGJ * CONVERTOGIGAJOULES;
                break;
        }
    }

    public double getTotalCO2EmissionsInKg() {
        calculateTotalEmissions();
        return totalCO2EmissionsInKg;
    }

    public void setTotalCO2EmissionsInKg(double totalCO2EmissionsInKg) {
        this.totalCO2EmissionsInKg = totalCO2EmissionsInKg;
    }

    public double getDailyEnergyConsumptionInGJ() {
        dailyEnergyConsumptionInGJ = totalEnergyConsumptionInGJ / billingPeriodInDays;
        return dailyEnergyConsumptionInGJ;
    }

    public void setDailyEnergyConsumptionInGJ(double dailyEnergyConsumptionInGJ) {
        this.dailyEnergyConsumptionInGJ = dailyEnergyConsumptionInGJ;
    }

    public double getDailyCO2EmissionsInKg() {
        dailyCO2EmissionsInKg = totalCO2EmissionsInKg / billingPeriodInDays;
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
                totalCO2EmissionsInKg = ELECTRIFY_FOOTPRINT_KG_PER_GJ * totalEnergyConsumptionInGJ;
                break;
            case FORTISBC:
                totalCO2EmissionsInKg = GAS_FOOTPRINT_KG_PER_GJ * totalEnergyConsumptionInGJ;
                break;
            default:
                totalCO2EmissionsInKg = 0;
                break;
        }
    }

    public double getTotalEmissionsPerOccupant() {
        totalEmissionsPerOccupant =  totalCO2EmissionsInKg/numberOfOccupants;
        return totalEmissionsPerOccupant;
    }

    public void setTotalEmissionsPerOccupant(double totalEmissionsPerOccupant) {
        this.totalEmissionsPerOccupant = totalEmissionsPerOccupant;
    }

    public double getTotalEnergyConsumptionPerOccupant() {
        totalEnergyConsumptionPerOccupant = totalEnergyConsumptionInGJ / numberOfOccupants;
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
                ", billingPeriodInDays=" + billingPeriodInDays +
                ", totalEnergyConsumptionInGJ=" + totalEnergyConsumptionInGJ +
                ", totalCO2EmissionsInKg=" + totalCO2EmissionsInKg +
                ", dailyEnergyConsumptionInGJ=" + dailyEnergyConsumptionInGJ +
                ", dailyCO2EmissionsInKg=" + dailyCO2EmissionsInKg +
                ", numberOfOccupants=" + numberOfOccupants +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
