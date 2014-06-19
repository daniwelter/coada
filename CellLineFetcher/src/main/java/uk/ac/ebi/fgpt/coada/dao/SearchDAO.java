package uk.ac.ebi.fgpt.coada.dao;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import uk.ac.ebi.fgpt.coada.model.CellLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Dani on 16/06/2014.
 */
public class SearchDAO {
    private String targetOntology;
    private ArrayList<String> cellNames;
    private ArrayList<CellLine> cellLines;
    private String searchString, ontologyParameter;

    static final ObjectMapper mapper = new ObjectMapper();
    private static String API_KEY;


    public void setTargetOntology(String targetOntology) {
        this.targetOntology = targetOntology;
    }

    public String getTargetOntology() {
        return targetOntology;
    }


    public void setCellLineNames(ArrayList<String> cellNames){
        this.cellNames = cellNames;
    }


    public SearchDAO(){
        this.cellLines = new ArrayList<CellLine>();

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResource("bioportal.properties").openStream());
            this.searchString = properties.getProperty("bioportal.root")
                    .concat(properties.getProperty("bioportal.search"));

            this.ontologyParameter = properties.getProperty("bioportal.ontologies");

            API_KEY = properties.getProperty("bioportal.apikey");

         }
        catch (IOException e) {
            throw new RuntimeException(
                    "Unable to create dispatcher service: failed to read Annotator.properties resource", e);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Unable to create dispatcher service: you must provide a integer query interval " +
                            "in minutes (Annotator.query.interval.mins)", e);
        }
    }

    public ArrayList<CellLine> retrieveCellLines(){
        if(targetOntology != ""){
            ontologyParameter = ontologyParameter.concat(getTargetOntology());
        }

        int fail = 0;
        int pass = 0;

        for (String term : cellNames) {
            try {
                System.out.println("Dispatching " + searchString + URLEncoder.encode(term,"ISO-8859-1") + ontologyParameter);
                JsonNode searchResult = jsonToNode(get(searchString + URLEncoder.encode(term,"ISO-8859-1") + ontologyParameter)).get("collection");


                if(searchResult.toString().equals("[]")){
                    System.out.println("No result found for cell line " + term);
                    fail++;
                }
                else{
                    pass++;
                    for (JsonNode result : searchResult){
                        String name = result.get("prefLabel").toString();
                        String uri = result.get("@id").toString();
                        String syn = "";

                        if(!name.equals(term)){
                            syn = term;
                        }

                        cellLines.add(new CellLine(name,uri,syn));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Search returned results for " + pass + " terms and failed to find anything for " + fail + " out of " + cellNames.size() + " terms");

        return cellLines;
    }


    private static JsonNode jsonToNode(String json) {
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static String get(String urlToGet) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
            conn.setRequestProperty("Accept", "application/json");
//            if(conn.getResponseCode() == 200){
                rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
//            }
//            else{
//                result = null;
//                System.out.println("No result for " + urlToGet);
//
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
