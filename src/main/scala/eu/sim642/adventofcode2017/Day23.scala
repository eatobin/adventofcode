package eu.sim642.adventofcode2017

import scala.util.matching.Regex

object Day23 {

  type Register = Char
  type Integer = Long

  def isPrime(n: Int): Boolean = n >= 2 && (2 to Math.sqrt(n).toInt).forall(n % _ != 0)

  trait Solution {
    def countMul(input: String): Int
    def registerH(input: String): Integer
  }

  object SimulateSolution extends Solution {

    sealed trait Value
    case class RegisterValue(register: Register) extends Value
    case class ConstValue(const: Integer) extends Value

    sealed trait Instruction
    case class Set(x: Register, y: Value) extends Instruction
    case class Sub(x: Register, y: Value) extends Instruction
    case class Mul(x: Register, y: Value) extends Instruction
    case class Jnz(x: Value, y: Value) extends Instruction

    type Instructions = Vector[Instruction]
    type Registers = Map[Register, Integer]

    case class AsmState(instructions: Instructions,
                        pc: Int = 0,
                        registers: Registers = Map.empty.withDefaultValue(0)) {
      def get(value: Value): Integer = value match {
        case RegisterValue(register) => registers(register)
        case ConstValue(const) => const
      }

      def instruction: Instruction = instructions(pc)

      def terminated: Boolean = !instructions.indices.contains(pc)
    }

    private val instructionRegex = """([a-z]+) ([a-z]|-?\d+)(?: ([a-z]|-?\d+))?""".r

    def parseValue(str: String): Value = {
      val c = str.head
      if (c.isLetter)
        RegisterValue(c)
      else
        ConstValue(str.toLong)
    }

    def parseInstruction(str: String): Instruction = str match {
      case instructionRegex(instruction, x, y) => instruction match {
        case "set" => Set(x.head, parseValue(y))
        case "sub" => Sub(x.head, parseValue(y))
        case "mul" => Mul(x.head, parseValue(y))
        case "jnz" => Jnz(parseValue(x), parseValue(y))
      }
    }

    def parseInstructions(str: String): Instructions = str.lines.map(parseInstruction).toVector

    def execSmallStep(state: AsmState): AsmState = {
      val AsmState(instructions, pc, registers) = state

      if (state.terminated)
        state

      state.instruction match {
        case Set(x, y) => state.copy(registers = registers + (x -> state.get(y)), pc = pc + 1)
        case Sub(x, y) => state.copy(registers = registers + (x -> (registers(x) - state.get(y))), pc = pc + 1)
        case Mul(x, y) => state.copy(registers = registers + (x -> (registers(x) * state.get(y))), pc = pc + 1)
        case Jnz(x, y) =>
          if (state.get(x) != 0)
            state.copy(pc = pc + state.get(y).toInt)
          else
            state.copy(pc = pc + 1)
      }
    }

    def iterateSmallStep(instructions: Instructions): Iterator[AsmState] = Iterator.iterate(AsmState(instructions))(execSmallStep)

    def countMul(instructions: Instructions): Int = iterateSmallStep(instructions).takeWhile(!_.terminated).count(state => state.instruction match {
      case _: Mul => true
      case _ => false
    })

    override def countMul(input: String): Int = countMul(parseInstructions(input))

    override def registerH(input: String): Integer = ???
  }

  object ReverseEngineeredSolution extends Solution {

    lazy val inputRegex: Regex = io.Source.fromInputStream(getClass.getResourceAsStream("day23/reverse_regex.txt")).mkString.trim.r

    case class RangeParams(b: Int, bMul: Int, bAdd: Int, cAdd: Int, bInc: Int) {
      def bRange: Range = {
        val b2 = b * bMul + bAdd
        val c2 = b2 + cAdd
        b2 to c2 by bInc
      }
    }

    def parseParams(input: String): RangeParams = {
      val m = inputRegex.findFirstMatchIn(input).get
      RangeParams(
        b = m.group("b").toInt,
        bMul = m.group("bMul").toInt,
        bAdd = m.group("bAdd").toInt,
        cAdd = m.group("cAdd").toInt,
        bInc = m.group("bInc").toInt
      )
    }

    override def countMul(input: String): Int = {
      val params: RangeParams = parseParams(input)
      (params.b - 2) * (params.b - 2)
    }

    override def registerH(input: String): Integer = {
      val params: RangeParams = parseParams(input)
      params.bRange.count(!isPrime(_))
    }
  }

  lazy val input: String = io.Source.fromInputStream(getClass.getResourceAsStream("day23.txt")).mkString.trim

  def main(args: Array[String]): Unit = {
    import ReverseEngineeredSolution._

    println(countMul(input))
    println(registerH(input))
  }
}