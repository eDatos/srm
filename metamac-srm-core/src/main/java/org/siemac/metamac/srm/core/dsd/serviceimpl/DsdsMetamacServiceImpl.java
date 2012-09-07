package org.siemac.metamac.srm.core.dsd.serviceimpl;

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
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.serviceimpl.utils.DsdsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Structure;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

/**
 * Implementation of DsdsMetamacService.
 */
@Service("dsdsMetamacService")
public class DsdsMetamacServiceImpl extends DsdsMetamacServiceImplBase {

    @Autowired
    private DataStructureDefinitionService dataStructureDefinitionService;
    
    @Autowired
    private StructureVersionRepository structureVersionRepository;
    
    public DsdsMetamacServiceImpl() {
    }

    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkSendDataStructureDefinitionToProductionValidation(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DRAFT, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);

        // Validate to send to production
        checkDataStructureDefinitionToSendToProductionValidation(ctx, urn, dataStructureDefinitionVersionMetamac);

        // Update proc status
        dataStructureDefinitionVersionMetamac.setProcStatus(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);
        dataStructureDefinitionVersionMetamac.setProductionValidationDate(new DateTime());
        dataStructureDefinitionVersionMetamac.setProductionValidationUser(ctx.getUserId());
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) structureVersionRepository.save(dataStructureDefinitionVersionMetamac);

        return dataStructureDefinitionVersionMetamac;
    }

    public DataStructureDefinitionVersionMetamac sendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkSendDataStructureDefinitionToDiffusionValidation(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // Validate to send to diffusion
        checkDataStructureDefinitionToSendToDiffusionValidation(ctx, urn, dataStructureDefinitionVersionMetamac);

        // Update proc status
        dataStructureDefinitionVersionMetamac.setProcStatus(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);
        dataStructureDefinitionVersionMetamac.setDiffusionValidationDate(new DateTime());
        dataStructureDefinitionVersionMetamac.setDiffusionValidationUser(ctx.getUserId());
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) structureVersionRepository.save(dataStructureDefinitionVersionMetamac);

        return dataStructureDefinitionVersionMetamac;
    }

    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkRejectDataStructureDefinitionProductionValidation(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // Validate to reject
        checkDataStructureDefinitionToRejectProductionValidation(ctx, urn, dataStructureDefinitionVersionMetamac);

        // Update proc status
        dataStructureDefinitionVersionMetamac.setProcStatus(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);
        dataStructureDefinitionVersionMetamac.setProductionValidationDate(null);
        dataStructureDefinitionVersionMetamac.setProductionValidationUser(null);
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) structureVersionRepository.save(dataStructureDefinitionVersionMetamac);

        return dataStructureDefinitionVersionMetamac;
    }

    public DataStructureDefinitionVersionMetamac rejectDataStructureDefinitionDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkDataStructureDefinitionDiffusionValidation(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // Validate to reject
        checkDataStructureDefinitionToRejectDiffusionValidation(ctx, urn, dataStructureDefinitionVersionMetamac);

        // Update proc status
        dataStructureDefinitionVersionMetamac.setProcStatus(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);
        dataStructureDefinitionVersionMetamac.setProductionValidationDate(null);
        dataStructureDefinitionVersionMetamac.setProductionValidationUser(null);
        dataStructureDefinitionVersionMetamac.setDiffusionValidationDate(null);
        dataStructureDefinitionVersionMetamac.setDiffusionValidationUser(null);
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) structureVersionRepository.save(dataStructureDefinitionVersionMetamac);

        return dataStructureDefinitionVersionMetamac;
    }

    public DataStructureDefinitionVersionMetamac publishInternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.checkPublishInternallyDataStructureDefinition(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // Validate to publish internally
        checkDataStructureDefinitionToPublishInternally(ctx, urn, dataStructureDefinitionVersionMetamac);

        // Update proc status
        dataStructureDefinitionVersionMetamac.setProcStatus(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);
        dataStructureDefinitionVersionMetamac.setInternalPublicationDate(new DateTime());
        dataStructureDefinitionVersionMetamac.setInternalPublicationUser(ctx.getUserId());
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) structureVersionRepository.save(dataStructureDefinitionVersionMetamac);
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.markDataStructureAsFinal(ctx, urn);

        return dataStructureDefinitionVersionMetamac;
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    public DataStructureDefinitionVersionMetamac publishExternallyDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkPublishExternallyConceptScheme(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);

        // Validate to publish externally
        checkDataStructureDefinitionToPublishExternally(ctx, urn, dataStructureDefinitionVersionMetamac);

        // Start concept scheme validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.startDataStructureDefinitionValidity(ctx, urn);

        // Update proc status
        dataStructureDefinitionVersionMetamac.setProcStatus(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);
        dataStructureDefinitionVersionMetamac.setExternalPublicationDate(new DateTime());
        dataStructureDefinitionVersionMetamac.setExternalPublicationUser(ctx.getUserId());
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) structureVersionRepository.save(dataStructureDefinitionVersionMetamac);

        // Fill validTo in previous internally published versions
        List<DataStructureDefinitionVersionMetamac> versionsExternallyPublished = findDataStructureVersionsOfDataStructureInProcStatus(ctx, dataStructureDefinitionVersionMetamac.getStructure(),
                ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);
        for (DataStructureDefinitionVersionMetamac versionExternallyPublished : versionsExternallyPublished) {
            if (versionExternallyPublished.getId().equals(dataStructureDefinitionVersionMetamac.getId())) {
                continue;
            }
            versionExternallyPublished.getMaintainableArtefact().setValidTo(dataStructureDefinitionVersionMetamac.getExternalPublicationDate());
            structureVersionRepository.save(versionExternallyPublished);
        }

        return dataStructureDefinitionVersionMetamac;
    }

    public void deleteDataStructureDefinitionVersionMetamac(ServiceContext ctx, String urn) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("deleteDataStructureDefinitionVersionMetamac not implemented");
        
        // Delete related concepts TODO intentar borrar más eficientemente
