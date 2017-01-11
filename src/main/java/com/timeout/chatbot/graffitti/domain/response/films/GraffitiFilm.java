package com.timeout.chatbot.graffitti.domain.response.films;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timeout.chatbot.graffitti.domain.response.categorisation.Categorisation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffitiFilm {
    private Categorisation categorisation;
    private Trailer trailer;

    public Categorisation getCategorisation() {
        return categorisation;
    }

    public void setCategorisation(Categorisation categorisation) {
        this.categorisation = categorisation;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public class Trailer {
        private String html;

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getUrl() { return "https://www.youtube.com/embed/0f1_fbdB6RQ"; }
    }
}
