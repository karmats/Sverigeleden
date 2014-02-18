package net.karmats.sverigeleden.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.karmats.sverigeleden.client.service.MapKmlService;
import net.karmats.sverigeleden.server.factory.PathServiceFactory;
import net.karmats.sverigeleden.server.handler.GPXHandler;
import net.karmats.sverigeleden.server.handler.KMLHandler;
import net.karmats.sverigeleden.server.util.KMLUtil;
import net.karmats.sverigeleden.shared.dto.LatLngDto;
import net.karmats.sverigeleden.shared.dto.LineStyleDto;
import net.karmats.sverigeleden.shared.dto.PathDto;

import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MapKmlServiceImpl extends RemoteServiceServlet implements MapKmlService {
    private static final long serialVersionUID = 1L;

    @Override
    public void generateKmlBasedOnDb() {
        // TODO Auto-generated method stub

    }

    @Override
    public String generateKml(String name) {
        PathDto path = PathServiceFactory.getPathService().getPathByName(name);
        System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
        return KMLUtil.genereateKml(Arrays.asList(path));

    }

    @Override
    public void parseKmlFromUrlAndSaveToDb(String urlString) {
        System.out.println("Parsing xml from " + urlString);
        try {
            List<PathDto> pathDtos = parseXml(new URL(urlString).openStream(), new KMLHandler());
            PathServiceFactory.getPathService().addPaths(pathDtos);
        } catch (Exception e) {
            // TODO Handle exception
        }
    }

    @Override
    public String generateKml(List<String> paths) {
        List<PathDto> pathDtos = PathServiceFactory.getPathService().getPathsByNames(paths);
        return KMLUtil.genereateKml(pathDtos);
    }

    @Override
    public void parseRawKmlAndSaveToDb(String kml) {
        List<PathDto> pathDtos = null;
        try {
            pathDtos = parseXml(new ByteArrayInputStream(kml.getBytes()), new KMLHandler());
        } catch (Exception e) {
            try {
                pathDtos = parseXml(new ByteArrayInputStream(kml.getBytes()), new GPXHandler());
            } catch (Exception e2) {
                // TODO Error handling
            }
        }
        if (pathDtos != null) {
            PathServiceFactory.getPathService().addPaths(pathDtos);
        }
    }

    @Override
    public List<PathDto> parseRawKml(String kml) {
        try {
            return parseXml(new ByteArrayInputStream(kml.getBytes()), new KMLHandler());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<PathDto> parseXml(InputStream stream, DefaultHandler handler) throws Exception {
        try {
            // Parse kml file
            SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
            sp.parse(stream, handler);
            if (handler instanceof KMLHandler) {
                // Add paths from the parse result
                Map<String, String> paths = ((KMLHandler) handler).getPaths();
                List<PathDto> pathDtos = new ArrayList<PathDto>();
                for (String name : paths.keySet()) {
                    // The line style for the path
                    String lineStyleUrl = ((KMLHandler) handler).getStyleUrlMap().get(name);
                    LineStyleDto lineStyle = ((KMLHandler) handler).getLineStyleMap().get(lineStyleUrl.substring(1));
                    // Path dto
                    PathDto pathDto = new PathDto();
                    pathDto.setName(name);
                    pathDto.setApproved(false);
                    pathDto.setLineStyle(lineStyle);

                    List<LatLngDto> latLngs = new ArrayList<LatLngDto>();
                    String coords = paths.get(name);
                    String[] coordArray = coords.split("\n");
                    for (String coord : coordArray) {
                        String[] latLngArray = coord.split(",");
                        if (latLngArray.length >= 3) {
                            latLngs.add(new LatLngDto(Double.parseDouble(latLngArray[1].trim()), Double.parseDouble(latLngArray[0].trim())));
                        }
                    }
                    pathDto.setLatLngs(latLngs);
                    pathDtos.add(pathDto);
                    // Save to db
                }
                return pathDtos;
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        return null;
    }

}
