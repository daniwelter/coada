package uk.ac.ebi.fgpt.coada.service;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.coada.exception.DispatcherException;
import uk.ac.ebi.fgpt.coada.model.CellAnnotation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by dwelter on 27/03/14.
 */
public class ExtractorDispatcherService {
//    private HttpContext httpContext;
//    private HttpClient httpClient;

    private String cloString, efoString, ontologyString;

    static private String API_KEY;
    static final ObjectMapper mapper = new ObjectMapper();

    private Logger log = LoggerFactory.getLogger(getClass());
    private String outFile;


    public ExtractorDispatcherService(){

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResource("extractor.properties").openStream());
            this.ontologyString = properties.getProperty("bioportal.root")
                    .concat(properties.getProperty("bioportal.ontology"));

            this.cloString = ontologyString.concat(properties.getProperty("bioportal.clo.root"));
            this.efoString = ontologyString.concat(properties.getProperty("bioportal.efo.root"));

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

    public void retrieveData(Collection<CellAnnotation> cellData) throws DispatcherException{
        ArrayList<CellAnnotation> results = new ArrayList<CellAnnotation>();

        Iterator<CellAnnotation> cellIterator = cellData.iterator();

        while(cellIterator.hasNext()){
            CellAnnotation current = cellIterator.next();

            if(current.getClo_id() != null){
                String id = current.getClo_id();
                String query = cloString.concat(id);
                JsonNode result = jsonToNode(get(query));
                if(result != null){
                    processCLOResult(result, current);
                }
                else{
                    System.out.println("Nothing returned for " + query);
                }

            }

            if(current.getEfo_id() != null){
                String id = current.getEfo_id();
                String query = efoString.concat(id);
                JsonNode result = jsonToNode(get(query));
                if(result != null){
                    processEFOResult(result, current);
                }
                else{
                    System.out.println("Nothing returned for " + query);
                }

            }
            results.add(current);
        }

        saveResults(results, getOutFile());

    }


    private static void processCLOResult(JsonNode ontologyClass, CellAnnotation cellAnnotation){
        cellAnnotation.setClo_label(ontologyClass.get("prefLabel").toString());

        Iterator<JsonNode> synonyms = ontologyClass.get("synonym").getElements();
        while (synonyms.hasNext()){
            cellAnnotation.addClo_syn(synonyms.next().toString());
        }
    }

    private static void processEFOResult(JsonNode ontologyClass, CellAnnotation cellAnnotation){
        cellAnnotation.setEfo_label(ontologyClass.get("prefLabel").toString());

        Iterator<JsonNode> synonyms = ontologyClass.get("synonym").getElements();
        while (synonyms.hasNext()){
            cellAnnotation.addEfo_syn(synonyms.next().toString());
        }
    }

    private static void saveResults(ArrayList<CellAnnotation> results, String fileName) {

        try {
            File f = new File(fileName);

            Writer out= null;

            out = new BufferedWriter(new FileWriter(f));

            out.write("Cell Name \t CLO ID \t CLO label \t CLO syns \t EFO ID \t EFO Label \t EFO syns \n");

            for(CellAnnotation result : results){
                ArrayList<String> clo_syns = result.getClo_syn();
                String clo = "";
                for(String syn : clo_syns){
                    clo = clo.concat(syn + ";");
                }

                ArrayList<String> efo_syns = result.getEfo_syn();
                String efo = "";
                for(String syn : efo_syns){
                    efo = efo.concat(syn + ";");
                }

                out.write(result.getCell_name() + "\t"
                        + result.getClo_id() + "\t"
                        + result.getClo_label() + "\t"
                        + clo + "\t"
                        + result.getEfo_id() + "\t"
                        + result.getEfo_label() + "\t"
                        + efo + "\n");
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode jsonToNode(String json) {
        if(json != null){
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
        else{
            return null;
        }
    }

    private static String get(String urlToGet) {
        System.out.println("Trying to retrieve " + urlToGet);
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        urlToGet=urlToGet.replace("\"","");
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
            conn.setRequestProperty("Accept", "application/json");

            if(conn.getResponseCode() == 200){
                rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            }
            else{
                result = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public String getOutFile() {
        return outFile;
    }
}
