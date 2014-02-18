package net.karmats.sverigeleden.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import net.karmats.sverigeleden.client.service.PathService;
import net.karmats.sverigeleden.server.model.LineStyle;
import net.karmats.sverigeleden.server.model.Path;
import net.karmats.sverigeleden.shared.dto.LatLngDto;
import net.karmats.sverigeleden.shared.dto.LineStyleDto;
import net.karmats.sverigeleden.shared.dto.PathDto;

import com.google.appengine.api.datastore.Text;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PathServiceImpl extends RemoteServiceServlet implements PathService {

    private static final long serialVersionUID = 1L;

    private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public void addPath(PathDto dto) {
        PersistenceManager pm = getPersistenceManager();
        try {
            pm.makePersistent(convert(dto));
        } finally {
            pm.close();
        }
    }

    public void addPaths(List<PathDto> pathDtos) {
        List<Path> paths = new ArrayList<Path>();
        for (PathDto pathDto : pathDtos) {
            paths.add(convert(pathDto));
        }
        PersistenceManager pm = getPersistenceManager();
        try {
            pm.makePersistentAll(paths);
        } finally {
            pm.close();
        }
    }

    public PathDto getPath(Long id) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Path.class, "id == " + id);
            Path p = (Path) q.execute();
            return convert(p, true);
        } finally {
            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    public PathDto getPathByName(String name) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Path.class, "name == \"" + name + "\"");
            List<Path> p = (List<Path>) q.execute();
            return convert(p.get(0), true);
        } finally {
            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<PathDto> getPathsByNames(List<String> names) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Path.class, "names.contains(this.name)");
            q.declareParameters("java.util.List names");
            List<Path> p = (List<Path>) q.execute(names);
            return convert(p);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<PathDto> getAllPaths() {
        List<PathDto> result = new ArrayList<PathDto>();
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Path.class);
            q.setOrdering("name ascending");
            List<Path> paths = (List<Path>) q.execute();
            for (Path p : paths) {
                result.add(convert(p, false));
            }
        } finally {
            pm.close();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LineStyleDto> getAllLineStyles() {
        List<LineStyleDto> result = new ArrayList<LineStyleDto>();
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(LineStyle.class);
            q.setOrdering("name ascending");
            List<LineStyle> lineStyle = (List<LineStyle>) q.execute();
            for (LineStyle ls : lineStyle) {
                result.add(convert(ls));
            }
        } finally {
            pm.close();
        }
        return result;

    }

    private PersistenceManager getPersistenceManager() {
        return PMF.getPersistenceManager();
    }

    private LineStyleDto convert(LineStyle ls) {
        LineStyleDto result = new LineStyleDto();
        result.setColor(ls.getColor());
        result.setName(ls.getName());
        result.setWidth(ls.getWidth());
        return result;
    }

    private Path convert(PathDto dto) {
        Path p = new Path();
        p.setDistance(dto.getDistance());
        p.setName(dto.getName());
        p.setApproved(dto.isApproved());
        StringBuilder latLngs = new StringBuilder();
        for (LatLngDto latLngDto : dto.getLatLngs()) {
            latLngs.append(latLngDto.getLat() + "," + latLngDto.getLng() + ";");
        }
        p.setLatLngs(new Text(latLngs.toString()));
        LineStyle lineStyle = new LineStyle();
        lineStyle.setColor(dto.getLineStyle().getColor());
        lineStyle.setName(dto.getLineStyle().getName());
        lineStyle.setWidth(dto.getLineStyle().getWidth());
        p.setStyle(lineStyle);
        return p;
    }

    private PathDto convert(Path p, boolean includeLatLngs) {
        PathDto dto = new PathDto();
        dto.setName(p.getName());
        dto.setDistance(p.getDistance());
        // Lat/Lng
        if (includeLatLngs) {
            List<LatLngDto> latLngDtos = new ArrayList<LatLngDto>();
            String[] latLngs = p.getLatLngs().getValue().split(";");
            for (String latLng : latLngs) {
                String[] ll = latLng.split(",");
                latLngDtos.add(new LatLngDto(Double.parseDouble(ll[0].trim()), Double.parseDouble(ll[1].trim())));
            }

            dto.setLatLngs(latLngDtos);
        }

        // The line style
        dto.setLineStyle(convert(p.getStyle()));

        return dto;
    }

    private List<PathDto> convert(List<Path> paths) {
        List<PathDto> result = new ArrayList<PathDto>();
        for (Path p : paths) {
            result.add(convert(p, true));
        }
        return result;
    }
}
