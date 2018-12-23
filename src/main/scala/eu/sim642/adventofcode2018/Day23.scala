package eu.sim642.adventofcode2018

import eu.sim642.adventofcode2017.Day20.Pos3

import scala.collection.mutable
import scala.util.control.Breaks._

object Day23 {

  case class Nanobot(pos: Pos3, radius: Int) {
    def overlaps(that: Nanobot): Boolean = (this.pos manhattanDistance that.pos) <= this.radius + that.radius

    def contains(cPos: Pos3): Boolean = (pos manhattanDistance cPos) <= radius

    def corners: Set[Pos3] = {
      Set(
        Pos3(-radius, 0, 0),
        Pos3(radius, 0, 0),
        Pos3(0, -radius, 0),
        Pos3(0, radius, 0),
        Pos3(0, 0, -radius),
        Pos3(0, 0, radius),
      ).map(pos + _)
    }

    def contains(that: Nanobot): Boolean = that.corners.forall(contains)
  }

  def nanobotsInLargestRadius(nanobots: Seq[Nanobot]): Int = {
    val largestRadius = nanobots.maxBy(_.radius)
    nanobots.count(nanobot => largestRadius.contains(nanobot.pos))
  }

  trait Part2Solution {
    def closestMostNanobots(nanobots: Seq[Nanobot]): Int
  }

  object CliquePart2Solution extends Part2Solution {
    def maximumClique(neighbors: Map[Nanobot, Set[Nanobot]]): Set[Nanobot] = {
      var best: Set[Nanobot] = Set.empty

      def bronKerbosh(r: Set[Nanobot], p: Set[Nanobot], x: Set[Nanobot]): Unit = {
        if (p.isEmpty && x.isEmpty) {
          //println(r)
          if (r.size > best.size)
            best = r
        }
        else {
          //val u = p.headOption.getOrElse(x.head)
          val u = (p ++ x).maxBy(neighbors(_).size) // pivot on highest degree
          var p2 = p
          var x2 = x
          for (v <- p -- neighbors(u)) {
            bronKerbosh(r + v, p2 intersect neighbors(v), x2 intersect neighbors(v))
            p2 -= v
            x2 += v
          }
        }
      }

      bronKerbosh(Set.empty, neighbors.keySet, Set.empty)
      best
    }

    def closestMostNanobots(nanobots: Seq[Nanobot]): Int = {
      val neighbors: Map[Nanobot, Set[Nanobot]] = nanobots.map(nanobot1 => nanobot1 -> nanobots.filter(nanobot2 => nanobot2 != nanobot1 && nanobot1.overlaps(nanobot2)).toSet).toMap

      val maximumOverlap = maximumClique(neighbors)
      //println(maximumOverlap)
      maximumOverlap.map(n => (n.pos manhattanDistance Pos3(0, 0, 0)) - n.radius).max
    }
  }

  object SplittingPart2Solution extends Part2Solution {
    def getInitialOctahedron(nanobots: Seq[Nanobot]): Nanobot = {
      //val initPos = Pos3(0, 0, 0)
      val poss = nanobots.map(_.pos)
      val initX = (poss.map(_.x).min + poss.map(_.x).max) / 2
      val initY = (poss.map(_.y).min + poss.map(_.y).max) / 2
      val initZ = (poss.map(_.z).min + poss.map(_.z).max) / 2
      val initPos = Pos3(initX, initY, initZ)
      Iterator.iterate(1)(_ * 3).map(Nanobot(initPos, _)).find(octahedron => nanobots.forall(octahedron.contains)).get
    }

    def getBounds(nanobots: Seq[Nanobot], octahedron: Nanobot): (Int, Int) = {
      val lower = nanobots.count(_.contains(octahedron))
      val upper = nanobots.count(_.overlaps(octahedron))
      (upper, lower)
    }

    def getSplits(octahedron: Nanobot): Set[Nanobot] = {
      val Nanobot(pos, radius) = octahedron
      val r2 = (1.0 / 3 * radius).ceil.toInt
      Set(
        Pos3(-r2, 0, 0),
        Pos3(r2, 0, 0),
        Pos3(0, -r2, 0),
        Pos3(0, r2, 0),
        Pos3(0, 0, -r2),
        Pos3(0, 0, r2),
        Pos3(0, 0, 0),
      ).map(offset => Nanobot(pos + offset, (2.0 / 3 * radius).floor.toInt))
    }

    def closestMostNanobots(nanobots: Seq[Nanobot]): Int = {
      val queue: mutable.PriorityQueue[((Int, Int), Nanobot)] = mutable.PriorityQueue.empty(Ordering.by(_._1))
      val done: mutable.Set[Nanobot] = mutable.Set.empty

      def enqueue(octahedron: Nanobot): Unit = {
        queue.enqueue((getBounds(nanobots, octahedron), octahedron))
      }

      val initialOctahedron = getInitialOctahedron(nanobots)
      enqueue(initialOctahedron)

      breakable {
        while (queue.nonEmpty) {
          val (_, octahedron) = queue.dequeue()
          if (!done.contains(octahedron)) {
            done += octahedron

            val (upper, lower) = getBounds(nanobots, octahedron)
            if (lower == upper) {
              return (octahedron.pos manhattanDistance Pos3(0, 0, 0)) - octahedron.radius
            }

            for (splitOctahedron <- getSplits(octahedron))
              enqueue(splitOctahedron)
          }
        }
      }
      ???
    }
  }


  private val nanobotRegex = """pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(\d+)""".r

  def parseNanobot(s: String): Nanobot = s match {
    case nanobotRegex(x, y, z, r) => Nanobot(Pos3(x.toInt, y.toInt, z.toInt), r.toInt)
  }

  def parseInput(input: String): Seq[Nanobot] = input.lines.map(parseNanobot).toSeq

  lazy val input: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23.txt")).mkString.trim

  def main(args: Array[String]): Unit = {
    import CliquePart2Solution._
    println(nanobotsInLargestRadius(parseInput(input)))
    println(closestMostNanobots(parseInput(input)))

    // 90702904 - too high
  }
}
