package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import org.springframework.stereotype.Component;

/**
 * Copy Metamac metadata
 */
@Component("codesCopyWithoutCodesCallbackMetamac")
public class CodesCopyWithoutCodesCallbackMetamacImpl extends CodesCopyCallbackMetamacImpl {

    @Override
    public Boolean mustCopyCodes() {
        return Boolean.FALSE;
    }
}