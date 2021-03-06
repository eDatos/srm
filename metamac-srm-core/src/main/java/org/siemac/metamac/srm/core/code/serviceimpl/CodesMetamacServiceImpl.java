package org.siemac.metamac.srm.core.code.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.joda.time.DateTime;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceapi.MiscMetamacService;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategorisationsUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesUtils;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.common.service.utils.SemanticIdentifierValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvImportationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.normalisation.DiceLuceneRamAproxStringMatch;
import org.siemac.metamac.srm.core.normalisation.MatchResult;
import org.siemac.metamac.srm.core.notices.serviceimpl.utils.NoticesCallbackMetamacImpl;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationVariableElementsTsvHeader;
import org.siemac.metamac.srm.core.task.serviceapi.TasksMetamacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseMergeAssert;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesInvocationValidator;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.srm.core.notices.serviceimpl.utils.NoticesCallback;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Implementation of CodesMetamacService.
 */
@Service("codesMetamacService")
public class CodesMetamacServiceImpl extends CodesMetamacServiceImplBase {

    @Autowired
    private BaseService                    baseService;

    @Autowired
    private CodesService                   codesService;

    @Autowired
    private CategoriesMetamacService       categoriesMetamacService;

    @Autowired
    private ItemSchemeVersionRepository    itemSchemeVersionRepository;

    @Autowired
    private CodeRepository                 codeRepository;

    @Autowired
    private ItemSchemeRepository           itemSchemeRepository;

    @Autowired
    private IdentifiableArtefactRepository identifiableArtefactRepository;

    @Autowired
    @Qualifier("codelistLifeCycle")
    private LifeCycle                      codelistLifeCycle;

    @Autowired
    private SrmValidation                  srmValidation;

    @Autowired
    private SrmConfiguration               srmConfiguration;

    @Autowired
    @Qualifier("codesVersioningCallbackMetamac")
    private ItemSchemesCopyCallback        codesVersioningWithCodesCallback;

    @Autowired
    @Qualifier("codesVersioningWithoutCodesCallbackMetamac")
    private ItemSchemesCopyCallback        codesVersioningWithoutCodesCallback;

    @Autowired
    @Qualifier("codesDummyVersioningCallbackMetamac")
    private ItemSchemesCopyCallback        codesDummyVersioningCallbackMetamac;

    @Autowired
    @Qualifier("codesCopyCallbackMetamac")
    private ItemSchemesCopyCallback        codesCopyCallback;

    @Autowired
    @Qualifier(NoticesCallbackMetamacImpl.BEAN_ID)
    private NoticesCallback                noticesCallbackMetamac;

    @Autowired
    private TasksMetamacService            tasksMetamacService;

    @Autowired
    private InternationalStringRepository  internationalStringRepository;

    @Autowired
    private MiscMetamacService             miscMetamacService;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager                entityManager;

    private static Logger                  logger = LoggerFactory.getLogger(CodesMetamacService.class);

    public CodesMetamacServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistVersionMetamac createCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {

        // Fill and validate Codelist
        preCreateCodelist(ctx, codelistVersion);

        // In creation, 'replaceTo' metadata must be copied to set after save codelist, due to flushing
        List<CodelistVersionMetamac> replaceTo = new ArrayList<CodelistVersionMetamac>(codelistVersion.getReplaceToCodelists());
        codelistVersion.removeAllReplaceToCodelists();

        // Save codelist
        codelistVersion = (CodelistVersionMetamac) codesService.createCodelist(ctx, codelistVersion, SrmConstants.VERSION_PATTERN_METAMAC);

        // Post create actions
        codelistVersion = createCodelistOrderVisualisationAlphabetical(ctx, codelistVersion); // Add alphabetical order
        codelistVersion = createCodelistOpennessVisualisationAllOpened(ctx, codelistVersion); // Add all opened visualization
        // Execute common actions after creation
        postCreateCodelist(ctx, codelistVersion, replaceTo);

        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac preCreateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Fill metadata
        codelistVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        codelistVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        codelistVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac postCreateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion, List<CodelistVersionMetamac> replaceTo) throws MetamacException {
        // Fill replaceTo metadata after save entity, fill replacedBy metadata too
        for (CodelistVersionMetamac codelistReplaceTo : replaceTo) {
            codelistVersion.addReplaceToCodelist(codelistReplaceTo);
        }
        codelistVersion = getCodelistVersionMetamacRepository().save(codelistVersion);

        // Add the codelist to the family and variable
        if (codelistVersion.getFamily() != null) {
            codelistVersion.getFamily().addCodelist(codelistVersion);
        }
        if (codelistVersion.getVariable() != null) {
            codelistVersion.getVariable().addCodelist(codelistVersion);
        }
        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac updateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Fill replaceBy metadata
        for (CodelistVersionMetamac replaceTo : codelistVersion.getReplaceToCodelists()) {
            replaceTo.setReplacedByCodelist(codelistVersion);
        }

        // if variable is changed, remove variable elements of codes
        if (codelistVersion.getIsVariableUpdated()) {
            // codelist can not be temporal, because it can merge in original version, and in this case variable must be inmutable
            SrmValidationUtils.checkArtefactIsNotTemporal(codelistVersion.getMaintainableArtefact());
            // actions in codes
            getCodeMetamacRepository().clearCodesVariableElementByCodelist(codelistVersion);
        }

        String oldUrn = codelistVersion.getMaintainableArtefact().getUrn();

        // Save codelist
        CodelistVersionMetamac codelistVersionMetamac = (CodelistVersionMetamac) codesService.updateCodelist(ctx, codelistVersion);

        // Updates URNs of CodelistOrderVisualisations and CodelistOpenVisualisations
        // If code have been changed, update URN. In metamac not is possible to change the maintainer,
        // if this were not so, the URN will also be updated when the maintainer changes.
        if (codelistVersionMetamac.getMaintainableArtefact().getIsCodeUpdated()) {
            // IMPORTANT: Update order and open urn efficiently to avoid one update for each code
            getCodelistOrderVisualisationRepository().updateUrnAllCodelistOrderVisualisationsByCodelistEfficiently(codelistVersionMetamac, oldUrn);
            getCodelistOpennessVisualisationRepository().updateUrnAllCodelistOpennessVisualisationsByCodelistEfficiently(codelistVersionMetamac, oldUrn);
            codelistVersionMetamac = retrieveCodelistByUrn(ctx, codelistVersion.getMaintainableArtefact().getUrn());
        }

        // Save codelist
        return codelistVersionMetamac;
    }

