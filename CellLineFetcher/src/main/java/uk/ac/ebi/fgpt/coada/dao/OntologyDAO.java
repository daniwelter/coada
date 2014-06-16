package uk.ac.ebi.fgpt.coada.dao;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Created by Dani on 16/06/2014.
 */
public class OntologyDAO {
    private String cloResource;

    private OWLOntologyManager manager;
    private OWLDataFactory factory;

    public void setCloResource(String cloResource) {
        this.cloResource = cloResource;
    }

    public String getCloResource() {
        return cloResource;
    }


    public OntologyDAO(){
        this.manager = OWLManager.createOWLOntologyManager();
        this.factory = manager.getOWLDataFactory();

    }
}
