package me.hugo.savethekweebecs.game;

import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

public class WorldPropertyManager {

    public SlimePropertyMap DEFAULT_PROPERTIES;

    public WorldPropertyManager() {
        DEFAULT_PROPERTIES = new SlimePropertyMap();

        DEFAULT_PROPERTIES.setValue(SlimeProperties.DIFFICULTY, "normal");
        DEFAULT_PROPERTIES.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        DEFAULT_PROPERTIES.setValue(SlimeProperties.ALLOW_MONSTERS, false);
    }

}
