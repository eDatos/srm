package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import static org.siemac.metamac.srm.core.common.repository.utils.SrmRepositoryUtils.getLong;
import static org.siemac.metamac.srm.core.common.repository.utils.SrmRepositoryUtils.getString;
import static org.siemac.metamac.srm.core.common.repository.utils.SrmRepositoryUtils.withoutTranslation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for OrganisationMetamac
 */
@Repository("organisationMetamacRepository")
public class OrganisationMetamacRepositoryImpl extends OrganisationMetamacRepositoryBase {

    public OrganisationMetamacRepositoryImpl() {
    }

    @Override
    public OrganisationMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<OrganisationMetamac> result = findByQuery("from OrganisationMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    // TODO pasar al repositorio Item
    @Override
    public void checkOrganisationTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        // name, description and comment
        checkOrganisationNameableArtefactTranslations(itemSchemeVersionId, locale, exceptionItems);
        // annotations
        checkOrganisationAnnotationsTranslations(itemSchemeVersionId, locale, exceptionItems);
    }

    /**
     * Checks name, description and comment of MaintainableArtefact
     */
    @SuppressWarnings("rawtypes")
    private void checkOrganisationNameableArtefactTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.CODE, a.NAME_FK as IS_NAME, lsN.LABEL as NAME_LABEL, a.DESCRIPTION_FK as IS_DESCRIPTION, lsD.LABEL as DESCRIPTION_LABEL, a.COMMENT_FK as IS_COMMENT, lsC.LABEL as COMMENT_LABEL ");
        sb.append("FROM TB_ITEMS i ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsN on lsN.INTERNATIONAL_STRING_FK = a.NAME_FK and lsN.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsD on lsD.INTERNATIONAL_STRING_FK = a.DESCRIPTION_FK and lsD.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsC on lsC.INTERNATIONAL_STRING_FK = a.COMMENT_FK and lsC.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("i.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND (a.NAME_FK IS NOT NULL OR a.DESCRIPTION_FK IS NOT NULL OR a.COMMENT_FK IS NOT NULL) ");
        sb.append("AND (lsN.LABEL IS NULL OR lsD.LABEL IS NULL OR lsC.LABEL IS NULL) ");
        sb.append("ORDER BY a.CODE ASC");

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
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME, code));
            }
            if (withoutTranslation(internationalStringDescription, localisedStringDescription)) {
                exceptionItems
                        .add(new MetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION, code));
            }
            if (withoutTranslation(internationalStringComment, localisedStringComment)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT, code));
            }
        }
    }

    /**
     * Checks text of Annotations
     */
    @SuppressWarnings("rawtypes")
    private void checkOrganisationAnnotationsTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.CODE) ");
        sb.append("FROM TB_ITEMS i ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("INNER JOIN TB_ANNOTATIONS ann on ann.ANNOTABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ann.TEXT_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("i.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND ann.TEXT_FK IS NOT NULL ");
        sb.append("AND ls.LABEL IS NULL ");
        sb.append("ORDER BY a.CODE ASC");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String code = getString(resultSql);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, code));
        }
    }
}
