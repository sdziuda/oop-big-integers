package pl.edu.mimuw;

import java.util.Arrays;
import static java.lang.Math.*;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int signLength = 0;

    if(number.charAt(signLength) == '-') {
      this.isPositive = false;
      signLength++;
    } else {
      this.isPositive = true;
    }

    final int[] digits = new int[number.length() - signLength];
    for (int i = 0; i < digits.length; i++) {
      digits[i] = (number.charAt(i + signLength) - '0');
    }

    this.digits = digits;
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

  private int[] addSameSign(BigInt other) {
    final var result = new int[max(this.digits.length, other.digits.length) + 1];

    for (int i = 1; i < result.length ; i++) {
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

  private int[] addDiffSign(BigInt smaller) {
    final var result = new int[max(this.digits.length, smaller.digits.length) + 1];

    for (int i = 1; i < result.length ; i++) {
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

  private static int [] deleteZeros(int[] digits) {
    int count = 0;

    if (digits.length > 1) {
      int it = 0;
      while (digits[it] == 0) {
        count++;
        it++;
      }
    }

    var result = new int[digits.length - count];
    System.arraycopy(digits, count, result, 0, result.length);

    return result;
  }

  public BigInt add(BigInt other) {
    var tempDigits = new int[max(this.digits.length, other.digits.length) + 1];
    boolean isPositive = true;

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
      tempDigits = new int[] {0};
    }

    var result = deleteZeros(tempDigits);

    return new BigInt(result, isPositive);
  }

  public BigInt times(BigInt other) {
    final var tempDigits = new int[this.digits.length + other.digits.length + 1];
    final boolean isPositive = this.isPositive == other.isPositive;

    for (int i = this.digits.length - 1; i >= 0; i--) {
      for (int j = other.digits.length - 1; j >= 0; j--) {
        tempDigits[i + j + 2] += this.digits[i] * other.digits[j];
      }
    }

    for (int i = tempDigits.length - 1; i >= 1; i--) {
      if (tempDigits[i] >= 10) {
        tempDigits[i - 1] += tempDigits[i] / 10;
        tempDigits[i] %= 10;
      }
    }

    var result = deleteZeros(tempDigits);

    return new BigInt(result, isPositive);
  }

  @Override
  public String toString() {
    final var result = new StringBuilder();

    if (!this.isPositive) {
      result.append("-");
    }

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
