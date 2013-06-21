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

import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
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
    public OrganisationSchemeVersionMetamac findByUrnForImportation(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<OrganisationSchemeVersionMetamac> result = findByQuery("from OrganisationSchemeVersionMetamac where (maintainableArtefact.urn = :urn OR maintainableArtefact.urnProvider = :urn) "
                + " AND maintainableArtefact.finalLogicClient is true AND (maintainableArtefact.isTemporal is null OR maintainableArtefact.isTemporal is false)", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public OrganisationSchemeVersionMetamac findByRefForImportation(String maintainer, String code, String version) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", code);
        parameters.put("version", version);
        String query = null;
        if (SdmxAlias.SDMX_MAINTAINER.equals(maintainer)) {
            query = "from OrganisationSchemeVersion where maintainableArtefact.maintainer is null and maintainableArtefact.code = :code and maintainableArtefact.versionLogic = :version"
                    + " AND maintainableArtefact.finalLogicClient is true AND (maintainableArtefact.isTemporal is null OR maintainableArtefact.isTemporal is false)";
        } else {
            parameters.put("maintainer", maintainer);
            query = "from OrganisationSchemeVersion where maintainableArtefact.maintainer.idAsMaintainer = :maintainer and maintainableArtefact.code = :code and maintainableArtefact.versionLogic = :version"
                    + " AND maintainableArtefact.finalLogicClient is true AND (maintainableArtefact.isTemporal is null OR maintainableArtefact.isTemporal is false)";
        }
        List<OrganisationSchemeVersionMetamac> result = findByQuery(query, parameters, 1);
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
    public void checkOrganisationSchemeVersionTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        organisationSchemeVersionRepository.checkOrganisationSchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // no metadata specific in metamac
    }

}
