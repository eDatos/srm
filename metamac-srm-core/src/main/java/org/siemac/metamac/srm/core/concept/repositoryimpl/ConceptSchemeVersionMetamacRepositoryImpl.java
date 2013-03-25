package org.siemac.metamac.srm.core.concept.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersionRepository;

/**
 * Repository implementation for ConceptSchemeVersionMetamac
 */
@Repository("conceptSchemeVersionMetamacRepository")
public class ConceptSchemeVersionMetamacRepositoryImpl extends ConceptSchemeVersionMetamacRepositoryBase {

    @Autowired
    private ConceptSchemeVersionRepository conceptSchemeVersionRepository;

    public ConceptSchemeVersionMetamacRepositoryImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac findByConcept(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptSchemeVersionMetamac> result = findByQuery("select cs from ConceptSchemeVersionMetamac cs join cs.items as i where i.nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public ConceptSchemeVersionMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptSchemeVersionMetamac> result = findByQuery("from ConceptSchemeVersionMetamac where maintainableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeVersionByProcStatus(String urn, ProcStatusEnum[] procStatusArray) throws MetamacException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        parameters.put("procStatus", SrmServiceUtils.procStatusEnumToList(procStatusArray));
        List<ConceptSchemeVersionMetamac> result = findByQuery("from ConceptSchemeVersionMetamac where maintainableArtefact.urn = :urn and lifeCycleMetadata.procStatus in (:procStatus)", parameters,
                1);
        if (result == null || result.isEmpty()) {
            // check concept scheme exists to throws specific exception
            ConceptSchemeVersionMetamac conceptSchemeVersion = findByUrn(urn);
            if (conceptSchemeVersion == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
            } else {
                // if exists, throw exception about wrong proc status
                String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatusArray);
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();

            }
        }
        return result.get(0);
    }

    @Override
    public void checkConceptSchemeVersionTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        conceptSchemeVersionRepository.checkConceptSchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItems);
        // no metadata specific in metamac
    }

}
