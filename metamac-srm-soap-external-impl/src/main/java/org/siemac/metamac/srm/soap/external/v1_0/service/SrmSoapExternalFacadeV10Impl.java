package org.siemac.metamac.srm.soap.external.v1_0.service;

import javax.jws.WebService;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.aop.LoggingInterceptor;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.srm.v1_0.ExceptionFault;
import org.siemac.metamac.soap.srm.v1_0.MetamacSrmInterfaceV10;
import org.siemac.metamac.soap.srm.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.soap.srm.v1_0.domain.VariableFamilies;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.soap.external.exception.SoapCommonServiceExceptionType;
import org.siemac.metamac.srm.soap.external.exception.SoapExceptionUtils;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesDo2SoapMapper;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesSoap2DoMapper;
import org.siemac.metamac.srm.soap.external.v1_0.service.utils.InvocationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@WebService(endpointInterface = "org.siemac.metamac.soap.srm.v1_0.MetamacSrmInterfaceV10", targetNamespace = "http://www.siemac.org/metamac/soap/srm/v1.0", serviceName = "MetamacSrmInterface_v1.0", portName = "MetamacSrmBindingSOAP_v1.0")
@Service("srmSoapExternalFacadeV10")
public class SrmSoapExternalFacadeV10Impl implements MetamacSrmInterfaceV10 {

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
            PagedResult<VariableFamily> result = codesService.findVariableFamiliesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            VariableFamilies variableFamilies = codesDo2SoapMapper.toVariableFamilies(result, sculptorCriteria.getPageSize());
            return variableFamilies;
        } catch (MetamacException e) {
            throw manageException(e);
        }
    }

    /**
     * Throws response error, logging exception
     */
    private ExceptionFault manageException(Exception e) {
        logger.error("Error", e);
        if (e instanceof ExceptionFault) {
            return (ExceptionFault) e;
        } else {
            // do not show information details about exception to user
            org.siemac.metamac.soap.srm.v1_0.exception.Exception exception = SoapExceptionUtils.getException(SoapCommonServiceExceptionType.UNKNOWN);
            return new ExceptionFault("EXCEPTION", exception);
        }
    }
}
