package org.siemac.metamac.srm.core.concept.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for Quantity
 */
@Repository("quantityRepository")
public class QuantityRepositoryImpl extends QuantityRepositoryBase {

    public QuantityRepositoryImpl() {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Long findOneQuantityRelatedWithConcept(Long conceptId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ID ");
        sb.append("FROM TB_M_QUANTITIES ");
        sb.append("WHERE NUMERATOR_FK = :conceptId OR DENOMINATOR_FK = :conceptId OR BASE_QUANTITY_FK = :conceptId ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("conceptId", conceptId);
        List resultsSql = query.getResultList();
        if (resultsSql.size() != 0) {
            return getLong(resultsSql.get(0));
        } else {
            return null;
        }

    }
}
