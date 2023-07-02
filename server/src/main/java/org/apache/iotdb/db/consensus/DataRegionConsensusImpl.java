/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.db.consensus;

import org.apache.iotdb.common.rpc.thrift.TEndPoint;
import org.apache.iotdb.commons.consensus.DataRegionId;
import org.apache.iotdb.consensus.ConsensusFactory;
import org.apache.iotdb.consensus.IConsensus;
import org.apache.iotdb.consensus.config.ConsensusConfig;
import org.apache.iotdb.consensus.config.IoTConsensusConfig;
import org.apache.iotdb.consensus.config.IoTConsensusConfig.RPC;
import org.apache.iotdb.consensus.config.RatisConfig;
import org.apache.iotdb.consensus.config.RatisConfig.Snapshot;
import org.apache.iotdb.db.conf.IoTDBConfig;
import org.apache.iotdb.db.conf.IoTDBDescriptor;
import org.apache.iotdb.db.consensus.statemachine.DataRegionStateMachine;
import org.apache.iotdb.db.engine.StorageEngineV2;

import org.apache.ratis.util.SizeInBytes;
import org.apache.ratis.util.TimeDuration;

import java.util.concurrent.TimeUnit;

/**
 * We can use DataRegionConsensusImpl.getInstance() to obtain a consensus layer reference for
 * dataRegion's reading and writing
 */
public class DataRegionConsensusImpl {
  private static final IoTDBConfig IO_TDB_CONFIG = IoTDBDescriptor.getInstance().getConfig();

  private static IConsensus INSTANCE = null;

  private DataRegionConsensusImpl() {}

  // need to create instance before calling this method
  public static IConsensus getInstance() {
    return INSTANCE;
  }

