package com.scorg.dms.model.responsemodel.filetreeresponsemodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileTreeResponseData {

@SerializedName("archiveData")
@Expose
private List<ArchiveDatum> archiveData = new ArrayList<ArchiveDatum>();

public List<ArchiveDatum> getArchiveData() {
return archiveData;
}

public void setArchiveData(List<ArchiveDatum> archiveData) {
this.archiveData = archiveData;
}

}