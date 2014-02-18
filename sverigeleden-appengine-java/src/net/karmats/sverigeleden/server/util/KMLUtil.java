package net.karmats.sverigeleden.server.util;

import java.io.StringWriter;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.karmats.sverigeleden.shared.dto.LatLngDto;
import net.karmats.sverigeleden.shared.dto.LineStyleDto;
import net.karmats.sverigeleden.shared.dto.PathDto;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KMLUtil {

    /**
     * Generetes a kml file based on a {@link PathDto}
     * 
     * @param paths
     *            A collection of {@link PathDto}s to generate a kml file for.
     * 
     * @return A kml string
     */
    public static String genereateKml(Collection<PathDto> paths) {
        System.setProperty("javax.xml.transform.TransformerFactory",  "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("kml");
            root.setAttribute("xmlns", "http://earth.google.com/kml/2.2");
            document.appendChild(root);
            Element el = document.createElement("Document");
            // Styles
            for (PathDto path : paths) {
                el.appendChild(createStyleElement(document, path.getLineStyle()));
            }
            // Placemarks
            for (PathDto path : paths) {
                el.appendChild(createPlacemarkElement(document, path));
            }
            root.appendChild(el);

            // Transform the object to an xml string representation
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            return sw.toString();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Element createPlacemarkElement(Document doc, PathDto path) {
        // <Placemark>
        Element placemarkElement = doc.createElement("Placemark");
        // <name>
        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(path.getName()));
        // <description>
        Element descriptionElement = doc.createElement("description");
        // styleUrl
        Element styleUrlElement = doc.createElement("styleUrl");
        styleUrlElement.appendChild(doc.createTextNode("#" + path.getLineStyle().getName()));

        // <ExtendedData>
        Element extendedDataElement = doc.createElement("ExtendedData");
        // <Data>
        Element dataElement = doc.createElement("Data");
        dataElement.setAttribute("name", "_SnapToRoads");
        // <value>
        Element dataValueElement = doc.createElement("value");
        dataValueElement.appendChild(doc.createTextNode("true"));
        // </Data>
        dataElement.appendChild(dataValueElement);
        // </ExtendedData>
        extendedDataElement.appendChild(dataElement);

        // <LineString>
        Element lineStringElement = doc.createElement("LineString");
        // <tesseleate>
        Element tesselateElement = doc.createElement("tesselate");
        tesselateElement.appendChild(doc.createTextNode("1"));
        // <coordinates>
        Element coordinatesElement = doc.createElement("coordinates");
        StringBuilder sb = new StringBuilder();
        for (LatLngDto latLng : path.getLatLngs()) {
            sb.append(latLng.getLng() + ",");
            sb.append(latLng.getLat() + ",");
            sb.append("0.0");
            sb.append("\n");
        }
        coordinatesElement.appendChild(doc.createTextNode(sb.toString()));
        // </LineString>
        lineStringElement.appendChild(tesselateElement);
        lineStringElement.appendChild(coordinatesElement);

        // </Placemark>
        placemarkElement.appendChild(nameElement);
        placemarkElement.appendChild(descriptionElement);
        placemarkElement.appendChild(styleUrlElement);
        placemarkElement.appendChild(extendedDataElement);
        placemarkElement.appendChild(lineStringElement);
        return placemarkElement;
    }

    private static Element createStyleElement(Document doc, LineStyleDto lineStyle) {
        // <Style>
        Element styleElement = doc.createElement("Style");
        styleElement.setAttribute("id", lineStyle.getName());
        // <LineStyle>
        Element lineStyleElement = doc.createElement("LineStyle");
        // <color>
        Element colorElement = doc.createElement("color");
        colorElement.appendChild(doc.createTextNode("#" + lineStyle.getColor()));
        // <width>
        Element widthElement = doc.createElement("width");
        widthElement.appendChild(doc.createTextNode(lineStyle.getWidth().toString()));

        lineStyleElement.appendChild(colorElement);
        lineStyleElement.appendChild(widthElement);
        styleElement.appendChild(lineStyleElement);
        return styleElement;
    }
}
