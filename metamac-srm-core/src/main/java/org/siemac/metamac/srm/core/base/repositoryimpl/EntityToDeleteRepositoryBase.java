package org.siemac.metamac.srm.core.base.repositoryimpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.orm.jpa.support.JpaDaoSupport;

@SuppressWarnings("deprecation")
public abstract class EntityToDeleteRepositoryBase extends JpaDaoSupport implements EntityToDeleteRepository {

    private EntityManager entityManager;

    public EntityToDeleteRepositoryBase() {
    }

    /**
     * Dependency injection
     */
    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected void setEntityManagerDependency(EntityManager entityManager) {
        this.entityManager = entityManager;
        // for JpaDaoSupport, JpaTemplate
        setEntityManager(entityManager);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
