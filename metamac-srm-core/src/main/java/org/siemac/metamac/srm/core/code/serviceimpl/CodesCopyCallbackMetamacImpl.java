package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.CodesCopyCallbackImpl;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Copy Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class CodesCopyCallbackMetamacImpl extends CodesCopyCallbackImpl {

    @Autowired
    private CodesMetamacService   codesMetamacService;

    @Autowired
    private CodeMetamacRepository codeMetamacRepository;

    @Override
    public ItemSchemeVersion createItemSchemeVersion() {
        return new CodelistVersionMetamac();
    }

    @Override
    public void copyItemSchemeVersion(ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) {
        super.copyItemSchemeVersion(sourceItemSchemeVersion, targetItemSchemeVersion);
        CodelistVersionMetamac source = (CodelistVersionMetamac) sourceItemSchemeVersion;
        CodelistVersionMetamac target = (CodelistVersionMetamac) targetItemSchemeVersion;
        target.setShortName(copyInternationalString(source.getShortName()));
        target.setDescriptionSource(copyInternationalString(source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        target.setVariable(source.getVariable());
        target.setFamily(source.getFamily());
        // note: replaceBy and replaceTo metadata do not must be copied, because they are related to concrete versions of codelist
    }

    @Override
    public void createItemSchemeVersionRelations(ServiceContext ctx, ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) throws MetamacException {
        super.createItemSchemeVersionRelations(ctx, sourceItemSchemeVersion, targetItemSchemeVersion);
        CodelistVersionMetamac source = (CodelistVersionMetamac) sourceItemSchemeVersion;
        CodelistVersionMetamac target = (CodelistVersionMetamac) targetItemSchemeVersion;
        // Visualisations
        codesMetamacService.versioningCodelistOrderVisualisations(ctx, source, target);
        codesMetamacService.versioningCodelistOpennessVisualisations(ctx, source, target);
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long codelistId) throws MetamacException {
        return codeMetamacRepository.findCodesByCodelistUnordered(codelistId, SrmServiceUtils.getItemResultSelection(getCopyOperationType()));
    }

    @Override
    public Item createItem() {
        return new CodeMetamac();
    }

    @Override
    public void copyItem(Item sourceItem, ItemResult sourceItemResult, Item targetItem) {
        super.copyItem(sourceItem, sourceItemResult, targetItem);

        CodeMetamac source = (CodeMetamac) sourceItem;
        CodeMetamac target = (CodeMetamac) targetItem;

        // Copy efficiently
        CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) sourceItemResult.getExtensionPoint();
        target.setShortName(copyInternationalString(extensionPoint.getShortName()));

        target.setVariableElement(source.getVariableElement());
        copyCodeOrderVisualisations(source, target);
        copyCodeOpennessVisualisations(source, target);
    }

    private void copyCodeOrderVisualisations(CodeMetamac source, CodeMetamac target) {
        for (int i = 1; i <= SrmConstants.CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
            Integer order = SrmServiceUtils.getCodeOrder(source, i);
            SrmServiceUtils.setCodeOrder(target, i, order);
        }
    }

    private void copyCodeOpennessVisualisations(CodeMetamac source, CodeMetamac target) {
        for (int i = 1; i <= SrmConstants.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER; i++) {
            Boolean openness = SrmServiceUtils.getCodeOpenness(source, i);
            SrmServiceUtils.setCodeOpenness(target, i, openness);
        }
    }
}