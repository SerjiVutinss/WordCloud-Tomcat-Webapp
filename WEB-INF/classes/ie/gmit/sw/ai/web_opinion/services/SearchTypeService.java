package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.models.Enums;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class SearchTypeService {

    private static ConcurrentSkipListMap<String, ie.gmit.sw.ai.web_opinion.models.WebSearchType> _friendlyModels;
    private static ConcurrentMap<Enums.WebSearchType, ie.gmit.sw.ai.web_opinion.models.WebSearchType> _searchModels;

    private SearchTypeService() {
    }

    private static void build() {
        _searchModels = new ConcurrentSkipListMap<>();
        _friendlyModels = new ConcurrentSkipListMap<>();

        ie.gmit.sw.ai.web_opinion.models.WebSearchType wikipediaModel = new ie.gmit.sw.ai.web_opinion.models.WebSearchType(Enums.WebSearchType.WIKIPEDIA, "https://en.wikipedia.org/wiki/");
        ie.gmit.sw.ai.web_opinion.models.WebSearchType duckDuckGoModel = new ie.gmit.sw.ai.web_opinion.models.WebSearchType(Enums.WebSearchType.DUCK_DUCK_GO, "https://duckduckgo.com/html/?q=");

        _searchModels.put(Enums.WebSearchType.WIKIPEDIA, wikipediaModel);
        _friendlyModels.put("wikipedia", wikipediaModel);

        _searchModels.put(Enums.WebSearchType.DUCK_DUCK_GO, duckDuckGoModel);
        _friendlyModels.put("duckduckgo", duckDuckGoModel);
    }

    public static ie.gmit.sw.ai.web_opinion.models.WebSearchType getSearchModel(String friendlyName) {
        if (_friendlyModels == null) build();
        return _friendlyModels.get(friendlyName.toLowerCase());
    }

    public static ie.gmit.sw.ai.web_opinion.models.WebSearchType getSearchModel(Enums.WebSearchType webSearchType) {
        if (_friendlyModels == null) build();
        return _searchModels.get(webSearchType);
    }

    public static Enums.WebSearchType getSearchType(String friendlyName) {
        if (_friendlyModels == null) build();
        return _searchModels.get(friendlyName.toLowerCase()).getSearchType();
    }

}