    @Override
    public CodelistVersionMetamac retrieveCodelistByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistVersionByUrn(urn);
        return codelistVersion;
    }

    @Override
    public List<CodelistVersionMetamac> retrieveCodelistVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve codelistVersions
        List<CodelistVersion> codelistVersions = codesService.retrieveCodelistVersions(ctx, urn);

        // Typecast to CodelistVersionMetamac
        return codelistVersionsToCodelistVersionsMetamac(codelistVersions);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<CodelistVersion> codelistVersionPagedResult = codesService.findCodelistsByCondition(ctx, conditions, pagingParameter);
        return pagedResultCodelistVersionToMetamac(codelistVersionPagedResult);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanReplaceToByCondition(ServiceContext ctx, String codelistUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        return findCodelistsCanReplaceToByCondition(ctx, codelistUrn, false, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsWhoseCodesCanBeVariableElementGeographicalGranularityByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build();
        }
        // codelist from DATA (codelist must be this one always)
        String codelistUrn = srmConfiguration.retrieveCodelistUrnForVariableElementGeographicalGranularity();
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().urn()).eq(codelistUrn).buildSingle());
        // scheme externally published (variable element has not lifecycle)
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE)
                .buildSingle());
        // Codelist with access type = PUBLIC
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());

        return findCodelistsByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public CodelistVersionMetamac sendCodelistToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac sendCodelistToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac rejectCodelistProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac rejectCodelistDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public TaskInfo publishInternallyCodelist(ServiceContext ctx, String urn, Boolean forceLatestFinal, Boolean canBeBackground) throws MetamacException {
        // Initialize
        CodelistVersion codelistVersionToPublish = retrieveCodelistByUrn(ctx, urn);

        TaskInfo versioningResult = new TaskInfo();
        if (mustExecuteTaskInBackground(true, codelistVersionToPublish, canBeBackground)) {
            // Scheduled is needed
            // Validation
            codelistLifeCycle.prePublishResourceInInternallyPublished(ctx, urn, ProcStatusEnum.INTERNALLY_PUBLISHED); // PrePublish for early error checking

            ItemScheme itemScheme = codelistVersionToPublish.getItemScheme();
            itemScheme.setIsTaskInBackground(Boolean.TRUE);
            itemScheme = itemSchemeRepository.save(itemScheme);

            // execute in background
            String jobKey = tasksMetamacService.plannifyPublicationInternallyCodelist(ctx, urn, forceLatestFinal);

            versioningResult.setIsPlannedInBackground(Boolean.TRUE);
            versioningResult.setJobKey(jobKey);
        } else {
            // The publication is now
            CodelistVersion codelist = (CodelistVersionMetamac) codelistLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
            ItemScheme itemScheme = codelist.getItemScheme();
            itemScheme.setIsTaskInBackground(Boolean.FALSE);
            itemScheme = itemSchemeRepository.save(itemScheme);

            versioningResult.setUrnResult(codelist.getMaintainableArtefact().getUrn());
        }
        return versioningResult;
    }

    @Override
    public CodelistVersionMetamac publishExternallyCodelist(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteCodelist(ServiceContext ctx, String urn) throws MetamacException {
        CodelistVersionMetamac codelistVersionMetamac = retrieveCodelistByUrn(ctx, urn);
        deleteCodelist(ctx, codelistVersionMetamac, Boolean.TRUE);
    }

    private void deleteCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersionMetamac, boolean doValidations) throws MetamacException {

        // Validation
        if (doValidations) {
            checkCodelistCanBeModified(codelistVersionMetamac);
        }
        // First, delete all codes efficiently
        getCodeMetamacRepository().deleteAllCodesEfficiently(codelistVersionMetamac.getId());

        // Delete
        codelistVersionMetamac = retrieveCodelistByUrn(ctx, codelistVersionMetamac.getMaintainableArtefact().getUrn()); // reload without codes
        codelistVersionMetamac.removeAllReplaceToCodelists(); // codelist can be deleted although it replaces to another codelist
        codelistVersionMetamac.setDefaultOrderVisualisation(null);
        codelistVersionMetamac.setDefaultOpennessVisualisation(null);
        codesService.deleteCodelist(ctx, codelistVersionMetamac.getMaintainableArtefact().getUrn());
    }

    @Override
    public TaskInfo copyCodelist(ServiceContext ctx, String urnToCopy, String newCode) throws MetamacException {
        ItemSchemesCopyCallback callback = codesCopyCallback;
        NoticesCallback noticesCallback = noticesCallbackMetamac;
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return codesService.copyCodelist(ctx, urnToCopy, newCode, maintainerUrn, versionPattern, Boolean.TRUE, callback, noticesCallback);
    }

    @Override
    public TaskInfo versioningCodelist(ServiceContext ctx, String urnToCopy, Boolean versioningCodes, VersionTypeEnum versionType) throws MetamacException {
        ItemSchemesCopyCallback callback = versioningCodes == null || versioningCodes ? codesVersioningWithCodesCallback : codesVersioningWithoutCodesCallback;
        return createVersionOfCodelist(ctx, urnToCopy, callback, noticesCallbackMetamac, versionType, false);
    }

    @Override
    public TaskInfo createTemporalCodelist(ServiceContext ctx, String urnToCopy) throws MetamacException {

        return createVersionOfCodelist(ctx, urnToCopy, codesDummyVersioningCallbackMetamac, noticesCallbackMetamac, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalCodelist(ServiceContext ctx, String urnToCopy, Boolean versioningCodes, VersionTypeEnum versionTypeEnum) throws MetamacException {

        CodelistVersionMetamac codelistVersionTemporal = retrieveCodelistByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(codelistVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        CodelistVersionMetamac codelistVersion = retrieveCodelistOriginalFromUrnTemporal(ctx, urnToCopy);

        String nextVersion = SdmxVersionUtils.createNextVersion(codelistVersion.getMaintainableArtefact().getVersionLogic(), codelistVersion.getItemScheme().getVersionPattern(), versionTypeEnum);

        // Set the new version in the temporal artifact
        codelistVersionTemporal.getMaintainableArtefact().setVersionLogic(nextVersion);

        codelistVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        codelistVersionTemporal = (CodelistVersionMetamac) codesService.updateCodelist(ctx, codelistVersionTemporal);

        // Set null replacedBy in the original entity
        codelistVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        categoriesMetamacService.createVersionFromTemporalCategorisations(ctx, codelistVersionTemporal.getMaintainableArtefact());

        // Convert order and openness visualisations in no temporal
        createOrderVisualisationVersionFromTemporalOrderVisualisation(ctx, codelistVersionTemporal);
        createOpennessVisualisationVersionFromTemporalOpennessVisualisation(ctx, codelistVersionTemporal);

        // Delete all codes if user decided not included them in the versioned codelist
        deleteAllCodesIfProceed(codelistVersionTemporal, versioningCodes);

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(codelistVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    @Override
    public CodelistVersionMetamac mergeTemporalVersion(ServiceContext ctx, String urnTemporal) throws MetamacException {

        CodelistVersionMetamac codelistTemporalVersion = retrieveCodelistByUrn(ctx, urnTemporal);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(codelistTemporalVersion.getMaintainableArtefact());
        SrmValidationUtils.checkArtefactProcStatus(codelistTemporalVersion.getLifeCycleMetadata(), codelistTemporalVersion.getMaintainableArtefact().getUrn(), ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        CodelistVersionMetamac codelistVersion = retrieveCodelistOriginalFromUrnTemporal(ctx, codelistTemporalVersion.getMaintainableArtefact().getUrn());

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToCodelist(codelistVersion, codelistTemporalVersion, internationalStringRepository);

        // Merge Metamac metadata of ItemScheme
        codelistVersion.setLifeCycleMetadata(
                BaseReplaceFromTemporalMetamac.replaceProductionAndDifussionLifeCycleMetadataFromTemporal(codelistVersion.getLifeCycleMetadata(), codelistTemporalVersion.getLifeCycleMetadata()));

        // ShortName
        if (!BaseMergeAssert.assertEqualsInternationalString(codelistVersion.getShortName(), codelistTemporalVersion.getShortName())) {
            codelistVersion.setShortName(BaseMergeAssert.mergeUpdateInternationalString(codelistVersion.getShortName(), codelistTemporalVersion.getShortName(), internationalStringRepository));
        }
        // IsRecommended
        codelistVersion.setIsRecommended(codelistTemporalVersion.getIsRecommended());
        // AccessType
        codelistVersion.setAccessType(codelistTemporalVersion.getAccessType());
        // CodelistFamily
        codelistVersion.setFamily(codelistTemporalVersion.getFamily());
        // Variable
        codelistVersion.setVariable(codelistTemporalVersion.getVariable());

        // Merge metadata of Item
        Map<String, Item> temporalItemMap = SrmServiceUtils.createMapOfItemsByOriginalUrn(codelistTemporalVersion.getItems());
        List<ItemResult> codesFoundEfficiently = getCodeMetamacRepository().findCodesByCodelistUnordered(codelistTemporalVersion.getId(), ItemMetamacResultSelection.ALL);
        Map<String, ItemResult> codesFoundEfficientlyByUrn = SdmxSrmUtils.createMapOfItemsResultByUrn(codesFoundEfficiently);

        for (Item item : codelistVersion.getItems()) {
            CodeMetamac code = (CodeMetamac) item;
            CodeMetamac codeTemp = (CodeMetamac) temporalItemMap.get(code.getNameableArtefact().getUrn());
            ItemResult codeTempItemResult = codesFoundEfficientlyByUrn.get(codeTemp.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(code, codeTempItemResult, internationalStringRepository);

            // Metamac Metadata
            // ShortName
            Map<String, String> shortName = SrmServiceUtils.getCodeItemResultShortName(codeTempItemResult);
            if (!BaseMergeAssert.assertEqualsInternationalString(code.getShortName(), shortName)) {
                code.setShortName(BaseMergeAssert.mergeUpdateInternationalString(code.getShortName(), shortName, internationalStringRepository));
            }

            // VariableElement
            code.setVariableElement(codeTemp.getVariableElement());

            // Order Visualisation
            for (int i = 1; i <= SrmConstants.CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
                Integer order = SrmServiceUtils.getCodeOrder(codeTemp, i);
                SrmServiceUtils.setCodeOrder(code, i, order);
            }

            // Openness Visualisation
            for (int i = 1; i <= SrmConstants.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER; i++) {
                Boolean openness = SrmServiceUtils.getCodeOpenness(codeTemp, i);
                SrmServiceUtils.setCodeOpenness(code, i, openness);
            }

        }

        // ===============================================================
        // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear the
        // context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
        CodesUtils.addCodelistOpennessVisualisationTemporalToItemScheme(codelistTemporalVersion, codelistVersion, internationalStringRepository);
        CodesUtils.addCodelistOrderVisualisationTemporalToItemScheme(codelistTemporalVersion, codelistVersion, internationalStringRepository);
        CategorisationsUtils.addCategorisationsTemporalToItemScheme(codelistTemporalVersion, codelistVersion);
        // FLUSH AND CLEAR NEEDED, see the next lines
        // ===============================================================

        setDefaultCodelistOrdenVisualisation(codelistTemporalVersion, codelistVersion);
        setDefaultCodelistOpennessVisualisation(codelistTemporalVersion, codelistVersion);

        // Delete temporal version
        // ===============================================================
        // DANGEROUS CODE: Also clear is necessary because items are already loaded
        entityManager.flush();
        entityManager.clear();
        // ===============================================================

        codelistTemporalVersion = retrieveCodelistByUrn(ctx, urnTemporal);
        deleteCodelist(ctx, codelistTemporalVersion, Boolean.FALSE);

        codelistVersion = retrieveCodelistByUrn(ctx, codelistVersion.getMaintainableArtefact().getUrn());
        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac startCodelistValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkStartValidity(urn, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, urn);
        srmValidation.checkStartValidity(ctx, codelistVersion.getMaintainableArtefact(), codelistVersion.getLifeCycleMetadata());

        // Start validity
        codelistVersion = (CodelistVersionMetamac) codesService.startCodelistValidity(ctx, urn, null);
        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac endCodelistValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, urn);
        srmValidation.checkEndValidity(ctx, codelistVersion.getMaintainableArtefact(), codelistVersion.getLifeCycleMetadata());

        // End validity
        codelistVersion = (CodelistVersionMetamac) codesService.endCodelistValidity(ctx, urn, null);
        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac retrieveCodelistByCodeUrn(ServiceContext ctx, String codeUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(codeUrn);

        // Retrieve
        CodelistVersionMetamac codelistVersion = getCodelistVersionMetamacRepository().findByCode(codeUrn);
        if (codelistVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(codeUrn).build();
        }
        return codelistVersion;
    }

    // ------------------------------------------------------------------------------------
    // CODES
    // ------------------------------------------------------------------------------------

    @Override
    public CodeMetamac createCode(ServiceContext ctx, String codelistUrn, CodeMetamac code) throws MetamacException {
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // IMPORTANT: If any restriction change, review operations of importation of tsv file and copy of codes,
        // because the importation doesnot call to service methods to avoid extra queries, flushing...
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);

        preCreateCode(ctx, codelistVersion, code);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        // Save code
        code = (CodeMetamac) codesService.createCode(ctx, codelistUrn, code);
        // Add to all visualisations of codelist
        code = initVisualisationsToCodeCreated(codelistVersion, code);
        return code;
    }

    @Override
    public void copyCodesInCodelist(ServiceContext ctx, String codelistSourceUrn, String codelistTargetUrn, List<CodeToCopy> codesToCopy) throws MetamacException {

        // Validation
        if (CollectionUtils.isEmpty(codesToCopy)) {
            return;
        }
        CodesMetamacInvocationValidator.checkCopyCodesInCodelist(codelistSourceUrn, codelistTargetUrn, codesToCopy, null);

        // TARGET: codelist
        CodelistVersionMetamac codelistVersionTarget = retrieveCodelistByUrn(ctx, codelistTargetUrn);
        checkCodelistCanBeModified(codelistVersionTarget);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersionTarget);
        // TARGET: actual codes in codelist
        List<Code> codesInCodelistTargetBeforeCopy = codelistVersionTarget.getItems();
        Map<String, CodeMetamac> codesInCodelistTargetBeforeCopyByCode = new HashMap<String, CodeMetamac>(codesInCodelistTargetBeforeCopy.size());
        for (Item item : codesInCodelistTargetBeforeCopy) {
            codesInCodelistTargetBeforeCopyByCode.put(item.getNameableArtefact().getCode(), (CodeMetamac) item);
        }

        // SOURCE: Get codelist source
        CodelistVersionMetamac codelistVersionSource = retrieveCodelistByUrn(ctx, codelistSourceUrn);
        // SOURCE: Retrieve all codes of codelist source together (although all codes wont be copied, it is prefered obtain in x single queries than x * numCodesToCopy queries)
        List<Code> codesInCodelistSource = codelistVersionSource.getItems();
        Map<String, CodeMetamac> codesInCodelistSourceByUrn = new HashMap<String, CodeMetamac>(codesInCodelistSource.size());
        for (Item item : codesInCodelistSource) {
            codesInCodelistSourceByUrn.put(item.getNameableArtefact().getUrn(), (CodeMetamac) item);
        }
        // SOURCE: Retrieve efficiently some metadata: name, description, annotations...
        List<ItemResult> codesItemResultInCodelistSource = getCodeMetamacRepository().findCodesByCodelistUnordered(codelistVersionSource.getId(), ItemMetamacResultSelection.COPY);
        Map<String, ItemResult> codesItemResultInCodelistSourceByUrn = new HashMap<String, ItemResult>(codesItemResultInCodelistSource.size());
        for (ItemResult itemResult : codesItemResultInCodelistSource) {
            codesItemResultInCodelistSourceByUrn.put(itemResult.getUrn(), itemResult);
        }

        boolean copyVariableElements = false;
        if (codelistVersionTarget.getVariable() != null && codelistVersionSource.getVariable() != null
                && codelistVersionSource.getVariable().getId().equals(codelistVersionTarget.getVariable().getId())) {
            copyVariableElements = true;
        }

        // Copy
        List<CodeMetamac> codesToPersist = new ArrayList<CodeMetamac>(codesToCopy.size());
        Map<String, CodeMetamac> codesToPersistByCode = new HashMap<String, CodeMetamac>(codesToCopy.size());
        for (CodeToCopy codeToCopy : codesToCopy) {
            CodeMetamac codeCopied = prepareCopyCodeInCodelist(codesInCodelistSourceByUrn, codesItemResultInCodelistSourceByUrn, codelistVersionTarget, codesInCodelistTargetBeforeCopyByCode,
                    codesToPersistByCode, codeToCopy, copyVariableElements);
            codesToPersist.add(codeCopied);
            codesToPersistByCode.put(codeCopied.getNameableArtefact().getCode(), codeCopied);
        }

        // Re-calculate all visualisations with new codes
        List<Code> allItemsToUpdateVisualisations = new ArrayList<Code>(codesInCodelistTargetBeforeCopy);
        for (Code item : codesToPersist) {
            if (!codesInCodelistTargetBeforeCopyByCode.containsKey(item.getNameableArtefact().getCode())) {
                allItemsToUpdateVisualisations.add(item);
            }
        }
        recalculateCodesVisualisations(ctx, allItemsToUpdateVisualisations, codelistVersionTarget.getOrderVisualisations(), codelistVersionTarget.getOpennessVisualisations(), false);

        // Save codes
        saveCodesEfficiently(codesToPersist, codesToPersistByCode);
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersionTarget);
    }

    @Override
    public TaskImportationInfo importCodesTsv(ServiceContext ctx, String codelistUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkImportCodesTsv(codelistUrn, file, fileName, updateAlreadyExisting, canBeBackground, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        if (codelistVersion.getVariable() == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_REQUIRED).withMessageParameters(ServiceExceptionParameters.CODELIST_VARIABLE).build();
        }
        TsvImportationUtils.checkItemSchemeCanExecuteImportation(codelistVersion, codelistVersion.getLifeCycleMetadata(), canBeBackground);

        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_ITEMS_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            codelistVersion.getItemScheme().setIsTaskInBackground(Boolean.TRUE);
            itemSchemeRepository.save(codelistVersion.getItemScheme());

            String jobKey = tasksMetamacService.plannifyImportCodesTsvInBackground(ctx, codelistUrn, file, fileName, updateAlreadyExisting);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        // Execute importation now
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<CodeMetamac> codesToPersist = null;
        Map<String, CodeMetamac> codesToPersistByCode = null;
        Map<String, CodeMetamac> codesPreviousInCodelistByCode = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationCodesTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportCodes(line, exceptionItems);

            // Codes
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Retrieve actual codes in codelist
                codesToPersist = new ArrayList<CodeMetamac>(); // save in order is required (first parent and then children)
                codesToPersistByCode = new HashMap<String, CodeMetamac>();
                codesPreviousInCodelistByCode = new HashMap<String, CodeMetamac>();
                for (Item item : codelistVersion.getItems()) {
                    codesPreviousInCodelistByCode.put(item.getNameableArtefact().getCode(), (CodeMetamac) item);
                }
                // Retrieve variable elements. It is more efficient retrieve all variable element in one query than execute one query by variable element
                Map<String, VariableElement> variableElementsInVariableByCode = new HashMap<String, VariableElement>();
                for (VariableElement variableElement : codelistVersion.getVariable().getVariableElements()) {
                    variableElementsInVariableByCode.put(variableElement.getIdentifiableArtefact().getCode(), variableElement);
                }

                int lineNumber = 2;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                        continue;
                    }
                    // Transform code and add to list to persist
                    CodeMetamac code = TsvImportationUtils.tsvLineToCode(header, columns, lineNumber, codelistVersion, variableElementsInVariableByCode, updateAlreadyExisting,
                            codesPreviousInCodelistByCode, codesToPersistByCode, exceptionItems, informationItems);
                    if (code != null) {
                        codesToPersist.add(code);
                        codesToPersistByCode.put(code.getNameableArtefact().getCode(), code);
                    }
                    lineNumber++;
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            try {
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                // do not relaunch error
                logger.error("Error closing streams", e);
            }
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            // rollback and inform about errors
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }

        // Re-calculate all visualisations with new codes
        List<Code> allItemsToUpdateVisualisations = new ArrayList<Code>(codelistVersion.getItems());
        for (Code item : codesToPersist) {
            if (!codesPreviousInCodelistByCode.containsKey(item.getNameableArtefact().getCode())) {
                allItemsToUpdateVisualisations.add(item);
            }
        }
        recalculateCodesVisualisations(ctx, allItemsToUpdateVisualisations, codelistVersion.getOrderVisualisations(), codelistVersion.getOpennessVisualisations(), false);

        // Save codes and scheme
        saveCodesEfficiently(codesToPersist, codesToPersistByCode);
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        codelistVersion.getItemScheme().setIsTaskInBackground(Boolean.FALSE);
        itemSchemeRepository.save(codelistVersion.getItemScheme());

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    @Override
    public TaskImportationInfo importCodeOrdersTsv(ServiceContext ctx, String codelistUrn, File file, String fileName, Boolean canBeBackground) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkImportCodeOrdersTsv(codelistUrn, file, fileName, canBeBackground, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        TsvImportationUtils.checkItemSchemeCanExecuteImportation(codelistVersion, codelistVersion.getLifeCycleMetadata(), canBeBackground);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CODES_ORDERS_IMPORTATION);

        // Plannify task if can be in background
        if (executeInBackground) {
            codelistVersion.getItemScheme().setIsTaskInBackground(Boolean.TRUE);
            itemSchemeRepository.save(codelistVersion.getItemScheme());

            String jobKey = tasksMetamacService.plannifyImportCodeOrdersTsvInBackground(ctx, codelistUrn, file, fileName);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<Code> codesInCodelist = null;
        Set<String> codesInTsvToCheckAllCodesAreUpdated = null;
        List<CodelistOrderVisualisation> orderVisualisations = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationCodeOrdersTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportCodeOrders(line, exceptionItems);
            if (CollectionUtils.isEmpty(exceptionItems)) {
                orderVisualisations = tsvHeadToOrderVisualisations(codelistVersion, header, exceptionItems);
            }

            // Orders
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Codes in codelist
                codesInCodelist = codelistVersion.getItems();
                Map<String, CodeMetamac> codesInCodelistByCode = new HashMap<String, CodeMetamac>();
                for (Item item : codesInCodelist) {
                    codesInCodelistByCode.put(item.getNameableArtefact().getCode(), (CodeMetamac) item);
                }
                codesInTsvToCheckAllCodesAreUpdated = new HashSet<String>();

                int lineNumber = 2;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                        continue;
                    }
                    // Transform orders, setting value of columns
                    CodeMetamac code = tsvLineToCodeOrder(header, columns, lineNumber, codesInCodelistByCode, orderVisualisations, exceptionItems);
                    if (code != null) {
                        if (codesInTsvToCheckAllCodesAreUpdated.contains(code.getNameableArtefact().getCode())) {
                            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, code.getNameableArtefact().getCode(), lineNumber));
                        } else {
                            codesInTsvToCheckAllCodesAreUpdated.add(code.getNameableArtefact().getCode());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            try {
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                // do not relaunch error
                logger.error("Error closing streams", e);
            }
        }

        if (CollectionUtils.isEmpty(exceptionItems) && codesInTsvToCheckAllCodesAreUpdated.size() != codesInCodelist.size()) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_INCORRECT_NUMBER_CODES, codesInCodelist.size(), codesInTsvToCheckAllCodesAreUpdated.size()));
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            // inform about errors
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }

        // Re-calculate orders
        for (CodelistOrderVisualisation orderVisualisation : orderVisualisations) {
            sortCodesByOrder(codesInCodelist, orderVisualisation, false);
        }

        // Persist
        for (Item code : codesInCodelist) {
            CodeMetamac codeMetamac = (CodeMetamac) code;
            getCodeMetamacRepository().save(codeMetamac);
        }
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        codelistVersion.getItemScheme().setIsTaskInBackground(Boolean.FALSE);
        itemSchemeRepository.save(codelistVersion.getItemScheme());

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    @Override
    public String exportCodesTsv(ServiceContext ctx, String codelistUrn) throws MetamacException {

        CodesMetamacInvocationValidator.checkExportCodesTsv(codelistUrn, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);

        CodelistOrderVisualisation orderVisualisationToRetrieveCodes = codelistVersion.getDefaultOrderVisualisation();
        if (orderVisualisationToRetrieveCodes == null) {
            orderVisualisationToRetrieveCodes = codelistVersion.getOrderVisualisations().get(0); // any
        }
        Integer orderColumnIndex = orderVisualisationToRetrieveCodes.getColumnIndex();

        List<ItemResult> items = getCodeMetamacRepository().findCodesByCodelistOrderedInDepth(codelistVersion.getId(), orderColumnIndex, null, CodeMetamacResultSelection.EXPORT);

        List<String> languages = srmConfiguration.retrieveLanguages();

        return TsvExportationUtils.exportCodes(items, languages);
    }

    @Override
    public String exportCodeOrdersTsv(ServiceContext ctx, String codelistUrn) throws MetamacException {

        CodesMetamacInvocationValidator.checkExportCodesTsv(codelistUrn, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        List<CodelistOrderVisualisation> orderVisualisations = codelistVersion.getOrderVisualisations();
        String defaultLanguage = srmConfiguration.retrieveLanguageDefault();

        // Retrieve items in depth
        List<ItemResult> itemsResult = getCodeMetamacRepository().findCodesByCodelistOrderedInDepth(codelistVersion.getId(), null, null, CodeMetamacResultSelection.EXPORT_ORDERS);
        // Retrieve all items to retrieve all orders
        List<Code> codes = codelistVersion.getItems();
        Map<Long, CodeMetamac> codesById = new HashMap<Long, CodeMetamac>(codes.size());
        for (Item code : codes) {
            codesById.put(code.getId(), (CodeMetamac) code);
        }

        // Export
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("codes-orders", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            // header
            writer.write(SrmConstants.TSV_HEADER_CODE);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_LABEL);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_LEVEL);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_PARENT);
            for (CodelistOrderVisualisation orderVisualisation : orderVisualisations) {
                writer.write(SrmConstants.TSV_SEPARATOR);
                writer.write(orderVisualisation.getNameableArtefact().getCode());
            }

            // codes
            for (ItemResult itemResult : itemsResult) {
                CodeMetamac codeMetamac = codesById.get(itemResult.getItemIdDatabase());

                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                // Code
                writer.write(itemResult.getCode());
                // Label
                writer.write(SrmConstants.TSV_SEPARATOR);
                writer.write(itemResult.getName().get(defaultLanguage));
                // Level
                writer.write(SrmConstants.TSV_SEPARATOR);
                writer.write(String.valueOf(((CodeMetamacResultExtensionPoint) itemResult.getExtensionPoint()).getLevel()));
                // Parent
                writer.write(SrmConstants.TSV_SEPARATOR);
                if (itemResult.getParent() != null) {
                    writer.write(itemResult.getParent().getCode());
                }
                // Order
                for (CodelistOrderVisualisation orderVisualisation : orderVisualisations) {
                    writer.write(SrmConstants.TSV_SEPARATOR);
                    writer.write(String.valueOf(SrmServiceUtils.getCodeOrder(codeMetamac, orderVisualisation.getColumnIndex()) + 1)); // +1 to start in 1
                }
            }
            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
    }

    @Override
    public void recalculateCodesVisualisations(ServiceContext ctx, List<Code> items, List<CodelistOrderVisualisation> orderVisualisations, List<CodelistOpennessVisualisation> opennessVisualisations,
            boolean executeSave) {

        // Recalculate all order codes
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        for (CodelistOrderVisualisation orderVisualisation : orderVisualisations) {
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(orderVisualisation)) {
                sortCodesInAlphabeticalOrder(items, orderVisualisation, false);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                sortCodesByOrder(items, orderVisualisation, false);
            }
        }

        // Fill openness visualisations (only empty)
        Boolean defaultOpennessValue = SrmConstants.CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE;
        Boolean allExpandedAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        for (CodelistOpennessVisualisation opennessVisualisation : opennessVisualisations) {
            Boolean value = null;
            if (!allExpandedAlreadyUpdated && SrmServiceUtils.isAllExpandedOpennessVisualisation(opennessVisualisation)) {
                value = Boolean.TRUE;
                allExpandedAlreadyUpdated = Boolean.TRUE;
            } else {
                value = defaultOpennessValue;
            }
            fillCodelistOpennessVisualisationToCodesWithEmptyOpenness(ctx, items, opennessVisualisation, value);
        }
    }

    @Override
    public CodeMetamac preCreateCode(ServiceContext ctx, CodelistVersionMetamac codelistVersion, CodeMetamac code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCode(codelistVersion, code, null);
        checkCodeToCreateOrUpdate(ctx, codelistVersion, code);

        return code;
    }

    @Override
    public CodeMetamac updateCode(ServiceContext ctx, CodeMetamac code) throws MetamacException {
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // IMPORTANT: If any restriction change, review operations of importation of tsv file and copy of codes,
        // because the importation doesnot call to service methods to avoid extra queries, flushing...
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, code.getNameableArtefact().getUrn());

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCode(codelistVersion, code, null);
        checkCodeToCreateOrUpdate(ctx, codelistVersion, code);

        return (CodeMetamac) codesService.updateCode(ctx, code);
    }

    @Override
    public CodeMetamac updateCodeVariableElement(ServiceContext ctx, String codeUrn, String variableElementUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeVariableElement(codeUrn, variableElementUrn, null);
        // Note: It is not necessary to check the codelist status. Variable element can be modified although the codelist is published

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        if (variableElementUrn == null) {
            // Delete
            if (code.getVariableElement() != null) {
                code.getVariableElement().removeCode(code);
            }
        } else {
            // Add
            VariableElement variableElement = retrieveVariableElementByUrn(variableElementUrn);
            CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
            checkCodeVariableElement(codelistVersion, variableElement);

            if (code.getVariableElement() != null) {
                code.getVariableElement().removeCode(code);
            }
            variableElement.addCode(code);
            code.setShortName(null); // reset short name
        }
        baseService.updateItemSchemeLastUpdated(ctx, code.getItemSchemeVersion());
        return getCodeMetamacRepository().save(code);
    }

    @Override
    public void updateCodesVariableElements(ServiceContext ctx, String codelistUrn, Map<Long, Long> variableElementsIdByCodeId) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodesVariableElements(codelistUrn, variableElementsIdByCodeId, null);
        // Note: It is not necessary to check the codelist status. Variable element can be modified although the codelist is published

        // Retrieve codelist. All codes must belong to same codelist. In repository, error ocurrs if any code doesnot belong to this codelist
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        // Retrieve variable. All variable elements must belong to codelist varliable. In repository, error ocurrs if any variable element doesnot belong to this variable
        Variable variable = codelistVersion.getVariable();
        if (variable == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_REQUIRED).withMessageParameters(ServiceExceptionParameters.CODELIST_VARIABLE).build();
        }
        // Update all
        getCodeMetamacRepository().updateCodesVariableElements(variableElementsIdByCodeId, codelistVersion.getId(), variable.getId());
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
    }

    @Override
    public void updateCodeParent(ServiceContext ctx, String codeUrn, String parentTargetUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeParent(codeUrn, parentTargetUrn, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
        checkCodelistCanBeModified(codelistVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        Code parentActual = code.getParent() != null ? code.getParent() : null;
        CodeMetamac parentTarget = parentTargetUrn != null ? retrieveCodeByUrn(ctx, parentTargetUrn) : null;
        if (!SdmxSrmUtils.isItemParentChanged(parentActual, parentTarget)) {
            // nothing
            return;
        }

        // Check target parent is not children of this code
        if (parentTargetUrn != null) {
            checkItemIsNotChildren(ctx, code, parentTargetUrn);
        }

        // Update target parent
        if (parentTarget == null) {
            code.setParent(null);
            code.setItemSchemeVersionFirstLevel(codelistVersion);
        } else {
            code.setParent(parentTarget);
            code.setItemSchemeVersionFirstLevel(null);
        }

        // Update orders, in previous and new level
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        Integer orderInLastPosition = null; // no avoid innecessary extra queries
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            // remove from previous level
            reorderCodesDeletingOneCode(codelistVersion, codelistOrderVisualisation, parentActual, code);
            // add to new level
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                setCodeOrderInAlphabeticalPositionAndReorderCodesInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                orderInLastPosition = setCodeOrderInLastPositionInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code, orderInLastPosition);
            }
        }
        // Note: openness configuration remains inmutable
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        code = getCodeMetamacRepository().save(code);
    }

    @Override
    public void updateCodeInOrderVisualisation(ServiceContext ctx, String codeUrn, String codelistOrderVisualisationUrn, Integer newCodeIndex) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeInOrderVisualisation(codeUrn, codelistOrderVisualisationUrn, newCodeIndex, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
        checkCodelistCanBeModified(codelistVersion);

        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(codelistOrderVisualisationUrn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(codelistOrderVisualisation);

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        // Reorder codes in same level
        reorderCodesDeletingOneCode(codelistVersion, codelistOrderVisualisation, code.getParent(), code);
        reorderCodesAddingOneCodeInMiddle(codelistVersion, codelistOrderVisualisation, code, newCodeIndex);
        // Update code
        SrmServiceUtils.setCodeOrder(code, codelistOrderVisualisation.getColumnIndex(), newCodeIndex);
        getCodeMetamacRepository().save(code);
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
    }

    @Override
    public void updateCodesInOpennessVisualisation(ServiceContext ctx, String opennessVisualisation, Map<String, Boolean> openness) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeInOpennessVisualisation(opennessVisualisation, openness, null);
        CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByUrn(opennessVisualisation);
        CodelistVersionMetamac codelistVersion = codelistOpennessVisualisation.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);
        SrmValidationUtils.checkNotOpenedOpennessVisualisation(codelistOpennessVisualisation);
        // Note: In repository method 'updateCodeOpennessColumn' check code belongs to this codelist

        // Update codes
        Integer columnIndex = codelistOpennessVisualisation.getColumnIndex();
        for (String codeUrn : openness.keySet()) {
            getCodeMetamacRepository().updateCodeOpennessColumn(codelistVersion, codeUrn, columnIndex, openness.get(codeUrn));
        }
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
    }

    @Override
    public CodeMetamac retrieveCodeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        CodesInvocationValidator.checkRetrieveByUrn(urn);
        CodeMetamac code = getCodeMetamacRepository().findByUrn(urn);
        if (code == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return code;
    }

    @Override
    public PagedResult<CodeMetamac> findCodesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Code> codesPagedResult = codesService.findCodesByCondition(ctx, conditions, pagingParameter);
        return pagedResultCodeToMetamac(codesPagedResult);
    }

    @Override
    public PagedResult<CodeMetamac> findCodesCanBeVariableElementGeographicalGranularityByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).distinctRoot().build();
        }
        // codelist from DATA (codelist must be this one always)
        String codelistUrn = srmConfiguration.retrieveCodelistUrnForVariableElementGeographicalGranularity();
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(codelistUrn).buildSingle());
        // scheme externally published (variable element has not lifecycle)
        conditions.add(
                ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic()).eq(Boolean.TRUE).buildSingle());
        // Codelist with access type = PUBLIC
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());

        return findCodesByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public void deleteCode(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, urn);
        checkCodelistCanBeModified(codelistVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        CodeMetamac code = retrieveCodeByUrn(ctx, urn);
        Code parent = code.getParent();
        // Before delete code, update order of other codes in level
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            reorderCodesDeletingOneCode(codelistVersion, codelistOrderVisualisation, parent, code);
        }
        // Openness visualisations of other codes in level are not affected

        // Delete code
        codesService.deleteCode(ctx, urn);
    }

    @Override
    public List<CodeMetamacVisualisationResult> retrieveCodesByCodelistUrn(ServiceContext ctx, String codelistUrn, String locale, String orderVisualisationUrn, String opennessVisualisationUrn)
            throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodesByCodelistUrn(codelistUrn, locale, orderVisualisationUrn, opennessVisualisationUrn, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        Integer orderColumnIndex = null;
        if (orderVisualisationUrn == null) {
            orderVisualisationUrn = codelistVersion.getDefaultOrderVisualisation() != null ? codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn() : null;
        }
        if (orderVisualisationUrn != null) {
            CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(ctx, orderVisualisationUrn);
            orderColumnIndex = codelistOrderVisualisation.getColumnIndex();
        }

        Integer opennessColumnIndex = null;
        if (opennessVisualisationUrn != null) {
            CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByUrn(ctx, opennessVisualisationUrn);
            opennessColumnIndex = codelistOpennessVisualisation.getColumnIndex();
        } else {
            // nothing! do not return default openness visualisation
        }

        // Retrieve
        return getCodeMetamacRepository().findCodesByCodelistUnorderedToVisualisation(codelistVersion.getId(), locale, orderColumnIndex, opennessColumnIndex);
    }

    @Override
    public List<ItemResult> retrieveCodesByCodelistUrnOrderedInDepth(ServiceContext ctx, String codelistUrn, ItemMetamacResultSelection itemResultSelection, String orderVisualisationCode,
            String opennessVisualisationCode) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodesByCodelistUrnOrderedInDepth(codelistUrn, itemResultSelection, orderVisualisationCode, opennessVisualisationCode, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);

        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByCode(codelistVersion, orderVisualisationCode);
        Integer orderColumnIndex = codelistOrderVisualisation.getColumnIndex();

        CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByCode(codelistVersion, opennessVisualisationCode);
        Integer opennessColumnIndex = codelistOpennessVisualisation.getColumnIndex();

        // Retrieve
        return getCodeMetamacRepository().findCodesByCodelistOrderedInDepth(codelistVersion.getId(), orderColumnIndex, opennessColumnIndex, itemResultSelection);
    }

    @Override
    public List<CodeVariableElementNormalisationResult> normaliseVariableElementsToCodes(ServiceContext ctx, String codelistUrn, String locale, boolean proposeOnlyWithoutVariableElement)
            throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkNormaliseVariableElementsToCodes(codelistUrn, locale, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        String variableUrn = codelistVersion.getVariable().getNameableArtefact().getUrn();

        // Find codes and variable elements
        List<CodeMetamacVisualisationResult> codes = retrieveCodesByCodelistUrn(ctx, codelistUrn, locale, null, null);
        List<VariableElementVisualisationResult> variableElements = retrieveVariableElementsByVariable(ctx, variableUrn, locale);
        Map<String, VariableElementVisualisationResult> variableElementsByUrn = new HashMap<String, VariableElementVisualisationResult>(variableElements.size());
        for (VariableElementVisualisationResult variableElement : variableElements) {
            variableElementsByUrn.put(variableElement.getUrn(), variableElement);
        }

        // Init dictionary with variable elements
        Map<String, String> dictionary = new HashMap<String, String>(variableElements.size());
        for (VariableElementVisualisationResult variableElement : variableElements) {
            if (variableElement.getShortName() != null) {
                dictionary.put(variableElement.getUrn(), variableElement.getShortName());
            }
        }
        DiceLuceneRamAproxStringMatch luceneMatch = new DiceLuceneRamAproxStringMatch(dictionary);

        // Get suggestions or mantain actual variable element
        List<CodeVariableElementNormalisationResult> results = new ArrayList<CodeVariableElementNormalisationResult>(codes.size());
        for (CodeMetamacVisualisationResult code : codes) {
            CodeVariableElementNormalisationResult result = new CodeVariableElementNormalisationResult();
            result.setCode(code);
            if (proposeOnlyWithoutVariableElement && code.getVariableElement() != null) {
                // maintain actual
                result.setVariableElementProposed(code.getVariableElement());
            } else {
                // propose by lucene
                String name = code.getName();
                if (name != null) {
                    List<MatchResult> suggestedVariableElements = luceneMatch.getSuggestedWords(name, 1);
                    if (suggestedVariableElements.size() != 0) {
                        VariableElementVisualisationResult variableElement = variableElementsByUrn.get(suggestedVariableElements.get(0).getDictionaryKey());
                        result.setVariableElementProposed(variableElement);
                    }
                }
            }
            results.add(result);
        }

        luceneMatch.shutdown();
        return results;
    }

    @Override
    public void checkCodelistVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByResourceUrn) throws MetamacException {
        getCodelistVersionMetamacRepository().checkCodelistVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getCodelistOpennessVisualisationRepository().checkCodelistOpennessVisualisationTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getCodelistOrderVisualisationRepository().checkCodelistOrderVisualisationTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getCodeMetamacRepository().checkCodeTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
    }

    @Override
    public void checkCodelistWithRelatedResourcesExternallyPublished(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        Long itemSchemeVersionId = codelistVersion.getId();
        String itemSchemeVersionUrn = codelistVersion.getMaintainableArtefact().getUrn();
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        getCodelistVersionMetamacRepository().checkCodelistWithReplaceToExternallyPublished(itemSchemeVersionId, exceptionItemsByUrn);
        categoriesMetamacService.checkCategorisationsWithRelatedResourcesExternallyPublished(ctx, itemSchemeVersionUrn, exceptionItemsByUrn);
        ExceptionUtils.throwIfException(new ArrayList<MetamacExceptionItem>(exceptionItemsByUrn.values()));
    }

    // ------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistFamily createCodelistFamily(ServiceContext ctx, CodelistFamily codelistFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistFamily(codelistFamily, null);
        checkCodelistFamilyToCreateOrUpdate(codelistFamily);

        // Create
        setCodelistFamilyUrnUnique(codelistFamily);
        return getCodelistFamilyRepository().save(codelistFamily);
    }

    @Override
    public CodelistFamily updateCodelistFamily(ServiceContext ctx, CodelistFamily codelistFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistFamily(codelistFamily, null);
        checkCodelistFamilyToCreateOrUpdate(codelistFamily);

        // If code has been changed, update URN
        if (codelistFamily.getNameableArtefact().getIsCodeUpdated()) {
            // setCodelistFamilyUrnUnique(codelistFamily);
            // METAMAC-1644
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE).build();
        }
        codelistFamily.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getCodelistFamilyRepository().save(codelistFamily);
    }

    @Override
    public CodelistFamily retrieveCodelistFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistFamily codelistFamily = retrieveCodelistFamilyByUrn(urn);
        return codelistFamily;
    }

    @Override
    public PagedResult<CodelistFamily> findCodelistFamiliesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).distinctRoot().build();
        }
        PagedResult<CodelistFamily> codelistFamilyPagedResult = getCodelistFamilyRepository().findByCondition(conditions, pagingParameter);
        return codelistFamilyPagedResult;
    }

    @Override
    public void deleteCodelistFamily(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        // Note: METAMAC-1644. Codelist family can be deleted even if it is published in api

        // Delete
        CodelistFamily codelistFamilyToDelete = retrieveCodelistFamilyByUrn(urn);

        // Delete association with codelists
        // Note: It is not necessary to check the codelist status. Family can be modified although the codelist is published
        codelistFamilyToDelete.removeAllCodelists();
        getCodelistFamilyRepository().save(codelistFamilyToDelete);

        getCodelistFamilyRepository().delete(codelistFamilyToDelete);
    }

    @Override
    public void addCodelistsToCodelistFamily(ServiceContext ctx, List<String> codelistUrns, String codelistFamilyUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkAddCodelistsToCodelistFamily(codelistUrns, codelistFamilyUrn, null);
        // Note: It is not necessary to check the codelist status. Family can be modified although the codelist is published

        CodelistFamily codelistFamily = retrieveCodelistFamilyByUrn(codelistFamilyUrn);

        // Update codelists
        for (String codelistUrn : codelistUrns) {
            CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
            SrmValidationUtils.checkArtefactWithoutTaskInBackground(codelistVersion);

            if (codelistVersion.getFamily() != null) {
                codelistVersion.getFamily().removeCodelist(codelistVersion);
            }
            codelistFamily.addCodelist(codelistVersion);
            getCodelistVersionMetamacRepository().save(codelistVersion);
            baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        }
    }

    @Override
    public void removeCodelistFromCodelistFamily(ServiceContext ctx, String codelistUrn, String codelistFamilyUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRemoveCodelistFromCodelistFamily(codelistUrn, codelistFamilyUrn, null);
        // Note: It is not necessary to check the codelist status. Family can be modified although the codelist is published

        CodelistFamily codelistFamily = retrieveCodelistFamilyByUrn(codelistFamilyUrn);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);

        // Do not remove the codelist if it has not been associated with the family previously
        if (!SrmServiceUtils.isCodelistInList(codelistVersion.getMaintainableArtefact().getUrn(), codelistFamily.getCodelists())) {
            return;
        }

        // Remove codelist from family
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        codelistFamily.removeCodelist(codelistVersion);
        getCodelistFamilyRepository().save(codelistFamily);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    public VariableFamily createVariableFamily(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateVariableFamily(variableFamily, null);
        setVariableFamilyUrnUnique(variableFamily);
        checkVariableFamilyToCreateOrUpdate(variableFamily);

        // Create
        return getVariableFamilyRepository().save(variableFamily);
    }

    @Override
    public VariableFamily updateVariableFamily(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariableFamily(variableFamily, null);
        checkVariableFamilyToCreateOrUpdate(variableFamily);
        // If code has been changed, update URN
        if (variableFamily.getNameableArtefact().getIsCodeUpdated()) {
            // setVariableFamilyUrnUnique(variableFamily);
            // METAMAC-1644
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE).build();
        }
        variableFamily.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getVariableFamilyRepository().save(variableFamily);
    }

    @Override
    public VariableFamily retrieveVariableFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        VariableFamily variableFamily = retrieveVariableFamilyByUrn(urn);
        return variableFamily;
    }

    @Override
    public void deleteVariableFamily(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        VariableFamily variableFamilyToDelete = retrieveVariableFamilyByUrn(urn);
        checkVariableFamilyCanBeDeleted(variableFamilyToDelete);

        // Delete associations with variables
        variableFamilyToDelete.removeAllVariables();
        getVariableFamilyRepository().save(variableFamilyToDelete);

        // Delete
        getVariableFamilyRepository().delete(variableFamilyToDelete);
    }

    @Override
    public PagedResult<VariableFamily> findVariableFamiliesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).distinctRoot().build();
        }
        PagedResult<VariableFamily> variableFamilyPagedResult = getVariableFamilyRepository().findByCondition(conditions, pagingParameter);
        return variableFamilyPagedResult;
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    @Override
    public Variable createVariable(ServiceContext ctx, Variable variable) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateVariable(variable, null);
        checkVariableToCreateOrUpdate(ctx, variable);

        // In creation, 'replaceTo' metadata must be copied to set after save variable, due to flushing
        List<Variable> replaceTo = new ArrayList<Variable>(variable.getReplaceToVariables());
        variable.removeAllReplaceToVariables();

        // Create
        setVariableUrnUnique(variable);
        variable = getVariableRepository().save(variable);

        // Fill replaceTo metadata after save entity
        for (Variable variableReplaceTo : replaceTo) {
            variable.addReplaceToVariable(variableReplaceTo);
        }
        variable = getVariableRepository().save(variable);
        return variable;
    }

    @Override
    public Variable updateVariable(ServiceContext ctx, Variable variable) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariable(variable, null);
        checkVariableToCreateOrUpdate(ctx, variable);

        // Code modifications unsupported (METAMAC-1644). If this change, avoid modify code to 'SrmConfigurationConstants.VARIABLE_WORLD'
        if (variable.getNameableArtefact().getIsCodeUpdated()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE).build();
            // If code has been changed, update URN
            // setVariableUrnUnique(variable);
            // for (VariableElement variableElement : variable.getVariableElements()) {
            // setVariableElementUrnUnique(variable, variableElement, true);
            // }
        }
        variable.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Fill replaceBy metadata
        for (Variable variableReplaceTo : variable.getReplaceToVariables()) {
            variableReplaceTo.setReplacedByVariable(variable);
        }

        // Clear geographical information if type is changed to non geographical
        if (variable.getType() == null && VariableTypeEnum.GEOGRAPHICAL.equals(variable.getPreviousType())) {
            Long variableElementsCount = getVariableElementRepository().countVariableElementsByVariable(variable.getId());
            if (variableElementsCount != 0) {
                getVariableElementRepository().clearGeographicalInformationByVariable(variable.getId());
            }
        }

        // Update
        return getVariableRepository().save(variable);
    }

    @Override
    public Variable retrieveVariableByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        Variable variable = retrieveVariableByUrn(urn);
        // Count variable elements
        Long variableElementsCount = getVariableElementRepository().countVariableElementsByVariable(variable.getId());
        variable.setHasVariableElements(variableElementsCount != 0);
        return variable;
    }

    @Override
    public PagedResult<Variable> findVariablesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).distinctRoot().build();
        }
        PagedResult<Variable> variablePagedResult = getVariableRepository().findByCondition(conditions, pagingParameter);
        return variablePagedResult;
    }

    @Override
    public void deleteVariable(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        Variable variableToDelete = retrieveVariableByUrn(urn);
        checkVariableCanBeDeleted(variableToDelete);

        // Delete associations with variable families
        variableToDelete.removeAllFamilies();
        getVariableRepository().save(variableToDelete);

        // Delete
        variableToDelete.removeAllReplaceToVariables(); // variable can be deleted although it replaces to another variable
        getVariableRepository().delete(variableToDelete);
    }

    @Override
    public void addVariablesToVariableFamily(ServiceContext ctx, List<String> variablesUrn, String familyUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkAddVariablesToFamily(variablesUrn, familyUrn, null);

        VariableFamily family = retrieveVariableFamilyByUrn(familyUrn);
        for (String variableUrn : variablesUrn) {
            Variable variable = retrieveVariableByUrn(variableUrn);

            // Do not add the variable if it has been associated with the family previously
            if (SrmServiceUtils.isVariableInList(variable.getNameableArtefact().getUrn(), family.getVariables())) {
                continue;
            }
            // Add variable to family
            family.addVariable(variable);
        }
        getVariableFamilyRepository().save(family);
    }

    @Override
    public void removeVariableFromVariableFamily(ServiceContext ctx, String variableUrn, String familyUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRemoveVariableFromFamily(variableUrn, familyUrn, null);

        VariableFamily family = retrieveVariableFamilyByUrn(familyUrn);
        Variable variable = retrieveVariableByUrn(variableUrn);

        // Do not remove the variable if it has not been associated with the family previously
        if (!SrmServiceUtils.isVariableInList(variable.getNameableArtefact().getUrn(), family.getVariables())) {
            return;
        }

        // Check variable has more families
        if (variable.getFamilies().size() == 1) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ONLY_IN_ONE_FAMILY).withMessageParameters(variableUrn, familyUrn).build();
        }

        // Remove variable from family
        family.removeVariable(variable);
        getVariableFamilyRepository().save(family);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------------------

    @Override
    public VariableElement createVariableElement(ServiceContext ctx, VariableElement variableElement) throws MetamacException {
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // IMPORTANT: If any restriction change, review importation of tsv file, because the importation doesnot call to service methods to avoid extra queries, flushing...
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Validation
        CodesMetamacInvocationValidator.checkCreateVariableElement(variableElement, null);
        checkVariableElementToCreateOrUpdate(ctx, variableElement);

        // In creation, 'replaceTo' metadata must be copied to set after save variable, due to flushing
        List<VariableElement> replaceTo = new ArrayList<VariableElement>(variableElement.getReplaceToVariableElements());
        variableElement.removeAllReplaceToVariableElements();

        // Create
        setVariableElementUrnUnique(variableElement.getVariable(), variableElement, Boolean.TRUE);
        variableElement = getVariableElementRepository().save(variableElement);

        // Fill replaceTo metadata after save entity
        for (VariableElement variableElementReplaceTo : replaceTo) {
            variableElement.addReplaceToVariableElement(variableElementReplaceTo);
        }
        variableElement = getVariableElementRepository().save(variableElement);
        return variableElement;
    }

    @Override
    public VariableElement updateVariableElement(ServiceContext ctx, VariableElement variableElement) throws MetamacException {
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // IMPORTANT: If any restriction change, review importation of tsv file, because the importation doesnot call to service methods to avoid extra queries, flushing...
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariableElement(variableElement, null);
        checkVariableElementToCreateOrUpdate(ctx, variableElement);

        // Code modifications unsupported (METAMAC-1644). If this change, avoid modify code to 'SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD'
        if (variableElement.getIdentifiableArtefact().getIsCodeUpdated()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE).build();
            // If code has been changed, update URN
            // setVariableElementUrnUnique(variableElement.getVariable(), variableElement, Boolean.TRUE);
        }
        variableElement.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Fill replaceBy metadata
        for (VariableElement replaceTo : variableElement.getReplaceToVariableElements()) {
            replaceTo.setReplacedByVariableElement(variableElement);
        }

        // Update
        return getVariableElementRepository().save(variableElement);
    }

    @Override
    public VariableElement retrieveVariableElementByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        VariableElement variableElement = retrieveVariableElementByUrn(urn);
        return variableElement;
    }

    @Override
    public PagedResult<VariableElement> findVariableElementsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).distinctRoot().build();
        }
        PagedResult<VariableElement> variableElementPagedResult = getVariableElementRepository().findByCondition(conditions, pagingParameter);
        return variableElementPagedResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PagedResult<VariableElement> findVariableElementsForCodesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String codelistUrn)
            throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindVariableElementsForCodesByCondition(conditions, pagingParameter, codelistUrn, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        if (codelistVersion.getVariable() == null) {
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).distinctRoot().build();
        }
        // Same variable
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).withProperty(VariableElementProperties.variable().nameableArtefact().urn())
                .eq(codelistVersion.getVariable().getNameableArtefact().getUrn()).buildSingle());
        PagedResult<VariableElement> variableElementPagedResult = getVariableElementRepository().findByCondition(conditions, pagingParameter);
        return variableElementPagedResult;
    }

    @Override
    public List<VariableElementVisualisationResult> retrieveVariableElementsByVariable(ServiceContext ctx, String variableUrn, String locale) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveVariableElementsByVariable(variableUrn, locale, null);

        // Find
        Variable variable = retrieveVariableByUrn(variableUrn);
        return getVariableElementRepository().retrieveVariableElementsByVariableEfficientlyToVisualisation(variable.getId(), locale);
    }

    @Override
    public List<VariableElementResult> findVariableElementsByVariableEfficiently(ServiceContext ctx, String variableUrn, List<String> variableElementCodes, VariableElementResultSelection selection,
            List<String> geographicalGranularityUrns) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindVariableElementsByVariableEfficiently(variableUrn, variableElementCodes, selection, null);

        // Find
        Variable variable = retrieveVariableByUrn(variableUrn);
        return getVariableElementRepository().findVariableElementsByVariableEfficiently(variable.getId(), variableElementCodes, selection, geographicalGranularityUrns);
    }

    @Override
    public void deleteVariableElement(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        VariableElement variableElementToDelete = retrieveVariableElementByUrn(urn);
        checkVariableElementCanBeDeleted(variableElementToDelete);

        // Delete
        variableElementToDelete.removeAllReplaceToVariableElements(); // variable element can be deleted although it replaces to another variable element
        getVariableElementRepository().delete(variableElementToDelete);
    }

    @Override
    public void addVariableElementsToVariable(ServiceContext ctx, List<String> variableElementsUrn, String variableUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkAddVariableElementsToVariable(variableElementsUrn, variableUrn, null);

        Variable variable = retrieveVariableByUrn(variableUrn);
        checkVariableIsNotSpecialVariableWorld(variable);

        for (String variableElementUrn : variableElementsUrn) {
            VariableElement variableElement = retrieveVariableElementByUrn(variableElementUrn);

            // Do not add the variableElement if it has been associated with the variable previously
            if (SrmServiceUtils.isVariableElementInList(variableElement.getIdentifiableArtefact().getUrn(), variable.getVariableElements())) {
                continue;
            }
            // Add variableElement to variable
            variable.addVariableElement(variableElement);
        }
        getVariableRepository().save(variable);
    }

    @Override
    public VariableElementOperation createVariableElementFusionOperation(ServiceContext ctx, List<String> sources, String target) throws MetamacException {
        return createVariableElementOperationCommon(VariableElementOperationTypeEnum.FUSION, sources, Arrays.asList(target));
    }

    @Override
    public VariableElementOperation createVariableElementSegregationOperation(ServiceContext ctx, String source, List<String> targets) throws MetamacException {
        return createVariableElementOperationCommon(VariableElementOperationTypeEnum.SEGREGATION, Arrays.asList(source), targets);
    }

    @Override
    public VariableElementOperation retrieveVariableElementOperationByCode(ServiceContext ctx, String code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveVariableElementOperationByCode(code, null);

        // Retrieve
        VariableElementOperation variableElementOperation = retrieveVariableElementOperationByCode(code);
        return variableElementOperation;
    }

    @Override
    public void deleteVariableElementOperation(ServiceContext ctx, String code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteVariableElementOperation(code, null);

        // Delete
        VariableElementOperation variableElementOperationToDelete = retrieveVariableElementOperationByCode(code);
        getVariableElementOperationRepository().delete(variableElementOperationToDelete);
    }

    @Override
    public List<VariableElementOperation> retrieveVariableElementsOperationsByVariable(ServiceContext ctx, String variableUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(variableUrn);

        // Retrieve
        Variable variable = retrieveVariableByUrn(variableUrn);
        return variable.getVariableElementsOperations();
    }

    @Override
    public List<VariableElementOperation> retrieveVariableElementsOperationsByVariableElement(ServiceContext ctx, String variableElementUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(variableElementUrn);

        // Retrieve
        return getVariableElementOperationRepository().findByVariableElementUrn(variableElementUrn);
    }

    @Override
    public TaskImportationInfo importVariableElementsTsv(ServiceContext ctx, String variableUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground)
            throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkImportVariableElementsTsv(variableUrn, file, fileName, updateAlreadyExisting, canBeBackground, null);
        Variable variable = retrieveVariableByUrn(variableUrn);
        checkVariableIsNotSpecialVariableWorld(variable);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_VARIABLE_ELEMENTS_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            String jobKey = tasksMetamacService.plannifyImportVariableElementsTsvInBackground(ctx, variableUrn, file, fileName, updateAlreadyExisting);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        // Import
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        Map<String, VariableElement> variableElementsToPersistByCode = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationVariableElementsTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportVariableElements(line, exceptionItems);
            if (CollectionUtils.isEmpty(exceptionItems)) {
                if (VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType()) && !header.isGeographicalGranularitySetted()) {
                    exceptionItems
                            .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_COLUMN_REQUIRED, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_GEOGRAPHICAL_GRANULARITY));
                }
            }

            // Variable elements
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Retrieve previous variable elements and prepare map with variable elements must be updated
                Map<String, VariableElement> variableElementsPreviousInVariableByCode = new HashMap<String, VariableElement>();
                for (VariableElement variableElement : variable.getVariableElements()) {
                    variableElementsPreviousInVariableByCode.put(variableElement.getIdentifiableArtefact().getCode(), variableElement);
                }
                variableElementsToPersistByCode = new HashMap<String, VariableElement>();

                // If geographical, retrieve all codes can be geographical granularity. To improve efficiency, In one query instead one query for line
                Map<String, CodeMetamac> codesCanBeGeographicalGranularityByCode = null;
                if (VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType())) {
                    CodelistVersionMetamac codelistWhoseCodesCanBeGeographicalGranularity = retrieveAndCheckCodelistWhoseCodesCanBeAsGeographicalGranularity(ctx);
                    List<Code> codesCanBeGeographicalGranularity = codelistWhoseCodesCanBeGeographicalGranularity.getItems();
                    codesCanBeGeographicalGranularityByCode = new HashMap<String, CodeMetamac>(codesCanBeGeographicalGranularity.size());
                    for (Code code : codesCanBeGeographicalGranularity) {
                        codesCanBeGeographicalGranularityByCode.put(code.getNameableArtefact().getCode(), (CodeMetamac) code);
                    }
                }

                int lineNumber = 2;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                        continue;
                    }
                    // Transform variable element and add to list to persist
                    VariableElement variableElement = tsvLineToVariableElement(header, columns, lineNumber, variable, updateAlreadyExisting, variableElementsPreviousInVariableByCode,
                            variableElementsToPersistByCode, codesCanBeGeographicalGranularityByCode, exceptionItems, informationItems);
                    if (variableElement != null) {
                        variableElementsToPersistByCode.put(variableElement.getIdentifiableArtefact().getCode(), variableElement);
                    }
                    lineNumber++;
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            try {
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                // do not relaunch error
                logger.error("Error closing streams", e);
            }
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            // rollback and inform about errors
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }
        // Persist
        for (VariableElement variableElement : variableElementsToPersistByCode.values()) {
            getVariableElementRepository().save(variableElement);
        }

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    @Override
    public TaskImportationInfo importVariableElementsShape(ServiceContext ctx, String variableUrn, URL shapeFileUrl, Boolean canBeBackground) throws MetamacException {
        return importVariableElementsGeographicalInformation(ctx, variableUrn, shapeFileUrl, canBeBackground, SrmConstants.SHAPE_OPERATION_IMPORT_SHAPES);
    }

    @Override
    public TaskImportationInfo importVariableElementsPoints(ServiceContext ctx, String variableUrn, URL shapeFileUrl, Boolean canBeBackground) throws MetamacException {
        return importVariableElementsGeographicalInformation(ctx, variableUrn, shapeFileUrl, canBeBackground, SrmConstants.SHAPE_OPERATION_IMPORT_POINTS);
    }

    // ------------------------------------------------------------------------------------
    // CODELIST ORDER VISUALISATIONS
    // ------------------------------------------------------------------------------------
    @Override
    public CodelistOrderVisualisation createCodelistOrderVisualisation(ServiceContext ctx, String codelistUrn, CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistOrderVisualisation(codelistUrn, codelistOrderVisualisation, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        checkCodelistCanBeModified(codelistVersion);

        // Create order visualisation
        codelistOrderVisualisation.setColumnIndex(getCodelistOrderVisualisationAvailable(codelistVersion));
        codelistOrderVisualisation.setCodelistVersion(codelistVersion);

        // Assign default order to all codes, by level (copy from alphabetic)
        getCodeMetamacRepository().copyCodesOrderColumn(codelistVersion, SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX, codelistOrderVisualisation.getColumnIndex());

        // Create
        setCodelistOrderVisualisationUrnUnique(codelistVersion, codelistOrderVisualisation, true);
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        return getCodelistOrderVisualisationRepository().save(codelistOrderVisualisation);
    }

    @Override
    public CodelistOrderVisualisation retrieveCodelistOrderVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(urn);
        return codelistOrderVisualisation;
    }

    @Override
    public CodelistOrderVisualisation retrieveCodelistOrderVisualisationByCode(ServiceContext ctx, String codelistUrn, String code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodelistOrderVisualisationByCode(codelistUrn, code, null);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByCode(codelistVersion, code);
        return codelistOrderVisualisation;
    }

    @Override
    public CodelistOrderVisualisation updateCodelistOrderVisualisation(ServiceContext ctx, CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistOrderVisualisation(codelistOrderVisualisation, null);
        CodelistVersionMetamac codelistVersion = codelistOrderVisualisation.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);

        // If code has been changed, update URN
        if (codelistOrderVisualisation.getNameableArtefact().getIsCodeUpdated()) {
            if (codelistOrderVisualisation.getNameableArtefact().getUrn().endsWith("." + SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE);
            }
            setCodelistOrderVisualisationUrnUnique(codelistOrderVisualisation.getCodelistVersion(), codelistOrderVisualisation, true);
        }
        codelistOrderVisualisation.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        return getCodelistOrderVisualisationRepository().save(codelistOrderVisualisation);
    }

    @Override
    public void deleteCodelistOrderVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        CodelistOrderVisualisation codelistOrderVisualisationToDelete = retrieveCodelistOrderVisualisationByUrn(urn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(codelistOrderVisualisationToDelete);

        // Check codelist
        CodelistVersionMetamac codelistVersion = codelistOrderVisualisationToDelete.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);

        // Check visualisation is not in original version
        if (BooleanUtils.isTrue(codelistVersion.getMaintainableArtefact().getIsTemporal())) {
            CodelistVersionMetamac codelistOriginalVersion = retrieveCodelistOriginalFromUrnTemporal(ctx, codelistVersion.getMaintainableArtefact().getUrn());
            CodelistOrderVisualisation codelistOrderVisualisationOriginal = getCodelistOrderVisualisationRepository().findByCode(codelistOriginalVersion.getId(),
                    codelistOrderVisualisationToDelete.getNameableArtefact().getCode());
            if (codelistOrderVisualisationOriginal != null) {
                throw new MetamacException(ServiceExceptionType.CODELIST_ORDER_VISUALISATION_DELETING_NOT_SUPPORTED_CODELIST_WAS_EVER_PUBLISHED, urn);
            }
        }

        // Clear if it is default visualisation
        if (codelistVersion.getDefaultOrderVisualisation() != null && codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn().equals(urn)) {
            codelistVersion.setDefaultOrderVisualisation(null);
            getCodelistVersionMetamacRepository().save(codelistVersion);
        }

        // Delete
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        getCodelistOrderVisualisationRepository().delete(codelistOrderVisualisationToDelete);
        // Clear orders in code columns
        getCodeMetamacRepository().clearCodesOrderColumn(codelistVersion, codelistOrderVisualisationToDelete.getColumnIndex());
    }

    @Override
    public List<CodelistOrderVisualisation> retrieveCodelistOrderVisualisationsByCodelist(ServiceContext ctx, String codelistUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(codelistUrn);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        return codelistVersion.getOrderVisualisations();
    }

    // ------------------------------------------------------------------------------------
    // CODELIST OPENNESS VISUALISATIONS
    // ------------------------------------------------------------------------------------
    @Override
    public CodelistOpennessVisualisation createCodelistOpennessVisualisation(ServiceContext ctx, String codelistUrn, CodelistOpennessVisualisation codelistOpennessVisualisation)
            throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistOpennessVisualisation(codelistUrn, codelistOpennessVisualisation, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        checkCodelistCanBeModified(codelistVersion);

        // Create openness visualisation
        codelistOpennessVisualisation.setColumnIndex(getCodelistOpennessVisualisationAvailable(codelistVersion));
        codelistOpennessVisualisation.setCodelistVersion(codelistVersion);

        // Assign default openness to all codes
        getCodeMetamacRepository().updateCodesOpennessColumnByCodelist(codelistVersion, codelistOpennessVisualisation.getColumnIndex(), SrmConstants.CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE);

        // Create
        setCodelistOpennessVisualisationUrnUnique(codelistVersion, codelistOpennessVisualisation, true);
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        return getCodelistOpennessVisualisationRepository().save(codelistOpennessVisualisation);
    }

    @Override
    public CodelistOpennessVisualisation retrieveCodelistOpennessVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByUrn(urn);
        return codelistOpennessVisualisation;
    }

    @Override
    public CodelistOpennessVisualisation retrieveCodelistOpennessVisualisationByCode(ServiceContext ctx, String codelistUrn, String code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodelistOpennessVisualisationByCode(codelistUrn, code, null);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByCode(codelistVersion, code);
        return codelistOpennessVisualisation;
    }

    @Override
    public CodelistOpennessVisualisation updateCodelistOpennessVisualisation(ServiceContext ctx, CodelistOpennessVisualisation codelistOpennessVisualisation) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistOpennessVisualisation(codelistOpennessVisualisation, null);
        CodelistVersionMetamac codelistVersion = codelistOpennessVisualisation.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);

        // If code has been changed, update URN
        if (codelistOpennessVisualisation.getNameableArtefact().getIsCodeUpdated()) {
            if (codelistOpennessVisualisation.getNameableArtefact().getUrn().endsWith("." + SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE);
            }
            setCodelistOpennessVisualisationUrnUnique(codelistOpennessVisualisation.getCodelistVersion(), codelistOpennessVisualisation, true);
        }
        codelistOpennessVisualisation.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        return getCodelistOpennessVisualisationRepository().save(codelistOpennessVisualisation);
    }

    @Override
    public void deleteCodelistOpennessVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        CodelistOpennessVisualisation codelistOpennessVisualisationToDelete = retrieveCodelistOpennessVisualisationByUrn(urn);
        SrmValidationUtils.checkNotOpenedOpennessVisualisation(codelistOpennessVisualisationToDelete);

        // Check codelist
        CodelistVersionMetamac codelistVersion = codelistOpennessVisualisationToDelete.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);

        // Check visualisation is not in original version
        if (BooleanUtils.isTrue(codelistVersion.getMaintainableArtefact().getIsTemporal())) {
            CodelistVersionMetamac codelistOriginalVersion = retrieveCodelistOriginalFromUrnTemporal(ctx, codelistVersion.getMaintainableArtefact().getUrn());
            CodelistOpennessVisualisation codelistOpennessVisualisationOriginal = getCodelistOpennessVisualisationRepository().findByCode(codelistOriginalVersion.getId(),
                    codelistOpennessVisualisationToDelete.getNameableArtefact().getCode());
            if (codelistOpennessVisualisationOriginal != null) {
                throw new MetamacException(ServiceExceptionType.CODELIST_OPENNESS_VISUALISATION_DELETING_NOT_SUPPORTED_CODELIST_WAS_EVER_PUBLISHED, urn);
            }
        }

        // Clear if it is default visualisation
        if (codelistVersion.getDefaultOpennessVisualisation() != null && codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn().equals(urn)) {
            codelistVersion.setDefaultOpennessVisualisation(null);
            getCodelistVersionMetamacRepository().save(codelistVersion);
        }

        // Delete
        baseService.updateItemSchemeLastUpdated(ctx, codelistVersion);
        getCodelistOpennessVisualisationRepository().delete(codelistOpennessVisualisationToDelete);
        // Clear configurations in code columns
        getCodeMetamacRepository().clearCodesOpennessColumnByCodelist(codelistVersion, codelistOpennessVisualisationToDelete.getColumnIndex());
    }

    @Override
    public List<CodelistOpennessVisualisation> retrieveCodelistOpennessVisualisationsByCodelist(ServiceContext ctx, String codelistUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(codelistUrn);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        return codelistVersion.getOpennessVisualisations();
    }

    @Override
    public String exportVariableElementsTsv(ServiceContext ctx, String variableUrn) throws MetamacException {

        CodesMetamacInvocationValidator.checkExportVariableElementsTsv(variableUrn, null);
        Variable variable = retrieveVariableByUrn(variableUrn);

        // Find
        VariableElementResultSelection selection = new VariableElementResultSelection();
        selection.setGeographicalGranularity(true);
        List<VariableElementResult> variableElements = getVariableElementRepository().findVariableElementsByVariableEfficiently(variable.getId(), null, selection, null);

        List<String> languages = srmConfiguration.retrieveLanguages();
        return TsvExportationUtils.exportVariableElements(variableElements, languages);
    }

    // ------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------------------

    /**
     * Typecast to Metamac type
     */
    private List<CodelistVersionMetamac> codelistVersionsToCodelistVersionsMetamac(List<CodelistVersion> sources) {
        List<CodelistVersionMetamac> targets = new ArrayList<CodelistVersionMetamac>(sources.size());
        for (ItemSchemeVersion source : sources) {
            targets.add((CodelistVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CodelistVersionMetamac> pagedResultCodelistVersionToMetamac(PagedResult<CodelistVersion> source) {
        List<CodelistVersionMetamac> codelistVersionsMetamac = codelistVersionsToCodelistVersionsMetamac(source.getValues());
        return new PagedResult<CodelistVersionMetamac>(codelistVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Common validations to create or update a codelist
     */
    private void checkCodelistToCreateOrUpdate(ServiceContext ctx, CodelistVersionMetamac codelist) throws MetamacException {

        if (codelist.getId() != null) {
            // Proc status
            checkCodelistCanBeModified(codelist);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(codelist.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, codelist.getMaintainableArtefact(), codelist.getMaintainableArtefact().getIsImported());

        // Visualisations (check they belong to this codelist)
        if (codelist.getDefaultOrderVisualisation() != null
                && !codelist.getDefaultOrderVisualisation().getCodelistVersion().getMaintainableArtefact().getUrn().equals(codelist.getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION)
                    .build();
        }
        if (codelist.getDefaultOpennessVisualisation() != null
                && !codelist.getDefaultOpennessVisualisation().getCodelistVersion().getMaintainableArtefact().getUrn().equals(codelist.getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                    .withMessageParameters(ServiceExceptionParameters.CODELIST_DEFAULT_OPENNESS_VISUALISATION).build();
        }

        // Check replaceTo metadata
        for (CodelistVersionMetamac replaceTo : codelist.getReplaceToCodelists()) {
            PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
            Long codelistReplaceToId = replaceTo.getId();
            List<ConditionalCriteria> criteriaToVerifyCodelistReplaceTo = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.id())
                    .eq(codelistReplaceToId).build();
            PagedResult<CodelistVersionMetamac> result = findCodelistsCanReplaceToByCondition(ctx, codelist.getMaintainableArtefact().getUrn(), true, criteriaToVerifyCodelistReplaceTo,
                    pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistReplaceToId)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CODELIST_REPLACE_TO).build();
            }
        }
    }

    private PagedResult<CodelistVersionMetamac> findCodelistsCanReplaceToByCondition(ServiceContext ctx, String codelistUrn, boolean retrieveCodelistsReplacedBySelectedCodelist,
            List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkFindCodelistsByConditionCanReplaceTo(codelistUrn, retrieveCodelistsReplacedBySelectedCodelist, conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build();
        }
        CodelistVersionMetamac codelistVersion = null;
        if (codelistUrn != null) {
            codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        }
        if (codelistVersion != null) {
            // Check it belong to another scheme
            ConditionalCriteria noReplaceSelfSchemeCondition = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).not()
                    .withProperty(CodelistVersionMetamacProperties.itemScheme().id()).eq(codelistVersion.getItemScheme().getId()).buildSingle();
            conditions.add(noReplaceSelfSchemeCondition);
        }
        // scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                .withProperty(CodelistVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);
        // Codelist not already replaced by another codelist. If requested, already include codelists are actually replaced by selected codelist
        if (codelistVersion == null || !retrieveCodelistsReplacedBySelectedCodelist) {
            ConditionalCriteria onlyNotReplaced = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.replacedByCodelist()).isNull()
                    .buildSingle();
            conditions.add(onlyNotReplaced);
        } else {
            ConditionalCriteria notAndAlreadyReplaced = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).lbrace()
                    .withProperty(CodelistVersionMetamacProperties.replacedByCodelist()).isNull().or().withProperty(CodelistVersionMetamacProperties.replacedByCodelist().id())
                    .eq(codelistVersion.getId()).rbrace().buildSingle();
            conditions.add(notAndAlreadyReplaced);
        }

        return findCodelistsByCondition(ctx, conditions, pagingParameter);
    }

    private TaskInfo createVersionOfCodelist(ServiceContext ctx, String urnToCopy, ItemSchemesCopyCallback itemSchemesCopyCallback, NoticesCallback noticesCallback, VersionTypeEnum versionType,
            boolean isTemporal) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkVersioningCodelist(urnToCopy, versionType, isTemporal, null, noticesCallback, null);
        checkCodelistToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        return codesService.versioningCodelist(ctx, urnToCopy, versionType, isTemporal, Boolean.TRUE, itemSchemesCopyCallback, noticesCallback);
    }

    private void checkCodelistToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {
        CodelistVersionMetamac codelistVersionToCopy = retrieveCodelistByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(codelistVersionToCopy.getMaintainableArtefact(), codelistVersionToCopy.getLifeCycleMetadata(), isTemporal);
        // Check does not exist any version 'no final'
        ItemSchemeVersion codelistVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(codelistVersionToCopy.getItemScheme().getId());
        if (codelistVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
                    .withMessageParameters(codelistVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    /**
     * Common validations to create or update a code
     */
    private void checkCodeToCreateOrUpdate(ServiceContext ctx, CodelistVersionMetamac codelistVersion, CodeMetamac code) throws MetamacException {
        checkCodelistCanBeModified(codelistVersion);
        checkCodeVariableElement(codelistVersion, code.getVariableElement());
    }

    /**
     * Common validations to create or update a variable family
     */
    private void checkVariableFamilyToCreateOrUpdate(VariableFamily variableFamily) throws MetamacException {
        // Check translations
        String locale = srmConfiguration.retrieveLanguageDefault();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        SrmServiceUtils.checkNameableArtefactTranslationsWithoutSql(variableFamily.getNameableArtefact(), locale, exceptionItems);
        if (exceptionItems.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
    }

    private void checkVariableFamilyCanBeDeleted(VariableFamily variableFamilyToDelete) throws MetamacException {
        // Note: METAMAC-1644. Variable family can be deleted even if it is published in api
        // Check variables of family to delete has more families, because family of variable is required (in exception, say only one variable)
        for (Variable variable : variableFamilyToDelete.getVariables()) {
            if (variable.getFamilies().size() == 1) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ONLY_IN_ONE_FAMILY)
                        .withMessageParameters(variable.getNameableArtefact().getUrn(), variableFamilyToDelete.getNameableArtefact().getUrn()).build();
            }
        }
    }

    /**
     * Common validations to create or update a codelist family
     */
    private void checkCodelistFamilyToCreateOrUpdate(CodelistFamily codelistFamily) throws MetamacException {
        // Check translations
        String locale = srmConfiguration.retrieveLanguageDefault();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        SrmServiceUtils.checkNameableArtefactTranslationsWithoutSql(codelistFamily.getNameableArtefact(), locale, exceptionItems);
        if (exceptionItems.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
    }

    private void checkCodeVariableElement(CodelistVersionMetamac codelistVersion, VariableElement variableElement) throws MetamacException {
        // Checks variable element belongs to same variable of codelist
        if (variableElement != null && !codelistVersion.getVariable().getId().equals(variableElement.getVariable().getId())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CODE_VARIABLE_ELEMENT).build();
        }
        // Note: in this point, do not check variable element as required if codelist variable is geographical, to allow save codes and then normalize all together with
        // 'normaliseVariableElementsToCodes'
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CodeMetamac> pagedResultCodeToMetamac(PagedResult<Code> source) {
        List<CodeMetamac> codesMetamac = codesToCodeMetamac(source.getValues());
        return new PagedResult<CodeMetamac>(codesMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private List<CodeMetamac> codesToCodeMetamac(List<Code> sources) {
        List<CodeMetamac> targets = new ArrayList<CodeMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((CodeMetamac) source);
        }
        return targets;
    }

    private CodelistFamily retrieveCodelistFamilyByUrn(String urn) throws MetamacException {
        CodelistFamily codelistFamily = getCodelistFamilyRepository().findByUrn(urn);
        if (codelistFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistFamily;
    }

    private VariableFamily retrieveVariableFamilyByUrn(String urn) throws MetamacException {
        VariableFamily variableFamily = getVariableFamilyRepository().findByUrn(urn);
        if (variableFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return variableFamily;
    }

    private Variable retrieveVariableByUrn(String urn) throws MetamacException {
        Variable variable = getVariableRepository().findByUrn(urn);
        if (variable == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return variable;
    }

    private VariableElement retrieveVariableElementByUrn(String urn) throws MetamacException {
        VariableElement variableElement = getVariableElementRepository().findByUrn(urn);
        if (variableElement == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return variableElement;
    }

    private VariableElementOperation retrieveVariableElementOperationByCode(String code) throws MetamacException {
        VariableElementOperation variableElementOperation = getVariableElementOperationRepository().findByCode(code);
        if (variableElementOperation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_OPERATION_NOT_FOUND).withMessageParameters(code).build();
        }
        return variableElementOperation;
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisationByUrn(String urn) throws MetamacException {
        CodelistOrderVisualisation codelistOrderVisualisation = getCodelistOrderVisualisationRepository().findByUrn(urn);
        if (codelistOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistOrderVisualisation;
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisationByCode(CodelistVersionMetamac codelistVersion, String code) throws MetamacException {
        CodelistOrderVisualisation codelistOrderVisualisation = getCodelistOrderVisualisationRepository().findByCode(codelistVersion.getId(), code);
        if (codelistOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CODELIST_ORDER_VISUALISATION_NOT_FOUND)
                    .withMessageParameters(code, codelistVersion.getMaintainableArtefact().getUrn()).build();
        }
        return codelistOrderVisualisation;
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisationByUrn(String urn) throws MetamacException {
        CodelistOpennessVisualisation codelistOpennessVisualisation = getCodelistOpennessVisualisationRepository().findByUrn(urn);
        if (codelistOpennessVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistOpennessVisualisation;
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisationByCode(CodelistVersionMetamac codelistVersion, String code) throws MetamacException {
        CodelistOpennessVisualisation codelistOpennessVisualisation = getCodelistOpennessVisualisationRepository().findByCode(codelistVersion.getId(), code);
        if (codelistOpennessVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CODELIST_OPENNESS_VISUALISATION_NOT_FOUND)
                    .withMessageParameters(code, codelistVersion.getMaintainableArtefact().getUrn()).build();
        }
        return codelistOpennessVisualisation;
    }

    private void checkCodelistCanBeModified(CodelistVersionMetamac codelistVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(codelistVersion.getLifeCycleMetadata(), codelistVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(codelistVersion);
    }

    /**
     * Common validations to create or update a variable
     */
    private void checkVariableToCreateOrUpdate(ServiceContext ctx, Variable variable) throws MetamacException {
        String variableUrn = variable.getNameableArtefact().getUrn();

        // Check variable doesnt replace self
        if (SrmServiceUtils.isVariableInList(variableUrn, variable.getReplaceToVariables())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_CAN_NOT_REPLACE_ITSELF).withMessageParameters(variableUrn).build();
        }

        // Check any variable in "replaceTo" was not already replaced by another variable
        for (Variable replaceTo : variable.getReplaceToVariables()) {
            if (replaceTo.getReplacedByVariable() != null && !replaceTo.getReplacedByVariable().getNameableArtefact().getUrn().equals(variableUrn)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED).withMessageParameters(replaceTo.getNameableArtefact().getUrn()).build();
            }
        }

        // Check translations
        String locale = srmConfiguration.retrieveLanguageDefault();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        SrmServiceUtils.checkNameableArtefactTranslationsWithoutSql(variable.getNameableArtefact(), locale, exceptionItems);
        SrmServiceUtils.checkInternationalStringTranslationsWithoutSql(variable.getShortName(), ServiceExceptionParameters.VARIABLE_SHORT_NAME, locale, exceptionItems);
        if (exceptionItems.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }

        // Check if type can be updated
        if (variable.getId() != null) {
            if (variable.getPreviousType() == null && VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType())) {
                Long variableElementsCount = getVariableElementRepository().countVariableElementsByVariable(variable.getId());
                if (variableElementsCount != 0) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_TYPE_UPDATE_TO_GEOGRAPHICAL_UNSUPPORTED).withMessageParameters(variableUrn).build();
                }
            } else if (!VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType())) {
                String variableWorldUrn = srmConfiguration.retrieveVariableWorldUrn();
                if (variableWorldUrn.equals(variable.getNameableArtefact().getUrn())) {
                    throw new MetamacException(ServiceExceptionType.METADATA_UNMODIFIABLE, ServiceExceptionParameters.VARIABLE_TYPE);
                }
            }
        }
    }

    private void checkVariableCanBeDeleted(Variable variableToDelete) throws MetamacException {
        String variableToDeleteUrn = variableToDelete.getNameableArtefact().getUrn();
        // Check it is not world
        checkVariableIsNotSpecialVariableWorld(variableToDelete);

        // Note: METAMAC-1644. Variable can be deleted even if it is published in api
        // Check variable has not concepts, variable elements neither codelists (in exception, say only one)
        if (CollectionUtils.isNotEmpty(variableToDelete.getCodelists())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WITH_RELATIONS)
                    .withMessageParameters(variableToDeleteUrn, variableToDelete.getCodelists().get(0).getMaintainableArtefact().getUrn()).build();
        }
        if (CollectionUtils.isNotEmpty(variableToDelete.getConcepts())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WITH_RELATIONS)
                    .withMessageParameters(variableToDeleteUrn, variableToDelete.getConcepts().get(0).getNameableArtefact().getUrn()).build();
        }
        if (CollectionUtils.isNotEmpty(variableToDelete.getVariableElements())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WITH_RELATIONS)
                    .withMessageParameters(variableToDeleteUrn, variableToDelete.getVariableElements().get(0).getIdentifiableArtefact().getUrn()).build();
        }
    }

    /**
     * Common validations to create or update a variable element
     */
    private void checkVariableElementToCreateOrUpdate(ServiceContext ctx, VariableElement variableElement) throws MetamacException {

        if (variableElement.getId() == null) {
            checkVariableIsNotSpecialVariableWorld(variableElement.getVariable());
        }

        // Check variable element doesnt replace self
        if (SrmServiceUtils.isVariableElementInList(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getReplaceToVariableElements())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_CAN_NOT_REPLACE_ITSELF).withMessageParameters(variableElement.getIdentifiableArtefact().getUrn())
                    .build();
        }

        // Check replaceTo belong to same variable
        for (VariableElement variableElementReplaceTo : variableElement.getReplaceToVariableElements()) {
            if (!variableElement.getVariable().getNameableArtefact().getUrn().equals(variableElementReplaceTo.getVariable().getNameableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE)
                        .withMessageParameters(variableElementReplaceTo.getIdentifiableArtefact().getUrn()).build();
            }
        }

        // Check do not change variable (execute this validation with 'contains method' is unefficient. Do parsing urn instead)
        if (variableElement.getId() != null) {
            if (!variableElement.getIdentifiableArtefact().getUrn().contains("=" + variableElement.getVariable().getNameableArtefact().getCode() + ".")) {
                throw new MetamacException(ServiceExceptionType.METADATA_UNMODIFIABLE, ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE);
            }
        }

        // Check any variable element in "replaceTo" was not already replaced by another variable element
        for (VariableElement replaceTo : variableElement.getReplaceToVariableElements()) {
            if (replaceTo.getReplacedByVariableElement() != null
                    && !replaceTo.getReplacedByVariableElement().getIdentifiableArtefact().getUrn().equals(variableElement.getIdentifiableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED).withMessageParameters(replaceTo.getIdentifiableArtefact().getUrn())
                        .build();
            }
        }

        // Check geographical information
        checkVariableElementGeographicalGranularity(ctx, variableElement);

        // Check translations
        String locale = srmConfiguration.retrieveLanguageDefault();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        SrmServiceUtils.checkInternationalStringTranslationsWithoutSql(variableElement.getShortName(), ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME, locale, exceptionItems);
        if (exceptionItems.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
    }

    private void checkVariableElementGeographicalGranularity(ServiceContext ctx, VariableElement variableElement) throws MetamacException {
        if (VariableTypeEnum.GEOGRAPHICAL.equals(variableElement.getVariable().getType())) {
            // Check required or empty
            String variableElementWorldUrn = srmConfiguration.retrieveVariableElementWorldUrn();
            if (variableElementWorldUrn.equals(variableElement.getIdentifiableArtefact().getUrn())) {
                if (variableElement.getGeographicalGranularity() != null) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNEXPECTED)
                            .withMessageParameters(ServiceExceptionParameters.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY).build();
                }
            } else {
                if (variableElement.getGeographicalGranularity() == null) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_REQUIRED)
                            .withMessageParameters(ServiceExceptionParameters.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY).build();
                }
            }
            // Check it is correct
            if (variableElement.getGeographicalGranularity() != null) {
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                CodeMetamac codeGeographicalGranularity = retrieveCodeByUrn(ctx, variableElement.getGeographicalGranularity().getNameableArtefact().getUrn());
                Long codeGeographicalGranularityId = codeGeographicalGranularity.getId();
                List<ConditionalCriteria> criteriaToVerifyGeographicalGranularity = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.id())
                        .eq(codeGeographicalGranularityId).build();
                PagedResult<CodeMetamac> result = findCodesCanBeVariableElementGeographicalGranularityByCondition(ctx, criteriaToVerifyGeographicalGranularity, pagingParameter);
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codeGeographicalGranularityId)) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                            .withMessageParameters(ServiceExceptionParameters.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY).build();
                }
            }
        } else {
            // empty metadata are checked in CodesMetamacInvocationValidator
        }
    }

    private void checkVariableIsNotSpecialVariableWorld(Variable variable) throws MetamacException {
        String variableWorldUrn = srmConfiguration.retrieveVariableWorldUrn(); // required property
        if (variableWorldUrn.equals(variable.getNameableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WORLD_OPERATION_NOT_SUPPORTED).withMessageParameters(variable.getNameableArtefact().getUrn())
                    .build();
        }
    }

    private void checkVariableElementCanBeDeleted(VariableElement variableElementToDelete) throws MetamacException {
        String variableElementToDeleteUrn = variableElementToDelete.getIdentifiableArtefact().getUrn();
        // Check it is not world
        checkVariableElementIsNotSpecialVariableElementWorld(variableElementToDelete);

        if (CollectionUtils.isNotEmpty(variableElementToDelete.getCodes())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_WITH_RELATIONS)
                    .withMessageParameters(variableElementToDeleteUrn, variableElementToDelete.getCodes().get(0).getNameableArtefact().getUrn()).build(); // say one
        }
        // Check variableElement has not operations
        List<VariableElementOperation> variableElementsOperations = getVariableElementOperationRepository().findByVariableElementUrn(variableElementToDeleteUrn);
        if (CollectionUtils.isNotEmpty(variableElementsOperations)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_WITH_OPERATIONS).withMessageParameters(variableElementToDeleteUrn).build();
        }
    }

    private void checkVariableElementIsNotSpecialVariableElementWorld(VariableElement variableElement) throws MetamacException {
        String variableElementWorldUrn = srmConfiguration.retrieveVariableElementWorldUrn(); // required property
        if (variableElementWorldUrn.equals(variableElement.getIdentifiableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_WORLD_OPERATION_NOT_SUPPORTED)
                    .withMessageParameters(variableElement.getIdentifiableArtefact().getUrn()).build();
        }
    }

    private CodelistVersionMetamac retrieveAndCheckCodelistWhoseCodesCanBeAsGeographicalGranularity(ServiceContext ctx) throws MetamacException {
        String codelistUrn = srmConfiguration.retrieveCodelistUrnForVariableElementGeographicalGranularity();

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        CodelistVersionMetamac codelistGeographicalGranularity = retrieveCodelistByUrn(ctx, codelistUrn);
        Long codelistGeographicalGranularityId = codelistGeographicalGranularity.getId();
        List<ConditionalCriteria> criteriaToVerifyGeographicalGranularity = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.id())
                .eq(codelistGeographicalGranularityId).build();
        PagedResult<CodelistVersionMetamac> result = findCodelistsWhoseCodesCanBeVariableElementGeographicalGranularityByCondition(ctx, criteriaToVerifyGeographicalGranularity, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistGeographicalGranularityId)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CODELIST_UNSUPPORTED_TO_GEOGRAPHICAL_GRANULARITY).withMessageParameters(codelistUrn).build();
        }
        return codelistGeographicalGranularity;
    }

    /**
     * Generate urn, check it is unique and set to codelist family. Set also urnProvider
     */
    private void setCodelistFamilyUrnUnique(CodelistFamily codelistFamily) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistFamilyUrn(codelistFamily);
        identifiableArtefactRepository.checkUrnUnique(urn, codelistFamily.getNameableArtefact().getId());

        codelistFamily.getNameableArtefact().setUrn(urn);
        codelistFamily.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable family. Set also urnProvider
     */
    private void setVariableFamilyUrnUnique(VariableFamily variableFamily) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableFamilyUrn(variableFamily);
        identifiableArtefactRepository.checkUrnUnique(urn, variableFamily.getNameableArtefact().getId());

        variableFamily.getNameableArtefact().setUrn(urn);
        variableFamily.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable. Set also urnProvider
     */
    private void setVariableUrnUnique(Variable variable) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableUrn(variable);
        identifiableArtefactRepository.checkUrnUnique(urn, variable.getNameableArtefact().getId());

        variable.getNameableArtefact().setUrn(urn);
        variable.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable element. Set also urnProvider
     */
    private void setVariableElementUrnUnique(Variable variable, VariableElement variableElement, boolean checkUrnUnique) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableElementUrn(variable, variableElement);
        if (checkUrnUnique) {
            // In import cases this verification isn't performed for performance. In BBDD this constraint is checked
            identifiableArtefactRepository.checkUrnUnique(urn, variableElement.getIdentifiableArtefact().getId());
        }

        variableElement.getIdentifiableArtefact().setUrn(urn);
        variableElement.getIdentifiableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to visualisation. Set also urnProvider
     */
    private void setCodelistOrderVisualisationUrnUnique(CodelistVersion codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, boolean checkUrnUnique) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistOrderVisualisationUrn(codelistVersion, codelistOrderVisualisation);

        if (checkUrnUnique) {
            // In importation or versioning this verification isn't performed for performance (avoid flushing). In BBDD this constraint is checked
            identifiableArtefactRepository.checkUrnUnique(urn, codelistOrderVisualisation.getNameableArtefact().getId());
        }

        codelistOrderVisualisation.getNameableArtefact().setUrn(urn);
        codelistOrderVisualisation.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to visualisation. Set also urnProvider
     */
    private void setCodelistOpennessVisualisationUrnUnique(CodelistVersion codelistVersion, CodelistOpennessVisualisation codelistOpennessVisualisation, boolean checkUrnUnique)
            throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistOpennessVisualisationUrn(codelistVersion, codelistOpennessVisualisation);

        if (checkUrnUnique) {
            // In importation or versioning this verification isn't performed for performance (avoid flushing). In BBDD this constraint is checked
            identifiableArtefactRepository.checkUrnUnique(urn, codelistOpennessVisualisation.getNameableArtefact().getId());
        }

        codelistOpennessVisualisation.getNameableArtefact().setUrn(urn);
        codelistOpennessVisualisation.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * A code can not be moved to its child
     */
    private void checkItemIsNotChildren(ServiceContext ctx, Item item, String parentTargetUrn) throws MetamacException {
        Item parentTarget = retrieveCodeByUrn(ctx, parentTargetUrn);
        while (parentTarget != null) {
            if (parentTarget.getId().equals(item.getId())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.ITEM_PARENT);
            }
            parentTarget = parentTarget.getParent();
        }
    }

    @Override
    public CodelistVersionMetamac createCodelistOrderVisualisationAlphabetical(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        CodelistOrderVisualisation alphabeticalOrderVisualisation = new CodelistOrderVisualisation();
        alphabeticalOrderVisualisation.setNameableArtefact(new NameableArtefact());
        alphabeticalOrderVisualisation.getNameableArtefact().setCode(SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE);
        // NameableArtefact
        InternationalString name = new InternationalString();
        name.addText(new LocalisedString("es", "Orden alfabético"));
        name.addText(new LocalisedString("en", "Alphabetical order"));
        name.addText(new LocalisedString("pt", "Ordem alfabética"));
        alphabeticalOrderVisualisation.getNameableArtefact().setName(name);
        alphabeticalOrderVisualisation.setColumnIndex(SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX);
        setCodelistOrderVisualisationUrnUnique(codelistVersion, alphabeticalOrderVisualisation, true);

        codelistVersion.addOrderVisualisation(alphabeticalOrderVisualisation);
        codelistVersion = getCodelistVersionMetamacRepository().save(codelistVersion);

        sortCodesInAlphabeticalOrder(codelistVersion.getItems(), alphabeticalOrderVisualisation, true);
        return codelistVersion;
    }

    private boolean isCodesSiblings(Item i1, Item i2) {
        if (i1.getParent() == null && i2.getParent() == null) {
            return true;
            // id can be null when save operation is executed when transaction is closed
        } else if (i1.getParent() != null && i2.getParent() != null && i1.getParent().getUuid().equals(i2.getParent().getUuid())) {
            return true;
        }
        return false;
    }

    /**
     * Sorts all codes, ordered by semantic identifier
     */
    private void sortCodesInAlphabeticalOrder(List<Code> items, CodelistOrderVisualisation alphabeticalOrderVisualisation, boolean executeSave) {
        sortCodes(items, alphabeticalOrderVisualisation, new AlphabeticalByLevelComparator(), executeSave);
    }

    /**
     * Sorts all codes, ordered by index. Put codes with null index at the end off level
     */
    private void sortCodesByOrder(List<Code> items, final CodelistOrderVisualisation orderVisualisation, boolean executeSave) {
        sortCodes(items, orderVisualisation, new OrderIndexByLevelComparator(orderVisualisation), executeSave);
    }

    /**
     * Sorts all codes by level, sorting codes by comparator
     */
    private void sortCodes(List<Code> items, CodelistOrderVisualisation orderVisualisation, Comparator<Code> comparator, boolean executeSave) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        List<Code> codesOrdered = new ArrayList<Code>(items);
        Collections.sort(codesOrdered, comparator);

        String previousParentUrn = null;
        int previousOrder = -1;
        for (Item item : codesOrdered) {
            CodeMetamac code = (CodeMetamac) item;
            String actualParentUrn = code.getParent() != null ? code.getParent().getNameableArtefact().getUrn() : null;
            if (!StringUtils.equals(previousParentUrn, actualParentUrn)) {
                previousOrder = -1; // another level
                previousParentUrn = code.getParent() != null ? code.getParent().getNameableArtefact().getUrn() : null;
            }
            int order = previousOrder == -1 ? 0 : previousOrder + 1;
            SrmServiceUtils.setCodeOrder(code, orderVisualisation.getColumnIndex(), Integer.valueOf(order));
            if (executeSave) {
                // in some operations, save method must not be executed due to efficiency of inserts operations
                getCodeMetamacRepository().save(code);
            }
            previousOrder = order;
        }
    }

    @Override
    public CodelistVersionMetamac createCodelistOpennessVisualisationAllOpened(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        CodelistOpennessVisualisation allExpandedOpennessVisualisation = new CodelistOpennessVisualisation();
        allExpandedOpennessVisualisation.setNameableArtefact(new NameableArtefact());
        allExpandedOpennessVisualisation.getNameableArtefact().setCode(SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE);
        // NameableArtefact
        InternationalString name = new InternationalString();
        name.addText(new LocalisedString("es", "Todos desplegados"));
        name.addText(new LocalisedString("en", "All opened"));
        name.addText(new LocalisedString("pt", "Tudo aberto"));
        allExpandedOpennessVisualisation.getNameableArtefact().setName(name);
        allExpandedOpennessVisualisation.setColumnIndex(SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_COLUMN_INDEX);
        setCodelistOpennessVisualisationUrnUnique(codelistVersion, allExpandedOpennessVisualisation, true);

        codelistVersion.addOpennessVisualisation(allExpandedOpennessVisualisation);
        codelistVersion = getCodelistVersionMetamacRepository().save(codelistVersion);

        // Add codes with expanded = true as default
        getCodeMetamacRepository().updateCodesOpennessColumnByCodelist(codelistVersion, allExpandedOpennessVisualisation.getColumnIndex(), Boolean.TRUE);
        return codelistVersion;
    }

    private void fillCodelistOpennessVisualisationToCodesWithEmptyOpenness(ServiceContext ctx, List<Code> items, CodelistOpennessVisualisation opennessVisualisation, Boolean value) {
        for (Item item : items) {
            CodeMetamac code = (CodeMetamac) item;
            if (SrmServiceUtils.getCodeOpenness(code, opennessVisualisation.getColumnIndex()) == null) {
                SrmServiceUtils.setCodeOpenness(code, opennessVisualisation.getColumnIndex(), value);
            }
        }
    }

    private VariableElementOperation createVariableElementOperationCommon(VariableElementOperationTypeEnum operationType, List<String> sources, List<String> targets) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkAddVariableElementOperation(sources, targets, null);
        checkVariableElementOperationToCreate(sources, targets);

        // Create
        VariableElementOperation variableElementOperation = new VariableElementOperation();
        variableElementOperation.setOperationType(operationType);
        variableElementOperation.setCode(UUID.randomUUID().toString());

        // Add sources and targets
        for (String sourceVariableElementUrn : sources) {
            VariableElement sourceVariableElement = retrieveVariableElementByUrn(sourceVariableElementUrn);
            variableElementOperation.addSource(sourceVariableElement);
        }
        for (String targetVariableElementUrn : targets) {
            VariableElement targetVariableElement = retrieveVariableElementByUrn(targetVariableElementUrn);
            variableElementOperation.addTarget(targetVariableElement);
        }

        // Check all elements belong to same variable and has validTo filled
        List<VariableElement> variableElements = new ArrayList<VariableElement>(variableElementOperation.getSources().size() + variableElementOperation.getTargets().size());
        variableElements.addAll(variableElementOperation.getSources());
        variableElements.addAll(variableElementOperation.getTargets());
        Variable variable = null;
        for (VariableElement variableElement : variableElements) {
            // ValidTo
            if (variableElement.getValidTo() == null) {
                throw new MetamacException(ServiceExceptionType.VARIABLE_ELEMENT_MUST_HAVE_VALID_TO_FILLED, variableElement.getIdentifiableArtefact().getUrn());
            }
            // Variable
            if (variable == null) {
                variable = variableElement.getVariable();
            } else {
                if (!variable.getNameableArtefact().getUrn().equals(variableElement.getVariable().getNameableArtefact().getUrn())) {
                    throw new MetamacException(ServiceExceptionType.VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE, variableElement.getIdentifiableArtefact().getUrn());
                }
            }
        }

        // Create
        variableElementOperation.setVariable(variable);
        variableElementOperation = getVariableElementOperationRepository().save(variableElementOperation);

        return variableElementOperation;
    }

    private void reorderCodesDeletingOneCode(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, Code parent, CodeMetamac code) throws MetamacException {
        Integer columnIndex = codelistOrderVisualisation.getColumnIndex();
        Integer previousOrder = SrmServiceUtils.getCodeOrder(code, columnIndex);
        getCodeMetamacRepository().reorderCodesDeletingOneCode(codelistVersion, parent, code, columnIndex, previousOrder);
    }

    private void reorderCodesAddingOneCodeInMiddle(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, CodeMetamac code, Integer order)
            throws MetamacException {
        Integer columnIndex = codelistOrderVisualisation.getColumnIndex();
        getCodeMetamacRepository().reorderCodesAddingOneCodeInMiddle(codelistVersion, code, columnIndex, order);
    }

    private void setCodeOrderInAlphabeticalPositionAndReorderCodesInLevel(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, Code parent, CodeMetamac code)
            throws MetamacException {
        if (!SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
            return;
        }
        Integer alphabeticalPosition = getCodeMetamacRepository().getCodeAlphabeticPositionInLevel(codelistVersion, parent, code);
        Integer columnIndex = SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX;
        if (alphabeticalPosition == null) {
            alphabeticalPosition = Integer.valueOf(0);
        }
        reorderCodesAddingOneCodeInMiddle(codelistVersion, codelistOrderVisualisation, code, alphabeticalPosition);
        SrmServiceUtils.setCodeOrder(code, columnIndex, alphabeticalPosition);
    }

    private Integer setCodeOrderInLastPositionInLevel(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, Code parent, CodeMetamac code,
            Integer precalculateOrder) throws MetamacException {
        Integer columnIndex = codelistOrderVisualisation.getColumnIndex();
        Integer orderInNewLevel = null;
        if (precalculateOrder == null) {
            SrmServiceUtils.setCodeOrder(code, columnIndex, -1); // min value
            Integer actualMaximumOrderInLevel = getCodeMetamacRepository().getCodeMaximumOrderInLevel(codelistVersion, parent, columnIndex);
            if (actualMaximumOrderInLevel == null) {
                orderInNewLevel = 0;
            } else {
                orderInNewLevel = actualMaximumOrderInLevel + 1;
            }
        } else {
            orderInNewLevel = precalculateOrder;
        }
        SrmServiceUtils.setCodeOrder(code, columnIndex, orderInNewLevel);
        return orderInNewLevel;
    }

    @Override
    public CodelistVersionMetamac versioningCodelistOrderVisualisations(ServiceContext ctx, CodelistVersionMetamac source, CodelistVersionMetamac target) throws MetamacException {
        for (CodelistOrderVisualisation codelistOrderVisualisationSource : source.getOrderVisualisations()) {
            versioningCodelistOrderVisualisation(codelistOrderVisualisationSource, target);
        }
        target = getCodelistVersionMetamacRepository().save(target);
        for (CodelistOrderVisualisation codelistOrderVisualisationTarget : target.getOrderVisualisations()) {
            if (source.getDefaultOrderVisualisation() != null
                    && source.getDefaultOrderVisualisation().getNameableArtefact().getCode().equals(codelistOrderVisualisationTarget.getNameableArtefact().getCode())) {
                target.setDefaultOrderVisualisation(codelistOrderVisualisationTarget);
                break;
            }
        }
        target = getCodelistVersionMetamacRepository().save(target);
        return target;
    }

    private CodelistOrderVisualisation versioningCodelistOrderVisualisation(CodelistOrderVisualisation source, CodelistVersionMetamac codelistTarget) throws MetamacException {
        CodelistOrderVisualisation target = new CodelistOrderVisualisation();
        target.setColumnIndex(source.getColumnIndex());
        target.setNameableArtefact(versioningCodelistVisualisationNameableArtefact(source.getNameableArtefact()));;
        setCodelistOrderVisualisationUrnUnique(codelistTarget, target, false);
        codelistTarget.addOrderVisualisation(target);
        return target;
    }

    private void createOrderVisualisationVersionFromTemporalOrderVisualisation(ServiceContext ctx, CodelistVersionMetamac codelistVersionTemporal) throws MetamacException {
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersionTemporal.getOrderVisualisations()) {
            setCodelistOrderVisualisationUrnUnique(codelistVersionTemporal, codelistOrderVisualisation, true);
        }

        getCodelistVersionMetamacRepository().save(codelistVersionTemporal);
    }

    @Override
    public CodelistVersionMetamac versioningCodelistOpennessVisualisations(ServiceContext ctx, CodelistVersionMetamac source, CodelistVersionMetamac target) throws MetamacException {
        for (CodelistOpennessVisualisation codelistOpennessVisualisationSource : source.getOpennessVisualisations()) {
            versioningCodelistOpennessVisualisation(codelistOpennessVisualisationSource, target);
        }
        target = getCodelistVersionMetamacRepository().save(target);
        for (CodelistOpennessVisualisation codelistOpennessVisualisationTarget : target.getOpennessVisualisations()) {
            if (source.getDefaultOpennessVisualisation() != null
                    && source.getDefaultOpennessVisualisation().getNameableArtefact().getCode().equals(codelistOpennessVisualisationTarget.getNameableArtefact().getCode())) {
                target.setDefaultOpennessVisualisation(codelistOpennessVisualisationTarget);
                break;
            }
        }
        target = getCodelistVersionMetamacRepository().save(target);
        return target;
    }

    private CodelistOpennessVisualisation versioningCodelistOpennessVisualisation(CodelistOpennessVisualisation source, CodelistVersionMetamac codelistTarget) throws MetamacException {
        CodelistOpennessVisualisation target = new CodelistOpennessVisualisation();
        target.setColumnIndex(source.getColumnIndex());
        target.setNameableArtefact(versioningCodelistVisualisationNameableArtefact(source.getNameableArtefact()));;
        setCodelistOpennessVisualisationUrnUnique(codelistTarget, target, false);
        codelistTarget.addOpennessVisualisation(target);
        return target;
    }

    private void createOpennessVisualisationVersionFromTemporalOpennessVisualisation(ServiceContext ctx, CodelistVersionMetamac codelistVersionTemporal) throws MetamacException {
        for (CodelistOpennessVisualisation codelistOpennessVisualisation : codelistVersionTemporal.getOpennessVisualisations()) {
            setCodelistOpennessVisualisationUrnUnique(codelistVersionTemporal, codelistOpennessVisualisation, true);
        }

        getCodelistVersionMetamacRepository().save(codelistVersionTemporal);
    }

    private void deleteAllCodesIfProceed(CodelistVersionMetamac codelistVersionTemporal, Boolean versioningCodes) throws MetamacException {
        if (Boolean.FALSE.equals(versioningCodes)) {
            getCodeMetamacRepository().deleteAllCodesEfficiently(codelistVersionTemporal.getId());
        }
    }

    private NameableArtefact versioningCodelistVisualisationNameableArtefact(NameableArtefact source) {
        NameableArtefact target = new NameableArtefact();

        // visualisations only have these metadata
        target.setName(BaseCopyAllMetadataUtils.copy(source.getName()));
        target.setCode(source.getCode());
        target.setCodeFull(source.getCodeFull());

        return target;
    }

    private CodeMetamac initVisualisationsToCodeCreated(CodelistVersionMetamac codelistVersion, CodeMetamac code) throws MetamacException {

        // Order visualisations
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        Integer orderInLastPosition = null; // no avoid unnecessary extra queries
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                setCodeOrderInAlphabeticalPositionAndReorderCodesInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                orderInLastPosition = setCodeOrderInLastPositionInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code, orderInLastPosition);
            }
        }

        // Openness visualisations
        Boolean defaultOpennessValue = SrmConstants.CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE;
        Boolean allExpandedAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        for (CodelistOpennessVisualisation codelistOpennessVisualisation : codelistVersion.getOpennessVisualisations()) {
            Boolean value = null;
            if (!allExpandedAlreadyUpdated && SrmServiceUtils.isAllExpandedOpennessVisualisation(codelistOpennessVisualisation)) {
                value = Boolean.TRUE;
                allExpandedAlreadyUpdated = Boolean.TRUE;
            } else {
                value = defaultOpennessValue;
            }
            SrmServiceUtils.setCodeOpenness(code, codelistOpennessVisualisation.getColumnIndex(), value);
        }

        code = getCodeMetamacRepository().save(code);
        return code;
    }

    private CodeMetamac prepareCopyCodeInCodelist(Map<String, CodeMetamac> codesInCodelistSourceByUrn, Map<String, ItemResult> codesItemResultInCodelistSourceByUrn,
            CodelistVersionMetamac codelistVersionTarget, Map<String, CodeMetamac> codesPreviousInCodelistTargetByCode, Map<String, CodeMetamac> codesToPersistByCode, CodeToCopy codeToCopy,
            boolean copyVariableElements) throws MetamacException {

        // Prepare source
        ItemResult codeItemResultSource = codesItemResultInCodelistSourceByUrn.get(codeToCopy.getSourceUrn());
        if (codeItemResultSource == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.URN).build();
        }
        CodeMetamac codeSource = codesInCodelistSourceByUrn.get(codeToCopy.getSourceUrn());

        // semantic identifier and urn
        String codeIdentifier = codeToCopy.getNewCodeIdentifier();
        if (codeIdentifier == null) {
            codeIdentifier = codeItemResultSource.getCode();
        }
        String urn = GeneratorUrnUtils.generateCodeUrn(codelistVersionTarget, codeIdentifier);
        if (codesToPersistByCode.containsKey(codeIdentifier) || codesPreviousInCodelistTargetByCode.containsKey(codeIdentifier)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED).withMessageParameters(urn).build();
        }
        if (!SemanticIdentifierValidationUtils.isCodeSemanticIdentifier(codeIdentifier)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE).build();
        }

        // parent
        CodeMetamac codeParent = null;
        String codeParentIdentifier = codeToCopy.getParentNewCodeIdentifier();
        if (codeParentIdentifier != null) {
            if (codesToPersistByCode.containsKey(codeParentIdentifier)) {
                codeParent = codesToPersistByCode.get(codeParentIdentifier);
            } else {
                codeParent = codesPreviousInCodelistTargetByCode.get(codeParentIdentifier); // try find code that already exists in codelist before copy
            }
            if (codeParent == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.ITEM_PARENT).build();
            }
        }

        // copy code
        CodeMetamac codeTarget = new CodeMetamac();
        codeTarget.setItemSchemeVersion(codelistVersionTarget);
        if (codeParent == null) {
            codeTarget.setParent(null);
            codeTarget.setItemSchemeVersionFirstLevel(codelistVersionTarget);
        } else {
            codeTarget.setParent(codeParent);
            codeTarget.setItemSchemeVersionFirstLevel(null);
        }
        codeTarget.setNameableArtefact(new NameableArtefact());
        codeTarget.getNameableArtefact().setCode(codeIdentifier);
        codeTarget.getNameableArtefact().setUrn(urn);
        codeTarget.getNameableArtefact().setUrnProvider(urn);
        codeTarget.getNameableArtefact().setName(BaseCopyAllMetadataUtils.copyInternationalString(codeItemResultSource.getName()));
        codeTarget.getNameableArtefact().setDescription(BaseCopyAllMetadataUtils.copyInternationalString(codeItemResultSource.getDescription()));
        codeTarget.getNameableArtefact().setComment(null);
        BaseCopyAllMetadataUtils.copyAnnotations(codeItemResultSource, codeTarget.getNameableArtefact());
        codeTarget.setShortName(BaseCopyAllMetadataUtils.copyInternationalString(SrmServiceUtils.getCodeItemResultShortName(codeItemResultSource)));
        if (copyVariableElements) {
            codeTarget.setVariableElement(codeSource.getVariableElement());
        }

        // extra validation to avoid save some incorrect
        CodesMetamacInvocationValidator.checkCreateCode(codelistVersionTarget, codeTarget, null);
        return codeTarget;
    }

    /**
     * Transforms tsv line to Code order. IMPORTANT: Do not execute save or update operation
     */
    private CodeMetamac tsvLineToCodeOrder(ImportationCodeOrdersTsvHeader header, String[] columns, int lineNumber, Map<String, CodeMetamac> codesInCodelist,
            List<CodelistOrderVisualisation> orderVisualisations, List<MetamacExceptionItem> exceptionItems) throws MetamacException {

        // semantic identifier
        String codeIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        // update orders
        CodeMetamac code = codesInCodelist.get(codeIdentifier);
        if (code == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_CODE_NOT_FOUND, codeIdentifier));
            return null;
        }
        for (CodelistOrderVisualisation orderVisualisation : orderVisualisations) {
            int columnIndex = header.getOrderVisualisationColumn(orderVisualisation.getNameableArtefact().getCode());
            String orderValueString = columns[columnIndex];
            if (StringUtils.isBlank(orderValueString)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, code.getNameableArtefact().getCode(),
                        ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ORDER));

                return null;
            }
            Integer orderValue = Integer.valueOf(orderValueString);
            SrmServiceUtils.setCodeOrder(code, orderVisualisation.getColumnIndex(), orderValue);
        }

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        return code;
    }

    /**
     * Transforms tsv line to VariableElement. IMPORTANT: Do not execute save or update operation
     */
    private VariableElement tsvLineToVariableElement(ImportationVariableElementsTsvHeader header, String[] columns, int lineNumber, Variable variable, boolean updateAlreadyExisting,
            Map<String, VariableElement> variableElementsPreviousInVariable, Map<String, VariableElement> variableElementsToPersistByCode,
            Map<String, CodeMetamac> codesCanBeGeographicalGranularityByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems) throws MetamacException {

        // semantic identifier
        String codeIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null; // do not add more errors in this line
        }
        if (variableElementsToPersistByCode.containsKey(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, codeIdentifier, lineNumber));
            return null;
        }
        if (!SemanticIdentifierValidationUtils.isVariableElementSemanticIdentifier(codeIdentifier)) {
            exceptionItems
                    .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, codeIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // init variable element
        VariableElement variableElement = variableElementsPreviousInVariable.get(codeIdentifier);
        if (variableElement == null) {
            variableElement = new VariableElement();
            variableElement.setVariable(variable);
            variableElement.setIdentifiableArtefact(new IdentifiableArtefact());
            variableElement.getIdentifiableArtefact().setCode(codeIdentifier);
            setVariableElementUrnUnique(variable, variableElement, Boolean.FALSE);
        } else {
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, variableElement.getIdentifiableArtefact().getCode()));
                return null;
            } else {
                variableElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.FALSE);
                variableElement.setUpdateDate(new DateTime());
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, variableElement.getIdentifiableArtefact().getCode()));
            }
        }
        // short name
        variableElement.setShortName(TsvImportationUtils.tsvLineToInternationalString(header.getShortName(), columns, variableElement.getShortName()));
        if (variableElement.getShortName() == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, variableElement.getIdentifiableArtefact().getCode(),
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_SHORT_NAME));
        }

        // geographical granularity
        if (header.isGeographicalGranularitySetted()) {
            String geographicalGranularityIdentifier = columns[header.getGeographicalGranularityPosition()];
            if (VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType())) {
                if (StringUtils.isBlank(geographicalGranularityIdentifier)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, variableElement.getIdentifiableArtefact().getCode(),
                            ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_GEOGRAPHICAL_GRANULARITY));
                } else {
                    CodeMetamac geographicalGranularity = codesCanBeGeographicalGranularityByCode.get(geographicalGranularityIdentifier);
                    variableElement.setGeographicalGranularity(geographicalGranularity);
                }
            } else {
                if (!StringUtils.isBlank(geographicalGranularityIdentifier)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_UNEXPECTED, variableElement.getIdentifiableArtefact().getCode(),
                            ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_GEOGRAPHICAL_GRANULARITY));
                }
            }
        }

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            // extra validation to avoid save an incorrect variable element
            if (variableElement.getId() == null) {
                CodesMetamacInvocationValidator.checkCreateVariableElement(variableElement, null);
            } else {
                CodesMetamacInvocationValidator.checkUpdateVariableElement(variableElement, null);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing variable element from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }
        return variableElement;
    }

    /**
     * Get a column available in Code to store orders
     */
    private Integer getCodelistOrderVisualisationAvailable(CodelistVersionMetamac codelistVersion) throws MetamacException {
        int index = -1;
        for (int i = 1; i <= SrmConstants.CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
            boolean alreadyExists = false;
            for (CodelistOrderVisualisation codelistOrderVisualisationPrevious : codelistVersion.getOrderVisualisations()) {
                if (codelistOrderVisualisationPrevious.getColumnIndex().intValue() == i) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new MetamacException(ServiceExceptionType.CODELIST_ORDER_VISUALISATION_MAXIMUM_REACHED, codelistVersion.getMaintainableArtefact().getUrn());
        }
        return Integer.valueOf(index);
    }

    /**
     * Get a column available in Code to store opennesss
     */
    private Integer getCodelistOpennessVisualisationAvailable(CodelistVersionMetamac codelistVersion) throws MetamacException {
        int index = -1;
        for (int i = 1; i <= SrmConstants.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER; i++) {
            boolean alreadyExists = false;
            for (CodelistOpennessVisualisation codelistOpennessVisualisationPrevious : codelistVersion.getOpennessVisualisations()) {
                if (codelistOpennessVisualisationPrevious.getColumnIndex().intValue() == i) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new MetamacException(ServiceExceptionType.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_REACHED, codelistVersion.getMaintainableArtefact().getUrn());
        }
        return Integer.valueOf(index);
    }

    private class AlphabeticalByLevelComparator implements Comparator<Code> {

        @Override
        public int compare(Code i1, Code i2) {
            if (isCodesSiblings(i1, i2)) {
                return calculeOrderCodeSiblingsAlphabetical(i1, i2);
            } else if (i1.getParent() == null) {
                return 1;
            } else if (i2.getParent() == null) {
                return -1;
            } else {
                // return i1.getParent().getId().compareTo(i2.getParent().getId()); // not used because id can be null when save operation is executed when transaction is closed
                return i1.getParent().getUuid().compareTo(i2.getParent().getUuid());
            }
        }
    }

    private class OrderIndexByLevelComparator implements Comparator<Code> {

        private final int columnIndex;

        public OrderIndexByLevelComparator(CodelistOrderVisualisation orderVisualisation) {
            columnIndex = orderVisualisation.getColumnIndex();
        }

        @Override
        public int compare(Code i1, Code i2) {
            CodeMetamac c1 = (CodeMetamac) i1;
            CodeMetamac c2 = (CodeMetamac) i2;
            if (isCodesSiblings(i1, i2)) {
                // The elements with order set as null will set to the end.
                Integer c1Order = SrmServiceUtils.getCodeOrder(c1, columnIndex);
                Integer c2Order = SrmServiceUtils.getCodeOrder(c2, columnIndex);
                if (c1Order == null && c2Order == null) {
                    return calculeOrderCodeSiblingsAlphabetical(i1, i2);
                } else if (c1Order == null) {
                    return 1;
                } else if (c2Order == null) {
                    return -1;
                } else {
                    return c1Order.compareTo(c2Order);
                }
            } else if (i1.getParent() == null) {
                return 1;
            } else if (i2.getParent() == null) {
                return -1;
            } else {
                // return i1.getParent().getId().compareTo(i2.getParent().getId()); // not used because id can be null when save operation is executed when transaction is closed
                return i1.getParent().getUuid().compareTo(i2.getParent().getUuid());
            }
        }

    };

    private int calculeOrderCodeSiblingsAlphabetical(Item i1, Item i2) {
        return i1.getNameableArtefact().getCode().compareToIgnoreCase(i2.getNameableArtefact().getCode());
    }

    private List<CodelistOrderVisualisation> tsvHeadToOrderVisualisations(CodelistVersionMetamac codelistVersion, ImportationCodeOrdersTsvHeader header, List<MetamacExceptionItem> exceptionItems) {
        List<CodelistOrderVisualisation> orderVisualisations = new ArrayList<CodelistOrderVisualisation>();
        for (String orderVisualisationCode : header.getOrderVisualisations()) {
            CodelistOrderVisualisation orderVisualisation = getCodelistOrderVisualisationRepository().findByCode(codelistVersion.getId(), orderVisualisationCode);
            if (orderVisualisation == null) {
                exceptionItems.add(
                        new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_ORDER_VISUALISATION_NOT_FOUND, orderVisualisationCode, codelistVersion.getMaintainableArtefact().getUrn()));
                break;
            } else if (SrmServiceUtils.isAlphabeticalOrderVisualisation(orderVisualisation)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_ALPHABETICAL_VISUALISATION_NOT_SUPPORTED));
                break;
            } else {
                orderVisualisations.add(orderVisualisation);
            }
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }
        return orderVisualisations;
    }

    private void saveCodesEfficiently(Collection<CodeMetamac> codesToPersist, Map<String, CodeMetamac> codesToPersistByCode) {
        for (CodeMetamac codeMetamac : codesToPersist) {
            if (codeMetamac.getParent() != null) {
                if (codesToPersistByCode.containsKey(codeMetamac.getParent().getNameableArtefact().getCode())) {
                    // update reference because it was saved
                    codeMetamac.setParent(codesToPersistByCode.get(codeMetamac.getParent().getNameableArtefact().getCode()));
                }
            }
            codeMetamac = getCodeMetamacRepository().save(codeMetamac);
            // update reference after save to assign to children
            codesToPersistByCode.put(codeMetamac.getNameableArtefact().getCode(), codeMetamac);
        }
    }

    /**
     * Determine if you need a schedule
     */
    private boolean mustExecuteTaskInBackground(boolean satisfyAdditionalConditions, ItemSchemeVersion itemSchemeVersion, Boolean canBeBackground) {
        if (satisfyAdditionalConditions && BooleanUtils.isTrue(canBeBackground)) {
            Long itemsCount = codeRepository.countItems(itemSchemeVersion.getId());
            if (itemsCount > SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND) {
                return true;
            }
        }
        return false;
    }

    private CodelistVersionMetamac retrieveCodelistVersionByUrn(String urn) throws MetamacException {
        CodesInvocationValidator.checkRetrieveByUrn(urn);
        CodelistVersionMetamac codelistVersion = getCodelistVersionMetamacRepository().findByUrn(urn);
        if (codelistVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistVersion;
    }

    private void checkVariableElementOperationToCreate(List<String> sourcesUrn, List<String> targetsUrn) throws MetamacException {
        // A variable element can not be source and target in same operation
        for (String source : sourcesUrn) {
            if (targetsUrn.contains(source)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_OPERATION_VARIABLE_ELEMENT_IN_SOURCE_AND_TARGET).withMessageParameters(source).build();
            }
        }
        // A variable element can not be as source in two operations
        for (String source : sourcesUrn) {
            VariableElementOperation variableElementOperation = getVariableElementOperationRepository().findVariableElementWithVariableElementAsSource(source);
            if (variableElementOperation != null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_ALREADY_AS_SOURCE_IN_OPERATION).withMessageParameters(source).build();
            }
        }
        // A variable element can not be as target in two operations
        for (String target : targetsUrn) {
            VariableElementOperation variableElementOperation = getVariableElementOperationRepository().findVariableElementWithVariableElementAsTarget(target);
            if (variableElementOperation != null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_ALREADY_AS_TARGET_IN_OPERATION).withMessageParameters(target).build();
            }
        }
    }

    private TaskImportationInfo importVariableElementsGeographicalInformation(ServiceContext ctx, String variableUrn, URL shapeFileUrl, Boolean canBeBackground, String operationType)
            throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkImportVariableElementsGeographicalInformation(variableUrn, shapeFileUrl, canBeBackground, null);
        Variable variable = retrieveVariableByUrn(variableUrn);
        SrmValidationUtils.checkVariableIsGeographical(variable);

        // Decide if task must be executed in background
        File file = null;
        try {
            file = new File(shapeFileUrl.toURI());
        } catch (Exception e) {
            logger.error("Error creating file", e);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_VARIABLE_ELEMENTS_GEO_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            String jobKey = null;
            if (SrmConstants.SHAPE_OPERATION_IMPORT_SHAPES.equals(operationType)) {
                jobKey = tasksMetamacService.plannifyImportVariableElementsShapeInBackground(ctx, variableUrn, shapeFileUrl);
            } else if (SrmConstants.SHAPE_OPERATION_IMPORT_POINTS.equals(operationType)) {
                jobKey = tasksMetamacService.plannifyImportVariableElementsPointsInBackground(ctx, variableUrn, shapeFileUrl);
            } else {
                throw new UnsupportedOperationException("Operation unsupported: " + operationType);
            }
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        // Retrieve variable elements
        List<VariableElement> variableElementsToPersist = new ArrayList<VariableElement>();
        Map<String, VariableElement> variableElementsByCode = new HashMap<String, VariableElement>();
        for (VariableElement variableElement : variable.getVariableElements()) {
            variableElementsByCode.put(variableElement.getIdentifiableArtefact().getCode(), variableElement);
        }

        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        String[] geometryTypes = null;
        if (SrmConstants.SHAPE_OPERATION_IMPORT_SHAPES.equals(operationType)) {
            geometryTypes = new String[]{SrmConstants.SHAPE_POLYGON, SrmConstants.SHAPE_MULTIPOLYGON};
        } else if (SrmConstants.SHAPE_OPERATION_IMPORT_POINTS.equals(operationType)) {
            geometryTypes = new String[]{SrmConstants.SHAPE_POINT};
        } else {
            throw new UnsupportedOperationException("Operation unsupported: " + operationType);
        }
        addVariableElementGeographicalInformationFromShapeFile(shapeFileUrl, variableElementsByCode, variableElementsToPersist, geometryTypes);
        // Persist
        for (VariableElement variableElement : variableElementsToPersist) {
            getVariableElementRepository().save(variableElement);
        }

        // Update global date of last update geographic information
        miscMetamacService.updateLastUpdatedVariableElementsGeographicalInformation(ctx, new DateTime());

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    /**
     * Adds geographical information of specific type. Modify existing variable element from map, and adds updated variable element to list to mark to execute save operation
     */
    private void addVariableElementGeographicalInformationFromShapeFile(URL shapeFileUrl, Map<String, VariableElement> variableElementsByCode, List<VariableElement> variableElementsToPersist,
            String... geometryTypes) throws MetamacException {

        DataStore dataStore = null;
        try {
            // Connection parameters
            Map<String, Serializable> datastoreParameters = new HashMap<String, Serializable>();
            datastoreParameters.put(ShapefileDataStoreFactory.URLP.key, shapeFileUrl);
            dataStore = DataStoreFinder.getDataStore(datastoreParameters);

            String[] typeNames = dataStore.getTypeNames();
            if (typeNames == null || typeNames.length == 0) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_SHAPE_ERROR_FILE_PARSING).withMessageParameters(shapeFileUrl).build();
            }
            String typeName = typeNames[0];

            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> iterator = collection.features();

            GeometryJSON geometryJSON = new GeometryJSON();

            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (!ArrayUtils.contains(geometryTypes, geometry.getGeometryType().toUpperCase())) {
                    continue;
                }

                // Extract code of variable element
                Object code = feature.getAttribute(SrmConstants.SHAPE_VARIABLE_ELEMENT_ATTRIBUTE);
                if (code == null) {
                    continue;
                }
                VariableElement variableElement = variableElementsByCode.get(code);
                if (variableElement == null) {
                    // ignore, do not throw exception
                    continue;
                }

                if (geometry instanceof MultiPolygon || geometry instanceof Polygon) {
                    variableElement.setShapeWkt(geometry.toText()); // well-known text format
                    variableElement.setShapeGeojson(geometryJSON.toString(geometry)); // geoJSON format
                    variableElementsToPersist.add(variableElement);
                } else if (geometry instanceof Point) {
                    Point point = (Point) geometry;
                    // Do not store well-known text to allow edit in web
                    variableElement.setLongitude(point.getX());
                    variableElement.setLatitude(point.getY());
                    variableElementsToPersist.add(variableElement);
                }
            }
            iterator.close();
        } catch (Exception e) {
            logger.error("Error importing shapefile", e);
            if (e instanceof MetamacException) {
                throw (MetamacException) e;
            } else {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_SHAPE_ERROR).withMessageParameters(e.getMessage()).build();
            }
        } finally {
            if (dataStore != null) {
                dataStore.dispose();
            }
        }
    }

    private CodelistVersionMetamac retrieveCodelistOriginalFromUrnTemporal(ServiceContext ctx, String urnTemporal) throws MetamacException {
        String urnOriginal = GeneratorUrnUtils.makeUrnFromTemporal(urnTemporal);
        return retrieveCodelistByUrn(ctx, urnOriginal);
    }

    private void setDefaultCodelistOrdenVisualisation(CodelistVersionMetamac temporaryCodelist, CodelistVersionMetamac codelist) {
        String defaultOrderVisualisationCode = temporaryCodelist.getDefaultOrderVisualisation() == null ? null : temporaryCodelist.getDefaultOrderVisualisation().getNameableArtefact().getCode();

        CodelistOrderVisualisation codelistOrderVisualisation = null;
        for (CodelistOrderVisualisation orderVisualisation : codelist.getOrderVisualisations()) {
            if (StringUtils.equals(defaultOrderVisualisationCode, orderVisualisation.getNameableArtefact().getCode())) {
                codelistOrderVisualisation = orderVisualisation;
            }
        }
        codelist.setDefaultOrderVisualisation(codelistOrderVisualisation);
    }

    private void setDefaultCodelistOpennessVisualisation(CodelistVersionMetamac temporaryCodelist, CodelistVersionMetamac codelist) {
        String defaultOpennessVisualisationCode = temporaryCodelist.getDefaultOpennessVisualisation() == null
                ? null
                : temporaryCodelist.getDefaultOpennessVisualisation().getNameableArtefact().getCode();

        CodelistOpennessVisualisation codelistOpennessVisualisation = null;
        for (CodelistOpennessVisualisation opennessVisualisation : codelist.getOpennessVisualisations()) {
            if (StringUtils.equals(defaultOpennessVisualisationCode, opennessVisualisation.getNameableArtefact().getCode())) {
                codelistOpennessVisualisation = opennessVisualisation;
            }
        }
        codelist.setDefaultOpennessVisualisation(codelistOpennessVisualisation);
    }
}
