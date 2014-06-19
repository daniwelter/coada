package uk.ac.ebi.fgpt.coada.service;

import org.semanticweb.owlapi.model.OWLClass;
import uk.ac.ebi.fgpt.coada.dao.OntologyDAO;
import uk.ac.ebi.fgpt.coada.model.CellLine;

import java.util.ArrayList;
import java.util.HashMap;

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


    public void extractAxioms(ArrayList<CellLine> cellLines) {
        getOntologyDAO().loadOntology();
        HashMap<String, OWLClass> allCLfromOnt = getOntologyDAO().getAllClasses();
        System.out.println("Acquired all children of cell line");


        for(CellLine cellLine : cellLines){
            String label = cellLine.getLabel();
            System.out.println("Processing cell line " + label);

            if(allCLfromOnt.get(label) != null){
                OWLClass cls = allCLfromOnt.get(label);

                getOntologyDAO().getAxioms(cls, cellLine);
            }
        }
    }
}
