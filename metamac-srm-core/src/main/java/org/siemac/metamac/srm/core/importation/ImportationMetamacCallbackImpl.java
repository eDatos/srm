package org.siemac.metamac.srm.core.importation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.importation.ImportationCallbackImplBase;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure;
import com.arte.statistic.sdmx.srm.core.structure.domain.ReportingYearStartDay;
import com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension;

@Component("importationMetamacCallback")
public class ImportationMetamacCallbackImpl extends ImportationCallbackImplBase {

    /**************************************************************************
     * VALIDATES
     *************************************************************************/

    @Override
    public void validateRestrictions(OrganisationSchemeVersion source) throws MetamacException {
        validateRestrictionsGeneral(source);
    }

    @Override
    public void validateRestrictions(CodelistVersion source) throws MetamacException {
        // TODO Auto-generated method stub

    }

    @Override
    public void validateRestrictions(ConceptSchemeVersion source) throws MetamacException {
        // TODO Auto-generated method stub

    }

    @Override
    public void validateRestrictions(DataStructureDefinitionVersion source) throws MetamacException {
        // TODO Auto-generated method stub

    }

    /**************************************************************************
     * FILLS
     *************************************************************************/

    @Override
    public void fillOrganisationSchemeVersion(OrganisationSchemeVersion source, OrganisationSchemeVersion target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillOrganisation(Organisation source, Organisation target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillCodelistVersion(CodelistVersion source, CodelistVersion target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillCode(Code source, Code target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillConceptSchemeVersion(ConceptSchemeVersion source, ConceptSchemeVersion target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillConcept(Concept source, Concept target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillDataStructureDefinitionVersion(DataStructureDefinitionVersion source, DataStructureDefinitionVersion target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillDimensionDescriptor(DimensionDescriptor source, DimensionDescriptor target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillMeasureDescriptor(MeasureDescriptor source, MeasureDescriptor target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillGroupDimensionDescriptor(GroupDimensionDescriptor source, GroupDimensionDescriptor target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillAttributeDescriptor(AttributeDescriptor source, AttributeDescriptor target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillDimension(Dimension source, Dimension target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillMeasureDimension(MeasureDimension source, MeasureDimension target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillTimeDimension(TimeDimension source, TimeDimension target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillPrimaryMeasure(PrimaryMeasure source, PrimaryMeasure target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillDataAttribute(DataAttribute source, DataAttribute target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillReportingYearStartDay(ReportingYearStartDay source, ReportingYearStartDay target) {
        // TODO Auto-generated method stub

    }

}
