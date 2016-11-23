/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.frentelite.util;

import java.math.BigDecimal;

/**
 *
 * @author DOUGLLASFPS
 */
public class MonetaryUtils {
    public static BigDecimal valueOrZero(BigDecimal value){
        return value == null ? BigDecimal.ZERO : value;
    }
}
