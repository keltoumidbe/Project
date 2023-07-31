/*
 * Copyright (c) 2022. HPS Solution.
 * Author: ismail.chakour@hps-worldwide.com (Ismail CHAKOUR)
 */

package com.Project.Project.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

@Data
@JacksonXmlRootElement(localName = "BaseMessage")
public class BaseMessage {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Field> field = new ArrayList<>();
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ComplexField> complexFields = new ArrayList<>();
    @JacksonXmlProperty(isAttribute = true, localName = "cid")
    private String cid;
    @JacksonXmlProperty(isAttribute = true, localName = "topic")
    private String topic;
    @JacksonXmlProperty(isAttribute = true, localName = "sink")
    private boolean sink;
    @JacksonXmlProperty(isAttribute = true, localName = "key")
    private String key;
    @JacksonXmlProperty(isAttribute = true, localName = "issKey")
    private String issKey;
    @JacksonXmlProperty(isAttribute = true, localName = "acqKey")
    private String acqKey;
    @JacksonXmlProperty(isAttribute = true, localName = "netkey")
    private String netKey;
    @JacksonXmlProperty(isAttribute = true, localName = "mode")
    private int mode;
    @JacksonXmlProperty(isAttribute = true, localName = "state")
    private String state;
    @JacksonXmlProperty(isAttribute = true, localName = "captureCode")
    private String captureCode;
    @JacksonXmlProperty(isAttribute = true, localName = "routingCode")
    private String routingCode;
    @JacksonXmlProperty(isAttribute = true, localName = "messageType")
    private int messageType;
    @JacksonXmlProperty(isAttribute = true, localName = "partition")
    private int partition;
    @JacksonXmlProperty(isAttribute = true, localName = "microservice")
    private String microservice;

}
