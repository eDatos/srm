package org.siemac.metamac.srm.web.server.rest;

import java.text.MessageFormat;
import java.util.Locale;

import javax.ws.rs.core.Response;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.core.common.util.ServiceContextUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.srm.core.invocation.MetamacApisLocator;
import org.siemac.metamac.srm.core.notices.ServiceNoticeAction;
import org.siemac.metamac.srm.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd.DataStructuresDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.siemac.metamac.srm.web.server.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.server.rest.utils.RestExceptionUtils;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.MaintainableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@Component(NoticesRestInternalFacade.BEAN_ID)
public class NoticesRestInternalFacadeImpl implements NoticesRestInternalFacade {

    @Autowired
    private MetamacApisLocator             metamacApisLocator;

    @Autowired
    private RestExceptionUtils             restExceptionUtils;

    @Autowired
    private ConceptsDo2RestMapperV10       conceptsDo2RestMapperV10;

    @Autowired
    private CodesDo2RestMapperV10          codesDo2RestMapperV10;

    @Autowired
    private DataStructuresDo2RestMapperV10 dataStructuresDo2RestMapperV10;

    @Autowired
    private OrganisationsDo2RestMapperV10  organisationsDo2RestMapperV10;

    @Autowired
    private CategoriesDo2RestMapperV10     categoriesDo2RestMapperV10;

    @Override
    public void createInternalPublicationNotification(ServiceContext serviceContext, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType)
            throws MetamacWebException {
        createNotification(serviceContext, ServiceNoticeAction.RESOURCE_INTERNAL_PUBLICATION, ServiceNoticeMessage.RESOURCE_INTERNAL_PUBLICATION_OK, maintainableArtefactDto, resourceType);
    }

    @Override
    public void createExternalPublicationNotification(ServiceContext serviceContext, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType)
            throws MetamacWebException {
        createNotification(serviceContext, ServiceNoticeAction.RESOURCE_EXTERNAL_PUBLICATION, ServiceNoticeMessage.RESOURCE_EXTERNAL_PUBLICATION_OK, maintainableArtefactDto, resourceType);
    }

