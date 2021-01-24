package com.ivini.saidasjuntas.tag;

import java.lang.reflect.Method;

import org.junit.jupiter.api.TestInfo;

public final class TagSaida {
	private TagSaida() {
	}
	
	public static boolean temTag(TestInfo info, String... tags) {
		for (String tagX : tags) {
			if (info.getTags().contains(tagX)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean metodoDeTesteComecaCom(TestInfo info, String... prefixos) {
		Method metodo = info.getTestMethod().orElse(null);
		if (metodo == null) {
			return false;
		}
		String nome = metodo.getName();
		for (String pref : prefixos) {
			if (nome.startsWith(pref)) {
				return true;
			}
		}
		return false;
	}
}
