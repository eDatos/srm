package org.siemac.metamac.srm.core.code.domain.shared;

public class TaskImportTsvInfo {

    private Boolean isPlannedInBackground;
    private String  jobKey;

    public TaskImportTsvInfo(Boolean isPlannedInBackground, String jobKey) {
        this.isPlannedInBackground = isPlannedInBackground;
        this.jobKey = jobKey;
    }

    /**
     * True when task was planned to execute in background
     */
    public Boolean getIsPlannedInBackground() {
        return isPlannedInBackground;
    }

    public void setIsPlannedInBackground(Boolean isPlannedInBackground) {
        this.isPlannedInBackground = isPlannedInBackground;
    }

    /**
     * Only filled when isPlannedInBackground is true
     */
    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }
}
