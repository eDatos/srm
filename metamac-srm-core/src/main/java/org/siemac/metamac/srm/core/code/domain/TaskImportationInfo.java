package org.siemac.metamac.srm.core.code.domain;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;

public class TaskImportationInfo {

    private Boolean                    isPlannedInBackground;
    private String                     jobKey;
    private List<MetamacExceptionItem> informationItems;

    public TaskImportationInfo(Boolean isPlannedInBackground, String jobKey) {
        this.isPlannedInBackground = isPlannedInBackground;
        this.jobKey = jobKey;
    }

    public TaskImportationInfo(Boolean isPlannedInBackground, List<MetamacExceptionItem> informationItems) {
        this.isPlannedInBackground = isPlannedInBackground;
        this.informationItems = informationItems;
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

    /**
     * Normally, only filled when isPlannedInBackground is false
     */
    public List<MetamacExceptionItem> getInformationItems() {
        return informationItems;
    }

    public void setInformationItems(List<MetamacExceptionItem> informationItems) {
        this.informationItems = informationItems;
    }
}
