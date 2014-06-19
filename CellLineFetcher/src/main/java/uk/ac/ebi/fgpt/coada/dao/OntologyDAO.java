package uk.ac.ebi.fgpt.coada.dao;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.ebi.fgpt.coada.model.CellLine;
import uk.ac.ebi.fgpt.coada.model.OntologyConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Dani on 16/06/2014.
 */
public class OntologyDAO {
    private String cloResource;

    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLOntology clOntology;
    private String cloURI;
    private OWLReasoner reasoner;

    public void setCloResource(String cloResource) {
        this.cloResource = cloResource;
    }

    public String getCloResource() {
        return cloResource;
    }

    public void setCloURI(String cloURI) {
        this.cloURI = cloURI;
    }

    public String getCloURI() {
        return cloURI;
    }

    public OntologyDAO(){
         this.manager = OWLManager.createOWLOntologyManager();
         this.factory = manager.getOWLDataFactory();
    }

    public void loadOntology(){
        try {
            IRI iri = IRI.create(getCloURI());
            File file = new File(getCloResource());
            manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(file)));

            this.clOntology = manager.loadOntology(iri);

            createReasoner();

            System.out.println("Ontology loaded successfully");

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    public void createReasoner(){
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(
                progressMonitor);
        this.reasoner = reasonerFactory.createReasoner(clOntology, config);

        System.out.println("Reasoner created");
    //    reasoner.precomputeInferences();
//        System.out.println("Inferences precomputed");
    }

    public HashMap<String, OWLClass> getAllClasses(){
        HashMap<String, OWLClass> allCLinOnt = new HashMap<String, OWLClass>();

        OWLClass cellLine = factory.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/EFO_0000322"));

        Set<OWLClass> cellLines = reasoner.getSubClasses(cellLine,false).getFlattened();

        for(OWLClass cls : cellLines){
            OWLAnnotationProperty label = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());


            // Get the annotations on the class that use the label property
            for (OWLAnnotation annotation : cls.getAnnotations(clOntology, label)) {
                if (annotation.getValue() instanceof OWLLiteral) {
                    OWLLiteral val = (OWLLiteral) annotation.getValue();

                    allCLinOnt.put(val.getLiteral(), cls);
                }
            }

        }

        return  allCLinOnt;
    }


    public void getAxioms(OWLClass cls, CellLine cellLine) {

         for(OWLAnnotation def : cls.getAnnotations(clOntology,factory.getOWLAnnotationProperty(IRI.create(OntologyConstants.DEFINITION)))){
             if(def.getValue() instanceof OWLLiteral){
                 cellLine.setDefinition(((OWLLiteral) def.getValue()).getLiteral());
             }
         }

         if(cellLine.getDefinition().equals("")){
             for(OWLAnnotation def : cls.getAnnotations(clOntology,factory.getOWLAnnotationProperty(IRI.create(OntologyConstants.COMMENT)))){
                 if(def.getValue() instanceof OWLLiteral){
                     cellLine.setDefinition(((OWLLiteral) def.getValue()).getLiteral());
                 }
             }
         }

         for(OWLAnnotation syn : cls.getAnnotations(clOntology, factory.getOWLAnnotationProperty(IRI.create(OntologyConstants.ALTERNATIVE_TERM)))){
             if(syn.getValue() instanceof OWLLiteral)     {
                 cellLine.addAltTerm(((OWLLiteral) syn.getValue()).getLiteral());
             }
         }

         for (OWLSubClassOfAxiom ax : clOntology.getSubClassAxiomsForSubClass(cls)) {
            OWLClassExpression superCls = ax.getSuperClass();

             Set<OWLClass> axioms = superCls.getClassesInSignature();


             if(axioms.size() == 1){
                 OWLClass rhs = (OWLClass) axioms.toArray()[0];
                 Set<OWLClass> parents = reasoner.getSuperClasses(rhs,false).getFlattened();

                 if(parents.contains(factory.getOWLClass(IRI.create(OntologyConstants.SEX)))){
                     for(OWLAnnotation lab : rhs.getAnnotations(clOntology,factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())))   {
                         if (lab.getValue() instanceof OWLLiteral) {
                             cellLine.setQuality(((OWLLiteral) lab.getValue()).getLiteral(), rhs.getIRI().toString());
                         }

                     }
                 }
                 else if (parents.contains(factory.getOWLClass(IRI.create(OntologyConstants.DISEASE)))){
                     for(OWLAnnotation lab : rhs.getAnnotations(clOntology,factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())))   {
                         if (lab.getValue() instanceof OWLLiteral) {
                             cellLine.setDisease(((OWLLiteral) lab.getValue()).getLiteral(), rhs.getIRI().toString());
                         }

                     }
                 }

             }
             else{
                 for(OWLClass rhs : axioms){
                     Set<OWLClass> parents = reasoner.getSuperClasses(rhs,false).getFlattened();
                     if(parents.contains(factory.getOWLClass(IRI.create(OntologyConstants.CELL_TYPE)))){
                         for(OWLAnnotation lab : rhs.getAnnotations(clOntology,factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())))   {
                             if (lab.getValue() instanceof OWLLiteral) {
                                 cellLine.setCell_type(((OWLLiteral) lab.getValue()).getLiteral(), rhs.getIRI().toString());
                             }

                         }
                     }
                     else if (parents.contains(factory.getOWLClass(IRI.create(OntologyConstants.ORGANISM_PART)))){
                         for(OWLAnnotation lab : rhs.getAnnotations(clOntology,factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())))   {
                             if (lab.getValue() instanceof OWLLiteral) {
                                 cellLine.setOrgan(((OWLLiteral) lab.getValue()).getLiteral(), rhs.getIRI().toString());
                             }

                         }
                     }
                     else if (parents.contains(factory.getOWLClass(IRI.create(OntologyConstants.ORGANISM)))){
                         for(OWLAnnotation lab : rhs.getAnnotations(clOntology,factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())))   {
                             if (lab.getValue() instanceof OWLLiteral) {
                                 cellLine.setSpecies(((OWLLiteral) lab.getValue()).getLiteral(), rhs.getIRI().toString());
                             }

                         }
                     }


                 }

             }

        }
    }
}
