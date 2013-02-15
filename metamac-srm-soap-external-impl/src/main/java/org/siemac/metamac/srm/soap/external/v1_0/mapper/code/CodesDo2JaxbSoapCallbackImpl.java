package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistsType;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;

@org.springframework.stereotype.Component("codesDo2JaxbSoapCallbackMetamac")
public class CodesDo2JaxbSoapCallbackImpl implements CodesDo2JaxbCallback {

    @Autowired
    private CodesDo2SoapMapperV10 codesDo2SoapMapperV10;

    @Override
    public CodelistType createCodelistJaxb(com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion source) {
        return new org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist();
    }

    @Override
    public void fillCodelistJaxb(CodelistVersion source, CodelistType target) {
        codesDo2SoapMapperV10.toCodelist((CodelistVersionMetamac) source, (org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist) target);
    }

    @Override
    public CodeType createCodeJaxb(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        return new org.siemac.metamac.soap.structural_resources.v1_0.domain.Code();
    }

    @Override
    public void fillCodeJaxb(Code source, CodeType target) {
        codesDo2SoapMapperV10.toCode((CodeMetamac) source, (org.siemac.metamac.soap.structural_resources.v1_0.domain.Code) target);
    }

    @Override
    public CodelistsType createCodelistsJaxb(List<CodelistVersion> source) {
        throw new IllegalArgumentException("createCodelistsJaxb not supported");
    }
}