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

## 数据在其他的datanode话获取的流程
```text
执行的dataNode1向拥有数据的dataNode2请求数据
dataNode2得到数据(tsBlock形式)后,向dataNode1发送1个包含数据id之类的元信息不包含实际数据tsBlock的请求 sinkHandle.send(tsBlock);
dataNode1收到请求后确认是正确后向dataNode2发送获取tsBlock的请求得到实质数据
a
```