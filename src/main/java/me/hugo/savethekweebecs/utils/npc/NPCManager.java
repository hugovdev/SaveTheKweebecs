package me.hugo.savethekweebecs.utils.npc;

import java.util.HashMap;

public class NPCManager {

    private HashMap<Integer, NPC> npcRelation;

    public NPCManager() {
        npcRelation = new HashMap<>();
    }

    public void registerNPC(NPC npc) {
        npcRelation.put(npc.getEntityID(), npc);
    }

    public NPC getNPC(int id) {
        return npcRelation.get(id);
    }

}
