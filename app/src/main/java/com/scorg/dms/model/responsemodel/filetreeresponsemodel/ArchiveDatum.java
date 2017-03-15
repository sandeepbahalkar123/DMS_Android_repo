package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArchiveDatum {

@SerializedName("fileType")
@Expose
private String fileType;
@SerializedName("lstDocCategories")
@Expose
private List<LstDocCategory> lstDocCategories = new ArrayList<LstDocCategory>();

public String getFileType() {
return fileType;
}

public void setFileType(String fileType) {
this.fileType = fileType;
}

public List<LstDocCategory> getLstDocCategories() {
return lstDocCategories;
}

public void setLstDocCategories(List<LstDocCategory> lstDocCategories) {
this.lstDocCategories = lstDocCategories;
}

}