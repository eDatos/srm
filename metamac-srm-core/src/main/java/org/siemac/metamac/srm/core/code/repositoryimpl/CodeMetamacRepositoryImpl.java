package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodeMetamac
 */
@Repository("codeMetamacRepository")
public class CodeMetamacRepositoryImpl extends CodeMetamacRepositoryBase {

    @Autowired
    // private CodeRepository codeRepository;
    // note: this query return null label if locale not exits for a code
    private final static String NATIVE_SQL_QUERY_CODES_BY_CODELIST_ORDERED = "SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, o.CODE_INDEX as CODE_ORDER FROM TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale LEFT OUTER JOIN TB_M_CODE_ORDER_VISUAL o on o.CODE_FK = i.ID WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion AND o.CODELIST_VISUALISATION_FK = :codelistVisualisation";
    private final static String NATIVE_SQL_QUERY_CODES_BY_CODELIST         = "SELECT i.ID as ITEM_ID, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL FROM TB_ITEMS i INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale WHERE i.ITEM_SCHEME_VERSION_FK = :codelistVersion";

    // private final static String NATIVE_SQL_QUERY_CODES_DESCRIPTIONS_BY_CODELIST =
    // "SELECT a.ID as ANNOTABLE_ARTEFACT_ID, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL from TB_ANNOTABLE_ARTEFACTS a left outer join TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.DESCRIPTION_FK WHERE a.ID in (select i.NAMEABLE_ARTEFACT_FK FROM TB_ITEMS i where i.ITEM_SCHEME_VERSION_FK = :codelistVersion)";

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
    // @SuppressWarnings("rawtypes")
    public List<CodeMetamacResult> findCodesByCodelistByNativeSqlQuery(Long idCodelist) {
        // TODO
        // Find codes
        // List<ItemResult> codes = codeRepository.findCodesByCodelistByNativeSqlQuery(idCodelist, Boolean.TRUE);

        // Fill short name
        // List descriptionsResultSql = executeNativeQuerySql(NATIVE_SQL_QUERY_CODES_DESCRIPTIONS_BY_CODELIST, idCodelist);
        // for (Object shortNameResultSql : descriptionsResultSql) {
        // Object[] descriptionResultSqlArray = (Object[]) shortNameResultSql;
        // Long actualAnnotableArtefactId = getLong(descriptionResultSqlArray[0]);
        // ItemResult target = mapCodeByAnnotableArtefactId.get(actualAnnotableArtefactId);
        // internationalStringResultSqltoInternationalStringResult(descriptionResultSqlArray, target.getDescription());
        // }
        return null;

    }

    @SuppressWarnings("rawtypes")
    private List executeQueryFindCodesByCodelistByNativeSqlQuery(Long idCodelist, String locale, Long idCodelistOrderVisualisation) {
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

}
