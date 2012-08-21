package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;

/**
 * Implementation of ConceptsMetamacService.
 */
@Service("conceptsMetamacService")
public class ConceptsMetamacServiceImpl extends ConceptsMetamacServiceImplBase {

    @Autowired
    private ConceptsService             conceptsService;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository; // TODO acceder al repositorio o al servicio para hacer "save"?

    public ConceptsMetamacServiceImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);

        // Fill metadata
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
    }

    @Override
    public ConceptSchemeVersionMetamac updateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindConceptSchemesByCondition(conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).build();
        }
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = getConceptSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return conceptSchemeVersionPagedResult;
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkSendConceptSchemeToProductionValidation(urn, null);

        // Retrieve version in draft
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionInProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DRAFT);

        // Validate to send to production
        checkConceptSchemeToSendToProductionValidation(ctx, urn, conceptSchemeVersion);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);
        conceptSchemeVersion.setProductionValidationDate(new DateTime());
        conceptSchemeVersion.setProductionValidationUser(ctx.getUserId());
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);

        return conceptSchemeVersion;
    }

    /**
     * Retrieves version of a concept scheme in specific procStatus
     */
    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionInProcStatus(ServiceContext ctx, String urn, ItemSchemeMetamacProcStatusEnum procStatus) throws MetamacException {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).eq(urn).withProperty(ConceptSchemeVersionMetamacProperties.procStatus()).eq(procStatus).build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = getConceptSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        if (conceptSchemeVersionPagedResult.getValues().size() != 1) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS).withMessageParameters(urn, new String[]{procStatus.name()}).build();
        }
        return conceptSchemeVersionPagedResult.getValues().get(0);
    }

    /**
     * Makes validations to sent to production validation.
     * Checks actual processing status and if it is correct checks conditions to send to production validation
     */
    private void checkConceptSchemeToSendToProductionValidation(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        String[] roles = new String[]{ItemSchemeMetamacProcStatusEnum.DRAFT.name(), ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED.name()};
        if (!ArrayUtils.contains(roles, conceptSchemeVersion.getProcStatus().name())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS).withMessageParameters(urn, roles).build();
        }
        // Check other conditions
        checkConditionsSinceSendToProductionValidation(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**
     * TODO alguna validación sobre los conceptos?
     */
    private void checkConditionsSinceSendToProductionValidation(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {

        // Metadata required
        ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);
    }
}
