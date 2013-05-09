package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDate;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

/**
 * Repository implementation for OrganisationMetamac
 */
@Repository("organisationMetamacRepository")
public class OrganisationMetamacRepositoryImpl extends OrganisationMetamacRepositoryBase {

    @Autowired
    private OrganisationRepository organisationRepository;

    public OrganisationMetamacRepositoryImpl() {
    }

    @Override
    public OrganisationMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<OrganisationMetamac> result = findByQuery("from OrganisationMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<ItemResult> findOrganisationsByOrganisationSchemeUnordered(Long itemSchemeVersionId, ItemMetamacResultSelection resultSelection) {
        // Find items
        List<ItemResult> items = organisationRepository.findOrganisationsByOrganisationSchemeUnordered(itemSchemeVersionId, resultSelection);
        // no extension point
        return items;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<OrganisationMetamacVisualisationResult> findOrganisationsByOrganisationSchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {
        // Find items. Returns only one row by item. NOTE: this query return null label if locale not exits for a code.
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT i.ID as ITEM_ID, i.CREATED_DATE, i.CREATED_DATE_TZ, a.URN, a.CODE, i.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, o.ORGANISATION_TYPE ");
        sb.append("FROM TB_ORGANISATIONS o ");
        sb.append("INNER JOIN TB_ITEMS_BASE i on o.TB_ITEMS_BASE = i.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on i.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale ");
        sb.append("WHERE i.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId");
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List itemsResultSql = query.getResultList();

        // Transform object[] results
        List<OrganisationMetamacVisualisationResult> targets = new ArrayList<OrganisationMetamacVisualisationResult>(itemsResultSql.size());
        Map<Long, OrganisationMetamacVisualisationResult> mapItemByItemId = new HashMap<Long, OrganisationMetamacVisualisationResult>(itemsResultSql.size());
        for (Object itemResultSql : itemsResultSql) {
            Object[] itemResultSqlArray = (Object[]) itemResultSql;
            OrganisationMetamacVisualisationResult target = itemResultSqlToOrganisationVisualisationResult(itemResultSqlArray);
            targets.add(target);
            mapItemByItemId.put(target.getItemIdDatabase(), target);
        }

        // Parent: fill manually with java code
        for (OrganisationMetamacVisualisationResult target : targets) {
            if (target.getParentIdDatabase() != null) {
                OrganisationMetamacVisualisationResult parent = mapItemByItemId.get(target.getParentIdDatabase());
                target.setParent(parent);
            }
        }
        return targets;
    }

    @Override
    public void checkOrganisationTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        organisationRepository.checkOrganisationTranslations(itemSchemeVersionId, locale, exceptionItems);
        // no metadata specific in metamac
    }

    private OrganisationMetamacVisualisationResult itemResultSqlToOrganisationVisualisationResult(Object[] source) throws MetamacException {
        OrganisationMetamacVisualisationResult target = new OrganisationMetamacVisualisationResult();
        int i = 0;
        target.setItemIdDatabase(getLong(source[i++]));
        target.setCreatedDate(getDate(source[i++], source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setParentIdDatabase(getLong(source[i++]));
        target.setName(getString(source[i++]));
        target.setType(OrganisationTypeEnum.valueOf(getString(source[i++])));
        return target;
    }
}