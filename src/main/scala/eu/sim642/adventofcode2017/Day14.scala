package eu.sim642.adventofcode2017

import Day3.Pos

object Day14 {

  def hashRow(key: String, row: Int): Seq[Byte] = Day10.knotHash(s"$key-$row").map(_.toByte)

  def bytes2bits(bytes: Seq[Byte]): Vector[Boolean] =
    bytes.flatMap(byte => (0 until 8).foldLeft(Vector.empty[Boolean])((acc, i) => (((byte >> i) & 1) != 0) +: acc)).toVector

  def hashGrid(key: String): Vector[Vector[Boolean]] = {
    (0 until 128).map(row => bytes2bits(hashRow(key, row))).toVector
  }

  def squaresUsed(key: String): Int = {
    val rows = hashGrid(key)
    rows.map(_.count(x => x)).sum
  }

  implicit class PosGrid[A](grid: Vector[Vector[A]]) {
    def apply(pos: Pos): A = grid(pos.y)(pos.x)
  }

  // copied from Day 12
  def bfs(poss: Set[Pos], startPos: Pos): Set[Pos] = {

    def helper(visited: Set[Pos], toVisit: Set[Pos]): Set[Pos] = {
      val neighbors = toVisit.flatMap(pos => Pos.axisOffsets.map(offset => pos + offset).filter(poss))
      val newVisited = visited ++ toVisit
      val newToVisit = neighbors -- visited
      if (newToVisit.isEmpty)
        newVisited
      else
        helper(newVisited, newToVisit)
    }

    helper(Set.empty, Set(startPos))
  }

  def bfsGroups(poss: Set[Pos]): Set[Set[Pos]] = {
    if (poss.isEmpty)
      Set.empty
    else {
      val startPos = poss.head // take any node
      val group = bfs(poss, startPos) // find its component
      val restPoss = poss -- group
      bfsGroups(restPoss) + group
    }
  }

  def regionsCount(key: String): Int = {
    val grid = hashGrid(key)

    val poss: Set[Pos] = (for {
      i <- grid.indices
      j <- grid(i).indices
      pos = Pos(j, i)
      if grid(pos)
    } yield pos).toSet

    bfsGroups(poss).size
  }

  lazy val input: String = io.Source.fromInputStream(getClass.getResourceAsStream("day14.txt")).mkString.trim

  def main(args: Array[String]): Unit = {
    println(squaresUsed(input))
    println(regionsCount(input))
  }
}