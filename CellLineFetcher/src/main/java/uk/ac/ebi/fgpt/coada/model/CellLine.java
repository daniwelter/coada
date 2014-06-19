package uk.ac.ebi.fgpt.coada.model;

import java.util.ArrayList;

/**
 * Created by Dani on 16/06/2014.
 */
public class CellLine {

    private String label;
    private String parent;
    private String quality;
    private String disease;
    private String cell_type;
    private String organ;
    private String species;
    private String uri;
    private String p_uri;
    private String q_uri;
    private String d_uri;
    private String ct_uri;
    private String o_uri;
    private String s_uri;
    private String definition;
    private ArrayList<String> syns;

    public CellLine(String label, String uri, String syn){
        this.label = label.replace("\"", "");
        this.uri = uri.replace("\"","");
        syns = new ArrayList<String>();
        if(syn != null){
            addAltTerm(syn);
        }
    } 

    public void addAltTerm(String at){
        syns.add(at);
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<String> getSyns() {
        return syns;
    }

    public void setDefinition(String def){
        this.definition = def;
    }

    public String getDefinition(){return definition;}


    public String getUri() {
        return uri;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent, String uri) {
        this.parent = parent;
        this.p_uri = uri;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality, String uri) {
        this.quality = quality;
        this.q_uri = uri;

    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease, String uri) {
        this.disease = disease;
        this.d_uri = uri;

    }

    public String getCell_type() {
        return cell_type;
    }

    public void setCell_type(String cell_type, String uri) {
        this.cell_type = cell_type;
        this.ct_uri = uri;

    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ, String uri) {
        this.organ = organ;
        this.o_uri = uri;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species, String uri) {
        this.species = species;
        this.s_uri = uri;
    }

    public String getP_uri() {
        return p_uri;
    }

    public String getQ_uri() {
        return q_uri;
    }

    public String getD_uri() {
        return d_uri;
    }

    public String getCt_uri() {
        return ct_uri;
    }

    public String getO_uri() {
        return o_uri;
    }

    public String getS_uri() {
        return s_uri;
    }

}
