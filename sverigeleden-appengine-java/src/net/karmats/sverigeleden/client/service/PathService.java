package net.karmats.sverigeleden.client.service;

import java.util.List;

import net.karmats.sverigeleden.shared.dto.LineStyleDto;
import net.karmats.sverigeleden.shared.dto.PathDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("path")
public interface PathService extends RemoteService {

    /**
     * Add a path to the db.
     * 
     * @param path
     *            The path to store
     */
    void addPath(PathDto path);

    /**
     * Add a list of paths to the db.
     * 
     * @param paths
     *            The paths to store
     */
    void addPaths(List<PathDto> paths);

    /**
     * Get a path by id
     * 
     * @param id
     *            The id of the path
     * @return a {@link PathDto}
     */
    PathDto getPath(Long id);

    /**
     * Get a path by its name.
     * 
     * @param name
     *            The name of the path
     * @return A {@link PathDto}
     */
    PathDto getPathByName(String name);
    
    /**
     * Get paths by its names.
     * 
     * @param names The names on the paths
     * @return A list with {@link PathDto}
     */
    List<PathDto> getPathsByNames(List<String> names);

    /**
     * Get all paths in db.
     * 
     * @return A list of {@link PathDto}s
     */
    List<PathDto> getAllPaths();
    
    /**
     * Get all line styles.
     * 
     * @return a list of {@link LineStyleDto}s
     */
    List<LineStyleDto> getAllLineStyles();
}
