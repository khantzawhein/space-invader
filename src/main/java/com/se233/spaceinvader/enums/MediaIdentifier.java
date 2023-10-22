package com.se233.spaceinvader.enums;

public enum MediaIdentifier {
    BG_SOUND("bgSound"),
    SHOOT_SOUND("shootSound"),
    INVADER_KILLED("invaderKilledSound"),
    EXPLOSION_SOUND("explosionSound"),
    BOSS_SOUND("bossSound"),
    GAME_OVER_SOUND("gameOverSound"),
    WIN_SOUND("winSound");

    private String name;

    MediaIdentifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
