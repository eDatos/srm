package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Repository implementation for CodeMetamac
 */
@Repository("codeMetamacRepository")
public class CodeMetamacRepositoryImpl extends CodeMetamacRepositoryBase {

    @Autowired
    private CodeRepository      codeRepository;

    private final static String NATIVE_SQL_QUERY_CODES_BY_CODELIST_ORDERED                     = "SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, o.CODE_INDEX as CODE_ORDER FROM TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale LEFT OUTER JOIN TB_M_CODE_ORDER_VISUAL o on o.CODE_FK = i.ID WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND o.CODELIST_VISUALISATION_FK = :codelistVisualisation";
    private final static String NATIVE_SQL_QUERY_CODES_BY_CODELIST                             = "SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL FROM TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion";

    private final static String NATIVE_SQL_QUERY_CODES_VARIABLE_ELEMENT_SHORT_NAME_BY_CODELIST = "select c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_M_CODES c LEFT OUTER JOIN TB_ITEMS i on i.ID = c.TB_CODES INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK where c.VARIABLE_ELEMENT_FK is not null AND  i.ITEM_SCHEME_VERSION_FK = :codelistVersion";
    private final static String NATIVE_SQL_QUERY_CODES_SHORT_NAME_BY_CODELIST                  = "select c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_M_CODES c LEFT OUTER JOIN TB_ITEMS i on i.ID = c.TB_CODES LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK where c.VARIABLE_ELEMENT_FK is null AND  i.ITEM_SCHEME_VERSION_FK = :codelistVersion and c.SHORT_NAME_FK is not null";

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
    @SuppressWarnings("rawtypes")
    public List<CodeMetamacVisualisationResult> findCodesVisualisationByCodelistByNativeSqlQuery(Long idCodelist, String locale, Long idCodelistOrderVisualisation) {

        // Find codes
        List codesResultSql = executeQueryFindCodesByCodelistByNativeSqlQuery(idCodelist, locale, idCodelistOrderVisualisation);
        List<CodeMetamacVisualisationResult> targets = new ArrayList<CodeMetamacVisualisationResult>(codesResultSql.size());
        Map<Long, CodeMetamacVisualisationResult> mapCodeByItemId = new HashMap<Long, CodeMetamacVisualisationResult>(codesResultSql.size());
        for (Object codeResultSql : codesResultSql) {
            Object[] codeResultSqlArray = (Object[]) codeResultSql;
            Long actualItemId = getLong(codeResultSqlArray[0]);
            CodeMetamacVisualisationResult target = mapCodeByItemId.get(actualItemId);
            if (target == null) {
                target = codeResultSqlToCodeResult(codeResultSqlArray, null, idCodelistOrderVisualisation);
                targets.add(target);
                mapCodeByItemId.put(target.getItemIdDatabase(), target);
            } else {
                codeResultSqlToCodeResult(codeResultSqlArray, target, idCodelistOrderVisualisation);
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
    public List<ItemResult> findCodesByCodelistByNativeSqlQuery(Long idCodelist) {
        // Find codes
        List<ItemResult> codes = codeRepository.findCodesByCodelistByNativeSqlQuery(idCodelist, Boolean.TRUE);

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
    private List executeQueryFindCodesByCodelistByNativeSqlQuery(Long idCodelist, String locale, Long idCodelistOrderVisualisation) {
        // note: this query return null label if locale not exits for a code
        String sqlQuery = idCodelistOrderVisualisation != null ? NATIVE_SQL_QUERY_CODES_BY_CODELIST_ORDERED : NATIVE_SQL_QUERY_CODES_BY_CODELIST;
        Query query = getEntityManager().createNativeQuery(sqlQuery);
        query.setParameter("codelistVersion", idCodelist);
        query.setParameter("locale", locale);
        if (idCodelistOrderVisualisation != null) {
            query.setParameter("codelistVisualisation", idCodelistOrderVisualisation);
        }
        List codesResultSql = query.getResultList();
        return codesResultSql;
    }

    private CodeMetamacVisualisationResult codeResultSqlToCodeResult(Object[] source, CodeMetamacVisualisationResult target, Long idCodelistOrderVisualisation) {
        if (target == null) {
            target = new CodeMetamacVisualisationResult();
        }
        int i = 0;
        target.setItemIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setParentIdDatabase(getLong(source[i++]));
        target.setName(getString(source[i++]));
        if (idCodelistOrderVisualisation != null) {
            target.setOrder(getLong(source[i++]));
        }
        return target;
    }

    private String getString(Object source) {
        return source != null ? (String) source : null;
    }

    private Long getLong(Object source) {
        return source != null ? Long.valueOf(source.toString()) : null;
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

}
