package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistsType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@org.springframework.stereotype.Component("codesDo2JaxbSoapCallbackMetamac")
public class CodesDo2JaxbSoapCallbackImpl implements CodesDo2JaxbCallback {

    @Autowired
    private CodesDo2SoapMapperV10            codesDo2SoapMapperV10;

    @Autowired
    private CodeMetamacRepository            codeRepository;

    private final ItemMetamacResultSelection codeMetamacResultSelection = new ItemMetamacResultSelection(true, false, true, true);

    @Override
    public CodelistType createCodelistJaxb() {
        return new org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist();
    }

    @Override
    public void fillCodelistJaxb(CodelistVersion source, CodelistType target) {
        codesDo2SoapMapperV10.toCodelist((CodelistVersionMetamac) source, (org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist) target);
    }

    @Override
    public CodeType createCodeJaxb() {
        return new org.siemac.metamac.soap.structural_resources.v1_0.domain.Code(); // when retrieve codes in codelist request, return some metamac information
    }

    @Override
    public void fillCodeJaxb(Code source, ItemResult sourceItemResult, ItemSchemeVersion itemSchemeVersion, CodeType target) {
        codesDo2SoapMapperV10.toCode((CodeMetamac) source, sourceItemResult, (org.siemac.metamac.soap.structural_resources.v1_0.domain.Code) target);
    }

    @Override
    public CodelistsType createCodelistsJaxb() {
        throw new IllegalArgumentException("createCodelistsJaxb unsupported");
    }

    @Override
    public boolean mustRetrieveCodesInsideCodelist() {
        return true;
    }

    @Override
    public List<ItemResult> findCodesByCodelistEfficiently(CodelistVersion codelistVersion) throws MetamacException {
        return codeRepository.findCodesByCodelistOrderedInDepth(codelistVersion.getId(), ((CodelistVersionMetamac) codelistVersion).getDefaultOrderVisualisation().getColumnIndex(), null,
                codeMetamacResultSelection);
    }
}