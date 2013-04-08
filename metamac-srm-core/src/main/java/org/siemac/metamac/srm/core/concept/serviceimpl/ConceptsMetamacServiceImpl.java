package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.ent.domain.ExternalItemRepository;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseMergeAssert;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseServiceUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsVersioningCopyUtils.ConceptVersioningCopyCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

/**
 * Implementation of ConceptsMetamacService.
 */
@Service("conceptsMetamacService")
public class ConceptsMetamacServiceImpl extends ConceptsMetamacServiceImplBase {

    @Autowired
    private ConceptsService                  conceptsService;

    @Autowired
    private ItemSchemeVersionRepository      itemSchemeVersionRepository;

    @Autowired
    private ConceptRepository                conceptRepository;

    @Autowired
    @Qualifier("conceptSchemeLifeCycle")
    private LifeCycle                        conceptSchemeLifeCycle;

    @Autowired
    private SrmValidation                    srmValidation;

    @Autowired
    private CodelistVersionMetamacRepository codelistVersionMetamacRepository;

    @Autowired
    @Qualifier("conceptVersioningCopyCallbackMetamac")
    private ConceptVersioningCopyCallback    conceptVersioningCopyCallback;

    @Autowired
    private InternationalStringRepository    internationalStringRepository;

    @Autowired
    private ExternalItemRepository           externalItemRepository;

    public ConceptsMetamacServiceImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        preCreateConceptScheme(ctx, conceptSchemeVersion);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public ConceptSchemeVersionMetamac preCreateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);
        checkConceptSchemeToCreateOrUpdate(ctx, conceptSchemeVersion);

        // Fill metadata
        conceptSchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        conceptSchemeVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        return conceptSchemeVersion;
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
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByConditionWithConceptsCanBeRole(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be extended
        // concept scheme must be Glossary
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.type())
                .eq(ConceptSchemeTypeEnum.ROLE).buildSingle();
        conditions.add(roleCondition);
        // concept scheme externally published
        ConditionalCriteria validityStartedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(validityStartedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByConditionWithConceptsCanBeExtended(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be extended
        // concept scheme must be Glossary
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.type())
                .eq(ConceptSchemeTypeEnum.GLOSSARY).buildSingle();
        conditions.add(roleCondition);
        // concept scheme externally published
        ConditionalCriteria validityStartedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(validityStartedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);
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
    public ConceptSchemeVersionMetamac publishInternallyConceptScheme(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
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
        return createVersionOfConceptScheme(ctx, urnToCopy, versionType, false);
    }

    @Override
    public ConceptSchemeVersionMetamac createTemporalVersionConceptScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfConceptScheme(ctx, urnToCopy, null, true);
    }

    @Override
    public ConceptSchemeVersionMetamac createVersionFromTemporalConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = retrieveConceptSchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        if (!VersionUtil.isTemporalVersion(conceptSchemeVersionTemporal.getMaintainableArtefact().getVersionLogic())) {
            throw new RuntimeException("Error creating a new version from a temporal. The URN is not for a temporary artifact");
        }

        // Retrieve the original artifact
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        conceptSchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(conceptSchemeVersion.getMaintainableArtefact().getVersionLogic(), conceptSchemeVersion.getItemScheme().getVersionPattern(), versionTypeEnum));

        conceptSchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        conceptSchemeVersionTemporal = (ConceptSchemeVersionMetamac) conceptsService.updateConceptScheme(ctx, conceptSchemeVersionTemporal);

        // Set null replacedBy in the original entity
        conceptSchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        return conceptSchemeVersionTemporal;
    }

    @Override
    public ConceptSchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        if (!VersionUtil.isTemporalVersion(conceptSchemeTemporalVersion.getMaintainableArtefact().getVersionLogic())) {
            throw new RuntimeException("Error creating a new version from a temporal. The URN is not for a temporary artifact");
        }
        SrmValidationUtils.checkArtefactProcStatus(conceptSchemeTemporalVersion.getLifeCycleMetadata(), conceptSchemeTemporalVersion.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(conceptSchemeTemporalVersion.getMaintainableArtefact().getUrn()));

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItemSchemeVersionWithoutItems(conceptSchemeVersion, conceptSchemeTemporalVersion, internationalStringRepository);

        // Merge Metamac metadata of ItemScheme
        conceptSchemeVersion.setLifeCycleMetadata(BaseReplaceFromTemporalMetamac.replaceProductionAndDifussionLifeCycleMetadataFromTemporal(conceptSchemeVersion.getLifeCycleMetadata(),
                conceptSchemeTemporalVersion.getLifeCycleMetadata()));
        conceptSchemeVersion.setType(conceptSchemeTemporalVersion.getType());
        conceptSchemeVersion.setRelatedOperation(BaseReplaceFromTemporalMetamac.replaceExternalItemFromTemporal(conceptSchemeVersion.getRelatedOperation(),
                conceptSchemeTemporalVersion.getRelatedOperation(), internationalStringRepository, externalItemRepository));

        // Merge Metamac metadata of Item
        Map<String, Item> temporalItemMap = BaseServiceUtils.createMapOfItems(conceptSchemeTemporalVersion.getItems());
        for (Item item : conceptSchemeVersion.getItems()) {
            ConceptMetamac concept = (ConceptMetamac) item;
            ConceptMetamac conceptTemp = (ConceptMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(concept, conceptTemp, internationalStringRepository);

            // Plural Name
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getPluralName(), conceptTemp.getPluralName())) {
                concept.setPluralName(BaseMergeAssert.mergeUpdateInternationalString(concept.getPluralName(), conceptTemp.getPluralName(), true, internationalStringRepository));
            }

            // Acronym
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getAcronym(), conceptTemp.getAcronym())) {
                concept.setAcronym(BaseMergeAssert.mergeUpdateInternationalString(concept.getAcronym(), conceptTemp.getAcronym(), true, internationalStringRepository));
            }

            // Description Source
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDescriptionSource(), conceptTemp.getDescriptionSource())) {
                concept.setDescriptionSource(BaseMergeAssert.mergeUpdateInternationalString(concept.getDescriptionSource(), conceptTemp.getDescriptionSource(), true, internationalStringRepository));
            }

            // Context
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getContext(), conceptTemp.getContext())) {
                concept.setContext(BaseMergeAssert.mergeUpdateInternationalString(concept.getContext(), conceptTemp.getContext(), true, internationalStringRepository));
            }

            // Doc Method
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDocMethod(), conceptTemp.getDocMethod())) {
                concept.setDocMethod(BaseMergeAssert.mergeUpdateInternationalString(concept.getDocMethod(), conceptTemp.getDocMethod(), true, internationalStringRepository));
            }

            // Derivation
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDerivation(), conceptTemp.getDerivation())) {
                concept.setDerivation(BaseMergeAssert.mergeUpdateInternationalString(concept.getDerivation(), conceptTemp.getDerivation(), true, internationalStringRepository));
            }

            // Legal Acts
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getLegalActs(), conceptTemp.getLegalActs())) {
                concept.setLegalActs(BaseMergeAssert.mergeUpdateInternationalString(concept.getLegalActs(), conceptTemp.getLegalActs(), true, internationalStringRepository));
            }

            concept.setSdmxRelatedArtefact(conceptTemp.getSdmxRelatedArtefact());
            concept.setConceptType(conceptTemp.getConceptType());

            // Roles : can copy "roles" because they are concepts in another concept scheme
            concept.removeAllRoleConcepts();
            for (ConceptMetamac conceptRole : conceptTemp.getRoleConcepts()) {
                concept.addRoleConcept(conceptRole);
            }

            concept.setVariable(conceptTemp.getVariable());

            // Extends : can copy "extend" because they are concepts in another concept scheme
            concept.setConceptExtends(conceptTemp.getConceptExtends());

            concept.removeAllRelatedConcepts(); // Clean related concepts, at the final the new related concepts will be added
        }

        // Versioning related concepts
        for (Item conceptToCopyRelatedConcepts : conceptSchemeTemporalVersion.getItems()) {
            versioningRelatedConcepts((ConceptMetamac) conceptToCopyRelatedConcepts, conceptSchemeVersion);
        }

        // Delete temporal version
        deleteConceptScheme(ctx, conceptSchemeTemporalVersion.getMaintainableArtefact().getUrn());

        return conceptSchemeVersion;
    }

    @Override
    public void versioningRelatedConcepts(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        // Versioning related concepts (metadata of Metamac 'relatedConcepts'). Note: other relations are copied in copy callback
        if (conceptSchemeVersionToCopy != null) {
            for (Item conceptToCopyRelatedConcepts : conceptSchemeVersionToCopy.getItems()) {
                versioningRelatedConcepts((ConceptMetamac) conceptToCopyRelatedConcepts, conceptSchemeNewVersion);
            }
        } else if (!conceptSchemeNewVersion.getMaintainableArtefact().getIsImported()) {
            throw new RuntimeException("Error copying related concepts to versioning");
        }
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

        // Validation
        preCreateConcept(ctx, conceptSchemeUrn, concept);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        srmValidation.checkItemsStructureCanBeModified(ctx, conceptSchemeVersion);

        // Save concept
        return (ConceptMetamac) conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
    }

    @Override
    public ConceptMetamac preCreateConcept(ServiceContext ctx, String conceptSchemeUrn, ConceptMetamac concept) throws MetamacException {
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        return concept;
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
    public PagedResult<ConceptMetamac> findConceptsCanBeRoleByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

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
    public PagedResult<ConceptMetamac> findConceptsCanBeExtendedByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be extended
        // concept scheme must be Glossary
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder
                .criteriaFor(ConceptMetamac.class)
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class))
                .eq(ConceptSchemeTypeEnum.GLOSSARY).buildSingle();
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
        srmValidation.checkItemsStructureCanBeModified(ctx, conceptSchemeVersion);

        // Delete bidirectional relations of concepts relate this concept and its children (will be removed in cascade)
        removeRelatedConceptsBidirectional(concept);

        // note: do not check if it is role or extends of another concept, because one concept must be published to be role or extends

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
    public List<MetamacExceptionItem> checkConceptSchemeVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale) {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        getConceptSchemeVersionMetamacRepository().checkConceptSchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItems);
        getConceptMetamacRepository().checkConceptTranslations(itemSchemeVersionId, locale, exceptionItems);
        return exceptionItems;
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

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindCodelistsCanBeEnumeratedRepresentationForConceptByCondition(conditions, pagingParameter, conceptUrn, null);

        // Find
        ConceptMetamac concept = retrieveConceptByUrn(ctx, conceptUrn);
        return findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(conditions, pagingParameter, concept);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter,
            ConceptMetamac concept) throws MetamacException {

        // Validation
        Variable variable = concept.getVariable();
        if (variable == null) {
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }
        // Prepare conditions
        Class entitySearchedClass = CodelistVersionMetamac.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Codelist internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE)
                .buildSingle());
        // Same variable
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(CodelistVersionMetamacProperties.variable().nameableArtefact().urn())
                .eq(variable.getNameableArtefact().getUrn()).buildSingle());
        // Do not repeat results
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build());

        // Find
        return codelistVersionMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
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
        List<ConceptMetamac> targets = new ArrayList<ConceptMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((ConceptMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<ConceptSchemeVersionMetamac> conceptSchemeVersionsToConceptSchemeVersionsMetamac(List<ConceptSchemeVersion> sources) {
        List<ConceptSchemeVersionMetamac> targets = new ArrayList<ConceptSchemeVersionMetamac>(sources.size());
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

    private void checkConceptSchemeToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);

        if (isTemporal) {
            // Check version to copy is published
            SrmValidationUtils.checkArtefactCanBeVersionedAsTemporal(conceptSchemeVersionToCopy.getMaintainableArtefact(), conceptSchemeVersionToCopy.getLifeCycleMetadata());
        } else {
            // Check version to copy is published and not imported
            SrmValidationUtils.checkArtefactCanBeVersioned(conceptSchemeVersionToCopy.getMaintainableArtefact(), conceptSchemeVersionToCopy.getLifeCycleMetadata());
        }

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

    private ConceptSchemeVersionMetamac createVersionOfConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType, boolean isTemporal) throws MetamacException {

        // Validation
        checkConceptSchemeToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptSchemeVersionMetamac conceptSchemeNewVersion = (ConceptSchemeVersionMetamac) conceptsService.versioningConceptScheme(ctx, urnToCopy, versionType, isTemporal,
                conceptVersioningCopyCallback);

        // Versioning related concepts (metadata of Metamac 'relatedConcepts'). Note: other relations are copied in copy callback
        versioningRelatedConcepts(ctx, conceptSchemeVersionToCopy, conceptSchemeNewVersion);

        return conceptSchemeNewVersion;
    }

    private void versioningRelatedConcepts(ConceptMetamac conceptToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) {
        if (conceptToCopy.getRelatedConcepts().size() == 0) {
            return;
        }
        ConceptMetamac conceptIntNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptToCopy.getNameableArtefact().getCode(), conceptSchemeNewVersion
                .getMaintainableArtefact().getUrn());

        if (conceptIntNewVersion != null) {
            // Copy relations with concepts in new version
            for (ConceptMetamac relatedConcept : conceptToCopy.getRelatedConcepts()) {
                ConceptMetamac relatedConceptIntNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(relatedConcept.getNameableArtefact().getCode(),
                        conceptSchemeNewVersion.getMaintainableArtefact().getUrn());
                if (relatedConceptIntNewVersion != null) {
                    conceptIntNewVersion.addRelatedConcept(relatedConceptIntNewVersion);
                } else if (!conceptSchemeNewVersion.getMaintainableArtefact().getIsImported()) {
                    throw new RuntimeException("Error copying related concepts to versioning");
                }
            }
        } else if (!conceptSchemeNewVersion.getMaintainableArtefact().getIsImported()) {
            throw new RuntimeException("Error copying related concepts to versioning");
        }
    }

    /**
     * Common validations to create or update a concept scheme
     */
    private void checkConceptSchemeToCreateOrUpdate(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // When updating
        if (conceptSchemeVersion.getId() != null) {
            // Proc status
            checkConceptSchemeCanBeModified(conceptSchemeVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(conceptSchemeVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, conceptSchemeVersion.getMaintainableArtefact(), conceptSchemeVersion.getMaintainableArtefact().getIsImported());

        // When updating
        // If the scheme has items (concepts), type cannot be modified
        if (conceptSchemeVersion.getId() != null) {
            if (conceptSchemeVersion.getItems() != null && !conceptSchemeVersion.getItems().isEmpty()) {
                if (conceptSchemeVersion.getIsTypeUpdated()) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE)
                            .build();
                }
            }
        }

        // When updating
        // if this version is not the first one, check not modify 'type'
        if (!SrmServiceUtils.isItemSchemeFirstVersion(conceptSchemeVersion)) {
            ConceptSchemeVersionMetamac conceptSchemePreviousVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.retrieveByVersion(conceptSchemeVersion.getItemScheme().getId(),
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
        checkConceptEnumeratedRepresentation(conceptSchemeVersion, concept);
    }

    /**
     * If concept scheme is not imported, checks representation.
     * If it is an enumerated representation must be a codelist of same variable of concept
     */
    private void checkConceptEnumeratedRepresentation(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) throws MetamacException {
        if (BooleanUtils.isFalse(conceptSchemeVersion.getMaintainableArtefact().getIsImported())) {
            if (concept.getCoreRepresentation() != null && RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType())) {
                String codelistUrn = concept.getCoreRepresentation().getEnumerationCodelist().getMaintainableArtefact().getUrn();
                // check codelist belongs to same variable of concept
                if (concept.getVariable() == null) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_DIFFERENT_VARIABLE).withMessageParameters(codelistUrn)
                            .build();
                }
                List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().urn())
                        .eq(codelistUrn).build();
                PagedResult<CodelistVersionMetamac> codelists = findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(conditions, PagingParameter.rowAccess(0, 1), concept);
                if (codelists.getValues().size() != 1) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_DIFFERENT_VARIABLE).withMessageParameters(codelistUrn)
                            .build();
                }
            }
        }
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