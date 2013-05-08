package org.siemac.metamac.srm.core.dsd.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.ent.domain.ExternalItemRepository;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
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
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
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
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.DataStructureDefinitionsCopyCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

/**
 * Implementation of DataStructureDefinitionMetamacService.
 */
@Service("dataStructureDefinitionMetamacService")
public class DataStructureDefinitionMetamacServiceImpl extends DataStructureDefinitionMetamacServiceImplBase {

    @Autowired
    private DataStructureDefinitionService          dataStructureDefinitionService;

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
        return (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx, urn);
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
        }

        // TODO recalcular los index-order al eliminar una dimension
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
    public TaskInfo copyDataStructureDefinition(ServiceContext ctx, String urnToCopy) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return dataStructureDefinitionService.copyDataStructureDefinition(ctx, urnToCopy, maintainerUrn, versionPattern, dataStructureDefinitionsCopyCallback);
    }

    @Override
    public DataStructureDefinitionVersionMetamac versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfDataStructureDefinition(ctx, urnToCopy, versionType, false);
    }

    @Override
    public DataStructureDefinitionVersionMetamac createTemporalDataStructureDefinition(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfDataStructureDefinition(ctx, urnToCopy, null, true);
    }

    @Override
    public DataStructureDefinitionVersionMetamac createVersionFromTemporalDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {
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

        return dataStructureVersionTemporal;
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
    public DataStructureDefinitionVersionMetamac endDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkEndValidity(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ProcStatusEnum.EXTERNALLY_PUBLISHED);

        // End validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, urn, null);

        return dataStructureDefinitionVersionMetamac;
    }

    @Override
    public List<MetamacExceptionItem> checkDataStructureDefinitionTranslations(ServiceContext ctx, Long structureVersionId, String locale) {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        getDataStructureDefinitionVersionMetamacRepository().checkDataStructureDefinitionVersionTranslations(structureVersionId, locale, exceptionItems);
        componentListRepository.checkComponentListTranslations(structureVersionId, locale, exceptionItems);
        componentRepository.checkComponentTranslations(structureVersionId, locale, exceptionItems);
        return exceptionItems;
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
        return findCodelistsCanBeEnumeratedRepresentationForDsd(ctx, conditions, pagingParameter, conceptUrn);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
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
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {
        return findCodelistsCanBeEnumeratedRepresentationForDsd(ctx, conditions, pagingParameter, conceptUrn);
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
     * Retrieves version of a data structure definition in specific procStatus
     */
    private DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionVersionByProcStatus(ServiceContext ctx, String urn, ProcStatusEnum... procStatus) throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                .withProperty(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn()).eq(urn)
                .withProperty(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacPagedResult = getDataStructureDefinitionVersionMetamacRepository().findByCondition(conditions,
                pagingParameter);

        if (dataStructureDefinitionVersionMetamacPagedResult.getValues().size() != 1) {
            // check data structure definition exists to throws specific exception
            retrieveDataStructureDefinitionByUrn(ctx, urn);

            // if exists, throw exception about wrong proc status
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
        return dataStructureDefinitionVersionMetamacPagedResult.getValues().get(0);
    }

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
    }

    private DataStructureDefinitionVersionMetamac createVersionOfDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType, boolean isTemporal) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkVersioningDataStructureDefinition(urnToCopy, versionType, isTemporal, null, null);
        checkDataStructureDefinitionToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacNewVersion = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService
                .versioningDataStructureDefinition(ctx, urnToCopy, versionType, isTemporal, dataStructureDefinitionsVersioningCallback);

        return dataStructureDefinitionVersionMetamacNewVersion;
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
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
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
        Property conceptRoleEnumProperty = buildConceptSchemePropertyToConceptSdmxRelatedArtefact();
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(entitySearchedClass).withProperty(conceptRoleEnumProperty).in((Object[]) conceptRolesEnum).buildSingle());
        // Do not repeat results and order by. Order by items id is due to bug in Sculpor criteria
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.items().id()).ascending().distinctRoot().build());

        // Find
        return conceptSchemeVersionMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    /**
     * Finds concepts could be concept in primary measure, time dimension, measure dimension or dimension in DSD
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
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
        // Concept primary_measure
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
            String conceptUrn) throws MetamacException {
        // Validation
        DsdsMetamacInvocationValidator.checkFindCodelistsCanBeEnumeratedRepresentationForDsd(conditions, pagingParameter, conceptUrn, null);

        // Retrieve variable of concept
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(ctx, conceptUrn);
        Variable variable = concept.getVariable();
        if (variable == null) {
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

    private Property<ConceptSchemeVersionMetamac> buildConceptSchemePropertyToConceptSdmxRelatedArtefact() {
        return new LeafProperty<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamacProperties.items().getName(), ConceptMetamacProperties.sdmxRelatedArtefact().getName(), false,
                ConceptSchemeVersionMetamac.class);

    }

}