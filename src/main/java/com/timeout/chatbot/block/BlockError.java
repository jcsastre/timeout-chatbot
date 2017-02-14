package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlockError {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public BlockError(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        User user
    ) {
        messengerSendClientWrapper.sendTemplate(
            user.getMessengerId(),
            ButtonTemplate.newBuilder(
                "Sorry, an error occurred. You can start over if you like",
                Button.newListBuilder()
                    .addPostbackButton(
                        "Start over",
                        new JSONObject()
                            .put("type", PayloadType.start_over)
                            .toString()
                    ).toList()
                    .build()
            ).build()
        );
    }
}
