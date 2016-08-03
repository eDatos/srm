package org.siemac.metamac.srm.soap.external.v1_0.service;

import java.util.List;

import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.soap.exception.SoapExceptionUtils;
import org.siemac.metamac.soap.structural_resources.v1_0.ExceptionFault;
import org.siemac.metamac.soap.structural_resources.v1_0.MetamacStructuralResourcesInterfaceV10;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.soap.external.exception.SoapServiceExceptionType;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesDo2SoapMapperV10;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.code.CodesSoap2DoMapperV10;
import org.siemac.metamac.srm.soap.external.v1_0.service.utils.InvocationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@WebService
@Service("srmSoapExternalFacadeV10")
public class SrmSoapExternalFacadeV10Impl implements MetamacStructuralResourcesInterfaceV10 {

    @Autowired
    private CodesMetamacService   codesService;

    @Autowired
    private CodesSoap2DoMapperV10 codesSoap2DoMapper;

    @Autowired
    private CodesDo2SoapMapperV10 codesDo2SoapMapper;

    private final ServiceContext  ctx    = new ServiceContext("soapExternal", "soapExternal", "soapExternal");
    private final Logger          logger = LoggerFactory.getLogger(SrmSoapExternalFacadeV10Impl.class);

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

    @Override
    public CodelistFamilies findCodelistFamilies(MetamacCriteria criteria) throws ExceptionFault {
        try {
            // Validation of parameters
            InvocationValidator.validateFindCodelistFamilies(criteria);

            // Transform
            SculptorCriteria sculptorCriteria = codesSoap2DoMapper.getCodelistFamilyCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

            // Find
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> result = codesService.findCodelistFamiliesByCondition(ctx, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            CodelistFamilies codelistFamilies = codesDo2SoapMapper.toCodelistFamilies(result, sculptorCriteria.getPageSize());
            return codelistFamilies;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Codelists findCodelists(MetamacCriteria criteria) throws ExceptionFault {
        try {
            // Validation of parameters
            InvocationValidator.validateFindCodelists(criteria);

            // Transform
            SculptorCriteria sculptorCriteria = codesSoap2DoMapper.getCodelistCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

            // Find
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac> result = findCodelistsExternallyPublished(null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            Codelists codelists = codesDo2SoapMapper.toCodelists(result, sculptorCriteria.getPageSize());
            return codelists;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Codelist retrieveCodelist(String urn) throws ExceptionFault {
        try {
            // Validation of parameters
            InvocationValidator.validateRetrieveCodelist(urn);

            // Find
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac> result = findCodelistsExternallyPublished(urn, null, PagingParameter.pageAccess(1, 1, false));
            if (result.getValues().size() != 1) {
                throw SoapExceptionUtils.buildExceptionFault(SoapServiceExceptionType.CODELIST_NOT_FOUND, urn);
            }

            // Transform
            Codelist codelist = codesDo2SoapMapper.toCodelist(result.getValues().get(0));
            return codelist;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<CodelistVersionMetamac> findCodelistsExternallyPublished(String urn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws ExceptionFault {
        try {
            if (CollectionUtils.isEmpty(conditions)) {
                conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build();
            }
            // urn
            if (urn != null) {
                conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).lbrace().withProperty(CodelistVersionMetamacProperties.maintainableArtefact().urn()).eq(urn)
                        .or().withProperty(CodelistVersionMetamacProperties.maintainableArtefact().urnProvider()).eq(urn).rbrace().build());
            }
            // externally published
            conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().publicLogic()).eq(Boolean.TRUE)
                    .buildSingle());
            // access public
            conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());

            // Find
            PagedResult<CodelistVersionMetamac> entitiesPagedResult = codesService.findCodelistsByCondition(ctx, conditions, pagingParameter);
            return entitiesPagedResult;
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
            return SoapExceptionUtils.buildExceptionFault(SoapServiceExceptionType.UNKNOWN);
        }
        return fault;
    }
}
