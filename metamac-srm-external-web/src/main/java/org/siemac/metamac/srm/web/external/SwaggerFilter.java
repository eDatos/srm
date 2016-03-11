package org.siemac.metamac.srm.web.external;

import org.siemac.metamac.core.common.util.swagger.AbstractSwaggerFilter;

public class SwaggerFilter extends AbstractSwaggerFilter {

    @Override
    protected String[] getSupportedApiVersions() {
        return new String[]{"/v1.0/"};
    }
}
