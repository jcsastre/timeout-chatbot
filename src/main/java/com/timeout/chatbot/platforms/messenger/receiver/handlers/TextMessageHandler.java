package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextMessageHandler implements TextMessageEventHandler {

    private final SessionPool sessionPool;

    @Autowired
    public TextMessageHandler(SessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(TextMessageEvent textMessageEvent) {
        final String pageId = textMessageEvent.getSender().getId();
        final String recipientId = textMessageEvent.getSender().getId();

        final Session session = sessionPool.getSession(pageId, recipientId);
        session.applyUtterance(textMessageEvent.getText());

//        String pageId = event.getSender().getId();
//        String recipientId = event.getSender().getId();
//
//        Session session = sessionPool.getSession(pageId, recipientId);
//
//        String text = event.getText();
//
//        Result apiaiResult = null;
//        try {
//            apiaiResult = apiAiService.processText(recipientId, text);
//        } catch (AIServiceException e) {
//            messengerSendWrapper.sendTextMessageProblems(recipientId);
//            e.printStackTrace();
//            return;
//        }
//
//        if (apiaiResult == null) {
//            messengerSendWrapper.sendTextMessageProblems(recipientId);
//            return;
//        }
//
//        final String action = apiaiResult.getAction();
//        System.out.println("------------------------------------");
//        System.out.println("ApiaiIntent: " + action);
//        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
//        for (Map.Entry<String, JsonElement> parameter : apiaiParameters.entrySet()) {
//            System.out.println("Parameter <" + parameter.getKey() + ">: " + parameter.getValue());
//        }
//
//        final SessionIntent sessionIntent = SessionIntent.fromApiaiActionString(action);
//
//        try {
//            messengerSendWrapper.sendTextMessage(recipientId, "ApiaiIntent: " + action);
//        } catch (MessengerApiException | MessengerIOException e) {
//            e.printStackTrace();
//        }
//
////        User fbRecipient = new User(recipientId);
////
////        List<QuickReply> quickReplies = QuickReply.newListBuilder()
////                .addTextQuickReply("great", "GREAT_PAYLOAD").toList()
////                .addTextQuickReply("brilliant", "BRILLIANT_PAYLOAD").imageUrl("https://image.freepik.com/free-icon/thumb-up_318-121727.jpg").toList()
////                .addLocationQuickReply().toList()
////                .build();
////
////        try {
////            messengerSendWrapper.sendTextMessage(recipientId, "Hi there, how are you today?", quickReplies);
//////            messengerSendWrapper.sendTextMessage(recipientId, "Recibido!");
////        } catch (MessengerApiException | MessengerIOException e) {
////            e.printStackTrace();
////        }
    }
}
