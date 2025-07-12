package com.ducnt.account.mq;

import com.ducnt.account.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountEventListener {
    IAccountService accountService;

    @KafkaListener(topics = "${kafka.topic.name}", topicPartitions = {
            @TopicPartition(topic = "${kafka.topic.name}", partitions = {"0"})
    })
    public void consumeReserveMess(@Header(KafkaHeaders.RECEIVED_KEY) String key, String message) {
        accountService.reserveAccountBalance(key, message);
    }

    @KafkaListener(topics = "${kafka.topic.name}", topicPartitions = {
            @TopicPartition(topic = "${kafka.topic.name}", partitions = {"1"})
    })
    public void consumeFinalizeMess(@Header(KafkaHeaders.RECEIVED_KEY) String key, String message) {
        accountService.finalizeAccountBalance(message);
    }
}
