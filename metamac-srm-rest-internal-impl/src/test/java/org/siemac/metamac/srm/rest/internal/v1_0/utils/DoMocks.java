package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

public class DoMocks {

    public static void mockItemSchemeVersion(ItemSchemeVersion target, String resourceID, String version, String agencyID) {
        target.setIsPartial(Boolean.FALSE);
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setMaintainableArtefact(new MaintainableArtefact());
        mockMaintainableArtefact(target.getMaintainableArtefact(), resourceID, version, agencyID);
    }

    public static void mockItem(Item target, String resourceID, ItemSchemeVersion itemSchemeVersion, Item parent) {
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setParent(parent);
        target.setItemSchemeVersion(itemSchemeVersion);
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefact(target.getNameableArtefact(), resourceID, null);
    }

    public static InternationalString mockInternationalString(String metadata, String subCode) {
        String subTitle = subCode != null ? metadata + "-" + subCode : metadata;
        return mockInternationalString("es", subTitle + " en Español", "en", subTitle + " in English");
    }

    public static OrganisationMetamac mockMaintainer(String agencyID) {
        OrganisationMetamac target = new OrganisationMetamac();
        target.setIdAsMaintainer("idAsMaintainer" + agencyID);
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefact(target.getNameableArtefact(), agencyID, null);
        return target;
    }

    public static SrmLifeCycleMetadata mockLifecycleExternallyPublished() {
        SrmLifeCycleMetadata lifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        lifeCycleMetadata.setProductionValidationDate(new DateTime(2009, 9, 1, 1, 1, 1, 1));
        lifeCycleMetadata.setProductionValidationUser("production-user");
        lifeCycleMetadata.setDiffusionValidationDate(new DateTime(2010, 10, 2, 1, 1, 1, 1));
        lifeCycleMetadata.setDiffusionValidationUser("diffusion-user");
        lifeCycleMetadata.setInternalPublicationDate(new DateTime(2011, 11, 3, 1, 1, 1, 1));
        lifeCycleMetadata.setInternalPublicationUser("internal-publication-user");
        lifeCycleMetadata.setExternalPublicationDate(new DateTime(2012, 12, 4, 1, 1, 1, 1));
        lifeCycleMetadata.setExternalPublicationUser("external-publication-user");
        lifeCycleMetadata.setIsExternalPublicationFailed(Boolean.FALSE);
        lifeCycleMetadata.setExternalPublicationFailedDate(new DateTime(2013, 8, 2, 1, 1, 1, 1));
        return lifeCycleMetadata;
    }

    public static void mockMaintainableArtefact(MaintainableArtefact target, String resourceID, String version, String agencyID) {
        target.setVersionLogic(version);
        target.setValidFrom(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setValidTo(new DateTime(2013, 10, 4, 10, 12, 13, 14));
        target.setFinalLogic(Boolean.TRUE);
        target.setFinalLogicClient(Boolean.TRUE);
        target.setLatestFinal(Boolean.TRUE);
        target.setPublicLogic(Boolean.TRUE);
        target.setLatestPublic(Boolean.TRUE);
        target.setIsLastVersion(Boolean.TRUE);
        target.setIsImported(Boolean.FALSE);
        target.setIsExternalReference(Boolean.FALSE);
        target.setStructureURL("structureUrl-" + resourceID);
        target.setServiceURL("serviceUrl-" + resourceID);
        target.setMaintainer(mockMaintainer(agencyID));
        target.setReplacedByVersion("replacedBy");
        target.setReplaceToVersion("replaceTo");
        mockNameableArtefact(target, resourceID, version);
    }

    public static void mockNameableArtefact(NameableArtefact target, String resourceID, String version) {
        target.setName(mockInternationalString("name", getString(resourceID, version)));
        target.setDescription(mockInternationalString("description", getString(resourceID, version)));
        target.setComment(mockInternationalString("comment", getString(resourceID, version)));
        mockIdentifiableArtefact(target, resourceID, version);
    }

    protected static String getString(String resourceID, String version) {
        if (version == null) {
            return resourceID;
        } else {
            return resourceID + "v" + version;
        }
    }

    private static InternationalString mockInternationalString(String locale1, String label1, String locale2, String label2) {
        InternationalString target = new InternationalString();
        target.addText(new LocalisedString(locale1, label1));
        target.addText(new LocalisedString(locale2, label2));
        return target;
    }

    private static void mockIdentifiableArtefact(IdentifiableArtefact target, String resourceID, String version) {
        target.setCode(resourceID);
        if (version == null) {
            target.setUrn("urn:" + resourceID);
        } else {
            target.setUrn("urn:" + resourceID + ":" + version);
        }
        target.setUrnProvider(target.getUrn());
        target.setUriProvider(RestTestConstants.URI_MARK_TO_RECOGNISE_IS_IN_DATABASE + "uri:" + getString(resourceID, version));
        mockAnnotableArtefact(target);
    }

    private static void mockAnnotableArtefact(AnnotableArtefact target) {
        target.addAnnotation(mockAnnotation("code1"));
        target.addAnnotation(mockAnnotation("code2"));
    }

    private static Annotation mockAnnotation(String code) {
        Annotation annotation = new Annotation();
        annotation.setCode(code);
        annotation.setTitle("title-" + code);
        annotation.setType("type-" + code);
        annotation.setText(mockInternationalString("text", code));
        annotation.setUrl("url-" + code);
        return annotation;
    }
}