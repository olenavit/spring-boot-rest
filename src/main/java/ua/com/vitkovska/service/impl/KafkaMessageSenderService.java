package ua.com.vitkovska.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;
import ua.com.vitkovska.message.EmailDetailsMessage;
import ua.com.vitkovska.service.MessageSenderService;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageSenderService implements MessageSenderService {
    @Value("${kafka.topic.teamEmail}")
    private String teamEmailReceivedTopic;

    private final KafkaOperations<String, EmailDetailsMessage> kafkaOperations;

    @Override
    public void sendMessage(EmailDetailsMessage emailDetailsMessage) {
        try {
            kafkaOperations.send(teamEmailReceivedTopic, emailDetailsMessage);
        } catch (Exception e) {
            log.error("Error while trying to send kafka message: ", e);
        }
    }
}
