package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addTranslationExceptionToExceptionItemsByResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodelistOpennessVisualisation
 */
@Repository("codelistOpennessVisualisationRepository")
public class CodelistOpennessVisualisationRepositoryImpl extends CodelistOpennessVisualisationRepositoryBase {

    public CodelistOpennessVisualisationRepositoryImpl() {
    }

    @Override
    public CodelistOpennessVisualisation findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistOpennessVisualisation> result = findByQuery("from CodelistOpennessVisualisation where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public CodelistOpennessVisualisation findByCode(Long codelistId, String opennessVisualisationCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("codelistId", codelistId);
        parameters.put("opennessVisualisationCode", opennessVisualisationCode);
        List<CodelistOpennessVisualisation> result = findByQuery("from CodelistOpennessVisualisation where nameableArtefact.code = :opennessVisualisationCode and codelistVersion.id = :codelistId",
                parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void updateUrnAllCodelistOpennessVisualisationsByCodelistEfficiently(CodelistVersionMetamac codelistVersionMetamac, String oldUrnExample) {

        // Extract versionable Substring
        String replaceText = GeneratorUrnUtils.extractVersionableArtefactFragment(codelistVersionMetamac.getMaintainableArtefact().getUrn());
        String oldText = GeneratorUrnUtils.extractVersionableArtefactFragment(oldUrnExample);

        StringBuilder sb = new StringBuilder();
        sb.append("update TB_ANNOTABLE_ARTEFACTS ");
        sb.append("set URN=replace(urn, '").append(oldText).append("', '").append(replaceText).append("') ");
        sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
        sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_codelist_openness_vis ov where ov.codelist_fk = :codelistVersion))");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("codelistVersion", codelistVersionMetamac.getId());
        query.executeUpdate();

        if (!codelistVersionMetamac.getMaintainableArtefact().getIsImported()) {
            sb = new StringBuilder();
            sb.append("update TB_ANNOTABLE_ARTEFACTS ");
            sb.append("set URN_PROVIDER=URN ");
            sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
            sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_codelist_openness_vis ov where ov.codelist_fk = :codelistVersion))");

            query = getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("codelistVersion", codelistVersionMetamac.getId());
            query.executeUpdate();
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void checkCodelistOpennessVisualisationTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.URN) ");
        sb.append("FROM TB_M_CODELIST_OPENNESS_VIS ov ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ov.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("ov.CODELIST_FK = :itemSchemeVersionId ");
        sb.append("AND a.NAME_FK IS NOT NULL AND ls.LABEL IS NULL ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urn = getString(resultSql);
            addTranslationExceptionToExceptionItemsByResource(exceptionItemsByUrn, urn, ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME);
        }
    }
}
