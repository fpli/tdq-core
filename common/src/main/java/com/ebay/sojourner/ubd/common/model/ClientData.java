package com.ebay.sojourner.ubd.common.model;

public class ClientData {
    private String ForwardFor;
    private String Script;
    private String Server;
    private String TMachine;
    private Long TStamp;
    private String TName;
    private String TPayload;
    private String colo;
    private String pool;
    private String agent;
    private String remoteIP;
    private String TType;
    private String TStatus;
    private String corrId;
    private Integer contentLength;
    private String nodeId;
    private String requestGuid;
    private String urlQueryString;
    private String referrer;
    private String rlogid;
    private String acceptEncoding;
    private Long TDuration;

    public String getForwardFor() {
        return ForwardFor;
    }

    public void setForwardFor(String forwardFor) {
        ForwardFor = forwardFor;
    }

    public String getScript() {
        return Script;
    }

    public void setScript(String script) {
        Script = script;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getTMachine() {
        return TMachine;
    }

    public void setTMachine(String TMachine) {
        this.TMachine = TMachine;
    }

    public Long getTStamp() {
        return TStamp;
    }

    public void setTStamp(Long TStamp) {
        this.TStamp = TStamp;
    }

    public String getTName() {
        return TName;
    }

    public void setTName(String TName) {
        this.TName = TName;
    }

    public String getTPayload() {
        return TPayload;
    }

    public void setTPayload(String TPayload) {
        this.TPayload = TPayload;
    }

    public String getColo() {
        return colo;
    }

    public void setColo(String colo) {
        this.colo = colo;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public String getTType() {
        return TType;
    }

    public void setTType(String TType) {
        this.TType = TType;
    }

    public String getTStatus() {
        return TStatus;
    }

    public void setTStatus(String TStatus) {
        this.TStatus = TStatus;
    }

    public String getCorrId() {
        return corrId;
    }

    public void setCorrId(String corrId) {
        this.corrId = corrId;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(String requestGuid) {
        this.requestGuid = requestGuid;
    }

    public String getUrlQueryString() {
        return urlQueryString;
    }

    public void setUrlQueryString(String urlQueryString) {
        this.urlQueryString = urlQueryString;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getRlogid() {
        return rlogid;
    }

    public void setRlogid(String rlogid) {
        this.rlogid = rlogid;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public Long getTDuration() {
        return TDuration;
    }

    public void setTDuration(Long TDuration) {
        this.TDuration = TDuration;
    }
}
