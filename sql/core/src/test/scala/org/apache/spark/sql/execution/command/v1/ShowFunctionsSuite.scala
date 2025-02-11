/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.command.v1

import java.util.Locale

import org.apache.spark.sql.execution.command

/**
 * This base suite contains unified tests for the `SHOW FUNCTIONS` command that checks V1
 * table catalogs. The tests that cannot run for all V1 catalogs are located in more
 * specific test suites:
 *
 *   - Temporary functions of V1 catalog:
 *     `org.apache.spark.sql.execution.command.v1.ShowTempFunctionsSuite`
 *   - Permanent functions of V1 catalog:
 *     `org.apache.spark.sql.hive.execution.command.ShowFunctionsSuite`
 */
trait ShowFunctionsSuiteBase extends command.ShowFunctionsSuiteBase
  with command.TestsV1AndV2Commands

/**
 * The class contains tests for the `SHOW FUNCTIONS` command to check temporary functions.
 */
class ShowTempFunctionsSuite extends ShowFunctionsSuiteBase with CommandSuiteBase {
  override def commandVersion: String = super[ShowFunctionsSuiteBase].commandVersion
  override protected def isTempFunctions(): Boolean = true

  override protected def createFunction(name: String): Unit = {
    spark.udf.register(name, (arg1: Int, arg2: String) => arg2 + arg1)
  }

  override protected def dropFunction(name: String): Unit = {
    spark.sessionState.catalog.dropTempFunction(name, false)
  }

  override protected def showFun(ns: String, name: String): String = {
    s"$catalog.$ns.$name".toLowerCase(Locale.ROOT)
  }
}
