package org.siemac.metamac.srm.core.service.utils;

import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasure;
import org.siemac.metamac.srm.core.structure.domain.TimeDimension;

public class SdmxToolsServer {

    static final String timeDimensionID         = "TIME_PERIOD";
    static final String primaryMeasureID        = "OBS_VALUE";
    static final String measureListID           = "MeasureDescriptor";
    static final String dimensionListID         = "DimensionDescriptor";
    static final String attributeListID         = "AttributeDescriptor";
    static final String reportingYearStartDayID = "REPORTING_YEAR_START_DAY";

    public static String checkIfFixedId(Object element) {

        if (element instanceof TimeDimension) {
            return timeDimensionID;
        } else if (element instanceof PrimaryMeasure) {
            return primaryMeasureID;
        } else if (element instanceof MeasureDescriptor) {
            return measureListID;
        } else if (element instanceof DimensionDescriptor) {
            return dimensionListID;
        } else if (element instanceof AttributeDescriptor) {
            return attributeListID;
        } else if (element instanceof AttributeDescriptor) {
            return reportingYearStartDayID;
        }

        return null;

    }

    /**
     * Used to check whether to delete orphan entities manually.
     * 
     * @param idOld
     * @param idNew
     * @return
     */
    public static boolean removeOld(Long idOld, Long idNew) {
        // Algorithm
        // if (idOld == null) {
        // ;
        // }
        // else {
        // if (idNew == null) {
        // return true;
        // }
        // else {
        // if (idNew == idOld) {
        // ;
        // }
        // else {
        // return true;
        // }
        // }
        // }
        if (idOld != null) {
            if (idNew == null) {
                return true;
            } else {
                if (idNew.compareTo(idOld) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
