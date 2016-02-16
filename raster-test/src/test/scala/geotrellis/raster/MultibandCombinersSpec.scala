package geotrellis.raster

import geotrellis.raster.testkit._

import org.scalatest._

class MultibandCombinersSpec extends FunSuite with RasterMatchers with Matchers {
  val tile       = IntConstantTile(99, 3, 3)

  private def mkMultiBandTile(arity: Int) = ArrayMultiBandTile((0 to arity) map (_ => tile))

  private def combineAssert(combined: Tile, arity: Int) = {
    val expected = IntConstantTile(99 * arity, 3, 3)
    assert(combined.toArray === expected.toArray)
  }

  test("Multiband combine function test: arity 2") {
    val arity = 2
    val combined = mkMultiBandTile(arity).combine(0, 1) { case (b0, b1) => b0 + b1 }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 3") {
    val arity = 3
    val combined = mkMultiBandTile(arity).combine(0, 1, 2) { case (b0, b1, b2) => b0 + b1 + b2 }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 4") {
    val arity = 4
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3) {
      case (b0, b1, b2, b3) => b0 + b1 + b2 + b3
    }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 5") {
    val arity = 5
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3, 4) {
      case (b0, b1, b2, b3, b4) => b0 + b1 + b2 + b3 + b4
    }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 6") {
    val arity = 6
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3, 4, 5) {
      case (b0, b1, b2, b3, b4, b5) => b0 + b1 + b2 + b3 + b4 + b5
    }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 7") {
    val arity = 7
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3, 4, 5, 6) {
      case (b0, b1, b2, b3, b4, b5, b6) => b0 + b1 + b2 + b3 + b4 + b5 + b6
    }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 8") {
    val arity = 8
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3, 4, 5, 6, 7) {
      case (b0, b1, b2, b3, b4, b5, b6, b7) => b0 + b1 + b2 + b3 + b4 + b5 + b6 + b7
    }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 9") {
    val arity = 9
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3, 4, 5, 6, 7, 8) {
      case (b0, b1, b2, b3, b4, b5, b6, b7, b8) => b0 + b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8
    }
    combineAssert(combined, arity)
  }

  test("Multiband combine function test: arity 10") {
    val arity = 10
    val combined = mkMultiBandTile(arity).combine(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) {
      case (b0, b1, b2, b3, b4, b5, b6, b7, b8, b9) => b0 + b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9
    }
    combineAssert(combined, arity)
  }
}
