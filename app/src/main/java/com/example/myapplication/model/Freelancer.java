package com.example.myapplication.model;

@SuppressWarnings("unused")
public class Freelancer {
    private String freelancerId;

    private String freelancerName;

    private String freelancerFunc;
    private String freelancerPhone;

    public Freelancer(String freelancerName, String freelancerFunc, String freelancerPhone) {
        this.freelancerName = freelancerName;
        this.freelancerFunc = freelancerFunc;
        this.freelancerPhone = freelancerPhone;
    }

    public String getFreelancerName() {
        return freelancerName;
    }

    public String getFreelancerFunc() {
        return freelancerFunc;
    }

    public String getFreelancerPhone() {
        return freelancerPhone;
    }

    public String getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(String freelancerId) {
        this.freelancerId = freelancerId;
    }

    public Freelancer() {

    }
}
