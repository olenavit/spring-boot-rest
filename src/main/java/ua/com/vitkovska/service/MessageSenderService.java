package ua.com.vitkovska.service;

import ua.com.vitkovska.message.EmailDetailsMessage;

public interface MessageSenderService {
    void sendMessage(EmailDetailsMessage dto);
}
