package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersionRepository;

/**
 * Repository implementation for OrganisationSchemeVersionMetamac
 */
@Repository("organisationSchemeVersionMetamacRepository")
public class OrganisationSchemeVersionMetamacRepositoryImpl extends OrganisationSchemeVersionMetamacRepositoryBase {

    @Autowired
    private OrganisationSchemeVersionRepository organisationSchemeVersionRepository;

    public OrganisationSchemeVersionMetamacRepositoryImpl() {
    }

    @Override
    public OrganisationSchemeVersionMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<OrganisationSchemeVersionMetamac> result = findByQuery("from OrganisationSchemeVersionMetamac where maintainableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public OrganisationSchemeVersionMetamac retrieveOrganisationSchemeVersionByProcStatus(String urn, ProcStatusEnum[] procStatusArray) throws MetamacException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        parameters.put("procStatus", SrmServiceUtils.procStatusEnumToList(procStatusArray));
        List<OrganisationSchemeVersionMetamac> result = findByQuery("from OrganisationSchemeVersionMetamac where maintainableArtefact.urn = :urn and lifeCycleMetadata.procStatus in (:procStatus)",
                parameters, 1);
        if (result == null || result.isEmpty()) {
            // check organisation scheme exists to throws specific exception
            OrganisationSchemeVersionMetamac organisationSchemeVersion = findByUrn(urn);
            if (organisationSchemeVersion == null) {
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
    public OrganisationSchemeVersionMetamac findByOrganisation(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<OrganisationSchemeVersionMetamac> result = findByQuery("select os from OrganisationSchemeVersionMetamac os join os.items as i where i.nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public void checkOrganisationSchemeVersionTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        organisationSchemeVersionRepository.checkOrganisationSchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItems);
        // no metadata specific in metamac
    }

}
