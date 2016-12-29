package com.timeout.chatbot.controllers;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.exceptions.MessengerVerificationException;
import com.github.messenger4j.receive.MessengerReceiveClient;
import com.timeout.chatbot.config.ApplicationConfig;
import com.timeout.chatbot.platforms.messenger.receiver.handlers.TextMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.messenger4j.MessengerPlatform.*;

@RestController
@RequestMapping("/messengerWebhook")
public class MessengerWebhook {
    private static final Logger logger = LoggerFactory.getLogger(MessengerWebhook.class);

    private final ApplicationConfig applicationConfig;

    private final MessengerReceiveClient receiveClient;

    /**
     * Constructs the {@code MessengerPlatformCallbackHandler} and initializes the {@code MessengerReceiveClient}.
     *
     * @param appSecret   the {@code Application Secret}
     * @param verifyToken the {@code Verification Token} that has been provided by you during the setup of the {@code
     *                    Webhook}
     * @param sendClient  the initialized {@code MessengerSendClient}
     */
    @Autowired
    public MessengerWebhook(
        ApplicationConfig applicationConfig,
        TextMessageHandler textMessageHandler
    ) {
        this.applicationConfig = applicationConfig;

        logger.debug(
            "Initializing MessengerReceiveClient - appSecret: {} | verifyToken: {}",
            applicationConfig.getMessenger().getApp().getSecret(),
            applicationConfig.getMessenger().getApp().getWebhookVerificationToken()
        );

        this.receiveClient =
            MessengerPlatform.newReceiveClientBuilder(
                applicationConfig.getMessenger().getApp().getSecret(),
                applicationConfig.getMessenger().getApp().getWebhookVerificationToken()
            )
            .onTextMessageEvent(textMessageHandler)
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

//    @RequestMapping("/")
//    @ResponseBody
//    String home() {
//        return applicationConfig.getMessenger().getApp().getPageAccessToken();
//    }
}