  public static synchronized IConsensus setupAndGetInstance() {
    if (INSTANCE == null) {
      INSTANCE =
          ConsensusFactory.getConsensusImpl(
                  IO_TDB_CONFIG.getDataRegionConsensusProtocolClass(),
                  ConsensusConfig.newBuilder()
                      .setThisNodeId(IO_TDB_CONFIG.getDataNodeId())
                      .setThisNode(new TEndPoint(IO_TDB_CONFIG.getInternalAddress(), IO_TDB_CONFIG.getDataRegionConsensusPort()))
                      .setStorageDir(IO_TDB_CONFIG.getDataRegionConsensusDir())
                      .setIoTConsensusConfig(
                          IoTConsensusConfig.newBuilder()
                              .setRpc(
                                  RPC.newBuilder()
                                      .setConnectionTimeoutInMs(IO_TDB_CONFIG.getConnectionTimeoutInMS())
                                      .setRpcSelectorThreadNum(IO_TDB_CONFIG.getRpcSelectorThreadCount())
                                      .setRpcMinConcurrentClientNum(
                                          IO_TDB_CONFIG.getRpcMinConcurrentClientNum())
                                      .setRpcMaxConcurrentClientNum(
                                          IO_TDB_CONFIG.getRpcMaxConcurrentClientNum())
                                      .setRpcThriftCompressionEnabled(
                                          IO_TDB_CONFIG.isRpcThriftCompressionEnable())
                                      .setSelectorNumOfClientManager(
                                          IO_TDB_CONFIG.getSelectorNumOfClientManager())
                                      .setThriftServerAwaitTimeForStopService(
                                          IO_TDB_CONFIG.getThriftServerAwaitTimeForStopService())
                                      .setThriftMaxFrameSize(IO_TDB_CONFIG.getThriftMaxFrameSize())
                                      .build())
                              .setReplication(
                                  IoTConsensusConfig.Replication.newBuilder()
                                      .setWalThrottleThreshold(IO_TDB_CONFIG.getThrottleThreshold())
                                      .setAllocateMemoryForConsensus(
                                          IO_TDB_CONFIG.getAllocateMemoryForConsensus())
                                      .build())
                              .build())
                      .setRatisConfig(
                          RatisConfig.newBuilder()
                              // An empty log is committed after each restart, even if no data is
                              // written. This setting ensures that compaction work is not discarded
                              // even if there are frequent restarts
                              .setSnapshot(
                                  Snapshot.newBuilder()
                                      .setCreationGap(1)
                                      .setAutoTriggerThreshold(
                                          IO_TDB_CONFIG.getDataRatisConsensusSnapshotTriggerThreshold())
                                      .build())
                              .setLog(
                                  RatisConfig.Log.newBuilder()
                                      .setUnsafeFlushEnabled(
                                          IO_TDB_CONFIG.isDataRatisConsensusLogUnsafeFlushEnable())
                                      .setSegmentSizeMax(
                                          SizeInBytes.valueOf(
                                              IO_TDB_CONFIG.getDataRatisConsensusLogSegmentSizeMax()))
                                      .setPreserveNumsWhenPurge(
                                          IO_TDB_CONFIG.getDataRatisConsensusPreserveWhenPurge())
                                      .build())
                              .setGrpc(
                                  RatisConfig.Grpc.newBuilder()
                                      .setFlowControlWindow(
                                          SizeInBytes.valueOf(
                                              IO_TDB_CONFIG.getDataRatisConsensusGrpcFlowControlWindow()))
                                      .build())
                              .setRpc(
                                  RatisConfig.Rpc.newBuilder()
                                      .setTimeoutMin(
                                          TimeDuration.valueOf(
                                              IO_TDB_CONFIG
                                                  .getDataRatisConsensusLeaderElectionTimeoutMinMs(),
                                              TimeUnit.MILLISECONDS))
                                      .setTimeoutMax(
                                          TimeDuration.valueOf(
                                              IO_TDB_CONFIG
                                                  .getDataRatisConsensusLeaderElectionTimeoutMaxMs(),
                                              TimeUnit.MILLISECONDS))
                                      .setRequestTimeout(
                                          TimeDuration.valueOf(
                                              IO_TDB_CONFIG.getDataRatisConsensusRequestTimeoutMs(),
                                              TimeUnit.MILLISECONDS))
                                      .setFirstElectionTimeoutMin(
                                          TimeDuration.valueOf(
                                              IO_TDB_CONFIG.getRatisFirstElectionTimeoutMinMs(),
                                              TimeUnit.MILLISECONDS))
                                      .setFirstElectionTimeoutMax(
                                          TimeDuration.valueOf(
                                              IO_TDB_CONFIG.getRatisFirstElectionTimeoutMaxMs(),
                                              TimeUnit.MILLISECONDS))
                                      .build())
                              .setLeaderLogAppender(
                                  RatisConfig.LeaderLogAppender.newBuilder()
                                      .setBufferByteLimit(
                                          IO_TDB_CONFIG.getDataRatisConsensusLogAppenderBufferSizeMax())
                                      .build())
                              .setRatisConsensus(
                                  RatisConfig.RatisConsensus.newBuilder()
                                      .setClientRequestTimeoutMillis(
                                          IO_TDB_CONFIG.getDataRatisConsensusRequestTimeoutMs())
                                      .setClientMaxRetryAttempt(
                                          IO_TDB_CONFIG.getDataRatisConsensusMaxRetryAttempts())
                                      .setClientRetryInitialSleepTimeMs(
                                          IO_TDB_CONFIG.getDataRatisConsensusInitialSleepTimeMs())
                                      .setClientRetryMaxSleepTimeMs(
                                          IO_TDB_CONFIG.getDataRatisConsensusMaxSleepTimeMs())
                                      .setTriggerSnapshotFileSize(
                                          IO_TDB_CONFIG.getDataRatisLogMaxMB() * 1024 * 1024)
                                      .build())
                              .build())
                      .build(),
                  gid ->
                      new DataRegionStateMachine(
                          StorageEngineV2.getInstance().getDataRegion((DataRegionId) gid)))
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          String.format(
                              ConsensusFactory.CONSTRUCT_FAILED_MSG,
                              IO_TDB_CONFIG.getDataRegionConsensusProtocolClass())));
    }
    return INSTANCE;
  }
}
