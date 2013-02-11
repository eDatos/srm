package org.siemac.metamac.srm.soap.external.v1_0.mapper.base;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.soap.common.v1_0.domain.InternationalString;

public interface BaseDo2SoapMapperV10 {

    public InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString sources);
    public Date toDate(DateTime source);
}
