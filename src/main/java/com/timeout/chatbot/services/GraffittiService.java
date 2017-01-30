package com.timeout.chatbot.services;

import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.entities.Cuisine;
import com.timeout.chatbot.domain.entities.Subcategory;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
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

    private final List<Cuisine> cuisines;
    private final List<Category> categories;

    @Autowired
    public GraffittiService(
        RestTemplate restTemplate,
        GraffittiEndpoints graffittiEndpoints
    ) {
        graffittiFacetV5Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV5(), GraffittiFacetV5Response.class);

        final GraffittiFacetV4Response graffittiFacetV4Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV4(), GraffittiFacetV4Response.class);

        this.categories = populateCategories(graffittiFacetV4Response);

        this.cuisines = populateCuisines(graffittiFacetV4Response);
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

    public List<GraffittiFacetV5Node> getWhatChildren(
        GraffittiFacetV5Node parentWhat,
        Integer pageNumber
    ) {
        List<GraffittiFacetV5Node> subcategories = new ArrayList<>();

        if (parentWhat.getChildren() != null) {
            for (GraffittiFacetV5Node subcategory : parentWhat.getChildren()) {
                subcategories.add(subcategory);
            }
        }

        return subcategories;
    }

    private List<Cuisine> populateCuisines(GraffittiFacetV4Response v4Response) {
        List<Cuisine> cuisines = new ArrayList<>();

        final GraffittiFacetV4FacetNode facetWhat = getFacetWhat(v4Response);
        final GraffittiFacetV4FacetNode nodeCategoriesOnFaceWhat = getNodeCategoriesOnFaceWhat(facetWhat);
        final GraffittiFacetV4FacetNode nodeRestaurants = getNodeRestaurantsOnNodeCategories(nodeCategoriesOnFaceWhat);

        for (GraffittiFacetV4FacetNode child : nodeRestaurants.getChildren()) {
            if (child.getCount()>0) {
                cuisines.add(
                    new Cuisine(
                        child.getId(),
                        child.getName()
                    )
                );
                if (child.getChildren()!=null && child.getChildren().size()>0) {
                    for (GraffittiFacetV4FacetNode childOfChild : child.getChildren()) {
                        if (childOfChild.getCount()>0) {
                            cuisines.add(
                                new Cuisine(
                                    childOfChild.getId(),
                                    childOfChild.getName()
                                )
                            );
                        }
                    }
                }
            }
        }

        return cuisines;
    }

    private List<Category> populateCategories(GraffittiFacetV4Response v4Response) {
        List<Category> categories = new ArrayList<>();

        final GraffittiFacetV4FacetNode facetWhat = getFacetWhat(v4Response);
        final GraffittiFacetV4FacetNode nodeCategoriesOnFaceWhat = getNodeCategoriesOnFaceWhat(facetWhat);

        final List<GraffittiFacetV4FacetNode> categoryNodes = nodeCategoriesOnFaceWhat.getChildren();
        for (GraffittiFacetV4FacetNode categoryNode : categoryNodes) {
            if (categoryNode.getCount()> 0) {
                List<Subcategory> subcategories = new ArrayList<>();
                for (GraffittiFacetV4FacetNode subcategoryNode : categoryNode.getChildren()) {
                    if (subcategoryNode.getCount()> 0) {
                        subcategories.add(
                            new Subcategory(
                                subcategoryNode.getId(),
                                subcategoryNode.getName(),
                                subcategoryNode.getConcept().getName()
                            )
                        );
                        if (subcategoryNode.getChildren() != null && subcategoryNode.getChildren().size() > 0) {
                            for (GraffittiFacetV4FacetNode subcategoryNodeChild : subcategoryNode.getChildren()) {
                                if (subcategoryNodeChild.getCount()>0) {
                                    subcategories.add(
                                        new Subcategory(
                                            subcategoryNode.getId(),
                                            subcategoryNode.getName(),
                                            subcategoryNode.getConcept().getName()
                                        )
                                    );
                                }
                            }
                        }
                    }
                }

                categories.add(
                    new Category(
                        categoryNode.getId(),
                        categoryNode.getName(),
                        categoryNode.getConcept().getName(),
                        subcategories
                    )
                );
            }
        }

        return categories;
    }

    public Category findCategoryByConceptName(String conceptName) {
        for (Category category : categories) {
            if (category.getConceptName().equalsIgnoreCase(conceptName)) {
                return category;
            }
        }

        return null;
    }

    private GraffittiFacetV4FacetNode getFacetWhat(GraffittiFacetV4Response v4Response) {
        for (GraffittiFacetV4FacetNode facet : v4Response.getBody().getFacets()) {
            if (facet.getId().equalsIgnoreCase("what")) {
                return facet;
            }
        }
        return null;
    }

    private GraffittiFacetV4FacetNode getNodeCategoriesOnFaceWhat(GraffittiFacetV4FacetNode facetWhat) {
        for (GraffittiFacetV4FacetNode facetWhatChild : facetWhat.getChildren()) {
            if (facetWhatChild.getConcept().getName().equalsIgnoreCase("categories")) {
                return facetWhatChild;
            }
        }
        return null;
    }

    private GraffittiFacetV4FacetNode getNodeRestaurantsOnNodeCategories(GraffittiFacetV4FacetNode nodeRestaurants) {
        for (GraffittiFacetV4FacetNode child : nodeRestaurants.getChildren()) {
            if (child.getConcept().getName().equalsIgnoreCase("restaurants (category)")) {
                return child;
            }
        }
        return null;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public class WhatChildrenPage {
        private Integer remainingItems;
        private List<GraffittiFacetV5Node> children;

        public WhatChildrenPage(
            Integer remainingItems,
            List<GraffittiFacetV5Node> children
        ) {
            this.remainingItems = remainingItems;
            this.children = children;
        }

        public Integer getRemainingItems() {
            return remainingItems;
        }

        public List<GraffittiFacetV5Node> getChildren() {
            return children;
        }
    }
}
