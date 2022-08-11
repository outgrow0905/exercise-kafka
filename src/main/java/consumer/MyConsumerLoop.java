package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MyConsumerLoop implements Runnable {
    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;
    private final int id;

    public MyConsumerLoop(int id, List<String> topics, String groupId) {
        this.id = id;
        this.topics = topics;

        Properties properties = new Properties();

        // required setting
        properties.put("bootstrap.servers", "kafka-lb-11608160-eb90449ba349.kr.lb.naverncp.com:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // optional setting
        properties.put("group.id", groupId);
//        properties.put("auto.offset.reset", "earliest");
//        properties.put("auto.commit.interval.ms", "500");

        this.consumer = new KafkaConsumer(properties);
    }

    @Override
    public void run() {
        try {
            this.consumer.subscribe(this.topics);
            Duration timeout = Duration.ofMillis(1000);
            while(true) {
                ConsumerRecords<String, String> records = this.consumer.poll(timeout);
                System.out.printf("records count: %d\n", records.count());
                for (ConsumerRecord<String, String> record : records) {
                    Thread.sleep(1000);
                    System.out.println("====================");
                    System.out.printf("consumerId: %d\ntopic: %s\npartition: %d\noffset: %d\nkey: %s\n value: %s\n",
                            this.id, record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    System.out.println("====================");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            this.consumer.close();
        }
    }
}
