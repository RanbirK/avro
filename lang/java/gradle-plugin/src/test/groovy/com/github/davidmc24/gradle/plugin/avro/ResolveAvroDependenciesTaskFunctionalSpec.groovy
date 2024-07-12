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

import org.hamcrest.MatcherAssert
import spock.lang.Subject
import uk.co.datumedge.hamcrest.json.SameJSONAs

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

@Subject(ResolveAvroDependenciesTask)
class ResolveAvroDependenciesTaskFunctionalSpec extends FunctionalSpec {
    def "resolves dependencies"() {
        def srcDir = projectFolder("src/avro/normalized")

        given: "a build with the task declared"
        applyAvroBasePlugin()
        buildFile << """
        |tasks.register("resolveAvroDependencies", com.github.davidmc24.gradle.plugin.avro.ResolveAvroDependenciesTask) {
        |    source file("src/avro/normalized")
        |    outputDir = file("build/avro/resolved")
        |}
        |""".stripMargin()

        and: "some normalized schema files"
        copyResource("/examples/separate/Breed.avsc", srcDir)
        copyResource("/examples/separate/Cat.avsc", srcDir)

        when: "running the task"
        def result = run("resolveAvroDependencies")

        then: "the resolved schema files are generated"
        result.task(":resolveAvroDependencies").outcome == SUCCESS
        MatcherAssert.assertThat(
            projectFile("build/avro/resolved/example/Cat.avsc").text,
            SameJSONAs.sameJSONAs(getClass().getResourceAsStream("/examples/inline/Cat.avsc").text))
        MatcherAssert.assertThat(
            projectFile("build/avro/resolved/example/Breed.avsc").text,
            SameJSONAs.sameJSONAs(getClass().getResourceAsStream("/examples/separate/Breed.avsc").text))
    }
}
