package uk.ac.ebi.fgpt.coada.service;

import uk.ac.ebi.fgpt.coada.dao.SearchDAO;
import uk.ac.ebi.fgpt.coada.model.CellLine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dani on 16/06/2014.
 */
public class SearchDispatcherService {
    private String inFile;
    private SearchDAO searchDAO;
    private DataExtractionService extractionService;

    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    public String getInFile() {
        return inFile;
    }

    public void setSearchDAO(SearchDAO searchDAO) {
        this.searchDAO = searchDAO;
    }

    public SearchDAO getSearchDAO() {
        return searchDAO;
    }

    public void setExtractionService(DataExtractionService extractionService) {
        this.extractionService = extractionService;
    }

    public DataExtractionService getExtractionService() {
        return extractionService;
    }

    public void findCellLines() {
        ArrayList<String> inputCellLines = loadNames();
        getSearchDAO().setCellLineNames(inputCellLines);

        ArrayList<CellLine> cellLines = getSearchDAO().retrieveCellLines();

        getExtractionService().extractAxioms(cellLines);

    }


    public ArrayList<String> loadNames(){
        ArrayList<String> terms = new ArrayList<String>();
        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader(getInFile()));
            System.out.println("Opened file " + getInFile());
        }

        catch (FileNotFoundException fnfe)
        {
            System.out.println("Error opening file '" + getInFile() + "'");
        }
        boolean done = false;

        while(!done){
            try{
                String entry = reader.readLine();

                if (entry != null){
                    terms.add(entry);
                }
                else{
                    done = true;
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return terms;
    }
}
