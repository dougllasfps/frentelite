/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.util;

/**
 *
 * @author DOUGLLASFPS
 */
public class StringUtils {
    public static boolean isNullOrEmpty(String value){
        return value == null || "".equals(value.trim());
    }
}
