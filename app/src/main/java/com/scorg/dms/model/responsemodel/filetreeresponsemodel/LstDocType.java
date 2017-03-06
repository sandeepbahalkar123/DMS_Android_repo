package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstDocType {

@SerializedName("docType")
@Expose
private DocType docType;
@SerializedName("createdDate")
@Expose
private String createdDate;
@SerializedName("pageCount")
@Expose
private Integer pageCount;

public DocType getDocType() {
return docType;
}

public void setDocType(DocType docType) {
this.docType = docType;
}

public String getCreatedDate() {
return createdDate;
}

public void setCreatedDate(String createdDate) {
this.createdDate = createdDate;
}

public Integer getPageCount() {
return pageCount;
}

public void setPageCount(Integer pageCount) {
this.pageCount = pageCount;
}

}