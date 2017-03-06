package com.scorg.dms.model.responsemodel.showsearchresultresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("patientFileData")
    @Expose
    private List<PatientFileData> patientFileData = null;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<PatientFileData> getPatientFileData() {
        return patientFileData;
    }

    public void setPatientFileData(List<PatientFileData> patientFileData) {
        this.patientFileData = patientFileData;
    }

}