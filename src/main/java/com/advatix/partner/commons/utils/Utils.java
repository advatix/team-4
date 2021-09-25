/*
 * @author Advatix
 * 
 * @since 2019
 * 
 * @version 1.0
 */
package com.advatix.partner.commons.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriUtils;

import com.advatix.partner.commons.logger.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class Utils.
 */
public class Utils {

	private Utils() {
		super();
	}

	/**
	 * Convert double value upto two decimal.
	 * 
	 * @param value
	 * @return formatted double value as #.##
	 */

	public static Double getFormattedDecimalVale(Double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(value));
	}

	/**
	 * Convert date.
	 *
	 * @param timeZone the time zone
	 * @return the date
	 */
	public static Date convertDate(TimeZone timeZone) {
		return new DateTime().toMutableDateTime(DateTimeZone.forTimeZone(timeZone)).toDate();
	}

	public static Date convertGivenDate(Date date, String timeZone) {
		return new DateTime(date.getTime()).toMutableDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timeZone)))
				.toDate();
	}

	/**
	 * Random password generator.
	 *
	 * @return the string
	 */
	public static String randomPasswordGenerator() {
		int length = 10;
		boolean useLetters = true;
		boolean useNumbers = true;
		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	/**
	 * ACCOUNTID : YYYY MM TTT AAAAA (14 Digit) YYYY : 4 digit year code MM : 2
	 * digit month code TTT : 3 digit alphabetical type code (From Account type
	 * master | CMR � Customer, PDR � Provider, CAR � Carrier, SPL - Supplier) AAAAA
	 * : 5 digit auto increment code.
	 *
	 * @param accountType the account type
	 * @param id          the id
	 * @return the string
	 */
	private static String uniqueAccountId(String accountType, Long id) {
		DateTime date = new DateTime();
		String accountId = String.valueOf(id);
		if (accountId.length() < 5) {
			int addZeros = 5 - accountId.length();
			for (int i = 0; i < addZeros; i++) {
				accountId = "0" + accountId;
			}
		}
		return new StringBuilder().append(date.getYear()).append(date.toString("MM")).append(accountType)
				.append(accountId).toString();
	}

	/**
	 * ACCOUNTID : 3 Alpha (A-Z) + 6 Numeric.
	 *
	 * @param alpha the alpha
	 * @param id    the id
	 * @return the string
	 */
	public static String accountId(String alpha, Long id) {
		if (id == 999999)
			alpha = alphaIncrement(alpha);
		String accountId = String.valueOf(id);
		if (accountId.length() < 6) {
			int addZeros = 6 - accountId.length();
			for (int i = 0; i < addZeros; i++) {
				accountId = "0" + accountId;
			}
		}
		return new StringBuilder(alpha).append(accountId).toString();
	}

	public static String workOrderGeneration(String parentOrder, String lastWorkOrderNumber) {
		Integer workOrderNumberInc = 0;
		if (Objects.nonNull(lastWorkOrderNumber))
			workOrderNumberInc = Integer.valueOf(lastWorkOrderNumber.substring(lastWorkOrderNumber.length() - 4));
		workOrderNumberInc += 1;

		if (workOrderNumberInc > 999)
			workOrderNumberInc = 1;

		if (Objects.nonNull(parentOrder)) {
			StringBuilder newWorkOrderNumber = new StringBuilder();
			int addZeros = 3 - String.valueOf(workOrderNumberInc).length();
			for (int i = 0; i < addZeros; i++) {
				newWorkOrderNumber = newWorkOrderNumber.append("0").append(newWorkOrderNumber);
			}
			return new StringBuilder(parentOrder).append(newWorkOrderNumber.toString()).toString();
		}
		return null;
	}

	/**
	 * ACCOUNTID : 3 Alpha (A-Z) + 6 Numeric.
	 *
	 * @param alpha the alpha
	 * @param id    the id
	 * @return the string
	 */
	public static String priceId(String alpha, Long id) {
		if (id == 999999)
			alpha = alphaIncrement(alpha);
		String accountId = String.valueOf(id);
		if (accountId.length() < 6) {
			int addZeros = 6 - accountId.length();
			for (int i = 0; i < addZeros; i++) {
				accountId = "0" + accountId;
			}
		}
		return new StringBuilder(alpha).append(accountId).toString();
	}

	/**
	 * LOCATIONID : YYYY MM TTT AAAAA LLLL (18 Digit) YYYY -> 4 digit year code MM
	 * -> 2 digit month code TTT -> 3 digit alphabetical type code(From Account type
	 * master) AAAAA-> 5 digit auto increment code LLLL -> 4 digit auto increment
	 * location code.
	 *
	 * @param accountType  the account type
	 * @param id           the id
	 * @param locationCode the location code
	 * @return the string
	 */
	private static String uniqueLocationId(String accountType, Long id, String locationCode) {
		DateTime date = new DateTime();
		String locId = String.valueOf(id);
		if (locId.length() < 5) {
			int addZeros = 5 - locId.length();
			for (int i = 0; i < addZeros; i++) {
				locId = "0" + locId;
			}
		}
		if (locationCode.length() < 4) {
			int addZeros = 4 - locationCode.length();
			for (int i = 0; i < addZeros; i++) {
				locationCode = "0" + locationCode;
			}
		}
		return new StringBuilder().append(date.getYear()).append(date.toString("MM")).append(accountType).append(locId)
				.append(locationCode).toString();
	}

	/**
	 * 3 Alpha + 7 Numeric Alpha is �LOC�.
	 *
	 * @param alpha the alpha
	 * @param id    the id
	 * @return the string
	 */
	public static String locationId(String alpha, Long id) {
		if (id == 9999999)
			alpha = alphaIncrement(alpha);
		String idStr = String.valueOf(id);
		if (idStr.length() < 7) {
			int addZeros = 7 - idStr.length();
			for (int i = 0; i < addZeros; i++) {
				idStr = "0" + idStr;
			}
		}
		return new StringBuilder(alpha).append(idStr).toString();
	}

	/**
	 * ORDERID : YYYY MM TT OOOOOO (14 Digit) YYYY -> 4 digit year code MM -> 2
	 * digit month code TT -> 2 digit alphabetical order type code(From Order type
	 * master | PU � Pickup, DL - Delivery) OOOOOO -> 6 digit auto increment code.
	 *
	 * @param orderType the order type
	 * @param id        the id
	 * @return the string
	 */
	private static String uniqueOrderId(String orderType, Long id) {
		DateTime date = new DateTime();
		String orderId = String.valueOf(id);
		if (orderId.length() < 6) {
			int addZeros = 6 - orderId.length();
			for (int i = 0; i < addZeros; i++) {
				orderId = "0" + orderId;
			}
		}
		if (orderType.length() < 2) {
			int addZeros = 2 - orderType.length();
			for (int i = 0; i < addZeros; i++) {
				orderType = "0" + orderType;
			}
		}
		return new StringBuilder().append(date.getYear()).append(date.toString("MM")).append(orderType).append(orderId)
				.toString();
	}

	/**
	 * 1 Alpha + 6 Numeric.
	 *
	 * @param alpha the alpha
	 * @param id    the id
	 * @return the string
	 */
	public static String orderId(String alpha, Long id) {
		if (id == 999999)
			alpha = alphaIncrement(alpha);
		String idStr = String.valueOf(id);
		if (idStr.length() < 6) {
			int addZeros = 6 - idStr.length();
			for (int i = 0; i < addZeros; i++) {
				idStr = "0" + idStr;
			}
		}
		return new StringBuilder(alpha).append(idStr).toString();
	}

	// Phase2 Order number generator
	public static String newOrderId(String orderTypeCode, String orderId) {
		if (Utils.validateStringNotEmpty(orderTypeCode) && Utils.validateStringIsEmpty(orderId))
			return new StringBuilder(orderTypeCode).append(1).toString();

		if (Utils.validateStringNotEmpty(orderTypeCode))
			try {
				return new StringBuilder(orderTypeCode)
						.append(Long.valueOf(orderId.substring(orderTypeCode.length())) + 1).toString();
			} catch (Exception e) {
				return new StringBuilder(orderTypeCode).append(1).toString();
			}

		return Utils.orderId("A", 0L);
	}

	/**
	 * 1 Alpha + 6 Numeric.
	 *
	 * @param alpha the alpha
	 * @param id    the id
	 * @return the string
	 */
	public static String invoiceId(String alpha, Long id) {
		if (id == 999999)
			alpha = alphaIncrement(alpha);
		String idStr = String.valueOf(id);
		if (idStr.length() < 6) {
			int addZeros = 6 - idStr.length();
			for (int i = 0; i < addZeros; i++) {
				idStr = "0" + idStr;
			}
		}
		return new StringBuilder(alpha).append(idStr).toString();
	}

	/**
	 * PRODUCTID : ACCOUNTID OOO (14 Digit) OOO -> 3 digit auto increment code.
	 *
	 * @param accountId the account id
	 * @param id        the id
	 * @return the string
	 */
	public static String uniqueProductId(String accountId, Long id) {
		String productId = String.valueOf(id);
		if (productId.length() < 3) {
			int addZeros = 3 - productId.length();
			for (int i = 0; i < addZeros; i++) {
				productId = "0" + productId;
			}
		}
		return new StringBuilder().append(accountId).append(productId).toString();
	}

	/**
	 * Alpha increment.
	 *
	 * @param alpha the alpha
	 * @return the string
	 */
	public static String alphaIncrement(String alpha) {
		if (alpha.length() == 1) {
			if (alpha.equals("Z"))
				return "AA";
			else
				return (char) (alpha.charAt(0) + 1) + "";
		}
		if (alpha.charAt(alpha.length() - 1) != 'Z') {
			return alpha.substring(0, alpha.length() - 1) + (char) (alpha.charAt(alpha.length() - 1) + 1);
		}
		return alphaIncrement(alpha.substring(0, alpha.length() - 1)) + "A";
	}

	/**
	 * Unique license plate id.
	 *
	 * @param prefix the prefix
	 * @param id     the id
	 * @return the string
	 */
	public static String uniqueLicensePlateId(String prefix, Long id) {
		DateTime date = new DateTime();
		String uniqueIdSeq = String.valueOf(id);
		if (uniqueIdSeq.length() < 4) {
			int addZeros = 4 - uniqueIdSeq.length();
			for (int i = 0; i < addZeros; i++) {
				uniqueIdSeq = "0" + uniqueIdSeq;
			}
		}
		return new StringBuilder().append(prefix).append(date.toString("yyyyMMddHHmm")).append(uniqueIdSeq).toString();
	}

	/**
	 * Display error for web.
	 *
	 * @param t the t
	 * @return the string
	 */
	public static String displayErrorForWeb(StackTraceElement[] t) {
		if (t == null)
			return "null";

		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : t) {
			sb.append(element.toString());
			sb.append("<br/>");
		}
		return sb.toString();
	}

	public static String truncate(String value, int length) {
		return value != null && value.length() > length ? value.substring(0, length) : value;
	}

	public static String urlEncoder(String url) {
		try {
			return UriUtils.encodeQuery(url, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean validateStringNotEmpty(String string) {
		return StringUtils.isNotBlank(string);
	}

	public static boolean validateStringIsEmpty(String string) {
		return StringUtils.isBlank(string);
	}

	public static void copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}

	public static boolean checkCollectionIsNotEmpty(Collection<?> collection) {
		return Boolean.FALSE.equals(CollectionUtils.isEmpty(collection));
	}

	public static boolean checkCollectionIsNotEmpty(Map<?, ?> collection) {
		return Boolean.FALSE.equals(CollectionUtils.isEmpty(collection));
	}

	public static boolean checkCollectionIsEmpty(Collection<?> collection) {
		return CollectionUtils.isEmpty(collection);
	}

	public static boolean checkCollectionIsEmpty(Map<?, ?> collection) {
		return CollectionUtils.isEmpty(collection);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static List<Date> getMinMaxDate(Date date) {
		return Arrays.asList(
				new DateTime(date).hourOfDay().withMinimumValue().minuteOfHour().withMinimumValue().secondOfMinute()
						.withMinimumValue().millisOfSecond().withMinimumValue().toDate(),
				new DateTime(date).hourOfDay().withMaximumValue().minuteOfHour().withMaximumValue().secondOfMinute()
						.withMaximumValue().millisOfSecond().withMaximumValue().toDate());
	}

	public static DateTime getMinDate(Date date) {
		return new DateTime(date).hourOfDay().withMinimumValue().minuteOfHour().withMinimumValue().secondOfMinute()
				.withMinimumValue().millisOfSecond().withMinimumValue();
	}

	private static boolean isWorkingDay(Calendar cal) {
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
			return false;
		// tests for other holidays here
		// ...
		return true;
	}

	public static Date getWorkingDays(Integer daysToAdd) {
		Calendar cal = new GregorianCalendar();
		for (int i = 0; i < daysToAdd; i++)
			do {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			} while (!isWorkingDay(cal));

		return cal.getTime();
	}

	public static void printJsonString(Object... objects) {
		String jsonString = toJsonString(objects);
		Logger.getLogger(Utils.class).info(jsonString);
	}

	public static String toJsonString(Object... objects) {
		ObjectMapper mapperObj = new ObjectMapper();
		try {
			return mapperObj.writeValueAsString(objects);
		} catch (JsonProcessingException e) {
			Logger.getLogger(Utils.class).error(e.getLocalizedMessage(), e);
		}
		return "";
	}

	public static String generateBatchId() {
		return UUID.randomUUID().toString();
	}

	public static String matchDate(String dateForUpdate, String currentDate) {
		String returnDate = null;
		try {
			SimpleDateFormat shipmentJourneyFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			SimpleDateFormat shipmentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date updateDate = shipmentJourneyFormat.parse(dateForUpdate);
			Date cc = shipmentFormat.parse(currentDate);
			if (cc.getTime() < updateDate.getTime()) {
				returnDate = shipmentFormat.format(updateDate);
				return returnDate;
			}
		} catch (ParseException e) {
			return returnDate;
		}

		return null;
	}

	public static String generateTrackingNumber() {
		return "XPDEL" + new DateTime().toString("yyyyMMddHHmmssSSS");
	}

	public static String generateBarcodeNumber() {
		return new DateTime().toString("yyyyMMddHHmmssSSS");
	}

	public static String generateBoxNumber() {
		return new DateTime().toString("yyyyMMddHHmmss");
	}

	public static String generateOrderNumber() {
		return new DateTime().toString("yyyyMMddHHmmssSSS");
	}

	public static String convertDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public static String getCurrentTime(String timeZone) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if (timeZone.equalsIgnoreCase("EST")) {
				calendar.add(Calendar.HOUR, 1);
			}
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			System.out.println("Time Zone : " + timeZone);
			System.out.println("Current Server Date : " + new Date());
			System.out.println("EST Time of Current Server Date : " + sdf.format(new Date()));
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStringDateInTimeZone(String timeZone, Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if (timeZone.equalsIgnoreCase("EST")) {
				calendar.add(Calendar.HOUR, 1);
			}
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			System.out.println("Time Zone : " + timeZone);
			System.out.println("Current Server Date : " + new Date());
			System.out.println("EST Time of Current Server Date : " + sdf.format(date));
			return sdf.format(calendar.getTime()); // sdf.format(date) //sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDatePlusOneDay(Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_WEEK, 1);

			// sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			// System.out.println("Time Zone : "+timeZone);
			System.out.println("Current Server Date : " + new Date());
			System.out.println("EST Time of Current Server Date : " + sdf.format(date));
			return sdf.format(calendar.getTime()); // sdf.format(date) //sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getTargetDateTime(Date trgtDate, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:00 aa");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:00");
		// SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException pe) {
			pe.printStackTrace();
			try {
				d = sdf1.parse(time);
			} catch (ParseException pe1) {
				pe1.printStackTrace();
				try {
					d = sdf2.parse(time);
				} catch (ParseException pe2) {
					pe2.printStackTrace();
				}
			}
		}
		Date dateTime = new Date(trgtDate.getTime() + d.getTime());
		return dateTime;
	}

	// public static String getCurrentTime(String timeZone) {
	/*
	 * public static void main(String[] args) { try { SimpleDateFormat sdf1 = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Calendar
	 * calendar = Calendar.getInstance(); calendar.setTime(new Date());
	 *//*
		 * if(timeZone.equalsIgnoreCase("EST")) { calendar.add(Calendar.HOUR, 1); }
		 *//*
			 * sdf.setTimeZone(TimeZone.getTimeZone("PST"));
			 * //System.out.println("Time Zone : "+timeZone);
			 * System.out.println("Current Server Date : "+new Date());
			 * System.out.println("EST Time of Current Server Date : "+sdf.format(new
			 * Date())); System.out.println("EST Time of Current Server Date : "+sdf.
			 * getDateTimeInstance().parse(sdf1.format(new Date())));
			 * 
			 * DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 * timeFormat.setTimeZone(TimeZone.getTimeZone("PST")); String estTime =
			 * timeFormat.format(new Date());
			 * 
			 * Calendar cal = new GregorianCalendar();
			 * cal.setTimeZone(TimeZone.getTimeZone("PST")); cal.setTime(new Date());
			 * System.out.println(cal.getTime()+ "-- "+sdf.parseObject(estTime)); //return
			 * sdf.format(calendar.getTime()); }catch (Exception e){ e.printStackTrace(); }
			 * //return null; }
			 */

	/*
	 * public static void main(String[] args) {
	 *//*
		 * TimeZone utcTZ= TimeZone.getTimeZone("UTC"); TimeZone estTZ=
		 * TimeZone.getTimeZone("EST");
		 * 
		 * //System.out.println(""+getCurrentTime("EST")); Calendar sourceCalendar =
		 * Calendar.getInstance(); sourceCalendar.setTime(new Date());
		 * sourceCalendar.setTimeZone(utcTZ);
		 * 
		 * Calendar targetCalendar = Calendar.getInstance(); for (int field : new int[]
		 * {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR,
		 * Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND}) {
		 * targetCalendar.set(field, sourceCalendar.get(field)); }
		 * targetCalendar.setTimeZone(estTZ);
		 * System.out.println(targetCalendar.getTime());
		 * 
		 * System.out.println(convertDate(targetCalendar.getTime()));
		 * 
		 * Date estTime = new Date(new Date().getTime() +
		 * TimeZone.getTimeZone("EST").getRawOffset()); System.out.println(estTime);
		 *//*
			
			*//*
				 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // timeZone =
				 * Calendar.getInstance().getTimeZone().getID(); // set timezone to
				 * SimpleDateFormat sdf.setTimeZone(TimeZone.getTimeZone("EST")); // return Date
				 * in required format with timezone as String
				 * System.out.println("--  "+sdf.format(new Date()));
				 *//*
					 * Calendar calendar = Calendar.getInstance(); calendar.setTime(new Date());
					 * calendar.add(Calendar.HOUR,1); SimpleDateFormat sdf = new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 * sdf.setTimeZone(TimeZone.getTimeZone("EST"));
					 * System.out.println(sdf.format(calendar.getTime()));
					 * 
					 * 
					 * }
					 */

	public static Date getCurrentDateTimeBasedOnZone(String timeZone) {
		/*
		 * try{ Calendar calendar = Calendar.getInstance(); calendar.setTime(new
		 * Date()); SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		 * System.out.println(sdf.format(calendar.getTime())); return
		 * sdf.parse(sdf.format(calendar.getTime())); }catch (Exception e){
		 * e.printStackTrace(); }
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		SimpleDateFormat sdfLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(new Date());
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf1.setTimeZone(TimeZone.getTimeZone(timeZone));
			System.out.println("--vin--" + sdf1.parse(sdf.format(calendar.getTime())));

			String d = sdf.format(calendar.getTime());
			System.out.println("current time zone ==> " + new Date());
			System.out.println("EST in string " + d);
			System.out.println("EST in date object ==> " + sdf.parse(sdf.format(calendar.getTime())));
			return sdfLocal.parse(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getScheduleDateBasedOnZone(Date scheduleDt, String timeZone) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(scheduleDt);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Double getDuration(Date startDate) {
		DateTime dateTimeEnd = new DateTime(new Date());
		DateTime dateTimeStart = new DateTime(startDate);
		Period p = new Period(dateTimeStart, dateTimeEnd);
		int durationMins = p.getMinutes() + ((p.getHours() + (p.getDays() * 24) + (p.getWeeks() * 7 * 24)) * 60);
		System.out.println("startDate : " + startDate + " -- EndDate : " + new Date() + " -- durationMins"
				+ durationMins + " -- in double : " + Double.parseDouble(durationMins + ""));
		return Double.parseDouble(durationMins + "");
	}

	public static String getDurationBetweenDates(Date startDate, Date endDate) {
		DateTime dateTimeEnd = new DateTime(endDate);
		DateTime dateTimeStart = new DateTime(startDate);
		Period p = new Period(dateTimeStart, dateTimeEnd);
		int durationMins = p.getMinutes() + (p.getHours() + (p.getDays() * 24) + (p.getWeeks() * 7 * 24)) * 60;
		System.out.println("startDate : " + startDate + " -- EndDate : " + new Date() + " -- durationMins"
				+ durationMins + " -- in double : " + Double.parseDouble(durationMins + ""));
		return durationMins + "";
	}

	public static long getEpochTime(Date dateValue) {
		// Date today = Calendar.getInstance().getTime();
		long epochTime = 0l;
		// Constructs a SimpleDateFormat using the given pattern
		SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
		// format() formats a Date into a date/time string.
		String currentTime = crunchifyFormat.format(dateValue);
		try {

			// parse() parses text from the beginning of the given string to produce a date.
			Date date = crunchifyFormat.parse(currentTime);
			// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00
			// GMT represented by this Date object.
			epochTime = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return epochTime;
	}

	public static Date getDateWithHourOfDay(int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	/*
	 * public static void main(String[] args) throws Exception{ Calendar calendar1 =
	 * Calendar.getInstance(); calendar1.setTime(new Date()); SimpleDateFormat sdf1
	 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * sdf1.setTimeZone(TimeZone.getTimeZone("EST"));
	 * System.out.println("Time in EST "+sdf1.format(calendar1.getTime()));
	 * System.out.println("--vin-- "+sdf1.parse(sdf1.format(calendar1.getTime())));
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * sdf.setTimeZone(TimeZone.getTimeZone("EST")); SimpleDateFormat sdfLocal = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); try { Calendar calendar =
	 * Calendar.getInstance(); calendar.setTime(new Date()); String d =
	 * sdf.format(calendar.getTime());
	 * System.out.println("current time zone ==> "+new Date());
	 * System.out.println("EST in string "+d);
	 * System.out.println("EST in date object ==> "+sdf.parse(sdf.format(calendar.
	 * getTime()))); }catch (Exception e){ e.printStackTrace(); } }
	 */

}
