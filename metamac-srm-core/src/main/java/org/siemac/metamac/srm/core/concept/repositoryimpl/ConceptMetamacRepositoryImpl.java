package org.siemac.metamac.srm.core.concept.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDate;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.withoutTranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.RelatedResourceVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;

/**
 * Repository implementation for ConceptMetamac
 */
@Repository("conceptMetamacRepository")
public class ConceptMetamacRepositoryImpl extends ConceptMetamacRepositoryBase {

    @Autowired
    private ItemRepository      itemRepository;

    @Autowired
    private ConceptRepository   conceptRepository;

    private static final String COLUMN_NAME_PLURAL_NAME        = "PLURAL_NAME_FK";
    private static final String COLUMN_NAME_ACRONYM            = "ACRONYM_FK";
    private static final String COLUMN_NAME_DESCRIPTION_SOURCE = "DESCRIPTION_SOURCE_FK";
    private static final String COLUMN_NAME_CONTEXT            = "CONTEXT_FK";
    private static final String COLUMN_NAME_DOC_METHOD         = "DOC_METHOD_FK";
    private static final String COLUMN_NAME_DERIVATION         = "DERIVATION_FK";
    private static final String COLUMN_NAME_LEGAL_ACTS         = "LEGAL_ACTS_FK";

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

    @SuppressWarnings("rawtypes")
    @Override
    public List<ConceptMetamacVisualisationResult> findConceptsByConceptSchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {
        // Find items. Returns only one row by item. NOTE: this query return null label if locale not exits for a code.
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT i.ID as ITEM_ID, i.CREATED_DATE, i.CREATED_DATE_TZ, a.URN, a.CODE, cb.PARENT_FK as ITEM_PARENT_ID, lsName.LABEL as NAME, lsAcronym.LABEL as ACRONYM, c.SDMX_RELATED_ARTEFACT ");
        sb.append("FROM TB_M_CONCEPTS c ");
        sb.append("INNER JOIN TB_CONCEPTS cb on c.TB_CONCEPTS = cb.TB_ITEMS_BASE ");
        sb.append("INNER JOIN TB_ITEMS_BASE i on c.TB_CONCEPTS = i.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsName on lsName.INTERNATIONAL_STRING_FK = a.NAME_FK and lsName.locale = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsAcronym on lsAcronym.INTERNATIONAL_STRING_FK = c.ACRONYM_FK and lsAcronym.locale = :locale ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId");
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List itemsResultSql = query.getResultList();

        // Transform object[] results
        List<ConceptMetamacVisualisationResult> targets = new ArrayList<ConceptMetamacVisualisationResult>(itemsResultSql.size());
        Map<Long, ItemVisualisationResult> mapItemByItemId = new HashMap<Long, ItemVisualisationResult>(itemsResultSql.size());
        for (Object itemResultSql : itemsResultSql) {
            Object[] itemResultSqlArray = (Object[]) itemResultSql;
            ConceptMetamacVisualisationResult target = itemResultSqlToConceptVisualisationResult(itemResultSqlArray);
            targets.add(target);
            mapItemByItemId.put(target.getItemIdDatabase(), target);
        }

        // Add description. Execute one independent query to retrieve it is more efficient than do a global query
        itemRepository.executeQueryFillItemDescription(itemSchemeVersionId, Concept.class, locale, mapItemByItemId);

        // Variable . Execute one independent query to retrieve variable s is more efficient than do a global query
        StringBuilder sbVariables = new StringBuilder();
        sbVariables.append("SELECT c.TB_CONCEPTS as ITEM_ID, v.ID as V_ID, av.URN as V_URN, av.URN_PROVIDER as V_URN_PROVIDER, av.CODE as V_CODE, ls.LABEL as V_NAME ");
        sbVariables.append("FROM TB_M_CONCEPTS c ");
        sbVariables.append("INNER JOIN TB_CONCEPTS cb on c.TB_CONCEPTS = cb.TB_ITEMS_BASE ");
        sbVariables.append("INNER JOIN TB_M_VARIABLES v on v.ID = c.VARIABLE_FK ");
        sbVariables.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS av on v.NAMEABLE_ARTEFACT_FK = av.ID ");
        sbVariables.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = av.NAME_FK and ls.locale = :locale ");
        sbVariables.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        Query queryVariables = getEntityManager().createNativeQuery(sbVariables.toString());
        queryVariables.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        queryVariables.setParameter("locale", locale);
        List variablesResultSql = queryVariables.getResultList();
        for (Object variableResultSql : variablesResultSql) {
            Object[] variableResultSqlArray = (Object[]) variableResultSql;
            Long actualItemId = getLong(variableResultSqlArray[0]);
            ConceptMetamacVisualisationResult target = (ConceptMetamacVisualisationResult) mapItemByItemId.get(actualItemId);
            RelatedResourceVisualisationResult variable = variableResultSqlToRelatedResourceVisualisationResult(variableResultSqlArray);
            target.setVariable(variable);
        }

        // Parent: fill manually with java code
        for (ConceptMetamacVisualisationResult target : targets) {
            if (target.getParentIdDatabase() != null) {
                ItemVisualisationResult parent = mapItemByItemId.get(target.getParentIdDatabase());
                target.setParent(parent);
            }
        }
        return targets;
    }

