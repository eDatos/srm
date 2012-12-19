package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsDoCopyUtils.ConceptCopyCallback;

/**
 * Implementation of ConceptsMetamacService.
 */
@Service("conceptsMetamacService")
public class ConceptsMetamacServiceImpl extends ConceptsMetamacServiceImplBase {

    @Autowired
    private ConceptsService             conceptsService;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Autowired
    private ConceptRepository           conceptRepository;

    @Autowired
    @Qualifier("conceptSchemeLifeCycle")
    private LifeCycle                   conceptSchemeLifeCycle;

    @Autowired
    private SrmValidation               srmValidation;

    @Autowired
    @Qualifier("conceptCopyCallbackMetamac")
    private ConceptCopyCallback         conceptCopyCallback;

    public ConceptsMetamacServiceImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);
        checkConceptSchemeToCreateOrUpdate(ctx, conceptSchemeVersion);

        // Fill metadata
        conceptSchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        conceptSchemeVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public ConceptSchemeVersionMetamac updateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConceptScheme(conceptSchemeVersion, null);
        checkConceptSchemeToCreateOrUpdate(ctx, conceptSchemeVersion);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.updateConceptScheme(ctx, conceptSchemeVersion);
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
    }

    @Override
    public List<ConceptSchemeVersionMetamac> retrieveConceptSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {

        // Retrieve conceptSchemeVersions
        List<ConceptSchemeVersion> conceptSchemeVersions = conceptsService.retrieveConceptSchemeVersions(ctx, urn);

        // Typecast to ConceptSchemeVersionMetamac
        return conceptSchemeVersionsToConceptSchemeVersionsMetamac(conceptSchemeVersions);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<ConceptSchemeVersion> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptSchemeVersionPagedResult);
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac rejectConceptSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac rejectConceptSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac publishInternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.publishInternally(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac publishExternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, urn);
        checkConceptSchemeCanBeModified(conceptSchemeVersion);

        // Delete
        conceptsService.deleteConceptScheme(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkVersioningConceptScheme(urnToCopy, versionType, null, null);
        checkConceptSchemeToVersioning(ctx, urnToCopy);

        // Versioning
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptSchemeVersionMetamac conceptSchemeNewVersion = (ConceptSchemeVersionMetamac) conceptsService.versioningConceptScheme(ctx, urnToCopy, versionType, conceptCopyCallback);

        // Versioning related concepts (metadata of Metamac 'relatedConcepts'). Note: other relations are copied in copy callback
        for (Item conceptToCopyRelatedConcepts : conceptSchemeVersionToCopy.getItems()) {
            versioningRelatedConcepts((ConceptMetamac) conceptToCopyRelatedConcepts, conceptSchemeNewVersion.getMaintainableArtefact().getUrn());
        }
        return conceptSchemeNewVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac endConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkEndValidity(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamacRepository().retrieveConceptSchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.endConceptSchemeValidity(ctx, urn, null);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptMetamac createConcept(ServiceContext ctx, String conceptSchemeUrn, ConceptMetamac concept) throws MetamacException {
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        // Save concept
        return (ConceptMetamac) conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
    }

    @Override
    public ConceptMetamac updateConcept(ServiceContext ctx, ConceptMetamac concept) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByConceptUrn(ctx, concept.getNameableArtefact().getUrn());

        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        return (ConceptMetamac) conceptsService.updateConcept(ctx, concept);
    }

    @Override
    public ConceptMetamac retrieveConceptByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptMetamac) conceptsService.retrieveConceptByUrn(ctx, urn);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsAsRoleByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be Role
        // concept scheme must be Role
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder
                .criteriaFor(ConceptMetamac.class)
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class))
                .eq(ConceptSchemeTypeEnum.ROLE).buildSingle();
        conditions.add(roleCondition);
        // concept scheme externally published
        ConditionalCriteria validityStartedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(validityStartedCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {

        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByConceptUrn(ctx, urn);
        checkConceptSchemeCanBeModified(conceptSchemeVersion);

        // Delete bidirectional relations of concepts relate this concept and its children (will be removed in cascade)
        removeRelatedConceptsBidirectional(concept);

        conceptsService.deleteConcept(ctx, urn);
    }

    @Override
    public List<ConceptMetamac> retrieveConceptsByConceptSchemeUrn(ServiceContext ctx, String conceptSchemeUrn) throws MetamacException {

        // Retrieve
        List<Concept> concepts = conceptsService.retrieveConceptsByConceptSchemeUrn(ctx, conceptSchemeUrn);

        // Typecast
        List<ConceptMetamac> conceptsMetamac = conceptsToConceptMetamac(concepts);
        return conceptsMetamac;
    }

    @Override
    public void addRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkAddRelatedConcept(urn1, urn2, null);

        // Not same concept
        if (urn1.equals(urn2)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concepts must be different").build();
        }
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);
        // Same concept scheme
        if (!concept1.getItemSchemeVersion().getMaintainableArtefact().getUrn().equals(concept2.getItemSchemeVersion().getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concept scheme must be same in two concepts").build();
        }
        // Concept scheme not published
        retrieveConceptSchemeVersionCanBeModified(ctx, concept1.getItemSchemeVersion().getMaintainableArtefact().getUrn()); // note: itemScheme is the same to two concepts

        // Not add relation if is already added
        for (Concept conceptRelatedActual : concept1.getRelatedConcepts()) {
            if (conceptRelatedActual.getNameableArtefact().getUrn().equals(concept2.getNameableArtefact().getUrn())) {
                return;
            }
        }

        // Create bidirectional relation
        concept1.addRelatedConcept(concept2);
        getConceptMetamacRepository().save(concept1);
        concept2.addRelatedConcept(concept1);
        getConceptMetamacRepository().save(concept2);
    }

    @Override
    public void deleteRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteRelatedConcept(urn1, urn2, null);

        // Concept scheme not published
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        retrieveConceptSchemeVersionCanBeModified(ctx, concept1.getItemSchemeVersion().getMaintainableArtefact().getUrn());

        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);

        // Delete
        concept1.removeRelatedConcept(concept2);
        getConceptMetamacRepository().save(concept1);
        concept2.removeRelatedConcept(concept1);
        getConceptMetamacRepository().save(concept2);
    }

    @Override
    public List<ConceptMetamac> retrieveRelatedConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveRelatedConcepts(urn, null);

        // Retrieve
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        return concept.getRelatedConcepts();
    }

    @Override
    public void addRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkAddRoleConcept(urn, conceptRoleUrn, null);

        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ConceptMetamac conceptRole = retrieveConceptByUrn(ctx, conceptRoleUrn);
        // Check concept scheme of concept 'urn' can be modified and it is Operation, Transversal or Measure (it is retrieved instead navigate across relation to avoid ClassCastException)
        ConceptSchemeVersionMetamac conceptSchemeVersionOfConcept = retrieveConceptSchemeVersionCanBeModified(ctx, concept.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        if (!ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersionOfConcept.getType()) && !ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersionOfConcept.getType())
                && !ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionOfConcept.getType())) {
            throw MetamacExceptionBuilder
                    .builder()
                    .withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(
                            conceptSchemeVersionOfConcept.getMaintainableArtefact().getUrn(),
                            new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_OPERATION, ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_TRANSVERSAL,
                                    ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_MEASURE}).build();
        }
        // Check concept scheme of concept 'conceptRoleUrn' is Role
        ConceptSchemeVersionMetamac conceptSchemeVersionOfRole = retrieveConceptSchemeByUrn(ctx, conceptRole.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        if (!ConceptSchemeTypeEnum.ROLE.equals(conceptSchemeVersionOfRole.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(conceptSchemeVersionOfRole.getMaintainableArtefact().getUrn(), new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_ROLE}).build();
        }
        // Check concept scheme of Role is externally published
        SrmValidationUtils.checkArtefactProcStatus(conceptSchemeVersionOfRole.getLifeCycleMetadata(), conceptSchemeVersionOfRole.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.EXTERNALLY_PUBLISHED);
        // Not add relation if Role is already added
        for (Concept conceptRoleActual : concept.getRoleConcepts()) {
            if (conceptRoleActual.getNameableArtefact().getUrn().equals(conceptRole.getNameableArtefact().getUrn())) {
                return;
            }
        }

        // Add Role
        concept.addRoleConcept(conceptRole);
        getConceptMetamacRepository().save(concept);
    }

    @Override
    public void deleteRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteRoleConcept(urn, conceptRoleUrn, null);

        // Check concept scheme of concept 'urn' can be modified
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        retrieveConceptSchemeVersionCanBeModified(ctx, concept.getItemSchemeVersion().getMaintainableArtefact().getUrn());

        // Delete
        ConceptMetamac conceptRole = retrieveConceptByUrn(ctx, conceptRoleUrn);
        concept.removeRoleConcept(conceptRole);
        getConceptMetamacRepository().save(concept);
    }

    @Override
    public List<ConceptMetamac> retrieveRoleConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveRoleConcepts(urn, null);

        // Retrieve
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        return concept.getRoleConcepts();
    }

    @Override
    public List<ConceptType> findAllConceptTypes(ServiceContext ctx) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindAllConceptTypes(null);

        // Find
        return getConceptTypeRepository().findAll();
    }

    @Override
    public ConceptType retrieveConceptTypeByIdentifier(ServiceContext ctx, String identifier) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptTypeByIdentifier(identifier, null);

        // Retrieve
        ConceptType conceptType = getConceptTypeRepository().findByIdentifier(identifier);
        if (conceptType == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_TYPE_NOT_FOUND).withMessageParameters(identifier).build();
        }
        return conceptType;
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeByConceptUrn(ServiceContext ctx, String conceptUrn) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptSchemeByConceptUrn(conceptUrn, null);

        // Retrieve
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamacRepository().findByConcept(conceptUrn);
        if (conceptSchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(conceptUrn).build();
        }
        return conceptSchemeVersion;
    }

    /**
     * Retrieves version of a concept scheme, checking that can be modified
     */
    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionCanBeModified(ServiceContext ctx, String urn) throws MetamacException {
        return getConceptSchemeVersionMetamacRepository().retrieveConceptSchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION});
    }

    /**
     * Typecast to Metamac type
     */
    private List<ConceptMetamac> conceptsToConceptMetamac(List<Concept> sources) {
        List<ConceptMetamac> targets = new ArrayList<ConceptMetamac>();
        for (Item source : sources) {
            targets.add((ConceptMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<ConceptSchemeVersionMetamac> conceptSchemeVersionsToConceptSchemeVersionsMetamac(List<ConceptSchemeVersion> sources) {
        List<ConceptSchemeVersionMetamac> targets = new ArrayList<ConceptSchemeVersionMetamac>();
        for (ItemSchemeVersion source : sources) {
            targets.add((ConceptSchemeVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<ConceptSchemeVersionMetamac> pagedResultConceptSchemeVersionToMetamac(PagedResult<ConceptSchemeVersion> source) {
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionsMetamac = conceptSchemeVersionsToConceptSchemeVersionsMetamac(source.getValues());
        return new PagedResult<ConceptSchemeVersionMetamac>(conceptSchemeVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<ConceptMetamac> pagedResultConceptToMetamac(PagedResult<Concept> source) {
        List<ConceptMetamac> conceptsMetamac = conceptsToConceptMetamac(source.getValues());
        return new PagedResult<ConceptMetamac>(conceptsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    private void checkConceptSchemeToVersioning(ServiceContext ctx, String urnToCopy) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(conceptSchemeVersionToCopy.getLifeCycleMetadata(), urnToCopy);
        // Check does not exist any version 'no final'
        ItemSchemeVersion conceptSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinal(conceptSchemeVersionToCopy.getItemScheme().getId());
        if (conceptSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(conceptSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void removeRelatedConceptsBidirectional(ConceptMetamac relatedConceptToRemove) {
        for (ConceptMetamac relatedConcept : relatedConceptToRemove.getRelatedConcepts()) {
            relatedConcept.removeRelatedConcept(relatedConceptToRemove);
            getConceptMetamacRepository().save(relatedConcept);
        }
        // Children (will be removed in cascade)
        for (Item child : relatedConceptToRemove.getChildren()) {
            removeRelatedConceptsBidirectional((ConceptMetamac) child);
        }
    }

    private void versioningRelatedConcepts(ConceptMetamac conceptToCopy, String conceptSchemeNewVersionUrn) {
        if (conceptToCopy.getRelatedConcepts().size() == 0) {
            return;
        }

        ConceptMetamac conceptIntNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptToCopy.getNameableArtefact().getCode(), conceptSchemeNewVersionUrn);
        // Copy relations with concepts in new version
        for (ConceptMetamac relatedConcept : conceptToCopy.getRelatedConcepts()) {
            ConceptMetamac relatedConceptIntNewVersion = (ConceptMetamac) conceptRepository
                    .findByCodeInConceptSchemeVersion(relatedConcept.getNameableArtefact().getCode(), conceptSchemeNewVersionUrn);
            conceptIntNewVersion.addRelatedConcept(relatedConceptIntNewVersion);
        }
    }

    /**
     * Common validations to create or update a concept scheme
     */
    private void checkConceptSchemeToCreateOrUpdate(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        if (conceptSchemeVersion.getId() != null) {
            // Proc status
            checkConceptSchemeCanBeModified(conceptSchemeVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(conceptSchemeVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, conceptSchemeVersion.getMaintainableArtefact(), conceptSchemeVersion.getMaintainableArtefact().getIsImported());

        // if this version is not the first one, check not modify 'type'
        if (!SrmServiceUtils.isItemSchemeFirstVersion(conceptSchemeVersion)) {
            ConceptSchemeVersionMetamac conceptSchemePreviousVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.findByVersion(conceptSchemeVersion.getItemScheme().getId(),
                    conceptSchemeVersion.getMaintainableArtefact().getReplaceToVersion());
            if (!conceptSchemePreviousVersion.getType().equals(conceptSchemeVersion.getType())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE).build();
            }
        }
    }
    /**
     * Common validations to create or update a concept
     */
    private void checkConceptToCreateOrUpdate(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) throws MetamacException {
        checkConceptSchemeCanBeModified(conceptSchemeVersion);
        checkConceptMetadataExtends(ctx, concept, conceptSchemeVersion.getMaintainableArtefact().getUrn());
    }

    private void checkConceptMetadataExtends(ServiceContext ctx, ConceptMetamac concept, String conceptSchemeUrn) throws MetamacException {
        if (concept.getConceptExtends() == null) {
            return;
        }

        // Check concept scheme source: type
        ConceptSchemeVersionMetamac conceptSchemeVersionSource = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        if (!ConceptSchemeTypeEnum.GLOSSARY.equals(conceptSchemeVersionSource.getType()) && !ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersionSource.getType())
                && !ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersionSource.getType()) && !ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionSource.getType())) {
            throw MetamacExceptionBuilder
                    .builder()
                    .withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(
                            conceptSchemeUrn,
                            new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_GLOSSARY, ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_OPERATION,
                                    ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_TRANSVERSAL, ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_MEASURE}).build();

        }

        // Check concept scheme target (of extends concept): procStatus and type
        ConceptSchemeVersionMetamac conceptSchemeVersionTarget = retrieveConceptSchemeByUrn(ctx, concept.getConceptExtends().getItemSchemeVersion().getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactProcStatus(conceptSchemeVersionTarget.getLifeCycleMetadata(), conceptSchemeVersionTarget.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.EXTERNALLY_PUBLISHED);
        if (!ConceptSchemeTypeEnum.GLOSSARY.equals(conceptSchemeVersionTarget.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(conceptSchemeVersionTarget.getMaintainableArtefact().getUrn(), new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_GLOSSARY}).build();
        }
    }

    private void checkConceptSchemeCanBeModified(ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(conceptSchemeVersion.getLifeCycleMetadata(), conceptSchemeVersion.getMaintainableArtefact().getUrn());
    }
}