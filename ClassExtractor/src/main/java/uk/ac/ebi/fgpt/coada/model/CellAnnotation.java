package uk.ac.ebi.fgpt.coada.model;

import java.util.ArrayList;

/**
 * Created by dwelter on 29/04/14.
 */
public class CellAnnotation {

    private String cell_name;
    private String clo_id;
    private String efo_id;
    private String efo_label, clo_label;
    private ArrayList<String> efo_syn, clo_syn;


    public CellAnnotation(String name, String clo, String efo){
        this.cell_name = name;
        this.clo_id =  clo;
        this.efo_id = efo;

        efo_syn = new ArrayList<String>();
        clo_syn = new ArrayList<String>();
    }

    public String getEfo_label() {
        return efo_label;
    }

    public void setEfo_label(String efo_label) {
        this.efo_label = efo_label;
    }

    public String getClo_label() {
        return clo_label;
    }

    public void setClo_label(String clo_label) {
        this.clo_label = clo_label;
    }

    public ArrayList<String> getEfo_syn() {
        return efo_syn;
    }

    public void addEfo_syn(String syn) {
        efo_syn.add(syn);
    }

    public ArrayList<String> getClo_syn() {
        return clo_syn;
    }

    public void addClo_syn(String syn) {
        clo_syn.add(syn);
    }


    public String getCell_name(){
        return cell_name;
    }

    public String getClo_id(){
        return clo_id;
    }

    public String getEfo_id(){
        return efo_id;
    }
}
