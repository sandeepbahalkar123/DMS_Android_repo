package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstDocCategory {

    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("lstDocTypes")
    @Expose
    private List<LstDocType> lstDocTypes = new ArrayList<LstDocType>();

    private int totalDocTypePageCount = -1;

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

    public List<LstDocType> getLstDocTypes() {
        return lstDocTypes;
    }

    public void setLstDocTypes(List<LstDocType> lstDocTypes) {
        this.lstDocTypes = lstDocTypes;
    }


    public int getTotalDocTypePageCount() {

        if (totalDocTypePageCount == -1) {
            int count = 0;
            for (LstDocType temp :
                    lstDocTypes) {
                count = count + temp.getPageCount();
            }
            setTotalDocTypePageCount(count);
        }
        return totalDocTypePageCount;
    }

    public void setTotalDocTypePageCount(int totalDocTypePageCount) {
        this.totalDocTypePageCount = totalDocTypePageCount;
    }
}
