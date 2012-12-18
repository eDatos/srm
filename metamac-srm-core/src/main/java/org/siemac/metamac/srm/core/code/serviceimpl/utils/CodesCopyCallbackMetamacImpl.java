package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesDoCopyUtils.CodesCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("codesCopyCallbackMetamac")
public class CodesCopyCallbackMetamacImpl implements CodesCopyCallback {

    @Override
    public CodelistVersion createCodelistVersion() {
        return new CodelistVersionMetamac();
    }

    @Override
    public void copyCodelistVersion(CodelistVersion sourceSdmx, CodelistVersion targetSdmx) {
        CodelistVersionMetamac source = (CodelistVersionMetamac) sourceSdmx;
        CodelistVersionMetamac target = (CodelistVersionMetamac) targetSdmx;
        target.setShortName(source.getShortName());
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public Code createCode() {
        return new CodeMetamac();
    }

    @Override
    public void copyCode(Code source, Code target) {
        // nothing more
    }
}