package com.levopravoce.backend.common.validator;

public interface ValidatorI<T> {
    boolean isValid(T param);
}
