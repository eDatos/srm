package org.siemac.metamac.srm.core.code.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;

import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodelistType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodelistsType;

@org.springframework.stereotype.Component("codesMetamacJaxb2DoCallback")
public class CodesJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements CodesJaxb2DoCallback {

    /**************************************************************************
     * CREATES
     **************************************************************************/
    @Override
    public Code createCodeDo(CodeType source) {
        Code code = new CodeMetamac();

        return code;
    }

    @Override
    public CodelistVersion createCodeListDo(CodelistType source) {
        CodelistVersion codelistVersion = new CodelistVersionMetamac();

        return codelistVersion;
    }

    @Override
    public List<CodelistVersion> createCodeListsDo(CodelistsType source) {
        List<CodelistVersion> codelistVersions = new ArrayList<CodelistVersion>();

        return codelistVersions;
    }

    /**************************************************************************
     * EXTENSIONS
     **************************************************************************/
    @Override
    public void codeListJaxbToDoExtension(CodelistType source, CodelistVersion target) {
        CodelistVersionMetamac targetMetamac = (CodelistVersionMetamac) target;

        // Fill metadata
        targetMetamac.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT)); // TODO call preCreateCategorisation
        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false
    }

    @Override
    public void codeJaxb2DoExtension(CodeType source, Code target) {
        // TODO Metamac codeJaxb2DoExtension
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, CodelistVersion source) throws MetamacException {
        validateRestrictionsGeneral(ctx, source);
        validateRestrictionsMaintainableArtefact(ctx, source.getMaintainableArtefact(), false);
    }

}
