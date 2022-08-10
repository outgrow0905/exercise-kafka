package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class MyConsumer {
//    private int failCounter = 0;

    public void consume() {
        Properties properties = new Properties();

        // required setting
        properties.put("bootstrap.servers", "kafka-lb-11608160-eb90449ba349.kr.lb.naverncp.com:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // optional setting
        properties.put("group.id", "test-group-id-1");
//        properties.put("auto.offset.reset", "earliest");

        KafkaConsumer consumer = new KafkaConsumer(properties);
        consumer.subscribe(Collections.singletonList("my-topic"));
        Duration timeout = Duration.ofMillis(1000);
        try {
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(timeout);
                System.out.printf("records.count() %s\n", records.count());
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("consume success.");
                    System.out.printf("topic: %s\n partition: %d\n offset: %d\n key: %s value: %s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
//
//                    System.out.printf("failCounter: %s", ++failCounter);
//                    if (failCounter == 3) {
//                        throw new RuntimeException("consumer fail");
//                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            consumer.close();
        }
    }
}
