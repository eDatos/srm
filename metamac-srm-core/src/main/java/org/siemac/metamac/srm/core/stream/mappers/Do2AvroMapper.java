package org.siemac.metamac.srm.core.stream.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Do2AvroMapper<D, A> {
    A toAvro(D source);

    default List<A> toAvros(Collection<D> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<A> list = new ArrayList<>();
        for (D d : source) {
            A a = toAvro(d);
            list.add(a);
        }
        return list;
    }
}
