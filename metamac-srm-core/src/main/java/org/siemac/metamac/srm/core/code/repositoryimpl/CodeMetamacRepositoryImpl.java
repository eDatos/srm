package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.booleanToBooleanDatabase;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getBoolean;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getInteger;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;

/**
 * Repository implementation for CodeMetamac
 */
@Repository("codeMetamacRepository")
public class CodeMetamacRepositoryImpl extends CodeMetamacRepositoryBase {

    @Autowired
    private CodeRepository      codeRepository;

    @Autowired
    private SrmConfiguration    srmConfiguration;

    private final static String NATIVE_SQL_QUERY_CODES_VARIABLE_ELEMENT_SHORT_NAME_BY_CODELIST = "select c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_M_CODES c INNER JOIN TB_ITEMS_BASE i on i.ID = c.TB_CODES INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK WHERE c.VARIABLE_ELEMENT_FK is not null AND  i.ITEM_SCHEME_VERSION_FK = :codelistVersion";
    private final static String NATIVE_SQL_QUERY_CODES_SHORT_NAME_BY_CODELIST                  = "select c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_M_CODES c INNER JOIN TB_ITEMS_BASE i on i.ID = c.TB_CODES LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK WHERE c.VARIABLE_ELEMENT_FK is null AND  i.ITEM_SCHEME_VERSION_FK = :codelistVersion and c.SHORT_NAME_FK is not null";

    public CodeMetamacRepositoryImpl() {
    }

