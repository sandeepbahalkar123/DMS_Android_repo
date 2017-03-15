package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstDocType {

    @SerializedName("typeId")
    @Expose
    private Integer typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("abbreviation")
    @Expose
    private String abbreviation;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;

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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

}