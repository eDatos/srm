package org.siemac.metamac.srm.core.concept.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.booleanToBooleanDatabase;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDate;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getInteger;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.withoutTranslation;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addTranslationExceptionToExceptionItemsByResource;
import static org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils.addExceptionToExceptionItemsByResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.RelatedResourceVisualisationResult;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

/**
 * Repository implementation for ConceptMetamac
 */
@Repository("conceptMetamacRepository")
public class ConceptMetamacRepositoryImpl extends ConceptMetamacRepositoryBase {

    @Autowired
    private ItemRepository      itemRepository;

    @Autowired
    private ConceptRepository   conceptRepository;

    @Autowired
    private SrmConfiguration    srmConfiguration;

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
        sb.append(
                "SELECT cb.ID as ITEM_ID, cb.CREATED_DATE, cb.CREATED_DATE_TZ, a.URN, a.CODE, cb.PARENT_FK as ITEM_PARENT_ID, lsName.LABEL as NAME, lsAcronym.LABEL as ACRONYM, c.SDMX_RELATED_ARTEFACT, c.ORDER_VALUE ");
        sb.append("FROM TB_M_CONCEPTS c ");
        sb.append("INNER JOIN TB_CONCEPTS cb on c.TB_CONCEPTS = cb.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsName on lsName.INTERNATIONAL_STRING_FK = a.NAME_FK and lsName.locale = :locale ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS lsAcronym on lsAcronym.INTERNATIONAL_STRING_FK = c.ACRONYM_FK and lsAcronym.locale = :locale ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("ORDER BY c.ORDER_VALUE ASC ");
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
        sbVariables.append("INNER JOIN TB_CONCEPTS cb on c.TB_CONCEPTS = cb.ID ");
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
    public List<ItemResult> findConceptsByConceptSchemeOrderedInDepth(Long conceptSchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find concepts
        List<ItemResult> concepts = conceptRepository.findConceptsByConceptSchemeUnordered(conceptSchemeVersionId, resultSelection);

        // Init extension point and index by id
        Map<Long, ItemResult> mapConceptByItemId = new HashMap<Long, ItemResult>(concepts.size());
        for (ItemResult itemResult : concepts) {
            itemResult.setExtensionPoint(new ConceptMetamacResultExtensionPoint());
            mapConceptByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        if (resultSelection.isInternationalStringsMetamac()) {
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.PLURAL_NAME);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.ACRONYM);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.DESCRIPTION_SOURCE);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.CONTEXT);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.DOC_METHOD);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.DERIVATION);
            executeQueryRetrieveConceptInternationalString(conceptSchemeVersionId, mapConceptByItemId, ConceptExtensionPointUtilities.LEGAL_ACTS);

            executeQueryRetrieveConceptConceptType(conceptSchemeVersionId, mapConceptByItemId);
            executeQueryRetrieveConceptRepresentation(conceptSchemeVersionId, mapConceptByItemId);
            executeQueryRetrieveConceptConceptExtends(conceptSchemeVersionId, mapConceptByItemId);
        }

        StringBuilder sb = new StringBuilder();
        if (srmConfiguration.isDatabaseOracle()) {
            sb.append("SELECT c1.ID ");
            sb.append("FROM TB_CONCEPTS c1, TB_M_CONCEPTS mc1 ");
            sb.append("WHERE c1.ITEM_SCHEME_VERSION_FK = :conceptSchemeVersion ");
            sb.append("AND mc1.TB_CONCEPTS = c1.ID ");
            sb.append("START WITH c1.PARENT_FK is null ");
            sb.append("CONNECT BY PRIOR c1.ID = c1.PARENT_FK ");
            sb.append("ORDER SIBLINGS BY mc1.ORDER_VALUE ASC ");
        } else if (srmConfiguration.isDatabaseSqlServer()) {
            sb.append("WITH parents(ID, OV) AS ");
            sb.append("   (SELECT c1.ID, format(mc1.ORDER_VALUE , '000000') OV ");
            sb.append("    FROM TB_CONCEPTS as c1, TB_M_CONCEPTS mc1  ");
            sb.append("    WHERE mc1.TB_CONCEPTS = c1.ID and c1.PARENT_FK is null and c1.ITEM_SCHEME_VERSION_FK = :conceptSchemeVersion ");
            sb.append("        UNION ALL ");
            sb.append("    SELECT c2.ID, p.OV + '-' + format(mc2.ORDER_VALUE , '000000') OV ");
            sb.append("    FROM TB_CONCEPTS as c2, TB_M_CONCEPTS mc2, parents p");
            sb.append("    WHERE mc2.TB_CONCEPTS = c2.ID and p.ID = c2.PARENT_FK) ");
            sb.append("SELECT ID FROM parents ");
            sb.append("ORDER BY OV ASC ");
        } else {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.UNKNOWN).withMessageParameters("Database unsupported").build();
        }
        Query queryOrder = getEntityManager().createNativeQuery(sb.toString());
        queryOrder.setParameter("conceptSchemeVersion", conceptSchemeVersionId);
        @SuppressWarnings("rawtypes")
        List resultsOrder = queryOrder.getResultList();

        // Order previous result list thanks to ordered list of items id
        List<ItemResult> ordered = new ArrayList<ItemResult>(concepts.size());
        for (Object resultOrder : resultsOrder) {
            Long conceptId = getLong(resultOrder);
            ItemResult concept = mapConceptByItemId.get(conceptId);
            ordered.add(concept);
        }
        return ordered;
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
    public void checkConceptTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        conceptRepository.checkConceptTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        checkConceptMetamacTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
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

    @SuppressWarnings("rawtypes")
    @Override
    public void checkConceptsWithConceptExtendsExternallyPublished(Long itemSchemeVersionId, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(acb2.URN) ");
        sb.append("FROM TB_M_CONCEPTS c1 ");
        sb.append("INNER JOIN TB_CONCEPTS cb1 ON cb1.ID = c1.TB_CONCEPTS ");
        sb.append("INNER JOIN TB_CONCEPTS cb2 ON cb2.ID = c1.EXTENDS_FK ");
        sb.append("INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv2 ON isv2.ID = cb2.ITEM_SCHEME_VERSION_FK ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv2 ON aisv2.ID = isv2.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS acb2 ON acb2.ID = cb2.NAMEABLE_ARTEFACT_FK ");
        sb.append("WHERE cb1.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND aisv2.PUBLIC_LOGIC != " + booleanToBooleanDatabase(true));

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urnRelatedResource = getString(resultSql);
            addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, urnRelatedResource);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void checkConceptsWithConceptRoleExternallyPublished(Long itemSchemeVersionId, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(acb2.URN) ");
        sb.append("FROM TB_M_CONCEPT_ROLES cr ");
        sb.append("INNER JOIN TB_CONCEPTS cb1 ON cb1.ID = cr.CONCEPT_FK ");
        sb.append("INNER JOIN TB_CONCEPTS cb2 ON cb2.ID = cr.CONCEPT_ROLE_FK ");
        sb.append("INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv2 ON isv2.ID = cb2.ITEM_SCHEME_VERSION_FK ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv2 ON aisv2.ID = isv2.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS acb2 ON acb2.ID = cb2.NAMEABLE_ARTEFACT_FK ");
        sb.append("WHERE cb1.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND aisv2.PUBLIC_LOGIC != " + booleanToBooleanDatabase(true));

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urnRelatedResource = getString(resultSql);
            addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, urnRelatedResource);
        }
    }

    @Override
    public void checkConceptsWithQuantityExternallyPublished(Long itemSchemeVersionId, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        checkConceptsWithQuantityConceptMetadataExternallyPublished(itemSchemeVersionId, "NUMERATOR_FK", exceptionItemsByUrn);
        checkConceptsWithQuantityConceptMetadataExternallyPublished(itemSchemeVersionId, "DENOMINATOR_FK", exceptionItemsByUrn);
        checkConceptsWithQuantityConceptMetadataExternallyPublished(itemSchemeVersionId, "BASE_QUANTITY_FK", exceptionItemsByUrn);
        checkConceptsWithQuantityCodeMetadataExternallyPublished(itemSchemeVersionId, "UNIT_CODE_FK", exceptionItemsByUrn);
        checkConceptsWithQuantityCodeMetadataExternallyPublished(itemSchemeVersionId, "BASE_LOCATION_FK", exceptionItemsByUrn);
    }

    @Override
    public void checkConceptsWithRepresentationExternallyPublished(Long itemSchemeVersionId, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        checkConceptsWithRepresentationExternallyPublished(itemSchemeVersionId, "CODELIST_FK", exceptionItemsByUrn, ServiceExceptionType.CODELIST_NOT_EXTERNALLY_PUBLISHED);
        checkConceptsWithRepresentationExternallyPublished(itemSchemeVersionId, "CONCEPT_SCHEME_FK", exceptionItemsByUrn, ServiceExceptionType.CONCEPT_SCHEME_NOT_EXTERNALLY_PUBLISHED);
    }

    @Override
    public Integer getConceptMaximumOrderInLevel(ConceptSchemeVersionMetamac conceptSchemeVersion, Concept parent) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT max(order_value) ");
        sb.append("FROM tb_m_concepts c, tb_concepts cb where c.tb_concepts = cb.id AND cb.item_scheme_version_fk = :conceptSchemeVersion ");
        if (parent == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
        }

        Query query = getEntityManager().createNativeQuery(sb.toString());
        if (parent != null) {
            query.setParameter("parent", parent.getId());
        }

        query.setParameter("conceptSchemeVersion", conceptSchemeVersion.getId());

        List resultsOrder = query.getResultList();
        if (CollectionUtils.isEmpty(resultsOrder) || resultsOrder.get(0) == null) {
            return null;
        } else {
            return Integer.valueOf(resultsOrder.get(0).toString());
        }
    }

    @Override
    public Integer reorderConceptsDeletingOneConcept(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) {
        Concept parent = concept.getParent();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_m_concepts set ORDER_VALUE = ORDER_VALUE - 1 ");
        sb.append("WHERE tb_concepts in ");
        sb.append("(SELECT cb.ID ");
        sb.append("FROM tb_concepts cb ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :conceptSchemeVersion ");
        if (parent == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
        }
        sb.append(") AND ORDER_VALUE > :order");

        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("conceptSchemeVersion", conceptSchemeVersion.getId());
        queryUpdate.setParameter("order", concept.getOrderValue());
        if (parent != null) {
            queryUpdate.setParameter("parent", parent.getId());
        }

        return queryUpdate.executeUpdate();
    }

    @Override
    public Integer reorderConceptsAddingOneConceptInMiddle(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept, Integer newConceptIndex) {
        Concept parent = concept.getParent();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_m_concepts set ORDER_VALUE = ORDER_VALUE + 1 ");
        sb.append("WHERE tb_concepts in ");
        sb.append("(SELECT cb.ID ");
        sb.append("FROM tb_concepts cb ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :conceptSchemeVersion ");
        if (parent == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
        }
        sb.append(") AND ORDER_VALUE >= :order");

        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("conceptSchemeVersion", conceptSchemeVersion.getId());
        queryUpdate.setParameter("order", newConceptIndex);
        if (parent != null) {
            queryUpdate.setParameter("parent", parent.getId());
        }

        return queryUpdate.executeUpdate();
    }

    @SuppressWarnings("rawtypes")
    private void checkConceptsWithQuantityConceptMetadataExternallyPublished(Long itemSchemeVersionId, String columnName, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(acb2.URN) ");
        sb.append("FROM TB_M_CONCEPTS c1 ");
        sb.append("INNER JOIN TB_CONCEPTS cb1 ON cb1.ID = c1.TB_CONCEPTS ");
        sb.append("INNER JOIN TB_M_QUANTITIES q1 ON q1.ID = c1.QUANTITY_FK ");
        sb.append("INNER JOIN TB_CONCEPTS cb2 ON cb2.ID = q1." + columnName + " ");
        sb.append("INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv2 ON isv2.ID = cb2.ITEM_SCHEME_VERSION_FK AND isv2.ID != :itemSchemeVersionId ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv2 ON aisv2.ID = isv2.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS acb2 ON acb2.ID = cb2.NAMEABLE_ARTEFACT_FK ");
        sb.append("WHERE cb1.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND aisv2.PUBLIC_LOGIC != " + booleanToBooleanDatabase(true));

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urnRelatedResource = getString(resultSql);
            addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, urnRelatedResource);
        }
    }

    @SuppressWarnings("rawtypes")
    private void checkConceptsWithQuantityCodeMetadataExternallyPublished(Long itemSchemeVersionId, String columnName, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(acb2.URN) ");
        sb.append("FROM TB_M_CONCEPTS c1 ");
        sb.append("INNER JOIN TB_CONCEPTS cb1 ON cb1.ID = c1.TB_CONCEPTS ");
        sb.append("INNER JOIN TB_M_QUANTITIES q1 ON q1.ID = c1.QUANTITY_FK ");
        sb.append("INNER JOIN TB_CODES cb2 ON cb2.ID = q1." + columnName + " ");
        sb.append("INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv2 ON isv2.ID = cb2.ITEM_SCHEME_VERSION_FK AND isv2.ID != :itemSchemeVersionId ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv2 ON aisv2.ID = isv2.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS acb2 ON acb2.ID = cb2.NAMEABLE_ARTEFACT_FK ");
        sb.append("WHERE cb1.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND aisv2.PUBLIC_LOGIC != " + booleanToBooleanDatabase(true));

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urnRelatedResource = getString(resultSql);
            addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CODE_NOT_EXTERNALLY_PUBLISHED, urnRelatedResource);
        }
    }

    @SuppressWarnings("rawtypes")
    private void checkConceptsWithRepresentationExternallyPublished(Long itemSchemeVersionId, String columnName, Map<String, MetamacExceptionItem> exceptionItemsByUrn,
            CommonServiceExceptionType serviceExceptionType) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(aisv2.URN) ");
        sb.append("FROM TB_CONCEPTS cb1 ");
        sb.append("INNER JOIN TB_REPRESENTATIONS r1 ON r1.ID = cb1.CORE_REPRESENTATION_FK ");
        sb.append("INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv2 ON isv2.ID = r1." + columnName + " ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv2 ON aisv2.ID = isv2.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("WHERE cb1.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND r1.REPRESENTATION_TYPE = :representationType ");
        sb.append("AND r1." + columnName + " IS NOT NULL ");
        sb.append("AND aisv2.PUBLIC_LOGIC != " + booleanToBooleanDatabase(true));

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("representationType", RepresentationTypeEnum.ENUMERATION.getName());
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urnRelatedResource = getString(resultSql);
            addExceptionToExceptionItemsByResource(exceptionItemsByUrn, serviceExceptionType, urnRelatedResource);
        }
    }

    /**
     * Checks concrete metadata of ConceptMetamac
     */
    @SuppressWarnings("rawtypes")
    private void checkConceptMetamacTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.URN, ");
        sb.append("c." + COLUMN_NAME_PLURAL_NAME + " as PLURAL_NAME_IS, lsplurn.LABEL as PLURAL_NAME_LABEL, ");
        sb.append("c." + COLUMN_NAME_ACRONYM + " as ACRONYM_IS, lsacron.LABEL as ACRONYM_LABEL, ");
        sb.append("c." + COLUMN_NAME_DESCRIPTION_SOURCE + " as DESCRIPTION_SOURCE_IS, lsdescs.LABEL as DESCRITION_SOURCE_LABEL, ");
        sb.append("c." + COLUMN_NAME_CONTEXT + " as CONTEXT_IS, lsconte.LABEL as CONTEXT_LABEL, ");
        sb.append("c." + COLUMN_NAME_DOC_METHOD + " as DOC_METHOD_IS, lsdocme.LABEL as DOC_METHOD_LABEL, ");
        sb.append("c." + COLUMN_NAME_DERIVATION + " as DERIVARION_IS, lsderiv.LABEL as DERIVARION_LABEL, ");
        sb.append("c." + COLUMN_NAME_LEGAL_ACTS + " as LEGAL_ACTS_IS, lslegal.LABEL as LEGAL_ACTS_LABEL ");
        sb.append("FROM TB_CONCEPTS cb ");
        sb.append("INNER JOIN TB_M_CONCEPTS c on c.TB_CONCEPTS = cb.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
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
            String urn = getString(resultArray[i++]);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_PLURAL_NAME, resultArray, i++, i++, exceptionItemsByUrn);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_ACRONYM, resultArray, i++, i++, exceptionItemsByUrn);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE, resultArray, i++, i++, exceptionItemsByUrn);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_CONTEXT, resultArray, i++, i++, exceptionItemsByUrn);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_DOC_METHOD, resultArray, i++, i++, exceptionItemsByUrn);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_DERIVATION, resultArray, i++, i++, exceptionItemsByUrn);
            checkTranslation(urn, ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, resultArray, i++, i++, exceptionItemsByUrn);
        }
    }

    private void checkTranslation(String urn, String metadataExceptionParameter, Object[] resultArray, int internationalStringIndex, int localisedStringIndex,
            Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        Long internationalStringName = getLong(resultArray[internationalStringIndex]);
        String localisedStringName = getString(resultArray[localisedStringIndex]);
        if (withoutTranslation(internationalStringName, localisedStringName)) {
            addTranslationExceptionToExceptionItemsByResource(exceptionItemsByUrn, urn, metadataExceptionParameter);
        }
    }

    @SuppressWarnings("rawtypes")
    private void executeQueryRetrieveConceptInternationalString(Long itemSchemVersionId, Map<Long, ItemResult> mapItemByItemId, ConceptExtensionPointUtility extractor) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT c.TB_CONCEPTS, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL ");
        sqlQuery.append("FROM TB_M_CONCEPTS c ");
        sqlQuery.append("INNER JOIN TB_CONCEPTS cb on cb.ID = c.TB_CONCEPTS ");
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

    @SuppressWarnings("rawtypes")
    private void executeQueryRetrieveConceptConceptType(Long itemSchemVersionId, Map<Long, ItemResult> mapItemByItemId) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT c.TB_CONCEPTS, ct.IDENTIFIER ");
        sqlQuery.append("FROM TB_M_CONCEPTS c ");
        sqlQuery.append("INNER JOIN TB_CONCEPTS cb on cb.ID = c.TB_CONCEPTS ");
        sqlQuery.append("INNER JOIN TB_M_LIS_CONCEPT_TYPES ct on ct.id = c.CONCEPT_TYPE_FK ");
        sqlQuery.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemVersionId and c.CONCEPT_TYPE_FK IS NOT NULL");

        Query query = getEntityManager().createNativeQuery(sqlQuery.toString());
        query.setParameter("itemSchemVersionId", itemSchemVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            Object[] resultSqlArray = (Object[]) resultSql;
            Long actualItemId = getLong(resultSqlArray[0]);
            String identifier = getString(resultSqlArray[1]);
            ItemResult target = mapItemByItemId.get(actualItemId);
            ((ConceptMetamacResultExtensionPoint) target.getExtensionPoint()).setConceptTypeIdentifier(identifier);
        }
    }

    @SuppressWarnings({"rawtypes", "static-access"})
    private void executeQueryRetrieveConceptRepresentation(Long itemSchemVersionId, Map<Long, ItemResult> mapItemByItemId) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT c.TB_CONCEPTS, r.REPRESENTATION_TYPE, r.TEXT_FORMAT_FK, r.CONCEPT_SCHEME_FK, r.CODELIST_FK ");
        sqlQuery.append("FROM TB_M_CONCEPTS c ");
        sqlQuery.append("INNER JOIN TB_CONCEPTS cb on cb.ID = c.TB_CONCEPTS ");
        sqlQuery.append("INNER JOIN TB_REPRESENTATIONS r ON r.ID = cb.CORE_REPRESENTATION_FK ");
        sqlQuery.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemVersionId AND cb.CORE_REPRESENTATION_FK IS NOT NULL");

        Query query = getEntityManager().createNativeQuery(sqlQuery.toString());
        query.setParameter("itemSchemVersionId", itemSchemVersionId);
        List resultsSql = query.getResultList();

        Map<Long, String> cachedItemSchemeUrnMap = new HashMap<Long, String>();
        Map<Long, FacetValueTypeEnum> cachedFacetValueEnumMap = new HashMap<Long, FacetValueTypeEnum>();
        for (Object resultSql : resultsSql) {
            Object[] resultSqlArray = (Object[]) resultSql;
            Long actualItemId = getLong(resultSqlArray[0]);
            String representationType = getString(resultSqlArray[1]);
            Long textFormatFK = getLong(resultSqlArray[2]);
            Long conceptSchemeFK = getLong(resultSqlArray[3]);
            Long codelistFK = getLong(resultSqlArray[4]);

            ItemResult target = mapItemByItemId.get(actualItemId);

            RepresentationTypeEnum representationTypeEnum = RepresentationTypeEnum.ENUMERATION.valueOf(representationType);
            if (RepresentationTypeEnum.ENUMERATION.equals(representationTypeEnum)) {
                String itemSchemeVersionUrn = null;
                if (codelistFK != null) {
                    itemSchemeVersionUrn = getUrnFromItemSchemeVersionId(cachedItemSchemeUrnMap, codelistFK, itemSchemeVersionUrn);
                } else if (conceptSchemeFK != null) {
                    itemSchemeVersionUrn = getUrnFromItemSchemeVersionId(conceptSchemeFK);
                }
                ((ConceptMetamacResultExtensionPoint) target.getExtensionPoint()).setRepresentationType(RepresentationsTsv.RepresentationEum.ENUMERATED);
                ((ConceptMetamacResultExtensionPoint) target.getExtensionPoint()).setRepresentationValue(itemSchemeVersionUrn);
            } else {
                // TEXT_FORMAT_FK
                FacetValueTypeEnum facetValueTypeEnum = null;
                if (cachedFacetValueEnumMap.containsKey(textFormatFK)) {
                    facetValueTypeEnum = cachedFacetValueEnumMap.get(textFormatFK);
                } else {
                    facetValueTypeEnum = getFacetValueTypeEnum(textFormatFK);
                    cachedFacetValueEnumMap.put(textFormatFK, facetValueTypeEnum);
                }
                ((ConceptMetamacResultExtensionPoint) target.getExtensionPoint()).setRepresentationType(RepresentationsTsv.RepresentationEum.NON_ENUMERATED);
                ((ConceptMetamacResultExtensionPoint) target.getExtensionPoint()).setRepresentationValue(facetValueTypeEnum.getValue());
            }
        }
    }

    private String getUrnFromItemSchemeVersionId(Map<Long, String> cachedItemSchemeUrnMap, Long codelistFK, String itemSchemeVersionUrn) {
        if (cachedItemSchemeUrnMap.containsKey(codelistFK)) {
            itemSchemeVersionUrn = cachedItemSchemeUrnMap.get(codelistFK);
        } else {
            itemSchemeVersionUrn = getUrnFromItemSchemeVersionId(codelistFK);
            cachedItemSchemeUrnMap.put(codelistFK, itemSchemeVersionUrn);
        }
        return itemSchemeVersionUrn;
    }
    @SuppressWarnings("rawtypes")
    private String getUrnFromItemSchemeVersionId(Long itemSchemeVersionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT aa.urn ");
        sb.append("FROM TB_ITEM_SCHEMES_VERSIONS isv ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS aa on aa.id = isv.MAINTAINABLE_ARTEFACT_FK ");
        sb.append("WHERE isv.id = :itemSchemeVersionId ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        return (String) query.getSingleResult();
    }

    @SuppressWarnings("rawtypes")
    private FacetValueTypeEnum getFacetValueTypeEnum(Long facetId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f.FACET_VALUE ");
        sb.append("FROM TB_FACETS f ");
        sb.append("WHERE f.id = :facetId ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("facetId", facetId);
        String facetValue = (String) query.getSingleResult();
        FacetValueTypeEnum facetValueTypeEnum = FacetValueTypeEnum.valueOf(facetValue);
        return facetValueTypeEnum;
    }

    @SuppressWarnings("rawtypes")
    private void executeQueryRetrieveConceptConceptExtends(Long itemSchemVersionId, Map<Long, ItemResult> mapItemByItemId) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT c1.TB_CONCEPTS, acb2.URN ");
        sqlQuery.append("FROM TB_M_CONCEPTS c1 ");
        sqlQuery.append("INNER JOIN TB_CONCEPTS cb1 on cb1.ID = c1.TB_CONCEPTS ");
        sqlQuery.append("INNER JOIN TB_CONCEPTS cb2 ON cb2.ID = c1.EXTENDS_FK ");

        sqlQuery.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS acb2 ON acb2.ID = cb2.NAMEABLE_ARTEFACT_FK ");
        sqlQuery.append("WHERE cb1.ITEM_SCHEME_VERSION_FK = :itemSchemVersionId");

        Query query = getEntityManager().createNativeQuery(sqlQuery.toString());
        query.setParameter("itemSchemVersionId", itemSchemVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            Object[] resultSqlArray = (Object[]) resultSql;
            Long actualItemId = getLong(resultSqlArray[0]);
            String urn = getString(resultSqlArray[1]);
            ItemResult target = mapItemByItemId.get(actualItemId);
            ((ConceptMetamacResultExtensionPoint) target.getExtensionPoint()).setConceptExtendsUrn(urn);
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
        target.setOrder(getInteger(source[i++]));

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