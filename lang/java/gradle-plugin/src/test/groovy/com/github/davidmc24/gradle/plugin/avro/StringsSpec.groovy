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

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject(Strings)
class StringsSpec extends Specification {
    @Unroll
    def "isEmpty(#str)"() {
        when:
        def actual = Strings.isEmpty(str)
        then:
        actual == expected
        where:
        str   | expected
        null  | true
        ""    | true
        " "   | false
        "abc" | false
    }

    @Unroll
    def "isNotEmpty(#str)"() {
        when:
        def actual = Strings.isNotEmpty(str)
        then:
        actual == expected
        where:
        str   | expected
        null  | false
        ""    | false
        " "   | true
        "abc" | true
    }

    @Unroll
    def "when not empty, requireNotEmpty returns argument (#str)"() {
        def message = "testMessage"
        expect:
        Strings.requireNotEmpty(str, message) == str
        where:
        str << [" ", "abc"]
    }

    @Unroll
    def "when empty, requireNotEmpty throws exception (#str)"() {
        def message = "testMessage"
        when:
        Strings.requireNotEmpty(str, message)
        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == message
        where:
        str << [null, ""]
    }
}
