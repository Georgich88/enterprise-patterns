package com.isaev.ee.transformview.transformers.people;

import com.isaev.ee.transformview.transformers.Transformer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlToXhtmlTransformer implements Transformer<String, String> {

    private static final String PATH_TO_STYLE_XSL = "/makehtml.xsl";

    private final String document;

    public XmlToXhtmlTransformer(String document) {
        this.document = document;
    }

    @Override
    public String transform() throws IOException, TransformerException {

       try (InputStream styleTemplate = XmlToXhtmlTransformer.class.getResourceAsStream(PATH_TO_STYLE_XSL)){
           var styleSource = new StreamSource(styleTemplate);
           var transformer = TransformerFactory.newInstance().newTransformer(styleSource);
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty(OutputKeys.METHOD, "xml");
           StreamSource source = new StreamSource(new StringReader(document));
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           transformer.transform(source, result);
           return writer.toString();
       }

    }
}

