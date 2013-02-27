package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ItemSchemeCommonActionsImport;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.ActionToPerformEnum;

@org.springframework.stereotype.Component("ItemSchemeCommonActionsImport")
public class ItemSchemeCommonActionsImportImpl implements ItemSchemeCommonActionsImport {

    @Autowired
    private CodesMetamacService codesMetamacService;

    @Autowired
    private CodeRepository      codeRepository;

    @Override
    public ActionToPerformEnum performImportationFinalAndPartialActions(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew) throws MetamacException {
        CodelistVersionMetamac codelistVersionOldMetamac = (CodelistVersionMetamac) codelistVersionOld;
        CodelistVersionMetamac codelistVersionNewMetamac = (CodelistVersionMetamac) codelistVersionNew;

        if (BooleanUtils.isTrue(codelistVersionOldMetamac.getIsPartial())) {
            if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(codelistVersionOldMetamac.getLifeCycleMetadata().getProcStatus())
                    || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(codelistVersionOldMetamac.getLifeCycleMetadata().getProcStatus())) {
                // The resource was published then create a dummy code list with the codes that not appears in the published version.
                // TODO hacer esto
                // return ActionToPerformEnum.ADD_ITEMS_IN_NEW_ITEMSCHEME;
                throw new UnsupportedOperationException();
            } else {
                // Merge: Delete the codes already persisted. The new codes will be persisted.
                List<Item> items = new ArrayList<Item>(codelistVersionNewMetamac.getItems());
                for (Item item : items) { // Because this items aren't persisted, this is optimize.
                    CodeMetamac codeMetamacOld = (CodeMetamac) codeRepository.findIfExistAPreviousVersion(CodeMetamac.class, codelistVersionOldMetamac.getId(), item.getNameableArtefact().getCode());
                    if (codeMetamacOld != null) {
                        codelistVersionNewMetamac.removeItem(item);
                        codelistVersionNewMetamac.removeItemsFirstLevel(item);
                    }
                }
                return ActionToPerformEnum.ADD_ITEMS_IN_CURRENT_ITEMSCHEME;
            }
        }

        return ActionToPerformEnum.NOTHING;
    }
    /**
     * New ItemScheme (Final = true, Partial = true)
     * 
     * @param ctx
     * @param codelistVersionOld
     * @param codelistVersionNew
     * @return
     */
    private ActionToPerformEnum performImportationFinalAndPartialActionsNewAsFinalAndPartial(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew)
            throws MetamacException {
        if (BooleanUtils.isTrue(codelistVersionOld.getMaintainableArtefact().getFinalLogic())) {
            if (BooleanUtils.isTrue(codelistVersionOld.getIsPartial())) {
                performRemoveToMergeItems(ctx, codelistVersionOld, codelistVersionNew);
                return ActionToPerformEnum.NOTHING;
            } else {
                return ActionToPerformEnum.NOTHING;
            }
        } else {
            throw new UnsupportedOperationException("In metamac there shouldn't be an earlier version of the device with the state imported final. [Data integrity of Metamac was violated]");
        }
    }

    /**
     * New ItemScheme (Final = true, Partial = false)
     * 
     * @param ctx
     * @param codelistVersionOld
     * @param codelistVersionNew
     * @return
     */
    private ActionToPerformEnum performImportationFinalAndPartialActionsNewAsFinalAndNotPartial(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew)
            throws MetamacException {
        if (BooleanUtils.isTrue(codelistVersionOld.getMaintainableArtefact().getFinalLogic())) {
            if (BooleanUtils.isTrue(codelistVersionOld.getIsPartial())) {
                // Replace all items with the supplied elements.
                codelistVersionOld.removeAllItems();
                // TODO Ahora habría que añadir los hijos
                return ActionToPerformEnum.NOTHING;
            } else {
                return ActionToPerformEnum.NOTHING;
            }
        } else {
            throw new UnsupportedOperationException("In metamac there shouldn't be an earlier version of the device with the state imported final. [Data integrity of Metamac was violated]");
        }
    }

    /**
     * Merge Items, metadata schema items do not change.
     * 
     * @param ctx
     * @param codelistVersionOld
     * @param codelistVersionNew
     * @throws MetamacException
     */
    private void performRemoveToMergeItems(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew) throws MetamacException {
        for (Item item : codelistVersionNew.getItems()) { // Because this items aren't persisted, this is optimize.
            // Delete del old instance of code if exists
            CodeMetamac codeMetamacOld = (CodeMetamac) codeRepository.findIfExistAPreviousVersion(CodeMetamac.class, codelistVersionOld.getId(), item.getNameableArtefact().getCode());
            if (codeMetamacOld != null) {
                codesMetamacService.deleteCode(ctx, codeMetamacOld.getNameableArtefact().getUrn());
            }
        }
    }
}
