package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.util.Calendar;

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
    private double totalEnergyConsumptionInGWh;
    private int numberOfOccupants;
    private boolean isDeleted;
    private Calendar startDate;
    private Calendar endDate;

    public UtilityModel(long id,
                        Company company,
                        String name,
                        double totalEnergyConsumptionInGWh,
                        int numberOfOccupants,
                        Calendar startDate,
                        Calendar endDate,
                        boolean isDeleted){
        setId(id);
        setCompanyName(company);
        setName(name);
        setTotalEnergyConsumption(totalEnergyConsumptionInGWh);
        setNumberOfOccupants(numberOfOccupants);
        setStartDate(startDate);
        setEndDate(endDate);
        setIsDeleted(isDeleted);
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
        return getTotalEnergyConsumptionInGJ() / calculateBillingPeriodInDays();
    }

    public double calculateDailyCO2EmissionsInKg() {
        return calculateTotalEmissions() / calculateBillingPeriodInDays();
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate(){
        return endDate;
    }

    public void setEndDate(Calendar endDay) {
        this.endDate = endDay;
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

    //TODO: find shorter way for duration. It seams the formula is not correct
    public int calculateBillingPeriodInDays(){
        int duration = ((endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR)) * 360) + (
                (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH)) * 30
        ) + (endDate.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH));
        return duration;
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
                ", totalEnergyConsumptionInGJ=" + totalEnergyConsumptionInGWh +
                ", numberOfOccupants=" + numberOfOccupants +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
