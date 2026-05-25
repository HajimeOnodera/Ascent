package fun.ascent.skyblock.bazaar;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BazaarData {

    public List<BazaarEntry> bazaarData;

    public BazaarEntry getFarming() {
        for(BazaarEntry bz : bazaarData){
            if(bz.id.equals("FARMING")) return bz;
        }
        return null;
    }

}
