package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.booleanToBooleanDatabase;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addTranslationExceptionToExceptionItemsByResource;
import static org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils.addExceptionToExceptionItemsByResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersionRepository;

/**
 * Repository implementation for CodelistVersionMetamac
 */
@Repository("codelistVersionMetamacRepository")
public class CodelistVersionMetamacRepositoryImpl extends CodelistVersionMetamacRepositoryBase {

    @Autowired
    private CodelistVersionRepository codelistVersionRepository;

    public CodelistVersionMetamacRepositoryImpl() {
    }

    @Override
    public CodelistVersionMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistVersionMetamac> result = findByQuery("from CodelistVersionMetamac where maintainableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public CodelistVersionMetamac retrieveCodelistVersionByProcStatus(String urn, ProcStatusEnum[] procStatusArray) throws MetamacException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        parameters.put("procStatus", SrmServiceUtils.procStatusEnumToList(procStatusArray));
        List<CodelistVersionMetamac> result = findByQuery("from CodelistVersionMetamac where maintainableArtefact.urn = :urn and lifeCycleMetadata.procStatus in (:procStatus)", parameters, 1);
        if (result == null || result.isEmpty()) {
            // check codelist exists to throws specific exception
            CodelistVersionMetamac codelistVersion = findByUrn(urn);
            if (codelistVersion == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
            } else {
                // if exists, throw exception about wrong proc status
                String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatusArray);
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();

            }
        }
        return result.get(0);
    }

    /**
     * Find a codelist by one of its codes (items)
     */
    @Override
    public CodelistVersionMetamac findByCode(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistVersionMetamac> result = findByQuery("select cl from CodelistVersionMetamac cl join cl.items as i where i.nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public void checkCodelistVersionTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        // item scheme common metadata
        codelistVersionRepository.checkCodelistVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // concrete metadata
        checkCodelistVersionMetamacTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void checkCodelistWithReplaceToExternallyPublished(Long itemSchemeVersionId, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(aisv.URN) ");
        sb.append("FROM TB_M_CODELISTS_VERSIONS c ");
        sb.append("INNER JOIN TB_ITEM_SCHEMES_VERSIONS isvb ON isvb.ID = c.TB_CODELISTS_VERSIONS ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv ON aisv.ID = isvb.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("WHERE c.REPLACED_BY_CODELIST_FK = :itemSchemeVersionId ");
        sb.append("AND aisv.PUBLIC_LOGIC != " + booleanToBooleanDatabase(true));

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urnRelatedResource = getString(resultSql);
            addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CODELIST_NOT_EXTERNALLY_PUBLISHED, urnRelatedResource);
        }
    }

    /**
     * Checks text of Codelist Metamac concrete metadata
     */
    @SuppressWarnings("rawtypes")
    private void checkCodelistVersionMetamacTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.URN) ");
        sb.append("FROM TB_ITEM_SCHEMES_VERSIONS iv ");
        sb.append("INNER JOIN TB_M_CODELISTS_VERSIONS cv on TB_CODELISTS_VERSIONS = iv.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on iv.MAINTAINABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = cv.SHORT_NAME_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("iv.ID = :itemSchemeVersionId ");
        sb.append("AND cv.SHORT_NAME_FK IS NOT NULL AND ls.LABEL IS NULL ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urn = getString(resultSql);
            addTranslationExceptionToExceptionItemsByResource(exceptionItemsByUrn, urn, ServiceExceptionParameters.CODELIST_SHORT_NAME);
        }
    }
}
