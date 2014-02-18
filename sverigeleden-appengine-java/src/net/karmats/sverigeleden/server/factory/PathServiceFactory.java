package net.karmats.sverigeleden.server.factory;

import net.karmats.sverigeleden.client.service.PathService;
import net.karmats.sverigeleden.server.PathServiceImpl;

public class PathServiceFactory {

    private static PathServiceImpl pathService = new PathServiceImpl();
    
    public static PathService getPathService() {
        return pathService;
    }
}
