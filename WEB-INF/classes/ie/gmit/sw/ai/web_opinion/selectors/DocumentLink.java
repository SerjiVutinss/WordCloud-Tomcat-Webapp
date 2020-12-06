package ie.gmit.sw.ai.web_opinion.selectors;

public class DocumentLink implements IEdge {

    private String _url;
    private String _title;
    private String _snippet;

    public DocumentLink(String url, String title, String snippet) {
        this._url = url;
        this._title = title;
        this._snippet = snippet;
    }

    @Override
    public String getIdentifier() {
        return _url;
    }
}
