package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Repository implementation for CodeMetamac
 */
@Repository("codeMetamacRepository")
public class CodeMetamacRepositoryImpl extends CodeMetamacRepositoryBase {

    @Autowired
    private CodeRepository      codeRepository;

    private final static String NATIVE_SQL_QUERY_CODES_BY_CODELIST                             = "SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL FROM TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion";

    private final static String NATIVE_SQL_QUERY_CODES_VARIABLE_ELEMENT_SHORT_NAME_BY_CODELIST = "select c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_M_CODES c INNER JOIN TB_ITEMS i on i.ID = c.TB_CODES INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK WHERE c.VARIABLE_ELEMENT_FK is not null AND  i.ITEM_SCHEME_VERSION_FK = :codelistVersion";
    private final static String NATIVE_SQL_QUERY_CODES_SHORT_NAME_BY_CODELIST                  = "select c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_M_CODES c INNER JOIN TB_ITEMS i on i.ID = c.TB_CODES LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK WHERE c.VARIABLE_ELEMENT_FK is null AND  i.ITEM_SCHEME_VERSION_FK = :codelistVersion and c.SHORT_NAME_FK is not null";

    public CodeMetamacRepositoryImpl() {
    }

    // TODO strings? si no se ponen como constantes, crear StringBuilder para poder unir las dos consultas de cada método

    @Override
    public void reorderCodesDeletingOneCode(CodelistVersionMetamac codelistVersion, Item parent, CodeMetamac code, Integer columnIndex, Integer orderCodeToDelete) {
        String column = getOrderColumnName(columnIndex);
        Query queryUpdate = null;
        if (parent == null) {
            queryUpdate = getEntityManager().createNativeQuery(
                    "UPDATE TB_M_CODES set " + column + " = " + column + " - 1 "
                            + "WHERE TB_CODES in (select i.ID from TB_ITEMS i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK is null) AND " + column + " > :order");
        } else {
            queryUpdate = getEntityManager().createNativeQuery(
                    "UPDATE TB_M_CODES set " + column + " = " + column + " - 1 "
                            + "WHERE TB_CODES in (select i.ID from TB_ITEMS i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK = :parent) AND " + column + " > :order");
            queryUpdate.setParameter("parent", parent.getId());
        }
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.setParameter("order", orderCodeToDelete);
        queryUpdate.executeUpdate();
    }

    @Override
    public void reorderCodesAddingOneCodeInMiddle(CodelistVersionMetamac codelistVersion, CodeMetamac code, Integer columnIndex, Integer orderCodeToAdd) {
        String column = getOrderColumnName(columnIndex);
        Query queryUpdate = null;
        if (code.getParent() == null) {
            queryUpdate = getEntityManager().createNativeQuery(
                    "UPDATE TB_M_CODES set " + column + " = " + column + " + 1 "
                            + "WHERE TB_CODES in (select i.ID from TB_ITEMS i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK is null) AND " + column + " >= :order");
        } else {
            queryUpdate = getEntityManager().createNativeQuery(
                    "UPDATE TB_M_CODES set " + column + " = " + column + " + 1 "
                            + "WHERE TB_CODES in (select i.ID from TB_ITEMS i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK = :parent) AND " + column + " >= :order");
            queryUpdate.setParameter("parent", code.getParent().getId());
        }
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.setParameter("order", orderCodeToAdd);
        queryUpdate.executeUpdate();
    }

