package eu.sim642.adventofcodelib

import eu.sim642.adventofcodelib.IntegralImplicits._

import scala.collection.BuildFrom
import scala.collection.generic.IsSeq
import scala.language.implicitConversions

object SeqImplicits {

  class RotateSeqOps[Repr, S <: IsSeq[Repr]](coll: Repr, seq: S) {
    def rotateLeft[That](n: Int)(implicit bf: BuildFrom[Repr, seq.A, That]): That = {
      val seqOps = seq(coll)
      val realN = n %+ seqOps.length
      val (init, tail) = seqOps.view.splitAt(realN)
      bf.fromSpecific(coll)(tail ++ init)
    }

    def rotateRight[That](n: Int)(implicit bf: BuildFrom[Repr, seq.A, That]): That =
      rotateLeft(-n)
  }

  // alternate more limited view-less implementation by OlegYch|h on freenode#scala
  /*class RotateOps[Repr, S <: IsSeq[Repr]](coll: Repr, seq: S) {
    def rotateLeft[That](n: Int)(implicit bf: BuildFrom[Repr, seq.A, That], seqIsRepr: seq.C =:= Repr): That = {
      val seqOps = seq(coll)
      val realN = n %+ seqOps.length
      val (init, tail) = seqOps.splitAt(realN)
      bf.fromSpecific(coll)(seq(tail) ++ seq(init))
    }

     def rotateRight[That](n: Int)(implicit bf: BuildFrom[Repr, seq.A, That], seqIsRepr: seq.C =:= Repr): That =
       rotateLeft(-n)
  }*/

  implicit def RotateSeqOps[Repr](coll: Repr)(implicit seq: IsSeq[Repr]): RotateSeqOps[Repr, seq.type] =
    new RotateSeqOps(coll, seq)
}
