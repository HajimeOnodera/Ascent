package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.collections.vars.CollectionCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CollectionMap {

    public EnumMap<CollectionCategory, List<CollData>> collectionMap = new EnumMap<>(CollectionCategory.class);

    public CollectionMap(List<CollData> collections){
        collections.forEach(data -> {
            List<CollData> list =  collectionMap.getOrDefault(data.collection.category,new ArrayList<>());
            list.add(data);
            collectionMap.put(data.collection.category,list);
        });
    }

    public CollectionMap(){
        collectionMap = generateCollections();
    }

    private EnumMap<CollectionCategory, List<CollData>> generateCollections() {
        //TODO: Generate Collections
        return  new EnumMap<>(CollectionCategory.class);
    }

}
