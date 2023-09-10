package com.ruanyi.mifish.common.utils;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * XMLUtil
 * 
 * @author ruanyi
 * @date 2008-3-18
 */
public final class XMLUtil {

    public static final String INDENT = "  ";

    /**
     * printIndent
     * 
     * @param out
     * @param indent
     */
    public static void printIndent(PrintWriter out, int indent) {
        for (int i = 0; i < indent; i++) {
            out.print(INDENT);
        }
    }

    /*
     * *
     * 
     * @param src
     * 
     * @return
     */
    public static String encode(String src) {
        if (src == null) {
            return "";
        }

        char[] chars = src.toString().toCharArray();
        StringBuffer out = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '&':
                    out.append("&amp;");
                    break;
                case '<':
                    out.append("&lt;");
                    break;
                case '>':
                    out.append("&gt;");
                    break;
                case '\"':
                    out.append("&quot;");
                    break;
                default:
                    out.append(chars[i]);
            }
        }

        return out.toString();
    }

    /*
     * *
     * 
     * @param parent
     * 
     * @param childName
     * 
     * @return
     */
    public static Element getChildElement(Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        int size = children.getLength();

        for (int i = 0; i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (childName.equals(element.getNodeName())) {
                    return element;
                }
            }
        }
        return null;
    }

    public static String getChildValue(Element parent, String childName) {
        Element child = getChildElement(parent, childName);
        if (child == null) {
            throw new RuntimeException("named[" + childName + "] element not exist");
        }
        return getText(child);
    }

    /*
     * *
     * 
     * @param parent
     * 
     * @return
     */
    public static List<Element> getAllChildElements(Element parent) {
        NodeList children = parent.getChildNodes();
        List<Element> list = new ArrayList<Element>();
        int size = children.getLength();

        for (int i = 0; i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                list.add(element);
            }
        }

        return list;
    }

    public static List<Attr> getAllAttribute(Element element) {

        List<Attr> list = new ArrayList<Attr>();

        NamedNodeMap map = element.getAttributes();

        int size = map.getLength();

        for (int i = 0; i < size; i++) {
            Node node = map.item(i);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                list.add((Attr)node);
            }
        }

        return list;

    }

    /**
     * 
     * @param element
     * @return
     */
    public static Map<String, String> getAttributeAsMap(Element element) {
        List<Attr> attrs = getAllAttribute(element);
        Map<String, String> maps = new HashMap<String, String>();
        for (Iterator<Attr> itr = attrs.iterator(); itr.hasNext();) {
            Attr attr = itr.next();
            maps.put(attr.getName(), attr.getValue());
        }
        return maps;
    }

    /*
     * *
     * 
     * @param parent
     * 
     * @param childName
     * 
     * @return
     */
    public static List<Element> getChildElements(Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        List<Element> list = new ArrayList<Element>();
        int size = children.getLength();

        for (int i = 0; i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (childName.equals(element.getNodeName())) {
                    list.add(element);
                }
            }
        }

        return list;
    }

    /*
     * *
     * 
     * @param element
     * 
     * @return
     */
    public static String getText(Element element) {
        StringBuffer sb = new StringBuffer();
        NodeList list = element.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            switch (child.getNodeType()) {
                case Node.CDATA_SECTION_NODE:
                case Node.TEXT_NODE:
                    sb.append(child.getNodeValue());
            }
        }

        return sb.toString().trim();
    }

    /*
     * *
     * 
     * @param attributeName
     * 
     * @param element
     * 
     * @return
     */
    public static String getAttribute(Element element, String attributeName) {
        return element.getAttribute(attributeName).trim();
    }

    public static int getAttrAsInteger(Element element, String name) {
        return Integer.parseInt(getAttribute(element, name));
    }

    public static double getAttrAsDouble(Element element, String name) {
        return Double.parseDouble(getAttribute(element, name));
    }

    public static byte[] getAttrAsBytes(Element element, String name) {
        return getAttributePrototype(element, name).getBytes();
    }

    /**
     * 
     * @param element
     * @param attributeName
     * @return
     */
    public static boolean hasAttribute(Element element, String attributeName) {
        return element.hasAttribute(attributeName);
    }

    public static boolean hasChild(Element root, String childName) {
        return getChildElement(root, childName) != null;
    }

    /*
     * *
     * 
     * @param attributeName
     * 
     * @param element
     * 
     * @return
     */
    public static String getAttributePrototype(Element element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    /*
     * *
     * 
     * @Creator paladin.xiehai
     * 
     * @CreateTime 2008-10-17 $Author$ ${date} $Revision$ $Date$
     */
    public static class DocumentErrorHandler implements ErrorHandler {
        public DocumentErrorHandler() {}

        @Override
        public void warning(SAXParseException exception) throws SAXException {}

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(getMessage(exception));
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(getMessage(exception));
        }

        private String getMessage(SAXParseException exception) {
            return exception.getMessage() + " (" + "line:" + exception.getLineNumber()
                + ((exception.getColumnNumber() > -1) ? ("col:" + exception.getColumnNumber()) : "") + ") ";
        }
    }

    /*
     * *
     * 
     * @Creator paladin.xiehai
     * 
     * @CreateTime 2008-10-17 $Author$ ${date} $Revision$ $Date$
     */
    public static class AllExceptionsErrorHandler implements ErrorHandler {

        private final List<String> exceptions = new ArrayList<String>();

        public AllExceptionsErrorHandler() {

        }

        public List<String> getExceptions() {
            return exceptions;
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {

        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            addMessage(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            addMessage(exception);
        }

        private void addMessage(SAXParseException exception) {
            exceptions.add(exception.getMessage() + " (" + "line:" + exception.getLineNumber()
                + ((exception.getColumnNumber() > -1) ? ("col:" + exception.getColumnNumber()) : "") + ") ");
        }
    }

    /**
     * *
     * 
     * @param is
     * 
     * @param rootName
     * 
     * @return
     * 
     * @throws Exception
     */
    public static Element loadAsElement(InputStream is, String rootName) throws IOException, SAXException {
        return loadAsElement(is, rootName, false, null);
    }

    /**
     * *
     * 
     * @param str
     * 
     * @param rootName
     * 
     * @return
     * 
     * @throws Exception
     */
    public static Element loadAsElement(String str, String rootName) throws IOException, SAXException {
        ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes());
        try {
            return loadAsElement(is, rootName);
        } finally {
            is.close();
        }
    }

    /**
     * 
     * @param is
     * 
     * @param rootName
     * 
     * @param valid
     * 
     * @param entityResolver
     * 
     * @return
     * @throws SAXException
     * 
     * @throws Exception
     */
    public static Element loadAsElement(InputStream is, String rootName, boolean valid, EntityResolver entityResolver)
        throws IOException, SAXException {
        Document doc = loadAsDocument(is, valid, entityResolver);

        NodeList nodes = (NodeList)doc.getElementsByTagName(rootName);

        if ((nodes == null) || (nodes.getLength() == 0)) {
            return null;
        }

        return (Element)nodes.item(0);
    }

    public static Element getElement(Document document, String rootName) {
        NodeList nodes = (NodeList)document.getElementsByTagName(rootName);

        if ((nodes == null) || (nodes.getLength() == 0)) {
            return null;
        }

        return (Element)nodes.item(0);
    }

    /**
     * 
     * @param is
     * 
     * @param rootName
     * 
     * @param valid
     * 
     * @param entityResolver
     * 
     * @return
     * 
     * @throws Exception
     */
    public static List<Element> loadAsElements(InputStream is, String rootName, boolean valid,
        EntityResolver entityResolver) throws Exception {
        try {
            List<Element> r = new ArrayList<Element>();

            Document doc = loadAsDocument(is, valid, entityResolver);
            NodeList nodes = (NodeList)doc.getElementsByTagName(rootName);

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element item = (Element)nodes.item(i);
                    r.add(item);
                }
            }
            return r;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 
     * @param is
     * @param rootName
     * @return
     * @throws Exception
     */
    public static List<Element> loadAsElements(InputStream is, String rootName) throws Exception {
        return loadAsElements(is, rootName, false, null);
    }

    /**
     * 
     * @param is
     * 
     * @param rootNames
     * 
     * @param valid
     * 
     * @param entityResolver
     * 
     * @return
     * 
     * @throws Exception
     */
    public static Element[] loadAsElements(InputStream is, String[] rootNames, boolean valid,
        EntityResolver entityResolver) throws Exception {
        try {
            if ((rootNames == null) || (rootNames.length == 0)) {
                return new Element[] {};
            }
            Element[] elements = new Element[rootNames.length];

            Document doc = loadAsDocument(is, valid, entityResolver);

            for (int i = 0; i < elements.length; i++) {
                NodeList nodes = doc.getElementsByTagName(rootNames[i]);
                if ((nodes != null) && (nodes.getLength() > 0)) {
                    elements[i] = (Element)nodes.item(0);
                } else {
                    elements[i] = null;
                }
            }

            return elements;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }
    }

    public static Document loadAsDocument(byte[] msg) throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream(msg);
        try {
            return loadAsDocument(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable tr) {

                }
            }
        }
    }

    /**
     * 
     * @param is
     * 
     * @param valid
     * 
     * @param entityResolver
     * 
     * @return
     * 
     * @throws Exception
     */
    public static Document loadAsDocument(InputStream is, boolean valid, EntityResolver entityResolver)
        throws IOException, SAXException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;

            // dbf.setNamespaceAware(true);
            // dbf.setFeature("", value)
            try {
                db = dbf.newDocumentBuilder();
                if (valid && (null != entityResolver)) {
                    dbf.setValidating(valid);
                    db.setEntityResolver(entityResolver);
                }
            } catch (ParserConfigurationException e) {
                throw new SAXException("Cannot creating document builder!!!", e);
            }

            db.setErrorHandler(new DocumentErrorHandler());
            Document doc = null;
            doc = db.parse(is);
            return doc;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }
    }

    public static Document loadAsDocument(InputStream is) throws IOException, SAXException {
        return loadAsDocument(is, false, null);
    }

    public static Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        return doc;
    }

    public static Element createElement(Document doc, String tagName) {
        return doc.createElement(tagName);
    }

    public static Element createElement(Document doc, String tagName, String value) {
        Element element = createElement(doc, tagName);
        element.setTextContent(value);
        return element;
    }

    public static Attr createAttr(Document doc, String attrname, String value) {
        Attr attr = doc.createAttribute(attrname);
        attr.setValue(value);
        return attr;
    }

    public static Attr addAttr(Element element, Attr attr) {
        return element.setAttributeNode(attr);
    }

    public static byte[] serialize(Document doc, String charset)
        throws TransformerException, UnsupportedEncodingException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transFormer = transFactory.newTransformer();
        transFormer.setOutputProperty(OutputKeys.ENCODING, charset);
        DOMSource domSource = new DOMSource(doc);
        StringWriter sw = new StringWriter();
        StreamResult xmlResult = new StreamResult(sw);
        transFormer.transform(domSource, xmlResult);
        try {
            return sw.toString().getBytes(charset);
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static byte[] serialize(Document doc) throws TransformerException, UnsupportedEncodingException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transFormer = transFactory.newTransformer();
        transFormer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource domSource = new DOMSource(doc);
        StringWriter sw = new StringWriter();
        StreamResult xmlResult = new StreamResult(sw);
        transFormer.transform(domSource, xmlResult);
        try {
            return sw.toString().getBytes("UTF-8");
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e) {
                }
            }
        }
    }
}