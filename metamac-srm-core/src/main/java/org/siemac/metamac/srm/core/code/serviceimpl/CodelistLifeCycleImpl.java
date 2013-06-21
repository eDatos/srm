package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;

@Service("codelistLifeCycle")
public class CodelistLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository      itemSchemeVersionRepository;

    @Autowired
    private CodelistVersionMetamacRepository codelistVersionMetamacRepository;

    @Autowired
    private CodeRepository                   codeRepository;

    @Autowired
    private CodeMetamacRepository            codeMetamacRepository;

    @Autowired
    private CodesService                     codesService;

    @Autowired
    private CodesMetamacService              codesMetamacService;

    @Autowired
    private BaseService                      baseService;

    public CodelistLifeCycleImpl() {
        this.callback = new CodelistLifeCycleCallback();
    }

    private class CodelistLifeCycleCallback implements LifeCycleCallback {

        @Override
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion) {
            return getCodelistVersionMetamac(srmResourceVersion).getLifeCycleMetadata();
        }

        @Override
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion) {
            return getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact();
        }

        @Override
        public Object updateSrmResource(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);
            // Update item scheme
            baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
            // Update item scheme version
            return itemSchemeVersionRepository.save(codelistVersion);
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return codelistVersionMetamacRepository.retrieveCodelistVersionByProcStatus(urn, procStatus);
        }

        @Override
        public Object executeBeforeSendProductionValidation(ServiceContext ctx, Object srmResourceVersion) {
            // nothing
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInProductionValidation(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);
            String codelistUrn = codelistVersion.getMaintainableArtefact().getUrn();
            Map<String, MetamacExceptionItem> exceptionsByResourceUrn = new HashMap<String, MetamacExceptionItem>();

            // Check codelist
            {
                List<MetamacExceptionItem> exceptionsCodelist = new ArrayList<MetamacExceptionItem>();
                // Metadata required
                ValidationUtils.checkMetadataRequired(codelistVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptionsCodelist);
                // following metadata is required to create, but not for import. Check always because it is a simple checking. Codes must not be checked, because variable element is optional
                ValidationUtils.checkMetadataRequired(codelistVersion.getVariable(), ServiceExceptionParameters.CODELIST_VARIABLE, exceptionsCodelist);
                // One code at least
                Long itemsCount = codeRepository.countItems(codelistVersion.getId());
                if (itemsCount == 0) {
                    exceptionsCodelist.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS));
                }
                // Group exceptions by codelist
                addOrUpdateExceptionItemByResourceUrnWhenExceptionsNonZero(exceptionsByResourceUrn, codelistUrn, exceptionsCodelist);
            }
            // Check codes
            {
                if (codelistVersion.getVariable() != null && VariableTypeEnum.GEOGRAPHICAL.equals(codelistVersion.getVariable().getType())) {
                    codeMetamacRepository.checkCodesWithVariableElements(codelistVersion.getId(), exceptionsByResourceUrn);
                }
            }
            // Check translations
            {
                codesMetamacService.checkCodelistVersionTranslations(ctx, codelistVersion.getId(), getLanguageDefault(), exceptionsByResourceUrn);
            }
            // Throw exception if there is any exception
            throwExceptionsInExceptionsMap(exceptionsByResourceUrn, codelistUrn);
        }

        @Override
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInInternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {
            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);
            String codelistUrn = codelistVersion.getMaintainableArtefact().getUrn();
            Map<String, MetamacExceptionItem> exceptionsByResourceUrn = new HashMap<String, MetamacExceptionItem>();

            // Check codelist
            {
                List<MetamacExceptionItem> exceptionsCodelist = new ArrayList<MetamacExceptionItem>();
                // Metadata required
                ValidationUtils.checkMetadataRequired(codelistVersion.getAccessType(), ServiceExceptionParameters.CODELIST_ACCESS_TYPE, exceptionsCodelist);
                ValidationUtils.checkMetadataRequired(codelistVersion.getDefaultOrderVisualisation(), ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION, exceptionsCodelist);
                ValidationUtils.checkMetadataRequired(codelistVersion.getDefaultOpennessVisualisation(), ServiceExceptionParameters.CODELIST_DEFAULT_OPENNESS_VISUALISATION, exceptionsCodelist);
                // Group exceptions by codelist
                addOrUpdateExceptionItemByResourceUrnWhenExceptionsNonZero(exceptionsByResourceUrn, codelistUrn, exceptionsCodelist);
            }
            // Throw exception if there is any exception
            throwExceptionsInExceptionsMap(exceptionsByResourceUrn, codelistUrn);
        }

        @Override
        public Object publishInternallyConcreteResource(ServiceContext ctx, Object srmResourceVersion) {
            // Mark leaf codes with openness = false
            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);
            for (CodelistOpennessVisualisation codelistOpennessVisualisation : codelistVersion.getOpennessVisualisations()) {
                // IMPORTANT! If default value to leaf codes is changed to false, decide what to do with default configuration with all expanded
                codeMetamacRepository.updateCodeOpennessColumnToLeafCodes(codelistVersion, codelistOpennessVisualisation.getColumnIndex(), Boolean.TRUE);
            }
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {
            codesMetamacService.checkCodelistWithRelatedResourcesExternallyPublished(ctx, getCodelistVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion, Boolean forceLastestFinal) throws MetamacException {
            return codesService.markCodelistAsFinal(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), forceLastestFinal);
        }

        @Override
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CodelistVersionMetamac codelistVersionMetamac = getCodelistVersionMetamac(srmResourceVersion);

            if (AccessTypeEnum.PUBLIC.equals(codelistVersionMetamac.getAccessType())) {
                return codesService.markCodelistAsPublic(ctx, codelistVersionMetamac.getMaintainableArtefact().getUrn());
            } else {
                // Do not mark as public to avoid retrieve in external API
                return srmResourceVersion;
            }
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return codesMetamacService.startCodelistValidity(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return codesMetamacService.endCodelistValidity(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus) {

            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.itemScheme().id())
                    .eq(codelistVersion.getItemScheme().getId()).withProperty(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<CodelistVersionMetamac> codelistVersionPagedResult = codelistVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return codelistMetamacToObject(codelistVersionPagedResult.getValues());
        }

        @Override
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds) {
            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                    .withMessageParameters(codelistVersion.getMaintainableArtefact().getUrn(), procStatusExpecteds).build();
        }

        @Override
        public Boolean canHaveCategorisations() {
            return Boolean.TRUE;
        }

        @Override
        public Object mergeTemporal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CodelistVersionMetamac codelistVersionMetamac = (CodelistVersionMetamac) srmResourceVersion;
            if (VersionUtil.isTemporalVersion(codelistVersionMetamac.getMaintainableArtefact().getVersionLogic())) {
                return codesMetamacService.mergeTemporalVersion(ctx, codelistVersionMetamac.getMaintainableArtefact().getUrn());
            }
            return srmResourceVersion;
        }

        @Override
        public Boolean isTemporalToPublishExternally(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            CodelistVersionMetamac codelistVersionMetamac = (CodelistVersionMetamac) srmResourceVersion;
            if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(codelistVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        private CodelistVersionMetamac getCodelistVersionMetamac(Object srmResource) {
            return (CodelistVersionMetamac) srmResource;
        }

        private List<Object> codelistMetamacToObject(List<CodelistVersionMetamac> codelistVersions) {
            List<Object> objects = new ArrayList<Object>(codelistVersions.size());
            for (CodelistVersionMetamac codelistVersion : codelistVersions) {
                objects.add(codelistVersion);
            }
            return objects;
        }

    }
}
