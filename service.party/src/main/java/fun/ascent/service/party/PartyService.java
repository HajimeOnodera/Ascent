package fun.ascent.service.party;

import fun.ascent.common.service.SkyBlockService;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.common.service.redis.ServiceToServerManager;

import java.util.List;
import java.util.stream.Collectors;

public class PartyService implements SkyBlockService {

    static void main() {
        String mongoUri = System.getenv("MONGODB_URI");
        if (mongoUri == null) mongoUri = "mongodb://127.0.0.1:27017";
        
        new PartyDatabase(null).connect(mongoUri);

        PartyCache.startExpirationChecker();
        
        ServiceToServerManager.initialize(ServiceType.PARTY);

        SkyBlockService.init(new PartyService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.PARTY;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("fun.ascent.service.party.endpoints", ServiceEndpoint.class).collect(Collectors.toList());
    }
}
