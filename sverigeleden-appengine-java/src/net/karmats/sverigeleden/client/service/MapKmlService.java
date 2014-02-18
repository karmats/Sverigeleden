package net.karmats.sverigeleden.client.service;

import java.util.List;

import net.karmats.sverigeleden.shared.dto.PathDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mapKml")
public interface MapKmlService extends RemoteService {

    /**
     * Generates a kml file based on the values in db.
     */
    void generateKmlBasedOnDb();

    /**
     * Generates a kml based on a collection of {@link PathDto}s.
     * 
     * @param paths
     *            The paths to generate a kml for
     * @return A kml string
     */
    String generateKml(List<String> paths);

    /**
     * Generates a kml file for a single route.
     * 
     * @param name
     *            The name of the path to generate kml for
     */
    String generateKml(String name);

    /**
     * Parse and save the kml to db
     * 
     * @param url
     *            The url where the kml is located
     */
    void parseKmlFromUrlAndSaveToDb(String url);

    /**
     * Parse and save raw kml to db
     * 
     * @param kml
     *            The kml as string to parse
     */
    void parseRawKmlAndSaveToDb(String kml);

    /**
     * Parse a kml string and return the result as a list with {@link PathDto}s
     * 
     * @param kml
     *            The kml as string to parse
     * @return A list
     */
    List<PathDto> parseRawKml(String kml);
}
