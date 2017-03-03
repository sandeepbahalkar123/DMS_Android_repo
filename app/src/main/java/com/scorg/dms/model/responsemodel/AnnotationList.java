package com.scorg.dms.model.responsemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 3/3/17.
 */

public class AnnotationList {

    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("docTypeList")
    @Expose
    private List<DocTypeList> docTypeList = null;

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

    public List<DocTypeList> getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List<DocTypeList> docTypeList) {
        this.docTypeList = docTypeList;
    }

}