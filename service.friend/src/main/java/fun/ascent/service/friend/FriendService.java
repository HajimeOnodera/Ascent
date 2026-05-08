package fun.ascent.service.friend;

import fun.ascent.common.service.AbstractSkyBlockService;
import fun.ascent.common.service.ServiceType;
import fun.ascent.common.service.SkyBlockService;
import fun.ascent.common.service.redis.ServiceToServerManager;

public class FriendService extends AbstractSkyBlockService {
    public FriendService() {
        super(ServiceType.FRIEND, "fun.ascent.service.friend.endpoints");
    }

    static void main() {
        String mongoUri = System.getenv("MONGODB_URI");
        if (mongoUri == null) mongoUri = "mongodb://127.0.0.1:27017";
        
        new FriendDatabase(null).connect(mongoUri);

        FriendCache.startExpirationChecker();
        
        ServiceToServerManager.initialize(ServiceType.FRIEND);

        SkyBlockService.init(new FriendService());
    }
}
