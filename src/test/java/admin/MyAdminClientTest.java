package admin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyAdminClientTest {

    MyAdminClient myAdminClient = new MyAdminClient();

    @Test
    void listTopics() throws Exception {
        myAdminClient.listTopics();
    }

    @Test
    void isTopicExists() throws Exception {
        assertTrue(myAdminClient.isTopicExists("my-topic"));
        assertFalse(myAdminClient.isTopicExists("my-topic-false"));
    }

    @Test
    void listConsumerGroups() {
        myAdminClient.listConsumerGroups();
    }

    @Test
    void describeConsumerGroups() {
        myAdminClient.describeConsumerGroups("my-group-id");
    }

    @Test
    void listConsumerGroupOffsets() {
        myAdminClient.listConsumerGroupOffsets("my-group-id");
    }
}