package net.karmats.sverigeleden.server.handler;

import java.util.HashMap;
import java.util.Map;

import net.karmats.sverigeleden.shared.dto.LineStyleDto;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class KMLHandler extends DefaultHandler {

    private static final String PLACEMARK_QNAME = "Placemark";
    private static final String NAME_QNAME = "name";
    private static final String COORDINATES_QNAME = "coordinates";
    private static final String STYLE_URL_QNAME = "styleUrl";
    private static final String STYLE_QNAME = "Style";
    private static final String COLOR_QNAME = "color";
    private static final String WIDTH_QNAME = "width";

    private Map<String, String> paths;
    private Map<String, String> styleUrlMap;
    private Map<String, LineStyleDto> lineStyleMap;
    private String currentPathName;
    private String currentStyleUrlName;
    private LineStyleDto currentLineStyle;

    private boolean inPlaceMark;
    private boolean inName;
    private boolean inCoordinates;
    private boolean inStyleUrl;
    private boolean inStyle;
    private boolean inStyleColor;
    private boolean inStyleWidth;

    private StringBuilder stringBuffer;

    @Override
    public void startDocument() throws SAXException {
        paths = new HashMap<String, String>();
        styleUrlMap = new HashMap<String, String>();
        lineStyleMap = new HashMap<String, LineStyleDto>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(PLACEMARK_QNAME)) {
            inPlaceMark = true;
            stringBuffer = new StringBuilder();
        } else if (qName.equalsIgnoreCase(NAME_QNAME)) {
            inName = true;
        } else if (qName.equalsIgnoreCase(COORDINATES_QNAME)) {
            inCoordinates = true;
        } else if (qName.equalsIgnoreCase(STYLE_URL_QNAME)) {
            inStyleUrl = true;
        } else if (qName.equalsIgnoreCase(STYLE_QNAME)) {
            inStyle = true;
            currentLineStyle = new LineStyleDto();
            currentLineStyle.setName(attributes.getValue("id"));
        } else if (qName.equalsIgnoreCase(COLOR_QNAME)) {
            inStyleColor = true;
        } else if (qName.equalsIgnoreCase(WIDTH_QNAME)) {
            inStyleWidth = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (inName && inPlaceMark) {
            currentPathName = new String(ch, start, length);
        } else if (inPlaceMark && inCoordinates) {
            stringBuffer.append(new String(ch, start, length));
        } else if (inPlaceMark && inStyleUrl) {
            currentStyleUrlName = new String(ch, start, length);
        } else if (inStyle && inStyleColor) {
            currentLineStyle.setColor(new String(ch, start, length));
        } else if (inStyle && inStyleWidth) {
            currentLineStyle.setWidth(Integer.parseInt(new String(ch, start, length)));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(PLACEMARK_QNAME)) {
            inPlaceMark = false;
            paths.put(currentPathName, stringBuffer.toString());
            styleUrlMap.put(currentPathName, currentStyleUrlName);
        } else if (qName.equalsIgnoreCase(NAME_QNAME)) {
            inName = false;
        } else if (qName.equalsIgnoreCase(COORDINATES_QNAME)) {
            inCoordinates = false;
        } else if (qName.equalsIgnoreCase(STYLE_URL_QNAME)) {
            inStyleUrl = false;
        } else if (qName.equalsIgnoreCase(STYLE_QNAME)) {
            inStyle = false;
            lineStyleMap.put(currentLineStyle.getName(), currentLineStyle);
        } else if (qName.equalsIgnoreCase(COLOR_QNAME)) {
            inStyleColor = false;
        } else if (qName.equalsIgnoreCase(WIDTH_QNAME)) {
            inStyleWidth = false;
        }
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public Map<String, String> getStyleUrlMap() {
        return styleUrlMap;
    }

    public Map<String, LineStyleDto> getLineStyleMap() {
        return lineStyleMap;
    }

}
