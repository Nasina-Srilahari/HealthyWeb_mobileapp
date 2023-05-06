package com.example.lmc;

public class Prescriptions {

    public String medicine;
    public String dosage;
    public String timing;
    public Prescriptions(){

    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Prescriptions(String medicine, String dosage,String timing ) {
        this.dosage = dosage;
        this.medicine = medicine;
        this.timing = timing;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
}
