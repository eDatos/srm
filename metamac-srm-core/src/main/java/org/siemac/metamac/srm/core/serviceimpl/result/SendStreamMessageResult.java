package org.siemac.metamac.srm.core.serviceimpl.result;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;

import java.util.List;

public class SendStreamMessageResult extends Result<StreamMessageStatusEnum> {

    public SendStreamMessageResult() {
    }

    public SendStreamMessageResult(StreamMessageStatusEnum status, List<MetamacException> exceptions) {
        super(status, exceptions);
    }

    @Override
    public boolean isOk() {
        return super.isOk() && content != StreamMessageStatusEnum.FAILED;
    }
}
