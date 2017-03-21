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
    private int numberOfOccupants;
    private boolean isDeleted;

//    public UtilityModel(Company companyName){
//        this.companyName = companyName;
//        name = new String();
//        billingPeriodInDays = 0;
//        totalEnergyConsumptionInGJ = 0;
//        numberOfOccupants = 0;
//        isDeleted = false;
//    }

    public UtilityModel(long id,
                        Company company,
                        String name,
                        int billingPeriodInDays,
                        double totalEnergyConsumptionInGJ,
                        int numberOfOccupants,
                        boolean isDeleted){
        this.id = id;
        this.companyName = company;
        this.name = name;
        this.billingPeriodInDays = billingPeriodInDays;
        this.totalEnergyConsumptionInGJ = totalEnergyConsumptionInGJ;
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
        return calculateTotalEmissions();
    }

    public double calculateDailyEnergyConsumptionInGJ() {
        return totalEnergyConsumptionInGJ / billingPeriodInDays;
    }

    public double calculateDailyCO2EmissionsInKg() {
        return calculateTotalEmissions() / billingPeriodInDays;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public double calculateTotalEmissions(){
        double totalCO2EmissionsInKg;
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
        return totalCO2EmissionsInKg;
    }

    public double getTotalEmissionsPerOccupant() {
        return  calculateTotalEmissions()/numberOfOccupants;
    }

    public double getTotalEnergyConsumptionPerOccupant() {
        return totalEnergyConsumptionInGJ / numberOfOccupants;
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
                ", numberOfOccupants=" + numberOfOccupants +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