    @Override
    public CodeMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodeMetamac> result = findByQuery("from CodeMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public CodeMetamac findByCodeWithoutFlushing(Long codelistVersionId, String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", code);
        parameters.put("codelistVersionId", codelistVersionId);
        List<CodeMetamac> result = findByQuery("from CodeMetamac where nameableArtefact.code = :code and itemSchemeVersion.id = :codelistVersionId", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void clearCodesVariableElementByCodelist(CodelistVersionMetamac codelistVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set VARIABLE_ELEMENT_FK = null ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void reorderCodesDeletingOneCode(CodelistVersionMetamac codelistVersion, Item parent, CodeMetamac code, Integer orderColumnIndex, Integer orderCodeToDelete) {
        String orderColumn = getOrderColumnName(orderColumnIndex);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + orderColumn + " = " + orderColumn + " - 1 ");
        sb.append("WHERE TB_CODES in ");
        sb.append("(SELECT i.ID ");
        sb.append("FROM TB_ITEMS_BASE i ");
        sb.append("WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        if (parent == null) {
            sb.append("AND i.PARENT_FK is null");
        } else {
            sb.append("AND i.PARENT_FK = :parent");
        }
        sb.append(") AND " + orderColumn + " > :order");

        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.setParameter("order", orderCodeToDelete);
        if (parent != null) {
            queryUpdate.setParameter("parent", parent.getId());
        }
        queryUpdate.executeUpdate();
    }

    @Override
    public void reorderCodesAddingOneCodeInMiddle(CodelistVersionMetamac codelistVersion, CodeMetamac code, Integer orderColumnIndex, Integer orderCodeToAdd) {
        String orderColumn = getOrderColumnName(orderColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + orderColumn + " = " + orderColumn + " + 1 ");
        sb.append("WHERE TB_CODES in (");
        sb.append("SELECT i.ID ");
        sb.append("FROM TB_ITEMS_BASE i ");
        sb.append("WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        if (code.getParent() == null) {
            sb.append("AND i.PARENT_FK is null");
        } else {
            sb.append("AND i.PARENT_FK = :parent");
        }
        sb.append(") AND " + orderColumn + " >= :order");

        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.setParameter("order", orderCodeToAdd);
        if (code.getParent() != null) {
            queryUpdate.setParameter("parent", code.getParent().getId());
        }
        queryUpdate.executeUpdate();
    }

    @Override
    public void clearCodesOrderColumn(CodelistVersionMetamac codelistVersion, Integer orderColumnIndex) {
        String orderColumn = getOrderColumnName(orderColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + orderColumn + " = null ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void copyCodesOrderColumn(CodelistVersionMetamac codelistVersion, Integer columnIndexSource, Integer columnIndexTarget) {
        String columnSource = getOrderColumnName(columnIndexSource);
        String columnTarget = getOrderColumnName(columnIndexTarget);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + columnTarget + " = " + columnSource + " ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void clearCodesOpennessColumnByCodelist(CodelistVersionMetamac codelistVersion, Integer opennessColumnIndex) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = null ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void copyCodesOpennessColumnByCodelist(CodelistVersionMetamac codelistVersion, Integer columnIndexSource, Integer columnIndexTarget) {
        String columnSource = getOpennessColumnName(columnIndexSource);
        String columnTarget = getOpennessColumnName(columnIndexTarget);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + columnTarget + " = " + columnSource + " ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void updateCodesOpennessColumnByCodelist(CodelistVersionMetamac codelistVersion, Integer opennessColumnIndex, Boolean expanded) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = " + getOpennessColumnValue(expanded) + " ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void updateCodeOpennessColumn(CodelistVersionMetamac codelistVersion, String codeUrn, Integer opennessColumnIndex, Boolean expanded) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = " + getOpennessColumnValue(expanded) + " ");
        sb.append("WHERE TB_CODES in (SELECT i.ID FROM TB_ITEMS_BASE i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID WHERE a.URN = :codeUrn AND i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codeUrn", codeUrn);
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void updateCodeOpennessColumnToLeafCodes(CodelistVersionMetamac codelistVersion, Integer opennessColumnIndex, Boolean expanded) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = " + getOpennessColumnValue(expanded) + " ");
        sb.append("WHERE ");
        sb.append("TB_CODES IN (SELECT i.ID FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        sb.append("AND ");
        sb.append("TB_CODES NOT IN (SELECT DISTINCT(i.PARENT_FK) FROM TB_ITEMS_BASE i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK IS NOT NULL) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());;
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Integer getCodeMaximumOrderInLevel(CodelistVersionMetamac codelistVersion, Item parent, Integer orderColumnIndex) {
        String orderColumn = getOrderColumnName(orderColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT max(" + orderColumn + ") ");
        sb.append("FROM TB_M_CODES c INNER JOIN TB_ITEMS_BASE i on i.ID = c.TB_CODES WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        if (parent == null) {
            sb.append("AND i.PARENT_FK is null");
        } else {
            sb.append("AND i.PARENT_FK = :parent");
        }
        Query query = getEntityManager().createNativeQuery(sb.toString());
        if (parent != null) {
            query.setParameter("parent", parent.getId());
        }
        query.setParameter("codelistVersion", codelistVersion.getId());
        List resultsOrder = query.getResultList();
        if (CollectionUtils.isEmpty(resultsOrder) || resultsOrder.get(0) == null) {
            return null;
        } else {
            return Integer.valueOf(resultsOrder.get(0).toString());
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Integer getCodeAlphabeticPositionInLevel(CodelistVersionMetamac codelistVersion, Item parent, Item code) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) ");
        sb.append("FROM TB_ITEMS_BASE i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        sb.append("AND lower(a.CODE) < lower('" + code.getNameableArtefact().getCode() + "') ");
        if (parent == null) {
            sb.append("AND i.PARENT_FK is null");
        } else {
            sb.append("AND i.PARENT_FK = :parent");
        }
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("codelistVersion", codelistVersion.getId());
        if (parent != null) {
            query.setParameter("parent", parent.getId());
        }
        List resultsOrder = query.getResultList();
        if (CollectionUtils.isEmpty(resultsOrder) || resultsOrder.get(0) == null) {
            return null;
        } else {
            return Integer.valueOf(resultsOrder.get(0).toString());
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<CodeMetamacVisualisationResult> findCodesByCodelistUnordered(Long idCodelist, String locale, Integer orderColumnIndex, Integer opennessColumnIndex) throws MetamacException {
        // Find codes
        List codesResultSql = executeQueryFindCodesByCodelistByNativeSqlQuery(idCodelist, locale, orderColumnIndex, opennessColumnIndex);

        // Transform object[] results
        List<CodeMetamacVisualisationResult> targets = new ArrayList<CodeMetamacVisualisationResult>(codesResultSql.size());
        Map<Long, CodeMetamacVisualisationResult> mapCodeByItemId = new HashMap<Long, CodeMetamacVisualisationResult>(codesResultSql.size());
        for (Object codeResultSql : codesResultSql) {
            Object[] codeResultSqlArray = (Object[]) codeResultSql;
            Long actualItemId = getLong(codeResultSqlArray[0]);
            CodeMetamacVisualisationResult target = mapCodeByItemId.get(actualItemId);
            if (target == null) {
                target = codeResultSqlToCodeResult(codeResultSqlArray, null, orderColumnIndex, opennessColumnIndex);
                targets.add(target);
                mapCodeByItemId.put(target.getItemIdDatabase(), target);
            } else {
                codeResultSqlToCodeResult(codeResultSqlArray, target, orderColumnIndex, opennessColumnIndex);
            }
        }
        // Parent: fill manually with java code
        for (CodeMetamacVisualisationResult target : targets) {
            if (target.getParentIdDatabase() != null) {
                CodeMetamacVisualisationResult parent = mapCodeByItemId.get(target.getParentIdDatabase());
                target.setParent(parent);
            }
        }
        return targets;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<ItemResult> findCodesByCodelistOrderedInDepth(Long idCodelist, Integer orderColumnIndex, Boolean extendedMetamacMetadata) throws MetamacException {

        // Find codes
        List<ItemResult> codes = codeRepository.findCodesByCodelistUnordered(idCodelist, Boolean.TRUE);

        // Init extension point and index by id
        Map<Long, ItemResult> mapCodeByItemId = new HashMap<Long, ItemResult>(codes.size());
        for (ItemResult itemResult : codes) {
            itemResult.setExtensionPoint(new CodeMetamacResultExtensionPoint());
            mapCodeByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        if (extendedMetamacMetadata) {
            // Fill short name
            // Try fill from variable element
            executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, NATIVE_SQL_QUERY_CODES_VARIABLE_ELEMENT_SHORT_NAME_BY_CODELIST, mapCodeByItemId);
            // If code has not variable element, try fill with short name in code
            executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, NATIVE_SQL_QUERY_CODES_SHORT_NAME_BY_CODELIST, mapCodeByItemId);
        }

        // TODO Vistas materializadas en bbdd para que sea el mismo código independientemente de la bbdd? Mientras, hay que modificar spring-test.properties y resources.xml para indicar el driver
        // Order
        String orderColumn = getOrderColumnName(orderColumnIndex);
        StringBuilder sb = new StringBuilder();
        // Retrieve items id ordered
        if (srmConfiguration.isDatabaseOracle()) {
            sb.append("SELECT ITEM_ID, SYS_CONNECT_BY_PATH(lpad(COD_ORDER, 6, '0'), '.') ORDER_PATH ");
            sb.append("FROM ");
            sb.append("(");
            sb.append("SELECT c." + orderColumn + " COD_ORDER, i.ID ITEM_ID, i.PARENT_FK ITEM_PARENT_FK ");
            sb.append("FROM TB_M_CODES c JOIN TB_ITEMS_BASE i on i.ID = c.TB_CODES ");
            sb.append("WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion");
            sb.append(")");
            sb.append("START WITH ITEM_PARENT_FK is null ");
            sb.append("CONNECT BY PRIOR ITEM_ID = ITEM_PARENT_FK ");
            sb.append("ORDER BY ORDER_PATH asc");
        } else if (srmConfiguration.isDatabaseSqlServer()) {
            sb.append("WITH Parents(R_ID, R_SORT, R_ITEM_SCHEME_VERSION_FK, R_SORT_STRING) AS ");
            sb.append("( ");
            sb.append("SELECT i1.ID AS R_ID, c1." + orderColumn + " AS R_SORT, i1.ITEM_SCHEME_VERSION_FK AS R_ITEM_SCHEME_VERSION_FK, ");
            sb.append("'/' + REPLICATE(0, 6 - LEN(c1." + orderColumn + ")) + CAST(c1." + orderColumn + " AS varchar(4000)) AS R_SORT_STRING ");
            sb.append("FROM TB_M_CODES AS c1 INNER JOIN TB_ITEMS_BASE AS i1 ON i1.ID = c1.TB_CODES ");
            sb.append("WHERE i1.PARENT_FK IS NULL and i1.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
            sb.append("UNION ALL ");
            sb.append("SELECT i2.ID, c2." + orderColumn + ", i2.ITEM_SCHEME_VERSION_FK AS R_ITEM_SCHEME_VERSION_FK, ");
            sb.append("p.R_SORT_STRING + '/' + REPLICATE(0, 6 - LEN(c2." + orderColumn + ")) + CAST(c2." + orderColumn + " AS varchar(4000)) AS R_SORT_STRING ");
            sb.append("FROM TB_M_CODES AS c2 INNER JOIN TB_ITEMS_BASE AS i2 ON i2.ID = c2.TB_CODES INNER JOIN Parents AS p ON p.R_ID = i2.PARENT_FK ) ");
            sb.append("SELECT * FROM Parents ORDER BY R_SORT_STRING");
        } else {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.UNKNOWN).withMessageParameters("Database unsupported").build();
        }
        Query queryOrder = getEntityManager().createNativeQuery(sb.toString());
        queryOrder.setParameter("codelistVersion", idCodelist);
        List resultsOrder = queryOrder.getResultList();

        // Order previous result list thanks to ordered list of items id
        List<ItemResult> ordered = new ArrayList<ItemResult>(codes.size());
        for (Object resultOrder : resultsOrder) {
            Long codeId = getLong(((Object[]) resultOrder)[0]);
            ItemResult code = mapCodeByItemId.get(codeId);
            ordered.add(code);
        }
        return ordered;
    }

    @Override
    public void checkCodeTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        // item common metadata
        codeRepository.checkCodeTranslations(itemSchemeVersionId, locale, exceptionItems);
        // concrete metadata
        checkCodeMetamacTranslations(itemSchemeVersionId, locale, exceptionItems);
    }

    @SuppressWarnings("rawtypes")
    private void executeQueryCodeShortNameAndUpdateCodeMetamacResult(Long idCodelist, String sqlQuery, Map<Long, ItemResult> mapCodeByItemId) {
        Query query = getEntityManager().createNativeQuery(sqlQuery);
        query.setParameter("codelistVersion", idCodelist);
        List shortNameResultsSql = query.getResultList();
        for (Object shortNameResultSql : shortNameResultsSql) {
            Object[] shortNameResultSqlArray = (Object[]) shortNameResultSql;
            Long actualItemId = getLong(shortNameResultSqlArray[0]);
            ItemResult target = mapCodeByItemId.get(actualItemId);
            internationalStringResultSqltoInternationalStringResult(shortNameResultSqlArray, ((CodeMetamacResultExtensionPoint) target.getExtensionPoint()).getShortName());
        }
    }

    /**
     * NOTE: this query return null label if locale not exits for a code
     */
    @SuppressWarnings("rawtypes")
    private List executeQueryFindCodesByCodelistByNativeSqlQuery(Long idCodelist, String locale, Integer orderColumnIndex, Integer opennessColumnIndex) {
        // default values, to execute same query, but in transformation to result they will be ignored
        if (orderColumnIndex == null) {
            orderColumnIndex = Integer.valueOf(1);
        }
        if (opennessColumnIndex == null) {
            opennessColumnIndex = Integer.valueOf(1);
        }
        String orderColumn = getOrderColumnName(orderColumnIndex);
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, c." + orderColumn + ", c." + opennessColumn + " ");
        sb.append("FROM TB_M_CODES c INNER JOIN TB_ITEMS_BASE i on c.TB_CODES = i.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale ");
        sb.append("WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion");
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("codelistVersion", idCodelist);
        query.setParameter("locale", locale);
        List codesResultSql = query.getResultList();
        return codesResultSql;
    }

    private CodeMetamacVisualisationResult codeResultSqlToCodeResult(Object[] source, CodeMetamacVisualisationResult target, Integer orderColumnIndex, Integer opennessColumnIndex)
            throws MetamacException {
        if (target == null) {
            target = new CodeMetamacVisualisationResult();
        }
        int i = 0;
        target.setItemIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setParentIdDatabase(getLong(source[i++]));
        target.setName(getString(source[i++]));
        if (orderColumnIndex != null) {
            target.setOrder(getInteger(source[i++]));
        } else {
            i++; // skip this column
        }
        if (opennessColumnIndex != null) {
            target.setOpenness(getBoolean(source[i++]));
        } else {
            i++; // skip this column
        }
        return target;
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

    private String getOrderColumnName(Integer columnIndex) {
        return "ORDER" + columnIndex;
    }

    private String getOpennessColumnName(Integer columnIndex) {
        return "OPENNESS" + columnIndex;
    }

    private String getOpennessColumnValue(Boolean expanded) {
        return booleanToBooleanDatabase(expanded);
    }

    /**
     * Checks text of Annotations
     */
    @SuppressWarnings("rawtypes")
    private void checkCodeMetamacTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.CODE) ");
        sb.append("FROM TB_ITEMS_BASE i ");
        sb.append("INNER JOIN TB_M_CODES c on TB_CODES = i.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("i.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND c.SHORT_NAME_FK IS NOT NULL AND ls.LABEL IS NULL ");
        sb.append("ORDER BY a.CODE ASC");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String code = getString(resultSql);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, ServiceExceptionParameters.CODE_SHORT_NAME, code));
        }
    }
}
