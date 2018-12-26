package eu.sim642.adventofcode2018

import org.scalatest.{FunSuite, Suites}
import Day23._
import eu.sim642.adventofcode2017.Day20.Pos3
import eu.sim642.adventofcode2018.Day23Test.{CliquePart2SolutionTest, Part1Test, SplittingPart2SolutionTest}
import org.scalatest.prop.PropertyChecks

class Day23Test extends Suites(
  new Part1Test,
  new CliquePart2SolutionTest,
  new SplittingPart2SolutionTest,
)

object Day23Test {

  class Part1Test extends FunSuite {

    val exampleInput1 =
      """pos=<0,0,0>, r=4
        |pos=<1,0,0>, r=1
        |pos=<4,0,0>, r=3
        |pos=<0,2,0>, r=1
        |pos=<0,5,0>, r=3
        |pos=<0,0,3>, r=1
        |pos=<1,1,1>, r=1
        |pos=<1,1,2>, r=1
        |pos=<1,3,1>, r=1""".stripMargin

    test("Part 1 examples") {
      assert(nanobotsInLargestRadius(parseInput(exampleInput1)) == 7)
    }

    test("Part 1 input answer") {
      assert(nanobotsInLargestRadius(parseInput(input)) == 219)
    }
  }

  sealed abstract class Part2SolutionTest(part2Solution: Part2Solution) extends FunSuite with PropertyChecks {

    val exampleInput2 =
      """pos=<10,12,12>, r=2
        |pos=<12,14,12>, r=2
        |pos=<16,12,12>, r=4
        |pos=<14,14,14>, r=6
        |pos=<50,50,50>, r=200
        |pos=<10,10,10>, r=5""".stripMargin

    lazy val dataforceInput: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23dataforce.txt")).mkString.trim
    lazy val dataforceFriendInput: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23dataforcefriend.txt")).mkString.trim
    lazy val lamperiInput: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23lamperi.txt")).mkString.trim
    lazy val vikestepInput: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23vikestep.txt")).mkString.trim
    lazy val magneInput: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23magne.txt")).mkString.trim

    test("Part 2 examples") {
      assert(part2Solution.closestMostNanobots(parseInput(exampleInput2)) == 36)
    }

    test("Part 2 closest to origin (symmetric)") {
      val positions = Table(
        ("pos1", "pos2"),
        (Pos3(1, 3, 0), Pos3(-1, 3, 0)),
        (Pos3(1, -3, 0), Pos3(-1, -3, 0)),
        (Pos3(3, 1, 0), Pos3(3, -1, 0)),
        (Pos3(-3, 1, 0), Pos3(-3, -1, 0)),

        (Pos3(0, 3, 1), Pos3(0, 3, -1)),
        (Pos3(0, -3, 1), Pos3(0, -3, -1)),
        (Pos3(3, 0, 1), Pos3(3, 0, -1)),
        (Pos3(-3, 0, 1), Pos3(-3, 0, -1)),

        (Pos3(1, 0, 3), Pos3(-1, 0, 3)),
        (Pos3(1, 0, -3), Pos3(-1, 0, -3)),
        (Pos3(0, 1, 3), Pos3(0, -1, 3)),
        (Pos3(0, 1, -3), Pos3(0, -1, -3)),
      )

      forAll (positions) { (pos1, pos2) =>
        val nanobots = Seq(
          Nanobot(pos1, 2),
          Nanobot(pos2, 2)
        )

        assert(part2Solution.closestMostNanobots(nanobots) == 2)
      }
    }

    test("Part 2 closest to origin (asymmetric)") {
      val positions = Table(
        ("pos1", "pos2"),
        (Pos3(1, 4, 0), Pos3(-1, 3, 0)),
        (Pos3(1, 3, 0), Pos3(-1, 4, 0)),
        (Pos3(1, -4, 0), Pos3(-1, -3, 0)),
        (Pos3(1, -3, 0), Pos3(-1, -4, 0)),
        (Pos3(4, 1, 0), Pos3(3, -1, 0)),
        (Pos3(3, 1, 0), Pos3(4, -1, 0)),
        (Pos3(-4, 1, 0), Pos3(-3, -1, 0)),
        (Pos3(-3, 1, 0), Pos3(-4, -1, 0)),

        (Pos3(0, 4, 1), Pos3(0, 3, -1)),
        (Pos3(0, 3, 1), Pos3(0, 4, -1)),
        (Pos3(0, -4, 1), Pos3(0, -3, -1)),
        (Pos3(0, -3, 1), Pos3(0, -4, -1)),
        (Pos3(4, 0, 1), Pos3(3, 0, -1)),
        (Pos3(3, 0, 1), Pos3(4, 0, -1)),
        (Pos3(-4, 0, 1), Pos3(-3, 0, -1)),
        (Pos3(-3, 0, 1), Pos3(-4, 0, -1)),

        (Pos3(1, 0, 4), Pos3(-1, 0, 3)),
        (Pos3(1, 0, 3), Pos3(-1, 0, 4)),
        (Pos3(1, 0, -4), Pos3(-1, 0, -3)),
        (Pos3(1, 0, -3), Pos3(-1, 0, -4)),
        (Pos3(0, 1, 4), Pos3(0, -1, 3)),
        (Pos3(0, 1, 3), Pos3(0, -1, 4)),
        (Pos3(0, 1, -4), Pos3(0, -1, -3)),
        (Pos3(0, 1, -3), Pos3(0, -1, -4)),
      )

      forAll (positions) { (pos1, pos2) =>
        val nanobots = Seq(
          Nanobot(pos1, 2),
          Nanobot(pos2, 2)
        )

        assert(part2Solution.closestMostNanobots(nanobots) == 3)
      }
    }

    test("Part 2 input answer") {
      assert(part2Solution.closestMostNanobots(parseInput(input)) == 83779034)
    }

    test("Part 2 Dataforce") {
      assert(part2Solution.closestMostNanobots(parseInput(dataforceInput)) == 119011326)
    }

    test("Part 2 Dataforce friend") {
      assert(part2Solution.closestMostNanobots(parseInput(dataforceFriendInput)) == 88894457)
    }

    test("Part 2 Lamperi") {
      assert(part2Solution.closestMostNanobots(parseInput(lamperiInput)) == 107272899) // unconfirmed answer
    }

    test("Part 2 VikeStep") {
      assert(part2Solution.closestMostNanobots(parseInput(vikestepInput)) == 93750870)
    }

    test("Part 2 Magne") {
      assert(part2Solution.closestMostNanobots(parseInput(magneInput)) == 71484642)
    }
  }

  class CliquePart2SolutionTest extends Part2SolutionTest(CliquePart2Solution)

  class SplittingPart2SolutionTest extends Part2SolutionTest(SplittingPart2Solution)
}