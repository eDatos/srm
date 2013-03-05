package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistsType;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@org.springframework.stereotype.Component("codesDo2JaxbSoapCallbackMetamac")
public class CodesDo2JaxbSoapCallbackImpl implements CodesDo2JaxbCallback {

    @Autowired
    private CodesDo2SoapMapperV10 codesDo2SoapMapperV10;

    @Autowired
    private CodeMetamacRepository codeRepository;

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
    public CodeType createCodeJaxb(ItemResult source) {
        return new org.siemac.metamac.soap.structural_resources.v1_0.domain.Code();
    }

    @Override
    public void fillCodeJaxb(Code source, CodeType target) {
        codesDo2SoapMapperV10.toCode((CodeMetamac) source, (org.siemac.metamac.soap.structural_resources.v1_0.domain.Code) target);
    }

    @Override
    public void fillCodeJaxb(ItemResult source, ItemSchemeVersion itemSchemeVersion, CodeType target) {
        codesDo2SoapMapperV10.toCode(source, itemSchemeVersion, (org.siemac.metamac.soap.structural_resources.v1_0.domain.Code) target);
    }

    @Override
    public CodelistsType createCodelistsJaxb(List<CodelistVersion> source) {
        throw new IllegalArgumentException("createCodelistsJaxb unsupported");
    }

    @Override
    public List<ItemResult> findCodesByCodelistEfficiently(Long idCodelist) {
        return codeRepository.findCodesByCodelistOrderedInDepth(idCodelist, null); // TODO orderColumnIndex
    }
}