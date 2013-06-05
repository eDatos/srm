package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.ExternalItemRepository;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseMergeAssert;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

/**
 * Implementation of ConceptsMetamacService.
 */
@Service("conceptsMetamacService")
public class ConceptsMetamacServiceImpl extends ConceptsMetamacServiceImplBase {

    @Autowired
    private BaseService                      baseService;

    @Autowired
    private ConceptsService                  conceptsService;

    @Autowired
    private CategoriesMetamacService         categoriesMetamacService;

    @Autowired
    private CodesMetamacService              codesMetamacService;

    @Autowired
    private ItemSchemeVersionRepository      itemSchemeVersionRepository;

    @Autowired
    private ItemRepository                   itemRepository;

    @Autowired
    private ConceptRepository                conceptRepository;

    @Autowired
    @Qualifier("conceptSchemeLifeCycle")
    private LifeCycle                        conceptSchemeLifeCycle;

    @Autowired
    private SrmConfiguration                 srmConfiguration;

    @Autowired
    private SrmValidation                    srmValidation;

    @Autowired
    private CodelistVersionMetamacRepository codelistVersionMetamacRepository;

    @Autowired
    @Qualifier("conceptsVersioningCallbackMetamac")
    private ItemSchemesCopyCallback          conceptsVersioningCallback;

    @Autowired
    @Qualifier("conceptsCopyCallbackMetamac")
    private ItemSchemesCopyCallback          conceptsCopyCallback;

