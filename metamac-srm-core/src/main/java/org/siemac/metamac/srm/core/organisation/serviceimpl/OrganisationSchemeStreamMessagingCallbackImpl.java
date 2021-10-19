package org.siemac.metamac.srm.core.organisation.serviceimpl;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.siemac.metamac.srm.core.stream.mappers.OrganisationSchemeDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

@Component("organisationSchemeStreamMessagingCallback")
public class OrganisationSchemeStreamMessagingCallbackImpl implements StreamMessagingCallback<OrganisationSchemeVersionMetamac, OrganisationSchemeAvro, OrganisationSchemeDo2AvroMapper> {

    @Autowired
    private OrganisationSchemeDo2AvroMapper organisationSchemeDo2Avro;

    @Autowired
    private OrganisationSchemeVersionMetamacRepository organisationSchemeVersionMetamacRepository;

    @Override
    public String getUniqueIdentifier(OrganisationSchemeVersionMetamac messageContent) {
        return messageContent.getMaintainableArtefact().getCode();
    }

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public String getProducerRecordKey(OrganisationSchemeVersionMetamac messageContent){
        return messageContent.getMaintainableArtefact().getUrn();
    }

    @Override
    public List<OrganisationSchemeVersionMetamac> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(OrganisationSchemeVersionMetamac.class)
                .lbrace()
                .withProperty(OrganisationSchemeVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(OrganisationSchemeVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .lbrace()
                .withProperty(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.EXTERNALLY_PUBLISHED)
                .or()
                .withProperty(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                .rbrace()
                .build();
        // @formatter:on
        return organisationSchemeVersionMetamacRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public OrganisationSchemeDo2AvroMapper getMapper() {
        return organisationSchemeDo2Avro;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(OrganisationSchemeVersionMetamac messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(OrganisationSchemeVersionMetamac messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public String getTopic() throws MetamacException {
        return configurationService.retrieveKafkaTopicOrganisationSchemesPublication();
    }
}
