package uk.ac.ebi.fgpt.coada.dao;

/**
 * Created by Dani on 16/06/2014.
 */
public class SearchDAO {
    private String targetOntology;
    private String stopWords;

    public void setTargetOntology(String targetOntology) {
        this.targetOntology = targetOntology;
    }

    public String getTargetOntology() {
        return targetOntology;
    }

    public void setStopWords(String stopWords) {
        this.stopWords = stopWords;
    }

    public String getStopWords() {
        return stopWords;
    }
}
