package com.scorg.dms.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;

public class ShowSearchResultResponseModel implements CustomResponse, Serializable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private SearchResultData searchResultData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public SearchResultData getSearchResultData() {
        return searchResultData;
    }

    public void setSearchResultData(SearchResultData searchResultData) {
        this.searchResultData = searchResultData;
    }
}