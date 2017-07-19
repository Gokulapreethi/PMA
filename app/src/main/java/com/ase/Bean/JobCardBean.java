package com.ase.Bean;

/**
 * Created by Preethi on 29-05-2017.
 */
public class JobCardBean {

    private int jobcardno;
    private String customername;
    private String address;
    private String mcModel;
    private String mcSrNo;
    private String servicerequestdate;
    private String chasisNo;
    private String observation;
    private String customerid;
    private String processflag;
    private String activecode;

    public String getActivecode() {
        return activecode;
    }

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    public String getProcessflag() {
        return processflag;
    }

    public void setProcessflag(String processflag) {
        this.processflag = processflag;
    }

    public int getJobcardno() {
        return jobcardno;
    }

    public void setJobcardno(int jobcardno) {
        this.jobcardno = jobcardno;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServicerequestdate() {
        return servicerequestdate;
    }

    public void setServicerequestdate(String servicerequestdate) {
        this.servicerequestdate = servicerequestdate;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getMcModel() {
        return mcModel;
    }

    public void setMcModel(String mcModel) {
        this.mcModel = mcModel;
    }

    public String getMcSrNo() {
        return mcSrNo;
    }

    public void setMcSrNo(String mcSrNo) {
        this.mcSrNo = mcSrNo;
    }
}
