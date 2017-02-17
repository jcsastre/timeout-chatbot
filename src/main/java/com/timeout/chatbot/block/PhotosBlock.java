package com.timeout.chatbot.block;

import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.domain.Image;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhotosBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public PhotosBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void send(
        String userId,
        Venue venue
    ) {
        List<Image> images = venue.getImages();
        if (venue.getImages().size() > 10) {
            images = images.subList(0, 10);
        }

        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (Image image : images) {
            String title = image.getAltText();
            if (title == null) {
                title = image.getTitle();
                if (title == null) {
                    title = venue.getName();
                }
            }

            listBuilder.addElement(title).imageUrl(image.getUrl()).toList().done();
        }
        final GenericTemplate genericTemplate = genericTemplateBuilder.build();

        messengerSendClientWrapper.sendTemplate(
            Recipient.newBuilder().recipientId(userId).build(),
            NotificationType.REGULAR,
            genericTemplate,
            quickReplyBuilderHelper.buildForSeeVenueItem(venue)
        );
    }
}
