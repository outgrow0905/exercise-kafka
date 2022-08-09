import consumer.MyConsumer;
import consumer.MyConsumerLoop;
import producer.MyProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        // Producer
        ExecutorService producerExecutor = Executors.newSingleThreadExecutor();
        producerExecutor.submit(() -> {
            MyProducer myProducer = new MyProducer();
            myProducer.send();
        });


        // Consumer
        int numberOfConsumers = 3;
        String topic = "my-topic";
        String groupId = "my-group-id";
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(numberOfConsumers);
//        List<MyConsumerLoop> myConsumerLoops = new ArrayList<>();
        for (int i = 0; i < numberOfConsumers; i++) {
            MyConsumerLoop consumer = new MyConsumerLoop(i, Arrays.asList(topic), groupId);
//            myConsumerLoops.add(consumer);
            consumerExecutor.submit(consumer);
        }

        Thread.sleep(60 * 60 * 24 * 1000);

        try {
            consumerExecutor.shutdown();
            consumerExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("thread shutdown fail in 10 seconds");
            consumerExecutor.shutdownNow();
        }
    }
}
