package com.scorg.dms.model.requestmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class FileTreeRequestModel implements CustomResponse{

    @SerializedName("lstSearchParam")
    @Expose
    private List<LstSearchParam> lstSearchParam = null;

    public List<LstSearchParam> getLstSearchParam() {
        return lstSearchParam;
    }

    public void setLstSearchParam(List<LstSearchParam> lstSearchParam) {
        this.lstSearchParam = lstSearchParam;
    }

}