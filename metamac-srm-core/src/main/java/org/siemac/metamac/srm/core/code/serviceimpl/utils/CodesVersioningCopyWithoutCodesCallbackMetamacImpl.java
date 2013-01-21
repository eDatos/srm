package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import org.springframework.stereotype.Component;

/**
 * Copy Metamac metadata
 */
@Component("codesVersioningCopyWithoutCodesCallbackMetamac")
public class CodesVersioningCopyWithoutCodesCallbackMetamacImpl extends CodesVersioningCopyCallbackMetamacImpl {

    @Override
    public Boolean mustCopyCodes() {
        return Boolean.FALSE;
    }
}