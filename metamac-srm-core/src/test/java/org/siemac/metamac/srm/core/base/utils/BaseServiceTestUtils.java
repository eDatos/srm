package org.siemac.metamac.srm.core.base.utils;

import static org.junit.Assert.fail;

import java.util.List;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;

public class BaseServiceTestUtils {

    public static void assertListItemsContainsItem(List<Item> items, String urn) {
        for (Item item : items) {
            if (item.getNameableArtefact().getUrn().equals(urn)) {
                return;
            }
        }
        fail("List does not contain item with urn " + urn);
    }

}
