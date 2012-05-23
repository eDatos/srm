package org.siemac.metamac.srm.web.dsd.listener;

public interface UploadListener {

    public void uploadComplete(String fileName);
    public void uploadFailed(String fileName);

}
