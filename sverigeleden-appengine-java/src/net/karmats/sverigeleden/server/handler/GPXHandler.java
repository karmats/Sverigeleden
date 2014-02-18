package net.karmats.sverigeleden.server.handler;

import java.util.ArrayList;
import java.util.List;

import net.karmats.sverigeleden.shared.dto.LatLngDto;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GPXHandler extends DefaultHandler {

    private static final String WPT_TAG = "wpt";
    private static final String RTE_TAG = "rte";
    private static final String TRK_TAG = "rte";

    private List<LatLngDto> latLngs = new ArrayList<LatLngDto>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals(WPT_TAG) || qName.equals(RTE_TAG) || qName.equals(TRK_TAG)) {
            latLngs.add(new LatLngDto(Double.parseDouble(attributes.getValue("lat")), Double.parseDouble(attributes.getValue("lon"))));
        }
    }

    public List<LatLngDto> getLatLngs() {
        return latLngs;
    }
}
