package org.siemac.metamac.srm.core.code.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodelistType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CodelistsType;

@org.springframework.stereotype.Component("codesMetamacJaxb2DoCallback")
public class CodesJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements CodesJaxb2DoCallback {

    @Autowired
    private CodesMetamacService codesMetamacService;

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
     * 
     * @throws MetamacException
     **************************************************************************/
    @Override
    public void codeListJaxbToDoExtensionPreCreate(ServiceContext ctx, CodelistType source, CodelistVersion previous, CodelistVersion target) throws MetamacException {
        CodelistVersionMetamac previousMetamac = (CodelistVersionMetamac) previous;
        CodelistVersionMetamac targetMetamac = (CodelistVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // TODO completar con metadata heredable
            // TODO herencia IString --> Name
            // TODO herencia IString --> Description
            // TODO herencia IString --> Annotations
            targetMetamac.setShortName(BaseVersioningCopyUtils.copy(previousMetamac.getShortName())); // ShortName
            targetMetamac.setIsRecommended(previousMetamac.getIsRecommended()); // IsRecommended
            targetMetamac.setAccessType(previousMetamac.getAccessType()); // AccesType
            targetMetamac.setFamily(previousMetamac.getFamily()); // CodelistFamily
            targetMetamac.setVariable(previousMetamac.getVariable()); // Variable
            // TODO herencia default order visualization
            // TODO herencia niveles de apertura
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Validate and complete fill
        codesMetamacService.preCreateCodelist(ctx, targetMetamac);

    }
    @Override
    public void codeListJaxbToDoExtensionPostCreate(ServiceContext ctx, CodelistType source, CodelistVersion previousVersion, CodelistVersion target) throws MetamacException {
        CodelistVersionMetamac previousMetamac = (CodelistVersionMetamac) previousVersion;
        CodelistVersionMetamac targetMetamac = (CodelistVersionMetamac) target;

        // Fill metadata
        codesMetamacService.postCreateCodelist(ctx, targetMetamac, (previousVersion != null) ? previousMetamac.getReplaceToCodelists() : new ArrayList<CodelistVersionMetamac>()); // ReplaceToCodelists
    }

    @Override
    public void codeJaxb2DoExtension(ServiceContext ctx, CodeType source, Code previous, Code target) throws MetamacException {
        CodeMetamac previousMetamac = (CodeMetamac) previous;
        CodeMetamac targetMetamac = (CodeMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // TODO herencia IString --> Name
            // TODO herencia IString --> Description
            // TODO herencia IString --> Annotations
            targetMetamac.setShortName(BaseVersioningCopyUtils.copy(previousMetamac.getShortName())); // ShortName
            targetMetamac.setVariableElement(previousMetamac.getVariableElement());
        }
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
