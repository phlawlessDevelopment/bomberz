package com.phlawless.bomberz.core.domain.math;

public record Vector2D(int x, int y) {
	public Vector2D add(Vector2D other) {
		return new Vector2D(x + other.x, y + other.y);
	}
}
