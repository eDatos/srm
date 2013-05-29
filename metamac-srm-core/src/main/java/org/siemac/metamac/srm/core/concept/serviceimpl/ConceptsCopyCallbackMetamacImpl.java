package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.ConceptsCopyCallbackImpl;

/**
 * Copy Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class ConceptsCopyCallbackMetamacImpl extends ConceptsCopyCallbackImpl {

    @Autowired
    private ConceptsMetamacService   conceptsMetamacService;

    @Autowired
    private ConceptMetamacRepository conceptMetamacRepository;

    @Override
    public ItemSchemeVersion createItemSchemeVersion() {
        return new ConceptSchemeVersionMetamac();
    }

    @Override
    public void copyItemSchemeVersion(ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) {
        super.copyItemSchemeVersion(sourceItemSchemeVersion, targetItemSchemeVersion);
        ConceptSchemeVersionMetamac source = (ConceptSchemeVersionMetamac) sourceItemSchemeVersion;
        ConceptSchemeVersionMetamac target = (ConceptSchemeVersionMetamac) targetItemSchemeVersion;
        target.setType(source.getType());
        target.setRelatedOperation(copy(source.getRelatedOperation()));
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public void createItemSchemeVersionRelations(ServiceContext ctx, ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) throws MetamacException {
        super.createItemSchemeVersionRelations(ctx, sourceItemSchemeVersion, targetItemSchemeVersion);
        ConceptSchemeVersionMetamac source = (ConceptSchemeVersionMetamac) sourceItemSchemeVersion;
        ConceptSchemeVersionMetamac target = (ConceptSchemeVersionMetamac) targetItemSchemeVersion;
        conceptsMetamacService.versioningRelatedConcepts(ctx, source, target);
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) {
        return conceptMetamacRepository.findConceptsByConceptSchemeUnordered(itemSchemeId, SrmServiceUtils.getItemResultSelection(getCopyOperationType()));
    }

    @Override
    public Item createItem() {
        return new ConceptMetamac();
    }

    @Override
    public void copyItem(Item sourceItem, ItemResult sourceItemResult, Item targetItem) {
        super.copyItem(sourceItem, sourceItemResult, targetItem);

        ConceptMetamac source = (ConceptMetamac) sourceItem;
        ConceptMetamac target = (ConceptMetamac) targetItem;

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult

        // Copy efficiently international strings
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) sourceItemResult.getExtensionPoint();
        target.setPluralName(copyInternationalString(extensionPoint.getPluralName()));
        target.setAcronym(copyInternationalString(extensionPoint.getAcronym()));
        target.setDescriptionSource(copyInternationalString(extensionPoint.getDescriptionSource()));
        target.setContext(copyInternationalString(extensionPoint.getContext()));
        target.setDocMethod(copyInternationalString(extensionPoint.getDocMethod()));
        target.setDerivation(copyInternationalString(extensionPoint.getDerivation()));
        target.setLegalActs(copyInternationalString(extensionPoint.getLegalActs()));

        // Other metadata
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setConceptType(source.getConceptType());
        target.setVariable(source.getVariable());
        target.setQuantity(copy(source.getQuantity()));

        // can copy "extends" and "roles", because they are concepts in another concept scheme
        target.setConceptExtends(source.getConceptExtends());
        for (ConceptMetamac conceptRole : source.getRoleConcepts()) {
            target.addRoleConcept(conceptRole);
        }
        // Do not copy related concepts in this point, because they belong to same concept scheme, and new concept in new version must relate to versioned related concept.
        // They will be copied in createItemSchemeVersionRelations
    }

    private static Quantity copy(Quantity source) {
        if (source == null) {
            return null;
        }
        Quantity target = new Quantity();
        target.setQuantityType(source.getQuantityType());
        target.setUnitCode(source.getUnitCode());
        target.setUnitSymbolPosition(source.getUnitSymbolPosition());
        target.setSignificantDigits(source.getSignificantDigits());
        target.setDecimalPlaces(source.getDecimalPlaces());
        target.setUnitMultiplier(source.getUnitMultiplier());
        target.setMinimum(source.getMinimum());
        target.setMaximum(source.getMaximum());
        // target.setNumerator(source.getNumerator()); // TODO no se puede copiar xq puede ser del mismo concept scheme
        // target.setDenominator(source.getDenominator()); // TODO no se puede copiar xq puede ser del mismo concept scheme
        target.setIsPercentage(source.getIsPercentage());
        target.setPercentageOf(copyInternationalString(source.getPercentageOf()));
        target.setBaseValue(source.getBaseValue());
        target.setBaseTime(source.getBaseTime());
        // target.setBaseLocation(source.getBaseLocation()); // TODO quantity.baseLocation
        // target.setBaseQuantity(source.getBaseQuantity()); // TODO no se puede copiar xq puede ser del mismo concept scheme
        return target;
    }
}