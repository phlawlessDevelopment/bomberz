package com.phlawless.bomberz.core.domain.model.lanterna;

import com.googlecode.lanterna.TextCharacter;
import com.phlawless.bomberz.core.domain.model.Entity;

/**
 * An entity with a Lanterna-specific sprite representation.
 */
public class LanternaEntity extends Entity {
    private TextCharacter sprite;

    public TextCharacter getSprite() {
        return sprite;
    }

    public void setSprite(TextCharacter sprite) {
        this.sprite = sprite;
    }
}
