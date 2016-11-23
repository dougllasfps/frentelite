/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author DOUGLLASFPS
 */
public enum Medida {
    
	NONE(null),
    KG("KG"),
    UND("UND"),
    LT("LT");
    
    private String desc;

    private Medida(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return getDesc() == null ? "Selecione" : getDesc();
    }
    
    public static ObservableList<Medida> getObservableList(){
        return FXCollections.observableArrayList(values());
    }
}
