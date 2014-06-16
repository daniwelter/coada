package uk.ac.ebi.fgpt.coada.service;

/**
 * Created by Dani on 16/06/2014.
 */
public class SearchDispatcherService {
    private String inFile;
    private String searchDAO;
    private DataExtractionService extractionService;

    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    public String getInFile() {
        return inFile;
    }

    public void setSearchDAO(String searchDAO) {
        this.searchDAO = searchDAO;
    }

    public String getSearchDAO() {
        return searchDAO;
    }

    public void setExtractionService(DataExtractionService extractionService) {
        this.extractionService = extractionService;
    }

    public DataExtractionService getExtractionService() {
        return extractionService;
    }
}
