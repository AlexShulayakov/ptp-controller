package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusResponse implements Serializable {

    private List<String> channels = new ArrayList<>();

//    List<String> requestObservers = new ArrayList<>();
//
//    List<String> responseObservers = new ArrayList<>();

    private List<String> confirmedObservers = new ArrayList<>();

    private List<ConnectionDto> connections = new ArrayList<>();

    public List<String> getChannels() {
        return channels;
    }
//
//    public List<String> getRequestObservers() {
//        return requestObservers;
//    }
//
//    public List<String> getResponseObservers() {
//        return responseObservers;
//    }

    public List<String> getConfirmedObservers() {
        return confirmedObservers;
    }

    public List<ConnectionDto> getConnections() {
        return connections;
    }
}
