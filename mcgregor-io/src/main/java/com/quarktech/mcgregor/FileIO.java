package com.quarktech.mcgregor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;

public class FileIO {
	
	public Object getObjectLine(final Object value, final String line) throws Exception {
		final Object object = value.getClass().newInstance();
		final Field fields[] = value.getClass().getDeclaredFields();
		for (Field field : fields) {
			final Arquivo arquivo = field.getAnnotation(Arquivo.class);
			if (arquivo != null) {
				final Object fieldValue = this.setTypeField(arquivo, arquivo.tipo(), line.substring(arquivo.inicio(), arquivo.fim()));
				FieldUtils.writeDeclaredField(object, arquivo.descricao(), fieldValue, Boolean.TRUE);
			}

		}
		return object;
	}
	
	public String getLineObject(final Object object) throws Exception {
		final StringBuffer line = new StringBuffer();
		final Field fields[] = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			final Arquivo arquivo = field.getAnnotation(Arquivo.class);
			if (arquivo != null) {
				final Object fieldValue = FieldUtils.readField(object, arquivo.descricao(), true);
				final Object subLine = getTypeField(arquivo, arquivo.tipo(), fieldValue);
				line.append(subLine);
			}
		}
		return line.toString();
	}
	
	private Object getTypeField(final Arquivo arquivo, final String typeField, final Object fieldValue) throws ParseException {
		switch (typeField) {
		case InformationFormat.TYPE_ALPHANUMERIC:
			return StringUtils.rightPad(String.valueOf(fieldValue), arquivo.tamanho(), " ");
		case InformationFormat.TYPE_NUMERIC:
			return StringUtils.leftPad(String.valueOf(fieldValue), arquivo.tamanho(), "0");
		case InformationFormat.TYPE_BOOLEAN:
			return ((Boolean) fieldValue).booleanValue() ? 1 : 0;
		case InformationFormat.TYPE_DATE:
			return new SimpleDateFormat(StringUtils.isNotBlank(arquivo.formato()) ? arquivo.formato() : "dd/MM/yyyy").parse(String.valueOf(fieldValue));
		case InformationFormat.TYPE_BIGDECIMAL:
			return StringUtils.leftPad(String.valueOf(fieldValue), arquivo.tamanho(), "0");
		default:
			return null;
		}
	}
	
	private Object setTypeField(final Arquivo arquivo, final String typeField, final String fieldValue) throws ParseException {
		switch (typeField) {
		case InformationFormat.TYPE_ALPHANUMERIC:
			return String.valueOf(fieldValue);
		case InformationFormat.TYPE_NUMERIC:
			return Integer.parseInt(fieldValue);
		case InformationFormat.TYPE_BOOLEAN:
			return Boolean.valueOf(fieldValue);
		case InformationFormat.TYPE_DATE:
			return new SimpleDateFormat(StringUtils.isNotBlank(arquivo.formato()) ? arquivo.formato() : "dd/MM/yyyy").parse(fieldValue);
		case InformationFormat.TYPE_BIGDECIMAL:
			return new BigDecimal(fieldValue.replaceAll(",", ""));
		default:
			return null;
		}
	}

}