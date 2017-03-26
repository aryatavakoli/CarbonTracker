package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arya on 2017-03-16.
 * Implements Utility Model
 */

public class UtilityModel implements CarbonFootprintComponent {
    public static final String DATE_FORMAT = "yyyy-MMM-dd";
    public static final double ELECTRICITY_FOOTPRINT_KG_PER_KWH = 0.009; // 9000; kg/Gwh //2.5 Kg/GJ
    public static final double GAS_FOOTPRINT_KG_PER_GJ = 51.6; // 51.6 Kg/Gj
    public static final double CONVERT_KWH_TO_GJ = 0.0036; // 1Kwh = 0.0036 GJ

    public enum Company{
        BCHYDRO,
        FORTISBC
    }

    public static Company IntToCompany(int number) {
        if(number == 0){
            return Company.BCHYDRO;
        }
        else if(number == 1) {
            return Company.FORTISBC;
        }
        return Company.BCHYDRO;
    }

    public static int CompanyToInt(Company company) {
        if(company == Company.BCHYDRO){
            return 0;
        }
        else if(company == Company.FORTISBC) {
            return 1;
        }
        return -1;
    }

    private long id; //index of the utility
    private Company companyName;
    private String name;
    private double totalEnergyConsumptionInKWh;
    private int numberOfOccupants;
    private boolean isDeleted;
    private Calendar startDate; //start of the utility creation
    private Calendar endDate; //end of utility creation

    public UtilityModel(long id,
                        Company company,
                        String name,
                        double totalEnergyConsumptionInKWh,
                        int numberOfOccupants,
                        Calendar startDate,
                        Calendar endDate,
                        boolean isDeleted){
        setId(id);
        setCompanyName(company);
        setName(name);
        setTotalEnergyConsumptionInKWh(totalEnergyConsumptionInKWh);
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

    public double getTotalEnergyConsumptionInKWh() {
        return totalEnergyConsumptionInKWh;
    }

    public void setTotalEnergyConsumptionInKWh(double totalEnergyConsumptionInKWh) {
        this.totalEnergyConsumptionInKWh = totalEnergyConsumptionInKWh;
    }

    public double getTotalEnergyConsumptionInGJ() {
        //convert to GJ
        double totalEnergyConsumptionInGJ = 0;
        switch (companyName) {
            //dont need to do anything
            case FORTISBC:
                totalEnergyConsumptionInGJ = totalEnergyConsumptionInKWh;
                break;
            //convert to GJ
            case BCHYDRO:
                totalEnergyConsumptionInGJ = totalEnergyConsumptionInKWh * CONVERT_KWH_TO_GJ;
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

    public double calculateDailyEnergyConsumptionInKWh() {
        return getTotalEnergyConsumptionInKWh() / calculateBillingPeriodInDays();
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

    public String getStartDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(getStartDate().getTime());
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate(){
        return endDate;
    }

    public String getEndDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(getEndDate().getTime());
    }

    public void setEndDate(Calendar endDay) {
        this.endDate = endDay;
    }

    public double calculateTotalEmissions(){
        double totalCO2EmissionsInKg;
        switch (companyName){
            case BCHYDRO:
                totalCO2EmissionsInKg = ELECTRICITY_FOOTPRINT_KG_PER_KWH * getTotalEnergyConsumptionInKWh();
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

    public int calculateBillingPeriodInDays(){
        long msDiff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        return (int)TimeUnit.MILLISECONDS.toDays(msDiff);
    }

    public double getTotalEmissionsPerOccupant() {
        return  calculateTotalEmissions()/numberOfOccupants;
    }

    public double getTotalEnergyConsumptionPerOccupantInGJ() {
        return getTotalEnergyConsumptionInGJ() / numberOfOccupants;
    }

    public double getTotalEnergyConsumptionPerOccupantInKWh() {
        return getTotalEnergyConsumptionInKWh() / numberOfOccupants;
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
                ", totalEnergyConsumptionInGJ=" + totalEnergyConsumptionInKWh +
                ", numberOfOccupants=" + numberOfOccupants +
                ", isDeleted=" + isDeleted +
                '}';
    }

}
