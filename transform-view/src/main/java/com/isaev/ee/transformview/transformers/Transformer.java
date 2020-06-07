package com.isaev.ee.transformview.transformers;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface Transformer<T, R> {

    R transform() throws IOException, TransformerConfigurationException, TransformerException;
}
