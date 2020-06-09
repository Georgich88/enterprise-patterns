package com.isaev.ee.serializedlob.database.converters;

import com.isaev.ee.serializedlob.organization.Department;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Converts XML-string into a department object.
 *
 * @author Georgy Isaev
 */
public class XmlToDepartment {

    final String departmentString;
    private static final Logger logger = Logger.getLogger(XmlToDepartment.class);
    public static final String MESSAGE_ERROR_CANNOT_CONVERT_XML_STRING = "Cannot convert XML string %s into a department";

    public XmlToDepartment(String department) {
        this.departmentString = department;
    }

    /**
     * Generates department from the XML-string
     *
     * @return the converted department or null if XML is not filled
     */
    public Department toDepartment() {

        Document doc = convertStringToDocument(this.departmentString);
        if (doc == null) {
            return null;
        }
        Element root = doc.getDocumentElement();
        return generateDepartment(root);
    }

    private Department generateDepartment(Element root) {

        Department department = new Department(root.getAttribute("name"));
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {

            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Department subsidiary = generateDepartment(((Element) node));
                department.addSubsidiary(subsidiary);
            }

        }
        return department;
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlStr)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            var message = String.format(MESSAGE_ERROR_CANNOT_CONVERT_XML_STRING, xmlStr);
            logger.error(message, e);
            return null;
        }
    }
}
