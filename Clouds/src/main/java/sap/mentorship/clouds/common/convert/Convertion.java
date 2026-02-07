package sap.mentorship.clouds.common.convert;

public interface Convertion<R, T> {
    R convert(T from);
}
