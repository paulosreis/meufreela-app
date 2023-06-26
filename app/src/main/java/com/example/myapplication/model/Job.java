package com.example.myapplication.model;

import java.util.List;

@SuppressWarnings("unused")
public class Job {
    private String jobId;
    private String jobDate;
    private String jobTime;
    private List<String> selectedFreelancers;

    @SuppressWarnings("unused")
    public Job() {
        // Construtor vazio necessário para o Firebase Realtime Database
    }

    public Job(String jobId, String jobDate, String jobTime, List<String> selectedFreelancers) {
        this.jobId = jobId;
        this.jobDate = jobDate;
        this.jobTime = jobTime;
        this.selectedFreelancers = selectedFreelancers;
    }

    @SuppressWarnings("unused")
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public List<String> getSelectedFreelancers() {
        return selectedFreelancers;
    }

    public void setSelectedFreelancers(List<String> selectedFreelancers) {
        this.selectedFreelancers = selectedFreelancers;
    }
}
