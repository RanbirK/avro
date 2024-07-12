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

import org.gradle.util.GradleVersion;

enum GradleFeatures {
    projectIntoExtensionInjection() {
        boolean isSupportedBy(GradleVersion version) {
            return version.compareTo(GradleVersions.v7_1) >= 0;
        }
    },
    objectFactoryFileCollection() {
        @Override
        boolean isSupportedBy(GradleVersion version) {
            return version.compareTo(GradleVersions.v5_3) >= 0;
        }
    },
    configCache() {
        @Override
        boolean isSupportedBy(GradleVersion version) {
            return version.compareTo(GradleVersions.v6_6) >= 0;
        }
    },
    getSourcesJarTaskName() {
        @Override
        boolean isSupportedBy(GradleVersion version) {
            return version.compareTo(GradleVersions.v6_0) >= 0;
        }
    },
    ideaModuleTestSources() {
        @Override
        boolean isSupportedBy(GradleVersion version) {
            return version.compareTo(GradleVersions.v7_4) >= 0;
        }
    };

    abstract boolean isSupportedBy(GradleVersion version);
    boolean isSupported() {
        return isSupportedBy(GradleVersion.current());
    }
}
