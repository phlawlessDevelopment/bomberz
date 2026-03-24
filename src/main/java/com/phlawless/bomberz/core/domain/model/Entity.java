package com.phlawless.bomberz.core.domain.model;

import com.phlawless.bomberz.core.domain.math.Vector2D;

public abstract class Entity {
	Vector2D position;

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}
}
