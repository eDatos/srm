package org.siemac.metamac.srm.core.serviceimpl.result;

import org.siemac.metamac.core.common.exception.MetamacException;

import java.util.ArrayList;
import java.util.List;

public abstract class Result<T> {
    T content;
    final List<MetamacException> exceptions = new ArrayList<>();

    protected Result() {
    }

    protected Result(T content, List<MetamacException> exceptions) {
        this.content = content;
        if (exceptions != null) {
            this.exceptions.addAll(exceptions);
        }
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public List<MetamacException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<MetamacException> exceptions) {
        this.exceptions.clear();
        this.exceptions.addAll(exceptions);
    }

    public MetamacException getMainException() {
        return exceptions.isEmpty() ? null : exceptions.get(0);
    }

    public List<MetamacException> getSecondaryExceptions() {
        return exceptions.isEmpty() ? new ArrayList<>() : exceptions.subList(1, exceptions.size());
    }

    public void addException(MetamacException exception) {
        exceptions.add(exception);
    }

    public boolean isOk() {
        return exceptions.isEmpty();
    }
}
