package org.siemac.metamac.srm.core.dsd.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategorisationsUtils;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.DimensionVisualisationInfo;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.serviceimpl.utils.DsdsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentListRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Facet;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.RepresentationConstraintValidator;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.NoSpecifiedRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure;
import com.arte.statistic.sdmx.srm.core.structure.domain.ReportingYearStartDay;
import com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.DataStructureDefinitionsCopyCallback;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.DataStructureInvocationValidator;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;

/**
 * Implementation of DataStructureDefinitionMetamacService.
 */
@Service("dataStructureDefinitionMetamacService")
public class DataStructureDefinitionMetamacServiceImpl extends DataStructureDefinitionMetamacServiceImplBase {

    @Autowired
    private DataStructureDefinitionService          dataStructureDefinitionService;

    @Autowired
    private CategoriesMetamacService                categoriesMetamacService;

    @Autowired
    private ConceptMetamacRepository                conceptMetamacRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository   conceptSchemeVersionMetamacRepository;

    @Autowired
    private ConceptsMetamacService                  conceptsService;

    @Autowired
    private CodelistVersionMetamacRepository        codelistVersionMetamacRepository;

    @Autowired
    private CodelistOrderVisualisationRepository    codelistOrderVisualisationRepository;

    @Autowired
    private CodelistOpennessVisualisationRepository codelistOpennessVisualisationRepository;

    @Autowired
    @Qualifier("dsdLifeCycle")
    private LifeCycle                               dsdLifeCycle;

    @Autowired
    private SrmValidation                           srmValidation;

    @Autowired
    @Qualifier("dataStructureDefinitionsVersioningCallbackMetamac")
    private DataStructureDefinitionsCopyCallback    dataStructureDefinitionsVersioningCallback;

    @Autowired
    @Qualifier("dataStructureDefinitionsDummyVersioningCallbackMetamac")
    private DataStructureDefinitionsCopyCallback    dataStructureDefinitionsDummyVersioningCallback;

    @Autowired
    @Qualifier("dataStructureDefinitionsCopyCallbackMetamac")
    private DataStructureDefinitionsCopyCallback    dataStructureDefinitionsCopyCallback;

    @Autowired
    private StructureVersionRepository              structureVersionRepository;

    @Autowired
    private ComponentListRepository                 componentListRepository;

    @Autowired
    private ComponentRepository                     componentRepository;

    @Autowired
    private SrmConfiguration                        srmConfiguration;

    @Autowired
    private InternationalStringRepository           internationalStringRepository;

    @Autowired
    private ExternalItemRepository                  externalItemRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager                         entityManager;

    public DataStructureDefinitionMetamacServiceImpl() {
    }

    @Override
    public DataStructureDefinitionVersionMetamac createDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Fill and validate Data Structure Definition
        DsdsMetamacInvocationValidator.checkCreateDataStructureDefinition(dataStructureDefinitionVersion, null);
        preCreateDataStructureDefinition(ctx, dataStructureDefinitionVersion);

        // Save
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.createDataStructureDefinition(ctx,
                dataStructureDefinitionVersion, SrmConstants.VERSION_PATTERN_METAMAC);

        // By default
        // Create the MeasureDescriptor
        // Create the PrimaryMeasure with:
        // - concept id by default: OBS_VALUE (this concept is specified in the data)
        // - an non enumerated representation with type Decimals
        String primaryMeasureConceptIdUrnDefault = srmConfiguration.retrievePrimaryMeasureConceptIdUrnDefault();
        if (StringUtils.isNotEmpty(primaryMeasureConceptIdUrnDefault)) {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.nameableArtefact().urn())
                    .eq(primaryMeasureConceptIdUrnDefault).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

            PagedResult<ConceptMetamac> conceptsCanBeDsdPrimaryMeasureByCondition = executeFindConceptsCanBeDsdSpecificDimensionByCondition(conditions, pagingParameter,
                    dataStructureDefinitionVersionMetamac.getStatisticalOperation().getUrn(), ConceptRoleEnum.PRIMARY_MEASURE);

