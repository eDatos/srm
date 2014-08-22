package org.siemac.metamac.srm.rest.internal.v1_0.constraint.utils;

import static org.siemac.metamac.common.test.utils.MetamacMocks.mockString;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.constraint.serviceapi.utils.ConstraintsMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;

public class ContentConstraintsDoMocks {

    public static ContentConstraint mockContentConstraint(String agencyID, String resourceID, String version) {
        ContentConstraint mockContentConstraintWithKeysFixedValues = ConstraintsMetamacDoMocks.mockContentConstraintWithKeysFixedValues("prefix", agencyID, resourceID, version);
        mockContentConstraintWithKeysFixedValues.setConstraintAttachment(mockExternalItemDatasetVersionFixedValues());
        return mockContentConstraintWithKeysFixedValues;
    }

    public static ExternalItem mockExternalItemDatasetVersionFixedValues() {
        String resourceID = "C00031A_000002";

        ExternalItem target = new ExternalItem();
        target.setCode(resourceID);
        target.setCodeNested(target.getCode() + "nested");
        target.setUri("uri:" + resourceID);
        target.setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=ISTAC:" + resourceID + "(001.005)");
        target.setUrnProvider("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=ISTAC-PROVIDER:" + resourceID + "(001.005)");
        target.setType(TypeExternalArtefactsEnum.DATASET);
        target.setManagementAppUrl("managementAppUrl" + mockString(10));
        target.setTitle(ConstraintsMetamacDoMocks.mockInternationalStringFixedValues("title", "sub"));
        return target;
    }
}
