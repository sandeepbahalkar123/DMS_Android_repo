package com.scorg.dms.model.responsemodel.showsearchresultresponsemodel;

/**
 * Created by sandeep on 2/22/17.
 */

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class SearchResultData implements CustomResponse {

    @SerializedName("searchResult")
    @Expose
    private List<SearchResult> searchResult = new ArrayList<SearchResult>();

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        this.searchResult = searchResult;
    }

}