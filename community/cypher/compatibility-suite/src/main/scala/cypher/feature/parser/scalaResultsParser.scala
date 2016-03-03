/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cypher.feature.parser

import cucumber.api.DataTable
import cypher.feature.parser.matchers.{ResultMatcher, RowMatcher, ValueMatcher}

import scala.collection.JavaConverters._

object matcherParser extends (String => ValueMatcher) {

  private def parser = new ResultsParser
  private val listener = new CypherMatchersCreator

  def apply(input: String): ValueMatcher = {
    parser.matcherParse(input, listener)
  }
}

object constructResultMatcher extends (DataTable => ResultMatcher) {

  override def apply(table: DataTable): ResultMatcher = {
    val keys = table.topCells().asScala
    val cells = table.cells(1).asScala

    new ResultMatcher(cells.map { list =>
      new RowMatcher(list.asScala.zipWithIndex.map { case (value, index) =>
        (keys(index), matcherParser(value))
      }.toMap.asJava)
    }.toList.asJava)
  }
}
