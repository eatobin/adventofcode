package eu.sim642.adventofcode2018

import org.scalatest.FunSuite
import Day8._
import eu.sim642.AdventOfCodeSuite

class Day8Test extends FunSuite with AdventOfCodeSuite {

  val exampleInput = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

  test("Part 1 examples") {
    assert(metadataSum(parseTree(exampleInput)) == 138)
  }

  test("Part 1 input answer") {
    assert(metadataSum(parseTree(input)) == 46578)
  }

  test("Part 2 examples") {
    assert(value(parseTree(exampleInput)) == 66)
  }

  test("Part 2 input answer") {
    assert(value(parseTree(input)) == 31251)
  }
}
