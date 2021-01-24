package com.ivini.saidasjuntas.acesso.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DataSistemaHelper {
	private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ISO_LOCAL_DATE;
	private DataSistemaHelper() {
	}
	
	public static String formatarData(LocalDate data) {
		return data == null ? "" : FMT_DATA.format(data);
	}
}
