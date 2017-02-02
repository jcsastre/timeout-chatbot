package com.timeout.chatbot.block.types;

import com.github.messenger4j.send.buttons.Button;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.categorisation.GraffittiCategorisation;
import com.timeout.chatbot.graffitti.response.categorisation.GraffittiCategorisationPrimary;
import com.timeout.chatbot.graffitti.response.categorisation.GraffittiCategorisationSecondary;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockTypeVenueHelper {

    public String buildGenericTemplateElementSubtitle(PageItem pageItem) {
        StringBuilder sb = new StringBuilder();

        final GraffittiCategorisation categorisation = pageItem.getGraffittiCategorisation();
        if (categorisation != null) {
            final GraffittiCategorisationPrimary categorisationPrimary =
                categorisation.getGraffittiCategorisationPrimary();
            if (categorisationPrimary != null) {
                sb.append(categorisationPrimary.getName());
            }

            final GraffittiCategorisationSecondary categorisationSecondary =
                categorisation.getGraffittiCategorisationSecondary();
            if (categorisationSecondary != null) {
                if (categorisationPrimary != null) {
                    sb.append(" ");
                }
                sb.append("\ud83c\udf74");
                sb.append(" ");
                sb.append(categorisationSecondary.getName());
            }
        }

//        sb.append(pageItem.getGraffittiCategorisation().buildName());

        if (pageItem.getDistance() != null) {
            sb.append(" ");
            sb.append("\uD83D\uDCCC");
            sb.append(pageItem.getDistanceInMeters().toString());
            sb.append(" m");
        } else if (pageItem.getLocation() != null) {
            sb.append(" ");
            sb.append("\uD83D\uDCCC");
            sb.append(" ");
            sb.append(pageItem.getLocation());
        }

        if (sb.length() > 80) {
            sb = sb.delete(80, sb.length());
        }

        return sb.toString();
    }

    public List<Button> buildButtonsList(
        PageItem pageItem
    ) {
        final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

        final String phone = pageItem.getPhone();
        if (phone != null) {
            final String curatedPhone = "+34" + phone.replaceAll(" ","");
            buttonsBuilder.addCallButton(
                "Call (" + curatedPhone +")",
                curatedPhone
            ).toList();
        }

        buttonsBuilder.addPostbackButton(
            "Book",
            new JSONObject()
                .put("type", PayloadType.venues_book)
                .put("restaurant_id", pageItem.getId())
                .toString()
        ).toList();

        buttonsBuilder.addUrlButton(
            "See at Timeout",
            pageItem.getToWebsite()
        ).toList();

        return
            buttonsBuilder.build();
    }
}
