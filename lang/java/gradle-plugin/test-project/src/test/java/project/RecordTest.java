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

import com.example.BuggyRecord;
import com.example.BuggyRecordWorkaround;
import com.somedomain.Messages;
import org.apache.avro.Schema;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.stream.Stream;

public class RecordTest {
    private static final EncoderFactory encoderFactory = EncoderFactory.get();

    @SuppressWarnings("unused")
    private static Stream<Arguments> buildAndWriteRecord() {
        return Stream.of(
            // From https://stackoverflow.com/questions/45581437/how-to-specify-converter-for-default-value-in-avro-union-logical-type-fields
            // Broken due to an Avro bug
            Arguments.of(BuggyRecord.newBuilder().setMyMandatoryDate(Instant.now()).build()),
            // Broken due to an Avro bug
            Arguments.of(BuggyRecordWorkaround.newBuilder().setMyMandatoryDate(Instant.now()).build()),
            // From https://github.com/davidmc24/gradle-avro-plugin/issues/120
            // Broken due to an Avro bug
            Arguments.of(Messages.newBuilder().setStart(Instant.now()).build())
        );
    }

    // Broken due to an Avro bug
    @Disabled
    @ParameterizedTest
    @MethodSource
    <T extends SpecificRecord> void buildAndWriteRecord(T record) throws Exception {
        Schema schema = record.getSchema();
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<>();
        OutputStream outputStream = new ByteArrayOutputStream();
        Encoder jsonEncoder = encoderFactory.jsonEncoder(schema, outputStream, true);
        Encoder validatingEncoder = encoderFactory.validatingEncoder(schema, jsonEncoder);
        writer.write(record, validatingEncoder);
    }
}
