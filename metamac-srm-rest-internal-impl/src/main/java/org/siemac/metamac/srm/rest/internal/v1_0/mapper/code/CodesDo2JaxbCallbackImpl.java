package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistsType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@org.springframework.stereotype.Component("codesDo2JaxbRestInternalCallbackMetamac")
public class CodesDo2JaxbCallbackImpl implements CodesDo2JaxbCallback {

    @Autowired
    private CodeMetamacRepository            codeRepository;

    @Autowired
    private CodesDo2RestMapperV10            codesDo2RestMapperV10;

    private final ItemMetamacResultSelection codeMetamacResultSelection = new ItemMetamacResultSelection(true, false, true, false);

    @Override
    public CodelistType createCodelistJaxb() {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist();
    }

    @Override
    public void fillCodelistJaxb(CodelistVersion source, CodelistType target) {
        codesDo2RestMapperV10.toCodelist((CodelistVersionMetamac) source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist) target);
    }

    @Override
    public CodeType createCodeJaxb() {
        // when retrieve Codelist, only return SDMX metadata
        return new CodeType();
    }

    @Override
    public void fillCodeJaxb(Code source, ItemResult sourceItemResult, ItemSchemeVersion itemSchemeVersion, CodeType target) {
        if (target instanceof org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code) {
            // nothing more (mapping is done explicitly in toCode(CodeMetamac source))
        } else {
            codesDo2RestMapperV10.toCode(source, sourceItemResult, itemSchemeVersion, target);
        }
    }

    @Override
    public CodelistsType createCodelistsJaxb() {
        throw new IllegalArgumentException("createCodelistsJaxb not supported");
    }

    @Override
    public List<ItemResult> findCodesByCodelistEfficiently(CodelistVersion codelistVersion) throws MetamacException {
        return codeRepository.findCodesByCodelistOrderedInDepth(codelistVersion.getId(), ((CodelistVersionMetamac) codelistVersion).getDefaultOrderVisualisation().getColumnIndex(),
                codeMetamacResultSelection);
    }
}