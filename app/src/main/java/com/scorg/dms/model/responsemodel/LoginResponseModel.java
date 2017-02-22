package com.scorg.dms.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {

@SerializedName("common")
@Expose
private Common common;
@SerializedName("data")
@Expose
private LoginResponseData loginResponseData;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

    public LoginResponseData getLoginResponseData() {
        return loginResponseData;
    }

    public void setLoginResponseData(LoginResponseData loginResponseData) {
        this.loginResponseData = loginResponseData;
    }
}