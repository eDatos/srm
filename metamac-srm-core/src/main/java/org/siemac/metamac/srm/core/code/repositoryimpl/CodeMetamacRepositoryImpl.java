package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.booleanToBooleanDatabase;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getBoolean;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDate;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getInteger;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addSubexceptionToExceptionItemByResource;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addTranslationExceptionToExceptionItemsByResource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

/**
 * Repository implementation for CodeMetamac
 */
@Repository("codeMetamacRepository")
public class CodeMetamacRepositoryImpl extends CodeMetamacRepositoryBase {

    @Autowired
    private ItemRepository                itemRepository;

    @Autowired
    private CodeRepository                codeRepository;

    @Autowired
    private InternationalStringRepository internationalStringRepository;

    @Autowired
    private SrmConfiguration              srmConfiguration;

    private final Integer                 ORDER_DEFAULT    = Integer.valueOf(1);
    private final Integer                 OPENNESS_DEFAULT = Integer.valueOf(1);

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
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void updateCodesVariableElements(Map<Long, Long> variableElementsByCodes, Long codelistVersionId, Long variableId) throws MetamacException {

        Map<Long, Long> shortNamesCandidateToDelete = executeSelectShortNamesByCodes(variableElementsByCodes.keySet());

        String sqlUpdateWithVariableElement = buildQueryUpdateCodeVariableElement(true);
        String sqlUpdateWithoutVariableElement = buildQueryUpdateCodeVariableElement(false);

        for (Long codeId : variableElementsByCodes.keySet()) {
            Long variableElementId = variableElementsByCodes.get(codeId);

            // Update
            String sql = variableElementId != null ? sqlUpdateWithVariableElement : sqlUpdateWithoutVariableElement;
            Query queryUpdate = getEntityManager().createNativeQuery(sql);
            queryUpdate.setParameter("codelistVersionId", codelistVersionId);
            queryUpdate.setParameter("codeId", codeId);
            if (variableElementId != null) {
                queryUpdate.setParameter("variableId", variableId);
                queryUpdate.setParameter("variableElementId", variableElementId);
            }
            int updateResult = queryUpdate.executeUpdate();

            if (updateResult > 1) {
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "Update result can not be more than one when updating variable element of code: code = " + codeId + "; variableElementId = "
                        + variableElementId);
            } else if (updateResult == 0) {
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "Code or variable element incorrect: code = " + codeId + "; variableElementId = " + variableElementId);
            }
            if (variableElementId == null) {
                // short name is not cleared
                shortNamesCandidateToDelete.remove(codeId);
            }
        }
        // Delete short names
        internationalStringRepository.deleteInternationalStringsEfficiently(shortNamesCandidateToDelete.values());
    }

    private String buildQueryUpdateCodeVariableElement(boolean variableElementNotNull) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES SET ");
        if (variableElementNotNull) {
            sb.append("VARIABLE_ELEMENT_FK = :variableElementId, SHORT_NAME_FK = null ");
        } else {
            sb.append("VARIABLE_ELEMENT_FK = null ");
        }
        sb.append("WHERE ");
        sb.append("    TB_CODES = :codeId ");
        sb.append("   AND ");
        // Check code belongs to codelist
        sb.append("    TB_CODES = ");
        sb.append("   (SELECT cb.ID ");
        sb.append("    FROM TB_CODES cb ");
        sb.append("    WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersionId ");
        sb.append("    AND cb.ID = :codeId) ");
        // Check variable element belongs to variable of codelist
        if (variableElementNotNull) {
            sb.append("   AND ");
            sb.append("   :variableElementId = ");
            sb.append("   (SELECT ve.ID ");
            sb.append("   FROM TB_M_VARIABLE_ELEMENTS ve ");
            sb.append("   WHERE ve.VARIABLE_FK = :variableId ");
            sb.append("   AND ve.ID = :variableElementId) ");
        }
        return sb.toString();
    }

    @Override
    public void reorderCodesDeletingOneCode(CodelistVersionMetamac codelistVersion, Code parent, CodeMetamac code, Integer orderColumnIndex, Integer orderCodeToDelete) {
        String orderColumn = getOrderColumnName(orderColumnIndex);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + orderColumn + " = " + orderColumn + " - 1 ");
        sb.append("WHERE TB_CODES in ");
        sb.append("(SELECT cb.ID ");
        sb.append("FROM TB_CODES cb ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        if (parent == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
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
        sb.append("SELECT cb.ID ");
        sb.append("FROM TB_CODES cb ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        if (code.getParent() == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
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
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void copyCodesOrderColumn(CodelistVersionMetamac codelistVersion, Integer columnIndexSource, Integer columnIndexTarget) {
        String columnSource = getOrderColumnName(columnIndexSource);
        String columnTarget = getOrderColumnName(columnIndexTarget);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + columnTarget + " = " + columnSource + " ");
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void clearCodesOpennessColumnByCodelist(CodelistVersionMetamac codelistVersion, Integer opennessColumnIndex) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = null ");
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void copyCodesOpennessColumnByCodelist(CodelistVersionMetamac codelistVersion, Integer columnIndexSource, Integer columnIndexTarget) {
        String columnSource = getOpennessColumnName(columnIndexSource);
        String columnTarget = getOpennessColumnName(columnIndexTarget);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + columnTarget + " = " + columnSource + " ");
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void updateCodesOpennessColumnByCodelist(CodelistVersionMetamac codelistVersion, Integer opennessColumnIndex, Boolean expanded) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = " + getOpennessColumnValue(expanded) + " ");
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @Override
    public void updateCodeOpennessColumn(CodelistVersionMetamac codelistVersion, String codeUrn, Integer opennessColumnIndex, Boolean expanded) {
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CODES set " + opennessColumn + " = " + getOpennessColumnValue(expanded) + " ");
        sb.append("WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID WHERE a.URN = :codeUrn AND cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
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
        sb.append("TB_CODES IN (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion) ");
        sb.append("AND ");
        sb.append("TB_CODES NOT IN (SELECT DISTINCT(cb.PARENT_FK) FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion AND cb.PARENT_FK IS NOT NULL) ");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("codelistVersion", codelistVersion.getId());
        queryUpdate.executeUpdate();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Integer getCodeMaximumOrderInLevel(CodelistVersionMetamac codelistVersion, Code parent, Integer orderColumnIndex) {
        String orderColumn = getOrderColumnName(orderColumnIndex);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT max(" + orderColumn + ") ");
        sb.append("FROM TB_M_CODES c INNER JOIN TB_CODES cb on cb.ID = c.TB_CODES WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        if (parent == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
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
    public Integer getCodeAlphabeticPositionInLevel(CodelistVersionMetamac codelistVersion, Code parent, Code code) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) ");
        sb.append("FROM TB_CODES cb ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        sb.append("AND lower(a.CODE) < lower('" + code.getNameableArtefact().getCode() + "') ");
        if (parent == null) {
            sb.append("AND cb.PARENT_FK is null");
        } else {
            sb.append("AND cb.PARENT_FK = :parent");
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
    public List<CodeMetamacVisualisationResult> findCodesByCodelistUnorderedToVisualisation(Long idCodelist, String locale, Integer orderColumnIndex, Integer opennessColumnIndex)
            throws MetamacException {
        // Visualisations: default values, to execute same query, but in transformation to result they will be ignored
        if (orderColumnIndex == null) {
            orderColumnIndex = ORDER_DEFAULT;
        }
        if (opennessColumnIndex == null) {
            opennessColumnIndex = OPENNESS_DEFAULT;
        }
        String orderColumn = getOrderColumnName(orderColumnIndex);
        String opennessColumn = getOpennessColumnName(opennessColumnIndex);

        // Find. NOTE: this query return null label if locale not exits for a code
        StringBuilder sbCodes = new StringBuilder();
        sbCodes.append("SELECT cb.ID as ITEM_ID, cb.CREATED_DATE, cb.CREATED_DATE_TZ, a.URN, a.CODE, cb.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, c." + orderColumn + ", c." + opennessColumn + " ");
        sbCodes.append("FROM TB_M_CODES c ");
        sbCodes.append("INNER JOIN TB_CODES cb on cb.ID = c.TB_CODES ");
        sbCodes.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sbCodes.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale ");
        sbCodes.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion");
        Query queryCodes = getEntityManager().createNativeQuery(sbCodes.toString());
        queryCodes.setParameter("codelistVersion", idCodelist);
        queryCodes.setParameter("locale", locale);
        List codesResultSql = queryCodes.getResultList();

        // Transform object[] results
        List<CodeMetamacVisualisationResult> targets = new ArrayList<CodeMetamacVisualisationResult>(codesResultSql.size());
        Map<Long, ItemVisualisationResult> mapCodeByItemId = new HashMap<Long, ItemVisualisationResult>(codesResultSql.size());
        for (Object codeResultSql : codesResultSql) {
            Object[] codeResultSqlArray = (Object[]) codeResultSql;
            CodeMetamacVisualisationResult target = codeResultSqlToCodeResult(codeResultSqlArray, null, orderColumnIndex, opennessColumnIndex);
            targets.add(target);
            mapCodeByItemId.put(target.getItemIdDatabase(), target);
        }

        // Add description. Execute one independent query to retrieve it is more efficient than do a global query
        itemRepository.executeQueryFillItemDescription(idCodelist, Code.class, locale, mapCodeByItemId);

        // Variable element. Execute one independent query to retrieve variable elements is more efficient than do a global query
        StringBuilder sbVariableElements = new StringBuilder();
        sbVariableElements.append("SELECT cb.ID as ITEM_ID, ve.ID as VE_ID, ave.URN as VE_URN, ave.CODE as VE_CODE, ls.LABEL as VE_SHORT_NAME_FK ");
        sbVariableElements.append("FROM TB_M_CODES c ");
        sbVariableElements.append("INNER JOIN TB_CODES cb on c.TB_CODES = cb.ID ");
        sbVariableElements.append("INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK ");
        sbVariableElements.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ave on ve.IDENTIFIABLE_ARTEFACT_FK = ave.ID ");
        sbVariableElements.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK and ls.locale = :locale ");
        sbVariableElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        Query queryVariableElements = getEntityManager().createNativeQuery(sbVariableElements.toString());
        queryVariableElements.setParameter("codelistVersion", idCodelist);
        queryVariableElements.setParameter("locale", locale);
        List variableElementsResultSql = queryVariableElements.getResultList();
        for (Object variableElementResultSql : variableElementsResultSql) {
            Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
            Long actualItemId = getLong(variableElementResultSqlArray[0]);
            CodeMetamacVisualisationResult target = (CodeMetamacVisualisationResult) mapCodeByItemId.get(actualItemId);
            VariableElementVisualisationResult variableElement = variableElementResultSqlToVariableElementVisualisationResult(variableElementResultSqlArray);
            target.setVariableElement(variableElement);
        }

        // Parent: fill manually with java code
        for (ItemVisualisationResult target : targets) {
            if (target.getParentIdDatabase() != null) {
                ItemVisualisationResult parent = mapCodeByItemId.get(target.getParentIdDatabase());
                target.setParent(parent);
            }
        }
        return targets;
    }

    @Override
    public List<ItemResult> findCodesByCodelistUnordered(Long idCodelist, ItemMetamacResultSelection resultSelection) throws MetamacException {

        // Find codes
        List<ItemResult> codes = codeRepository.findCodesByCodelistUnordered(idCodelist, resultSelection);

        // Init extension point and index by id
        Map<Long, ItemResult> mapCodeByItemId = new HashMap<Long, ItemResult>(codes.size());
        for (ItemResult itemResult : codes) {
            itemResult.setExtensionPoint(new CodeMetamacResultExtensionPoint());
            mapCodeByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        if (resultSelection.isInternationalStringsMetamac()) {
            // Fill short name
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL ");
            sb.append("FROM TB_M_CODES c INNER JOIN TB_CODES cb on cb.ID = c.TB_CODES ");
            sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK ");
            sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion and c.SHORT_NAME_FK is not null");
            executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, sb.toString(), mapCodeByItemId);
        }
        return codes;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<ItemResult> findCodesByCodelistOrderedInDepth(Long idCodelist, Integer orderColumnIndex, Integer opennessColumnIndex, ItemMetamacResultSelection resultSelection)
            throws MetamacException {

        // Find codes
        List<ItemResult> codes = codeRepository.findCodesByCodelistUnordered(idCodelist, resultSelection);

        // Init extension point and index by id
        Map<Long, ItemResult> mapCodeByItemId = new HashMap<Long, ItemResult>(codes.size());
        for (ItemResult itemResult : codes) {
            itemResult.setExtensionPoint(new CodeMetamacResultExtensionPoint());
            mapCodeByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        if (resultSelection.isInternationalStringsMetamac()) {
            // Fill short name
            {
                // Try fill from variable element
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL ");
                sb.append("FROM TB_M_CODES c ");
                sb.append("INNER JOIN TB_CODES cb on cb.ID = c.TB_CODES ");
                sb.append("INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK ");
                sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK ");
                sb.append("WHERE c.VARIABLE_ELEMENT_FK is not null AND cb.ITEM_SCHEME_VERSION_FK = :codelistVersion");
                executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, sb.toString(), mapCodeByItemId);
            }
            {
                // If code has not variable element, try fill with short name in code
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT c.TB_CODES, ls.LOCALE as LS_LOCALE, ls.LABEL as LS_LABEL ");
                sb.append("FROM TB_M_CODES c INNER JOIN TB_CODES cb on cb.ID = c.TB_CODES ");
                sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK ");
                sb.append("WHERE c.VARIABLE_ELEMENT_FK is null AND cb.ITEM_SCHEME_VERSION_FK = :codelistVersion and c.SHORT_NAME_FK is not null");
                executeQueryCodeShortNameAndUpdateCodeMetamacResult(idCodelist, sb.toString(), mapCodeByItemId);
            }
        }

        if (resultSelection instanceof CodeMetamacResultSelection) {
            CodeMetamacResultSelection codeResultSelection = (CodeMetamacResultSelection) resultSelection;
            // Variable element. Execute one independent query to retrieve variable elements is more efficient than do a global query
            if (codeResultSelection.isVariableElementCode()) {
                executeQueryCodeVariableElementAndUpdateCodeMetamacResult(idCodelist, mapCodeByItemId);
            } else if (codeResultSelection.isVariableElementFull()) {
                executeQueryFullVariableElementAndUpdateCodeMetamacResult(idCodelist, mapCodeByItemId);
            }
        }

        // Order and openness selected
        if (orderColumnIndex != null || opennessColumnIndex != null) {
            executeQueryCodeVisualisationAndUpdateCodeMetamacResult(idCodelist, orderColumnIndex, opennessColumnIndex, mapCodeByItemId);
        }

        // Retrieve items id ordered
        String orderColumn = getOrderColumnName(orderColumnIndex != null ? orderColumnIndex : ORDER_DEFAULT); // get column name. any if no order or openness is selected
        StringBuilder sb = new StringBuilder();
        int resultOrderIndex = -1;
        if (srmConfiguration.isDatabaseOracle()) {
            resultOrderIndex = 1;
            sb.append("SELECT ITEM_ID, SYS_CONNECT_BY_PATH(lpad(COD_ORDER, " + SrmConstants.CODE_QUERY_COLUMN_ORDER_LENGTH + ", '0'), '.') ORDER_PATH ");
            sb.append("FROM ");
            sb.append("(");
            sb.append("SELECT c." + orderColumn + " AS COD_ORDER, c.TB_CODES AS ITEM_ID, cb.PARENT_FK AS ITEM_PARENT_FK ");
            sb.append("FROM TB_M_CODES c ");
            sb.append("JOIN TB_CODES cb on cb.ID = c.TB_CODES ");
            sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion");
            sb.append(")");
            sb.append("START WITH ITEM_PARENT_FK is null ");
            sb.append("CONNECT BY PRIOR ITEM_ID = ITEM_PARENT_FK ");
            sb.append("ORDER BY ORDER_PATH asc");
        } else if (srmConfiguration.isDatabaseSqlServer()) {
            resultOrderIndex = 3;
            sb.append("WITH Parents(R_ID, R_SORT, R_ITEM_SCHEME_VERSION_FK, R_SORT_STRING) AS ");
            sb.append("( ");
            sb.append("SELECT c1.TB_CODES AS R_ID, c1." + orderColumn + " AS R_SORT, cb1.ITEM_SCHEME_VERSION_FK AS R_ITEM_SCHEME_VERSION_FK, ");
            sb.append("'/' + REPLICATE(0, " + SrmConstants.CODE_QUERY_COLUMN_ORDER_LENGTH + " - LEN(c1." + orderColumn + ")) + CAST(c1." + orderColumn + " AS varchar(4000)) AS R_SORT_STRING ");
            sb.append("FROM TB_M_CODES AS c1 ");
            sb.append("INNER JOIN TB_CODES AS cb1 ON cb1.ID = c1.TB_CODES ");
            sb.append("WHERE cb1.PARENT_FK IS NULL and cb1.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
            sb.append("UNION ALL ");
            sb.append("SELECT c2.TB_CODES, c2." + orderColumn + ", cb2.ITEM_SCHEME_VERSION_FK AS R_ITEM_SCHEME_VERSION_FK, ");
            sb.append("p.R_SORT_STRING + '/' + REPLICATE(0, " + SrmConstants.CODE_QUERY_COLUMN_ORDER_LENGTH + " - LEN(c2." + orderColumn + ")) + CAST(c2." + orderColumn
                    + " AS varchar(4000)) AS R_SORT_STRING ");
            sb.append("FROM TB_M_CODES AS c2 ");
            sb.append("INNER JOIN TB_CODES AS cb2 ON cb2.ID = c2.TB_CODES ");
            sb.append("INNER JOIN Parents AS p ON p.R_ID = cb2.PARENT_FK ) ");
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
            Object[] resultOrderArray = (Object[]) resultOrder;
            Long codeId = getLong(resultOrderArray[0]);
            ItemResult code = mapCodeByItemId.get(codeId);
            Object order = resultOrderArray[resultOrderIndex];
            if (order != null) {
                ((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).setOrderConcatenatedByLevel(order.toString());
            }
            ordered.add(code);
        }
        return ordered;
    }

    @Override
    public void checkCodeTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        // item common metadata
        codeRepository.checkCodeTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // concrete metadata
        checkCodeMetamacTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void checkCodesWithVariableElements(Long itemSchemeVersionId, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.URN) ");
        sb.append("FROM TB_CODES cb ");
        sb.append("INNER JOIN TB_M_CODES c on cb.ID = c.TB_CODES ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("WHERE ");
        sb.append("cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND c.VARIABLE_ELEMENT_FK IS NULL ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urn = getString(resultSql);
            addSubexceptionToExceptionItemByResource(exceptionItemsByUrn, urn, ServiceExceptionType.CODE_VARIABLE_ELEMENT_REQUIRED_WHEN_GEOGRAPHICAL, (Serializable[]) (null));
        }
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
     * Variable element. Execute one independent query to retrieve variable elements is more efficient than do a global query
     */
    @SuppressWarnings("rawtypes")
    private void executeQueryCodeVariableElementAndUpdateCodeMetamacResult(Long idCodelist, Map<Long, ItemResult> mapCodeByItemId) {
        StringBuilder sbVariableElements = new StringBuilder();
        sbVariableElements.append("SELECT c.TB_CODES as ITEM_ID, ave.CODE as VE_CODE ");
        sbVariableElements.append("FROM TB_M_CODES c ");
        sbVariableElements.append("INNER JOIN TB_CODES cb on c.TB_CODES = cb.ID ");
        sbVariableElements.append("INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK ");
        sbVariableElements.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ave on ve.IDENTIFIABLE_ARTEFACT_FK = ave.ID ");
        sbVariableElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");
        Query queryVariableElements = getEntityManager().createNativeQuery(sbVariableElements.toString());
        queryVariableElements.setParameter("codelistVersion", idCodelist);
        List variableElementsResultSql = queryVariableElements.getResultList();
        for (Object variableElementResultSql : variableElementsResultSql) {
            Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
            Long actualItemId = getLong(variableElementResultSqlArray[0]);
            String variableElementCode = getString(variableElementResultSqlArray[1]);
            ItemResult target = mapCodeByItemId.get(actualItemId);
            ((CodeMetamacResultExtensionPoint) target.getExtensionPoint()).setVariableElementCode(variableElementCode);
        }
    }

    @SuppressWarnings("rawtypes")
    private void executeQueryFullVariableElementAndUpdateCodeMetamacResult(Long idCodelist, Map<Long, ItemResult> mapCodeByItemId) {
        StringBuilder sbVariableElements = new StringBuilder();
        sbVariableElements.append("SELECT c.TB_CODES as ITEM_ID, ave.URN as VE_URN, ave.CODE as VE_CODE, ls.LOCALE as SHORT_NAME_LOCALE, ls.LABEL as SHORT_NAME_LABEL, ");
        sbVariableElements.append("graAn.CODE AS GRA_CODE, graAn.URN AS GRA_URN, graLs.LOCALE AS GRA_CODE_NAME_LOCALE, graLs.LABEL AS GRA_CODE_NAME_LABEL ");
        sbVariableElements.append("FROM TB_M_CODES c ");
        sbVariableElements.append("INNER JOIN TB_CODES cb on c.TB_CODES = cb.ID ");
        sbVariableElements.append("INNER JOIN TB_M_VARIABLE_ELEMENTS ve on ve.ID = c.VARIABLE_ELEMENT_FK ");
        sbVariableElements.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ave on ve.IDENTIFIABLE_ARTEFACT_FK = ave.ID ");
        sbVariableElements.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls ON ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK ");
        sbVariableElements.append("LEFT OUTER JOIN TB_CODES graCod ON graCod.ID = ve.GEOGRAPHICAL_GRANULARITY_FK ");
        sbVariableElements.append("LEFT OUTER JOIN TB_ANNOTABLE_ARTEFACTS graAn on graCod.NAMEABLE_ARTEFACT_FK = graAn.ID ");
        sbVariableElements.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS graLs ON graLs.INTERNATIONAL_STRING_FK = graAn.NAME_FK ");
        sbVariableElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion ");

        Query queryVariableElements = getEntityManager().createNativeQuery(sbVariableElements.toString());
        queryVariableElements.setParameter("codelistVersion", idCodelist);
        List variableElementsResultSql = queryVariableElements.getResultList();
        for (Object variableElementResultSql : variableElementsResultSql) {
            Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
            int i = 0;
            Long actualItemId = getLong(variableElementResultSqlArray[i++]);
            ItemResult target = mapCodeByItemId.get(actualItemId);
            VariableElementResult variableElementResult = ((CodeMetamacResultExtensionPoint) target.getExtensionPoint()).getVariableElement();
            if (variableElementResult == null) {
                variableElementResult = new VariableElementResult();
                ((CodeMetamacResultExtensionPoint) target.getExtensionPoint()).setVariableElement(variableElementResult);
                variableElementResult.setUrn(getString(variableElementResultSqlArray[i++]));
                variableElementResult.setCode(getString(variableElementResultSqlArray[i++]));
            } else {
                i++; // code
                i++; // urn
            }
            String locale = getString(variableElementResultSqlArray[i++]);
            if (locale == null) {
                i++;
            } else {
                String label = getString(variableElementResultSqlArray[i++]);
                variableElementResult.getShortName().put(locale, label);
            }

            // Granularity: Is Optional
            int code = i++;
            int urn = i++;
            if (variableElementResultSqlArray[code] != null && variableElementResultSqlArray[urn] != null) {
                ItemResult geographicalGranularity = variableElementResult.getGeographicalGranularity();
                if (geographicalGranularity == null) {
                    geographicalGranularity = new ItemResult();
                    variableElementResult.setGeographicalGranularity(geographicalGranularity);
                    geographicalGranularity.setCode(getString(variableElementResultSqlArray[code]));
                    geographicalGranularity.setUrn(getString(variableElementResultSqlArray[urn]));
                }
                String localeGranularity = getString(variableElementResultSqlArray[i++]);
                if (localeGranularity == null) {
                    i++; // label
                } else {
                    String labelGranularity = getString(variableElementResultSqlArray[i++]);
                    geographicalGranularity.getName().put(localeGranularity, labelGranularity);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    private void executeQueryCodeVisualisationAndUpdateCodeMetamacResult(Long idCodelist, Integer orderColumnIndex, Integer opennessColumnIndex, Map<Long, ItemResult> mapCodeByItemId)
            throws MetamacException {
        // get column names. any if no order or openness is selected. NOT returned in result
        String orderColumn = getOrderColumnName(orderColumnIndex != null ? orderColumnIndex : ORDER_DEFAULT);
        String opennessColumn = getOpennessColumnName(opennessColumnIndex != null ? opennessColumnIndex : OPENNESS_DEFAULT);

        // find
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cb.ID as ITEM_ID, c." + orderColumn + ", c." + opennessColumn + " ");
        sb.append("FROM TB_M_CODES c ");
        sb.append("INNER JOIN TB_CODES cb on cb.ID = c.TB_CODES ");
        sb.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :codelistVersion");
        Query queryCodes = getEntityManager().createNativeQuery(sb.toString());
        queryCodes.setParameter("codelistVersion", idCodelist);
        List codesResultSql = queryCodes.getResultList();
        for (Object codeResultSql : codesResultSql) {
            Object[] codeResultSqlArray = (Object[]) codeResultSql;
            int i = 0;
            Long actualItemId = getLong(codeResultSqlArray[i++]);
            ItemResult target = mapCodeByItemId.get(actualItemId);
            if (orderColumnIndex != null) {
                ((CodeMetamacResultExtensionPoint) target.getExtensionPoint()).setOrder(getInteger(codeResultSqlArray[i++]));
            } else {
                i++; // skip
            }
            if (opennessColumnIndex != null) {
                ((CodeMetamacResultExtensionPoint) target.getExtensionPoint()).setOpenness(getBoolean(codeResultSqlArray[i++]));
            } else {
                i++; // skip
            }
        }
    }

    private CodeMetamacVisualisationResult codeResultSqlToCodeResult(Object[] source, CodeMetamacVisualisationResult target, Integer orderColumnIndex, Integer opennessColumnIndex)
            throws MetamacException {
        if (target == null) {
            target = new CodeMetamacVisualisationResult();
        }
        int i = 0;
        target.setItemIdDatabase(getLong(source[i++]));
        target.setCreatedDate(getDate(source[i++], source[i++]));
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

    private VariableElementVisualisationResult variableElementResultSqlToVariableElementVisualisationResult(Object[] source) {
        VariableElementVisualisationResult target = new VariableElementVisualisationResult();
        int i = 0;
        i++; // itemId
        target.setIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setShortName(getString(source[i++]));
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
     * Checks text of Code
     */
    @SuppressWarnings("rawtypes")
    private void checkCodeMetamacTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.URN) ");
        sb.append("FROM TB_CODES cb ");
        sb.append("INNER JOIN TB_M_CODES c on cb.ID = c.TB_CODES ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("AND c.SHORT_NAME_FK IS NOT NULL AND ls.LABEL IS NULL ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urn = getString(resultSql);
            addTranslationExceptionToExceptionItemsByResource(exceptionItemsByUrn, urn, ServiceExceptionParameters.CODE_SHORT_NAME);
        }
    }

    @SuppressWarnings("rawtypes")
    private Map<Long, Long> executeSelectShortNamesByCodes(Set<Long> codes) {
        Map<Long, Long> shortNamesByCode = new HashMap<Long, Long>(codes.size()); // resulting map could have minor size, but more memory is prefered than relocate few times later

        StringBuilder codesParameter = new StringBuilder();
        int count = 0;
        int countToSqlParameter = 0;
        for (Long codeId : codes) {
            countToSqlParameter++;
            codesParameter.append(codeId);
            if (countToSqlParameter == CoreCommonConstants.SQL_IN_CLAUSE_MAXIMUM_NUMBER || count == codes.size() - 1) {
                // Execute select
                Query query = getEntityManager().createNativeQuery("SELECT TB_CODES, SHORT_NAME_FK FROM TB_M_CODES WHERE TB_CODES IN (" + codesParameter + ") AND SHORT_NAME_FK IS NOT NULL");
                List resultsSql = query.getResultList();
                for (Object resultSql : resultsSql) {
                    Object[] resultSqlArray = (Object[]) resultSql;
                    Long codeIdSqlResult = getLong(resultSqlArray[0]);
                    Long internationalStringIdSqlResult = getLong(resultSqlArray[1]);
                    shortNamesByCode.put(codeIdSqlResult, internationalStringIdSqlResult);
                }

                // Reset parameter
                countToSqlParameter = 0;
                codesParameter = new StringBuilder();
            } else {
                codesParameter.append(",");
            }
            count++;
        }
        return shortNamesByCode;
    }

    @Override
    public void deleteAllCodesEfficiently(final Long itemSchemeVersionId) throws MetamacException {
        Session session = (Session) getEntityManager().getDelegate();

        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {

                    // Mark international strings to delete: Short name, name, description, comment, annotation text...
                    insertInternationalStringToDeleteFromCodeMetamac(connection, itemSchemeVersionId, "SHORT_NAME_FK");
                    insertInternationalStringToDeleteFromCodeNameableArtefact(connection, itemSchemeVersionId, "NAME_FK");
                    insertInternationalStringToDeleteFromCodeNameableArtefact(connection, itemSchemeVersionId, "DESCRIPTION_FK");
                    insertInternationalStringToDeleteFromCodeNameableArtefact(connection, itemSchemeVersionId, "COMMENT_FK");
                    insertInternationalStringToDeleteFromAnnotation(connection, itemSchemeVersionId, "TEXT_FK");

                    // Codes and annotable artefacts
                    deleteCodeAnnotationsEfficiently(connection, itemSchemeVersionId);
                    List<Long> annotableArtefactsToDelete = getAnnotableArtefactsToDelete(connection, itemSchemeVersionId); // need obtain before delete codes
                    deleteCodesEfficiently(connection, itemSchemeVersionId);
                    deleteAnnotableArtefactsEfficiently(connection, annotableArtefactsToDelete);
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting item scheme version " + itemSchemeVersionId, e);
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Error deleting item scheme version " + itemSchemeVersionId + ": " + e.getMessage());
        }
    }

    /**
     * Insert in temporal table with entities to delete
     */
    private void insertInternationalStringToDeleteFromCodeMetamac(Connection connection, Long itemSchemeVersionId, String columnName) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_ENTITIES_TO_DELETE ");
        sb.append("(TABLE_NAME, ID_TO_DELETE) ");
        sb.append("SELECT ");
        sb.append("'" + SrmConstants.TABLE_INTERNATIONAL_STRINGS + "', ");
        sb.append("c." + columnName + " ");
        sb.append("FROM TB_M_CODES c ");
        sb.append("INNER JOIN TB_CODES cb on c.TB_CODES = cb.ID ");
        sb.append("WHERE ");
        sb.append("cb.ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId + " ");
        sb.append("AND ");
        sb.append("c." + columnName + " ");
        sb.append("IS NOT NULL");

        executeSqlStatement(connection, sb);
    }

    private void insertInternationalStringToDeleteFromCodeNameableArtefact(Connection connection, Long itemSchemeVersionId, String columnName) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_ENTITIES_TO_DELETE ");
        sb.append("(TABLE_NAME, ID_TO_DELETE) ");
        sb.append("SELECT ");
        sb.append("'TB_INTERNATIONAL_STRINGS', ");
        sb.append("a." + columnName + " ");
        sb.append("FROM TB_CODES cb ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("WHERE ");
        sb.append("cb.ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId + " ");
        sb.append("AND ");
        sb.append("a." + columnName + " ");
        sb.append("IS NOT NULL");

        executeSqlStatement(connection, sb);
    }

    private void insertInternationalStringToDeleteFromAnnotation(Connection connection, Long itemSchemeVersionId, String columnName) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_ENTITIES_TO_DELETE ");
        sb.append("(TABLE_NAME, ID_TO_DELETE) ");
        sb.append("SELECT ");
        sb.append("'TB_INTERNATIONAL_STRINGS', ");
        sb.append("a." + columnName + " ");
        sb.append("FROM TB_ANNOTATIONS a ");
        sb.append("WHERE ");
        sb.append("a.ANNOTABLE_ARTEFACT_FK in (SELECT cb.NAMEABLE_ARTEFACT_FK FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId + ") ");
        sb.append("AND ");
        sb.append("a." + columnName + " ");
        sb.append("IS NOT NULL");

        executeSqlStatement(connection, sb);
    }

    /**
     * Delete annotations
     */
    private void deleteCodeAnnotationsEfficiently(Connection connection, Long itemSchemeVersionId) throws SQLException {
        executeSqlStatement(connection, "DELETE FROM TB_ANNOTATIONS WHERE ANNOTABLE_ARTEFACT_FK in (SELECT cb.NAMEABLE_ARTEFACT_FK FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = "
                + itemSchemeVersionId + ")");
    }

    /**
     * Delete entities of Code
     */
    private void deleteCodesEfficiently(Connection connection, Long itemSchemeVersionId) throws SQLException {
        // Delete metamac codes
        executeSqlStatement(connection, "DELETE FROM TB_M_CODES WHERE TB_CODES in (SELECT cb.ID FROM TB_CODES cb WHERE cb.ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId + ")");
        // Clear parent relation
        executeSqlStatement(connection, "UPDATE TB_CODES set PARENT_FK = null WHERE ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId);
        // Delete codes
        executeSqlStatement(connection, "DELETE FROM TB_CODES WHERE ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId);
    }

    private void deleteAnnotableArtefactsEfficiently(Connection connection, List<Long> annotableArtefactsToDelete) throws SQLException {
        if (CollectionUtils.isEmpty(annotableArtefactsToDelete)) {
            return;
        }

        StringBuilder annotableArtefactsToDeleteParameter = new StringBuilder();
        int count = 0;
        for (int i = 0; i < annotableArtefactsToDelete.size(); i++) {
            count++;
            Long id = annotableArtefactsToDelete.get(i);
            annotableArtefactsToDeleteParameter.append(id);
            if (count == 1000 || i == annotableArtefactsToDelete.size() - 1) {
                count = 0;
                String sb = "DELETE FROM TB_ANNOTABLE_ARTEFACTS WHERE ID IN (" + annotableArtefactsToDeleteParameter + ")";
                executeSqlStatement(connection, sb);
                annotableArtefactsToDeleteParameter = new StringBuilder();
            } else {
                annotableArtefactsToDeleteParameter.append(",");
            }
        }
    }

    private List<Long> getAnnotableArtefactsToDelete(Connection connection, Long itemSchemeVersionId) throws SQLException {
        List<Long> annotableArtefactsToDelete = new ArrayList<Long>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cb.NAMEABLE_ARTEFACT_FK ");
        sb.append("FROM TB_CODES cb ");
        sb.append("WHERE ");
        sb.append("cb.ITEM_SCHEME_VERSION_FK = " + itemSchemeVersionId);

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            logger.debug(sb);
            rs = statement.executeQuery(sb.toString());
            while (rs.next()) {
                Long annotableArtefactId = rs.getLong("NAMEABLE_ARTEFACT_FK");
                annotableArtefactsToDelete.add(annotableArtefactId);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return annotableArtefactsToDelete;
    }

    private void executeSqlStatement(Connection connection, String sb) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            logger.info(sb); // TODO remove this log in future (METAMAC-2076)
            statement.execute(sb);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void executeSqlStatement(Connection connection, StringBuilder sb) throws SQLException {
        executeSqlStatement(connection, sb.toString());
    }
}
