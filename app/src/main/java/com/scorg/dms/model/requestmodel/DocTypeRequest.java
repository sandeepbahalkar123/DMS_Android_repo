package com.scorg.dms.model.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocTypeRequest {

@SerializedName("typeId")
@Expose
private Integer typeId;
@SerializedName("typeName")
@Expose
private String typeName;
@SerializedName("abbreviation")
@Expose
private String abbreviation;

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

public String getAbbreviation() {
return abbreviation;
}

public void setAbbreviation(String abbreviation) {
this.abbreviation = abbreviation;
}

}