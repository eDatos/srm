package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseDoCopyUtils;
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
        target.setShortName(BaseDoCopyUtils.copy(source.getShortName()));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        // note: replaceBy and replaceTo metadata do not must be copied, because they are related to concrete versions of codelist
    }

    @Override
    public Code createCode() {
        return new CodeMetamac();
    }

    @Override
    public void copyCode(Code sourceSdmx, Code targetSdmx) {
        CodeMetamac source = (CodeMetamac) sourceSdmx;
        CodeMetamac target = (CodeMetamac) targetSdmx;
        target.setShortName(BaseDoCopyUtils.copy(source.getShortName()));
        target.setVariableElement(source.getVariableElement());
    }
}