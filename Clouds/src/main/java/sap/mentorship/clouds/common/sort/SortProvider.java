package sap.mentorship.clouds.common.sort;

import java.util.Comparator;

public interface SortProvider<T, S> {
    Comparator<T> comparatorFor(S sort);
}
