package com.sb.services.common.registry.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ServiceInstanceInfo implements Serializable{

    @JsonProperty("ID")
    private String id = "N/A";

    @JsonProperty("Node")
    private String node = "N/A";

    @JsonProperty("Address")
    private String address = "N/A";

    @JsonProperty("ServiceID")
    private String serviceId = "N/A";

    @JsonProperty("ServiceName")
    private String serviceName = "N/A";

    @JsonProperty("ServiceAddress")
    private String serviceAddress;

    @JsonProperty("ServicePort")
    private int servicePort;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{ID:").append(this.id).append(",Address:").append(address).append(",ServiceAddress:").append(serviceAddress)
                .append(",ServicePort:").append(servicePort).append("}");
        return sb.toString();
    }
}
