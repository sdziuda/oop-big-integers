package pl.edu.mimuw;

import java.util.Arrays;

import static java.lang.Math.*;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int signLength = 0;

    if (number.charAt(signLength) == '-') {
      this.isPositive = false;
      signLength++;
    } else {
      this.isPositive = true;
    }

    final int[] digits = new int[number.length() - signLength];
    for (int i = 0; i < digits.length; i++) {
      digits[i] = (number.charAt(i + signLength) - '0');
    }

    this.digits = deleteZeros(digits);
  }

  public BigInt(int[] digits, boolean isPositive) {
    this.digits = digits;
    this.isPositive = isPositive;
  }

  public BigInt(int number) {
    this(Integer.toString(number));
  }

  public BigInt neg() {
    final var digits = Arrays.copyOf(this.digits, this.digits.length);
    return new BigInt(digits, !this.isPositive);
  }

  /* A method to add two numbers of  the same sign.
  This method will return a table of digits of the
  result of adding the object we are calling it on
  and the parameter. */
  private int[] addSameSign(BigInt other) {
    final var result = new int[max(this.digits.length, other.digits.length) + 1];

    // Standard algorithm for addition of two big numbers.
    for (int i = 1; i < result.length; i++) {
      if (this.digits.length - i >= 0) {
        result[result.length - i] += this.digits[this.digits.length - i];
      }

      if (other.digits.length - i >= 0) {
        result[result.length - i] += other.digits[other.digits.length - i];
      }

      if (result[result.length - i] >= 10) {
        result[result.length - i] %= 10;
        result[result.length - i - 1] += 1;
      }
    }

    return result;
  }

  /* A method to determine which of two numbers has
  greater (worth notable: not equal) value.
  This method will return true if the object we are
  calling it on has greater absolute value than the
  parameter. */
  private boolean greaterAbs(BigInt other) {
    if (this.digits.length > other.digits.length) {
      return true;
    } else if (this.digits.length < other.digits.length) {
      return false;
    } else {
      int it = 0;
      while (this.digits[it] == other.digits[it]) {
        it++;
        if (it == this.digits.length) return false;
      }
      return (this.digits[it] > other.digits[it]);
    }
  }

  /* A method to add two numbers with different sign,
  the parameter of this function should be the one
  number that has a smaller absolute value. */
  private int[] addDiffSign(BigInt smaller) {
    final var result = new int[max(this.digits.length, smaller.digits.length) + 1];

    // Standard algorithm for subtraction of two big numbers.
    for (int i = 1; i < result.length; i++) {
      if (this.digits.length - i >= 0) {
        result[result.length - i] += this.digits[this.digits.length - i];
      }

      if (smaller.digits.length - i >= 0) {
        result[result.length - i] -= smaller.digits[smaller.digits.length - i];
      }

      if (result[result.length - i] < 0) {
        result[result.length - i] += 10;
        this.digits[this.digits.length - i - 1]--;
        if (this.digits[this.digits.length - i - 1] < 0) {
          this.digits[this.digits.length - i - 1] = 9;
        }
      }
    }

    return result;
  }

  /* A method to return a new table with the same content as
  digits[] but without leading zeros. */
  private static int[] deleteZeros(int[] digits) {
    int count = 0;

    // First we have to count how many leading zeros are there.
    if (digits.length > 1) {
      int it = 0;
      while (digits[it] == 0) {
        count++;
        it++;
      }
    }

    // Now we have to create a new table and copy content from digits[].
    var result = new int[digits.length - count];
    System.arraycopy(digits, count, result, 0, result.length);

    return result;
  }

  public BigInt add(BigInt other) {
    var tempDigits = new int[max(this.digits.length, other.digits.length) + 1];
    boolean isPositive = true;

    /* We have to differentiate the cases in which,
    the two numbers are of the same or different sign
    (or when they are equal, for convenience). */
    if (this.isPositive == other.isPositive) {
      tempDigits = this.addSameSign(other);
      isPositive = this.isPositive;
    } else if (this.greaterAbs(other)) {
      tempDigits = this.addDiffSign(other);
      isPositive = this.isPositive;
    } else if (other.greaterAbs(this)) {
      tempDigits = other.addDiffSign(this);
      isPositive = other.isPositive;
    } else {
      tempDigits = new int[]{0};
    }

    // Last thing to do is delete the leading zeros.
    var result = deleteZeros(tempDigits);

    return new BigInt(result, isPositive);
  }

  public BigInt times(BigInt other) {
    final var tempDigits = new int[this.digits.length + other.digits.length + 1];
    final boolean isPositive = this.isPositive == other.isPositive;

    // First we have to multiply digits of two numbers one by one.
    for (int i = this.digits.length - 1; i >= 0; i--) {
      for (int j = other.digits.length - 1; j >= 0; j--) {
        tempDigits[i + j + 2] += this.digits[i] * other.digits[j];
      }
    }

    /* Now we have to "fix" the table, so that each place holds
    only one digit. */
    for (int i = tempDigits.length - 1; i >= 1; i--) {
      if (tempDigits[i] >= 10) {
        tempDigits[i - 1] += tempDigits[i] / 10;
        tempDigits[i] %= 10;
      }
    }

    // Last thing to do is delete the leading zeros.
    var result = deleteZeros(tempDigits);

    return new BigInt(result, isPositive);
  }

  @Override
  public String toString() {
    final var result = new StringBuilder();

    // First we have to check the sign.
    if (!this.isPositive) {
      result.append("-");
    }

    // All we have to do now is convert from digits to chars.
    for (int digit : this.digits) {
      result.append((char) (digit + '0'));
    }

    return result.toString();
  }

  @Override
  public int hashCode() {
    final var arrayHashCode = Arrays.hashCode(digits);
    final var positiveHashCode = Boolean.hashCode(isPositive);

    return arrayHashCode * 17 + positiveHashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BigInt)) return false;
    final var other = (BigInt) obj;

    return Arrays.equals(this.digits, other.digits)
      && this.isPositive == other.isPositive;
  }
}
