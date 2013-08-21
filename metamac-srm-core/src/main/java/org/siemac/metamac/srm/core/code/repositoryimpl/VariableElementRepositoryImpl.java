package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDouble;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getStringFromClob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for VariableElement
 */
@Repository("variableElementRepository")
public class VariableElementRepositoryImpl extends VariableElementRepositoryBase {

    public VariableElementRepositoryImpl() {
    }

    @Override
    public VariableElement findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<VariableElement> result = findByQuery("from VariableElement where identifiableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<VariableElementVisualisationResult> retrieveVariableElementsByVariableEfficientlyToVisualisation(Long variableId, String locale) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ve.ID as VARIABLE_ELEMENT_ID, a.URN, a.CODE, ls.LABEL as SHORT_NAME_LABEL ");
        sb.append("FROM TB_M_VARIABLE_ELEMENTS ve ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ve.IDENTIFIABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK and ls.locale = :locale ");
        sb.append("WHERE ve.VARIABLE_FK = :variableId ");
        sb.append("ORDER BY a.URN ASC");
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("variableId", variableId);
        query.setParameter("locale", locale);
        List variableElementsResultSql = query.getResultList();

        // Transform object[] results
        List<VariableElementVisualisationResult> targets = new ArrayList<VariableElementVisualisationResult>(variableElementsResultSql.size());
        for (Object variableElementResultSql : variableElementsResultSql) {
            Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
            VariableElementVisualisationResult target = variableElementResultSqlToVariableElementVisualisationResult(variableElementResultSqlArray);
            targets.add(target);
        }
        return targets;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<VariableElementResult> findVariableElementsByVariableEfficiently(Long variableId, List<String> variableElementCodes, VariableElementResultSelection selection) {
        if (CollectionUtils.isEmpty(variableElementCodes)) {
            variableElementCodes = new ArrayList<String>();
        }
        int maximumInClause = CoreCommonConstants.SQL_IN_CLAUSE_MAXIMUM_NUMBER;
        int startIndex = 0;
        int endIndex = 0;

        List<VariableElementResult> targets = new ArrayList<VariableElementResult>();
        Map<Long, VariableElementResult> variableElementsById = new HashMap<Long, VariableElementResult>();
        do {
            if (!CollectionUtils.isEmpty(variableElementCodes)) {
                startIndex = endIndex;
                if (startIndex >= variableElementCodes.size()) {
                    break;
                }
                endIndex += maximumInClause;
                if (endIndex > variableElementCodes.size()) {
                    endIndex = variableElementCodes.size();
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ve.ID as VARIABLE_ELEMENT_ID, a.URN, a.CODE, ls.LOCALE as SHORT_NAME_LOCALE, ls.LABEL as SHORT_NAME_LABEL ");
            sb.append("FROM TB_M_VARIABLE_ELEMENTS ve ");
            sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ve.IDENTIFIABLE_ARTEFACT_FK = a.ID ");
            if (selection.isReturnOnlyGeographicalVariableElements()) {
                sb.append("INNER JOIN TB_M_VARIABLES v on ve.VARIABLE_FK = v.ID ");
            }
            sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls ON ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK ");
            sb.append("WHERE ve.VARIABLE_FK = :variableId ");
            if (!CollectionUtils.isEmpty(variableElementCodes)) {
                sb.append("AND a.CODE IN (:variableElementCodes) ");
            }
            if (selection.isReturnOnlyGeographicalVariableElements()) {
                sb.append("AND v.VARIABLE_TYPE = :variableType ");
            }
            Query query = getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("variableId", variableId);
            if (!CollectionUtils.isEmpty(variableElementCodes)) {
                query.setParameter("variableElementCodes", variableElementCodes.subList(startIndex, endIndex));
            }
            if (selection.isReturnOnlyGeographicalVariableElements()) {
                query.setParameter("variableType", VariableTypeEnum.GEOGRAPHICAL.getName());
            }
            List variableElementsResultSql = query.getResultList();

            // Transform object[] results
            for (Object variableElementResultSql : variableElementsResultSql) {
                Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
                Long id = getLong(variableElementResultSqlArray[0]);
                VariableElementResult variableElementResult = variableElementsById.get(id);
                if (variableElementResult == null) {
                    variableElementResult = new VariableElementResult();
                    targets.add(variableElementResult);
                    variableElementsById.put(id, variableElementResult);
                }
                variableElementResultSqlToVariableElementResult(variableElementResultSqlArray, variableElementResult);
            }

            if (!CollectionUtils.isEmpty(variableElementsResultSql)) {
                // latitude, longitude, shape...
                if (selection.isLongitudeLatitude() || selection.isShapeGeojson() || selection.isShapeWkt()) {
                    StringBuilder sbGeo = new StringBuilder();
                    sbGeo.append("SELECT ve.ID as VARIABLE_ELEMENT_ID");
                    if (selection.isShapeWkt()) {
                        sbGeo.append(", ve.SHAPE_WKT");
                    }
                    if (selection.isShapeGeojson()) {
                        sbGeo.append(", ve.SHAPE_GEOJSON");
                    }
                    if (selection.isLongitudeLatitude()) {
                        sbGeo.append(", ve.LATITUDE, ve.LONGITUDE");
                    }
                    sbGeo.append(" ");
                    sbGeo.append("FROM TB_M_VARIABLE_ELEMENTS ve ");
                    sbGeo.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ve.IDENTIFIABLE_ARTEFACT_FK = a.ID ");
                    sbGeo.append("WHERE ve.VARIABLE_FK = :variableId ");
                    if (!CollectionUtils.isEmpty(variableElementCodes)) {
                        sbGeo.append("AND a.CODE IN (:variableElementCodes) ");
                    }
                    Query queryGeo = getEntityManager().createNativeQuery(sbGeo.toString());
                    queryGeo.setParameter("variableId", variableId);
                    if (!CollectionUtils.isEmpty(variableElementCodes)) {
                        queryGeo.setParameter("variableElementCodes", variableElementCodes.subList(startIndex, endIndex));
                    }
                    List variableElementsResultSqlGeo = queryGeo.getResultList();

                    // Transform object[] results
                    for (Object variableElementResultSql : variableElementsResultSqlGeo) {
                        Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
                        Long id = getLong(variableElementResultSqlArray[0]);
                        VariableElementResult variableElementResult = variableElementsById.get(id);
                        variableElementGeoResultSqlToVariableElementResult(variableElementResultSqlArray, variableElementResult, selection);
                    }
                }
                // geographic granularity TODO geographicalGranularity
                if (selection.isGeographicalGranularity()) {

                }

            }
            if (CollectionUtils.isEmpty(variableElementCodes)) {
                break;
            }
        } while (true);
        return targets;
    }

    @Override
    public Long countVariableElementsByVariable(Long variableId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) ");
        sb.append("FROM TB_M_VARIABLE_ELEMENTS ");
        sb.append("WHERE VARIABLE_FK = :variableId");
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("variableId", variableId);
        return getLong(query.getResultList().get(0));
    }

    @Override
    public void clearGeographicalInformationByVariable(Long variableId) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_VARIABLE_ELEMENTS set LATITUDE = null, LONGITUDE = null, SHAPE_WKT = null, SHAPE_GEOJSON = null, GEOGRAPHICAL_GRANULARITY_FK = null ");
        sb.append("WHERE VARIABLE_FK = :variableId");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("variableId", variableId);
        queryUpdate.executeUpdate();
    }

    private VariableElementVisualisationResult variableElementResultSqlToVariableElementVisualisationResult(Object[] source) {
        VariableElementVisualisationResult target = new VariableElementVisualisationResult();
        int i = 0;
        target.setIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setShortName(getString(source[i++]));
        return target;
    }

    private void variableElementResultSqlToVariableElementResult(Object[] source, VariableElementResult target) {
        int i = 0;
        target.setIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        String locale = getString(source[i++]);
        if (locale == null) {
            i++;
        } else {
            String label = getString(source[i++]);
            target.getShortName().put(locale, label);
        }
    }

    private void variableElementGeoResultSqlToVariableElementResult(Object[] source, VariableElementResult target, VariableElementResultSelection selection) {
        int i = 1; // skip id
        if (selection.isShapeWkt()) {
            target.setShapeWkt(getStringFromClob(source[i++]));
        }
        if (selection.isShapeGeojson()) {
            target.setShapeGeojson(getStringFromClob(source[i++]));
        }
        if (selection.isLongitudeLatitude()) {
            target.setLatitude(getDouble(source[i++]));
            target.setLongitude(getDouble(source[i++]));
        }
    }

}
