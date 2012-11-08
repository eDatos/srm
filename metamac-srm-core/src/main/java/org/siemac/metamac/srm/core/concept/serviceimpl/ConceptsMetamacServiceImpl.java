package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRelation;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRelationRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.enume.domain.ConceptRelationTypeEnum;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsDoCopyUtils.ConceptCopyCallback;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

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
    private ConceptRelationRepository   conceptRelationRepository;

    @Autowired
    @Qualifier("conceptSchemeLifeCycle")
    private LifeCycle                   conceptSchemeLifeCycle;

    @Autowired
    @Qualifier("conceptCopyCallbackMetamac")
    private ConceptCopyCallback         conceptCopyCallback;

    public ConceptsMetamacServiceImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);

        // Fill metadata
        conceptSchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public ConceptSchemeVersionMetamac updateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConceptScheme(conceptSchemeVersion, null);
        // ConceptsService checks conceptScheme isn't final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

        // if this version is not the first one, check not modify 'type'
        if (!isConceptSchemeFirstVersion(conceptSchemeVersion)) {
            ConceptSchemeVersionMetamac conceptSchemePreviousVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.findByVersion(conceptSchemeVersion.getItemScheme().getId(),
                    conceptSchemeVersion.getMaintainableArtefact().getReplaceTo());
            if (!conceptSchemePreviousVersion.getType().equals(conceptSchemeVersion.getType())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE).build();
            }
        }

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

    // TODO Para llevar a cabo la publicación interna de un recurso será necesario que previamente exista al menos un anuncio sobre el esquema de conceptos a publicar
    @Override
    public ConceptSchemeVersionMetamac publishInternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.publishInternally(ctx, urn);
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    @Override
    public ConceptSchemeVersionMetamac publishExternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Check concepts of other concept scheme dont relate concept of this scheme by 'extends' metadata
        ConceptMetamac conceptRelationExtends = getConceptMetamacRepository().findOneConceptWithConceptExtendsOfConceptSchemeVersion(urn);
        if (conceptRelationExtends != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WITH_RELATED_CONCEPTS)
                    .withMessageParameters(conceptRelationExtends.getItemSchemeVersion().getMaintainableArtefact().getUrn(), conceptRelationExtends.getNameableArtefact().getUrn()).build();
        }

        // Delete related concepts
        ConceptSchemeVersion conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, urn);
        List<ConceptRelation> relatedConceptsAllConceptSchemeVersion = findConceptsRelationsBidirectionalByConceptSchemeVersion(conceptSchemeVersion.getMaintainableArtefact().getUrn());
        for (ConceptRelation relatedConcept : relatedConceptsAllConceptSchemeVersion) {
            conceptRelationRepository.delete(relatedConcept);
        }

        // Note: ConceptsService checks conceptScheme isn't final and other conditions
        conceptsService.deleteConceptScheme(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkVersioningConceptScheme(urnToCopy, versionType, null, null);
        checkVersioningConceptSchemeIsSupported(ctx, urnToCopy);

        // Versioning
        ConceptSchemeVersionMetamac conceptSchemeNewVersion = (ConceptSchemeVersionMetamac) conceptsService.versioningConceptScheme(ctx, urnToCopy, versionType, conceptCopyCallback);

        // Copy concept relations (metadata of Metamac 'relatedConcepts'). Note: metadata 'roles' is copied in SDMX module
        String conceptSchemeNewVersionUrn = conceptSchemeNewVersion.getMaintainableArtefact().getUrn();
        List<ConceptRelation> relatedConcepts = findConceptsRelationsBidirectionalByConceptSchemeVersion(urnToCopy);
        for (ConceptRelation conceptRelationOldVersion : relatedConcepts) {
            String concept1CodeVersionToCopy = conceptRelationOldVersion.getConcept1().getNameableArtefact().getCode();
            String concept2CodeVersionToCopy = conceptRelationOldVersion.getConcept2().getNameableArtefact().getCode();

            Concept concept1NewVersion = conceptRepository.findByCodeInConceptSchemeVersion(concept1CodeVersionToCopy, conceptSchemeNewVersionUrn);
            Concept concept2NewVersion = conceptRepository.findByCodeInConceptSchemeVersion(concept2CodeVersionToCopy, conceptSchemeNewVersionUrn);

            ConceptRelation conceptRelation = new ConceptRelation(ConceptRelationTypeEnum.BIDIRECTIONAL, concept1NewVersion, concept2NewVersion);
            conceptRelation = conceptRelationRepository.save(conceptRelation);
        }

        return conceptSchemeNewVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac endConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkEndConceptSchemeValidity(urn, null, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamacRepository().retrieveConceptSchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.endConceptSchemeValidity(ctx, urn, null);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptMetamac createConcept(ServiceContext ctx, String conceptSchemeUrn, ConceptMetamac concept) throws MetamacException {

        // Validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = null;
        if (conceptSchemeUrn != null) {
            conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        }
        ConceptsMetamacInvocationValidator.checkCreateConcept(conceptSchemeVersion, concept, null);
        // ConceptsService checks conceptScheme isn't final

        // Save concept
        return (ConceptMetamac) conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
    }

    @Override
    public ConceptMetamac updateConcept(ServiceContext ctx, ConceptMetamac concept) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersion = null;
        if (concept != null && concept.getItemSchemeVersion() != null && concept.getItemSchemeVersion().getMaintainableArtefact() != null
                && concept.getItemSchemeVersion().getMaintainableArtefact().getUrn() != null) {
            conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, concept.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        }
        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConcept(conceptSchemeVersion, concept, null);
        // ConceptsService checks conceptScheme isn't final

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
        // concept scheme with validity started (= externally published)
        ConditionalCriteria validityStartedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().validFrom()).isNotNull().buildSingle();
        conditions.add(validityStartedCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    // TODO Pendiente de confirmación de Alberto: se está lanzando excepción si hay conceptos relacionados
    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {

        Concept concept = retrieveConceptByUrn(ctx, urn);

        // Check concept has not related concepts (metadatas 'relatedConcepts' and 'extends')
        checkToDeleteConceptHierarchyWithoutRelations(concept);

        // Note: ConceptsService checks conceptScheme isn't final
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
    public ConceptRelation addConceptRelation(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkAddConceptRelation(urn1, urn2, null);

        // Check not exists
        ConceptRelation conceptRelation = findConceptRelationBidirectionalByConcepts(urn1, urn2);
        if (conceptRelation != null) {
            return conceptRelation;
        }
        // Not same concept
        if (urn1.equals(urn2)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concepts must be different").build();
        }

        // Create
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);

        // Same concept scheme
        if (!concept1.getItemSchemeVersion().getMaintainableArtefact().getUrn().equals(concept2.getItemSchemeVersion().getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concept scheme must be same in two concepts").build();
        }
        // Concept scheme not published
        retrieveConceptSchemeVersionCanBeModified(ctx, concept1.getItemSchemeVersion().getMaintainableArtefact().getUrn()); // note: itemScheme is the same to two concepts

        conceptRelation = new ConceptRelation(ConceptRelationTypeEnum.BIDIRECTIONAL, concept1, concept2);
        conceptRelation = conceptRelationRepository.save(conceptRelation);

        return conceptRelation;
    }

    @Override
    public void deleteConceptRelation(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteConceptRelation(urn1, urn2, null);

        // Retrieve
        ConceptRelation conceptRelation = findConceptRelationBidirectionalByConcepts(urn1, urn2);
        if (conceptRelation == null) {
            return;
        }

        // Concept scheme not published
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptRelation.getConcept1().getItemSchemeVersion().getMaintainableArtefact().getUrn()); // note: itemScheme is the same to two concepts

        // Delete
        conceptRelationRepository.delete(conceptRelation);
    }

    @Override
    public List<ConceptMetamac> retrieveRelatedConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveRelatedConcepts(urn, null);

        // Retrieve
        List<ConceptRelation> conceptsRelations = retrieveConceptsRelationsBidirectionalByConcept(urn);

        List<ConceptMetamac> relatedConcepts = new ArrayList<ConceptMetamac>();
        for (ConceptRelation conceptRelation : conceptsRelations) {
            if (!conceptRelation.getConcept1().getNameableArtefact().getUrn().equals(urn)) {
                relatedConcepts.add((ConceptMetamac) conceptRelation.getConcept1());
            } else if (!conceptRelation.getConcept2().getNameableArtefact().getUrn().equals(urn)) {
                relatedConcepts.add((ConceptMetamac) conceptRelation.getConcept2());
            }
        }
        return relatedConcepts;
    }

    @Override
    public ConceptRelation addConceptRelationRoles(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.checkAddConceptRelationRoles(urn, conceptRoleUrn, null);

        Concept concept = retrieveConceptByUrn(ctx, urn);
        Concept conceptRole = retrieveConceptByUrn(ctx, conceptRoleUrn);

        // Check concept scheme of concept 'urn' is Operation or Transversal (it is retrieved by conceptsService to avoid ClassCastException)
        ConceptSchemeVersionMetamac conceptSchemeVersionOfConcept = (ConceptSchemeVersionMetamac) conceptsService.retrieveConceptSchemeByUrn(ctx, concept.getItemSchemeVersion()
                .getMaintainableArtefact().getUrn());
        if (!ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersionOfConcept.getType()) && !ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersionOfConcept.getType())) {
            throw MetamacExceptionBuilder
                    .builder()
                    .withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(conceptSchemeVersionOfConcept.getMaintainableArtefact().getUrn(),
                            new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_OPERATION, ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_TRANSVERSAL}).build();
        }
        // Check concept scheme of concept 'conceptRoleUrn' is Role
        ConceptSchemeVersionMetamac conceptSchemeVersionOfRole = (ConceptSchemeVersionMetamac) conceptsService.retrieveConceptSchemeByUrn(ctx, conceptRole.getItemSchemeVersion()
                .getMaintainableArtefact().getUrn());
        if (!ConceptSchemeTypeEnum.ROLE.equals(conceptSchemeVersionOfRole.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(conceptSchemeVersionOfRole.getMaintainableArtefact().getUrn(), new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_ROLE}).build();
        }

        return conceptsService.addConceptRelationRoles(ctx, urn, conceptRoleUrn);
    }

    @Override
    public void deleteConceptRelationRoles(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.checkDeleteConceptRelationRoles(urn, conceptRoleUrn, null);

        conceptsService.deleteConceptRelationRoles(ctx, urn, conceptRoleUrn);
    }

    @Override
    public List<ConceptMetamac> retrieveRelatedConceptsRoles(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsInvocationValidator.checkRetrieveRelatedConceptsRoles(urn, null);

        // Retrieve
        List<Concept> conceptsRole = conceptsService.retrieveRelatedConceptsRoles(ctx, urn);
        List<ConceptMetamac> conceptsRoleMetamac = conceptsToConceptMetamac(conceptsRole);

        return conceptsRoleMetamac;
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

    private void checkToDeleteConceptHierarchyWithoutRelations(Concept concept) throws MetamacException {

        String conceptUrn = concept.getNameableArtefact().getUrn();

        // Check there is not any concept that relates concept to delete by metadata 'extends'
        ConceptMetamac conceptRelationExtends = getConceptMetamacRepository().findOneConceptByConceptExtends(concept.getId());
        if (conceptRelationExtends != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_WITH_RELATED_CONCEPTS)
                    .withMessageParameters(conceptUrn, conceptRelationExtends.getNameableArtefact().getUrn()).build();
        }

        // Check there is not any concept that relates concept to delete by 'related concepts'
        List<ConceptRelation> conceptsRelations = retrieveConceptsRelationsBidirectionalByConcept(conceptUrn);
        if (conceptsRelations.size() != 0) {
            // in exception, say only one relation by example
            String conceptRelationUrn = conceptsRelations.get(0).getConcept1().getNameableArtefact().getUrn();
            if (conceptUrn.equals(conceptRelationUrn)) {
                conceptRelationUrn = conceptsRelations.get(0).getConcept2().getNameableArtefact().getUrn();
            }
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_WITH_RELATED_CONCEPTS).withMessageParameters(conceptUrn, conceptRelationUrn).build();
        }
        for (Item item : concept.getChildren()) {
            Concept child = (Concept) item;
            checkToDeleteConceptHierarchyWithoutRelations(child);
        }
    }
    /**
     * Retrieves version of a concept scheme, checking that can be modified
     */
    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionCanBeModified(ServiceContext ctx, String urn) throws MetamacException {
        return getConceptSchemeVersionMetamacRepository().retrieveConceptSchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION});
    }

    private ConceptRelation findConceptRelationBidirectionalByConcepts(String urn1, String urn2) {
        ConceptRelation conceptRelation = null;

        // First possible order
        conceptRelation = conceptRelationRepository.findConceptRelation(urn1, urn2, ConceptRelationTypeEnum.BIDIRECTIONAL);
        if (conceptRelation != null) {
            return conceptRelation;
        }

        // Second possible order
        conceptRelation = conceptRelationRepository.findConceptRelation(urn2, urn1, ConceptRelationTypeEnum.BIDIRECTIONAL);
        if (conceptRelation != null) {
            return conceptRelation;
        }

        return null;
    }

    private List<ConceptRelation> retrieveConceptsRelationsBidirectionalByConcept(String urn) {

        List<ConceptRelation> conceptsRelations1 = conceptRelationRepository.findByConcept1(urn, ConceptRelationTypeEnum.BIDIRECTIONAL);
        List<ConceptRelation> conceptsRelations2 = conceptRelationRepository.findByConcept2(urn, ConceptRelationTypeEnum.BIDIRECTIONAL);

        List<ConceptRelation> conceptsRelations = new ArrayList<ConceptRelation>();
        conceptsRelations.addAll(conceptsRelations1);
        conceptsRelations.addAll(conceptsRelations2);
        return conceptsRelations;
    }

    private List<ConceptRelation> findConceptsRelationsBidirectionalByConceptSchemeVersion(String urn) {
        // can search by concept1 or concept2, because conceptSchemeVersion is same
        return conceptRelationRepository.findByConceptSchemeVersionSearchingByConcept2(urn, ConceptRelationTypeEnum.BIDIRECTIONAL);
    }

    private Boolean isConceptSchemeFirstVersion(ItemSchemeVersion itemSchemeVersion) {
        return itemSchemeVersion.getMaintainableArtefact().getReplaceTo() == null;
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

    private void checkVersioningConceptSchemeIsSupported(ServiceContext ctx, String urnToCopy) throws MetamacException {

        // Retrieve version to copy and check it is final (internally published)
        ConceptSchemeVersion conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);
        if (!conceptSchemeVersionToCopy.getMaintainableArtefact().getFinalLogic()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(conceptSchemeVersionToCopy.getMaintainableArtefact().getUrn()).build();
        }
        // Check does not exist any version 'no final'
        ItemSchemeVersion conceptSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinal(conceptSchemeVersionToCopy.getItemScheme().getId());
        if (conceptSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(conceptSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }
}