    @Autowired
    @Qualifier("conceptsDummyVersioningCallbackMetamac")
    private ItemSchemesCopyCallback          conceptsDummyVersioningCallback;

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
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);

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
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeEnumeratedRepresentationForConcepts(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }

        String conceptSchemeRelatedOperationUrn = null;
        if (StringUtils.isNotEmpty(conceptUrn)) {
            ConceptMetamac concept = retrieveConceptByUrn(ctx, conceptUrn);
            ConceptSchemeVersionMetamac conceptScheme = retrieveConceptSchemeByUrn(ctx, concept.getItemSchemeVersion().getMaintainableArtefact().getUrn());
            ExternalItem relatedOperation = conceptScheme.getRelatedOperation();
            if (relatedOperation != null) {
                conceptSchemeRelatedOperationUrn = (relatedOperation.getUrn() != null) ? relatedOperation.getUrn() : conceptSchemeRelatedOperationUrn;
            }
        }

        ConditionalCriteria measureCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.type())
                .eq(ConceptSchemeTypeEnum.MEASURE).buildSingle();
        conditions.add(measureCondition);

        ConditionalCriteria secondPart = null;
        if (conceptSchemeRelatedOperationUrn == null) {
            secondPart = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation())//
                    .isNull()//
                    .buildSingle();
        } else {
            secondPart = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)//
                    .lbrace()//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation())//
                    .isNull()//
                    .or()//
                    .lbrace()//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation()).isNotNull()//
                    .and()//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation().urn())//
                    .eq(conceptSchemeRelatedOperationUrn)//
                    .rbrace() //
                    .rbrace()//
                    .buildSingle();
        }
        conditions.add(secondPart);

        // // Add restrictions to be extended
        // // concept scheme must be MEASURE
        // ConditionalCriteria measureCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
        // //
        // .withProperty(ConceptSchemeVersionMetamacProperties.type()).eq(ConceptSchemeTypeEnum.MEASURE)
        // //
        // .and()
        // //
        // .lbrace().withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation()).isNull()
        // //
        // .or()
        // //
        // .lbrace()
        // //
        // .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation()).isNotNull()//
        // .and()//
        // .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation().urn())//
        // .eq(conceptSchemeRelatedOperationUrn)//
        // .rbrace() //
        // .rbrace()//
        // .buildSingle();

        // concept scheme externally published
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);

    }
    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByConditionWithConceptsCanBeQuantityBaseQuantity(ServiceContext ctx, String conceptSchemeUrn,
            List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByConditionWithConceptsCanBeQuantityNumerator(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByConditionWithConceptsCanBeQuantityDenominator(ServiceContext ctx, String conceptSchemeUrn,
            List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsByConditionWithCodesCanBeQuantityUnit(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build();
        }
        // scheme externally published
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                .withProperty(CodelistVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);

        return codesMetamacService.findCodelistsByCondition(ctx, conditions, pagingParameter);
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
    public TaskInfo copyConceptScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return conceptsService.copyConceptScheme(ctx, urnToCopy, maintainerUrn, versionPattern, conceptsCopyCallback);
    }

    @Override
    public TaskInfo versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfConceptScheme(ctx, urnToCopy, conceptsVersioningCallback, versionType, false);
    }

    @Override
    public TaskInfo createTemporalVersionConceptScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfConceptScheme(ctx, urnToCopy, conceptsDummyVersioningCallback, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = retrieveConceptSchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(conceptSchemeVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        conceptSchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(conceptSchemeVersion.getMaintainableArtefact().getVersionLogic(), conceptSchemeVersion.getItemScheme().getVersionPattern(), versionTypeEnum));

        conceptSchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        conceptSchemeVersionTemporal = (ConceptSchemeVersionMetamac) conceptsService.updateConceptScheme(ctx, conceptSchemeVersionTemporal);

        // Set null replacedBy in the original entity
        conceptSchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        categoriesMetamacService.createVersionFromTemporalCategorisations(ctx, conceptSchemeVersionTemporal.getMaintainableArtefact());

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    @Override
    public ConceptSchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(conceptSchemeTemporalVersion.getMaintainableArtefact());
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

        // Merge metadata of Item
        Map<String, Item> temporalItemMap = SrmServiceUtils.createMapOfItemsByOriginalUrn(conceptSchemeTemporalVersion.getItems());
        List<ItemResult> conceptsFoundEfficiently = getConceptMetamacRepository().findConceptsByConceptSchemeUnordered(conceptSchemeTemporalVersion.getId(), ItemMetamacResultSelection.ALL);
        Map<String, ItemResult> conceptsFoundEfficientlyByUrn = SdmxSrmUtils.createMapOfItemsResultByUrn(conceptsFoundEfficiently);

        for (Item item : conceptSchemeVersion.getItems()) {
            ConceptMetamac concept = (ConceptMetamac) item;
            ConceptMetamac conceptTemp = (ConceptMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());
            ItemResult conceptTempItemResult = conceptsFoundEfficientlyByUrn.get(conceptTemp.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(concept, conceptTempItemResult, internationalStringRepository);

            // Metamac Metadata
            ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) conceptTempItemResult.getExtensionPoint();

            // Plural Name
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getPluralName(), extensionPoint.getPluralName())) {
                concept.setPluralName(BaseMergeAssert.mergeUpdateInternationalString(concept.getPluralName(), extensionPoint.getPluralName(), internationalStringRepository));
            }

            // Acronym
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getAcronym(), extensionPoint.getAcronym())) {
                concept.setAcronym(BaseMergeAssert.mergeUpdateInternationalString(concept.getAcronym(), extensionPoint.getAcronym(), internationalStringRepository));
            }

            // Description Source
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDescriptionSource(), extensionPoint.getDescriptionSource())) {
                concept.setDescriptionSource(BaseMergeAssert.mergeUpdateInternationalString(concept.getDescriptionSource(), extensionPoint.getDescriptionSource(), internationalStringRepository));
            }

            // Context
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getContext(), extensionPoint.getContext())) {
                concept.setContext(BaseMergeAssert.mergeUpdateInternationalString(concept.getContext(), extensionPoint.getContext(), internationalStringRepository));
            }

            // Doc Method
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDocMethod(), extensionPoint.getDocMethod())) {
                concept.setDocMethod(BaseMergeAssert.mergeUpdateInternationalString(concept.getDocMethod(), extensionPoint.getDocMethod(), internationalStringRepository));
            }

            // Derivation
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDerivation(), extensionPoint.getDerivation())) {
                concept.setDerivation(BaseMergeAssert.mergeUpdateInternationalString(concept.getDerivation(), extensionPoint.getDerivation(), internationalStringRepository));
            }

            // Legal Acts
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getLegalActs(), extensionPoint.getLegalActs())) {
                concept.setLegalActs(BaseMergeAssert.mergeUpdateInternationalString(concept.getLegalActs(), extensionPoint.getLegalActs(), internationalStringRepository));
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
        if (conceptSchemeVersionToCopy == null) {
            // source only can be null when importing
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }
        for (Item conceptToCopyRelatedConcepts : conceptSchemeVersionToCopy.getItems()) {
            versioningRelatedConcepts((ConceptMetamac) conceptToCopyRelatedConcepts, conceptSchemeNewVersion);
        }
    }
    @Override
    public void versioningConceptsQuantity(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        // Versioning quantity. Note: other relations are copied in copy callback
        if (conceptSchemeVersionToCopy == null) {
            // source only can be null when importing
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }
        for (Item conceptToCopyQuantity : conceptSchemeVersionToCopy.getItems()) {
            versioningConceptQuantity((ConceptMetamac) conceptToCopyQuantity, conceptSchemeNewVersion);
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

        // Automatically updates pre-validation
        SrmServiceUtils.assignToConceptSameVariableOfCodelist(conceptSchemeVersion, concept);

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        return concept;
    }

    @Override
    public ConceptMetamac updateConcept(ServiceContext ctx, ConceptMetamac concept) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByConceptUrn(ctx, concept.getNameableArtefact().getUrn());

        // Automatically updates pre-validation
        SrmServiceUtils.assignToConceptSameVariableOfCodelist(conceptSchemeVersion, concept);

        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        return (ConceptMetamac) conceptsService.updateConcept(ctx, concept);
    }

    /**
     * Checks representation, to imported and not imported artefact
     * If it is an enumerated representation must be a codelist of same variable of concept and must be published
     */
    @Override
    public MetamacExceptionItem checkConceptEnumeratedRepresentation(ServiceContext ctx, ConceptMetamac concept, boolean throwException, Boolean isImported) throws MetamacException {

        // NOTE: do not call 'findCodelistsCanBeEnumeratedRepresentationForConceptByCondition' to throw specific exception

        if (isImported && concept.getId() == null) {
            return null; // when importing (only when creating), skip validations
        }

        if (concept.getCoreRepresentation() == null || !RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType())) {
            return null;
        }

        if (concept.getCoreRepresentation().getEnumerationCodelist() == null && concept.getCoreRepresentation().getEnumerationConceptScheme() == null) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
        }

        MetamacExceptionItem exceptionItem = null;
        if (concept.getCoreRepresentation().getEnumerationCodelist() != null) {
            if (ConceptRoleEnum.MEASURE_DIMENSION.equals(concept.getSdmxRelatedArtefact())) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
            }

            CodelistVersionMetamac codelistVersion = (CodelistVersionMetamac) concept.getCoreRepresentation().getEnumerationCodelist();
            String codelistUrn = codelistVersion.getMaintainableArtefact().getUrn();

            // 1) Check codelist published
            exceptionItem = SrmValidationUtils.checkArtefactProcStatusReturningExceptionItem(codelistVersion.getLifeCycleMetadata(), codelistUrn, ProcStatusEnum.INTERNALLY_PUBLISHED,
                    ProcStatusEnum.EXTERNALLY_PUBLISHED);
            if (exceptionItem == null) {
                // 2) Check variable
                if (concept.getVariable() == null || !concept.getVariable().getId().equals(codelistVersion.getVariable().getId())) {
                    exceptionItem = MetamacExceptionItemBuilder.metamacExceptionItem()
                            .withCommonServiceExceptionType(ServiceExceptionType.CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_DIFFERENT_VARIABLE)
                            .withMessageParameters(concept.getNameableArtefact().getCode(), codelistUrn).build();
                }

            }
        } else if (concept.getCoreRepresentation().getEnumerationConceptScheme() != null) {
            if (!ConceptRoleEnum.MEASURE_DIMENSION.equals(concept.getSdmxRelatedArtefact())) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
            }
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            Long conceptSchemeId = concept.getCoreRepresentation().getEnumerationConceptScheme().getId();
            List<ConditionalCriteria> criteriaToVerifyConceptSchemeRepresentation = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.id()).eq(conceptSchemeId).build();
            PagedResult<ConceptSchemeVersionMetamac> result = findConceptSchemesCanBeEnumeratedRepresentationForConcepts(ctx, criteriaToVerifyConceptSchemeRepresentation, pagingParameter, concept
                    .getNameableArtefact().getUrn());
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptSchemeId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
            }
        }

        if (exceptionItem != null && throwException) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(Arrays.asList(exceptionItem)).build();
        }
        return exceptionItem;
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
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);

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
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeQuantityNumerator(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        return findConceptsCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeQuantityDenominator(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        return findConceptsCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeQuantityBaseQuantity(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        return findConceptsCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodeMetamac> findCodesCanBeQuantityUnit(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).distinctRoot().build();
        }
        // codelist externally published
        ConditionalCriteria externallyPublishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle();
        conditions.add(externallyPublishedCondition);
        return codesMetamacService.findCodesByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {

        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByConceptUrn(ctx, urn);
        checkConceptSchemeCanBeModified(conceptSchemeVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, conceptSchemeVersion);
        checkNoQuantityRelated(concept);

        // Delete bidirectional relations of concepts relate this concept and its children (will be removed in cascade)
        removeRelatedConceptsBidirectional(concept);

        // note: do not check if it is role or extends of another concept, because one concept must be published to be role or extends

        conceptsService.deleteConcept(ctx, urn);
    }

    @Override
    public List<ConceptMetamacVisualisationResult> retrieveConceptsByConceptSchemeUrn(ServiceContext ctx, String conceptSchemeUrn, String locale) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptsByConceptSchemeUrn(conceptSchemeUrn, locale, null);

        // Retrieve
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        return getConceptMetamacRepository().findConceptsByConceptSchemeUnorderedToVisualisation(conceptSchemeVersion.getId(), locale);
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
        if (!concept1.getItemSchemeVersion().getId().equals(concept2.getItemSchemeVersion().getId())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concept scheme must be same in two concepts").build();
        }
        // Concept scheme not published
        ItemSchemeVersion conceptSchemeVersion = concept1.getItemSchemeVersion();// note: itemScheme is the same to two concepts
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

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
        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
    }

    @Override
    public void deleteRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteRelatedConcept(urn1, urn2, null);

        // Concept scheme not published
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        ItemSchemeVersion conceptSchemeVersion = concept1.getItemSchemeVersion();
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);

        // Delete
        concept1.removeRelatedConcept(concept2);
        getConceptMetamacRepository().save(concept1);
        concept2.removeRelatedConcept(concept1);
        getConceptMetamacRepository().save(concept2);
        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
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

        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersionOfConcept);
    }

    @Override
    public void deleteRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteRoleConcept(urn, conceptRoleUrn, null);

        // Check concept scheme of concept 'urn' can be modified
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ItemSchemeVersion conceptSchemeVersion = concept.getItemSchemeVersion();
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

        // Delete
        ConceptMetamac conceptRole = retrieveConceptByUrn(ctx, conceptRoleUrn);
        concept.removeRoleConcept(conceptRole);
        getConceptMetamacRepository().save(concept);

        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn, String variableUrn) throws MetamacException {

        // IMPORTANT: If any condition change, review 'checkConceptEnumeratedRepresentation' method

        // Validation
        ConceptsMetamacInvocationValidator.checkFindCodelistsCanBeEnumeratedRepresentationForConceptByCondition(conditions, pagingParameter, conceptUrn, variableUrn, null);

        // Find
        Variable variable = null;
        if (conceptUrn != null) {
            ConceptMetamac concept = retrieveConceptByUrn(ctx, conceptUrn);
            variable = concept.getVariable();
        } else if (variableUrn != null) {
            variable = codesMetamacService.retrieveVariableByUrn(ctx, variableUrn);
        }
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

        // Find
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build());
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
            SrmValidationUtils.checkArtefactCanBeVersioned(conceptSchemeVersionToCopy.getMaintainableArtefact(), conceptSchemeVersionToCopy.getLifeCycleMetadata(), isTemporal);
        }

        // Check does not exist any version 'no final'
        ItemSchemeVersion conceptSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(conceptSchemeVersionToCopy.getItemScheme().getId());
        if (conceptSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
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

    /**
     * When concept is going to be deleted, checks the concept is not as quantity in another concept
     */
    private void checkNoQuantityRelated(ConceptMetamac concept) throws MetamacException {
        Long quantityRelatedId = getQuantityRepository().findOneQuantityRelatedWithConcept(concept.getId());
        if (quantityRelatedId != null) {
            ConceptMetamac conceptQuantity = getConceptMetamacRepository().retrieveConceptWithQuantityId(quantityRelatedId);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_DELETE_NOT_SUPPORTED_CONCEPT_IN_QUANTITY)
                    .withMessageParameters(concept.getNameableArtefact().getUrn(), conceptQuantity.getNameableArtefact().getUrn()).build();
        }

        // Children (will be removed in cascade)
        for (Item child : concept.getChildren()) {
            checkNoQuantityRelated((ConceptMetamac) child);
        }
    }

    private TaskInfo createVersionOfConceptScheme(ServiceContext ctx, String urnToCopy, ItemSchemesCopyCallback itemSchemesCopyCallback, VersionTypeEnum versionType, boolean isTemporal)
            throws MetamacException {

        // Validation
        checkConceptSchemeToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        return conceptsService.versioningConceptScheme(ctx, urnToCopy, versionType, isTemporal, itemSchemesCopyCallback);
    }

    private void versioningRelatedConcepts(ConceptMetamac conceptToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        if (conceptToCopy.getRelatedConcepts().size() == 0) {
            return;
        }
        ConceptMetamac conceptInNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptToCopy.getNameableArtefact().getCode(), conceptSchemeNewVersion
                .getMaintainableArtefact().getUrn());

        if (conceptInNewVersion == null) {
            // concept only can not exist in new version when importing: in this case, do not copy related concepts to this concept
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }

        // Copy relations with concepts in new version
        for (ConceptMetamac relatedConcept : conceptToCopy.getRelatedConcepts()) {
            ConceptMetamac relatedConceptIntNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(relatedConcept.getNameableArtefact().getCode(), conceptSchemeNewVersion
                    .getMaintainableArtefact().getUrn());
            if (relatedConceptIntNewVersion == null) {
                // concept only can not exist in new version when importing: in this case, do not copy
                SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
                continue;
            }
            conceptInNewVersion.addRelatedConcept(relatedConceptIntNewVersion);
        }
    }

    private void versioningConceptQuantity(ConceptMetamac conceptToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        if (conceptToCopy.getQuantity() == null) {
            return;
        }
        ConceptMetamac conceptInNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptToCopy.getNameableArtefact().getCode(), conceptSchemeNewVersion
                .getMaintainableArtefact().getUrn());

        if (conceptInNewVersion == null) {
            // concept only can not exist in new version when importing: in this case, do not copy quantity to this concept
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }

        // Copy quantity in new version
        Quantity quantityToCopy = conceptToCopy.getQuantity();
        Long itemSchemeVersionIdToCopy = conceptToCopy.getItemSchemeVersion().getId();

        Quantity quantityCopied = new Quantity();
        quantityCopied.setQuantityType(quantityToCopy.getQuantityType());
        quantityCopied.setUnitCode(quantityToCopy.getUnitCode());
        quantityCopied.setUnitSymbolPosition(quantityToCopy.getUnitSymbolPosition());
        quantityCopied.setSignificantDigits(quantityToCopy.getSignificantDigits());
        quantityCopied.setDecimalPlaces(quantityToCopy.getDecimalPlaces());
        quantityCopied.setUnitMultiplier(quantityToCopy.getUnitMultiplier());
        quantityCopied.setMinimum(quantityToCopy.getMinimum());
        quantityCopied.setMaximum(quantityToCopy.getMaximum());
        quantityCopied.setNumerator(versioningConceptRelatedInQuantity(itemSchemeVersionIdToCopy, conceptSchemeNewVersion, quantityToCopy.getNumerator()));
        quantityCopied.setDenominator(versioningConceptRelatedInQuantity(itemSchemeVersionIdToCopy, conceptSchemeNewVersion, quantityToCopy.getDenominator()));
        quantityCopied.setIsPercentage(quantityToCopy.getIsPercentage());
        quantityCopied.setPercentageOf(BaseCopyAllMetadataUtils.copy(quantityToCopy.getPercentageOf()));
        quantityCopied.setBaseValue(quantityToCopy.getBaseValue());
        quantityCopied.setBaseTime(quantityToCopy.getBaseTime());
        // target.setBaseLocation(source.getBaseLocation()); // TODO quantity.baseLocation
        quantityCopied.setBaseQuantity(versioningConceptRelatedInQuantity(itemSchemeVersionIdToCopy, conceptSchemeNewVersion, quantityToCopy.getBaseQuantity()));

        conceptInNewVersion.setQuantity(quantityCopied);
    }

    // TODO mejorar. pasar mapa en target para no tener q hacer find?
    private ConceptMetamac versioningConceptRelatedInQuantity(Long itemSchemeVersionIdToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion, ConceptMetamac conceptInQuantityToCopy)
            throws MetamacException {
        if (conceptInQuantityToCopy == null) {
            return null;
        }
        if (!conceptInQuantityToCopy.getItemSchemeVersion().getId().equals(itemSchemeVersionIdToCopy)) {
            // belong to another concept scheme. So, can copy link
            return conceptInQuantityToCopy;
        } else {
            // belong to same concept scheme
            ConceptMetamac numeratorInNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptInQuantityToCopy.getNameableArtefact().getCode(), conceptSchemeNewVersion
                    .getMaintainableArtefact().getUrn());
            if (numeratorInNewVersion == null) {
                // concept only can not exist in new version when importing: in this case, do not copy
                SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
                return null;
            } else {
                return numeratorInNewVersion;
            }
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
            if (conceptSchemeVersion.getIsTypeUpdated() && !conceptSchemeVersion.getMaintainableArtefact().getIsImported()) {
                Long itemsCount = itemRepository.countItems(conceptSchemeVersion.getId());
                if (itemsCount != 0) {
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
        checkConceptMetadataExtends(ctx, conceptSchemeVersion, concept);
        checkConceptEnumeratedRepresentation(ctx, concept, true, conceptSchemeVersion.getMaintainableArtefact().getIsImported());
        checkConceptQuantity(ctx, conceptSchemeVersion, concept);
    }

    private void checkConceptMetadataExtends(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionSource, ConceptMetamac concept) throws MetamacException {
        if (concept.getConceptExtends() == null) {
            return;
        }

        // Not same concept scheme
        if (concept.getConceptExtends() != null) {
            if (conceptSchemeVersionSource.getItemScheme().getId().equals(concept.getConceptExtends().getItemSchemeVersion().getItemScheme().getId())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CONCEPT_EXTENDS).build();
            }
        }

        // Check concept scheme source: type
        if (!ConceptSchemeTypeEnum.GLOSSARY.equals(conceptSchemeVersionSource.getType()) && !ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersionSource.getType())
                && !ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersionSource.getType()) && !ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionSource.getType())) {
            throw MetamacExceptionBuilder
                    .builder()
                    .withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(
                            conceptSchemeVersionSource.getMaintainableArtefact().getUrn(),
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

    /**
     * Numerator, denominator and base quantity can be a concept of same concept scheme or a concept of another concept scheme type measure without operation
     */
    private void checkConceptQuantity(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionSource, ConceptMetamac concept) throws MetamacException {
        Quantity quantity = concept.getQuantity();
        if (quantity == null) {
            return;
        }
        String conceptSchemeVersionSourceUrn = conceptSchemeVersionSource.getMaintainableArtefact().getUrn();
        // Check concept scheme source: type
        if (!ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionSource.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(conceptSchemeVersionSourceUrn, new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_MEASURE}).build();

        }

        // Check quantity concepts and codes
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        if (quantity.getUnitCode() != null) {
            Long unitCodeId = quantity.getUnitCode().getId();
            List<ConditionalCriteria> criteriaToVerifyUnitCode = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.id()).eq(unitCodeId).build();
            PagedResult<CodeMetamac> result = findCodesCanBeQuantityUnit(ctx, criteriaToVerifyUnitCode, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(unitCodeId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_CODE);
            }
        }
        if (quantity.getNumerator() != null) {
            Long conceptNumeratorId = quantity.getNumerator().getId();
            List<ConditionalCriteria> criteriaToVerifyNumerator = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptNumeratorId)
                    .build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeQuantityNumerator(ctx, conceptSchemeVersionSourceUrn, criteriaToVerifyNumerator, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptNumeratorId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_NUMERATOR);
            }
        }
        if (quantity.getDenominator() != null) {
            Long conceptDenominatorId = quantity.getDenominator().getId();
            List<ConditionalCriteria> criteriaToVerifyDenominator = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptDenominatorId)
                    .build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeQuantityDenominator(ctx, conceptSchemeVersionSourceUrn, criteriaToVerifyDenominator, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptDenominatorId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_DENOMINATOR);
            }
        }
        if (quantity.getBaseQuantity() != null) {
            Long conceptBaseQuantityId = quantity.getBaseQuantity().getId();
            List<ConditionalCriteria> criteriaToVerifyBaseQuantity = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptBaseQuantityId)
                    .build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeQuantityBaseQuantity(ctx, conceptSchemeVersionSourceUrn, criteriaToVerifyBaseQuantity, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptBaseQuantityId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_QUANTITY);
            }
        }

        // TODO quantity.baseLocation
    }

    private void checkConceptSchemeCanBeModified(ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(conceptSchemeVersion.getLifeCycleMetadata(), conceptSchemeVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(conceptSchemeVersion);
    }

    /**
     * At the moment, conditions to be numerator, numerator o base quantity are identical.
     */
    private PagedResult<ConceptMetamac> findConceptsCanBeQuantity(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be numerated. IMPORTANT! If any condition change, review findConceptSchemesCanBeQuantity
        // concept scheme can be same scheme or another concept scheme type Measure without operation
        ConditionalCriteria quantityCondition = ConditionalCriteriaBuilder
                .criteriaFor(ConceptMetamac.class)
                // same scheme
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn())
                .eq(conceptSchemeUrn)
                .or()
                // in another scheme, externally published and specific type
                .lbrace()
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class))
                .eq(ConceptSchemeTypeEnum.MEASURE)
                .and()
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.relatedOperation().getName(), true,
                                ConceptMetamac.class)).isNull().and().withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic()).eq(Boolean.TRUE).rbrace()
                .buildSingle();
        conditions.add(quantityCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    /**
     * At the moment, conditions to be numerator, numerator o base quantity are identical
     */
    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeQuantity(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be numerated. IMPORTANT! If any condition change, review findConceptsCanBeQuantity
        // concept scheme can be same scheme or another concept scheme type Measure without operation
        ConditionalCriteria quantityCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                // same scheme
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).eq(conceptSchemeUrn).or()
                // in another scheme, externally published and specific type
                .lbrace().withProperty(ConceptSchemeVersionMetamacProperties.type()).eq(ConceptSchemeTypeEnum.MEASURE).and().withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation())
                .isNull().and().withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE).rbrace().buildSingle();
        conditions.add(quantityCondition);

        PagedResult<ConceptSchemeVersion> conceptSchemeVersionsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptSchemeVersionsPagedResult);
    }

}