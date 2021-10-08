package org.siemac.metamac.srm.core.serviceapi;

import org.apache.avro.specific.SpecificRecordBase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;

import java.util.List;

public interface StreamMessagingService {

    <E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> SendStreamMessageResult sendMessage(E messageContent, StreamMessagingCallback<E, A, M> streamMessagingCallback);
    <E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> void resendAllPendingAndFailedMessages(StreamMessagingCallback<E, A, M> streamMessagingCallback) throws MetamacException;

    /**
     * @param <E> The Entity that contains the information to be sent
     * @param <A> The Avro object to be sent via kafka
     * @param <M> The Mapper that converts the entity into the avro object
     */
    interface StreamMessagingCallback<E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> {

        String getUniqueIdentifier(E messageContent);
        String getProducerRecordKey(E messageContent);
        List<E> getPendingOrFailedMessages() throws MetamacException;
        M getMapper();
        StreamMessageStatusEnum getStreamMessageStatus(E messageContent);
        void setStreamMessageStatus(E messageContent, StreamMessageStatusEnum streamMessageStatus);
        String getTopic();
    }
}