    @Override
    public void clearCodeOrders(CodelistVersionMetamac codelistVersion, Integer columnIndex) {
        String column = getOrderColumnName(columnIndex);
        Query queryUpdate = getEntityManager().createNativeQuery(
                "UPDATE TB_M_CODES set " + column + " = null WHERE TB_CODES in (select i.ID from TB_ITEMS i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion)");
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void copyCodeOrders(CodelistVersionMetamac codelistVersion, Integer columnIndexSource, Integer columnIndexTarget) {
        String columnSource = getOrderColumnName(columnIndexSource);
        String columnTarget = getOrderColumnName(columnIndexTarget);
        Query queryUpdate = getEntityManager().createNativeQuery(
                "UPDATE TB_M_CODES set " + columnTarget + " = " + columnSource + " WHERE TB_CODES in (select i.ID from TB_ITEMS i WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion)");
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Integer getCodeMaximumOrderInLevel(CodelistVersionMetamac codelistVersion, Item parent, Integer columnIndex) {
        String column = getOrderColumnName(columnIndex);
        Query query = null;
        if (parent == null) {
            query = getEntityManager().createNativeQuery(
                    "SELECT max(" + column + ") from TB_M_CODES c INNER JOIN TB_ITEMS i on i.ID = c.TB_CODES WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK is null");
        } else {
            query = getEntityManager().createNativeQuery(
                    "SELECT max(" + column + ") from TB_M_CODES c INNER JOIN TB_ITEMS i on i.ID = c.TB_CODES WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND i.PARENT_FK = :parent");
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

    @Override
    public Integer getCodeAlphabeticPositionInLevel(CodelistVersionMetamac codelistVersion, Item parent, Item code) {
        Query query = null;
        if (parent == null) {
            query = getEntityManager().createNativeQuery(
                    "SELECT COUNT(1) from TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND a.CODE < '"
                            + code.getNameableArtefact().getCode() + "' AND i.PARENT_FK is null ");
        } else {
            query = getEntityManager().createNativeQuery(
                    "SELECT COUNT(1) from TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND a.CODE < '"
                            + code.getNameableArtefact().getCode() + "' AND i.PARENT_FK = :parent");
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
    @SuppressWarnings("rawtypes")
    public List<CodeMetamacVisualisationResult> findCodesByCodelistUnordered(Long idCodelist, String locale, Integer orderColumnIndex) {
        // Find codes
        List codesResultSql = executeQueryFindCodesByCodelistByNativeSqlQuery(idCodelist, locale, orderColumnIndex);

        // Transform object[] results
        List<CodeMetamacVisualisationResult> targets = new ArrayList<CodeMetamacVisualisationResult>(codesResultSql.size());
        Map<Long, CodeMetamacVisualisationResult> mapCodeByItemId = new HashMap<Long, CodeMetamacVisualisationResult>(codesResultSql.size());
        for (Object codeResultSql : codesResultSql) {
            Object[] codeResultSqlArray = (Object[]) codeResultSql;
            Long actualItemId = getLong(codeResultSqlArray[0]);
            CodeMetamacVisualisationResult target = mapCodeByItemId.get(actualItemId);
            if (target == null) {
                target = codeResultSqlToCodeResult(codeResultSqlArray, null, orderColumnIndex);
                targets.add(target);
                mapCodeByItemId.put(target.getItemIdDatabase(), target);
            } else {
                codeResultSqlToCodeResult(codeResultSqlArray, target, orderColumnIndex);
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

    @Override
    public List<ItemResult> findCodesByCodelistOrderedInDepth(Long idCodelist, Integer orderColumnIndex) {

        // Find codes
        List<ItemResult> codes = codeRepository.findCodesByCodelistUnordered(idCodelist, Boolean.TRUE);

        // Init extension point and index by id
        Map<Long, ItemResult> mapCodeByItemId = new HashMap<Long, ItemResult>(codes.size());
        for (ItemResult itemResult : codes) {
            itemResult.setExtensionPoint(new CodeMetamacResultExtensionPoint());
            mapCodeByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        // Fill short name
        // Try fill from variable element
        executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, NATIVE_SQL_QUERY_CODES_VARIABLE_ELEMENT_SHORT_NAME_BY_CODELIST, mapCodeByItemId);
        // If code has not variable element, try fill with short name in code
        executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, NATIVE_SQL_QUERY_CODES_SHORT_NAME_BY_CODELIST, mapCodeByItemId);

        // Order // TODO y columna X
        // // Query query1 = getEntityManager().createNativeQuery(
        // // "SELECT COD_TB_CODES, COD_ORDER1, ITEM_ID, ITEM_PARENT_FK, LEVEL, SYS_CONNECT_BY_PATH(COD_ORDER1, '.') ORDER_PATH " + "FROM " + "("
        // // + "SELECT codes.tb_codes COD_TB_CODES, codes.ORDER1 COD_ORDER1, items.ID ITEM_ID, items.PARENT_FK ITEM_PARENT_FK "
        // // + "FROM TB_M_CODES codes JOIN TB_ITEMS items on items.ID = codes.TB_CODES " + "WHERE items.item_scheme_version_fk = :codelistVersion order by codes.ORDER1 asc"
        // // + ") START WITH ITEM_PARENT_FK is null " + "CONNECT BY PRIOR ITEM_ID = ITEM_PARENT_FK " + "ORDER BY ORDER_PATH");
        // Query query1 = getEntityManager().createNativeQuery(
        // "SELECT ITEM_ID, COD_ORDER1, SYS_CONNECT_BY_PATH(lpad(COD_ORDER1, 7, '0'), '.') ORDER_PATH " + "FROM " + "(" + "SELECT c.ORDER1 COD_ORDER1, i.ID ITEM_ID, i.PARENT_FK ITEM_PARENT_FK "
        // + "FROM TB_M_CODES c JOIN TB_ITEMS i on i.ID = c.TB_CODES " + "WHERE i.item_scheme_version_fk = :codelistVersion " + "order by c.ORDER1 asc " + ") "
        // + "START WITH ITEM_PARENT_FK is null " + "CONNECT BY PRIOR ITEM_ID = ITEM_PARENT_FK " + "ORDER BY ORDER_PATH asc");
        // // TODO mirar si hay q hacer order by dentro
        // query1.setParameter("codelistVersion", idCodelist);
        // List resultsOrder = query1.getResultList();
        // // TODO lpad con 6

        // List<ItemResult> ordered = new ArrayList<ItemResult>(codes.size());
        // for (Object resultOrder : resultsOrder) {
        // Long codeId = getLong(((Object[]) resultOrder)[0]);
        // ItemResult code = mapCodeByItemId.get(codeId);
        // ordered.add(code);
        // }
        // return ordered;
        return codes;
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

    @SuppressWarnings("rawtypes")
    private List executeQueryFindCodesByCodelistByNativeSqlQuery(Long idCodelist, String locale, Integer columnIndex) {
        // note: this query return null label if locale not exits for a code
        String sqlQuery = null;
        if (columnIndex != null) {
            String column = getOrderColumnName(columnIndex);
            sqlQuery = "SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, c." + column + " FROM TB_M_CODES c INNER JOIN TB_ITEMS i on c.TB_CODES = i.ID "
                    + "INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale "
                    + "WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion";
        } else {
            sqlQuery = NATIVE_SQL_QUERY_CODES_BY_CODELIST;
        }
        Query query = getEntityManager().createNativeQuery(sqlQuery);
        query.setParameter("codelistVersion", idCodelist);
        query.setParameter("locale", locale);
        List codesResultSql = query.getResultList();
        return codesResultSql;
    }

    private CodeMetamacVisualisationResult codeResultSqlToCodeResult(Object[] source, CodeMetamacVisualisationResult target, Integer orderColumnIndex) {
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
        }
        return target;
    }

    private String getString(Object source) {
        return source != null ? (String) source : null;
    }

    private Long getLong(Object source) {
        return source != null ? Long.valueOf(source.toString()) : null;
    }

    private Integer getInteger(Object source) {
        return source != null ? Integer.valueOf(source.toString()) : null;
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
}
