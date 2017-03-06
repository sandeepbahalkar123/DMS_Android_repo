package com.scorg.dms.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;

public class GetPdfDataResponseModel implements CustomResponse,Serializable{

@SerializedName("common")
@Expose
private Common common;
@SerializedName("data")
@Expose
private GetPdfDataResponseData getPdfDataResponseData;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

    public GetPdfDataResponseData getGetPdfDataResponseData() {
        return getPdfDataResponseData;
    }

    public void setGetPdfDataResponseData(GetPdfDataResponseData getPdfDataResponseData) {
        this.getPdfDataResponseData = getPdfDataResponseData;
    }
}