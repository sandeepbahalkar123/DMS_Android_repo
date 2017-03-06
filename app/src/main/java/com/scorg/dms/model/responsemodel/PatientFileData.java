package com.scorg.dms.model.responsemodel;

/**
 * Created by sandeep on 2/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PatientFileData {

    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("referenceId")
    @Expose
    private Integer referenceId;
    @SerializedName("admissionDate")
    @Expose
    private String admissionDate;
    @SerializedName("dischargeDate")
    @Expose
    private String dischargeDate;


    private boolean isShowCompleteList = false;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public boolean isShowCompleteList() {
        return isShowCompleteList;
    }

    public void setShowCompleteList(boolean showCompleteList) {
        isShowCompleteList = showCompleteList;
    }
}
