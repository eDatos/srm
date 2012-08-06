package org.siemac.metamac.srm.web.server.handlers;

import javax.servlet.http.HttpServletRequestWrapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.ValidateTicketAbstractActionHandler;
import org.siemac.metamac.web.common.server.session.SingleSignOutFilter;
import org.siemac.metamac.web.common.shared.ValidateTicketAction;
import org.siemac.metamac.web.common.shared.ValidateTicketResult;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ValidateTicketActionHandler extends ValidateTicketAbstractActionHandler {

    @Override
    public ValidateTicketResult execute(ValidateTicketAction action, ExecutionContext context) throws ActionException {

        final String ticket = action.getTicket();
        final String service = action.getServiceUrl();

        try {
            MetamacPrincipal metamacPrincipal = validateTicket.validateTicket(ticket, service);
            ServiceContext serviceContext = new ServiceContext(metamacPrincipal.getUserId(), ticket, SrmConstants.SECURITY_APPLICATION_ID);
            serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
            ServiceContextHolder.putCurrentServiceContext(serviceContext);

            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(ServiceContextHolder.getCurrentRequest()) {

                public String getParameter(String name) {
                    if (TICKET_PARAMETER.equals(name)) {
                        return ticket;
                    }
                    return super.getParameter(name);
                }
                @Override
                public String getQueryString() {
                    return super.getQueryString() + TICKET_QUERY_STRING + ticket;
                }
            };
            SingleSignOutFilter.getSingleSignOutHandler().recordSession(requestWrapper);

            return new ValidateTicketResult(metamacPrincipal);
        } catch (final org.siemac.metamac.sso.exception.TicketValidationException e) {
            throw new ActionException(e);
        }
    }

}
