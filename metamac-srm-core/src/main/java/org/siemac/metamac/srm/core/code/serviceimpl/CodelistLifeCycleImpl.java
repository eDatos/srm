package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;

@Service("codelistLifeCycle")
public class CodelistLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private ItemSchemeVersionRepository      itemSchemeVersionRepository;

    @Autowired
    private CodelistVersionMetamacRepository codelistVersionMetamacRepository;

    @Autowired
    private CodesService                     codesService;

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
        public Object updateSrmResource(Object srmResourceVersion) {
            return itemSchemeVersionRepository.save(getCodelistVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return codelistVersionMetamacRepository.retrieveCodelistVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void checkConcreteResourceInProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {

            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);

            // Metadata required
            ValidationUtils.checkMetadataRequired(codelistVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);
            // following metadata is required to create, but not for import. Check always because it is a simple checking. Codes must not be checked, because variable element is optional
            ValidationUtils.checkMetadataRequired(codelistVersion.getVariable(), ServiceExceptionParameters.CODELIST_VARIABLE, exceptions);

            // One code at least
            if (codelistVersion.getItems().size() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITHOUT_ITEMS, codelistVersion.getMaintainableArtefact().getUrn()));
            }
        }

        @Override
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInInternallyPublished(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // Metadata required
            CodelistVersionMetamac codelistVersion = getCodelistVersionMetamac(srmResourceVersion);
            ValidationUtils.checkMetadataRequired(codelistVersion.getAccessType(), ServiceExceptionParameters.CODELIST_ACCESS_TYPE, exceptions);
            ValidationUtils.checkMetadataRequired(codelistVersion.getDefaultOrderVisualisation(), ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION, exceptions);
            ValidationUtils.checkMetadataRequired(codelistVersion.getDefaultOpennessVisualisation(), ServiceExceptionParameters.CODELIST_DEFAULT_OPENNESS_VISUALISATION, exceptions);
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return codesService.markCodelistAsFinal(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return codesService.markCodelistAsPublic(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return codesService.startCodelistValidity(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), null);
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return codesService.endCodelistValidity(ctx, getCodelistVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), null);
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
