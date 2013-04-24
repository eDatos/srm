package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesVersioningCopyUtils.CodesVersioningCopyCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Copy Metamac metadata
 */
@Component(CodesVersioningCopyCallbackMetamacImpl.BEAN_ID)
public class CodesVersioningCopyCallbackMetamacImpl implements CodesVersioningCopyCallback {

    public static final String    BEAN_ID = "codesVersioningCopyCallbackMetamac";

    @Autowired
    private CodesMetamacService   codesMetamacService;

    @Autowired
    private CodeMetamacRepository codeMetamacRepository;

    @Override
    public CodelistVersion createCodelistVersion() {
        return new CodelistVersionMetamac();
    }

    @Override
    public void copyCodelistVersion(CodelistVersion sourceSdmx, CodelistVersion targetSdmx) {
        CodelistVersionMetamac source = (CodelistVersionMetamac) sourceSdmx;
        CodelistVersionMetamac target = (CodelistVersionMetamac) targetSdmx;
        target.setShortName(BaseVersioningCopyUtils.copy(source.getShortName()));
        target.setDescriptionSource(BaseVersioningCopyUtils.copy(source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        target.setVariable(source.getVariable());
        target.setFamily(source.getFamily());
        // note: replaceBy and replaceTo metadata do not must be copied, because they are related to concrete versions of codelist
    }

    @Override
    public void createCodelistRelations(ServiceContext ctx, CodelistVersion sourceSdmx, CodelistVersion targetSdmx) throws MetamacException {
        CodelistVersionMetamac source = (CodelistVersionMetamac) sourceSdmx;
        CodelistVersionMetamac target = (CodelistVersionMetamac) targetSdmx;
        // Visualisations
        codesMetamacService.versioningCodelistOrderVisualisations(ctx, source, target);
        codesMetamacService.versioningCodelistOpennessVisualisations(ctx, source, target);
    }

    @Override
    public Boolean mustCopyCodes() {
        return Boolean.TRUE;
    }

    @Override
    public Boolean isOverridedFindCodesEfficiently() {
        return Boolean.TRUE;
    }

    @Override
    public List<ItemResult> findCodesEfficiently(Long codelistId) {
        return codeMetamacRepository.findCodesByCodelistUnordered(codelistId, CodeMetamacResultSelection.VERSIONING);
    }

    @Override
    public Code createCode() {
        return new CodeMetamac();
    }

    @Override
    public void copyCode(Code sourceSdmx, ItemResult itemResultSource, Code targetSdmx) {
        CodeMetamac source = (CodeMetamac) sourceSdmx;
        CodeMetamac target = (CodeMetamac) targetSdmx;
        if (itemResultSource != null) {
            if (itemResultSource.getExtensionPoint() != null) {
                CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) itemResultSource.getExtensionPoint();
                target.setShortName(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getShortName()));
            }
        } else {
            target.setShortName(BaseVersioningCopyUtils.copy(source.getShortName()));
        }
        target.setVariableElement(source.getVariableElement());
        copyCodeOrderVisualisations(source, target);
        copyCodeOpennessVisualisations(source, target);
    }

    @Override
    public String getBeanName() {
        return BEAN_ID;
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