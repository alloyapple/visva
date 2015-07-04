package com.visva.android.app.funface.model;

public class EffectItem {
    public String  textEffect;
    public int     effectId;
    public boolean isSelected;

    public EffectItem(String textEffect, int effectId) {
        this.textEffect = textEffect;
        this.effectId = effectId;
        isSelected = false;
    }
}
