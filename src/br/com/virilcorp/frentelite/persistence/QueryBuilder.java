/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.persistence;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DOUGLLASFPS
 */
public class QueryBuilder {
    
    private Map<String,Object> params;
    private StringBuffer sb;
    
    public QueryBuilder(String query) {
        this.params = new HashMap<>();
        this.sb = new StringBuffer(query == null ? "" : query);
    }

    public QueryBuilder() {
        this(null);
    }
    
    public String getQuery(){
        return sb.toString();
    }
    
    public QueryBuilder append(String piece){
        sb.append(piece);
        return this;
    }
    
    public QueryBuilder addParam(String name, Object value){
        params.put(name, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }   
}
