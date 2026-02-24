package com.aaronhuang.medintel.service;

import java.util.List;

/**
 * DTOs for RxNav REST API responses.
 * 
 * <p>
 * 
 * DTO = Data Transfer Object -> simple object acting as a structured data container
 * 
 *  think of it as a labeled box that holds the structured data, nothing more. 
 */
public final class RxNavModels {

    /**
     * record automatically generates DTO constructors and more
     */

    /**
     * DTO for RxNav response when looking up spelling suggestions for a medication name.
     *
     * <p><strong>Example request:</strong></p>
     * <pre>
     * GET /REST/spellingsuggestions.json?name=liptor
     * </pre>
     *
     * <p><strong>Example response:</strong></p>
     * <pre>
     * {
     *   "suggestionGroup": {
     *     "name": null,
     *     "suggestionList": {
     *       "suggestion": [
     *         "lipitor"
     *       ]
     *     }
     *   }
     * }
     * </pre>
     */
    public record SuggestionResponse(SuggestionGroup suggestionGroup){
        public record SuggestionGroup(SuggestionList suggestionList){};
        public record SuggestionList(List<String> suggestion){};
    }


    /**
     * DTO for RxNav response when looking up RxCUI by medication name.
     * 
     * 
     * <p><strong>Example request:</strong></p>
     * <pre>
     * GET /REST/rxcui.json?name=aspirin
     * </pre>
     *
     * <p><strong>Example response:</strong></p>
     * <pre>
     * {
     *   "idGroup": {
     *     "rxnormId": ["1191"]
     *   }
     * }
     * </pre>
     */
    public record RxCuiResponse(IdGroup idGroup){
        public record IdGroup(List<String> rxnormId){};
    }

    /**
     * DTO for RxNav response when looking up normalized medication name by RxCUI.
     * 
     * <p><strong>Example request:</strong></p>
     * <pre>
     * GET /REST/rxcui/1191.json
     * </pre>
     * 
     * <p><strong>Example response:</strong></p>
     * <pre>
     * {
     *  "idGroup": {
     *   "name": ["Aspirin"]
     *  }
     * }
     * </pre>
     */
    public record NameResponse(IdGroup idGroup){
        public record IdGroup(List<String> name){};
    }


    /**
     * DTO for RxNav response when looking up related concepts by TTY.
     *
     * <p><strong>Example request:</strong></p>
     * <pre>
     * GET /REST/rxcui/161/related.json?tty=IN+SBD
     * </pre>
     *
     * <p><strong>Example response:</strong></p>
     * <pre>
     * {
     *   "relatedGroup": {
     *     "conceptGroup": [
     *       {
     *         "tty": "IN",
     *         "conceptProperties": [
     *           {
     *             "name": "acetaminophen",
     *             "tty": "IN"
     *           }
     *         ]
     *       }
     *     ]
     *   }
     * }
     * </pre>
     */

    public record PropertiesResponse(RelatedGroup relatedGroup){
        public record RelatedGroup(List<ConceptGroup> conceptGroup){}
        public record ConceptGroup(List<ConceptProperties> conceptProperties){}
        public record ConceptProperties(String name, String tty){}
    }

    private RxNavModels() {} // common java pattern to prevent instantiation of a utility class that only contains static members (in this case, the record types)
    //we don't actually want to create instances of RxNavModels, we just want the nested record types inside it 
    //we can remove this private & compiler will make a default public constructor, but it has no real use 
}
