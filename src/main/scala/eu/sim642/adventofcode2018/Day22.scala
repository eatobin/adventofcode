package eu.sim642.adventofcode2018

import eu.sim642.adventofcode2017.Day19.Grid
import eu.sim642.adventofcode2017.Day3.Pos
import eu.sim642.adventofcode2017.Day21.GridOps
import eu.sim642.adventofcode2017.Day14.PosGrid
import eu.sim642.adventofcode2017.Day19.PosGrid2

import scala.collection.mutable

object Day22 {

  sealed trait CaveType {
    def riskLevel: Int
    def allowedTools: Set[Tool]
  }
  case object Rocky extends CaveType {
    override def riskLevel: Int = 0
    override def allowedTools: Set[Tool] = Set(ClimbingGear, Torch)
  }
  case object Wet extends CaveType {
    override def riskLevel: Int = 1
    override def allowedTools: Set[Tool] = Set(ClimbingGear, Neither)
  }
  case object Narrow extends CaveType {
    override def riskLevel: Int = 2
    override def allowedTools: Set[Tool] = Set(Torch, Neither)
  }

  sealed trait Tool
  case object Torch extends Tool
  case object ClimbingGear extends Tool
  case object Neither extends Tool

  def calculateErosionLevel(depth: Int, target: Pos, max: Pos): Grid[Int] = {
    val erosionLevel = mutable.Seq.fill(max.y + 1, max.x + 1)(-1)
    for (y <- 0 to max.y) {
      for (x <- 0 to max.x) {
        val geologicIndex = (y, x) match {
          case (0, 0) => 0
          case (y, x) if x == target.x && y == target.y => 0
          case (0, x) => x * 16807
          case (y, 0) => y * 48271
          case (y, x) => erosionLevel(y)(x - 1) * erosionLevel(y - 1)(x)
        }

        erosionLevel(y)(x) = (geologicIndex + depth) % 20183
      }
    }
    erosionLevel.toVector.map(_.toVector)
  }

  def calculateCaveType(depth: Int, target: Pos, max: Pos): Grid[CaveType] = {
    val erosionLevel = calculateErosionLevel(depth, target, max)
    erosionLevel.mapGrid({ _ % 3 match {
      case 0 => Rocky
      case 1 => Wet
      case 2 => Narrow
    }})
  }

  def totalRiskLevel(depth: Int, target: Pos): Int = {
    val caveType = calculateCaveType(depth, target, target)
    val riskLevel: Grid[Int] = caveType.mapGrid(_.riskLevel)
    riskLevel.map(_.sum).sum
  }

  def totalRiskLevel(input: String): Int = {
    val (depth, target) = parseInput(input)
    totalRiskLevel(depth, target)
  }

  def fastestToTarget(depth: Int, target: Pos): Int = {
    val caveType = calculateCaveType(depth, target, Pos(target.x * 5, target.y * 5))

    type PosTool = (Pos, Tool)

    val visited: mutable.Set[PosTool] = mutable.Set.empty
    val distance: mutable.Map[PosTool, Int] = mutable.Map.empty
    val toVisit: mutable.PriorityQueue[(Int, PosTool)] = mutable.PriorityQueue.empty(Ordering.by(-_._1))

    val startPosTool = (Pos(0, 0), Torch)
    distance(startPosTool) = 0
    toVisit.enqueue((0, startPosTool))

    val targetPosTool = (target, Torch)

    while (toVisit.nonEmpty) {
      val (dist, posTool@(pos, tool)) = toVisit.dequeue()
      if (!visited.contains(posTool)) {
        visited += posTool
        distance(posTool) = dist

        def goNeighbor(newPosTool: PosTool, distDelta: Int): Unit = {
          val newDist = dist + distDelta
          toVisit.enqueue((newDist, newPosTool))
        }

        for {
          offset <- Pos.axisOffsets
          newPos = pos + offset
          if caveType.containsPos(newPos)
          if caveType(newPos).allowedTools.contains(tool)
        } goNeighbor((newPos, tool), 1)

        for {
          newTool <- caveType(pos).allowedTools - tool
        } goNeighbor((pos, newTool), 7)
      }
    }

    distance(targetPosTool)
  }

  def fastestToTarget(input: String): Int = {
    val (depth, target) = parseInput(input)
    fastestToTarget(depth, target)
  }

  private val inputRegex =
    """depth: (\d+)
      |target: (\d+),(\d+)""".stripMargin.r

  def parseInput(input: String): (Int, Pos) = input match {
    case inputRegex(depth, targetX, targetY) => (depth.toInt, Pos(targetX.toInt, targetY.toInt))
  }

  lazy val input: String = io.Source.fromInputStream(getClass.getResourceAsStream("day22.txt")).mkString.trim

  def main(args: Array[String]): Unit = {
    println(totalRiskLevel(input))
    println(fastestToTarget(input))

    // part 2: 977 - too high
  }
}