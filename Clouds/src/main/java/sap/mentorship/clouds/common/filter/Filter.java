package sap.mentorship.clouds.common.filter;

@FunctionalInterface
public interface Filter<T> {
    boolean test(T t);

    default Filter<T> and(Filter<T> other) {
        return x -> this.test(x) && other.test(x);
    }

    static <T> Filter<T> all() {
        return x -> true;
    }
}
