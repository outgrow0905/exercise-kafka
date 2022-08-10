package admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class MyAdminClient {
    private AdminClient adminClient;

    public MyAdminClient() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "kafka-lb-11608160-eb90449ba349.kr.lb.naverncp.com:9092");

        this.adminClient = AdminClient.create(properties);
    }

    public void listTopics() throws Exception {
        ListTopicsResult result = this.adminClient.listTopics();
        result.names().get().forEach(System.out::println);
    }

    public boolean isTopicExists(String topic) throws InterruptedException {
        DescribeTopicsResult result = this.adminClient.describeTopics(Arrays.asList(topic));
        Map<String, KafkaFuture<TopicDescription>> nameFutures = result.topicNameValues();

        try {
            TopicDescription topicDescription = nameFutures.get(topic).get();
            System.out.printf("name: %s\nisInternal: %s\n", topicDescription.name(), topicDescription.isInternal());
            return true;
        } catch (InterruptedException e1) {
            System.out.println(e1.getMessage());
            throw e1;
        } catch (ExecutionException e2) {
            System.out.println(e2.getMessage());
            if (e2.getCause() instanceof UnknownTopicOrPartitionException) {
                System.out.printf("topic %s doesn't exists.\n", topic);
                return false;
            }
        }

        throw new InterruptedException();
    }

    public void listConsumerGroups() {
        try {
            this.adminClient.listConsumerGroups().valid().get().forEach(System.out::println);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void describeConsumerGroups(String consumerGroupId) {
        try {
            ConsumerGroupDescription consumerGroupDescription =
                    adminClient.describeConsumerGroups(Arrays.asList(consumerGroupId)).describedGroups().get(consumerGroupId).get();

            System.out.println("Description of group " + consumerGroupId + ": " + consumerGroupDescription);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void listConsumerGroupOffsets(String consumerGroupId) {
        try {
            Map<TopicPartition, OffsetAndMetadata> offsets =
                    adminClient.listConsumerGroupOffsets(consumerGroupId)
                            .partitionsToOffsetAndMetadata().get();
            Map<TopicPartition, OffsetSpec> requestLatestOffsets = new HashMap<>();
            for (TopicPartition tp : offsets.keySet()) {
                requestLatestOffsets.put(tp, OffsetSpec.latest());
            }
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> latestOffsets =
                    adminClient.listOffsets(requestLatestOffsets).all().get();
            for (Map.Entry<TopicPartition, OffsetAndMetadata> e : offsets.entrySet()) {
                String topic = e.getKey().topic();
                int partition = e.getKey().partition();
                long committedOffset = e.getValue().offset();
                long latestOffset = latestOffsets.get(e.getKey()).offset();
                System.out.println("Consumer group " + consumerGroupId
                        + " has committed offset " + committedOffset
                        + " to topic " + topic + " partition " + partition
                        + ". The latest offset in the partition is "
                        + latestOffset + " so consumer group is "
                        + (latestOffset - committedOffset) + " records behind");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}