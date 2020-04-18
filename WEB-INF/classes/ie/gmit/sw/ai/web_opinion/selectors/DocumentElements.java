package ie.gmit.sw.ai.web_opinion.selectors;

import java.util.List;

public class DocumentElements {

    private String _title;
    private List<DocumentLink> _links;
    private List<String> _paragraphs;
    private List<String> _headingOnes;

    public DocumentElements(String _title, List<DocumentLink> _links, List<String> _paragraphs, List<String> _headingOnes) {
        this._title = _title;
        this._links = _links;
        this._paragraphs = _paragraphs;
        this._headingOnes = _headingOnes;
    }

    public String getTitle() {
        return _title;
    }

    public List<DocumentLink> getLinks() {
        return _links;
    }

    public List<String> getParagraphs() {
        return _paragraphs;
    }

    public List<String> getHeadingOnes() {
        return _headingOnes;
    }


}
