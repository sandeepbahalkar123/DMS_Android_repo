package com.scorg.dms.model.requestmodel.getpdfdatarequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstDocTypeRequest {

@SerializedName("docType")
@Expose
private DocTypeRequest docTypeRequest;
@SerializedName("createdDate")
@Expose
private String createdDate;
@SerializedName("pageCount")
@Expose
private Integer pageCount;

public DocTypeRequest getDocTypeRequest() {
    return docTypeRequest;
}

public void setDocTypeRequest(DocTypeRequest docTypeRequest) {
    this.docTypeRequest = docTypeRequest;
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