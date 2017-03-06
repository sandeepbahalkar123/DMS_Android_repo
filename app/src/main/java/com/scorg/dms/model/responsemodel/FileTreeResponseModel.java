package com.scorg.dms.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;

public class FileTreeResponseModel implements CustomResponse,Serializable{

@SerializedName("common")
@Expose
private Common common;
@SerializedName("data")
@Expose
private FileTreeResponseData fileTreeResponseData;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

public FileTreeResponseData getFileTreeResponseData() {
    return fileTreeResponseData;
}

public void setFileTreeResponseData(FileTreeResponseData fileTreeResponseData) {
    this.fileTreeResponseData = fileTreeResponseData;
}
}