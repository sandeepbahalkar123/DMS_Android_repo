package com.scorg.dms.model.responsemodel.annotationlistresponsemodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnnotationListData {

    @SerializedName("lstAnnotations")
    @Expose
    private List<AnnotationList> annotationLists = null;

    public List<AnnotationList> getAnnotationLists() {
        return annotationLists;
    }

    public void setAnnotationLists(List<AnnotationList> annotationLists) {
        this.annotationLists = annotationLists;
    }
}