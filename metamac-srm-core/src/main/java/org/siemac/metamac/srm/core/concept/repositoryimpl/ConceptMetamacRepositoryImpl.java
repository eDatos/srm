package org.siemac.metamac.srm.core.concept.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.withoutTranslation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;

/**
 * Repository implementation for ConceptMetamac
 */
@Repository("conceptMetamacRepository")
public class ConceptMetamacRepositoryImpl extends ConceptMetamacRepositoryBase {

    @Autowired
    private ConceptRepository conceptRepository;

    @Autowired
    private ItemRepository    itemRepository;

    public ConceptMetamacRepositoryImpl() {
    }

    @Override
    public ConceptMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptMetamac> result = findByQuery("from ConceptMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<ItemVisualisationResult> findConceptsByConceptSchemeUnorderedToVisualisation(Long conceptSchemeVersionId, String locale) throws MetamacException {
        return itemRepository.findItemsByItemSchemeUnorderedToVisualisation(conceptSchemeVersionId, locale);
    }

    @Override
    public void checkConceptTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        conceptRepository.checkConceptTranslations(itemSchemeVersionId, locale, exceptionItems);
        checkConceptMetamacTranslations(itemSchemeVersionId, locale, exceptionItems);
    }

    /**
     * Checks concrete metadata of ConceptMetamac
     */
    @SuppressWarnings("rawtypes")
    private void checkConceptMetamacTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.CODE, ");
        sb.append("c.PLURAL_NAME_FK as PLURAL_NAME_IS, lsplurn.LABEL as PLURAL_NAME_LABEL, ");
        sb.append("c.ACRONYM_FK as ACRONYM_IS, lsacron.LABEL as ACRONYM_LABEL, ");
        sb.append("c.DESCRIPTION_SOURCE_FK as DESCRIPTION_SOURCE_IS, lsdescs.LABEL as DESCRITION_SOURCE_LABEL, ");
        sb.append("c.CONTEXT_FK as CONTEXT_IS, lsconte.LABEL as CONTEXT_LABEL, ");
        sb.append("c.DOC_METHOD_FK as DOC_METHOD_IS, lsdocme.LABEL as DOC_METHOD_LABEL, ");
        sb.append("c.DERIVATION_FK as DERIVARION_IS, lsderiv.LABEL as DERIVARION_LABEL, ");
        sb.append("c.LEGAL_ACTS_FK as LEGAL_ACTS_IS, lslegal.LABEL as LEGAL_ACTS_LABEL ");
        sb.append("FROM TB_ITEMS_BASE i ");
        sb.append("INNER JOIN TB_M_CONCEPTS c on c.TB_CONCEPTS = i.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsplurn on lsplurn.INTERNATIONAL_STRING_FK = c.PLURAL_NAME_FK AND lsplurn.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsacron on lsacron.INTERNATIONAL_STRING_FK = c.ACRONYM_FK AND lsacron.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsdescs on lsdescs.INTERNATIONAL_STRING_FK = c.DESCRIPTION_SOURCE_FK AND lsdescs.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsconte on lsconte.INTERNATIONAL_STRING_FK = c.CONTEXT_FK AND lsconte.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsdocme on lsdocme.INTERNATIONAL_STRING_FK = c.DOC_METHOD_FK AND lsdocme.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsderiv on lsderiv.INTERNATIONAL_STRING_FK = c.DERIVATION_FK AND lsderiv.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lslegal on lslegal.INTERNATIONAL_STRING_FK = c.LEGAL_ACTS_FK AND lslegal.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("i.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND (");
        sb.append("       (c.PLURAL_NAME_FK IS NOT NULL AND lsplurn.LABEL IS NULL) ");
        sb.append("    OR (c.ACRONYM_FK IS NOT NULL AND lsacron.LABEL IS NULL) ");
        sb.append("    OR (c.DESCRIPTION_SOURCE_FK IS NOT NULL AND lsdescs.LABEL IS NULL) ");
        sb.append("    OR (c.CONTEXT_FK IS NOT NULL AND lsconte.LABEL IS NULL) ");
        sb.append("    OR (c.ACRONYM_FK IS NOT NULL AND lsacron.LABEL IS NULL) ");
        sb.append("    OR (c.DOC_METHOD_FK IS NOT NULL AND lsdocme.LABEL IS NULL) ");
        sb.append("    OR (c.DOC_METHOD_FK IS NOT NULL AND lsdocme.LABEL IS NULL) ");
        sb.append("    OR (c.DERIVATION_FK IS NOT NULL AND lsderiv.LABEL IS NULL) ");
        sb.append("    OR (c.LEGAL_ACTS_FK IS NOT NULL AND lslegal.LABEL IS NULL) ");
        sb.append(") ");
        sb.append("ORDER BY a.CODE ASC");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();

        for (Object resultSql : resultsSql) {
            Object[] resultArray = (Object[]) resultSql;
            int i = 0;
            String conceptCode = getString(resultArray[i++]);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_PLURAL_NAME, resultArray, i++, i++, exceptionItems);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_ACRONYM, resultArray, i++, i++, exceptionItems);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE, resultArray, i++, i++, exceptionItems);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_CONTEXT, resultArray, i++, i++, exceptionItems);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_DOC_METHOD, resultArray, i++, i++, exceptionItems);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_DERIVATION, resultArray, i++, i++, exceptionItems);
            checkTranslation(conceptCode, ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, resultArray, i++, i++, exceptionItems);
        }
    }

    private void checkTranslation(String conceptCode, String metadataExceptionParameter, Object[] resultArray, int internationalStringIndex, int localisedStringIndex,
            List<MetamacExceptionItem> exceptionItems) {
        Long internationalStringName = getLong(resultArray[internationalStringIndex]);
        String localisedStringName = getString(resultArray[localisedStringIndex]);
        if (withoutTranslation(internationalStringName, localisedStringName)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, metadataExceptionParameter, conceptCode));
        }
    }
}