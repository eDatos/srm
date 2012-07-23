package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.domain.srm.enume.domain.MaintainableProcStatusEnum;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeRepository;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeVersion;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeVersionRepository;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
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

    // TODO metadatos propios de conceptos
    // TODO validación metadatos
    // TODO ciclo de vida
    // TODO revisar todos los metadatos de MaintainableArtefact
    @Override
    public ConceptSchemeVersion createConceptScheme(ServiceContext ctx, ConceptSchemeVersion conceptSchemeVersion) throws MetamacException {

        ItemScheme conceptScheme = conceptSchemeVersion.getItemScheme();
        MaintainableArtefact maintainableArtefact = conceptSchemeVersion.getMaintainableArtefact();

        // Validation
        ConceptsInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);
        validateConceptSchemeUnique(ctx, maintainableArtefact.getMaintainer(), maintainableArtefact.getIdLogic(), null);

        // Fill metadata
        // Save conceptScheme
        conceptScheme = getItemSchemeRepository().save(conceptScheme);

        // Save draft version
        maintainableArtefact.setProcStatus(MaintainableProcStatusEnum.DRAFT);
        maintainableArtefact.setIsLastVersion(Boolean.TRUE);
        maintainableArtefact.setFinalLogic(Boolean.FALSE);
        maintainableArtefact.setVersionLogic(VersionUtil.createNextVersionTag(null, true));
        maintainableArtefact.setUrn(GeneratorUrnUtils.generateSdmxConceptSchemeUrn(maintainableArtefact.getMaintainer().getCode(), maintainableArtefact.getIdLogic(),
                maintainableArtefact.getVersionLogic()));
        maintainableArtefact.setValidFrom(null);
        maintainableArtefact.setValidTo(null);
        maintainableArtefact.setReplacedBy(null);
        maintainableArtefact.setReplaceTo(null);
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
        if (!MaintainableProcStatusEnum.DRAFT.equals(conceptSchemeVersion.getMaintainableArtefact().getProcStatus())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS)
                    .withMessageParameters(urn, new String[]{ServiceExceptionParameters.PROC_STATUS_DRAFT}).build();
        }
        // Delete whole concept scheme or only last version
        if (VersionUtil.VERSION_LOGIC_INITIAL_VERSION.equals(conceptSchemeVersion.getMaintainableArtefact().getVersionLogic())) {
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
    // // TODO: Validate code unique: organization, id_logic, version?
    // // validateConceptSchemeUnique(ctx, entity.getItemScheme().getIdLogic(), null);
    //
    // // TODO actualizar urn y de los hijos, si cambia el code
    // return conceptSchemeRepository.save(entity);
    //
    // }
    //
    // public void deleteConceptScheme(ServiceContext ctx, Long id) throws MetamacException {
    //
    // ConceptsInvocationValidator.checkDeleteConceptScheme(id, null);
    //
    // ConceptScheme conceptScheme = findConceptSchemeById(ctx, id);
    //
    // // TODO: Check if the conceptScheme can be deleted.
    // // We have to check if delete whole conceptScheme or only the last version
    // conceptSchemeRepository.delete(conceptScheme);
    //
    // }
    //
    // // ------------------------------------------------------------------------------------
    // // CONCEPTS
    // // ------------------------------------------------------------------------------------
    //
    // public Concept findConceptById(ServiceContext ctx, Long id) throws MetamacException {
    //
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("findConceptById not implemented");
    //
    // }
    //
    // public Concept createConcept(ServiceContext ctx, Long conceptSchemeId, ConceptScheme entity) throws MetamacException {
    //
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("createConcept not implemented");
    //
    // }
    //
    // public Concept updateConcept(ServiceContext ctx, ConceptScheme entity) throws MetamacException {
    //
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("updateConcept not implemented");
    //
    // }
    //
    // public void deleteConcept(ServiceContext ctx, Long id) throws MetamacException {
    //
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("deleteConcept not implemented");
    //
    // }
    //
    // public List<Concept> findConceptSchemeConcepts(ServiceContext ctx, Long conceptSchemeId) throws MetamacException {
    //
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("findConceptSchemeConcepts not implemented");
    //
    // }
    //

    // TODO
    private void validateConceptSchemeUnique(ServiceContext ctx, ExternalItem maintainer, String idLogic, Long conceptSchemeId) throws MetamacException {
        // List<ConditionalCriteria> conditions =
        // ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersion.class).withProperty(ConceptSchemeVersionProperties.maintainableArtefact().maintainer().code())
        // .eq(maintainer.getCode()).not().withProperty(ConceptSchemeVersionProperties.itemScheme().id()).eq(conceptSchemeId).withProperty(ConceptSchemeVersionProperties.maintainableArtefact().idLogic()).ignoreCaseEq(idLogic)
        // .distinctRoot().build();
        //
        // List<ConceptScheme> conceptSchemes = findConceptSchemeByCondition(ctx, conditions);
        //
        // if (conceptSchemes != null && conceptSchemes.size() != 0) {
        // throw new MetamacException(MetamacCoreExceptionType.CONCPET_SCHEME_ALREADY_EXIST_ID_LOGIC_DUPLICATED, idLogic, maintainer.getCode());
        // }
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

}
