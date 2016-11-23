/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virilcorp.converter;

import br.com.virilcorp.frentelite.exception.ConverterException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author DOUGLLASFPS
 */
public class NumericConverter {
    
    public static BigDecimal convertBigDecimal(String value, boolean threeDigits) {
        if(value == null || "".equals(value.trim()) ){
            return null;
        }
        
        try {
            value = ( threeDigits ? getDecimalFormatThreeDigits() : getDecimalFormat()).parse(value.trim()).toString();
            return new BigDecimal( value );
        } catch (ParseException e) {
            throw new ConverterException("Ocorreu um erro" + e.getMessage());
        }
    }
    
    public static BigDecimal convertBigDecimal(String value) {
        return convertBigDecimal(value, false);
    }
    
    public static Integer convertInteger(String value){
        try{
            return Integer.valueOf(value);
        }catch(NumberFormatException e){
            throw new ConverterException("Ocorreu um erro" + e.getMessage());
        }
    }
    
    public static DecimalFormat getDecimalFormat(){
        DecimalFormat nf = (DecimalFormat) NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.DOWN);
        nf.setGroupingUsed(true);
        return nf;
    }
    
    public static DecimalFormat getDecimalFormatThreeDigits(){
        DecimalFormat nf = (DecimalFormat) NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.DOWN);
        nf.setGroupingUsed(true);
        return nf;
    }
    
    public static String formatNumber(BigDecimal number, int digits){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(digits);
        nf.setMinimumFractionDigits(digits);
        nf.setRoundingMode(RoundingMode.DOWN);
        nf.setGroupingUsed(true);
        String formated =  nf.format(number);
        return formated.substring(digits, formated.length());
    }
    
    public static String formatCurrent(BigDecimal number){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.DOWN);
        nf.setGroupingUsed(true);
        String formated =  nf.format(number);
        return formated.substring(2, formated.length());
    }
    
    public static String formatWeight(BigDecimal number){
        return formatNumber(number, 3);
    }
    
    public static void main (String[] a) throws ParseException{
        //testar essa bagaça
        System.out.println(convertBigDecimal("11.000.123.230,23"));
    }
}
