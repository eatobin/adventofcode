package eu.sim642.adventofcode2018

import org.scalatest.FunSuite
import Day22._
import eu.sim642.adventofcode2017.Day3.Pos

class Day22Test extends FunSuite {

  val exampleInput =
    """depth: 510
      |target: 10,10""".stripMargin

  test("parseInput") {
    assert(parseInput(exampleInput) == (510, Pos(10, 10)))
  }

  test("Part 1 examples") {
    assert(totalRiskLevel(exampleInput) == 114)
  }

  test("Part 1 input answer") {
    assert(totalRiskLevel(input) == 11359)
  }
}