//        DataStructureDefinitionVersion dataStructureDefinitionVersion = retrieveDataStructureDefinitionByUrn(ctx, urn);
//        List<ConceptRelation> relatedConceptsAllConceptSchemeVersion = findRelatedConceptsByConceptSchemeVersion(conceptSchemeVersion.getMaintainableArtefact().getUrn());
//        for (ConceptRelation relatedConcept : relatedConceptsAllConceptSchemeVersion) {
//            getConceptRelationRepository().delete(relatedConcept);
//        }
//
//        // Note: ConceptsService checks conceptScheme isn't final
//        conceptsService.deleteConceptScheme(ctx, urn);

    }

    public DataStructureDefinitionVersionMetamac versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("versioningDataStructureDefinition not implemented");

    }

    public DataStructureDefinitionVersionMetamac cancelDataStructureDefinitionVersionMetamacValidity(ServiceContext ctx, String urn) throws MetamacException {
        
        // Validation
        ConceptsMetamacInvocationValidator.checkCancelConceptSchemeValidity(urn, null);

        // Retrieve version in specific procStatus
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = retrieveDataStructureDefinitionVersionByProcStatus(ctx, urn, ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);

        // Cancel validity
        dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, urn);

        return dataStructureDefinitionVersionMetamac;
    }

    @Override
    public DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (DataStructureDefinitionVersionMetamac)dataStructureDefinitionService.retrieveDataStructureDefinitionByUrn(ctx, urn);
    }

    @Override
    public List<DataStructureDefinitionVersionMetamac> retrieveDataStructureDefinitionVersions(ServiceContext ctx, String urn) throws MetamacException {
        
        // Retrieve conceptSchemeVersions
        List<DataStructureDefinitionVersion> dataStructureDefinitionVersionVersions = dataStructureDefinitionService.retrieveDataStructureDefinitionVersions(ctx, urn);

        // Type cast to ConceptSchemeVersionMetamac
        List<DataStructureDefinitionVersionMetamac> dataStructureVersionMetamacs = new ArrayList<DataStructureDefinitionVersionMetamac>();
        for (DataStructureDefinitionVersion dataStructureDefinitionVersion : dataStructureDefinitionVersionVersions) {
            dataStructureVersionMetamacs.add((DataStructureDefinitionVersionMetamac) dataStructureDefinitionVersion);
        }

        return dataStructureVersionMetamacs;
    }

    @Override
    public PagedResult<DataStructureDefinitionVersionMetamac> findDataStructureDefinitionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    /**************************************************************************
     * PRIVATE
     *************************************************************************/

    /**
     * Makes validations to sent to production validation
     */
    private void checkDataStructureDefinitionToSendToProductionValidation(ServiceContext ctx, String urn, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkDatastructureDefinitionProcStatus(dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum.DRAFT, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);

        // Check other conditions
        checkConditionsSinceSendToProductionValidation(dataStructureDefinitionVersionMetamac, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**
     * Makes validations to sent to diffusion validation
     */
    private void checkDataStructureDefinitionToSendToDiffusionValidation(ServiceContext ctx, String urn, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkDatastructureDefinitionProcStatus(dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // Check other conditions
        checkConditionsSinceSendToDiffusionValidation(dataStructureDefinitionVersionMetamac, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**
     * Makes validations to reject production validation
     */
    private void checkDataStructureDefinitionToRejectProductionValidation(ServiceContext ctx, String urn, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkDatastructureDefinitionProcStatus(dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }
    
    
    /**
     * Makes validations to reject diffusion validation
     */
    private void checkDataStructureDefinitionToRejectDiffusionValidation(ServiceContext ctx, String urn, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkDatastructureDefinitionProcStatus(dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**
     * Makes validations to publish internally
     */
    private void checkDataStructureDefinitionToPublishInternally(ServiceContext ctx, String urn, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkDatastructureDefinitionProcStatus(dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);

        // Check other conditions
        checkConditionsSincePublishInternally(dataStructureDefinitionVersionMetamac, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    /**
     * Makes validations to publish externally
     */
    private void checkDataStructureDefinitionToPublishExternally(ServiceContext ctx, String urn, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkDatastructureDefinitionProcStatus(dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);

        // Check other conditions
        checkConditionsSincePublishExternally(dataStructureDefinitionVersionMetamac, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    private void checkDatastructureDefinitionProcStatus(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, ItemSchemeMetamacProcStatusEnum... procStatus) throws MetamacException {
        if (!ArrayUtils.contains(procStatus, dataStructureDefinitionVersionMetamac.getProcStatus())) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS)
                    .withMessageParameters(dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), procStatusString).build();
        }
    }
    
    private void checkConditionsSinceSendToProductionValidation(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) {
        // Metadata required
    }

    
    private void checkConditionsSinceSendToDiffusionValidation(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToProductionValidation(dataStructureDefinitionVersionMetamac, exceptions);
    }
    
    // TODO metadato 'roles' de sdmx: En principio se podrán asociar independientemente del estado del esquema de conceptos, pero al publicar internamente se ha de comprobar que el esquema de
    // conceptos relacionado tb
    // esté publicado internamente. Idem al publicar externamente. Está pendiente que Alberto confirme que se podrán asociar en un principio aunque estén en borrador.
    private void checkConditionsSincePublishInternally(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToDiffusionValidation(dataStructureDefinitionVersionMetamac, exceptions);
    }
    
    // TODO metadato 'roles' de sdmx:En principio se podrán asociar independientemente del estado del esquema de conceptos, pero al publicar internamente se ha de comprobar que el esquema de conceptos
    // relacionado tb
    // esté publicado internamente. Idem al publicar externamente. Está pendiente que Alberto confirme que se podrán asociar en un principio aunque estén en borrador.
    private void checkConditionsSincePublishExternally(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac, List<MetamacExceptionItem> exceptions) {
        checkConditionsSincePublishInternally(dataStructureDefinitionVersionMetamac, exceptions);
    }
    
    /**
     * Retrieves version of a data structure definition in specific procStatus
     */
    private DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionVersionByProcStatus(ServiceContext ctx, String urn, ItemSchemeMetamacProcStatusEnum... procStatus)
            throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                .withProperty(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn()).eq(urn).withProperty(DataStructureDefinitionVersionMetamacProperties.procStatus())
                .in((Object[]) procStatus).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacPagedResult = getDataStructureDefinitionVersionMetamacRepository().findByCondition(conditions,
                pagingParameter);

        if (dataStructureDefinitionVersionMetamacPagedResult.getValues().size() != 1) {
            // check concept scheme exists to throws specific exception
            retrieveDataStructureDefinitionByUrn(ctx, urn);

            // if exists, throw exception about wrong proc status
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
        return dataStructureDefinitionVersionMetamacPagedResult.getValues().get(0);
    }

    /**
     * Finds versions of data structure definition in specific procStatus
     */
    private List<DataStructureDefinitionVersionMetamac> findDataStructureVersionsOfDataStructureInProcStatus(ServiceContext ctx, Structure dataStructureDefinition, ItemSchemeMetamacProcStatusEnum... procStatus)
            throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class).withProperty(DataStructureDefinitionVersionMetamacProperties.structure().id())
                .eq(dataStructureDefinition.getId()).withProperty(DataStructureDefinitionVersionMetamacProperties.procStatus()).in((Object[]) procStatus).distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.noLimits();
        PagedResult<DataStructureDefinitionVersionMetamac> dataStructureVersionPagedResult = getDataStructureDefinitionVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return dataStructureVersionPagedResult.getValues();
    }
}