    private void createNotification(ServiceContext ctx, String actionCode, String messageCode, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType)
            throws MetamacWebException {

        ResourceInternal resource = this.maintainableResourceToResourceInternal(ctx, maintainableArtefactDto, resourceType);
        ResourceInternal[] resources = {resource};

        String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTRUCTURALES.getName();
        String subject = buildSubject(ctx, actionCode, resourceType);
        String messageText = buildMessage(ctx, messageCode, maintainableArtefactDto, resourceType);

        Message message = MessageBuilder.message().withText(messageText).withResources(resources).build();

        Notice notification = NoticeBuilder.notification().withMessages(message).withSendingApplication(sendingApp).withSendingUser(ctx.getUserId()).withSubject(subject).build();

        try {
            Response response = metamacApisLocator.getNoticesRestInternalFacadeV10().createNotice(notification);
            restExceptionUtils.checkSendNotificationRestResponseAndThrowErrorIfApplicable(ctx, response);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private ResourceInternal maintainableResourceToResourceInternal(ServiceContext ctx, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType) {
        ResourceInternal resource = new ResourceInternal();
        if (maintainableArtefactDto != null) {
            resource.setId(maintainableArtefactDto.getCode());
            resource.setUrn(maintainableArtefactDto.getUrn());
            resource.setKind(ExternalItemUtils.getResourceKind(resourceType));
            resource.setName(this.toInternationalString(maintainableArtefactDto.getName()));
            resource.setManagementAppLink(getMaintainableArtefactManagementApplicationLink(maintainableArtefactDto, resourceType));
            resource.setSelfLink(getMaintainableArtefactSelfLink(maintainableArtefactDto, resourceType));
        }
        return resource;
    }

    private ResourceLink getMaintainableArtefactSelfLink(MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType) {
        String agencyId = maintainableArtefactDto.getMaintainer().getCode();
        String resourceId = maintainableArtefactDto.getCode();
        String version = maintainableArtefactDto.getVersionLogic();
        switch (resourceType) {
            case CONCEPT_SCHEME:
                return conceptsDo2RestMapperV10.toConceptSchemeSelfLink(agencyId, resourceId, version);
            case CODELIST:
                return codesDo2RestMapperV10.toCodelistSelfLink(agencyId, resourceId, version);
            case DATASTRUCTURE:
                return dataStructuresDo2RestMapperV10.toDataStructureSelfLink(agencyId, resourceId, version);
            case ORGANISATION_UNIT_SCHEME:
                return getOrganisationSchemeSelfLink(agencyId, resourceId, version, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
            case DATA_CONSUMER_SCHEME:
                return getOrganisationSchemeSelfLink(agencyId, resourceId, version, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
            case DATA_PROVIDER_SCHEME:
                return getOrganisationSchemeSelfLink(agencyId, resourceId, version, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
            case AGENCY_SCHEME:
                return getOrganisationSchemeSelfLink(agencyId, resourceId, version, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
            case CATEGORY_SCHEME:
                return categoriesDo2RestMapperV10.toCategorySchemeSelfLink(agencyId, resourceId, version);
            default:
                return null;
        }
    }

    private ResourceLink getOrganisationSchemeSelfLink(String agencyId, String resourceId, String version, OrganisationSchemeTypeEnum organisationSchemeType) {
        return organisationsDo2RestMapperV10.toOrganisationSchemeSelfLink(agencyId, resourceId, version, organisationSchemeType);
    }

    private String getMaintainableArtefactManagementApplicationLink(MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType) {
        String urn = maintainableArtefactDto.getUrn();
        switch (resourceType) {
            case CONCEPT_SCHEME:
                return conceptsDo2RestMapperV10.toConceptSchemeManagementApplicationLink(urn);
            case CODELIST:
                return codesDo2RestMapperV10.toCodelistManagementApplicationLink(urn);
            case DATASTRUCTURE:
                return dataStructuresDo2RestMapperV10.toDataStructureManagementApplicationLink(urn);
            case ORGANISATION_UNIT_SCHEME:
                return getOrganisationSchemeManagementApplicationLink(urn, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
            case DATA_CONSUMER_SCHEME:
                return getOrganisationSchemeManagementApplicationLink(urn, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
            case DATA_PROVIDER_SCHEME:
                return getOrganisationSchemeManagementApplicationLink(urn, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
            case AGENCY_SCHEME:
                return getOrganisationSchemeManagementApplicationLink(urn, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
            case CATEGORY_SCHEME:
                return categoriesDo2RestMapperV10.toCategorySchemeManagementApplicationLink(urn);
            default:
                return null;
        }
    }

    private String getOrganisationSchemeManagementApplicationLink(String urn, OrganisationSchemeTypeEnum organisationSchemeType) {
        return organisationsDo2RestMapperV10.toOrganisationSchemeManagementApplicationLink(urn, organisationSchemeType);
    }

    private InternationalString toInternationalString(InternationalStringDto sources) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (LocalisedStringDto source : sources.getTexts()) {
            LocalisedString target = new LocalisedString();
            target.setValue(source.getLabel());
            target.setLang(source.getLocale());
            targets.getTexts().add(target);
        }
        return targets;
    }

    private String buildSubject(ServiceContext ctx, String actionCode, TypeExternalArtefactsEnum resourceType) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        String localizedMessage = getLocalizedMessage(locale, actionCode);
        String localizedResourceTypeName = getLocalizedResourceTypeName(locale, resourceType);
        localizedMessage = MessageFormat.format(localizedMessage, localizedResourceTypeName);
        return localizedMessage;
    }

    private String buildMessage(ServiceContext ctx, String messageCode, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        return getLocalizedMessage(locale, messageCode);
    }

    private String getLocalizedResourceTypeName(Locale locale, TypeExternalArtefactsEnum resourceType) {
        String resourceTypeCode = ExternalItemUtils.getResourceNameParameter(resourceType);
        return getLocalizedMessage(locale, resourceTypeCode);
    }

    private String getLocalizedMessage(Locale locale, String code) {
        return LocaleUtil.getMessageForCode(code, locale);
    }
}
