object Prime {
  val odd: Stream[Int] = Stream.from(1, 2)
  def isPrime(n: Int): Boolean = primes.takeWhile(_ <= math.sqrt(n)).forall(n % _ != 0)
  val primes: Stream[Int] = 2 #:: odd.tail.filter(isPrime)

  private def sieve(s: Stream[Int]): Stream[Int] = s.head #:: sieve(s.tail.filter(_ % s.head != 0))
  val primesSieve: Stream[Int] = 2 #:: sieve(odd.tail)
}

object PrimePairSet {
  import scala.collection.mutable.{HashSet => MutableSet}

  def canBePair(p1: Int, p2: Int): Boolean = {
    Prime.isPrime(s"$p1$p2".toInt) && Prime.isPrime(s"$p2$p1".toInt)
  }

  def canBePairs(p: Int, pair: List[Int]): Boolean = {
    pair.forall(canBePair(p, _))
  }
  def apply(): List[Int] = {
    val pairs: List[MutableSet[List[Int]]] = List(
      MutableSet[List[Int]](),
      MutableSet[List[Int]](),
      MutableSet[List[Int]](),
      MutableSet[List[Int]](),
      MutableSet[List[Int]]()
    )

    Prime.primes.takeWhile(_ => pairs.last.isEmpty).foreach { prime =>
      println(prime) // debug
      val upgradings: List[Set[List[Int]]] = pairs.dropRight(1).map { level =>
        level.filter(canBePairs(prime, _)).map(prime +: _).toSet
      }

      pairs.tail.zip(upgradings).foreach(tuple => tuple._1 ++= tuple._2)
      pairs.head.add(List(prime))
      println(pairs.map(_.size)) //debug
    }

    pairs.last.head
  }
}
