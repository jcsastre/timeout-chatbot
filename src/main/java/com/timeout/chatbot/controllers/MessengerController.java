package com.timeout.chatbot.controllers;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.exceptions.MessengerVerificationException;
import com.github.messenger4j.receive.MessengerReceiveClient;
import com.timeout.chatbot.configuration.MessengerConfiguration;
import com.timeout.chatbot.handler.messenger.AttachmentMessageEventHandlerImpl;
import com.timeout.chatbot.handler.messenger.PostbackEventHandlerImpl;
import com.timeout.chatbot.handler.messenger.QuickReplyMessageEventHandlerImpl;
import com.timeout.chatbot.handler.messenger.TextMessageEventHandlerImpl;
import com.timeout.chatbot.session.SessionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.messenger4j.MessengerPlatform.*;

@RestController
@RequestMapping("/messenger")
public class
MessengerController {
    private static final Logger logger = LoggerFactory.getLogger(MessengerController.class);

    private final MessengerReceiveClient receiveClient;

    @Autowired
    public MessengerController(
        MessengerConfiguration messengerConfiguration,
        SessionPool sessionPool,
        TextMessageEventHandlerImpl textMessageEventHandlerImpl,
        QuickReplyMessageEventHandlerImpl quickReplyMessageEventHandlerImpl,
        PostbackEventHandlerImpl postbackEventHandlerImpl,
        AttachmentMessageEventHandlerImpl attachmentMessageEventHandlerImpl
    ) {
        logger.debug(
            "Initializing MessengerReceiveClient - appSecret: {} | verifyToken: {}",
            messengerConfiguration.getSecret(),
            messengerConfiguration.getWebhookVerificationToken()
        );

        this.receiveClient =
            MessengerPlatform.newReceiveClientBuilder(
                messengerConfiguration.getSecret(),
                messengerConfiguration.getWebhookVerificationToken()
            )
            .onTextMessageEvent(
                textMessageEventHandlerImpl
            )
            .onQuickReplyMessageEvent(
                quickReplyMessageEventHandlerImpl
            )
            .onPostbackEvent(
                postbackEventHandlerImpl
            )
            .onAttachmentMessageEvent(
                attachmentMessageEventHandlerImpl
            )
            .build();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> verifyWebhook(
        @RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
        @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken,
        @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge
    ) {
        logger.debug(
            "Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}",
            mode, verifyToken, challenge
        );

        try {
            return ResponseEntity.ok(this.receiveClient.verifyWebhook(mode, verifyToken, challenge));
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(
        @RequestBody final String payload,
        @RequestHeader(SIGNATURE_HEADER_NAME) final String signature
    ) {
        logger.info("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);

//        return ResponseEntity.status(HttpStatus.OK).build();

        try {
            this.receiveClient.processCallbackPayload(payload, signature);
            logger.debug("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
