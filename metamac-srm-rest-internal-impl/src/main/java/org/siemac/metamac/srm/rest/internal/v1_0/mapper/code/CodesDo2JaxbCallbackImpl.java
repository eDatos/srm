package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodelistType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodelistsType;

@org.springframework.stereotype.Component("codesDo2JaxbCallbackMetamac")
public class CodesDo2JaxbCallbackImpl implements CodesDo2JaxbCallback {

    @Autowired
    private CodesDo2RestMapperV10 codesDo2RestMapperV10;

    @Override
    public CodelistType createCodelistJaxb(com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion source) {
        org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelist target = new org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelist();
        codesDo2RestMapperV10.toCodelist((CodelistVersionMetamac) source, target);
        return target;
    }

    @Override
    public CodeType createCodeJaxb(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        // when retrieve Codelist, only return SDMX metadata
        CodeType target = new CodeType();
        return target;
    }

    @Override
    public CodelistsType createCodelistsJaxb(List<CodelistVersion> source) {
        throw new IllegalArgumentException("createCodelistsJaxb not supported");
    }
}