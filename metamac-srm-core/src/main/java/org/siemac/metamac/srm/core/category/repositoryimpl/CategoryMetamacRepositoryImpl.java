package org.siemac.metamac.srm.core.category.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategoryRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

/**
 * Repository implementation for CategoryMetamac
 */
@Repository("categoryMetamacRepository")
public class CategoryMetamacRepositoryImpl extends CategoryMetamacRepositoryBase {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository     itemRepository;

    @Autowired
    private SrmConfiguration   srmConfiguration;

    public CategoryMetamacRepositoryImpl() {
    }

    @Override
    public CategoryMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CategoryMetamac> result = findByQuery("from CategoryMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<ItemResult> findCategoriesByCategorySchemeUnordered(Long categorySchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find items
        List<ItemResult> items = categoryRepository.findCategoriesByCategorySchemeUnordered(categorySchemeVersionId, resultSelection);
        // no extension point
        return items;
    }

    @Override
    public List<ItemResult> findCategoriesByCategorySchemeOrderedInDepth(Long itemSchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find categories
        List<ItemResult> categories = categoryRepository.findCategoriesByCategorySchemeUnordered(itemSchemeVersionId, resultSelection);

        // Init index by id
        Map<Long, ItemResult> mapCategoriesByItemId = new HashMap<Long, ItemResult>(categories.size());
        for (ItemResult itemResult : categories) {
            mapCategoriesByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        StringBuilder sb = new StringBuilder();
        if (srmConfiguration.isDatabaseOracle()) {
            sb.append("SELECT c1.ID ");
            sb.append("FROM TB_CATEGORIES c1 ");
            sb.append("WHERE c1.ITEM_SCHEME_VERSION_FK = :categorySchemeVersion ");
            sb.append("START WITH c1.PARENT_FK is null ");
            sb.append("CONNECT BY PRIOR c1.ID = c1.PARENT_FK ");
        } else if (srmConfiguration.isDatabaseSqlServer()) {
            sb.append("WITH parents(ID) AS ");
            sb.append("   (SELECT ID ");
            sb.append("    FROM TB_CATEGORIES ");
            sb.append("    WHERE PARENT_FK is null and ITEM_SCHEME_VERSION_FK = :categorySchemeVersion ");
            sb.append("        UNION ALL ");
            sb.append("    SELECT c2.ID ");
            sb.append("    FROM TB_CATEGORIES as c2, parents ");
            sb.append("    WHERE parents.ID = c2.PARENT_FK) ");
            sb.append("SELECT ID FROM parents ");
        } else {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.UNKNOWN).withMessageParameters("Database unsupported").build();
        }
        Query queryOrder = getEntityManager().createNativeQuery(sb.toString());
        queryOrder.setParameter("categorySchemeVersion", itemSchemeVersionId);
        @SuppressWarnings("rawtypes")
        List resultsOrder = queryOrder.getResultList();

        // Order previous result list thanks to ordered list of items id
        List<ItemResult> ordered = new ArrayList<ItemResult>(categories.size());
        for (Object resultOrder : resultsOrder) {
            Long categoryId = getLong(resultOrder);
            ItemResult category = mapCategoriesByItemId.get(categoryId);
            ordered.add(category);
        }
        return ordered;
    }

    @Override
    public List<ItemVisualisationResult> findCategoriesByCategorySchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {
        return itemRepository.findItemsByItemSchemeUnorderedToVisualisation(itemSchemeVersionId, Category.class, locale);
    }

    @Override
    public void checkCategoryTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        categoryRepository.checkCategoryTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // no metadata specific in metamac
    }

}
