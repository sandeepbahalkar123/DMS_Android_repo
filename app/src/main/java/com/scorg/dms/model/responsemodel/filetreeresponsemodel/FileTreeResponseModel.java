package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.responsemodel.Common;
import com.scorg.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseData;

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