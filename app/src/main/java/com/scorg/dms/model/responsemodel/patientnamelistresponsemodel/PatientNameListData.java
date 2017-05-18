package com.scorg.dms.model.responsemodel.patientnamelistresponsemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientNameListData {

@SerializedName("lstPatients")
@Expose
private List<LstPatient> lstPatients = null;

public List<LstPatient> getLstPatients() {
return lstPatients;
}

public void setLstPatients(List<LstPatient> lstPatients) {
this.lstPatients = lstPatients;
}

}