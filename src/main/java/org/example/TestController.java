package org.example;

import io.grpc.stub.StreamObserver;
import org.example.config.PeerConfiguration;
import org.example.connectivity.ConnectionManager;
import org.example.connectivity.state.GlobalConnectionRegistry;
import org.example.messaging.ProtostuffMessageSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TestController {

    @Autowired
    private ProtostuffMessageSerializer serializer;


    @Autowired
    private ConnectionManager connectionManager;

    @Autowired
    private GlobalConnectionRegistry registry;

    @Autowired
    private PeerConfiguration peerConfiguration;

    @GetMapping(value = "/send")
    public void send(@RequestParam(value = "to") String to) {
//        streamManager.send(to);
        sendBatch(to);
    }

    @GetMapping(value = "/complete")
    public void complete(@RequestParam(value = "to") String to) {
//        streamManager.complete(to);
    }

//    @GetMapping(value = "/error")
//    public void error(@RequestParam(value = "to") String to) {
//        streamManager.error(to);
//    }

    @GetMapping(value = "/status")
    public ResponseEntity<StatusResponse> getObservers() {
        StatusResponse response = new StatusResponse();
//        response.getChannels().addAll(managedChannelHolder.getChannels());
//        response.getRequestObservers().addAll(streamManager.getRequestObservers2().keySet().stream().toList());
//        response.getResponseObservers().addAll(streamManager.getResponseObservers2().keySet().stream().toList());
        response.getConfirmedObservers().addAll(registry.getConfirmedConnections().keySet().stream().toList());

        List<ConnectionDto> connections = registry.getConnections().values().stream().map(c -> new ConnectionDto(c.getId(), c.getRemotePeerId(), c.getType())).toList();

//        response.getConnections().addAll(connections);
        return ResponseEntity.ok(response);
    }

    //TODO move to other service
    public void sendSingle(String target) {

        TestMessage testMessage = new TestMessage();

        Envelope envelope = Envelope.newBuilder()
                .setFrom(peerConfiguration.getId())
                .setTo(target)
                .setPayload(serializer.pack(testMessage))
                .build();

        registry.getConfirmedConnections().get(target).getTargetObserver().onNext(envelope);
    }

    //TODO move to other service
    public void sendBatch(String target) {

        StreamObserver<Envelope> observer = registry.getConfirmedConnections().get(target).getTargetObserver();
        double random =  Math.random() * 1000;
        int count = (int) random;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int taskId = 0; taskId < 5; taskId++) {
                executor.submit(() -> {
                    System.out.println("Отправляю %s сообщений в потоке %s".formatted(count, Thread.currentThread()));
                    for (int i = 0; i < count; i++) {
                        TestMessage message = new TestMessage();
                        Envelope envelope = Envelope.newBuilder()
                                .setUuid(java.util.UUID.randomUUID().toString())
                                .setFrom(peerConfiguration.getId())
                                .setTo(target)
                                .setTimestamp(System.currentTimeMillis())
                                .setPayload(serializer.pack(message))
                                .build();
                        System.out.println(Date.from(Instant.now()) + " Отправлено сообщение для " + envelope.getTo() + " co значением: " + message.getValue() + " из потока " + Thread.currentThread());
                        observer.onNext(envelope);
                    }
                });
            }
        }
    }
}
