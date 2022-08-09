package producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class MyProducer {
    public void send() {
        Properties properties = new Properties();
        // required setting
        properties.put("bootstrap.servers", "kafka-lb-11608160-eb90449ba349.kr.lb.naverncp.com:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // optional setting
//        properties.put("client.id", "email-service-1");
//        properties.put("acks", "1");
//        properties.put("compression.type", "gzip");
//        properties.put("linger.ms", 1);
        Producer<String, String> producer = new KafkaProducer<>(properties);
        try {
            while (true) {
                Thread.sleep(1000);
                producer.send(
                    new ProducerRecord<>("my-topic", null, String.valueOf((int) (Math.random() * 10))),
                    new MyProducerCallback());
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            producer.close();
        }
    }

    class MyProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (null != exception) {
                // fail
                exception.printStackTrace();
            } else {
                // success
                System.out.println("send success. metadata: [" + metadata + "]");
                System.out.println("topic: " + metadata.topic());
                System.out.println("partition: " + metadata.partition());
                System.out.println("offset: " + metadata.offset());
            }
        }
    }
}