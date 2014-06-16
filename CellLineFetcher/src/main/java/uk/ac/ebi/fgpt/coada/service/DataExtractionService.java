package uk.ac.ebi.fgpt.coada.service;

import uk.ac.ebi.fgpt.coada.dao.OntologyDAO;

/**
 * Created by Dani on 16/06/2014.
 */
public class DataExtractionService {
    private OntologyDAO ontologyDAO;

    public void setOntologyDAO(OntologyDAO ontologyDAO) {
        this.ontologyDAO = ontologyDAO;
    }

    public OntologyDAO getOntologyDAO() {
        return ontologyDAO;
    }
}
