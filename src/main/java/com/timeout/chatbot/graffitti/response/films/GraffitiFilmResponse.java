package com.timeout.chatbot.graffitti.response.films;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.response.common.categorisation.GraffittiCategorisation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffitiFilmResponse {

    private Meta meta;
    private Body body;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Meta {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Body {

        private String id;
        private GraffittiCategorisation graffittiCategorisation;
        private String name;
        @JsonProperty("image_url")
        private String imageUrl;
        private Trailer trailer;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public GraffittiCategorisation getGraffittiCategorisation() {
            return graffittiCategorisation;
        }

        public void setGraffittiCategorisation(GraffittiCategorisation graffittiCategorisation) {
            this.graffittiCategorisation = graffittiCategorisation;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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

            public String getUrl() {
                if (html != null) {
                    return
                        html
                            .replace("<iframe width=\"560\" height=\"315\" src=\"", "")
                            .replace("\" frameborder=\"0\" allowfullscreen></iframe>", "");
                } else {
                    return null;
                }
            }
        }
    }
}
