package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {

	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private DateFormat dateFormat;
	
	public DateJsonValueProcessor(){}
	
	public DateJsonValueProcessor(String datePattern) {
        try {
            dateFormat = new SimpleDateFormat(datePattern);
        } catch (Exception e) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN); 
        }
    }
	
	public Object processArrayValue(Object value, JsonConfig arg1) {
		return process(value);
	}

	public Object processObjectValue(String arg0, Object value, JsonConfig arg2) {
		return process(value);
	}

	private Object process(Object value) {
        if (value == null) {
            return "";
        } else {
            return dateFormat.format((Date) value);
        }
    }
}