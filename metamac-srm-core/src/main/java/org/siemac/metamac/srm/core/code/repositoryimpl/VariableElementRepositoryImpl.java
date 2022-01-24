package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDouble;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getStringFromClob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Repository implementation for VariableElement
 */
@Repository("variableElementRepository")
public class VariableElementRepositoryImpl extends VariableElementRepositoryBase {

    @Autowired
    private SrmConfiguration srmConfiguration;

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
    public List<VariableElementResult> findVariableElementsByVariableEfficiently(Long variableId, List<String> variableElementCodes, VariableElementResultSelection selection,
            List<String> geographicalGranularityUrns) throws MetamacException {
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
            sb.append("SELECT ve.ID as VARIABLE_ELEMENT_ID, a.URN, a.CODE, ls.LOCALE as SHORT_NAME_LOCALE, ls.LABEL as SHORT_NAME_LABEL, ve.RENDERING_COLOR ");
            sb.append("FROM TB_M_VARIABLE_ELEMENTS ve ");
            sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ve.IDENTIFIABLE_ARTEFACT_FK = a.ID ");
            if (selection.isReturnOnlyGeographicalVariableElements()) {
                sb.append("INNER JOIN TB_M_VARIABLES v on ve.VARIABLE_FK = v.ID ");
            }
            if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                sb.append("INNER JOIN TB_CODES c on ve.GEOGRAPHICAL_GRANULARITY_FK = c.ID ");
                sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ac on c.NAMEABLE_ARTEFACT_FK = ac.ID ");
            }
            sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls ON ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK ");
            sb.append("WHERE ve.VARIABLE_FK = :variableId ");
            if (!CollectionUtils.isEmpty(variableElementCodes)) {
                sb.append("AND a.CODE IN (:variableElementCodes) ");
            }
            if (selection.isReturnOnlyGeographicalVariableElements()) {
                sb.append("AND v.VARIABLE_TYPE = :variableType ");
            }
            if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                sb.append("AND ac.URN IN (:geographicalGranularityUrns) ");
            }

            Query query = getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("variableId", variableId);
            if (!CollectionUtils.isEmpty(variableElementCodes)) {
                query.setParameter("variableElementCodes", variableElementCodes.subList(startIndex, endIndex));
            }
            if (selection.isReturnOnlyGeographicalVariableElements()) {
                query.setParameter("variableType", VariableTypeEnum.GEOGRAPHICAL.getName());
            }
            if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                query.setParameter("geographicalGranularityUrns", geographicalGranularityUrns);
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
                        sbGeo.append(", ve.SHAPE_WKT as SHAPE_WKT");
                    }
                    if (selection.isShapeGeojson()) {
                        sbGeo.append(", ve.SHAPE_GEOJSON as SHAPE_GEOJSON");
                    }
                    if (selection.isLongitudeLatitude()) {
                        sbGeo.append(", ve.LATITUDE as LATITUDE, ve.LONGITUDE as LONGITUDE");
                    }
                    sbGeo.append(" ");
                    sbGeo.append("FROM TB_M_VARIABLE_ELEMENTS ve ");
                    sbGeo.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ve.IDENTIFIABLE_ARTEFACT_FK = a.ID ");

                    if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                        sbGeo.append("INNER JOIN TB_CODES c on ve.GEOGRAPHICAL_GRANULARITY_FK = c.ID ");
                        sbGeo.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ac on c.NAMEABLE_ARTEFACT_FK = ac.ID ");
                    }

                    sbGeo.append("WHERE ve.VARIABLE_FK = " + variableId);
                    if (!CollectionUtils.isEmpty(variableElementCodes)) {
                        // METAMAC-1653: Put parameter in this way instead of query.setParameter("variableElementCodes", variableElementCodes.subList(startIndex, endIndex)) to avoid
                        // 'org.hibernate.HibernateException: Could not determine a type for class: java.util.RandomAccessSubList' exception
                        StringBuilder variableElementsCodeParameter = new StringBuilder();
                        for (int i = startIndex; i < endIndex; i++) {
                            variableElementsCodeParameter.append("'" + variableElementCodes.get(i) + "'");
                            if (i < endIndex - 1) {
                                variableElementsCodeParameter.append(",");
                            }
                        }
                        sbGeo.append("AND a.CODE IN (" + variableElementsCodeParameter.toString() + ") ");
                    }

                    if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                        StringBuilder geographicalGranularityUrnsParameter = new StringBuilder();
                        for (int i = 0; i < geographicalGranularityUrns.size(); i++) {
                            geographicalGranularityUrnsParameter.append("'" + geographicalGranularityUrns.get(i) + "'");
                            if (i < geographicalGranularityUrns.size() - 1) {
                                geographicalGranularityUrnsParameter.append(",");
                            }
                        }
                        sbGeo.append("AND ac.URN IN (" + geographicalGranularityUrnsParameter.toString() + ") ");
                    }

                    // METAMAC-1653: addScalar is needed due to mssql can be determine that SHAPE_GEOJSON and SHAPE_WKT columns are 'Text' types
                    SQLQuery queryGeo = ((Session) getEntityManager().getDelegate()).createSQLQuery(sbGeo.toString());
                    queryGeo = queryGeo.addScalar("VARIABLE_ELEMENT_ID", org.hibernate.Hibernate.LONG);
                    if (selection.isShapeWkt()) {
                        queryGeo = queryGeo.addScalar("SHAPE_WKT", isDatabasePostgreSQL() ? org.hibernate.Hibernate.TEXT : org.hibernate.Hibernate.CLOB);
                    }
                    if (selection.isShapeGeojson()) {
                        queryGeo = queryGeo.addScalar("SHAPE_GEOJSON", isDatabasePostgreSQL() ? org.hibernate.Hibernate.TEXT : org.hibernate.Hibernate.CLOB);
                    }
                    if (selection.isLongitudeLatitude()) {
                        queryGeo = queryGeo.addScalar("LATITUDE", org.hibernate.Hibernate.FLOAT).addScalar("LONGITUDE", org.hibernate.Hibernate.FLOAT);
                    }

                    List variableElementsResultSqlGeo = queryGeo.list();

                    // Transform object[] results
                    for (Object variableElementResultSql : variableElementsResultSqlGeo) {
                        Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
                        Long id = getLong(variableElementResultSqlArray[0]);
                        VariableElementResult variableElementResult = variableElementsById.get(id);
                        variableElementGeoResultSqlToVariableElementResult(variableElementResultSqlArray, variableElementResult, selection);
                    }
                }
                // geographical granularity
                if (selection.isGeographicalGranularity()) {
                    StringBuilder sbGeo = new StringBuilder();
                    sbGeo.append("SELECT ve.ID as VARIABLE_ELEMENT_ID, ac.CODE, ac.URN, ls.LOCALE as CODE_NAME_LOCALE, ls.LABEL as CODE_NAME_LABEL  ");
                    sbGeo.append("FROM TB_M_VARIABLE_ELEMENTS ve ");
                    sbGeo.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ve.IDENTIFIABLE_ARTEFACT_FK = a.ID ");
                    sbGeo.append("INNER JOIN TB_CODES c on ve.GEOGRAPHICAL_GRANULARITY_FK = c.ID ");
                    sbGeo.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ac on c.NAMEABLE_ARTEFACT_FK = ac.ID ");
                    sbGeo.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls ON ls.INTERNATIONAL_STRING_FK = ac.NAME_FK ");
                    sbGeo.append("WHERE ve.VARIABLE_FK = :variableId ");
                    if (!CollectionUtils.isEmpty(variableElementCodes)) {
                        sbGeo.append("AND a.CODE IN (:variableElementCodes) ");
                    }
                    if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                        sbGeo.append("AND ac.URN IN (:geographicalGranularityUrns) ");
                    }
                    Query queryGeo = getEntityManager().createNativeQuery(sbGeo.toString());
                    queryGeo.setParameter("variableId", variableId);
                    if (!CollectionUtils.isEmpty(variableElementCodes)) {
                        queryGeo.setParameter("variableElementCodes", variableElementCodes.subList(startIndex, endIndex));
                    }
                    if (!CollectionUtils.isEmpty(geographicalGranularityUrns)) {
                        queryGeo.setParameter("geographicalGranularityUrns", geographicalGranularityUrns);
                    }
                    List variableElementsResultSqlGeo = queryGeo.getResultList();

                    // Transform object[] results
                    for (Object variableElementResultSql : variableElementsResultSqlGeo) {
                        Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
                        Long id = getLong(variableElementResultSqlArray[0]);
                        VariableElementResult variableElementResult = variableElementsById.get(id);
                        variableElementGeographicalGranularityResultSqlToVariableElementResult(variableElementResultSqlArray, variableElementResult, selection);
                    }
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
            i++; // label
        } else {
            String label = getString(source[i++]);
            target.getShortName().put(locale, label);
        }
        target.setRenderingColor(getString(source[i++]));
    }

    private void variableElementGeoResultSqlToVariableElementResult(Object[] source, VariableElementResult target, VariableElementResultSelection selection) throws MetamacException {
        int i = 1; // skip id
        if (selection.isShapeWkt()) {
            String shapeWkt = isDatabasePostgreSQL() ? getString(source[i++]) : getStringFromClob(source[i++]);
            target.setShapeWkt(shapeWkt);
        }
        if (selection.isShapeGeojson()) {
            String shapeGeoJson = isDatabasePostgreSQL() ? getString(source[i++]) : getStringFromClob(source[i++]);
            target.setShapeGeojson(shapeGeoJson);
        }
        if (selection.isLongitudeLatitude()) {
            target.setLatitude(getDouble(source[i++]));
            target.setLongitude(getDouble(source[i++]));
        }
    }

    private void variableElementGeographicalGranularityResultSqlToVariableElementResult(Object[] source, VariableElementResult target, VariableElementResultSelection selection) {
        int i = 1; // skip id
        ItemResult geographicalGranularity = target.getGeographicalGranularity();
        if (geographicalGranularity == null) {
            geographicalGranularity = new ItemResult();
            target.setGeographicalGranularity(geographicalGranularity);
            geographicalGranularity.setCode(getString(source[i++]));
            geographicalGranularity.setUrn(getString(source[i++]));
        } else {
            i++; // code
            i++; // urn
        }
        String locale = getString(source[i++]);
        if (locale == null) {
            i++; // label
        } else {
            String label = getString(source[i++]);
            geographicalGranularity.getName().put(locale, label);
        }
    }

    private boolean isDatabasePostgreSQL() throws MetamacException {
        return srmConfiguration.isDatabasePostgreSQL();
    }

    private String getString(Object source) {
        return source == null ? null : source.toString();
    }
}
