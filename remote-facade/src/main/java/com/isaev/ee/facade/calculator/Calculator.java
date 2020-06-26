/**
 * Calculator.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.isaev.ee.facade.calculator;

public interface Calculator extends javax.xml.rpc.Service {

    String getCalculatorSoapAddress();

    CalculatorSoap getCalculatorSoap() throws javax.xml.rpc.ServiceException;

    CalculatorSoap getCalculatorSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
