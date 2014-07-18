package org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraintType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraints;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Key;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.KeyPartType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.KeyParts;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Keys;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Region;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.RegionReference;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.RegionValueType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Regions;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.BaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;

import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;
import com.arte.statistic.sdmx.srm.core.constraint.domain.KeyPart;
import com.arte.statistic.sdmx.srm.core.constraint.domain.KeyValue;
import com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue;

@org.springframework.stereotype.Component
public class ContentConstraintsDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl implements ContentConstraintsDo2RestMapperV10 {

    @Override
    public ContentConstraints toContentConstraints(PagedResult<ContentConstraint> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        ContentConstraints targets = new ContentConstraints();
        targets.setKind(SrmRestConstants.KIND_CONTENT_CONSTRAINTS);

        // Pagination
        String baseLink = toContentConstraintsLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (ContentConstraint source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getContentConstraints().add(target);
        }
        return targets;
    }

    @Override
    public org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint toContentConstraint(ContentConstraint source) {
        if (source == null) {
            return null;
        }

        org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint target = new org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint();

        // API
        target.setKind(SrmRestConstants.KIND_CONTENT_CONSTRAINT);
        target.setSelfLink(toContentConstraintSelfLink(source));
        target.setParentLink(toContentConstraintParentLink(source));
        target.setChildLinks(toContentConstraintChildLinks(source));

        // Extension
        SrmLifeCycleMetadata srmLifeCycleMetadata = null; // Draft Status is Null in API
        if (BooleanUtils.isTrue(source.getMaintainableArtefact().getFinalLogic())) {
            srmLifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.INTERNALLY_PUBLISHED);
        }
        if (BooleanUtils.isTrue(source.getMaintainableArtefact().getPublicLogic())) {
            srmLifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        }
        toMaintainableArtefact(source.getMaintainableArtefact(), srmLifeCycleMetadata, target);

        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }

        // Others
        target.setType(ContentConstraintType.fromValue(source.getType().getName()));
        target.setConstraintAttachment(toResourceExternalItem(source.getConstraintAttachment(), getStatisticalResourcesApiExternalEndpoint()));
        target.setRegions(toRegions(source.getRegions()));

        return target;
    }

    @Override
    public RegionReference toRegionReference(String contentConstraintUrn, RegionValue source) {
        if (source == null) {
            return null;
        }

        Region region = toRegion(source);
        RegionReference target = new RegionReference();
        target.setCode(region.getCode());
        target.setKeys(region.getKeys());
        target.setRegionValueType(region.getRegionValueType());
        target.setContentConstraintUrn(contentConstraintUrn);

        return target;
    }

    /**
     * SELF LINK
     * 
     * @param source
     * @return
     */
    private ResourceLink toContentConstraintSelfLink(ContentConstraint source) {
        return toResourceLink(SrmRestConstants.KIND_CONTENT_CONSTRAINT, toContentConstraintLink(source));
    }

    /**
     * PARENT LINK
     * 
     * @param source
     * @return
     */
    private ResourceLink toContentConstraintParentLink(ContentConstraint source) {
        return toResourceLink(SrmRestConstants.KIND_CONTENT_CONSTRAINTS, toContentConstraintsLink(null, null, null));
    }

    /**
     * CHILD LINK
     * 
     * @param source
     * @return
     */
    private ChildLinks toContentConstraintChildLinks(ContentConstraint source) {
        // nothing
        return null;
    }

    /**
     * CONTENT CONSTRAINTS LINK
     * 
     * @param agencyID
     * @param resourceID
     * @param version
     * @return
     */
    private String toContentConstraintsLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathContentConstraints(), agencyID, resourceID, version);
    }

    /**
     * CONTENT CONSTRAINT LINK
     * 
     * @param structureVersion
     * @return
     */
    private String toContentConstraintLink(ContentConstraint structureVersion) {
        return toMaintainableArtefactLink(toSubpathContentConstraints(), structureVersion.getMaintainableArtefact());
    }

    private Resource toResource(ContentConstraint source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CONTENT_CONSTRAINT, toContentConstraintSelfLink(source), target);
        return target;
    }

    private String toSubpathContentConstraints() {
        return SrmRestConstants.LINK_SUBPATH_CONTENT_CONSTRAINTS;
    }

    private Regions toRegions(Set<RegionValue> sources) {
        Regions target = new Regions();
        for (RegionValue regionValue : sources) {
            Region region = toRegion(regionValue);
            if (region != null) {
                target.getRegions().add(region);
            }
        }
        target.setTotal(BigInteger.valueOf(target.getRegions().size()));

        return target;
    }

    private Region toRegion(RegionValue source) {
        if (source == null) {
            return null;
        }

        Region target = new Region();

        target.setCode(source.getCode());
        target.setRegionValueType(RegionValueType.fromValue(source.getRegionValueTypeEnum().getName()));

        target.setKeys(toKeys(source.getKeys()));

        return target;
    }

    private Keys toKeys(Set<KeyValue> sources) {
        Keys target = new Keys();
        for (KeyValue sourceKeyValue : sources) {
            Key key = toKey(sourceKeyValue);
            if (key != null) {
                target.getKeies().add(key);
            }
        }
        target.setTotal(BigInteger.valueOf(target.getKeies().size()));

        return target;
    }

    private Key toKey(KeyValue source) {
        if (source == null) {
            return null;
        }

        Key target = new Key();

        target.setIncluded(BooleanUtils.toBoolean(source.getIncluded()));
        target.setKeyParts(toKeyParts(source.getParts()));

        return target;
    }

    private KeyParts toKeyParts(List<KeyPart> sources) {
        KeyParts target = new KeyParts();

        for (KeyPart sourceKeyPart : sources) {
            org.siemac.metamac.rest.structural_resources.v1_0.domain.KeyPart targetKeyPart = toKeyPart(sourceKeyPart);
            if (targetKeyPart != null) {
                target.getKeyParts().add(targetKeyPart);
            }
        }
        target.setTotal(BigInteger.valueOf(target.getKeyParts().size()));

        return target;
    }

    private org.siemac.metamac.rest.structural_resources.v1_0.domain.KeyPart toKeyPart(KeyPart source) {
        if (source == null) {
            return null;
        }

        org.siemac.metamac.rest.structural_resources.v1_0.domain.KeyPart keyPart = new org.siemac.metamac.rest.structural_resources.v1_0.domain.KeyPart();
        keyPart.setIdentifier(source.getIdentifier());
        keyPart.setValue(source.getValue());
        keyPart.setPosition(source.getPosition());
        keyPart.setType(KeyPartType.valueOf(source.getType().getName()));

        if (KeyPartType.TIME_RANGE.equals(source.getType())) {
            keyPart.setBeforePeriod(source.getBeforePeriod());
            keyPart.setBeforePeriodInclusive(source.getBeforePeriodInclusive());

            keyPart.setAfterPeriod(source.getAfterPeriod());
            keyPart.setAfterPeriodInclusive(source.getAfterPeriodInclusive());

            keyPart.setStartPeriod(source.getStartPeriod());
            keyPart.setStartPeriodInclusive(source.getStartPeriodInclusive());

            keyPart.setEndPeriod(source.getEndPeriod());
            keyPart.setEndPeriodInclusive(source.getEndPeriodInclusive());
        } else {
            keyPart.setCascadeValues(BooleanUtils.toBoolean(source.getCascadeValues()));
        }

        return keyPart;
    }
}