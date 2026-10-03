package org.example;

import org.example.connectivity.state.Connection;

import java.io.Serializable;

public class ConnectionDto implements Serializable {

    private final String id;

    private final String targetId;

    private final Connection.Type type;

    public ConnectionDto(String id, String targetId, Connection.Type type) {
        this.id = id;
        this.targetId = targetId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTargetId() {
        return targetId;
    }

    public Connection.Type getType() {
        return type;
    }
}
