package org.kim.vierGewinnt.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum Messages {

    PREFIX(mm("<b><gradient:#62D660:#09A9B7>Vier-Gewinnt</gradient></b> <gray>| ")),
    ACCEPT_CMD_USAGE(PREFIX.getMessage().append(mm("<color:#09A9B7>Verwende: /accept <Spielername>"))),
    MAIN_CMD_USAGE(PREFIX.getMessage().append(mm("<color:#09A9B7>Verwende: /viergewinnt <Spieler/List>"))),
    REQUEST_NOT_FOUND(PREFIX.getMessage().append(mm("<color:#bf2a37>Anfrage konnte nicht gefunden werden!"))),
    PLAYER_NOT_FOUND(PREFIX.getMessage().append(mm("<color:#bf2a37>Spieler konnte nicht gefunden werden!"))),
    PLAYER_NOT_PLAYING(PREFIX.getMessage().append(mm("<color:#bf2a37>Dieser Spieler spielt aktuell kein <gradient:#62D660:#09A9B7>Vier-Gewinnt</gradient>Vier-Gewinnt!"))),
    NO_OPEN_GAMES(PREFIX.getMessage().append(mm("<color:#bf2a37>Aktuell gibt es keine <gradient:#62D660:#09A9B7>Vier-Gewinnt</gradient>Vier-Gewinnt <color:#bf2a37>Spiele."))),
    REQUEST_ERROR(PREFIX.getMessage().append(mm("<color:#bf2a37>Du kannst nicht gegen dich selbst Spielen!"))),
    GAME_WON(PREFIX.getMessage().append(mm("<color:#77ff73> Du hast gewonnen! </color>"))),
    GAME_LOOSE(PREFIX.getMessage().append(mm("<color:#ff4d4d> Du hast verloren! </color>"))),
    GAME_TIE(PREFIX.getMessage().append(mm("<color:#D7C000> Unentschieden")));







    @Getter
    private final Component message;

    Messages(Component message) {
        this.message = message;
    }

    public static Component mm(String msg) {
        return MiniMessage.miniMessage().deserialize(msg);
    }
}
