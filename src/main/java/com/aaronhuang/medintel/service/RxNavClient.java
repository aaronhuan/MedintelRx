package com.aaronhuang.medintel.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * RxNavClient is the bridge between this application and the RxNav API.
 * 
 * <p> Defines calls to rxnavinabox API in terms of functions</p>
*/
@Component //tells spring to create an instance of this class and manage it as a bean, able to be injected into other classes
public class RxNavClient {
    private final RestClient restClient; //Spring's HTTP client, allows me to make (GET/POST/etc) requests to another server and read response.
    private final String baseUrl; //base url of the api

    /**
     * Constructor for RxNavClient, takes in a RestClient builder and base URL from application properties.
     * 
     * @Value annotation injects the value of the property "rxnav.base-url" from application properties, with a default of "http://localhost:4000/REST" if not set.
     * 
     * @param builder RestClient builder provided by Spring, used to build the RestClient instance for making HTTP requests (setup tool)
     * @param baseUrl base URL for the RxNav API, injected from application properties
     */
    public RxNavClient(RestClient.Builder builder, @Value("${rxnav.base-url:http://localhost:4000/REST}") String baseUrl) {
        this.restClient = builder.build(); //builds the actual RestClient instance using the provided builder
        this.baseUrl = baseUrl;
    }

    /**
     * Given a user input medication name, fetch spelling suggestions from RxNorm API.
     * 
     * @param name medication name to search for
     * @return list of spelling suggestions matching the medication name (can be empty if no matches found)
     */
    public List<String> getSpellingSuggestions(String name){
        RxNavModels.SuggestionResponse response = restClient.get()
            .uri(baseUrl + "/spellingsuggestions.json?name={name}", name) //build the url with query parameter
            .accept(MediaType.APPLICATION_JSON) //want JSON response
            .retrieve() //perform the HTTP request
            .body(RxNavModels.SuggestionResponse.class); //JSON response and convert it to SuggestionResponse DTO using Spring's built-in JSON deserialization

            return response.suggestionGroup().suggestionList().suggestion(); //navigate through the nested records to get the list of suggestions
    }

    /**
     * Given a medication name, fetch corresponding RxCUIs from RxNorm API.
     * 
     * @param name medication name to search for
     * @return list of RxCUIs matching the medication name (can be empty if no matches found)
     */
    public List<String> getRxCuisByName(String name){
        RxNavModels.RxCuiResponse response = restClient.get() //start a get request
            .uri(baseUrl + "/rxcui.json?name={name}", name) //build the url
            .accept(MediaType.APPLICATION_JSON) //want JSON response
            .retrieve() //perform the HTTP request
            .body(RxNavModels.RxCuiResponse.class); //JSON response and convert it to RxCuiResponse DTO using Spring's built-in JSON deserialization

            return response.idGroup().rxnormId(); 
    }

    /**
     * Given an RxCUI, fetch the normalized medication name from RxNorm API.
     * 
     * @param rxCui RxCUI to look up
     * @return normalized medication name for the given RxCUI, or null if not found
     */
    public String getRxNormName(String rxCui){
        RxNavModels.NameResponse response = restClient.get()
            .uri(baseUrl + "/rxcui/{rxCui}/properties.json", rxCui) //build the url
            .accept(MediaType.APPLICATION_JSON) //want JSON response
            .retrieve() //perform the HTTP request
            .body(RxNavModels.NameResponse.class); //JSON response and convert it to NameResponse DTO using Spring's built-in JSON deserialization

            return response.idGroup().name().get(0); //navigate through the nested records to get the normalized name
    }

    /**
     * Object to hold the results of fetching ingredients and brands for a given RxCUI, containing lists of ingredient names and brand names.
     * 
     */
    public record IngredientBrandResult(List<String> ingredients, List<String> brands) {}

    /**
     * Given an RxCUI, fetch related properties from RxNorm API.
     * 
     * @param rxCui RxCUI to look up
     * @return normalized RxNorm Ingredients & Brands for the given RxCUI (empty lists if none found)
     */
    public IngredientBrandResult getPropertiesByRxCui(String rxCui){
        RxNavModels.PropertiesResponse response = restClient.get()
            .uri(baseUrl + "/rxcui/{rxCui}/related.json?tty=IN+BN", rxCui) //build the url
            .accept(MediaType.APPLICATION_JSON) //want JSON response
            .retrieve() //perform the HTTP request
            .body(RxNavModels.PropertiesResponse.class); //JSON response and convert it to PropertiesResponse DTO using Spring's built-in JSON deserialization
            
        Set<String> ingredients = new HashSet<>();
        Set<String> brands = new HashSet<>();

        if (response == null || response.relatedGroup() == null || response.relatedGroup().conceptGroup() == null) {
            return new IngredientBrandResult(List.of(), List.of());
        }

        //navigate through the nested records to get the list of related concepts, then separate them into ingredients and brands based on TTY
        for (RxNavModels.PropertiesResponse.ConceptGroup conceptGroup : response.relatedGroup().conceptGroup()) {
            if (conceptGroup == null || conceptGroup.conceptProperties() == null) {
                continue;
            }
            for (RxNavModels.PropertiesResponse.ConceptProperties conceptProperties : conceptGroup.conceptProperties()) {
                if (conceptProperties == null || conceptProperties.name() == null || conceptProperties.tty() == null) {
                    continue;
                }
                if ("IN".equals(conceptProperties.tty())) {
                    ingredients.add(conceptProperties.name());
                } else if ("BN".equals(conceptProperties.tty())) {
                    brands.add(conceptProperties.name());
                }
            }
        }

        return new IngredientBrandResult(new ArrayList<>(ingredients), new ArrayList<>(brands));
    }


}
