package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.domain.srm.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeRepository;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeVersion;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeVersionRepository;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionProperties;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of ConceptsService.
 */
@Service("conceptsService")
public class ConceptsServiceImpl extends ConceptsServiceImplBase {

    @Autowired
    private ItemSchemeRepository        itemSchemeRepository;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    public ConceptsServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ------------------------------------------------------------------------------------

    // TODO ciclo de vida común para todos los ItemScheme
    @Override
    public ConceptSchemeVersion createConceptScheme(ServiceContext ctx, ConceptSchemeVersion conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);
        validateConceptSchemeUnique(ctx, conceptSchemeVersion);

        // Fill metadata
        MaintainableArtefact maintainableArtefact = conceptSchemeVersion.getMaintainableArtefact();
        maintainableArtefact.setVersionLogic(VersionUtil.VERSION_INITIAL_VERSION);
        maintainableArtefact.setValidFrom(null);
        maintainableArtefact.setValidTo(null);
        maintainableArtefact.setFinalLogic(Boolean.FALSE);
        maintainableArtefact.setProcStatus(MaintainableArtefactProcStatusEnum.DRAFT);
        maintainableArtefact.setIsLastVersion(Boolean.TRUE);
        maintainableArtefact.setUrn(generateConceptSchemeUrn(conceptSchemeVersion));
        maintainableArtefact.setReplacedBy(null);
        maintainableArtefact.setReplaceTo(null);
        maintainableArtefact.setUri(null); // does not filled to ConceptScheme

        // Save conceptScheme
        ItemScheme conceptScheme = conceptSchemeVersion.getItemScheme();
        conceptScheme = getItemSchemeRepository().save(conceptScheme);

        // Save draft version
        conceptSchemeVersion.setItemScheme(conceptScheme);
        conceptSchemeVersion = (ConceptSchemeVersion) getItemSchemeVersionRepository().save(conceptSchemeVersion);
        conceptScheme.getVersions().add(conceptSchemeVersion);
        getItemSchemeRepository().save(conceptScheme);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptSchemeVersion findConceptSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.checkFindConceptSchemeByUrn(urn, null);

