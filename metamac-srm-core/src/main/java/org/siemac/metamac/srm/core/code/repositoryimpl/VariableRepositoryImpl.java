package org.siemac.metamac.srm.core.code.repositoryimpl;

import org.siemac.metamac.srm.core.code.domain.Variable;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for Variable
 */
@Repository("variableRepository")
public class VariableRepositoryImpl extends VariableRepositoryBase {
    public VariableRepositoryImpl() {
    }

    public Variable findByIdentifier(String identifier) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findByIdentifier not implemented");

    }
}
