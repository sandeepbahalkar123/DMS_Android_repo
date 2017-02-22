package com.scorg.dms.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {

@SerializedName("PatientId")
@Expose
private Integer patientId;
@SerializedName("PatientName")
@Expose
private String patientName;
@SerializedName("FileType")
@Expose
private String fileType;
@SerializedName("referenceId")
@Expose
private Integer referenceId;
@SerializedName("AdmissionDate")
@Expose
private String admissionDate;
@SerializedName("DischargeDate")
@Expose
private String dischargeDate;

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

}