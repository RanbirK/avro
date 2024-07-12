/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.davidmc24.gradle.plugin.avro

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class KotlinDSLCompatibilityFunctionalSpec extends FunctionalSpec {
    File kotlinBuildFile

    def "setup"() {
        buildFile.delete() // Don't use the Groovy build file created by the superclass
        kotlinBuildFile = projectFile("build.gradle.kts")
        kotlinBuildFile << """
        |plugins {
        |    java
        |    id("com.github.davidmc24.gradle.plugin.avro")
        |}
        |repositories {
        |    mavenCentral()
        |}
        |dependencies {
        |    implementation("org.apache.avro:avro:${avroVersion}")
        |}
        |""".stripMargin()
    }

    def "works with kotlin DSL"() {
        given:
        copyResource("user.avsc", avroDir)

        when:
        def result = run()

        then:
        result.task(":generateAvroJava").outcome == SUCCESS
        result.task(":compileJava").outcome == SUCCESS
        projectFile(buildOutputClassPath("example/avro/User.class")).file
    }

    def "extension supports configuring all supported properties"() {
        given:
        copyResource("user.avsc", avroDir)
        kotlinBuildFile << """
        |avro {
        |    isCreateSetters.set(true)
        |    isCreateOptionalGetters.set(false)
        |    isGettersReturnOptional.set(false)
        |    isOptionalGettersForNullableFieldsOnly.set(false)
        |    fieldVisibility.set("PUBLIC")
        |    outputCharacterEncoding.set("UTF-8")
        |    stringType.set("String")
        |    templateDirectory.set(null as String?)
        |    isEnableDecimalLogicalType.set(true)
        |}
        |""".stripMargin()

        when:
        def result = run()

        then:
        result.task(":generateAvroJava").outcome == SUCCESS
        result.task(":compileJava").outcome == SUCCESS
        projectFile(buildOutputClassPath("example/avro/User.class")).file
    }
}
