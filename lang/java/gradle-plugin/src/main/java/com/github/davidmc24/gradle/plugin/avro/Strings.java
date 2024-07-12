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

package com.github.davidmc24.gradle.plugin.avro;

/**
 * Utility methods for working with {@link String}s.
 */
class Strings {
    /**
     * Not intended for instantiation.
     */
    private Strings() { }

    /**
     * Checks if a {@link String} is empty ({@code ""}) or {@code null}.
     *
     * @param str the String to check, may be {@code null}
     * @return true if the String is empty or {@code null}
     */
    static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a {@link String} is not empty ({@code ""}) and not {@code null}.
     *
     * @param str the String to check, may be {@code null}
     * @return true if the String is not empty and not {@code null}
     */
    static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Requires that a {@link String} is not empty ({@code ""}) and not {@code null}.
     * If the requirement is violated, an {@link IllegalArgumentException} will be thrown.
     *
     * @param str the String to check, may be {@code null}
     * @param message the message to include in
     * @return the String, if the requirement was not violated
     * @throws IllegalArgumentException if the requirement was violated
     */
    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    static String requireNotEmpty(String str, String message) {
        if (isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }
}
