package com.se233.spaceinvader.models;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Key {
    private final HashMap<KeyCode,Boolean> keys;

    public Key() {
        keys = new HashMap<>();
    }

    public void add(KeyCode key) {
        keys.put(key, true);
    }

    public void remove(KeyCode key) {
        keys.put(key, false);
    }

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key,false);
    }
}
