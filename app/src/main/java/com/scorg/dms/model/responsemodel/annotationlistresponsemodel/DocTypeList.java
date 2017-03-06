package com.scorg.dms.model.responsemodel.annotationlistresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocTypeList {

@SerializedName("typeId")
@Expose
private Integer typeId;
@SerializedName("typeName")
@Expose
private String typeName;

public Integer getTypeId() {
return typeId;
}

public void setTypeId(Integer typeId) {
this.typeId = typeId;
}

public String getTypeName() {
return typeName;
}

public void setTypeName(String typeName) {
this.typeName = typeName;
}

}