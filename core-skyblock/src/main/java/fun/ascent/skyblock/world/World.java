package fun.ascent.skyblock.world;


import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;

import java.io.File;

public class World {

    private InstanceContainer instance;
    public File worldFile;
    public File templateFile;

    public String name;
    public boolean save;

    public World(String name,File worldFile,File templateFile, boolean save){
        this.worldFile = worldFile;
        this.templateFile = templateFile;
        this.save = save;
        this.name = name;
    }

    public InstanceContainer getInstance(){
        if(instance == null){
            registerWorld();
        }
        return instance;
    }

    public void registerWorld(){
        if(instance != null) return;
        instance = WorldHandler.createWorld(this);
    }
}
