package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocCategory {

@SerializedName("categoryId")
@Expose
private Integer categoryId;
@SerializedName("categoryName")
@Expose
private String categoryName;
@SerializedName("docTypeList")
@Expose
private Object docTypeList;

public Integer getCategoryId() {
return categoryId;
}

public void setCategoryId(Integer categoryId) {
this.categoryId = categoryId;
}

public String getCategoryName() {
return categoryName;
}

public void setCategoryName(String categoryName) {
this.categoryName = categoryName;
}

public Object getDocTypeList() {
return docTypeList;
}

public void setDocTypeList(Object docTypeList) {
this.docTypeList = docTypeList;
}

}