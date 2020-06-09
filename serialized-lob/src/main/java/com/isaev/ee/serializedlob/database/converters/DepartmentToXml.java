package com.isaev.ee.serializedlob.database.converters;

import com.isaev.ee.serializedlob.organization.Department;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Converts a department object into XML-string
 *
 * @author Georgy Isaev
 */
public class DepartmentToXml {

    public static final String MESSAGE_ERROR_CONVERTING_TO_STRING = "Error converting to String";
    private final Department department;

    private static final Logger logger = Logger.getLogger(DepartmentToXml.class);

    public static final String DEPARTMENT_TAG_NAME = "department";
    public static final String DEPARTMENT_NAME_ATTRIBUTE_NAME = "name";
    public static final String MESSAGE_ERROR_DEPARTMENT_IS_NULL = "Department should not be null";

    public DepartmentToXml(Department department) {
        this.department = department;
    }

    public static String documentToString(Document document) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException e) {
            throw new RuntimeException(MESSAGE_ERROR_CONVERTING_TO_STRING, e);
        }
    }

    public String toXmlString() throws ParserConfigurationException {
        return documentToString(this.toXml());
    }

    public Document toXml() throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement(DEPARTMENT_TAG_NAME);
        if (this.department == null) {
            document.appendChild(root);
            return document;
        }
        root.setAttribute(DEPARTMENT_NAME_ATTRIBUTE_NAME, this.department.getName());
        document.appendChild(root);
        setSubsidiaries(this.department, document, root);

        return document;
    }

    private void setSubsidiaries(Department department, Document document, Element root) {
        department.getSubsidiaries().forEach(subsidiary -> {
            Element child = document.createElement(DEPARTMENT_TAG_NAME);
            child.setAttribute(DEPARTMENT_NAME_ATTRIBUTE_NAME, subsidiary.getName());
            root.appendChild(child);
            setSubsidiaries(subsidiary, document, child);
        });
    }
}
