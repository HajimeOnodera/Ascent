package fun.ascent.common.service;

import fun.ascent.common.service.redis.ServiceEndpoint;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSkyBlockService implements SkyBlockService {
    private final ServiceType type;
    private final String endpointPackage;

    protected AbstractSkyBlockService(ServiceType type, String endpointPackage) {
        this.type = type;
        this.endpointPackage = endpointPackage;
    }

    @Override
    public ServiceType getType() {
        return type;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage(endpointPackage, ServiceEndpoint.class).collect(Collectors.toList());
    }
}
