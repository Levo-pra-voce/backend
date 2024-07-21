package com.levopravoce.backend.common.validator;

import org.springframework.stereotype.Component;

@Component
public class CnhValidator implements ValidatorI<String> {

  @Override
  public boolean isValid(String cnh) {
    if (cnh == null || cnh.length() != 11 || !cnh.matches("\\d{11}")) {
      return false;
    }

    int sum1 = 0;
    int sum2 = 0;
    int weight1 = 9;
    int weight2 = 1;

    for (int i = 0; i < 9; i++) {
      int digit = Character.getNumericValue(cnh.charAt(i));
      sum1 += digit * (weight1 - i);
      sum2 += digit * (weight2 + i);
    }

    int digit1 = sum1 % 11;
    int digit2 = sum2 % 11;

    if (digit1 == 10) {
      digit1 = 0;
    }

    if (digit2 == 10) {
      digit2 = 0;
    }

    return digit1 == Character.getNumericValue(cnh.charAt(9))
        && digit2 == Character.getNumericValue(cnh.charAt(10));
  }
}
