package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesDoCopyUtils.CodesCopyCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

/**
 * Implementation of CodesMetamacService.
 */
@Service("codesMetamacService")
public class CodesMetamacServiceImpl extends CodesMetamacServiceImplBase {

    @Autowired
    private CodesService                codesService;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    // @Autowired
    // private CodeRepository codeRepository;
    //
    @Autowired
    @Qualifier("codelistLifeCycle")
    private LifeCycle                   codelistLifeCycle;

    @Autowired
    @Qualifier("codesCopyCallbackMetamac")
    private CodesCopyCallback           codesCopyCallback;

    public CodesMetamacServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistVersionMetamac createCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelist(codelistVersion, null);

        // Fill metadata
        codelistVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        codelistVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save codelist
        return (CodelistVersionMetamac) codesService.createCodelist(ctx, codelistVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public CodelistVersionMetamac updateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelist(codelistVersion, null);
        // CodesService checks codelist isn't final (Codelists cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

        // Save codelist
        return (CodelistVersionMetamac) codesService.updateCodelist(ctx, codelistVersion);
    }

    public CodelistVersionMetamac retrieveCodelistByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codesService.retrieveCodelistByUrn(ctx, urn);
    }

    @Override
    public List<CodelistVersionMetamac> retrieveCodelistVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve codelistVersions
        List<CodelistVersion> codelistVersions = codesService.retrieveCodelistVersions(ctx, urn);

        // Typecast to CodelistVersionMetamac
        return codelistVersionsToCodelistVersionsMetamac(codelistVersions);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<CodelistVersion> codelistVersionPagedResult = codesService.findCodelistsByCondition(ctx, conditions, pagingParameter);
        return pagedResultCodelistVersionToMetamac(codelistVersionPagedResult);
    }

    @Override
    public CodelistVersionMetamac sendCodelistToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac sendCodelistToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac rejectCodelistProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac rejectCodelistDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    // TODO Para llevar a cabo la publicación interna de un recurso será necesario que previamente exista al menos un anuncio sobre el esquema de conceptos a publicar
    @Override
    public CodelistVersionMetamac publishInternallyCodelist(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.publishInternally(ctx, urn);
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    @Override
    public CodelistVersionMetamac publishExternallyCodelist(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteCodelist(ServiceContext ctx, String urn) throws MetamacException {
        // Note: CodesService checks codelist isn't final and other conditions
        codesService.deleteCodelist(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac versioningCodelist(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkVersioningCodelist(urnToCopy, versionType, null, null);
        checkVersioningCodelistIsSupported(ctx, urnToCopy);

        // Versioning
        CodelistVersionMetamac conceptSchemeNewVersion = (CodelistVersionMetamac) codesService.versioningCodelist(ctx, urnToCopy, versionType, codesCopyCallback);

        return conceptSchemeNewVersion;
    }

    @Override
    public CodelistVersionMetamac endCodelistValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkEndCodelistValidity(urn, null, null);

        // Retrieve version in specific procStatus
        CodelistVersionMetamac codelistVersion = getCodelistVersionMetamacRepository().retrieveCodelistVersionByProcStatus(urn, new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        codelistVersion = (CodelistVersionMetamac) codesService.endCodelistValidity(ctx, urn, null);

        return codelistVersion;
    }

    // ------------------------------------------------------------------------------------
    // CODES
    // ------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------------------

    /**
     * Typecast to Metamac type
     */
    private List<CodelistVersionMetamac> codelistVersionsToCodelistVersionsMetamac(List<CodelistVersion> sources) {
        List<CodelistVersionMetamac> targets = new ArrayList<CodelistVersionMetamac>();
        for (ItemSchemeVersion source : sources) {
            targets.add((CodelistVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CodelistVersionMetamac> pagedResultCodelistVersionToMetamac(PagedResult<CodelistVersion> source) {
        List<CodelistVersionMetamac> codelistVersionsMetamac = codelistVersionsToCodelistVersionsMetamac(source.getValues());
        return new PagedResult<CodelistVersionMetamac>(codelistVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    private void checkVersioningCodelistIsSupported(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Retrieve version to copy and check it is final (internally published)
        CodelistVersion codelistVersionToCopy = retrieveCodelistByUrn(ctx, urnToCopy);
        if (!codelistVersionToCopy.getMaintainableArtefact().getFinalLogic()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(codelistVersionToCopy.getMaintainableArtefact().getUrn()).build();
        }
        // Check does not exist any version 'no final'
        ItemSchemeVersion codelistVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinal(codelistVersionToCopy.getItemScheme().getId());
        if (codelistVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(codelistVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

}
