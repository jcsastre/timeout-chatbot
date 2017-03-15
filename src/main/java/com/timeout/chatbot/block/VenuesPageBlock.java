package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementHelper;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VenuesPageBlock {

    private final MessengerSendClient messengerSendClient;
    private final GenericTemplateElementHelper genericTemplateElementHelper;
    private final SenderActionsHelper senderActionsHelper;

    @Autowired
    public VenuesPageBlock(
        MessengerSendClient messengerSendClient,
        GenericTemplateElementHelper genericTemplateElementHelper,
        SenderActionsHelper senderActionsHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.genericTemplateElementHelper = genericTemplateElementHelper;
        this.senderActionsHelper = senderActionsHelper;
    }

    public void send(
        String userMessengerId,
        List<PageItem> pageItems,
        String itemPluralName
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        sendHorizontalCarroussel(
            userMessengerId,
            pageItems
        );
    }

    private void sendHorizontalCarroussel(
        String recipientId,
        List<PageItem> pageItems
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final WaitingPhrasesBlock waitingPhrasesBlock =
            new WaitingPhrasesBlock(
                recipientId,
                messengerSendClient,
                senderActionsHelper
            );

        (new Thread(waitingPhrasesBlock)).start();

        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (PageItem pageItem : pageItems) {
            genericTemplateElementHelper.addNotSingleElementInList(listBuilder, pageItem);
        }

        waitingPhrasesBlock.cancel();

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClient.sendTemplate(recipientId, genericTemplate);
    }
}
