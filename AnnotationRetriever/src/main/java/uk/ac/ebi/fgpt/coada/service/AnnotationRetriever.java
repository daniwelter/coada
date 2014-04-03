package uk.ac.ebi.fgpt.coada.service;

import uk.ac.ebi.fgpt.coada.exception.DispatcherException;

import java.util.Collection;

/**
 * Created by dwelter on 27/03/14.
 */
public class AnnotationRetriever {
    private AnnotatorDispatcherService dispatcherService;
    private DBLoader dbLoader;

    public void setDispatcherService(AnnotatorDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    public AnnotatorDispatcherService getDispatcherService() {
        return dispatcherService;
    }

    public void setDbLoader(DBLoader dbLoader) {
        this.dbLoader = dbLoader;
    }

    public DBLoader getDbLoader() {
        return dbLoader;
    }


    public void dispatchAnnotator(){
        Collection<String> cellData = getDbLoader().retrieveAllEntries();

        try {
            getDispatcherService().dispatchAnnotatorQuery(cellData);
            System.out.println("Annotation retrieval complete");
    //        outputResults(results);

        } catch (DispatcherException e) {
            e.printStackTrace();
        }
    }

//    public void outputResults(List<BPAnnotation> data){
//        for(BPAnnotation annotation: data){
//            String id = annotation.getAnnotatedClass().get_id();
//            String ontology = annotation.getAnnotatedClass().getLinks().getOntology();
//            int from = annotation.getAnnotations().get(0).getFrom();
//            int to = annotation.getAnnotations().get(0).getTo();
//            String matchType = annotation.getAnnotations().get(0).getMatchType();
//            String text = annotation.getAnnotations().get(0).getText();
//
//            System.out.println(id + "\t" + ontology+ "\t" + from+ "\t" + to + "\t" + matchType + "\t" + text);
//        }
//    }
}
