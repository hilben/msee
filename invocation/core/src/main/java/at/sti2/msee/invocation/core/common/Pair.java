package at.sti2.msee.invocation.core.common;

import java.io.Serializable;

class Pair<L, R> implements Serializable {
	private static final long serialVersionUID = 1L;
	private L name;
	private R value;

	public Pair(L name, R value) {
		this.name = name;
		this.value = value;
	}

	public L name() {
		return name;
	}

	public R value() {
		return value;
	}
}