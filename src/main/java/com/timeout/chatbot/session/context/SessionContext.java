package com.timeout.chatbot.session.context;

import ai.api.AIServiceException;
import ai.api.model.Result;
import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.ApiAiService;

public abstract class SessionContext {

    final User user;
    final ApiAiService apiAiService;
    final MessengerSendClientWrapper messengerSendClientWrapper;

    SessionContext(
        User user,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.user = user;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public abstract void handleTextMessageEvent(TextMessageEvent event);
    public abstract void handlePostbackEvent(PostbackEvent event);
    public abstract void handleQuickReplyMessageEvent(QuickReplyMessageEvent event);
    public abstract void handleAttachmentMessageEvent(AttachmentMessageEvent event);

    Result getApiaiResult(
        String utterance
    ) {
        try {
            return apiAiService.processText(utterance);
        } catch (AIServiceException e) {
            e.printStackTrace();
            return null;
        }
    }
}
