package org.siemac.metamac.srm.core.category.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersionRepository;

/**
 * Repository implementation for CategorySchemeVersionMetamac
 */
@Repository("categorySchemeVersionMetamacRepository")
public class CategorySchemeVersionMetamacRepositoryImpl extends CategorySchemeVersionMetamacRepositoryBase {

    @Autowired
    private CategorySchemeVersionRepository categorySchemeVersionRepository;

    public CategorySchemeVersionMetamacRepositoryImpl() {
    }

    @Override
    public CategorySchemeVersionMetamac findByCategory(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CategorySchemeVersionMetamac> result = findByQuery("select c from CategorySchemeVersionMetamac c join c.items as i where i.nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public CategorySchemeVersionMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CategorySchemeVersionMetamac> result = findByQuery("from CategorySchemeVersionMetamac where maintainableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public CategorySchemeVersionMetamac retrieveCategorySchemeVersionByProcStatus(String urn, ProcStatusEnum[] procStatusArray) throws MetamacException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        parameters.put("procStatus", SrmServiceUtils.procStatusEnumToList(procStatusArray));
        List<CategorySchemeVersionMetamac> result = findByQuery("from CategorySchemeVersionMetamac where maintainableArtefact.urn = :urn and lifeCycleMetadata.procStatus in (:procStatus)",
                parameters, 1);
        if (result == null || result.isEmpty()) {
            // check scheme exists to throws specific exception
            CategorySchemeVersionMetamac categorySchemeVersionMetamac = findByUrn(urn);
            if (categorySchemeVersionMetamac == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
            } else {
                // if exists, throw exception about wrong proc status
                String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatusArray);
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();

            }
        }
        return result.get(0);
    }

    @Override
    public void checkCategorySchemeVersionTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        categorySchemeVersionRepository.checkCategorySchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // no metadata specific in metamac
    }

}
