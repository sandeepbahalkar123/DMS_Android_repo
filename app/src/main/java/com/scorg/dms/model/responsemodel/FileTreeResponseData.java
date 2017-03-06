package com.scorg.dms.model.responsemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileTreeResponseData {

@SerializedName("archiveData")
@Expose
private List<ArchiveDatum> archiveData = null;

public List<ArchiveDatum> getArchiveData() {
return archiveData;
}

public void setArchiveData(List<ArchiveDatum> archiveData) {
this.archiveData = archiveData;
}

}