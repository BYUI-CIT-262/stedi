package com.getsimplex.steptimer.service;

import akka.actor.UntypedActor;
import com.getsimplex.steptimer.model.DeviceMessage;
import com.getsimplex.steptimer.model.StartKafka;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.logging.Logger;

public class KafkaRiskTopicActor extends UntypedActor {
    private static Logger logger = Logger.getLogger(KafkaRiskTopicActor.class.getName());

    public void onReceive(Object object){

        if (object instanceof StartKafka) {
            logger.info("Connecting to Kafka Topic: customer-risk");
            Consumer<Long, String> kafkaCustomerRiskTopic = KafkaConsumerUtil.createConsumer();

            while (true) {

                final ConsumerRecords<Long, String> consumerRecords = kafkaCustomerRiskTopic.poll(1);
                consumerRecords.forEach(record -> {
                    DeviceMessage deviceMessage = new DeviceMessage();
                    deviceMessage.setDeviceId("1234");
                    deviceMessage.setDate(System.currentTimeMillis());
                    deviceMessage.setMessage(record.value());
                    logger.info("Received 1 more message from Kafka: " + record.value());

                    MessageIntake.route(deviceMessage);

                });
            }
        }

    }

}
