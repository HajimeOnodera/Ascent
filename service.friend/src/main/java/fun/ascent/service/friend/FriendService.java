package fun.ascent.service.friend;

import fun.ascent.common.service.SkyBlockService;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.service.redis.ServiceEndpoint;
import fun.ascent.common.service.redis.ServiceToServerManager;

import java.util.List;
import java.util.stream.Collectors;

public class FriendService implements SkyBlockService {

    static void main() {
        String mongoUri = System.getenv("MONGODB_URI");
        if (mongoUri == null) mongoUri = "mongodb://127.0.0.1:27017";
        
        new FriendDatabase(null).connect(mongoUri);

        FriendCache.startExpirationChecker();
        
        ServiceToServerManager.initialize(ServiceType.FRIEND);

        SkyBlockService.init(new FriendService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.FRIEND;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("fun.ascent.service.friend.endpoints", ServiceEndpoint.class).collect(Collectors.toList());
    }
}
