package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import static org.siemac.metamac.srm.core.common.repository.utils.SrmRepositoryUtils.getLong;
import static org.siemac.metamac.srm.core.common.repository.utils.SrmRepositoryUtils.getString;
import static org.siemac.metamac.srm.core.common.repository.utils.SrmRepositoryUtils.withoutTranslation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for OrganisationSchemeVersionMetamac
 */
@Repository("organisationSchemeVersionMetamacRepository")
public class OrganisationSchemeVersionMetamacRepositoryImpl extends OrganisationSchemeVersionMetamacRepositoryBase {

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

    // TODO pasar al repositorio ItemSchemeVersion
    @Override
    public void checkOrganisationSchemeVersionTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        // name, description and comment
        checkOrganisationSchemeVersionMaintainableArtefactTranslations(itemSchemeVersionId, locale, exceptionItems);
        // annotations
        checkOrganisationSchemeVersionAnnotationsTranslations(itemSchemeVersionId, locale, exceptionItems);
    }

    /**
     * Checks name, description and comment of MaintainableArtefact
     */
    @SuppressWarnings("rawtypes")
    private void checkOrganisationSchemeVersionMaintainableArtefactTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.CODE, a.NAME_FK as IS_NAME, lsN.LABEL as NAME_LABEL, a.DESCRIPTION_FK as IS_DESCRIPTION, lsD.LABEL as DESCRIPTION_LABEL, a.COMMENT_FK as IS_COMMENT, lsC.LABEL as COMMENT_LABEL ");
        sb.append("FROM TB_ITEM_SCHEMES_VERSIONS iv ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on iv.MAINTAINABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsN on lsN.INTERNATIONAL_STRING_FK = a.NAME_FK and lsN.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsD on lsD.INTERNATIONAL_STRING_FK = a.DESCRIPTION_FK and lsD.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsC on lsC.INTERNATIONAL_STRING_FK = a.COMMENT_FK and lsC.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("iv.ID = :itemSchemeVersionId ");
        sb.append("AND (a.NAME_FK IS NOT NULL OR a.DESCRIPTION_FK IS NOT NULL OR a.COMMENT_FK IS NOT NULL) ");
        sb.append("AND (lsN.LABEL IS NULL OR lsD.LABEL IS NULL OR lsC.LABEL IS NULL)");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();

        for (Object resultSql : resultsSql) {
            Object[] resultArray = (Object[]) resultSql;
            int i = 0;
            String code = getString(resultArray[i++]);
            Long internationalStringName = getLong(resultArray[i++]);
            String localisedStringName = getString(resultArray[i++]);
            Long internationalStringDescription = getLong(resultArray[i++]);
            String localisedStringDescription = getString(resultArray[i++]);
            Long internationalStringComment = getLong(resultArray[i++]);
            String localisedStringComment = getString(resultArray[i++]);
            if (withoutTranslation(internationalStringName, localisedStringName)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME, code));
            }
            if (withoutTranslation(internationalStringDescription, localisedStringDescription)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION,
                        code));
            }
            if (withoutTranslation(internationalStringComment, localisedStringComment)) {
                exceptionItems
                        .add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT, code));
            }
        }
    }

    /**
     * Checks text of Annotations
     */
    @SuppressWarnings("rawtypes")
    private void checkOrganisationSchemeVersionAnnotationsTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.CODE) ");
        sb.append("FROM TB_ITEM_SCHEMES_VERSIONS iv ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on iv.MAINTAINABLE_ARTEFACT_FK = a.ID ");
        sb.append("INNER JOIN TB_ANNOTATIONS ann on ann.ANNOTABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ann.TEXT_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("iv.ID = :itemSchemeVersionId ");
        sb.append("AND ann.TEXT_FK IS NOT NULL ");
        sb.append("AND ls.LABEL IS NULL ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        if (!CollectionUtils.isEmpty(resultsSql)) {
            String code = getString(resultsSql.get(0));
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_SCHEME_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, code));
        }
    }

}
