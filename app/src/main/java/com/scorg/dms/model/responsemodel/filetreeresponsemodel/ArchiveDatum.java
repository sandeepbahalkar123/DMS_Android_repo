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


    private int totalDocCategoryPageCount = -1;


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


    public int getTotalDocCategoryPageCount() {
        if (totalDocCategoryPageCount == -1) {
            int count = 0;
            for (LstDocCategory temp :
                    lstDocCategories) {
                count = count + temp.getTotalDocTypePageCount();
            }
            setTotalDocCategoryPageCount(count);
        }
        return totalDocCategoryPageCount;
    }

    public void setTotalDocCategoryPageCount(int totalDocCategoryPageCount) {
        this.totalDocCategoryPageCount = totalDocCategoryPageCount;
    }

    @Override
    public String toString() {
        return "ArchiveDatum{" +
                "fileType='" + fileType + '\'' +
                ", lstDocCategories=" + lstDocCategories +
                '}';
    }
}