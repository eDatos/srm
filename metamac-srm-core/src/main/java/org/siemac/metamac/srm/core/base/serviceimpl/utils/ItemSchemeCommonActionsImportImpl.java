package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ItemSchemeCommonActionsImport;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;

@org.springframework.stereotype.Component("ItemSchemeCommonActionsImport")
public class ItemSchemeCommonActionsImportImpl implements ItemSchemeCommonActionsImport {

    @Override
    public boolean performImportationFinalAndPartialActions(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew) throws MetamacException {

        return true;
        /*
         * if (BooleanUtils.isTrue(codelistVersionNew.getMaintainableArtefact().getFinalLogic())) {
         * if (codelistVersionNew.getIsPartial()) {
         * return performImportationFinalAndPartialActionsNewAsFinalAndPartial(ctx, codelistVersionOld, codelistVersionNew);
         * } else if (!codelistVersionNew.getIsPartial()) {
         * return performImportationFinalAndPartialActionsNewAsFinalAndNotPartial(ctx, codelistVersionOld, codelistVersionNew);
         * }
         * } else {
         * // Check: All codelists imported in METAMAC must be FINAL
         * throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_FINAL_LOGIC)
         * .build();
         * }
         * return true;
         */
    }

    /**
     * New ItemScheme (Final = true, Partial = true)
     * 
     * @param ctx
     * @param codelistVersionOld
     * @param codelistVersionNew
     * @return
     */
    private boolean performImportationFinalAndPartialActionsNewAsFinalAndPartial(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew) throws MetamacException {
        if (BooleanUtils.isTrue(codelistVersionOld.getMaintainableArtefact().getFinalLogic())) {
            if (BooleanUtils.isTrue(codelistVersionOld.getIsPartial())) {
                // Merge: Habría que añadir los nuevos Items que aparecen respecto al estado anterior.
                mergeItems(codelistVersionOld, codelistVersionNew);
            } else {
                // Error (o simplemente no hacer nada)
                return true;
            }
        } else {
            throw new UnsupportedOperationException("In metamac there shouldn't be an earlier version of the device with the state imported final. [Data integrity of Metamac was violated]");
        }
        return true;
    }

    /**
     * New ItemScheme (Final = true, Partial = false)
     * 
     * @param ctx
     * @param codelistVersionOld
     * @param codelistVersionNew
     * @return
     */
    private boolean performImportationFinalAndPartialActionsNewAsFinalAndNotPartial(ServiceContext ctx, CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew) throws MetamacException {
        if (BooleanUtils.isTrue(codelistVersionOld.getMaintainableArtefact().getFinalLogic())) {
            if (BooleanUtils.isTrue(codelistVersionOld.getIsPartial())) {
                // Replace all items with the supplied elements.
                codelistVersionOld.removeAllItems();
                // TODO Ahora habría que añadir los hijos
                return false;
            } else {
                // TODO Se da un mensaje de error/aviso donde se notifica al usuario que no se importa porque ya existe esa versión con el mismo final y partial.
                return true;
            }
        } else {
            throw new UnsupportedOperationException("In metamac there shouldn't be an earlier version of the device with the state imported final. [Data integrity of Metamac was violated]");
        }
    }

    private void mergeItems(CodelistVersion codelistVersionOld, CodelistVersion codelistVersionNew) {
        for (Item item : codelistVersionNew.getItems()) { // Because this items aren't persisted, this is optimize.
            CodeMetamac codeMetamac = (CodeMetamac) item;

        }
        // TODO Auto-generated method stub
    }

}
