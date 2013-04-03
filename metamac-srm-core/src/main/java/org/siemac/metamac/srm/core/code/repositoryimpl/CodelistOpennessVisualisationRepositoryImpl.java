package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodelistOpennessVisualisation
 */
@Repository("codelistOpennessVisualisationRepository")
public class CodelistOpennessVisualisationRepositoryImpl extends CodelistOpennessVisualisationRepositoryBase {

    public CodelistOpennessVisualisationRepositoryImpl() {
    }

    @Override
    public CodelistOpennessVisualisation findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistOpennessVisualisation> result = findByQuery("from CodelistOpennessVisualisation where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void updateUrnAllCodelistOpenVisualisationsByCodelistEfficiently(CodelistVersionMetamac codelistVersionMetamac, String oldUrnExample) {

        // Extract versionable Substring
        String replaceText = GeneratorUrnUtils.extractVersionableArtefactFragment(codelistVersionMetamac.getMaintainableArtefact().getUrn());
        String oldText = GeneratorUrnUtils.extractVersionableArtefactFragment(oldUrnExample);

        StringBuilder sb = new StringBuilder();
        sb.append("update TB_ANNOTABLE_ARTEFACTS ");
        sb.append("set URN=replace(urn, '").append(oldText).append("', '").append(replaceText).append("') ");
        sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
        sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_codelist_openness_vis ov where ov.codelist_fk = :codelistVersion))");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("codelistVersion", codelistVersionMetamac.getId());
        query.executeUpdate();

        if (!codelistVersionMetamac.getMaintainableArtefact().getIsImported()) {
            sb = new StringBuilder();
            sb.append("update TB_ANNOTABLE_ARTEFACTS ");
            sb.append("set URN_PROVIDER=URN ");
            sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
            sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_codelist_openness_vis ov where ov.codelist_fk = :codelistVersion))");

            query = getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("codelistVersion", codelistVersionMetamac.getId());
            query.executeUpdate();
        }
    }
}