    @Override
    public List<ItemResult> findConceptsByConceptSchemeUnordered(Long conceptSchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find items
        List<ItemResult> items = conceptRepository.findConceptsByConceptSchemeUnordered(conceptSchemeVersionId, resultSelection);

        // Init extension point and index by id
        Map<Long, ItemResult> mapItemByItemId = new HashMap<Long, ItemResult>(items.size());
        for (ItemResult itemResult : items) {
            itemResult.setExtensionPoint(new ConceptMetamacResultExtensionPoint());
            mapItemByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        if (resultSelection.isInternationalStringsMetamac()) {
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.PLURAL_NAME);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.ACRONYM);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.DESCRIPTION_SOURCE);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.CONTEXT);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.DOC_METHOD);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.DERIVATION);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapItemByItemId, ConceptExtensionPointUtilities.LEGAL_ACTS);
        }
        return items;
    }

    @Override
    public void checkConceptTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        conceptRepository.checkConceptTranslations(itemSchemeVersionId, locale, exceptionItems);
        checkConceptMetamacTranslations(itemSchemeVersionId, locale, exceptionItems);
    }

    @Override
    public ConceptMetamac retrieveConceptWithQuantityId(Long quantityId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("quantityId", quantityId);
        List<ConceptMetamac> result = findByQuery("from ConceptMetamac where quantity.id = :quantityId", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * Checks concrete metadata of ConceptMetamac
     */
    @SuppressWarnings("rawtypes")
    private void checkConceptMetamacTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.CODE, ");
        sb.append("c." + COLUMN_NAME_PLURAL_NAME + " as PLURAL_NAME_IS, lsplurn.LABEL as PLURAL_NAME_LABEL, ");
        sb.append("c." + COLUMN_NAME_ACRONYM + " as ACRONYM_IS, lsacron.LABEL as ACRONYM_LABEL, ");
        sb.append("c." + COLUMN_NAME_DESCRIPTION_SOURCE + " as DESCRIPTION_SOURCE_IS, lsdescs.LABEL as DESCRITION_SOURCE_LABEL, ");
        sb.append("c." + COLUMN_NAME_CONTEXT + " as CONTEXT_IS, lsconte.LABEL as CONTEXT_LABEL, ");
        sb.append("c." + COLUMN_NAME_DOC_METHOD + " as DOC_METHOD_IS, lsdocme.LABEL as DOC_METHOD_LABEL, ");
        sb.append("c." + COLUMN_NAME_DERIVATION + " as DERIVARION_IS, lsderiv.LABEL as DERIVARION_LABEL, ");
        sb.append("c." + COLUMN_NAME_LEGAL_ACTS + " as LEGAL_ACTS_IS, lslegal.LABEL as LEGAL_ACTS_LABEL ");
        sb.append("FROM TB_CONCEPTS cb ");
        sb.append("INNER JOIN TB_ITEMS_BASE i on cb.TB_ITEMS_BASE = i.ID ");
        sb.append("INNER JOIN TB_M_CONCEPTS c on c.TB_CONCEPTS = i.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsplurn on lsplurn.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_PLURAL_NAME + " AND lsplurn.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsacron on lsacron.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_ACRONYM + " AND lsacron.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsdescs on lsdescs.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_DESCRIPTION_SOURCE + " AND lsdescs.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsconte on lsconte.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_CONTEXT + " AND lsconte.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsdocme on lsdocme.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_DOC_METHOD + " AND lsdocme.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsderiv on lsderiv.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_DERIVATION + " AND lsderiv.LOCALE = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lslegal on lslegal.INTERNATIONAL_STRING_FK = c." + COLUMN_NAME_LEGAL_ACTS + " AND lslegal.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND (");
        sb.append("       (c." + COLUMN_NAME_PLURAL_NAME + " IS NOT NULL AND lsplurn.LABEL IS NULL) ");
        sb.append("    OR (c." + COLUMN_NAME_ACRONYM + " IS NOT NULL AND lsacron.LABEL IS NULL) ");
        sb.append("    OR (c." + COLUMN_NAME_DESCRIPTION_SOURCE + " IS NOT NULL AND lsdescs.LABEL IS NULL) ");
        sb.append("    OR (c." + COLUMN_NAME_CONTEXT + " IS NOT NULL AND lsconte.LABEL IS NULL) ");
        sb.append("    OR (c." + COLUMN_NAME_DOC_METHOD + " IS NOT NULL AND lsdocme.LABEL IS NULL) ");
        sb.append("    OR (c." + COLUMN_NAME_DERIVATION + " IS NOT NULL AND lsderiv.LABEL IS NULL) ");
        sb.append("    OR (c." + COLUMN_NAME_LEGAL_ACTS + " IS NOT NULL AND lslegal.LABEL IS NULL) ");
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

    @SuppressWarnings("rawtypes")
    private void executeQueryRetrieveConceptInternationalString(Long itemSchemVersionId, Map<Long, ItemResult> mapItemByItemId, ConceptExtensionPointUtility extractor) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT c.TB_CONCEPTS, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL ");
        sqlQuery.append("FROM TB_M_CONCEPTS c INNER JOIN TB_CONCEPTS cb on cb.TB_ITEMS_BASE = c.TB_CONCEPTS ");
        sqlQuery.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c." + extractor.getColumnName() + " ");
        sqlQuery.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemVersionId and c." + extractor.getColumnName() + " IS NOT NULL");

        Query query = getEntityManager().createNativeQuery(sqlQuery.toString());
        query.setParameter("itemSchemVersionId", itemSchemVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            Object[] resultSqlArray = (Object[]) resultSql;
            Long actualItemId = getLong(resultSqlArray[0]);
            ItemResult target = mapItemByItemId.get(actualItemId);
            internationalStringResultSqltoInternationalStringResult(resultSqlArray, extractor.getMetadata(target));
        }
    }

    private void internationalStringResultSqltoInternationalStringResult(Object[] source, Map<String, String> target) {
        int i = 1; // 0 is itemId
        String locale = getString(source[i++]);
        if (locale == null) {
            return;
        }
        String label = getString(source[i++]);
        target.put(locale, label);
    }

    private interface ConceptExtensionPointUtility {

        public String getColumnName();
        public Map<String, String> getMetadata(ItemResult item);
    }

    private static class ConceptExtensionPointUtilities {

        private static ConceptMetamacResultExtensionPoint getConceptExtensionPoint(ItemResult item) {
            return (ConceptMetamacResultExtensionPoint) item.getExtensionPoint();
        }

        public static ConceptExtensionPointUtility PLURAL_NAME        = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_PLURAL_NAME;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getPluralName();
                                                                          }
                                                                      };

        public static ConceptExtensionPointUtility ACRONYM            = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_ACRONYM;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getAcronym();
                                                                          }
                                                                      };

        public static ConceptExtensionPointUtility DESCRIPTION_SOURCE = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_DESCRIPTION_SOURCE;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getDescriptionSource();
                                                                          }
                                                                      };
        public static ConceptExtensionPointUtility CONTEXT            = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_CONTEXT;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getContext();
                                                                          }
                                                                      };
        public static ConceptExtensionPointUtility DOC_METHOD         = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_DOC_METHOD;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getDocMethod();
                                                                          }
                                                                      };
        public static ConceptExtensionPointUtility DERIVATION         = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_DERIVATION;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getDerivation();
                                                                          }
                                                                      };
        public static ConceptExtensionPointUtility LEGAL_ACTS         = new ConceptExtensionPointUtility() {

                                                                          @Override
                                                                          public String getColumnName() {
                                                                              return COLUMN_NAME_LEGAL_ACTS;
                                                                          };
                                                                          @Override
                                                                          public java.util.Map<String, String> getMetadata(ItemResult item) {
                                                                              return getConceptExtensionPoint(item).getLegalActs();
                                                                          }
                                                                      };
    }

    private ConceptMetamacVisualisationResult itemResultSqlToConceptVisualisationResult(Object[] source) throws MetamacException {
        ConceptMetamacVisualisationResult target = new ConceptMetamacVisualisationResult();
        int i = 0;
        target.setItemIdDatabase(getLong(source[i++]));
        target.setCreatedDate(getDate(source[i++], source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setParentIdDatabase(getLong(source[i++]));
        target.setName(getString(source[i++]));
        target.setAcronym(getString(source[i++]));
        String sdmxRelatedArtefact = getString(source[i++]);
        target.setSdmxRelatedArtefact(sdmxRelatedArtefact != null ? ConceptRoleEnum.valueOf(sdmxRelatedArtefact) : null);
        return target;
    }

    private RelatedResourceVisualisationResult variableResultSqlToRelatedResourceVisualisationResult(Object[] source) {
        RelatedResourceVisualisationResult target = new RelatedResourceVisualisationResult();
        int i = 0;
        i++; // skip item id
        i++; // skip variable id
        target.setUrn(getString(source[i++]));
        target.setUrnProvider(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setTitle(getString(source[i++]));
        return target;
    }
}