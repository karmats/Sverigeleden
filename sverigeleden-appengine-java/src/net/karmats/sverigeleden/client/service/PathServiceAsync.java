package net.karmats.sverigeleden.client.service;

import java.util.List;

import net.karmats.sverigeleden.shared.dto.LineStyleDto;
import net.karmats.sverigeleden.shared.dto.PathDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PathServiceAsync {

    void addPath(PathDto path, AsyncCallback<Void> callback);

    void addPaths(List<PathDto> path, AsyncCallback<Void> callback);

    void getPath(Long id, AsyncCallback<PathDto> callback);

    void getAllPaths(AsyncCallback<List<PathDto>> callback);

    void getPathByName(String name, AsyncCallback<PathDto> callback);

    void getPathsByNames(List<String> names, AsyncCallback<List<PathDto>> callback);

    void getAllLineStyles(AsyncCallback<List<LineStyleDto>> callback);

}
