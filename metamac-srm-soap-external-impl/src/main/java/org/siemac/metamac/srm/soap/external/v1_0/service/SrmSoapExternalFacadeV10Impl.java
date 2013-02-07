package org.siemac.metamac.srm.soap.external.v1_0.service;

import javax.jws.WebService;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.aop.LoggingInterceptor;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.soap.exception.SoapCommonServiceExceptionType;
import org.siemac.metamac.soap.exception.SoapExceptionUtils;
import org.siemac.metamac.soap.structural_resources.v1_0.ExceptionFault;
import org.siemac.metamac.soap.structural_resources.v1_0.MetamacStructuralResourcesInterfaceV10;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesDo2SoapMapper;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesSoap2DoMapper;
import org.siemac.metamac.srm.soap.external.v1_0.service.utils.InvocationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@WebService
@Service("srmSoapExternalFacadeV10")
public class SrmSoapExternalFacadeV10Impl implements MetamacStructuralResourcesInterfaceV10 {

    @Autowired
    private CodesMetamacService  codesService;

    @Autowired
    private CodesSoap2DoMapper   codesSoap2DoMapper;

    @Autowired
    private CodesDo2SoapMapper   codesDo2SoapMapper;

    private final ServiceContext ctx    = new ServiceContext("soapExternal", "soapExternal", "soapExternal");
    private final Logger         logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public VariableFamilies findVariableFamilies(MetamacCriteria criteria) throws ExceptionFault {
        try {
            // Validation of parameters
            InvocationValidator.validateFindVariableFamilies(criteria);

            // Transform
            SculptorCriteria sculptorCriteria = codesSoap2DoMapper.getVariableFamilyCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

            // Find
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> result = codesService.findVariableFamiliesByCondition(ctx, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            VariableFamilies variableFamilies = codesDo2SoapMapper.toVariableFamilies(result, sculptorCriteria.getPageSize());
            return variableFamilies;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variables findVariables(MetamacCriteria criteria) throws ExceptionFault {
        try {
            // Validation of parameters
            InvocationValidator.validateFindVariables(criteria);

            // Transform
            SculptorCriteria sculptorCriteria = codesSoap2DoMapper.getVariableCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

            // Find
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> result = codesService.findVariablesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Variables variables = codesDo2SoapMapper.toVariables(result, sculptorCriteria.getPageSize());
            return variables;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    /**
     * Throws response error, logging exception
     */
    private ExceptionFault manageException(Exception e) {
        logger.error("Error", e);
        ExceptionFault fault = null;
        if (e instanceof ExceptionFault) {
            fault = (ExceptionFault) e;
        } else {
            // do not show information details about exception to user
            org.siemac.metamac.soap.common.v1_0.domain.Exception exception = SoapExceptionUtils.getException(SoapCommonServiceExceptionType.UNKNOWN);
            fault = new ExceptionFault(exception.getCode(), exception);
        }
        return fault;
    }
}
