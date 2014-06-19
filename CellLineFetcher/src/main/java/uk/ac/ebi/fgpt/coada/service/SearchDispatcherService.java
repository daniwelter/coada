package uk.ac.ebi.fgpt.coada.service;

import uk.ac.ebi.fgpt.coada.dao.SearchDAO;
import uk.ac.ebi.fgpt.coada.model.CellLine;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Dani on 16/06/2014.
 */
public class SearchDispatcherService {
    private String inFile;
    private SearchDAO searchDAO;
    private DataExtractionService extractionService;
    private String outFile;

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

        System.out.println("About to retrieve all cells from BioPortal");

        ArrayList<CellLine> cellLines = getSearchDAO().retrieveCellLines();

        System.out.println("Retrieved all cell lines from BioPortal");

        printCellLines(cellLines);

//        getExtractionService().extractAxioms(cellLines);
//
//        saveData(cellLines);
    }

    private void saveData(ArrayList<CellLine> cellLines) {

        try{
            FileWriter writer = new FileWriter(outFile);


            String header = "Label\t \tDisease \t \tQuality\t \tCell type\t \tOrganism part\t \tOrganism\t \tDefinition\tSynonyms\n";
            writer.write(header);

            for (CellLine cl : cellLines){
                StringBuilder nl = new StringBuilder();
                nl.append(cl.getLabel() + "\t" + cl.getUri() + "\t");
                nl.append(cl.getDisease() + "\t" + cl.getD_uri() + "\t");
                nl.append(cl.getQuality() + "\t" + cl.getQ_uri() + "\t");
                nl.append(cl.getCell_type() + "\t" + cl.getCt_uri() + "\t");
                nl.append(cl.getOrgan() + "\t" + cl.getO_uri() + "\t");
                nl.append(cl.getSpecies() + "\t" + cl.getS_uri() + "\t");
                nl.append(cl.getDefinition() + "\t");
                for(String syn : cl.getSyns()){
                    nl.append(syn + ", ");
                }
                nl.append("\n");
                writer.write(nl.toString());
            }

            writer.close();

        }
        catch (IOException ex){
            ex.printStackTrace();
        }


    }

    private void printCellLines(ArrayList<CellLine> cellLines) {
        try{
            FileWriter writer = new FileWriter(outFile);


            String header = "Label\t \tSynonyms\n";
            writer.write(header);

            for (CellLine cl : cellLines){
                StringBuilder nl = new StringBuilder();
                nl.append(cl.getLabel() + "\t" + cl.getUri() + "\t");
                for(String syn : cl.getSyns()){
                    nl.append(syn + ", ");
                }
                nl.append("\n");
                writer.write(nl.toString());
            }

            writer.close();

        }
        catch (IOException ex){
            ex.printStackTrace();
        }
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

                if (entry != null && entry != ""){
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

        System.out.println("File read successfully");

        return terms;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
