package com.timeout.chatbot.services;

import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4Facet;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetChild;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4Response;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Response;
import com.timeout.chatbot.graffitti.uri.GraffittiEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class GraffittiService {

    private final GraffittiFacetV5Response graffittiFacetV5Response;
    private final List<GraffittiFacetV4FacetChild> whereItems = new ArrayList<>();

    @Autowired
    public GraffittiService(
        RestTemplate restTemplate,
        GraffittiEndpoints graffittiEndpoints
    ) {
        graffittiFacetV5Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV5(), GraffittiFacetV5Response.class);

        final GraffittiFacetV4Response graffittiFacetV4Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV4(), GraffittiFacetV4Response.class);

        final List<GraffittiFacetV4Facet> facets = graffittiFacetV4Response.getBody().getFacets();
        for (GraffittiFacetV4Facet facetV4Facet : facets) {
            if (facetV4Facet.getId().equalsIgnoreCase("where")) {
                for (GraffittiFacetV4FacetChild child : facetV4Facet.getChildren()) {
                    if (!child.getId().equalsIgnoreCase("canned-near_here")) {
                        whereItems.add(child);
                    }
                }
            }
        }
    }

    public List<GraffittiFacetV5Node> getPrimaryCategories() {
        return
            graffittiFacetV5Response.getBody().getFacets().getWhat().getChildren();
    }

//    public GraffittiFacetV5Node getCategoryPrimaryByName(String name) {
//        for (GraffittiFacetV5Node categoryPrimary : getPrimaryCategories()) {
//            if (categoryPrimary.getName().equals(name)) {
//                return categoryPrimary;
//            }
//        }
//
//        return null;
//    }

//    public GraffittiFacetV5Node getCategoryPrimaryById(String id) {
//        for (GraffittiFacetV5Node categoryPrimary : getPrimaryCategories()) {
//            if (categoryPrimary.getId().equals(id)) {
//                return categoryPrimary;
//            }
//        }
//
//        return null;
//    }

//    public List<CategorySecondary> getSecondaryCategories(CategoryPrimary primaryCategoryPrimary) {
//        for (CategoryPrimary categoryPrimary : getPrimaryCategories()) {
//            if (categoryPrimary.getId().equals(primaryCategoryPrimary.getId())) {
//                return categoryPrimary.getSecondaryCategories();
//            }
//
//        }
//
//        return null;
//    }

//    public GraffittiFacetV5Response getGraffittiFacetV5Response() {
//        return graffittiFacetV5Response;
//    }

    public GraffittiFacetV5Node getGraffittiFacetV5NodeById(String id) {
        for (GraffittiFacetV5Node parentNode : graffittiFacetV5Response.getBody().getFacets().getWhat().getChildren()) {
            for (GraffittiFacetV5Node childNode : parentNode.getChildren()) {
                if (childNode.getId().equalsIgnoreCase(id)) {
                    return childNode;
                }
            }
        }

        return null;
    }

    public GraffittiFacetV5Node getGraffittiFacetV5NodeByIdByName(String name) {
        for (GraffittiFacetV5Node parentNode : graffittiFacetV5Response.getBody().getFacets().getWhat().getChildren()) {
            for (GraffittiFacetV5Node childNode : parentNode.getChildren()) {
                if (childNode.getName().equalsIgnoreCase(name)) {
                    return childNode;
                }
            }
        }

        return null;
    }

    public List<GraffittiFacetV5Node> getWhatSubcategories(
        GraffittiFacetV5Node parentWhat
    ) {
        List<GraffittiFacetV5Node> subcategories = new ArrayList<>();

        if (parentWhat.getChildren() != null) {
            for (GraffittiFacetV5Node subcategory : parentWhat.getChildren()) {
                subcategories.add(subcategory);
            }
        }

        return subcategories;
    }

    public List<GraffittiFacetV4FacetChild> getWhereItems() {
        return whereItems;
    }
}
