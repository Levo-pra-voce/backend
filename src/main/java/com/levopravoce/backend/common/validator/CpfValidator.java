package com.levopravoce.backend.common.validator;

import java.util.InputMismatchException;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CpfValidator implements ValidatorI<String> {
  private static Set<String> invalidCpfs = Set.of("00000000000", "11111111111", "22222222222",
      "33333333333", "44444444444", "55555555555", "66666666666", "77777777777", "88888888888",
      "99999999999");

  @Override
  public boolean isValid(String cpf) {
    if (cpf == null) {
      return false;
    }
    if (invalidCpfs.contains(cpf) || cpf.length() != 11) {
      return false;
    }
    char dig10,
        dig11;
    int sm, i, r, num, peso;
    try {
      sm = 0;
      peso = 10;
      for (i = 0; i < 9; i++) {
        num = cpf.charAt(i) - 48;
        sm = sm + (num * peso);
        peso = peso - 1;
      }
      r = 11 - (sm % 11);
      if ((r == 10) || (r == 11)) {
        dig10 = '0';
      } else {
        dig10 = (char) (r + 48);
      }
      sm = 0;
      peso = 11;
      for (i = 0; i < 10; i++) {
        num = cpf.charAt(i) - 48;
        sm = sm + (num * peso);
        peso = peso - 1;
      }
      r = 11 - (sm % 11);
      if ((r == 10) || (r == 11)) {
        dig11 = '0';
      } else {
        dig11 = (char) (r + 48);
      }
      return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
    } catch (InputMismatchException erro) {
      return (false);
    }
  }

}
