/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.connect.storage.partitioner;

import org.apache.kafka.connect.sink.SinkRecord;

import java.util.List;
import java.util.Map;

/**
 * Partition incoming records, and generates directories and file names in which
 * to store the incoming records.
 *
 * @param <T> The type representing the field schemas.
 */
public interface Partitioner<T> {
  void configure(Map<String, Object> config);

  /**
   * Returns string representing the output path for a sinkRecord to be encoded
   * and stored.
   *
   * @param sinkRecord The record to be stored by the Sink Connector
   * @return The path/filename the SinkRecord will be stored into after it is
   *         encoded
   */
  String encodePartition(SinkRecord sinkRecord);

  /**
   * Returns string representing the output path for a sinkRecord to be encoded
   * and stored.
   *
   * @param sinkRecord  The record to be stored by the Sink Connector
   * @param nowInMillis The current time in ms. Some Partitioners will use this
   *                    option, but by default it is unused.
   * @return The path/filename the SinkRecord will be stored into after it is
   *         encoded
   */
  default String encodePartition(SinkRecord sinkRecord, long nowInMillis) {
    return encodePartition(sinkRecord);
  }

  String generatePartitionedPath(String topic, String encodedPartition);

  List<T> partitionFields();

  /**
   * Method that allows custom partitioners to determine when we should rotate
   * partitions
   * 
   * @param encodedPartition        The path/filename the SinkRecord will be
   *                                stored into after it is encoded
   * @param currentEncodedPartition The path/filename that was used for the
   *                                previous SinkRecord
   * @return true if the partition should be rotated. false otherwise.
   */
  default boolean shouldRotatePartition(String encodedPartition, String currentEncodedPartition) {
    return !encodedPartition.equals(currentEncodedPartition);
  }

}
