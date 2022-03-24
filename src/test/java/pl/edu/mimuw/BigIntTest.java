package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigIntTest {

  @Test
  void testSmallAdd() {
    final var x = new BigInt(42);
    final var y = new BigInt(-24);
    final var expectedResult = "18";

    final var result = x.add(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testBigAdd() {
    final var x = new BigInt("-853487563485263495358327542");
    final var y = new BigInt("123432958723478523239");
    final var expectedResult = "-853487440052304771879804303";

    final var result = x.add(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testSmallTimes() {
    final var x = new BigInt("45");
    final var y = new BigInt("-19");
    final var expectedResult = "-855";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testBigTimes() {
    final var x = new BigInt("58937589237582356237853629847328956732489237534982658925687242586325028357230957235890325723852");
    final var y = new BigInt("98573984723646366363663463453485237953498573984385257092345798042752857249857253252358237582");
    final var expectedResult = "5809713021153987676483114449890793999581914862036170922471262096320535256086937377845554214156208895499803136701250190519179308087899330996009447803284249218272913580990134871209140205864";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testNegation() {
    final var small = new BigInt("37");
    final var big = new BigInt("-3453458734658736");

    final var expectedSmallNegated = "-37";
    final var expectedBigNegated = "3453458734658736";

    final var smallNegated = small.neg().toString();
    final var bigNegated = big.neg().toString();

    assertEquals(expectedSmallNegated, smallNegated);
    assertEquals(expectedBigNegated, bigNegated);
  }
}
