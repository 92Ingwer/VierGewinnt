package org.kim.vierGewinnt.services;

import java.util.HashMap;
import java.util.UUID;

public class GameService {
    public static HashMap<UUID, UUID> requestHashMap = new HashMap<>(); // if player creates a request, the target player is stored in this hashmap
    public static HashMap<UUID, UUID> spectateVierGewinntHashMap = new HashMap<>(); // if player wants to spectate a game, the spectatedPlayer UUID is stored in this hashmap
}
