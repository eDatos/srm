package org.siemac.metamac.srm.core.code.repositoryimpl;

import org.siemac.metamac.srm.core.code.domain.VariableFamily;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for VariableFamily
 */
@Repository("variableFamilyRepository")
public class VariableFamilyRepositoryImpl extends VariableFamilyRepositoryBase {
    public VariableFamilyRepositoryImpl() {
    }

    public VariableFamily findByIdentifier(String identifier) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findByIdentifier not implemented");

    }
}
