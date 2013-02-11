package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistsType;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;

@org.springframework.stereotype.Component("codesDo2JaxbRestInternalCallbackMetamac")
public class CodesDo2JaxbCallbackImpl implements CodesDo2JaxbCallback {

    @Autowired
    private CodesDo2RestMapperV10 codesDo2RestMapperV10;

    @Override
    public CodelistType createCodelistJaxb(com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion source) {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist();
    }

    @Override
    public void fillCodelistJaxb(CodelistVersion source, CodelistType target) {
        codesDo2RestMapperV10.toCodelist((CodelistVersionMetamac) source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist) target);
    }

    @Override
    public CodeType createCodeJaxb(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        // when retrieve Codelist, only return SDMX metadata
        CodeType target = new CodeType();
        return target;
    }

    @Override
    public void fillCodeJaxb(Code source, CodeType target) {
        codesDo2RestMapperV10.toCode(source, target);
    }

    @Override
    public CodelistsType createCodelistsJaxb(List<CodelistVersion> source) {
        throw new IllegalArgumentException("createCodelistsJaxb not supported");
    }
}