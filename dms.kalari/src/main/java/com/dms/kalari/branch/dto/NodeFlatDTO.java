package com.dms.kalari.branch.dto;

public interface NodeFlatDTO {
    Long getNodeId();
    Long getParentId();
    String getNodeName();
    String getNodeType();
    Integer getLvl();
    Integer getNodeStatus();
}
