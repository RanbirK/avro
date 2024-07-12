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

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.apache.avro.Schema;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.specs.NotSpec;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Task to read Avro schema files, resolve their dependencies, and write out dependency-free Avro schema files.
 */
@CacheableTask
public class ResolveAvroDependenciesTask extends OutputDirTask {
    private final SchemaResolver resolver = new SchemaResolver(getProject().getLayout(), getLogger());

    @TaskAction
    protected void process() {
        getLogger().info("Found {} files", getInputs().getSourceFiles().getFiles().size());
        failOnUnsupportedFiles();
        processFiles();
    }

    private void failOnUnsupportedFiles() {
        FileCollection unsupportedFiles = filterSources(new NotSpec<>(new FileExtensionSpec(Constants.SCHEMA_EXTENSION)));
        if (!unsupportedFiles.isEmpty()) {
            throw new GradleException(
                String.format("Unsupported file extension for the following files: %s", unsupportedFiles));
        }
    }

    private void processFiles() {
        int processedFileCount = processSchemaFiles();
        setDidWork(processedFileCount > 0);
    }

    private int processSchemaFiles() {
        Set<File> inputFiles = filterSources(new FileExtensionSpec(Constants.SCHEMA_EXTENSION)).getFiles();
        ProcessingState processingState = resolver.resolve(inputFiles);
        for (Schema schema : processingState.getSchemas()) {
            try {
                File outputFile = new File(getOutputDir().get().getAsFile(), AvroUtils.assemblePath(schema));
                String schemaJson = schema.toString(true);
                FileUtils.writeJsonFile(outputFile, schemaJson);
                getLogger().debug("Wrote {}", outputFile.getPath());
            } catch (IOException ex) {
                throw new GradleException(String.format("Failed to write resolved schema definition for %s", schema.getFullName()), ex);
            }
        }
        return processingState.getProcessedTotal();
    }
}
