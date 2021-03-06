package eu.sim642.adventofcode2015

import org.scalatest.FunSuite
import Day24._

class Day24Test extends FunSuite {

  val exampleWeights = ((1 to 5) ++ (7 to 11)).toList

  test("Part 1 examples") {
    assert(idealFirstQE(exampleWeights, 3) == 99)
  }

  test("Part 1 input answer") {
    assert(idealFirstQE(input, 3) == 11846773891L)
  }

  test("Part 2 examples") {
    assert(idealFirstQE(exampleWeights, 4) == 44)
  }

  test("Part 2 input answer") {
    assert(idealFirstQE(input, 4) == 80393059)
  }
}
