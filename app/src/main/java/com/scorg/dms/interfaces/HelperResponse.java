package com.scorg.dms.interfaces;

/**
 * @author Sandeep Bahalkar
 */

public interface HelperResponse {

    public void onSuccess(int mOldDataTag, CustomResponse customResponse);

    public void onParseError(int mOldDataTag, String errorMessage);

    public void onServerError(int mOldDataTag, String serverErrorMessage);

    public void onNoConnectionError(int mOldDataTag, String serverErrorMessage);

}
