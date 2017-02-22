package com.scorg.dms.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Common {

@SerializedName("success")
@Expose
private Integer success;
@SerializedName("statusCode")
@Expose
private Integer statusCode;
@SerializedName("statusMessage")
@Expose
private String statusMessage;

public Integer getSuccess() {
return success;
}

public void setSuccess(Integer success) {
this.success = success;
}

public Integer getStatusCode() {
return statusCode;
}

public void setStatusCode(Integer statusCode) {
this.statusCode = statusCode;
}

public String getStatusMessage() {
return statusMessage;
}

public void setStatusMessage(String statusMessage) {
this.statusMessage = statusMessage;
}

}