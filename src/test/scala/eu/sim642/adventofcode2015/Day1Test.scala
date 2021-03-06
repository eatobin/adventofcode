package eu.sim642.adventofcode2015

import org.scalatest.FunSuite
import Day1._

class Day1Test extends FunSuite {

  test("Part 1 examples") {
    assert(finalFloor("(())") == 0)
    assert(finalFloor("()()") == 0)
    assert(finalFloor("(((") == 3)
    assert(finalFloor("(()(()(") == 3)
    assert(finalFloor("))(((((") == 3)
    assert(finalFloor("())") == -1)
    assert(finalFloor("))(") == -1)
    assert(finalFloor(")))") == -3)
    assert(finalFloor(")())())") == -3)
  }

  test("Part 1 input answer") {
    assert(finalFloor(input) == 232)
  }

  test("Part 2 examples") {
    assert(basementPosition(")") == 1)
    assert(basementPosition("()())") == 5)
  }

  test("Part 2 input answer") {
    assert(basementPosition(input) == 1783)
  }
}
