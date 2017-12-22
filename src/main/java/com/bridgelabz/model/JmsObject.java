package com.bridgelabz.model;

import java.io.Serializable;

public class JmsObject<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T object;

	private String index;

	private String type;

	private String id;

	public T getObject() {
		return object;
	}

	public JmsObject<T> setObject(T object) {
		this.object = object;
		return this;
	}

	public String getIndex() {
		return index;
	}

	public JmsObject<T> setIndex(String index) {
		this.index = index;
		return this;
	}

	public String getType() {
		return type;
	}

	public JmsObject<T> setType(String type) {
		this.type = type;
		return this;
	}

	public String getId() {
		return id;
	}

	public JmsObject<T> setId(String id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		return "JmsObject [object=" + object + ", index=" + index + ", type=" + type + ", id=" + id + "]";
	}

}
