package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MyConsumer {
    public void consume() {
        Properties properties = new Properties();

        // required setting
        properties.put("bootstrap.servers", "kafka-lb-11608160-eb90449ba349.kr.lb.naverncp.com:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // optional setting
        properties.put("group.id", "test-group-id-1");
        properties.put("auto.offset.reset", "earliest");

        KafkaConsumer consumer = new KafkaConsumer(properties);
        consumer.subscribe(Collections.singletonList("my-topic"));

        Duration timeout = Duration.ofMillis(100);

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(timeout);

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("topic: %s\n partition: %d\n offset: %d\n key: %s value: %s",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }
}
