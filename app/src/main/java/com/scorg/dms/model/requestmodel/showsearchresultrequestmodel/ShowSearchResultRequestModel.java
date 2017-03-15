package com.scorg.dms.model.requestmodel.showsearchresultrequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class ShowSearchResultRequestModel implements CustomResponse {

    @SerializedName("patientId")
    @Expose
    private String patientId = "";
    @SerializedName("patientName")
    @Expose
    private String patientName = "";
    @SerializedName("fileType")
    @Expose
    private String fileType = "";
    @SerializedName("referenceId")
    @Expose
    private String referenceId = "";
    @SerializedName("dateType")
    @Expose
    private String dateType = "";
    @SerializedName("fromDate")
    @Expose
    private String fromDate = "";
    @SerializedName("toDate")
    @Expose
    private String toDate = "";
    @SerializedName("annotationText")
    @Expose
    private String annotationText = "";

    private String[] DocTypeId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }

    public String[] getDocTypeId() {
        return DocTypeId;
    }

    public void setDocTypeId(String[] docTypeId) {

        if (docTypeId != null) {
            String[] temp = new String[docTypeId.length];
            for (int i = 0; i < docTypeId.length; i++) {
                String dataValue = docTypeId[i];
                if (dataValue.contains("\\|")) {
                    temp[i] = dataValue.split("\\|")[1];
                } else {
                    temp[i] = dataValue;
                }
            }
            DocTypeId = temp;
        } else {
            DocTypeId = docTypeId;
        }
    }

    @Override
    public String toString() {
        return "ShowSearchResultRequestModel{" +
                "patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", referenceId=" + referenceId +
                ", dateType='" + dateType + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", annotationText='" + annotationText + '\'' +
                '}';
    }
}