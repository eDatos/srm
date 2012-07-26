package org.siemac.metamac.srm.core.common.aop;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.siemac.metamac.core.common.aop.FlushingInterceptorBase;

public class FlushingInterceptor extends FlushingInterceptorBase {

    @Override
    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}