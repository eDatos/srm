package org.siemac.metamac.srm.core.common.tests;

import javax.xml.datatype.DatatypeConfigurationException;

import org.joda.time.Duration;

public class DurationTest {

    public static void main(String[] args) throws DatatypeConfigurationException {

        Duration duration = new Duration("P2y10m14dT20h13m45s");

        System.out.println("====toString====== " + duration.toString());

        // XMLGregorianCalendar departureDate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2006, 1, 31, 0);
        //
        // System.out.println("====toString====== " + departureDate.toString());
        // System.out.println("====toXMLFormat====== " + departureDate.toXMLFormat());
        //
        // System.out.println("====Date.toString====== " + XMLGregorianCalendarUtil.transformToDate(departureDate).toString());
    }
        
}