        // Retrieve
        ConceptSchemeVersion conceptSchemeVersion = findConceptSchemeVersionByUrn(urn);
        return conceptSchemeVersion;
    }

    @Override
    public List<ConceptSchemeVersion> retrieveConceptSchemeHistoric(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.retrieveConceptSchemeVersions(urn, null);

        // Retrieve all versions
        ConceptSchemeVersion conceptSchemeVersion = findConceptSchemeVersionByUrn(urn);
        List<ItemSchemeVersion> itemSchemeVersions = conceptSchemeVersion.getItemScheme().getVersions();

        // Type cast
        List<ConceptSchemeVersion> conceptSchemeVersions = new ArrayList<ConceptSchemeVersion>();
        for (ItemSchemeVersion itemSchemeVersion : itemSchemeVersions) {
            conceptSchemeVersions.add((ConceptSchemeVersion) itemSchemeVersion);
        }
        return conceptSchemeVersions;
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Validation of parameters
        ConceptsInvocationValidator.checkDeleteConceptScheme(urn, null);

        // Retrieve concept scheme and check it is in draft
        ConceptSchemeVersion conceptSchemeVersion = findConceptSchemeVersionByUrn(urn);
        if (!MaintainableArtefactProcStatusEnum.DRAFT.equals(conceptSchemeVersion.getMaintainableArtefact().getProcStatus())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS)
                    .withMessageParameters(urn, new String[]{ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_PROC_STATUS_DRAFT}).build();
        }
        // Delete whole concept scheme or only last version
        if (VersionUtil.VERSION_INITIAL_VERSION.equals(conceptSchemeVersion.getMaintainableArtefact().getVersionLogic())) {
            // Delete whole concept scheme
            ItemScheme conceptScheme = conceptSchemeVersion.getItemScheme();
            getItemSchemeRepository().delete(conceptScheme);
        } else {
            // Delete draft version
            ItemScheme conceptScheme = conceptSchemeVersion.getItemScheme();
            conceptScheme.getVersions().remove(conceptSchemeVersion);
            getItemSchemeVersionRepository().delete(conceptSchemeVersion);

            // Update previous version
            ItemSchemeVersion conceptSchemePreviousVersion = findConceptSchemeVersionByItemSchemeAndVersion(conceptScheme.getId(), conceptSchemeVersion.getMaintainableArtefact().getReplaceTo());
            conceptSchemePreviousVersion.getMaintainableArtefact().setIsLastVersion(Boolean.TRUE); // previous version is now last version
            conceptSchemePreviousVersion.getMaintainableArtefact().setReplacedBy(null);
            getItemSchemeVersionRepository().save(conceptSchemePreviousVersion);
        }
    }

    @Override
    public PagedResult<ConceptSchemeVersion> findConceptSchemeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.checkFindConceptSchemeByCondition(conditions, pagingParameter, null);

        // Retrieve last versions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(ConceptSchemeVersionProperties.maintainableArtefact().isLastVersion(), Boolean.TRUE));

        // Find
        CriteriaUtils.initCriteriaConditions(conditions, ConceptSchemeVersion.class);
        PagedResult<ConceptSchemeVersion> conceptSchemeVersionPagedResult = getConceptSchemeVersionRepository().findByCondition(conditions, pagingParameter);
        return conceptSchemeVersionPagedResult;
    }

    // }
    // public ConceptScheme updateConceptScheme(ServiceContext ctx, ConceptScheme entity) throws MetamacException {
    //
    // ConceptsInvocationValidator.checkUpdateConceptScheme(entity, null);
    //
    // // TODO: Validate code unique: organization, code, version?
    // // validateConceptSchemeUnique(ctx, entity.getItemScheme().getCode(), null);
    //
    // // TODO actualizar urn y de los hijos, si cambia el code
    // return conceptSchemeRepository.save(entity);
    //
    // }

    /**
     * Check code of concept scheme is unique in mantainer
     * TODO se cambiará el externalItem de mantainer por una entidad propia para Organizaciones. Revisar entonces esta validación
     */
    private void validateConceptSchemeUnique(ServiceContext ctx, ConceptSchemeVersion conceptSchemeVersion) throws MetamacException {

        String conceptSchemeCode = conceptSchemeVersion.getMaintainableArtefact().getCode();
        String maintainerUrn = conceptSchemeVersion.getMaintainableArtefact().getMaintainer().getUrn();
        Long conceptSchemeId = conceptSchemeVersion.getItemScheme().getId();

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersion.class).withProperty(ConceptSchemeVersionProperties.maintainableArtefact().code())
                .ignoreCaseEq(conceptSchemeCode).withProperty(ConceptSchemeVersionProperties.maintainableArtefact().maintainer().urn()).eq(maintainerUrn).distinctRoot().build();

        if (conceptSchemeId != null) {
            ConditionalCriteria conditionNotAnotherVersionSameScheme = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersion.class).not()
                    .withProperty(ConceptSchemeVersionProperties.itemScheme().id()).eq(conceptSchemeId).buildSingle();
            conditions.add(conditionNotAnotherVersionSameScheme);
        }

        PagedResult<ConceptSchemeVersion> conceptSchemesVersions = getConceptSchemeVersionRepository().findByCondition(conditions, PagingParameter.noLimits());
        if (conceptSchemesVersions != null && conceptSchemesVersions.getValues().size() != 0) {
            throw new MetamacException(ServiceExceptionType.CONCEPT_SCHEME_ALREADY_EXIST_CODE_DUPLICATED, conceptSchemeCode, maintainerUrn);
        }
    }

    public ItemSchemeRepository getItemSchemeRepository() {
        return itemSchemeRepository;
    }

    public ItemSchemeVersionRepository getItemSchemeVersionRepository() {
        return itemSchemeVersionRepository;
    }

    private ConceptSchemeVersion findConceptSchemeVersionByUrn(String urn) throws MetamacException {

        // Prepare criteria
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersion.class).withProperty(ConceptSchemeVersionProperties.maintainableArtefact().urn()).eq(urn)
                .distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);

        // Find
        PagedResult<ItemSchemeVersion> result = getItemSchemeVersionRepository().findByCondition(conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND).withMessageParameters(urn).build();
        }

        // Return unique result
        return (ConceptSchemeVersion) result.getValues().get(0);
    }

    private ConceptSchemeVersion findConceptSchemeVersionByItemSchemeAndVersion(Long itemSchemeId, String versionNumber) throws MetamacException {

        // Prepare criteria
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersion.class).withProperty(ConceptSchemeVersionProperties.itemScheme().id()).eq(itemSchemeId)
                .withProperty(ConceptSchemeVersionProperties.maintainableArtefact().versionLogic()).eq(versionNumber).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);

        // Find
        PagedResult<ItemSchemeVersion> result = getItemSchemeVersionRepository().findByCondition(conditions, pagingParameter);
        return (ConceptSchemeVersion) result.getValues().get(0);
    }

    /**
     * Generate urn to concept scheme
     */
    private String generateConceptSchemeUrn(ConceptSchemeVersion conceptSchemeVersion) {
        return GeneratorUrnUtils.generateSdmxConceptSchemeUrn(conceptSchemeVersion.getMaintainableArtefact().getMaintainer().getCode(), conceptSchemeVersion.getMaintainableArtefact().getCode(),
                conceptSchemeVersion.getMaintainableArtefact().getVersionLogic());
    }

    // TODO updateConceptScheme: si cambia el code, cambiar la urn, y la de los concepts asociados
    // TODO updateConceptScheme: testear que cambia code y está repetido en el mismo mantainer 

}
