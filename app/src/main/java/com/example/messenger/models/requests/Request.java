package com.example.messenger.models.requests;

import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


class Request {

    Request()
    {
    }

    org.w3c.dom.Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
