package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstDocCategory {

@SerializedName("docCategory")
@Expose
private DocCategory docCategory;
@SerializedName("lstDocTypes")
@Expose
private List<LstDocType> lstDocTypes = null;

public DocCategory getDocCategory() {
return docCategory;
}

public void setDocCategory(DocCategory docCategory) {
this.docCategory = docCategory;
}

public List<LstDocType> getLstDocTypes() {
return lstDocTypes;
}

public void setLstDocTypes(List<LstDocType> lstDocTypes) {
this.lstDocTypes = lstDocTypes;
}

}