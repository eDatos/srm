package org.siemac.metamac.srm.core.base.repositoryimpl;

import org.siemac.metamac.core.common.exception.MetamacException;

public interface EntityToDeleteRepository {

    public static final String BEAN_ID = "entityToDelete";

    public void deleteEntitiesMarkedToDelete() throws MetamacException;
}
