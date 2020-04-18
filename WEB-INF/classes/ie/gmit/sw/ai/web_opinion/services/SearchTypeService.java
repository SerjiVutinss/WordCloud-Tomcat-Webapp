package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.models.Enums;
import ie.gmit.sw.ai.web_opinion.models.SearchModel;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class SearchTypeService {

    private static ConcurrentSkipListMap<String, SearchModel> _friendlyModels;
    private static ConcurrentMap<Enums.SearchType, SearchModel> _searchModels;

    private SearchTypeService() {
    }

    private static void build() {
        _searchModels = new ConcurrentSkipListMap<>();
        _friendlyModels = new ConcurrentSkipListMap<>();

        SearchModel wikipediaModel = new SearchModel(Enums.SearchType.WIKIPEDIA, "https://en.wikipedia.org/wiki/");
        SearchModel duckDuckGoModel = new SearchModel(Enums.SearchType.DUCK_DUCK_GO, "https://duckduckgo.com/html/?q=");

        _searchModels.put(Enums.SearchType.WIKIPEDIA, wikipediaModel);
        _friendlyModels.put("wikipedia", wikipediaModel);

        _searchModels.put(Enums.SearchType.DUCK_DUCK_GO, duckDuckGoModel);
        _friendlyModels.put("duckduckgo", duckDuckGoModel);
    }

    public static SearchModel getSearchModel(String friendlyName) {
        if (_friendlyModels == null) build();
        return _friendlyModels.get(friendlyName.toLowerCase());
    }

    public static SearchModel getSearchModel(Enums.SearchType searchType) {
        if (_friendlyModels == null) build();
        return _searchModels.get(searchType);
    }

    public static Enums.SearchType getSearchType(String friendlyName) {
        if (_friendlyModels == null) build();
        return _searchModels.get(friendlyName.toLowerCase()).getSearchType();
    }

}