            if (conceptsCanBeDsdPrimaryMeasureByCondition.getTotalRows() == 1) {
                PrimaryMeasure primaryMeasure = new PrimaryMeasure();
                primaryMeasure.setCptIdRef(conceptsCanBeDsdPrimaryMeasureByCondition.getValues().iterator().next());
                Representation localRepresentation = new Representation();
                localRepresentation.setRepresentationType(RepresentationTypeEnum.TEXT_FORMAT);
                localRepresentation.setTextFormat(new Facet(FacetValueTypeEnum.DECIMAL_FVT));
                primaryMeasure.setLocalRepresentation(localRepresentation);
                dataStructureDefinitionService.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), new MeasureDescriptor());
                dataStructureDefinitionService.saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), primaryMeasure);
            }

        }

        return dataStructureDefinitionVersionMetamac;
    }

    @Override
    public DataStructureDefinitionVersionMetamac preCreateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkPreCreateDataStructureDefinition(dataStructureDefinitionVersion, null);
        checkDataStructureDefinitionToCreateOrUpdate(ctx, dataStructureDefinitionVersion);

        // Fill metadata
        dataStructureDefinitionVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        dataStructureDefinitionVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        dataStructureDefinitionVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        dataStructureDefinitionVersion.setAutoOpen(Boolean.TRUE);

        return dataStructureDefinitionVersion;
    }

    @Override
    public DataStructureDefinitionVersionMetamac updateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkUpdateDataStructureDefinition(dataStructureDefinitionVersion, null);
        checkDataStructureDefinitionToCreateOrUpdate(ctx, dataStructureDefinitionVersion);

        // Save
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.updateDataStructureDefinition(ctx, dataStructureDefinitionVersion);
    }

    @Override
    public DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        DataStructureInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = retrieveDataStructureDefinitionVersionByUrn(urn);
        return dataStructureDefinitionVersion;
    }

    @Override
    public List<DataStructureDefinitionVersionMetamac> retrieveDataStructureDefinitionVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve dataStructureDefinitionVersions
        List<DataStructureDefinitionVersion> dataStructureDefinitionVersions = dataStructureDefinitionService.retrieveDataStructureDefinitionVersions(ctx, urn);

        // Typecast to DataStructureDefinitionVersionMetamac
        return dataStructureDefinitionVersionsToMetamac(dataStructureDefinitionVersions);
    }

    @Override
    public PagedResult<DataStructureDefinitionVersionMetamac> findDataStructureDefinitionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        PagedResult<DataStructureDefinitionVersion> dataStructureDefinitionVersionPagedResult = dataStructureDefinitionService.findDataStructureDefinitionByCondition(ctx, conditions, pagingParameter);
        return pagedResultDataStructureDefinitionVersionToMetamac(dataStructureDefinitionVersionPagedResult);
    }

    @Override
    public ComponentList saveDescriptorForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, ComponentList componentList) throws MetamacException {
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        // Validation
        DsdsMetamacInvocationValidator.checkDescriptorToCreateOrUpdate(componentList, null);
        checkDescriptorToCreateOrUpdate(ctx, dataStructureDefinitionVersionMetamac, componentList);

        return dataStructureDefinitionService.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, componentList);
    }

    @Override
    public void deleteDescriptorForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, ComponentList componentList) throws MetamacException {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersionMetamac);
        srmValidation.checkItemsStructureCanBeModified(ctx, dataStructureDefinitionVersionMetamac);

        dataStructureDefinitionService.deleteDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, componentList);
    }

    @Override
    public Component saveComponentForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, Component component) throws MetamacException {
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        // Validation
        DsdsMetamacInvocationValidator.checkComponentToCreateOrUpdate(component, null);
        checkComponentToCreateOrUpdate(ctx, dataStructureDefinitionVersionMetamac, component);

        // Clean Show Decimals Precision
        if (component instanceof MeasureDimension) {
            if (((MeasureDimension) component).getIsEnumeratedRepresentationUpdated()) {
                // perform a clean up of the Show Decimals Precision of DSD
                dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions().clear();
            }
        }

        // Check if clean VisualizationInfo is necessary
        if (component instanceof DimensionComponent) {
            // When representation changed
            if (((DimensionComponent) component).getIsEnumeratedRepresentationUpdated()) {
                // Check if a clean up of DimensionVisualisationInfo is needed
                if (component.getLocalRepresentation() != null) {
                    // Dimension Representation
                    checkIfDimensionVisualizationInfo(dataStructureDefinitionVersionMetamac, component.getLocalRepresentation().getEnumerationCodelist(), (DimensionComponent) component);
                } else {
                    if (component.getCptIdRef().getCoreRepresentation() != null) {
                        checkIfDimensionVisualizationInfo(dataStructureDefinitionVersionMetamac, component.getCptIdRef().getCoreRepresentation().getEnumerationCodelist(),
                                (DimensionComponent) component);
                    }
                }
            }
            // When conceptIdChange
            if (((DimensionComponent) component).getIsConceptIdUpdated() && component.getLocalRepresentation() == null) {
                if (component.getCptIdRef().getCoreRepresentation() != null) {
                    checkIfDimensionVisualizationInfo(dataStructureDefinitionVersionMetamac, component.getCptIdRef().getCoreRepresentation().getEnumerationCodelist(), (DimensionComponent) component);
                }
            }
        }

        return dataStructureDefinitionService.saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, component);
    }

    private void checkIfDimensionVisualizationInfo(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, CodelistVersion codelistTarget, DimensionComponent dimensionTarget) {
        // DimensionVisualisationInfo
        for (DimensionVisualisationInfo dimensionVisualizationInfo : dataStructureDefinitionVersionMetamac.getDimensionVisualisationInfos()) {
            if (dimensionVisualizationInfo.getDimension().getUrn().equals(dimensionTarget.getUrn())) {

                if (RepresentationTypeEnum.ENUMERATION.equals(dimensionTarget.getLocalRepresentation().getRepresentationType())) {
                    // Check display order
                    if (dimensionVisualizationInfo.getDisplayOrder() != null) {
                        if (!dimensionVisualizationInfo.getDisplayOrder().getCodelistVersion().equals(codelistTarget.getMaintainableArtefact().getUrn())) {
                            dimensionVisualizationInfo.setDisplayOrder(null);
                        }
                    }

                    // Check hierarchy levels open
                    if (dimensionVisualizationInfo.getHierarchyLevelsOpen() != null) {
                        if (!dimensionVisualizationInfo.getHierarchyLevelsOpen().getCodelistVersion().equals(codelistTarget.getMaintainableArtefact().getUrn())) {
                            dimensionVisualizationInfo.setHierarchyLevelsOpen(null);
                        }
                    }

                    if (dimensionVisualizationInfo.getDisplayOrder() == null && dimensionVisualizationInfo.getHierarchyLevelsOpen() == null) {
                        dataStructureDefinitionVersionMetamac.removeDimensionVisualisationInfo(dimensionVisualizationInfo);
                    }
                } else {
                    dataStructureDefinitionVersionMetamac.removeDimensionVisualisationInfo(dimensionVisualizationInfo);
                }

                break;
            }
        }
    }

    @Override
    public void deleteComponentForDataStructureDefinition(ServiceContext ctx, String dataStructureDefinitionVersionUrn, Component component) throws MetamacException {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionVersionUrn);

        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersionMetamac);
        srmValidation.checkItemsStructureCanBeModified(ctx, dataStructureDefinitionVersionMetamac);

        if (component instanceof DimensionComponent) {
            // If is a Dimension check is not exist in the stub or heading, or delete it if exist
            for (DimensionOrder dimensionOrder : new ArrayList<DimensionOrder>(dataStructureDefinitionVersionMetamac.getStubDimensions())) {
                if (StringUtils.equals(((DimensionComponent) component).getCode(), dimensionOrder.getDimension().getCode())) {
                    dataStructureDefinitionVersionMetamac.removeStubDimension(dimensionOrder);
                }
            }

            for (DimensionOrder dimensionOrder : new ArrayList<DimensionOrder>(dataStructureDefinitionVersionMetamac.getHeadingDimensions())) {
                if (StringUtils.equals(((DimensionComponent) component).getCode(), dimensionOrder.getDimension().getCode())) {
                    dataStructureDefinitionVersionMetamac.removeHeadingDimension(dimensionOrder);
                }
            }

            // If the dimension has associated information to be deleted, delete it too.
            for (DimensionVisualisationInfo dimensionVisualizationInfo : new ArrayList<DimensionVisualisationInfo>(dataStructureDefinitionVersionMetamac.getDimensionVisualisationInfos())) {
                if (StringUtils.equals(((DimensionComponent) component).getCode(), dimensionVisualizationInfo.getDimension().getCode())) {
                    dataStructureDefinitionVersionMetamac.removeDimensionVisualisationInfo(dimensionVisualizationInfo);
                }
            }

            // Remove show decimals by concept if the dimension is a measure dimension.
            if (component instanceof MeasureDimension) {
                dataStructureDefinitionVersionMetamac.removeAllShowDecimalsPrecisions();
            }

            // Note: Is not necessary re-calculate the dimension OrderLogic because in Metamac, this metadata never set it. See METAMAC-2148 for more info.
        }

        dataStructureDefinitionService.deleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionUrn, component);
    }

    @Override
    public void deleteDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = retrieveDataStructureDefinitionByUrn(ctx, urn);
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);
        dataStructureDefinitionVersion.removeAllHeadingDimensions();
        dataStructureDefinitionVersion.removeAllStubDimensions();
        dataStructureDefinitionVersion.removeAllShowDecimalsPrecisions();
        dataStructureDefinitionVersion.removeAllDimensionVisualisationInfos();
        // Delete
        dataStructureDefinitionService.deleteDataStructureDefinition(ctx, urn);
    }

    @Override
    public TaskInfo copyDataStructureDefinition(ServiceContext ctx, String urnToCopy, String newCode) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return dataStructureDefinitionService.copyDataStructureDefinition(ctx, urnToCopy, newCode, maintainerUrn, versionPattern, dataStructureDefinitionsCopyCallback);
    }

    @Override
    public TaskInfo versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfDataStructureDefinition(ctx, urnToCopy, dataStructureDefinitionsVersioningCallback, versionType, false);
    }

    @Override
    public TaskInfo createTemporalDataStructureDefinition(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfDataStructureDefinition(ctx, urnToCopy, dataStructureDefinitionsDummyVersioningCallback, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {
        DataStructureDefinitionVersionMetamac dataStructureVersionTemporal = retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(dataStructureVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = retrieveDataStructureDefinitionByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        dataStructureVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(dataStructureDefinitionVersion.getMaintainableArtefact().getVersionLogic(), dataStructureDefinitionVersion.getStructure().getVersionPattern(),
                        versionTypeEnum));

        dataStructureVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        dataStructureVersionTemporal = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.updateDataStructureDefinition(ctx, dataStructureVersionTemporal);

        // Set null replacedBy in the original entity
        dataStructureDefinitionVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        categoriesMetamacService.createVersionFromTemporalCategorisations(ctx, dataStructureVersionTemporal.getMaintainableArtefact());

        // Result
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setUrnResult(dataStructureVersionTemporal.getMaintainableArtefact().getUrn());
        return taskInfo;
    }

    @Override
    public DataStructureDefinitionVersionMetamac mergeTemporalVersion(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(dataStructureTemporalVersion.getMaintainableArtefact());
        SrmValidationUtils.checkArtefactProcStatus(dataStructureTemporalVersion.getLifeCycleMetadata(), dataStructureTemporalVersion.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = retrieveDataStructureDefinitionByUrn(ctx,
                GeneratorUrnUtils.makeUrnFromTemporal(dataStructureTemporalVersion.getMaintainableArtefact().getUrn()));

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToStructureVersionWithoutComponentListsAndComponents(dataStructureDefinitionVersion, dataStructureTemporalVersion,
                internationalStringRepository);

        // Merge Metamac metadata of ItemScheme
        dataStructureDefinitionVersion.setAutoOpen(dataStructureTemporalVersion.getAutoOpen());
        dataStructureDefinitionVersion.setShowDecimals(dataStructureTemporalVersion.getShowDecimals());
        dataStructureDefinitionVersion.setStatisticalOperation(BaseReplaceFromTemporalMetamac.replaceExternalItemFromTemporal(dataStructureDefinitionVersion.getStatisticalOperation(),
                dataStructureTemporalVersion.getStatisticalOperation(), internationalStringRepository, externalItemRepository));

        // Merge componentlists and components
        Map<String, ComponentList> temporalComponentListMap = SrmServiceUtils.createMapOfComponentListsByOriginalUrn(dataStructureTemporalVersion.getGrouping());
        for (ComponentList componentList : dataStructureDefinitionVersion.getGrouping()) {
            ComponentList componentListTemp = temporalComponentListMap.get(componentList.getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToComponentListWithoutComponents(componentList, componentListTemp, internationalStringRepository);

            if (!(componentList instanceof GroupDimensionDescriptor)) {
                Map<String, Component> temporalComponentMap = SrmServiceUtils.createMapOfComponentsByOriginalUrn(componentList.getComponents());

                for (Component component : componentList.getComponents()) {
                    Component componentTemp = temporalComponentMap.get(component.getUrn());

                    // Inherit InternationalStrings
                    BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToComponent(component, componentTemp, internationalStringRepository);
                }
            }
        }

        // Heading and Stub
        dataStructureDefinitionVersion.removeAllHeadingDimensions();
        dataStructureDefinitionVersion.removeAllStubDimensions();
        dataStructureDefinitionVersion = versioningHeadingAndStub(ctx, dataStructureTemporalVersion, dataStructureDefinitionVersion);

        // ShowDecimalsPrecision
        dataStructureDefinitionVersion.removeAllShowDecimalsPrecisions();
        dataStructureDefinitionVersion = versioningShowDecimalsPrecision(ctx, dataStructureTemporalVersion, dataStructureDefinitionVersion);

        // Visualization Info
        dataStructureDefinitionVersion.removeAllDimensionVisualisationInfos();
        dataStructureDefinitionVersion = versioningDimensionVisualisationInfo(ctx, dataStructureTemporalVersion, dataStructureDefinitionVersion);

        // Add Categorisations
        boolean thereAreNewCategorisations = false;
        thereAreNewCategorisations = CategorisationsUtils.addCategorisationsTemporalToStructure(dataStructureTemporalVersion, dataStructureDefinitionVersion);
        if (thereAreNewCategorisations) {
            // ===============================================================
            // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear the
            // context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
            entityManager.flush();
            entityManager.clear();
            // ===============================================================
        }

        // Delete temporal version
        deleteDataStructureDefinition(ctx, dataStructureTemporalVersion.getMaintainableArtefact().getUrn());

        dataStructureDefinitionVersion = retrieveDataStructureDefinitionByUrn(ctx, dataStructureDefinitionVersion.getMaintainableArtefact().getUrn());
        return dataStructureDefinitionVersion;
    }

    @Override
    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac publishInternallyDataStructureDefinition(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
    }

    @Override
    public DataStructureDefinitionVersionMetamac publishExternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac) dsdLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public DataStructureDefinitionVersionMetamac startDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkStartValidity(urn, null);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByUrn(urn);
        srmValidation.checkStartValidity(ctx, dataStructureDefinitionVersionMetamac.getMaintainableArtefact(), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata());

        // End validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.startDataStructureDefinitionValidity(ctx, urn, null);
        return dataStructureDefinitionVersionMetamac;
    }

    @Override
    public DataStructureDefinitionVersionMetamac endDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkEndValidity(urn, null);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByUrn(urn);
        srmValidation.checkEndValidity(ctx, dataStructureDefinitionVersionMetamac.getMaintainableArtefact(), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata());

        // End validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, urn, null);
        return dataStructureDefinitionVersionMetamac;
    }

    @Override
    public void checkDataStructureDefinitionTranslations(ServiceContext ctx, Long structureVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByResourceUrn)
            throws MetamacException {
        getDataStructureDefinitionVersionMetamacRepository().checkDataStructureDefinitionVersionTranslations(structureVersionId, locale, exceptionItemsByResourceUrn);
        componentListRepository.checkComponentListTranslations(structureVersionId, locale, exceptionItemsByResourceUrn);
        componentRepository.checkComponentTranslations(structureVersionId, locale, exceptionItemsByResourceUrn);
    }

    @Override
    public void checkDataStructureDefinitionWithRelatedResourcesExternallyPublished(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        String itemSchemeVersionUrn = dataStructureDefinitionVersion.getMaintainableArtefact().getUrn();
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        categoriesMetamacService.checkCategorisationsWithRelatedResourcesExternallyPublished(ctx, itemSchemeVersionUrn, exceptionItemsByUrn);
        // Other relations like enumerated representations or concept identification relations are checked in statistic-srm module
        ExceptionUtils.throwIfException(new ArrayList<MetamacExceptionItem>(exceptionItemsByUrn.values()));
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {
        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.PRIMARY_MEASURE);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.PRIMARY_MEASURE);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {
        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.DIMENSION, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdTimeDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.DIMENSION, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {
        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.MEASURE_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdMeasureDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.MEASURE_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {
        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.DIMENSION, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.DIMENSION, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdSpatialDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {

        addSpatialConditionForConceptSchemes(conditions); // Condition Spatial

        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.DIMENSION, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdSpatialDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {

        addSpatialConditionForConcepts(conditions); // Condition Spatial

        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.DIMENSION, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdRoleByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        return findConceptSchemesWithSpecificType(ctx, conditions, pagingParameter, ConceptSchemeTypeEnum.ROLE);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdRoleByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkFindConceptsCanBeDsdRoleByCondition(conditions, pagingParameter, null);

        // Prepare conditions
        Class entitySearchedClass = ConceptMetamac.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // ConceptScheme internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient())
                .eq(Boolean.TRUE).buildSingle());
        // ConceptScheme Role
        Property conceptSchemeTypeProperty = buildConceptPropertyToConceptSchemeType();
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(conceptSchemeTypeProperty).eq(ConceptSchemeTypeEnum.ROLE).buildSingle());
        // Do not repeat results
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build());

        // Find
        return conceptMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkFindCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(conditions, pagingParameter, null);

        // Find
        return findCodelistsPublishedByConditions(ctx, conditions, pagingParameter, null);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {
        return findCodelistsCanBeEnumeratedRepresentationForDsd(ctx, conditions, pagingParameter, conceptUrn, false);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsdSpatialDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {
        return findCodelistsCanBeEnumeratedRepresentationForDsd(ctx, conditions, pagingParameter, conceptUrn, true);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesWithSpecificType(ctx, conditions, pagingParameter, ConceptSchemeTypeEnum.MEASURE);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesWithSpecificType(ctx, conditions, pagingParameter, ConceptSchemeTypeEnum.MEASURE);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {
        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.ATTRIBUTE, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.ATTRIBUTE, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdSpatialAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {

        addSpatialConditionForConceptSchemes(conditions); // Spatial condition

        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.ATTRIBUTE, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdSpatialAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {

        addSpatialConditionForConcepts(conditions); // Spatial condition

        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.ATTRIBUTE, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdMeasureAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {

        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.MEASURE_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdMeasureAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.MEASURE_DIMENSION);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdTimeAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn) throws MetamacException {
        return findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.ATTRIBUTE, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeDsdTimeAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn)
            throws MetamacException {
        return findConceptsCanBeDsdSpecificDimensionByCondition(ctx, conditions, pagingParameter, dsdUrn, ConceptRoleEnum.ATTRIBUTE, ConceptRoleEnum.ATTRIBUTE_OR_DIMENSION);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {
        return findCodelistsCanBeEnumeratedRepresentationForDsd(ctx, conditions, pagingParameter, conceptUrn, false);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsdSpatialAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {
        return findCodelistsCanBeEnumeratedRepresentationForDsd(ctx, conditions, pagingParameter, conceptUrn, true);
    }

    @Override
    public PagedResult<CodelistOrderVisualisation> findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dimensionUrn) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(conditions, pagingParameter, dimensionUrn, null);

        DimensionComponent dimensionComponent = (DimensionComponent) componentRepository.findByUrn(dimensionUrn);

        Representation representation = dimensionComponent.getLocalRepresentation();
        if (representation == null) {
            representation = dimensionComponent.getCptIdRef().getCoreRepresentation();
        }

        Long codelistId = null;
        if (representation != null) {
            if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType()) && representation.getEnumerationCodelist() != null) {
                codelistId = representation.getEnumerationCodelist().getId();
            }
        }

        if (codelistId == null) {
            return new PagedResult<CodelistOrderVisualisation>(new ArrayList<CodelistOrderVisualisation>(), -1, -1, -1);
        }

        return findCodelistOrderVisualisationRepositoryByConditions(ctx, conditions, pagingParameter, codelistId);
    }

    @Override
    public PagedResult<CodelistOpennessVisualisation> findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dimensionUrn) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(conditions, pagingParameter, dimensionUrn, null);

        DimensionComponent dimensionComponent = (DimensionComponent) componentRepository.findByUrn(dimensionUrn);

        Representation representation = dimensionComponent.getLocalRepresentation();
        if (representation == null) {
            representation = dimensionComponent.getCptIdRef().getCoreRepresentation();
        }

        Long codelistId = null;
        if (representation != null) {
            if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType()) && representation.getEnumerationCodelist() != null) {
                codelistId = representation.getEnumerationCodelist().getId();
            }
        }

        if (codelistId == null) {
            return new PagedResult<CodelistOpennessVisualisation>(new ArrayList<CodelistOpennessVisualisation>(), -1, -1, -1);
        }

        return findCodelistOpennessVisualisationRepositoryByConditions(ctx, conditions, pagingParameter, codelistId);
    }

    /**************************************************************************
     * PRIVATE
     *************************************************************************/

    /**
     * Typecast to Metamac type
     */
    private List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionsToMetamac(List<DataStructureDefinitionVersion> sources) {
        List<DataStructureDefinitionVersionMetamac> targets = new ArrayList<DataStructureDefinitionVersionMetamac>(sources.size());
        for (DataStructureDefinitionVersion source : sources) {
            targets.add((DataStructureDefinitionVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<DataStructureDefinitionVersionMetamac> pagedResultDataStructureDefinitionVersionToMetamac(PagedResult<DataStructureDefinitionVersion> source) {
        List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionsMetamac = dataStructureDefinitionVersionsToMetamac(source.getValues());
        return new PagedResult<DataStructureDefinitionVersionMetamac>(dataStructureDefinitionVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Common validations to create or update a category scheme
     */
    private void checkDataStructureDefinitionToCreateOrUpdate(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {

        // When updating
        if (dataStructureDefinitionVersion.getId() != null) {
            // Proc status
            checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(dataStructureDefinitionVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, dataStructureDefinitionVersion.getMaintainableArtefact(), dataStructureDefinitionVersion.getMaintainableArtefact().getIsImported());

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
        // Show Decimals
        if (dataStructureDefinitionVersion.getShowDecimals() != null) {
            if (dataStructureDefinitionVersion.getShowDecimals() > 6 || dataStructureDefinitionVersion.getShowDecimals() < 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_SHOWDECIMALS));
            }
        }
        // Show Decimals Precision
        for (MeasureDimensionPrecision precision : dataStructureDefinitionVersion.getShowDecimalsPrecisions()) {
            if (precision.getShowDecimalPrecision() > 6 || precision.getShowDecimalPrecision() < 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_SHOWDECIMALS_PRECISION));
            }
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Common validations to create or update a componentList
     */
    private void checkDescriptorToCreateOrUpdate(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, ComponentList componentList) throws MetamacException {
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);
    }

    /**
     * Common validations to create or update a componentList
     */
    private void checkComponentToCreateOrUpdate(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component) throws MetamacException {
        checkDataStructureDefinitionCanBeModified(dataStructureDefinitionVersion);

        // Restrictions to related resources.
        List<MetamacExceptionItem> exceptions = new LinkedList<MetamacExceptionItem>();
        checkPrimaryMeasure(ctx, dataStructureDefinitionVersion, component, exceptions);
        checkTimeDimension(ctx, dataStructureDefinitionVersion, component, exceptions);
        checkMeasureDimension(ctx, dataStructureDefinitionVersion, component, exceptions);
        checkDimension(ctx, dataStructureDefinitionVersion, component, exceptions);
        checkAttribute(ctx, dataStructureDefinitionVersion, component, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    @Override
    public void checkPrimaryMeasure(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (component instanceof PrimaryMeasure) {
            if (exceptions == null) {
                exceptions = new LinkedList<MetamacExceptionItem>();
            }

            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            String dataStructureDefinitionVersionUrn = dataStructureDefinitionVersion.getMaintainableArtefact().getUrn();

            // ConceptIdentity
            {
                Long conceptIdentityId = component.getCptIdRef().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id())
                        .eq(conceptIdentityId).build();
                PagedResult<ConceptMetamac> result = findConceptsCanBeDsdPrimaryMeasureByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptIdentityId)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.PRIMARY_MEASURE_CONCEPT_ID_REF));
                }
            }

            // Representation
            {
                if (component.getLocalRepresentation() != null) {
                    Representation representation = component.getLocalRepresentation();
                    if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
                        if (representation.getEnumerationCodelist() != null) {
                            Long codelistRepresentationId = representation.getEnumerationCodelist().getId();
                            List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                                    .withProperty(CodelistVersionMetamacProperties.id()).eq(codelistRepresentationId).build();
                            PagedResult<CodelistVersionMetamac> result = findCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(ctx, criteriaToVerifyConceptIdentityCode,
                                    pagingParameter);
                            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistRepresentationId)) {
                                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.PRIMARY_MEASURE_REPRESENTATION_ENUMERATED));
                            }
                        } else {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.PRIMARY_MEASURE_REPRESENTATION_ENUMERATED));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkTimeDimension(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (component instanceof TimeDimension) {
            if (exceptions == null) {
                exceptions = new LinkedList<MetamacExceptionItem>();
            }
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            String dataStructureDefinitionVersionUrn = dataStructureDefinitionVersion.getMaintainableArtefact().getUrn();

            // ConceptIdentity
            Long conceptIdentityId = component.getCptIdRef().getId();
            List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id())
                    .eq(conceptIdentityId).build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeDsdTimeDimensionByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptIdentityId)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.TIME_DIMENSION_CONCEPT_ID_REF));
            }
        }
    }

    @Override
    public void checkMeasureDimension(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (component instanceof MeasureDimension) {
            if (exceptions == null) {
                exceptions = new LinkedList<MetamacExceptionItem>();
            }
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            String dataStructureDefinitionVersionUrn = dataStructureDefinitionVersion.getMaintainableArtefact().getUrn();

            // ConceptIdentity
            {
                Long conceptIdentityId = component.getCptIdRef().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id())
                        .eq(conceptIdentityId).build();
                PagedResult<ConceptMetamac> result = findConceptsCanBeDsdMeasureDimensionByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptIdentityId)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MEASURE_DIMENSION_CONCEPT_ID_REF));
                }
            }

            // Role
            checkComponentRoles(ctx, dataStructureDefinitionVersion, component, exceptions);

            // Representation
            {
                if (component.getLocalRepresentation() != null) {
                    Representation representation = component.getLocalRepresentation();
                    if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
                        if (representation.getEnumerationConceptScheme() != null) {
                            Long conceptSchemeRepresentationId = representation.getEnumerationConceptScheme().getId();
                            List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                                    .withProperty(CodelistVersionMetamacProperties.id()).eq(conceptSchemeRepresentationId).build();
                            PagedResult<ConceptSchemeVersionMetamac> result = findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(ctx,
                                    criteriaToVerifyConceptIdentityCode, pagingParameter);
                            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptSchemeRepresentationId)) {
                                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MEASURE_DIMENSION_REPRESENTATION_ENUMERATED));
                            }
                        } else {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MEASURE_DIMENSION_REPRESENTATION_ENUMERATED));
                        }
                    }
                } else {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MEASURE_DIMENSION_REPRESENTATION_ENUMERATED));
                }
            }
        }
    }

    @Override
    public void checkDimension(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (component instanceof Dimension) {
            if (exceptions == null) {
                exceptions = new LinkedList<MetamacExceptionItem>();
            }
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            String dataStructureDefinitionVersionUrn = dataStructureDefinitionVersion.getMaintainableArtefact().getUrn();

            SpecialDimensionTypeEnum specialDimensionTypeEnum = ((DimensionComponent) component).getSpecialDimensionType();

            // ConceptIdentity
            {
                Long conceptIdentityId = component.getCptIdRef().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id())
                        .eq(conceptIdentityId).build();

                PagedResult<ConceptMetamac> result = null;
                if (SpecialDimensionTypeEnum.SPATIAL.equals(specialDimensionTypeEnum)) {
                    result = findConceptsCanBeDsdSpatialDimensionByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                } else {
                    result = findConceptsCanBeDsdDimensionByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                }

                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptIdentityId)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DIMENSION_CONCEPT_ID_REF));
                }
            }

            // Role
            checkComponentRoles(ctx, dataStructureDefinitionVersion, component, exceptions);

            // Geographical dimension
            {
                if (SpecialDimensionTypeEnum.SPATIAL.equals(specialDimensionTypeEnum)) {
                    // If it is a geographical dimension, then must have a enumerated local representation or a enumerated inherited representation
                    Representation spatialRepresentation = checkComponentRequiredLocalOrInheritedRepresentation(component, RepresentationTypeEnum.ENUMERATION, exceptions,
                            ServiceExceptionType.DATA_STRUCTURE_DEFINITION_DIM_REPRESENTATION_ENUM_REQUIRED);

                    // Check constraint over spatialRepresentation
                    if (spatialRepresentation != null) {
                        if (!spatialDimensionCheckRepresentation(ctx, component, spatialRepresentation)) {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DIMENSION_REPRESENTATION_ENUMERATED));
                        }
                    }
                    return;
                }
            }

            // Representation
            {
                if (!SpecialDimensionTypeEnum.SPATIAL.equals(specialDimensionTypeEnum)) {
                    // validation of enumerated local representation or a enumerated inherited representation
                    if (component.getLocalRepresentation() != null) {
                        Representation representation = component.getLocalRepresentation();
                        if (!dimensionCheckRepresentation(ctx, component, representation)) {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DIMENSION_REPRESENTATION_ENUMERATED));
                        }
                    } else {
                        // Check if the concept identity has a valid representation
                        if (component.getCptIdRef().getCoreRepresentation() != null) {
                            if (!dimensionCheckRepresentation(ctx, component, component.getCptIdRef().getCoreRepresentation())) {
                                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_DIM_REPRESENTATION_INHERITED_INVALID));
                            }
                        } else {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_DIM_REPRESENTATION_INHERITED_INVALID));
                        }
                    }
                }
            }
        }
    }

    /**
     * Check required local or inherited representation
     *
     * @param component
     * @param representationTypeEnum type of representation to be checked
     * @param exceptions
     * @param candidateSeviceExceptionType
     * @return
     */
    protected Representation checkComponentRequiredLocalOrInheritedRepresentation(Component component, RepresentationTypeEnum representationTypeEnum, List<MetamacExceptionItem> exceptions,
            CommonServiceExceptionType candidateSeviceExceptionType) {
        // Check required enumerated representation, then must have a enumerated local representation or a enumerated inherited representation
        Representation representation = null;
        if (component.getLocalRepresentation() != null) {
            representation = component.getLocalRepresentation();
            if (!representationTypeEnum.equals(representation.getRepresentationType())) {
                exceptions.add(new MetamacExceptionItem(candidateSeviceExceptionType));
                representation = null;
            }
        } else {
            if (component.getCptIdRef().getCoreRepresentation() != null) {
                representation = component.getCptIdRef().getCoreRepresentation();
                if (!representationTypeEnum.equals(representation.getRepresentationType())) {
                    exceptions.add(new MetamacExceptionItem(candidateSeviceExceptionType));
                    representation = null;
                }
            } else {
                exceptions.add(new MetamacExceptionItem(candidateSeviceExceptionType));
                representation = null;
            }
        }
        return representation;
    }

    protected boolean dimensionCheckRepresentation(ServiceContext ctx, Component component, Representation representation) throws MetamacException {
        if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
            if (representation.getEnumerationCodelist() != null) {
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                Long codelistRepresentationId = representation.getEnumerationCodelist().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                        .withProperty(CodelistVersionMetamacProperties.id()).eq(codelistRepresentationId).build();
                PagedResult<CodelistVersionMetamac> result = findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, component
                        .getCptIdRef().getNameableArtefact().getUrn());
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistRepresentationId)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    protected boolean spatialDimensionCheckRepresentation(ServiceContext ctx, Component component, Representation representation) throws MetamacException {
        if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
            if (representation.getEnumerationCodelist() != null) {
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                Long codelistRepresentationId = representation.getEnumerationCodelist().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                        .withProperty(CodelistVersionMetamacProperties.id()).eq(codelistRepresentationId).build();
                PagedResult<CodelistVersionMetamac> result = findCodelistsCanBeEnumeratedRepresentationForDsdSpatialDimensionByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter,
                        component.getCptIdRef().getNameableArtefact().getUrn());
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistRepresentationId)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void checkAttribute(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (component instanceof DataAttribute) {
            if (exceptions == null) {
                exceptions = new LinkedList<MetamacExceptionItem>();
            }
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            String dataStructureDefinitionVersionUrn = dataStructureDefinitionVersion.getMaintainableArtefact().getUrn();
            SpecialAttributeTypeEnum specialAttributeType = ((DataAttribute) component).getSpecialAttributeType();

            // ConceptIdentity
            {
                Long conceptIdentityId = component.getCptIdRef().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id())
                        .eq(conceptIdentityId).build();
                PagedResult<ConceptMetamac> result = null;
                if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(specialAttributeType)) {
                    result = findConceptsCanBeDsdMeasureAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                } else if (SpecialAttributeTypeEnum.SPATIAL_EXTENDS.equals(specialAttributeType)) {
                    result = findConceptsCanBeDsdSpatialAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                } else if (SpecialAttributeTypeEnum.TIME_EXTENDS.equals(specialAttributeType)) {
                    result = findConceptsCanBeDsdTimeAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                } else {
                    result = findConceptsCanBeDsdAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, dataStructureDefinitionVersionUrn);
                }

                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptIdentityId)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_ATTRIBUTE_CONCEPT_ID_REF));
                }
            }

            // Role
            checkComponentRoles(ctx, dataStructureDefinitionVersion, component, exceptions);

            // Representation: Geographical attribute
            {
                // If it is a geographical attribute, then must have a enumerated local representation or a enumerated inherited representation
                if (SpecialAttributeTypeEnum.SPATIAL_EXTENDS.equals(specialAttributeType)) {
                    Representation spatialRepresentation = checkComponentRequiredLocalOrInheritedRepresentation(component, RepresentationTypeEnum.ENUMERATION, exceptions,
                            ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_ENUM_REQUIRED);

                    // Check constraint over spatialRepresentation
                    if (spatialRepresentation != null) {
                        if (!spatialAttributeCheckRepresentation(ctx, component, spatialRepresentation)) {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_ATTRIBUTE_REPRESENTATION_ENUMERATED));
                        }
                    }

                    // Check: this type of attribute only supports a "not specified" relationship type
                    if (!(((DataAttribute) component).getRelateTo() instanceof NoSpecifiedRelationship)) {
                        exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_RELATETO_INVALID));
                    }
                    return;
                }
            }

            // Representation: Measure attribute
            {
                if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(specialAttributeType)) {
                    // If it is a measure attribute, then must have a enumerated local representation or a enumerated inherited representation
                    Representation measureRepresentation = checkComponentRequiredLocalOrInheritedRepresentation(component, RepresentationTypeEnum.ENUMERATION, exceptions,
                            ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_ENUM_REQUIRED);

                    // Check constraint over measure representation
                    if (measureRepresentation != null) {
                        if (!measureAttributeCheckRepresentation(ctx, component, measureRepresentation)) {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_ATTRIBUTE_REPRESENTATION_ENUMERATED));
                        }
                    }

                    // Check: this type of attribute only supports a "not specified" relationship type
                    if (!(((DataAttribute) component).getRelateTo() instanceof NoSpecifiedRelationship)) {
                        exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_RELATETO_INVALID));
                    }
                    return;
                }
            }

            // Representation: Time attribute
            {
                if (SpecialAttributeTypeEnum.TIME_EXTENDS.equals(specialAttributeType)) {
                    // If it is a time attribute, then must have a non enumerated local representation or a non enumerated inherited representation
                    Representation timeRepresentation = checkComponentRequiredLocalOrInheritedRepresentation(component, RepresentationTypeEnum.TEXT_FORMAT, exceptions,
                            ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_NONENUM_REQUIRED);

                    // Check constraint over time representation
                    if (timeRepresentation != null) {
                        List<MetamacExceptionItem> exceptionsTimeAttributeRepresentation = timeAttributeCheckRepresentation(ctx, component, timeRepresentation);
                        if (!exceptionsTimeAttributeRepresentation.isEmpty()) {
                            exceptions.addAll(exceptionsTimeAttributeRepresentation);
                        }
                    }

                    // Check: this type of attribute only supports a "not specified" relationship type
                    if (!(((DataAttribute) component).getRelateTo() instanceof NoSpecifiedRelationship)) {
                        exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_RELATETO_INVALID));
                    }
                    return;
                }
            }

            // Representation: Other
            {
                if (!SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(specialAttributeType) && !SpecialAttributeTypeEnum.SPATIAL_EXTENDS.equals(specialAttributeType)
                        && !SpecialAttributeTypeEnum.TIME_EXTENDS.equals(specialAttributeType)) {
                    if (component.getLocalRepresentation() != null) {
                        Representation representation = component.getLocalRepresentation();
                        if (!attributeCheckRepresentation(ctx, component, representation)) {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_ATTRIBUTE_REPRESENTATION_ENUMERATED));
                        }
                    } else {
                        // Check if the concept identity has a valid representation
                        if (component.getCptIdRef().getCoreRepresentation() != null) {
                            if (!attributeCheckRepresentation(ctx, component, component.getCptIdRef().getCoreRepresentation())) {
                                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_INVALID));
                            }
                        } else {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_INVALID));
                        }
                    }
                }
            }
        }
    }

    protected boolean attributeCheckRepresentation(ServiceContext ctx, Component component, Representation representation) throws MetamacException {
        if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            if (representation.getEnumerationCodelist() != null) {
                Long codelistRepresentationId = representation.getEnumerationCodelist().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                        .withProperty(CodelistVersionMetamacProperties.id()).eq(codelistRepresentationId).build();
                PagedResult<CodelistVersionMetamac> result = findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter, component
                        .getCptIdRef().getNameableArtefact().getUrn());
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistRepresentationId)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean spatialAttributeCheckRepresentation(ServiceContext ctx, Component component, Representation representation) throws MetamacException {
        if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
            if (representation.getEnumerationCodelist() != null) {
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                Long codelistRepresentationId = representation.getEnumerationCodelist().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                        .withProperty(CodelistVersionMetamacProperties.id()).eq(codelistRepresentationId).build();
                PagedResult<CodelistVersionMetamac> result = findCodelistsCanBeEnumeratedRepresentationForDsdSpatialAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode, pagingParameter,
                        component.getCptIdRef().getNameableArtefact().getUrn());
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistRepresentationId)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    protected boolean measureAttributeCheckRepresentation(ServiceContext ctx, Component component, Representation representation) throws MetamacException {
        if (RepresentationTypeEnum.ENUMERATION.equals(representation.getRepresentationType())) {
            if (representation.getEnumerationConceptScheme() != null) {
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                Long conceptSchemeRepresentationId = representation.getEnumerationConceptScheme().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptIdentityCode = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                        .withProperty(ConceptSchemeVersionMetamacProperties.id()).eq(conceptSchemeRepresentationId).build();
                PagedResult<ConceptSchemeVersionMetamac> result = findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureAttributeByCondition(ctx, criteriaToVerifyConceptIdentityCode,
                        pagingParameter);
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptSchemeRepresentationId)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    protected List<MetamacExceptionItem> timeAttributeCheckRepresentation(ServiceContext ctx, Component component, Representation representation) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new LinkedList<MetamacExceptionItem>();
        RepresentationConstraintValidator.validateTimeDimensionRepresentationType(representation, exceptions);
        return exceptions;
    }

    private void checkComponentRoles(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion, Component component, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        String serviceExceptionParameter = null;
        Set<Concept> roles = null;
        if (component instanceof Dimension) {
            serviceExceptionParameter = ServiceExceptionParameters.DIMENSION_ROLE;
            roles = ((Dimension) component).getRole();
        } else if (component instanceof MeasureDimension) {
            serviceExceptionParameter = ServiceExceptionParameters.MEASURE_DIMENSION_ROLE;
            roles = ((MeasureDimension) component).getRole();
        } else if (component instanceof DataAttribute && !(component instanceof ReportingYearStartDay)) {
            serviceExceptionParameter = ServiceExceptionParameters.DATA_ATTRIBUTE_ROLE;
            roles = ((DataAttribute) component).getRole();
        } else {
            return;
        }

        if (exceptions == null) {
            exceptions = new LinkedList<MetamacExceptionItem>();
        }

        // Role
        Set<Long> conceptRolesId = new HashSet<Long>();
        for (Concept concept : roles) {
            conceptRolesId.add(concept.getId());
        }

        if (!conceptRolesId.isEmpty()) {
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            List<ConditionalCriteria> criteriaToVerifyRoleCode = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).in(conceptRolesId).build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeDsdRoleByCondition(ctx, criteriaToVerifyRoleCode, pagingParameter);

            // Check, all concept roles must be exists in the result
            Set<Long> conceptRolesResultId = new HashSet<Long>();
            for (Concept concept : result.getValues()) {
                conceptRolesResultId.add(concept.getId());
            }

            for (Concept concept : roles) {
                if (!conceptRolesResultId.contains(concept.getId())) {
                    exceptions.add(MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.METADATA_INCORRECT)
                            .withMessageParameters(serviceExceptionParameter).build());
                }
            }
        }
    }

    private TaskInfo createVersionOfDataStructureDefinition(ServiceContext ctx, String urnToCopy, DataStructureDefinitionsCopyCallback dataStructureDefinitionsCopyCallback,
            VersionTypeEnum versionType, boolean isTemporal) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkVersioningDataStructureDefinition(urnToCopy, versionType, isTemporal, null, null);
        checkDataStructureDefinitionToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        return dataStructureDefinitionService.versioningDataStructureDefinition(ctx, urnToCopy, versionType, isTemporal, dataStructureDefinitionsCopyCallback);
    }

    @Override
    public DataStructureDefinitionVersionMetamac versioningHeadingAndStub(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacToCopy,
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacNewVersion) throws MetamacException {

        // Map of new dimension
        Map<String, Component> dimensionOrderMap = new HashMap<String, Component>();
        for (ComponentList componentList : dataStructureDefinitionVersionMetamacNewVersion.getGrouping()) {
            if (componentList instanceof DimensionDescriptor) {
                for (Component component : componentList.getComponents()) {
                    dimensionOrderMap.put(component.getCode(), component);
                }
                break;
            }
        }

        // Heading
        Integer dimOrderHeading = 1;
        for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamacToCopy.getHeadingDimensions()) {
            DimensionComponent targetDimension = (DimensionComponent) dimensionOrderMap.get(dimensionOrder.getDimension().getCode());
            if (targetDimension != null) {
                DimensionOrder targetDimensionOrder = new DimensionOrder();
                targetDimensionOrder.setDimension(targetDimension);
                targetDimensionOrder.setDimOrder(dimOrderHeading);
                dataStructureDefinitionVersionMetamacNewVersion.addHeadingDimension(targetDimensionOrder);
                dimOrderHeading++;
            }
        }

        // Stub
        Integer dimOrderStub = 1;
        for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamacToCopy.getStubDimensions()) {
            DimensionComponent targetDimension = (DimensionComponent) dimensionOrderMap.get(dimensionOrder.getDimension().getCode());
            if (targetDimension != null) {
                DimensionOrder targetDimensionOrder = new DimensionOrder();
                targetDimensionOrder.setDimension(targetDimension);
                targetDimensionOrder.setDimOrder(dimOrderStub);
                dataStructureDefinitionVersionMetamacNewVersion.addStubDimension(targetDimensionOrder);
                dimOrderStub++;
            }
        }

        return dataStructureDefinitionVersionMetamacNewVersion;
    }

    @Override
    public DataStructureDefinitionVersionMetamac versioningShowDecimalsPrecision(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacToCopy,
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacNewVersion) throws MetamacException {

        // Check if inherit if valid

        // Find Measure Dimension in old Version
        MeasureDimension previousMeasureDimension = null;
        for (ComponentList componentList : dataStructureDefinitionVersionMetamacToCopy.getGrouping()) {
            if (componentList instanceof DimensionDescriptor) {
                for (Component component : componentList.getComponents()) {
                    if (component instanceof MeasureDimension) {
                        previousMeasureDimension = (MeasureDimension) component;
                        break;
                    }
                }
            }
        }

        // Nothing to inherit
        if (previousMeasureDimension == null) {
            return dataStructureDefinitionVersionMetamacNewVersion;
        }

        // Find Measure Dimension in new Version
        MeasureDimension newMeasureDimension = null;
        for (ComponentList componentList : dataStructureDefinitionVersionMetamacNewVersion.getGrouping()) {
            if (componentList instanceof DimensionDescriptor) {
                for (Component component : componentList.getComponents()) {
                    if (component instanceof MeasureDimension) {
                        newMeasureDimension = (MeasureDimension) component;
                        break;
                    }
                }
            }
        }

        // There isn't measure dimension in new version
        if (newMeasureDimension == null) {
            return dataStructureDefinitionVersionMetamacNewVersion;
        }

        String previousConceptSchemeRepresentationUrn = previousMeasureDimension.getLocalRepresentation().getEnumerationConceptScheme().getMaintainableArtefact().getUrn();
        String newConceptSchemeRepresentationUrn = newMeasureDimension.getLocalRepresentation().getEnumerationConceptScheme().getMaintainableArtefact().getUrn();

        if (previousConceptSchemeRepresentationUrn.equals(newConceptSchemeRepresentationUrn)) {
            // showDecimalsPrecisions
            for (MeasureDimensionPrecision measureDimensionPrecision : dataStructureDefinitionVersionMetamacToCopy.getShowDecimalsPrecisions()) {
                MeasureDimensionPrecision targetMeasureDimensionPrecion = new MeasureDimensionPrecision();
                targetMeasureDimensionPrecion.setConcept(measureDimensionPrecision.getConcept());
                targetMeasureDimensionPrecion.setShowDecimalPrecision(measureDimensionPrecision.getShowDecimalPrecision());
                dataStructureDefinitionVersionMetamacNewVersion.addShowDecimalsPrecision(targetMeasureDimensionPrecion);
            }
        }

        return dataStructureDefinitionVersionMetamacNewVersion;
    }

    @Override
    public DataStructureDefinitionVersionMetamac versioningDimensionVisualisationInfo(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacToCopy,
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacNewVersion) throws MetamacException {

        // Map of new dimension
        Map<String, Component> newDimensionMap = new HashMap<String, Component>();
        for (ComponentList componentList : dataStructureDefinitionVersionMetamacNewVersion.getGrouping()) {
            if (componentList instanceof DimensionDescriptor) {
                for (Component component : componentList.getComponents()) {
                    newDimensionMap.put(component.getCode(), component);
                }
                break;
            }
        }

        // DimensionVisualisationInfo
        for (DimensionVisualisationInfo dimensionVisualizationInfo : dataStructureDefinitionVersionMetamacToCopy.getDimensionVisualisationInfos()) {
            // If exist the dimension in the new DSD
            if (newDimensionMap.containsKey(dimensionVisualizationInfo.getDimension().getCode())) {
                Representation representationToCopy = dimensionVisualizationInfo.getDimension().getLocalRepresentation();
                if (representationToCopy != null) {
                    // Dimension Representation
                    copyIfValidDimensionVisualizationInfo(dataStructureDefinitionVersionMetamacNewVersion, newDimensionMap, dimensionVisualizationInfo, representationToCopy);
                } else {
                    // Concept identity representation
                    Concept cptIdRefPrevious = dimensionVisualizationInfo.getDimension().getCptIdRef();
                    Concept cptIdRefNew = newDimensionMap.get(dimensionVisualizationInfo.getDimension().getCode()).getCptIdRef();
                    // check concept identities are the same
                    if (cptIdRefPrevious.getNameableArtefact().getUrn().equals(cptIdRefNew.getNameableArtefact().getUrn())) {
                        Representation coreRepresentationToCopy = dimensionVisualizationInfo.getDimension().getCptIdRef().getCoreRepresentation();
                        if (coreRepresentationToCopy != null) {
                            copyIfValidDimensionVisualizationInfo(dataStructureDefinitionVersionMetamacNewVersion, newDimensionMap, dimensionVisualizationInfo, coreRepresentationToCopy);
                        }
                    }
                }
            }
        }

        return dataStructureDefinitionVersionMetamacNewVersion;
    }

    private void copyIfValidDimensionVisualizationInfo(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacNewVersion, Map<String, Component> newDimensionMap,
            DimensionVisualisationInfo dimensionVisualizationInfo, Representation representation) {
        CodelistVersion codelistVersion = representation.getEnumerationCodelist();
        DimensionVisualisationInfo newDimensionVisualizationInfo = new DimensionVisualisationInfo();
        newDimensionVisualizationInfo.setDimension((DimensionComponent) newDimensionMap.get(dimensionVisualizationInfo.getDimension().getCode()));
        boolean addDimensionVisualizationInfo = false;
        // If the codeList representation is the same as that of the DisplayOrder.
        if (codelistVersion != null && dimensionVisualizationInfo.getDisplayOrder() != null) {
            if (codelistVersion.getMaintainableArtefact().getUrn().equals(dimensionVisualizationInfo.getDisplayOrder().getCodelistVersion().getMaintainableArtefact().getUrn())) {
                newDimensionVisualizationInfo.setDisplayOrder(dimensionVisualizationInfo.getDisplayOrder());
                addDimensionVisualizationInfo = true;
            }
        }
        // If the codeList representation is the same as that of the OpenessLevel.
        if (codelistVersion != null && dimensionVisualizationInfo.getHierarchyLevelsOpen() != null) {
            if (codelistVersion.getMaintainableArtefact().getUrn().equals(dimensionVisualizationInfo.getHierarchyLevelsOpen().getCodelistVersion().getMaintainableArtefact().getUrn())) {
                newDimensionVisualizationInfo.setHierarchyLevelsOpen(dimensionVisualizationInfo.getHierarchyLevelsOpen());
                addDimensionVisualizationInfo = true;
            }
        }
        // Add new info
        if (addDimensionVisualizationInfo) {
            dataStructureDefinitionVersionMetamacNewVersion.addDimensionVisualisationInfo(newDimensionVisualizationInfo);
        }
    }

    private void checkDataStructureDefinitionToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionToCopy = retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(dataStructureDefinitionVersionToCopy.getMaintainableArtefact(), dataStructureDefinitionVersionToCopy.getLifeCycleMetadata(), isTemporal);

        // Check does not exist any version 'no final'
        StructureVersion dataStructureVersionNoFinal = structureVersionRepository.findStructureVersionNoFinalClient(dataStructureDefinitionVersionToCopy.getStructure().getId());
        if (dataStructureVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
                    .withMessageParameters(dataStructureVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkDataStructureDefinitionCanBeModified(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(dataStructureDefinitionVersion.getLifeCycleMetadata(), dataStructureDefinitionVersion.getMaintainableArtefact().getUrn());
    }

    /**
     * Finds concept schemes with concepts could be concept in primary measure, time dimension, measure dimension or dimension in DSD
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeDsdSpecificDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String dsdUrn, ConceptRoleEnum... conceptRolesEnum) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkFindConceptsCanBeDsdPrimaryMeasureByCondition(conditions, pagingParameter, dsdUrn, null);

        // Retrieve dsd
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dsdUrn);
        if (dataStructureDefinitionVersionMetamac.getStatisticalOperation() == null) {
            // do not execute find, zero results
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }

        // Prepare conditions
        Class entitySearchedClass = ConceptSchemeVersionMetamac.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // ConceptScheme internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE)
                .buildSingle());
        // ConceptScheme Transversal u operation (of DSD)
        Property<ConceptSchemeVersionMetamac> conceptSchemeTypeProperty = ConceptSchemeVersionMetamacProperties.type();
        Property conceptSchemeRelatedOperationUrnProperty = ConceptSchemeVersionMetamacProperties.relatedOperation().urn();
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(conceptSchemeTypeProperty).eq(ConceptSchemeTypeEnum.TRANSVERSAL).or().lbrace()
                .withProperty(conceptSchemeTypeProperty).eq(ConceptSchemeTypeEnum.OPERATION).and().withProperty(conceptSchemeRelatedOperationUrnProperty)
                .eq(dataStructureDefinitionVersionMetamac.getStatisticalOperation().getUrn()).rbrace().buildSingle());
        // Concept primary_measure
        Property conceptRoleEnumProperty = new LeafProperty<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamacProperties.items().getName(), ConceptMetamacProperties.sdmxRelatedArtefact()
                .getName(), false, ConceptSchemeVersionMetamac.class);
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(conceptRoleEnumProperty).in((Object[]) conceptRolesEnum).buildSingle());

        // Find
        return conceptSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    /**
     * Finds concepts could be concept in primary measure, time dimension, measure dimension or dimension in DSD
     */
    @SuppressWarnings({"unchecked"})
    private PagedResult<ConceptMetamac> findConceptsCanBeDsdSpecificDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String dsdUrn,
            ConceptRoleEnum... conceptRolesEnum) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkFindConceptsCanBeDsdPrimaryMeasureByCondition(conditions, pagingParameter, dsdUrn, null);

        // Retrieve dsd
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx,
                dsdUrn);
        if (dataStructureDefinitionVersionMetamac.getStatisticalOperation() == null) {
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }

        return executeFindConceptsCanBeDsdSpecificDimensionByCondition(conditions, pagingParameter, dataStructureDefinitionVersionMetamac.getStatisticalOperation().getUrn(), conceptRolesEnum);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<ConceptMetamac> executeFindConceptsCanBeDsdSpecificDimensionByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String statisticalOperationUrn,
            ConceptRoleEnum... conceptRolesEnum) {
        // Prepare conditions
        Class entitySearchedClass = ConceptMetamac.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // ConceptScheme internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient())
                .eq(Boolean.TRUE).buildSingle());
        // ConceptScheme Transversal u operation (of DSD)
        Property conceptSchemeTypeProperty = buildConceptPropertyToConceptSchemeType();
        Property conceptSchemeRelatedOperationUrnProperty = new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties
                .relatedOperation().urn().getName(), false, entitySearchedClass);
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(conceptSchemeTypeProperty).eq(ConceptSchemeTypeEnum.TRANSVERSAL).or().lbrace()
                .withProperty(conceptSchemeTypeProperty).eq(ConceptSchemeTypeEnum.OPERATION).and().withProperty(conceptSchemeRelatedOperationUrnProperty).eq(statisticalOperationUrn).rbrace()
                .buildSingle());
        // Concept related artefact
        Property conceptRoleEnumProperty = ConceptMetamacProperties.sdmxRelatedArtefact();
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(conceptRoleEnumProperty).in((Object[]) conceptRolesEnum).buildSingle());
        // Do not repeat results
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).distinctRoot().buildSingle());

        // Find
        return conceptMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException (Concept to ConceptMetamac)
    }

    /**
     * Finds concept schemes with type Role, Measure...
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithSpecificType(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter,
            ConceptSchemeTypeEnum conceptSchemeTypeEnum) throws MetamacException {

        // Prepare conditions
        Class entitySearchedClass = ConceptSchemeVersionMetamac.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // ConceptScheme internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE)
                .buildSingle());
        // ConceptScheme measure
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(ConceptSchemeVersionMetamacProperties.type()).eq(conceptSchemeTypeEnum).buildSingle());
        // Do not repeat results
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build());

        // Find
        return conceptSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    @SuppressWarnings("unchecked")
    private PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsd(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter,
            String conceptUrn, boolean isSpatialVariableRequired) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkFindCodelistsCanBeEnumeratedRepresentationForDsd(conditions, pagingParameter, conceptUrn, null);

        // Retrieve variable of concept
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(ctx, conceptUrn);
        Variable variable = concept.getVariable();
        if (variable == null || (isSpatialVariableRequired && !VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType()))) {
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }

        return findCodelistsPublishedByConditions(ctx, conditions, pagingParameter, variable.getNameableArtefact().getUrn());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<CodelistVersionMetamac> findCodelistsPublishedByConditions(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String variableUrn)
            throws MetamacException {

        // Prepare conditions
        Class entitySearchedClass = CodelistVersionMetamac.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Codelist internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE)
                .buildSingle());
        // Variable
        if (variableUrn != null) {
            conditions
                    .add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(CodelistVersionMetamacProperties.variable().nameableArtefact().urn()).eq(variableUrn).buildSingle());
        }
        // Do not repeat results
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build());

        // Codelist with access type = PUBLIC
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());

        // Find
        return codelistVersionMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<CodelistOrderVisualisation> findCodelistOrderVisualisationRepositoryByConditions(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter,
            Long codelistId) throws MetamacException {

        // Prepare conditions
        Class entitySearchedClass = CodelistOrderVisualisation.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Codelist internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(CodelistOrderVisualisationProperties.codelistVersion().id()).eq(codelistId).buildSingle());
        // Do not repeat results
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build());

        // Find
        return codelistOrderVisualisationRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private PagedResult<CodelistOpennessVisualisation> findCodelistOpennessVisualisationRepositoryByConditions(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, Long codelistId) throws MetamacException {

        // CodelistOrderVisualisationProperties

        // Prepare conditions
        Class entitySearchedClass = CodelistOrderVisualisation.class;
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Codelist internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(CodelistOrderVisualisationProperties.codelistVersion().id()).eq(codelistId).buildSingle());
        // Do not repeat results
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build());

        // Find
        return codelistOpennessVisualisationRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    private Property<ConceptMetamac> buildConceptPropertyToConceptSchemeType() {
        return new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), false, ConceptMetamac.class);
    }

    private DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionVersionByUrn(String urn) throws MetamacException {
        DataStructureInvocationValidator.checkRetrieveByUrn(urn);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = getDataStructureDefinitionVersionMetamacRepository().findByUrn(urn);
        if (dataStructureDefinitionVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return dataStructureDefinitionVersion;
    }

    protected void addSpatialConditionForConceptSchemes(List<ConditionalCriteria> conditions) {
        // Prepare conditions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Condition Spatial
        @SuppressWarnings("rawtypes")
        Property conceptSpatialVariableProperty = new LeafProperty<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamacProperties.items().getName(), ConceptMetamacProperties.variable().type()
                .getName(), false, ConceptSchemeVersionMetamac.class);

        conditions.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(conceptSpatialVariableProperty).eq(VariableTypeEnum.GEOGRAPHICAL).buildSingle());
    }

    protected void addSpatialConditionForConcepts(List<ConditionalCriteria> conditions) {
        // Prepare conditions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Condition Spatial
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.variable().type()).eq(VariableTypeEnum.GEOGRAPHICAL).buildSingle());
    }

}