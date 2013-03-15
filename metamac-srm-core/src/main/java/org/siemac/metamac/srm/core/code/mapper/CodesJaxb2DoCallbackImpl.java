package org.siemac.metamac.srm.core.code.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodelistsType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseJaxb2DoInheritUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesJaxb2DoCallback;

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
     **************************************************************************/
    @Override
    public void codeListJaxbToDoExtensionPreCreate(ServiceContext ctx, CodelistType source, CodelistVersion previous, CodelistVersion target) throws MetamacException {
        CodelistVersionMetamac previousMetamac = (CodelistVersionMetamac) previous;
        CodelistVersionMetamac targetMetamac = (CodelistVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            targetMetamac.getMaintainableArtefact().setName(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName())); // Name
            targetMetamac.getMaintainableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription())); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotations(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations

            targetMetamac.setShortName(BaseVersioningCopyUtils.copy(previousMetamac.getShortName())); // ShortName
            targetMetamac.setIsRecommended(previousMetamac.getIsRecommended()); // IsRecommended
            targetMetamac.setAccessType(previousMetamac.getAccessType()); // AccesType
            targetMetamac.setFamily(previousMetamac.getFamily()); // CodelistFamily
            targetMetamac.setVariable(previousMetamac.getVariable()); // Variable
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

        // TODO DECIDIR SI SE VAN A HEREDAR LOS ÓRDENES. Después, hablar con San para decidir el algoritmo de copia de órdenes. OJO! No volver a crear el orden alfabético en V2
        // TODO herencia order visualisations
        // TODO herencia default order visualisation
        // TODO herencia niveles de apertura
        // TODO herencia todas las columnas de orden de los code
        // TODO herencia idem para openness
    }

    @Override
    public void codeJaxb2DoExtensionPreCreate(ServiceContext ctx, CodeType source, CodelistVersion codelistVersion, Code previous, Code target) throws MetamacException {
        CodeMetamac previousMetamac = (CodeMetamac) previous;
        CodeMetamac targetMetamac = (CodeMetamac) target;
        CodelistVersionMetamac codelistVersionMetamac = (CodelistVersionMetamac) codelistVersion;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            targetMetamac.getNameableArtefact().setName(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName())); // Name
            targetMetamac.getNameableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription())); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotations(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations

            targetMetamac.setShortName(BaseVersioningCopyUtils.copy(previousMetamac.getShortName())); // ShortName
            targetMetamac.setVariableElement(previousMetamac.getVariableElement());
        }

        // Fill metadata
        codesMetamacService.preCreateCode(ctx, codelistVersionMetamac, targetMetamac);
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, CodelistVersion source) throws MetamacException {
        validateRestrictionsItemSchemeVersion(ctx, source, false);
    }

}
