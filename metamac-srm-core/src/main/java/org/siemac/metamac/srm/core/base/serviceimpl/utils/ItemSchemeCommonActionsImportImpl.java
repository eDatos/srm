package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
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

        if (BooleanUtils.isTrue(codelistVersionNew.getMaintainableArtefact().getFinalLogic())) {
            if (BooleanUtils.isTrue(codelistVersionNew.getIsPartial())) {
                return performImportationFinalAndPartialActionsNewAsFinalAndPartial(ctx, codelistVersionOld, codelistVersionNew);
            } else {
                return performImportationFinalAndPartialActionsNewAsFinalAndNotPartial(ctx, codelistVersionOld, codelistVersionNew);
            }
        } else {
            // Check: All codelists imported in METAMAC must be FINAL
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_FINAL_LOGIC)
                    .build();
        }
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
                return ActionToPerformEnum.PERSIST_ITEMS;
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
                return ActionToPerformEnum.PERSIST_ITEMS;
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
