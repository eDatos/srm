package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getBoolean;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDate;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationResultExtensionPoint;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

/**
 * Repository implementation for OrganisationMetamac
 */
@Repository("organisationMetamacRepository")
public class OrganisationMetamacRepositoryImpl extends OrganisationMetamacRepositoryBase {

    @Autowired
    private ItemRepository         itemRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private SrmConfiguration       srmConfiguration;

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
    public List<ItemResult> findOrganisationsByOrganisationSchemeUnordered(Long itemSchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find items
        List<ItemResult> items = organisationRepository.findOrganisationsByOrganisationSchemeUnordered(itemSchemeVersionId, resultSelection);
        // no extension point
        return items;
    }

    @Override
    public List<ItemResult> findOrganisationsByOrganisationSchemeOrderedInDepth(Long itemSchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find organisations
        List<ItemResult> organisations = organisationRepository.findOrganisationsByOrganisationSchemeUnordered(itemSchemeVersionId, resultSelection);

        // Init extension point and index by id
        Map<Long, ItemResult> mapOrganisationByItemId = new HashMap<Long, ItemResult>(organisations.size());
        for (ItemResult itemResult : organisations) {
            itemResult.setExtensionPoint(new OrganisationResultExtensionPoint());
            mapOrganisationByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        StringBuilder sb = new StringBuilder();
        if (srmConfiguration.isDatabaseOracle()) {
            sb.append("SELECT c1.ID ");
            sb.append("FROM TB_ORGANISATIONS c1 ");
            sb.append("WHERE c1.ITEM_SCHEME_VERSION_FK = :organisationSchemeVersion ");
            sb.append("START WITH c1.PARENT_FK is null ");
            sb.append("CONNECT BY PRIOR c1.ID = c1.PARENT_FK ");
        } else if (srmConfiguration.isDatabaseSqlServer()) {
            sb.append("WITH parents(ID) AS ");
            sb.append("   (SELECT ID ");
            sb.append("    FROM TB_ORGANISATIONS ");
            sb.append("    WHERE PARENT_FK is null and ITEM_SCHEME_VERSION_FK = :organisationSchemeVersion ");
            sb.append("        UNION ALL ");
            sb.append("    SELECT c2.ID ");
            sb.append("    FROM TB_ORGANISATIONS as c2, parents ");
            sb.append("    WHERE parents.ID = c2.PARENT_FK) ");
            sb.append("SELECT ID FROM parents ");
        } else {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.UNKNOWN).withMessageParameters("Database unsupported").build();
        }
        Query queryOrder = getEntityManager().createNativeQuery(sb.toString());
        queryOrder.setParameter("organisationSchemeVersion", itemSchemeVersionId);
        @SuppressWarnings("rawtypes")
        List resultsOrder = queryOrder.getResultList();

        // Order previous result list thanks to ordered list of items id
        List<ItemResult> ordered = new ArrayList<ItemResult>(organisations.size());
        for (Object resultOrder : resultsOrder) {
            Long organisationId = getLong(resultOrder);
            ItemResult organisation = mapOrganisationByItemId.get(organisationId);
            ordered.add(organisation);
        }
        return ordered;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<OrganisationMetamacVisualisationResult> findOrganisationsByOrganisationSchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {
        // Find items. Returns only one row by item. NOTE: this query return null label if locale not exits for a code.
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ob.ID as ITEM_ID, ob.CREATED_DATE, ob.CREATED_DATE_TZ, a.URN, a.CODE, ob.PARENT_FK as ITEM_PARENT_ID, ls.LABEL, ob.ORGANISATION_TYPE, o.SPECIAL_ORG_HAS_BEEN_PUBLISHED ");
        sb.append("FROM TB_M_ORGANISATIONS o ");
        sb.append("INNER JOIN TB_ORGANISATIONS ob on o.TB_ORGANISATIONS = ob.ID ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ob.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale ");
        sb.append("WHERE ob.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        sb.append("ORDER BY a.CODE ASC ");
        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List itemsResultSql = query.getResultList();

        // Transform object[] results
        List<OrganisationMetamacVisualisationResult> targets = new ArrayList<OrganisationMetamacVisualisationResult>(itemsResultSql.size());
        Map<Long, ItemVisualisationResult> mapItemByItemId = new HashMap<Long, ItemVisualisationResult>(itemsResultSql.size());
        for (Object itemResultSql : itemsResultSql) {
            Object[] itemResultSqlArray = (Object[]) itemResultSql;
            OrganisationMetamacVisualisationResult target = itemResultSqlToOrganisationVisualisationResult(itemResultSqlArray);
            targets.add(target);
            mapItemByItemId.put(target.getItemIdDatabase(), target);
        }

        // Add description. Execute one independent query to retrieve it is more efficient than do a global query
        itemRepository.executeQueryFillItemDescription(itemSchemeVersionId, Organisation.class, locale, mapItemByItemId);

        // Parent: fill manually with java code
        for (OrganisationMetamacVisualisationResult target : targets) {
            if (target.getParentIdDatabase() != null) {
                ItemVisualisationResult parent = mapItemByItemId.get(target.getParentIdDatabase());
                target.setParent(parent);
            }
        }
        return targets;
    }

    @Override
    public void checkOrganisationTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        organisationRepository.checkOrganisationTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // no metadata specific in metamac
    }

    @Override
    public void updateHasBeenPublishedEfficiently(Long itemSchemeVersionId) {
        Query queryUpdate = getEntityManager()
                .createNativeQuery(
                        "UPDATE TB_M_ORGANISATIONS SET SPECIAL_ORG_HAS_BEEN_PUBLISHED = :hasBeenPublished WHERE TB_ORGANISATIONS IN (SELECT ID FROM TB_ORGANISATIONS WHERE ITEM_SCHEME_VERSION_FK = :itemSchemeVersion)");
        queryUpdate.setParameter("itemSchemeVersion", itemSchemeVersionId);
        queryUpdate.setParameter("hasBeenPublished", true);
        queryUpdate.executeUpdate();
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
        target.setSpecialOrganisationHasBeenPublished(getBoolean(source[i++]));
        return target;
    }
}