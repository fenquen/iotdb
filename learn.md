## 客户端的原生session连接到的是dataNode

客户端上的连到server代码

```java
TSOpenSessionResp openResp=client.openSession(openReq);
```

这是1个thrift体系的接口函数 在客户但端(iotdb-session)和服务端dataNode(iotdb-server)都有相应的实现

```java
public TSOpenSessionResp openSession(TSOpenSessionReq req)throws org.apache.thrift.TException;
```

服务端上的实现

```java
@Override
public TSOpenSessionResp openSession(TSOpenSessionReq req)throws TException{
        IoTDBConstant.ClientVersion clientVersion=parseClientVersion(req);
        
        BasicOpenSessionResp openSessionResp=
        SESSION_MANAGER.login(
        SESSION_MANAGER.getCurrSession(),
        req.username,
        req.password,
        req.zoneId,
        req.client_protocol,
        clientVersion);
        
        TSStatus tsStatus=RpcUtils.getStatus(openSessionResp.getCode(),openSessionResp.getMessage());
        
        TSOpenSessionResp resp=new TSOpenSessionResp(tsStatus,CURRENT_RPC_VERSION);
        
        return resp.setSessionId(openSessionResp.getSessionId());
        }
```
## InsertRowStatement 到 InsertRowNode
```text

```
public String[] getDataDirs() {
return dataDirs;
}

    public String getRpcAddress() {
        return rpcAddress;
    }

    void setRpcAddress(String rpcAddress) {
        this.rpcAddress = rpcAddress;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    void setRpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
    }

    public boolean isEnableWal() {
        return enableWal;
    }

    public void setEnableWal(boolean enableWal) {
        this.enableWal = enableWal;
    }

    public int getFlushWalThreshold() {
        return flushWalThreshold;
    }

    public void setFlushWalThreshold(int flushWalThreshold) {
        this.flushWalThreshold = flushWalThreshold;
    }

    public long getForceWalPeriodInMs() {
        return forceWalPeriodInMs;
    }

    public void setForceWalPeriodInMs(long forceWalPeriodInMs) {
        this.forceWalPeriodInMs = forceWalPeriodInMs;
    }

    public String getSystemDir() {
        return systemDir;
    }

    void setSystemDir(String systemDir) {
        this.systemDir = systemDir;
    }

    public String getWalFolder() {
        return walFolder;
    }

    void setWalFolder(String walFolder) {
        this.walFolder = walFolder;
    }

    void setDataDirs(String[] dataDirs) {
        this.dataDirs = dataDirs;
    }

    public String getMultiDirStrategyClassName() {
        return multiDirStrategyClassName;
    }

    void setMultiDirStrategyClassName(String multiDirStrategyClassName) {
        this.multiDirStrategyClassName = multiDirStrategyClassName;
    }

    public String getIndexFileDir() {
        return indexFileDir;
    }

    private void setIndexFileDir(String indexFileDir) {
        this.indexFileDir = indexFileDir;
    }

    public int getMergeConcurrentThreads() {
        return mergeConcurrentThreads;
    }

    void setMergeConcurrentThreads(int mergeConcurrentThreads) {
        this.mergeConcurrentThreads = mergeConcurrentThreads;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxMemtableNumber() {
        return maxMemtableNumber;
    }

    public void setMaxMemtableNumber(int maxMemtableNumber) {
        this.maxMemtableNumber = maxMemtableNumber;
    }

    public int getConcurrentFlushThread() {
        return concurrentFlushThread;
    }

    void setConcurrentFlushThread(int concurrentFlushThread) {
        this.concurrentFlushThread = concurrentFlushThread;
    }

    void setZoneID(ZoneId zoneID) {
        this.zoneID = zoneID;
    }

    public long getTsFileSizeThreshold() {
        return tsFileSizeThreshold;
    }

    public void setTsFileSizeThreshold(long tsFileSizeThreshold) {
        this.tsFileSizeThreshold = tsFileSizeThreshold;
    }

    public int getBackLoopPeriodSec() {
        return backLoopPeriodSec;
    }

    void setBackLoopPeriodSec(int backLoopPeriodSec) {
        this.backLoopPeriodSec = backLoopPeriodSec;
    }

    public boolean isEnableStatMonitor() {
        return enableStatMonitor;
    }

    public void setEnableStatMonitor(boolean enableStatMonitor) {
        this.enableStatMonitor = enableStatMonitor;
    }

    public int getStatMonitorDetectFreqSec() {
        return statMonitorDetectFreqSec;
    }

    void setStatMonitorDetectFreqSec(int statMonitorDetectFreqSec) {
        this.statMonitorDetectFreqSec = statMonitorDetectFreqSec;
    }

    public int getStatMonitorRetainIntervalSec() {
        return statMonitorRetainIntervalSec;
    }

    void setStatMonitorRetainIntervalSec(int statMonitorRetainIntervalSec) {
        this.statMonitorRetainIntervalSec = statMonitorRetainIntervalSec;
    }

    public int getmManagerCacheSize() {
        return mManagerCacheSize;
    }

    void setmManagerCacheSize(int mManagerCacheSize) {
        this.mManagerCacheSize = mManagerCacheSize;
    }

    public boolean isSyncEnable() {
        return isSyncEnable;
    }

    void setSyncEnable(boolean syncEnable) {
        isSyncEnable = syncEnable;
    }

    public int getSyncServerPort() {
        return syncServerPort;
    }

    void setSyncServerPort(int syncServerPort) {
        this.syncServerPort = syncServerPort;
    }

    public String getLanguageVersion() {
        return languageVersion;
    }

    void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    public boolean isUpdateHistoricalDataPossibility() {
        return updateHistoricalDataPossibility;
    }

    void setUpdateHistoricalDataPossibility(boolean updateHistoricalDataPossibility) {
        this.updateHistoricalDataPossibility = updateHistoricalDataPossibility;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(String ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public long getCacheFileReaderClearPeriod() {
        return cacheFileReaderClearPeriod;
    }

    public void setCacheFileReaderClearPeriod(long cacheFileReaderClearPeriod) {
        this.cacheFileReaderClearPeriod = cacheFileReaderClearPeriod;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getRpcImplClassName() {
        return rpcImplClassName;
    }

    public void setRpcImplClassName(String rpcImplClassName) {
        this.rpcImplClassName = rpcImplClassName;
    }

    public int getWalBufferSize() {
        return walBufferSize;
    }

    void setWalBufferSize(int walBufferSize) {
        this.walBufferSize = walBufferSize;
    }

    public boolean isChunkBufferPoolEnable() {
        return chunkBufferPoolEnable;
    }

    void setChunkBufferPoolEnable(boolean chunkBufferPoolEnable) {
        this.chunkBufferPoolEnable = chunkBufferPoolEnable;
    }

    public boolean isEnableParameterAdapter() {
        return enableParameterAdapter;
    }

    public void setEnableParameterAdapter(boolean enableParameterAdapter) {
        this.enableParameterAdapter = enableParameterAdapter;
    }

    public long getAllocateMemoryForWrite() {
        return allocateMemoryForWrite;
    }

    public void setAllocateMemoryForWrite(long allocateMemoryForWrite) {
        this.allocateMemoryForWrite = allocateMemoryForWrite;
    }

    public long getAllocateMemoryForRead() {
        return allocateMemoryForRead;
    }

    public void setAllocateMemoryForRead(long allocateMemoryForRead) {
        this.allocateMemoryForRead = allocateMemoryForRead;
    }

    public boolean isEnablePerformanceStat() {
        return enablePerformanceStat;
    }

    public void setEnablePerformanceStat(boolean enablePerformanceStat) {
        this.enablePerformanceStat = enablePerformanceStat;
    }

    public long getPerformanceStatDisplayInterval() {
        return performanceStatDisplayInterval;
    }

    public void setPerformanceStatDisplayInterval(long performanceStatDisplayInterval) {
        this.performanceStatDisplayInterval = performanceStatDisplayInterval;
    }

    public int getPerformanceStatMemoryInKB() {
        return performanceStatMemoryInKB;
    }

    public void setPerformanceStatMemoryInKB(int performanceStatMemoryInKB) {
        this.performanceStatMemoryInKB = performanceStatMemoryInKB;
    }

    public long getMemtableSizeThreshold() {
        return memtableSizeThreshold;
    }

    public void setMemtableSizeThreshold(long memtableSizeThreshold) {
        this.memtableSizeThreshold = memtableSizeThreshold;
    }