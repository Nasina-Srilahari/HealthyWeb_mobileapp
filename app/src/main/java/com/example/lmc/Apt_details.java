package com.example.lmc;

public class Apt_details {
    public String apt_doctor;
    public String apt_date;
    public String apt_time;
    public String apt_pat_no;

    public Apt_details(String apt_doctor, String apt_date, String apt_time, String apt_pat_no) {
        this.apt_doctor = apt_doctor;
        this.apt_date = apt_date;
        this.apt_time = apt_time;
        this.apt_pat_no = apt_pat_no;
    }

    public Apt_details(String apt_doctor, String apt_date, String apt_time) {
        this.apt_doctor = apt_doctor;
        this.apt_date = apt_date;
        this.apt_time = apt_time;
    }

    public Apt_details() {

    }
}
