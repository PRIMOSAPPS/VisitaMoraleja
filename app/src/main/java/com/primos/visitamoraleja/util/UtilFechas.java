package com.primos.visitamoraleja.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Clase para realizar la conversion entre fechas entre local y servidor, asumiendo que el
 * servidor tiene hora UTC 
 * @author h
 *
 */
public class UtilFechas {
	private final static String FORMATO_FECHA = "dd-MM-yyyy HH:mm:ss";

	private final static TimeZone TIMEZONE_DEFAULT = TimeZone.getDefault();

	private final static String IDZONE_UTC = "UTC";
	private final static TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(IDZONE_UTC);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
	
	/**
	 * Metodo que simplemente parsea una fecha a un formate String
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return sdf.format(date);
	}

	/**
	 * Metodo que convierte una fecha indicada en un String, asumiendo TimeZone local
	 * a un objeto Date en UTC
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date fechaToUTC(String strDate) throws ParseException {
		Date date = parseFromStrDefault(strDate);
		return fechaToUTC(date);
	}

	/**
	 * Metodo que convierte una fecha indicada en un String, asumiendo TimeZone UTC
	 * a un objeto Date con TimeZone local
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date fechaFromUTC(String strDate) throws ParseException {
		sdf.setTimeZone(TIMEZONE_UTC);
		return sdf.parse(strDate);
	}

	/**
	 * Metodo que convierte una fecha indicada en un String, asumiendo TimeZone local
	 * a un objeto Date con TimeZone UTC, pero este Date no es correcto, hay que aplicarle
	 * el desplazamiento horario
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parseFromStrDefault(String strDate) throws ParseException {
		sdf.setTimeZone(TIMEZONE_DEFAULT);
		return sdf.parse(strDate);
	}

	/**
	 * Convierte un objeto Date, asumiendo TimeZone local en un objeto Date que representa
	 * la fecha en UTC
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date fechaToUTC(Date date) throws ParseException {
		sdf.setTimeZone(TIMEZONE_UTC);
		String strDate = sdf.format(date);
		return parseFromStrDefault(strDate);
	}

	/**
	 * Convierte un objeto Date, asumiendo TimeZone UTC en un objeto Date que representa
	 * la fecha en TimeZone local
	 * @param dateUTC
	 * @return
	 * @throws ParseException
	 */
	public static Date fechaFromUTC(Date dateUTC) throws ParseException {
		
		TimeZone sdfDefault = TimeZone.getDefault();
		Date ahora = new Date();
		
	    Date fromGmt = new Date(dateUTC.getTime() + sdfDefault.getOffset(ahora.getTime()));
	    return fromGmt;
	}
	
	
	public static boolean isActivaFechaActual(Date inicio, Date fin) {
		boolean resul = false;
		long ahoraMilis = new Date().getTime();
		if(inicio == null && fin == null) {
			resul = true;
		} else if (inicio == null) {
			long finMilis = fin.getTime();
			resul = ahoraMilis < finMilis;
		} else if (fin == null) {
			long inicioMilis = inicio.getTime();
			resul = ahoraMilis > inicioMilis;
		} else {
			long finMilis = fin.getTime();
			long inicioMilis = inicio.getTime();
			resul = ahoraMilis > inicioMilis && ahoraMilis < finMilis;
		}
		return resul;
	}

}
