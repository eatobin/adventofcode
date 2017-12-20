package eu.sim642.adventofcode2017

object Day20 {

  case class Pos3(x: Int, y: Int, z: Int) {
    def manhattanDistance(that: Pos3): Int =
      (x - that.x).abs + (y - that.y).abs + (z - that.z).abs

    def +(that: Pos3): Pos3 =
      Pos3(x + that.x, y + that.y, z + that.z)
  }

  private val particleRegex = """p=<\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)>, v=<\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)>, a=<\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)>""".r

  case class Particle(p: Pos3, v: Pos3, a: Pos3) {
    def updated: Particle = {
      val v2 = v + a
      Particle(p + v2, v2, a)
    }
  }

  def parseParticle(str: String): Particle = str match {
    case particleRegex(px, py, pz, vx, vy, vz, ax, ay, az) =>
      Particle(Pos3(px.toInt, py.toInt, pz.toInt), Pos3(vx.toInt, vy.toInt, vz.toInt), Pos3(ax.toInt, ay.toInt, az.toInt))
  }

  def parseParticles(input: String): Seq[Particle] = input.lines.map(parseParticle).toSeq

  def staysClosest(particles: Seq[Particle]): Int = {
    val endParticles = (0 until 1000).foldLeft(particles.toVector)({ case (acc, _) => acc.map(_.updated) })
    endParticles.zipWithIndex.minBy({ case (p, i) => p.p manhattanDistance Pos3(0, 0, 0) })._2
  }

  def staysClosest(input: String): Int = staysClosest(parseParticles(input))

  lazy val input: String = io.Source.fromInputStream(getClass.getResourceAsStream("day20.txt")).mkString.trim

  def main(args: Array[String]): Unit = {
    println(staysClosest(input)) // 119 too low

    parseParticles(input).zipWithIndex.sortBy(_._1.a manhattanDistance Pos3(0, 0, 0)).foreach(println)
  }
}
