/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.iotdb.commons.partition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaPartitionInfo {

  // Map<StorageGroup, Map<DeviceGroupID, SchemaRegionPlaceInfo>>
  private Map<String, Map<DeviceGroupId, SchemaRegionReplicaSet>> schemaPartitionInfo;

  public Map<String, Map<DeviceGroupId, SchemaRegionReplicaSet>> getSchemaPartitionInfo() {
    return schemaPartitionInfo;
  }

  public void setSchemaPartitionInfo(
      Map<String, Map<DeviceGroupId, SchemaRegionReplicaSet>> schemaPartitionInfo) {
    this.schemaPartitionInfo = schemaPartitionInfo;
  }

  public Map<String, Map<Integer, SchemaRegionReplicaSet>> getSchemaPartition(
      String storageGroup, List<Integer> deviceGroupIDs) {
    Map<String, Map<Integer, SchemaRegionReplicaSet>> storageGroupMap = new HashMap<>();
    Map<Integer, SchemaRegionReplicaSet> deviceGroupMap = new HashMap<>();
    deviceGroupIDs.forEach(
        deviceGroupID -> {
          if (schemaPartitionInfo.get(storageGroup).containsKey(new DeviceGroupId(deviceGroupID))) {
            deviceGroupMap.put(
                deviceGroupID, schemaPartitionInfo.get(storageGroup).get(deviceGroupID));
          }
        });
    storageGroupMap.put(storageGroup, deviceGroupMap);
    return storageGroupMap;
  }
}