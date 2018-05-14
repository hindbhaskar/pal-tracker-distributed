package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String registrationServerEndpoint;
    private final Map<Long, ProjectInfo> cache = new ConcurrentHashMap<>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations= restOperations;
        this.registrationServerEndpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo infoObj=restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class);
        cache.put(projectId,infoObj);
        return infoObj;
    }

    public ProjectInfo getProjectFromCache(long projectId) {

        ProjectInfo infoFromCache=cache.get(projectId);

        return infoFromCache;
    }

}
