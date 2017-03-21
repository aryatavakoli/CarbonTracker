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
    private double totalEnergyConsumptionInGWh;
    private int numberOfOccupants;
    private boolean isDeleted;
    //TODO: Adding startData and endData

    public UtilityModel(long id,
                        Company company,
                        String name,
                        int billingPeriodInDays,
                        double totalEnergyConsumptionInGWh,
                        int numberOfOccupants,
                        boolean isDeleted){
        this.id = id;
        this.companyName = company;
        this.name = name;
        this.billingPeriodInDays = billingPeriodInDays;
        this.totalEnergyConsumptionInGWh = totalEnergyConsumptionInGWh;
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

    public double getTotalEnergyConsumption() {
        return totalEnergyConsumptionInGWh;
    }

    public void setTotalEnergyConsumption(double totalEnergyConsumptionInGWh) {
        this.totalEnergyConsumptionInGWh = totalEnergyConsumptionInGWh;
    }

    public double getTotalEnergyConsumptionInGJ() {
        //convert to GJ
        double totalEnergyConsumptionInGJ = 0;
        switch (companyName) {
            //dont need to do anything
            case FORTISBC:
                totalEnergyConsumptionInGJ = totalEnergyConsumptionInGWh;
                break;
            //convert to GJ
            case BCHYDRO:
                totalEnergyConsumptionInGJ = totalEnergyConsumptionInGWh * CONVERTOGIGAJOULES;
                break;
        }
        return totalEnergyConsumptionInGJ;
    }

    public double getTotalCO2EmissionsInKg() {
        return calculateTotalEmissions();
    }

    public double calculateDailyEnergyConsumptionInGJ() {
        return getTotalEnergyConsumptionInGJ() / billingPeriodInDays;
    }

    public double calculateDailyCO2EmissionsInKg() {
        return calculateTotalEmissions() / billingPeriodInDays;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public double calculateTotalEmissions(){
        double totalCO2EmissionsInKg;
        switch (companyName){
            case BCHYDRO:
                totalCO2EmissionsInKg = ELECTRIFY_FOOTPRINT_KG_PER_GJ * getTotalEnergyConsumptionInGJ();
                break;
            case FORTISBC:
                totalCO2EmissionsInKg = GAS_FOOTPRINT_KG_PER_GJ * getTotalEnergyConsumptionInGJ();
                break;
            default:
                totalCO2EmissionsInKg = 0;
                break;
        }
        return totalCO2EmissionsInKg;
    }

    public double getTotalEmissionsPerOccupant() {
        return  calculateTotalEmissions()/numberOfOccupants;
    }

    public double getTotalEnergyConsumptionPerOccupant() {
        return totalEnergyConsumptionInGWh / numberOfOccupants;
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
                ", totalEnergyConsumptionInGJ=" + totalEnergyConsumptionInGWh +
                ", numberOfOccupants=" + numberOfOccupants +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
