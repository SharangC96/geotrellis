/*
 * Copyright 2016 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.raster.viewshed

import geotrellis.raster._
import geotrellis.raster.testkit._
import org.scalatest._

/**
 * Created by jchien on 5/1/14.
 */
class ApproxViewshedSpec extends FunSpec
                            with Matchers
                            with RasterMatchers with TestFiles
                            with TileBuilders {
  describe("Viewshed") {
    it("computes the viewshed of a flat int plane") {
      val r = createTile(Array.fill(7 * 8)(1), 7, 8)
      assertEqual(BitConstantTile(true, 7, 8), ApproxViewshed(r, 4, 3))
    }

    it("computes the viewshed of a flat double plane") {
      val r = createTile(Array.fill(7 * 8)(1.5), 7, 8)
      assertEqual(BitConstantTile(true, 7, 8), ApproxViewshed(r, 4, 3))
    }

    it("computes the viewshed of a double line") {
      val rasterData = Array (
        300.0, 1.0, 99.0, 0.0, 10.0, 200.0, 137.0
      )
      val viewable = Array (
        1, 0, 1, 1, 1, 1, 0
      )
      val r = createTile(rasterData, 7, 1)
      val viewRaster = createTile(viewable, 7, 1)
      assertEqual(viewRaster.localEqual(createTile(Array.fill(7 * 1)(1), 7, 1)), ApproxViewshed(r, 3, 0))
    }

    it("computes the viewshed of a double plane") {
      val rasterData = Array (
        999.0, 1.0,   1.0,   1.0,   1.0,   1.0,   999.0,
        1.0,   1.0,   1.0,   1.0,   1.0,   499.0, 1.0,
        1.0,   1.0,   1.0,   1.0,   99.0,  1.0,   1.0,
        1.0,   1.0,   999.0, 1.0,   1.0,   1.0,   1.0,
        1.0,   1.0,   1.0,   1.0,   100.0, 1.0,   1.0,
        1.0,   1.0,   1.0,   1.0,   1.0,   101.0, 1.0,
        1.0,   1.0,   1.0,   1.0,   1.0,   1.0,   102.0
      )
      val viewable = Array (
        1,     1,     1,     1,     0,     0,     1,
        0,     1,     1,     1,     0,     1,     0,
        0,     0,     1,     1,     1,     0,     0,
        0,     0,     1,     1,     1,     1,     1,
        0,     0,     1,     1,     1,     0,     0,
        0,     1,     1,     1,     0,     0,     0,
        1,     1,     1,     1,     0,     0,     0
      )
      val r = createTile(rasterData, 7, 7)
      val viewRaster = createTile(viewable, 7, 7)
      assertEqual(viewRaster.localEqual(createTile(Array.fill(7 * 7)(1), 7, 7)), ApproxViewshed(r, 3, 3))
    }

    it("computes the approx viewshed of a int plane") {
      val rasterData = Array (
        999, 1,   1,   1,   1,   499, 999,
        1,   1,   1,   1,   1,   499, 250,
        1,   1,   1,   1,   99,  1,   1,
        1,   999, 1,   1,   1,   1,   1,
        1,   1,   1,   1,   1,   1,   1,
        1,   1,   1,   0,   1,   1,   1,
        1,   1,   1,   1,   1,   1,   1
      )
      val viewable = Array (
        1,     1,     1,     1,     0,     1,     1,
        1,     1,     1,     1,     0,     1,     0, // this cell will be incorrectly computed due to the algorithm
        0,     1,     1,     1,     1,     0,     0,
        0,     1,     1,     1,     1,     1,     1,
        0,     1,     1,     1,     1,     1,     1,
        1,     1,     1,     0,     1,     1,     1,
        1,     1,     1,     1,     1,     1,     1
      )
      val r = createTile(rasterData, 7, 7)
      val viewRaster = createTile(viewable, 7, 7)
      assertEqual(viewRaster.localEqual(createTile(Array.fill(7 * 7)(1), 7, 7)), ApproxViewshed(r, 3, 3))
    }

    it("should match viewshed generated by ArgGIS") { // discrepancy of 6.7% with ESRI, 6.6% with exact algo
      val elevation = loadTestArg("data/viewshed-elevation")
      val re = elevation.rasterExtent
      val expected = loadTestArg("data/viewshed-expected")

      val (x, y) = (-93.63300872055451407, 30.54649743277299123) // create overload
      val (col, row) = re.mapToGrid(x, y)
      val actual = ApproxViewshed(elevation, col, row)

      def countDiff(a: Tile, b: Tile): Int = {
        var ans = 0
        for(col <- 0 until 256) {
          for(row <- 0 until 256) {
            if (a.get(col, row) != b.get(col, row)) ans += 1;
          }
        }
        ans
      }
    }
  }
}
