package net.karmats.sverigeleden.client.service;

import java.util.List;

import net.karmats.sverigeleden.shared.dto.PathDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapKmlServiceAsync {

    void generateKmlBasedOnDb(AsyncCallback<Void> callback);

    void generateKml(String name, AsyncCallback<String> callback);

    void parseKmlFromUrlAndSaveToDb(String url, AsyncCallback<Void> callback);

    void generateKml(List<String> paths, AsyncCallback<String> callback);

    void parseRawKmlAndSaveToDb(String kml, AsyncCallback<Void> callback);

    void parseRawKml(String kml, AsyncCallback<List<PathDto>> callback);

}
