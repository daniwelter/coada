package uk.ac.ebi.fgpt.coada.service;

import uk.ac.ebi.fgpt.coada.exception.DispatcherException;
import uk.ac.ebi.fgpt.coada.model.CellAnnotation;

import java.util.Collection;

/**
 * Created by dwelter on 27/03/14.
 */
public class ClassRetriever {
    private ExtractorDispatcherService dispatcherService;
    private DataBaseLoader dataBaseLoader;

    public void setDispatcherService(ExtractorDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    public ExtractorDispatcherService getDispatcherService() {
        return dispatcherService;
    }

    public void setDataBaseLoader(DataBaseLoader dataBaseLoader) {
        this.dataBaseLoader = dataBaseLoader;
    }

    public DataBaseLoader getDataBaseLoader() {
        return dataBaseLoader;
    }


    public void dispatchAnnotator(){
        Collection<CellAnnotation> cellData = getDataBaseLoader().retrieveAllEntries();

        try {
            getDispatcherService().retrieveData(cellData);
            System.out.println("Annotation retrieval complete");

        } catch (DispatcherException e) {
            e.printStackTrace();
        }
    }
}
