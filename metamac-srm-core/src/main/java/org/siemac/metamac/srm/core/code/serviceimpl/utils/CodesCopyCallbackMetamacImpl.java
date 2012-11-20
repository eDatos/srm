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
    public CodelistVersion copyCodelistVersion(CodelistVersion source) {
        return copyCodelistVersion((CodelistVersionMetamac) source);
    }

    private CodelistVersionMetamac copyCodelistVersion(CodelistVersionMetamac source) {
        CodelistVersionMetamac target = new CodelistVersionMetamac();
        target.setShortName(source.getShortName());
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        return target;
    }

    @Override
    public Code copyCode(Code source) {
        return copyCode((CodeMetamac) source);
    }

    private CodeMetamac copyCode(CodeMetamac source) {
        CodeMetamac target = new CodeMetamac();
        return target;
    }
}