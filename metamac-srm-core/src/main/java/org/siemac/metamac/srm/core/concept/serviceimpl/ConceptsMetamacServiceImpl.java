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
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptRelation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.DoCopyUtils;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
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

    public ConceptsMetamacServiceImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);

        // Fill metadata
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
    }

    @Override
    public ConceptSchemeVersionMetamac updateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConceptScheme(conceptSchemeVersion, null);
        // ConceptsService checks conceptScheme isn't final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

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

        // Type cast to ConceptSchemeVersionMetamac
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionMetamacs = new ArrayList<ConceptSchemeVersionMetamac>();
        for (ConceptSchemeVersion conceptSchemeVersion : conceptSchemeVersions) {
            conceptSchemeVersionMetamacs.add((ConceptSchemeVersionMetamac) conceptSchemeVersion);
        }

        return conceptSchemeVersionMetamacs;
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindConceptSchemesByCondition(conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = getConceptSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return conceptSchemeVersionPagedResult;
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkSendConceptSchemeToProductionValidation(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DRAFT,
                ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);

        // Validate to send to production
        checkConceptSchemeToSendToProductionValidation(ctx, urn, conceptSchemeVersion);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);
        conceptSchemeVersion.setProductionValidationDate(new DateTime());
        conceptSchemeVersion.setProductionValidationUser(ctx.getUserId());
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkSendConceptSchemeToDiffusionValidation(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // Validate to send to diffusion
        checkConceptSchemeToSendToDiffusionValidation(ctx, urn, conceptSchemeVersion);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);
        conceptSchemeVersion.setDiffusionValidationDate(new DateTime());
        conceptSchemeVersion.setDiffusionValidationUser(ctx.getUserId());
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac rejectConceptSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRejectConceptSchemeProductionValidation(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // Validate to reject
        checkConceptSchemeToRejectProductionValidation(ctx, urn, conceptSchemeVersion);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);
        conceptSchemeVersion.setProductionValidationDate(null);
        conceptSchemeVersion.setProductionValidationUser(null);
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac rejectConceptSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRejectConceptSchemeDiffusionValidation(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // Validate to reject
        checkConceptSchemeToRejectDiffusionValidation(ctx, urn, conceptSchemeVersion);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);
        conceptSchemeVersion.setProductionValidationDate(null);
        conceptSchemeVersion.setProductionValidationUser(null);
        conceptSchemeVersion.setDiffusionValidationDate(null);
        conceptSchemeVersion.setDiffusionValidationUser(null);
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);

        return conceptSchemeVersion;
    }

    // TODO Para llevar a cabo la publicación interna de un recurso será necesario que previamente exista al menos un anuncio sobre el esquema de conceptos a publicar
    @Override
    public ConceptSchemeVersionMetamac publishInternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkPublishInternallyConceptScheme(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // Validate to publish internally
        checkConceptSchemeToPublishInternally(ctx, urn, conceptSchemeVersion);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);
        conceptSchemeVersion.setInternalPublicationDate(new DateTime());
        conceptSchemeVersion.setInternalPublicationUser(ctx.getUserId());
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.markConceptSchemeAsFinal(ctx, urn);

        return conceptSchemeVersion;
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    @Override
    public ConceptSchemeVersionMetamac publishExternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkPublishExternallyConceptScheme(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);

        // Validate to publish externally
        checkConceptSchemeToPublishExternally(ctx, urn, conceptSchemeVersion);

        // Start concept scheme validity
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.startConceptSchemeValidity(ctx, urn);

        // Update proc status
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);
        conceptSchemeVersion.setExternalPublicationDate(new DateTime());
        conceptSchemeVersion.setExternalPublicationUser(ctx.getUserId());
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.save(conceptSchemeVersion);

        // Fill validTo in previous internally published versions
        List<ConceptSchemeVersionMetamac> versionsExternallyPublished = findConceptSchemeVersionsOfConceptSchemeInProcStatus(ctx, conceptSchemeVersion.getItemScheme(),
                ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);
        for (ConceptSchemeVersionMetamac versionExternallyPublished : versionsExternallyPublished) {
            if (versionExternallyPublished.getId().equals(conceptSchemeVersion.getId())) {
                continue;
            }
            versionExternallyPublished.getMaintainableArtefact().setValidTo(conceptSchemeVersion.getExternalPublicationDate());
            itemSchemeVersionRepository.save(versionExternallyPublished);
        }

        return conceptSchemeVersion;
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Delete related concepts TODO intentar borrar más eficientemente
        ConceptSchemeVersion conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, urn);
        List<ConceptRelation> relatedConceptsAllConceptSchemeVersion = findRelatedConceptsByConceptSchemeVersion(conceptSchemeVersion.getMaintainableArtefact().getUrn());
        for (ConceptRelation relatedConcept : relatedConceptsAllConceptSchemeVersion) {
            getConceptRelationRepository().delete(relatedConcept);
        }

        // Note: ConceptsService checks conceptScheme isn't final
        conceptsService.deleteConceptScheme(ctx, urn);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ConceptSchemeVersionMetamac versioningConceptScheme(ServiceContext ctx, String urn, VersionTypeEnum versionType) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkVersioningConceptScheme(urn, versionType, null);

        // Initialize new version, copying values of version selected
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED,
                ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);

        // Check not exists version not published
        List<ConceptSchemeVersionMetamac> versionsNotPublished = findConceptSchemeVersionsOfConceptSchemeInProcStatus(ctx, conceptSchemeVersionToCopy.getItemScheme(),
                ItemSchemeMetamacProcStatusEnum.DRAFT, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION,
                ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);
        if (versionsNotPublished.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(versionsNotPublished.get(0).getMaintainableArtefact().getUrn()).build();
        }

        // Copy values
        ConceptSchemeVersionMetamac conceptSchemeNewVersion = DoCopyUtils.copyConceptSchemeVersionMetamac(conceptSchemeVersionToCopy);
        conceptSchemeNewVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
        List concepts = DoCopyUtils.copyConceptsMetamac(conceptSchemeVersionToCopy);

        // Versioning
        return (ConceptSchemeVersionMetamac) conceptsService.versioningConceptScheme(ctx, conceptSchemeVersionToCopy.getItemScheme(), conceptSchemeNewVersion, concepts, versionType);
    }

    @Override
    public ConceptSchemeVersionMetamac cancelConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkCancelConceptSchemeValidity(urn, null);

        // Retrieve version in specific procStatus
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);

        // Cancel validity
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.endConceptSchemeValidity(ctx, urn);

        return conceptSchemeVersion;
    }

    // TODO metadato 'roles' de sdmx: Se rellena únicamente para esquemas de tipo "operación" y "transversal", y serán relaciones a conceptos de un esquema de tipo rol.
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
    public ConceptMetamac retrieveConceptByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptMetamac) conceptsService.retrieveConceptByUrn(ctx, urn);
    }

    // TODO se lanza error si hay conceptos relacionados. Pendiente de confirmación de Alberto
    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {

        Concept concept = retrieveConceptByUrn(ctx, urn);

        // Check concept has not related concepts
        checkConceptHierarchyWithoutRelatedConcepts(concept);

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
        ConceptRelation conceptRelation = getConceptRelationRepository().find(urn1, urn2);
        if (conceptRelation != null) {
            return conceptRelation;
        }
        
        // Create
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);

        // Concept scheme not published
        retrieveConceptSchemeVersionCanBeModified(ctx, concept1.getItemSchemeVersion().getMaintainableArtefact().getUrn()); // note: itemScheme is the same to two concepts

        conceptRelation = new ConceptRelation(concept1, concept2);
        conceptRelation = getConceptRelationRepository().save(conceptRelation);

        return conceptRelation;
    }

    @Override
    public void deleteConceptRelation(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteConceptRelation(urn1, urn2, null);

        // Retrieve
        ConceptRelation conceptRelation = getConceptRelationRepository().find(urn1, urn2);
        if (conceptRelation == null) {
            return;
        }

        // Concept scheme not published
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptRelation.getConcept1().getItemSchemeVersion().getMaintainableArtefact().getUrn()); // note: itemScheme is the same to two concepts

        // Delete
        getConceptRelationRepository().delete(conceptRelation);
    }

    @Override
    public List<ConceptMetamac> retrieveRelatedConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveRelatedConcepts(urn, null);

        // Retrieve
        List<ConceptRelation> conceptsRelations = getConceptRelationRepository().findByConcept(urn);

        List<ConceptMetamac> relatedConcepts = new ArrayList<ConceptMetamac>();
        for (ConceptRelation conceptRelation : conceptsRelations) {
            if (!conceptRelation.getConcept1().getNameableArtefact().getUrn().equals(urn)) {
                relatedConcepts.add(conceptRelation.getConcept1());
            } else if (!conceptRelation.getConcept2().getNameableArtefact().getUrn().equals(urn)) {
                relatedConcepts.add(conceptRelation.getConcept2());
            }
        }
        return relatedConcepts;
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

    private List<ConceptMetamac> conceptsToConceptMetamac(List<Concept> items) {
        List<ConceptMetamac> concepts = new ArrayList<ConceptMetamac>();
        for (Item item : items) {
            concepts.add((ConceptMetamac) item);
        }
        return concepts;
    }

    /**
     * Retrieves version of a concept scheme in specific procStatus
     */
    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionByProcStatus(ServiceContext ctx, String urn, ItemSchemeMetamacProcStatusEnum... procStatus) throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).eq(urn).withProperty(ConceptSchemeVersionMetamacProperties.procStatus()).in((Object[]) procStatus)
                .distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = getConceptSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);

        if (conceptSchemeVersionPagedResult.getValues().size() != 1) {
            // check concept scheme exists to throws specific exception
            retrieveConceptSchemeByUrn(ctx, urn);

            // if exists, throw exception about wrong proc status
            String[] procStatusString = procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
        return conceptSchemeVersionPagedResult.getValues().get(0);
    }

    /**
     * Finds versions of concept scheme in specific procStatus
     */
    private List<ConceptSchemeVersionMetamac> findConceptSchemeVersionsOfConceptSchemeInProcStatus(ServiceContext ctx, ItemScheme conceptScheme, ItemSchemeMetamacProcStatusEnum... procStatus)
            throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.itemScheme().id())
                .eq(conceptScheme.getId()).withProperty(ConceptSchemeVersionMetamacProperties.procStatus()).in((Object[]) procStatus).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.noLimits();
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = getConceptSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return conceptSchemeVersionPagedResult.getValues();
    }

    /**
     * Makes validations to sent to production validation
     */
    private void checkConceptSchemeToSendToProductionValidation(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkConceptSchemeProcStatus(conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum.DRAFT, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);

        // Check other conditions
        checkConditionsSinceSendToProductionValidation(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to sent to diffusion validation
     */
    private void checkConceptSchemeToSendToDiffusionValidation(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkConceptSchemeProcStatus(conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // Check other conditions
        checkConditionsSinceSendToDiffusionValidation(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject production validation
     */
    private void checkConceptSchemeToRejectProductionValidation(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkConceptSchemeProcStatus(conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject diffusion validation
     */
    private void checkConceptSchemeToRejectDiffusionValidation(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkConceptSchemeProcStatus(conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish internally
     */
    private void checkConceptSchemeToPublishInternally(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkConceptSchemeProcStatus(conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // Check other conditions
        checkConditionsSincePublishInternally(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish externally
     */
    private void checkConceptSchemeToPublishExternally(ServiceContext ctx, String urn, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkConceptSchemeProcStatus(conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);

        // Check other conditions
        checkConditionsSincePublishExternally(conceptSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * TODO alguna validación sobre los conceptos?
     */
    private void checkConditionsSinceSendToProductionValidation(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {

        // Metadata required
        ValidationUtils.checkMetadataRequired(conceptSchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);
    }

    private void checkConditionsSinceSendToDiffusionValidation(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToProductionValidation(conceptSchemeVersion, exceptions);
    }

    // TODO metadato 'roles' de sdmx: En principio se podrán asociar independientemente del estado del esquema de conceptos, pero al publicar internamente se ha de comprobar que el esquema de
    // conceptos relacionado tb
    // esté publicado internamente. Idem al publicar externamente. Está pendiente que Alberto confirme que se podrán asociar en un principio aunque estén en borrador.
    private void checkConditionsSincePublishInternally(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToDiffusionValidation(conceptSchemeVersion, exceptions);
    }

    // TODO metadato 'roles' de sdmx:En principio se podrán asociar independientemente del estado del esquema de conceptos, pero al publicar internamente se ha de comprobar que el esquema de conceptos
    // relacionado tb
    // esté publicado internamente. Idem al publicar externamente. Está pendiente que Alberto confirme que se podrán asociar en un principio aunque estén en borrador.
    private void checkConditionsSincePublishExternally(ConceptSchemeVersionMetamac conceptSchemeVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSincePublishInternally(conceptSchemeVersion, exceptions);
    }

    private void checkConceptSchemeProcStatus(ConceptSchemeVersionMetamac conceptSchemeVersion, ItemSchemeMetamacProcStatusEnum... procStatus) throws MetamacException {
        if (!ArrayUtils.contains(procStatus, conceptSchemeVersion.getProcStatus())) {
            String[] procStatusString = procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS)
                    .withMessageParameters(conceptSchemeVersion.getMaintainableArtefact().getUrn(), procStatusString).build();
        }
    }

    private String[] procStatusEnumToString(ItemSchemeMetamacProcStatusEnum... procStatus) {
        String[] procStatusString = new String[procStatus.length];
        for (int i = 0; i < procStatus.length; i++) {
            procStatusString[i] = procStatus[i].name();
        }
        return procStatusString;
    }

    private List<ConceptRelation> findRelatedConceptsByConceptSchemeVersion(String urn) {
        return getConceptRelationRepository().findByConceptSchemeVersion(urn);
    }

    private void checkConceptHierarchyWithoutRelatedConcepts(Concept concept) throws MetamacException {
        List<ConceptRelation> conceptsRelations = getConceptRelationRepository().findByConcept(concept.getNameableArtefact().getUrn());
        if (conceptsRelations.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_WITH_RELATED_CONCEPTS).withMessageParameters(concept.getNameableArtefact().getUrn()).build();
        }
        for (Item item : concept.getChildren()) {
            ConceptMetamac child = (ConceptMetamac) item;
            checkConceptHierarchyWithoutRelatedConcepts(child);
        }
    }

    /**
     * Retrieves version of a concept scheme, checking that can be modified
     */
    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionCanBeModified(ServiceContext ctx, String urn) throws MetamacException {
        return retrieveConceptSchemeVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DRAFT, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED,
                ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);
    }
}
