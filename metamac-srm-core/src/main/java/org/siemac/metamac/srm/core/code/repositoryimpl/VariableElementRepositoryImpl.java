package org.siemac.metamac.srm.core.code.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementResult;
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
    public List<VariableElementResult> retrieveVariableElementsByVariableEfficiently(Long variableId, String locale) {
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
        List<VariableElementResult> targets = new ArrayList<VariableElementResult>(variableElementsResultSql.size());
        for (Object variableElementResultSql : variableElementsResultSql) {
            Object[] variableElementResultSqlArray = (Object[]) variableElementResultSql;
            VariableElementResult target = variableElementResultSqlToVariableElementResult(variableElementResultSqlArray);
            targets.add(target);
        }
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
        sb.append("UPDATE TB_M_VARIABLE_ELEMENTS set LATITUDE = null, LONGITUDE = null, SHAPE = null, SHAPE_GEOJSON = null, GEOGRAPHICAL_GRANULARITY_FK = null ");
        sb.append("WHERE VARIABLE_FK = :variableId");
        Query queryUpdate = getEntityManager().createNativeQuery(sb.toString());
        queryUpdate.setParameter("variableId", variableId);
        queryUpdate.executeUpdate();
    }

    private VariableElementResult variableElementResultSqlToVariableElementResult(Object[] source) {
        VariableElementResult target = new VariableElementResult();
        int i = 0;
        target.setIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setShortName(getString(source[i++]));
        return target;
    }

}
