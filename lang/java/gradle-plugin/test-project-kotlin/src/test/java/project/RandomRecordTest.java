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

package project;

import org.apache.avro.specific.SpecificRecord;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static project.CLIUtil.runCLITool;

public class RandomRecordTest {
    @TempDir
    Path cliGeneratedDir;
    Path schemaDir = Paths.get("src/main/avro");
    
    @SuppressWarnings("unused")
    private static Stream<Arguments> generateRandomRecords() {
        return Stream.of(
            // From https://stackoverflow.com/questions/45581437/how-to-specify-converter-for-default-value-in-avro-union-logical-type-fields
            Arguments.of("BuggyRecord.avsc"),
            // From https://github.com/davidmc24/gradle-avro-plugin/issues/120
            Arguments.of("Messages.avsc")
        );
    }

    @ParameterizedTest
    @MethodSource
    <T extends SpecificRecord> void generateRandomRecords(String schemaPath) throws Exception {
        Path schemaFile = schemaDir.resolve(schemaPath);
        Path outputFile = cliGeneratedDir.resolve("random.avro");
        List<String> args = new ArrayList<>();
        args.add("random");
        args.add("--count");
        args.add("1");
        args.add("--schema-file");
        args.add(schemaFile.toString());
        args.add(outputFile.toString());
        runCLITool(args.toArray(new String[0]));
    }
}
