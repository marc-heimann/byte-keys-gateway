package de.bytesoftware.keys.gateway.util;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import sun.net.smtp.SmtpClient;

public final class Helper {

  public static enum RoundPips {
    NORMAL, HALF, QUARTER, TENTH;
  }

  public static enum RoundMode {
    NORMAL, UP, DOWN;
  }

  public static String generateConversationKey(long left, long right) {
    if (left > right) {
      return right + "-" + left;
    } else {
      return left + "-" + right;
    }
  }

  private static String hostname = null;

  /** @deprecated use <code>RoundPips.NORMAL</code> instead */
  @Deprecated
  public static final int ROUND_PIPS_NORMAL = RoundPips.NORMAL.ordinal();
  /** @deprecated use <code>RoundPips.HALF</code> instead */
  @Deprecated
  public static final int ROUND_PIPS_HALF = RoundPips.HALF.ordinal();
  /** @deprecated use <code>RoundPips.QUARTER</code> instead */
  @Deprecated
  public static final int ROUND_PIPS_QUARTER = RoundPips.QUARTER.ordinal();
  /** @deprecated use <code>RoundPips.TENTH</code> instead */
  @Deprecated
  public static final int ROUND_PIPS_TENTH = RoundPips.TENTH.ordinal();

  public static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS_GERMAN = new DecimalFormatSymbols(
      Locale.GERMAN);
  public static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS_US = new DecimalFormatSymbols(
      Locale.US);

  /** @deprecated use <code>RoundMode.NORMAL</code> instead */
  @Deprecated
  public static final int FORCE_ROUND_NORMAL = RoundMode.NORMAL.ordinal();
  /** @deprecated use <code>RoundMode.UP</code> instead */
  @Deprecated
  public static final int FORCE_ROUND_UP = RoundMode.UP.ordinal();
  /** @deprecated use <code>RoundMode.DOWN</code> instead */
  @Deprecated
  public static final int FORCE_ROUND_DOWN = RoundMode.DOWN.ordinal();

  /** @deprecated Was ist denn das? */
  @Deprecated
  public static final int SHIFT_NONE = 0;
  /** @deprecated Was ist denn das? */
  @Deprecated
  public static final int SHIFT_DOWN = -1;
  /** @deprecated Was ist denn das? */
  @Deprecated
  public static final int SHIFT_UP = 1;

  private static final long[] FACTORS = { 1L, //
      10L, //
      100L, //
      1000L, //
      10000L, //
      100000L, //
      1000000L, //
      10000000L, //
      100000000L, //
      1000000000L, //
      10000000000L, //
      100000000000L, //
      1000000000000L, //
      10000000000000L, //
      100000000000000L, //
      1000000000000000L, //
      10000000000000000L, //
      100000000000000000L, //
      1000000000000000000L //
  };

  private static final double[] FRACTIONS = { 1., //
      .1, //
      .01, //
      .001, //
      .0001, //
      .00001, //
      .000001, //
      .0000001, //
      .00000001, //
      .000000001, //
      .0000000001, //
      .00000000001, //
      .000000000001, //
      .0000000000001, //
      .00000000000001, //
      .000000000000001, //
      .0000000000000001 //
  };

  private Helper() {
    // No instances, please.
  }

  public final static double round(double value, int digits) {
    return _round(value, digits, RoundMode.NORMAL, RoundPips.NORMAL);
  }

  /**
   * Same as {@link #round2(double, int, int, int)} with
   * <code>force_direction</code> set to {@link #FORCE_ROUND_NORMAL}.
   * 
   * @param value
   *          value to round
   * @param digits
   *          decimal places
   * @param denominator
   *          denominator for fractions after the decimal places
   * @return rounded double value
   */
  public final static double round2(double value, int digits, int denominator) {
    return _round2(value, digits, denominator, RoundMode.NORMAL);
  }

  public final static double round(double value, int digits,
      RoundMode forceDirection) {
    return _round(value, digits, forceDirection, RoundPips.NORMAL);
  }

  public final static double round2(double value, int digits, int denominator,
      RoundMode roundMode, RoundPips roundPips) {
    return denominator == 1 ? round(value, digits, roundMode, roundPips)
        : _round2(value, digits, denominator, roundMode);
  }

  public final static double round(double value, int digits,
      RoundMode roundMode, RoundPips roundPips) {
    return _round(value, digits, roundMode, roundPips);
  }

  /**
   * Neue <code>Helper._round()</code>-Methode arbeitet im Gegensatz zur alten
   * Version mit Wertebereichen, die der Reihe nach durchgearbeitet werden.
   * Theoretisch sollten damit die Rundungsungenauigkeiten der alten
   * <code>Helper._round()</code>-Methode nicht mehr vorkommen k??nnen. Innerhalb
   * der Runung wird mit <code>long</code> gearbeitet, so dass innerhalb des
   * Wertebereichs (i.d.R. +2 Stellen Genauigkeit) keine Ungenauigkeiten
   * auftreten k??nnen. Wertebereiche werden so zugeordnet, das eine 0.999999999
   * als 1 gewertet wird.
   */
  private final static double _round(double value, int digits,
      RoundMode force_direction, RoundPips pips_round) {

    switch (pips_round) {
    case NORMAL: {
      value = _roundNormal(value, digits, force_direction);
      break;
    }

    case HALF: {
      value = _roundHalf(value, digits, force_direction);
      break;
    }

    case QUARTER: {
      value = _roundQuarter(value, digits, force_direction);
      break;
    }

    case TENTH: {
      value = _roundNormal(value, digits + 1, force_direction);
      break;
    }
    }

    return value;
  }

  private static double _round2(double value, int digits, int denominator,
      RoundMode force_direction) {
    if (denominator == 1)
      return _roundNormal(value, digits, force_direction);

    if (denominator == 4)
      return _roundQuarter(value, digits, force_direction);

    if (denominator == 2)
      return _roundHalf(value, digits, force_direction);

    if (denominator == 10)
      return _roundNormal(value, digits + 1, force_direction);

    if (denominator < 1)
      throw new IllegalArgumentException( //
          "denominator is less than 1: " + denominator);

    final long factor = getFactor(digits);
    final double prod = value * factor;

    // the integer part of the result
    final long intPart = (long) prod;

    // the fractions of the result
    final long frac = (long) _roundNormal( //
        (prod - intPart) * denominator, 0, force_direction);
    final double fracValue = (double) frac / denominator;

    // determine number of decimal digits of the integer part of the value
    int intDigits = -1;
    if (value >= 0.) {
      if (value < 1.) {
        intDigits = 0;
      } else if (value < 10.) {
        intDigits = 1;
      } else if (value < 100.) {
        intDigits = 2;
      } else if (value < 1000.) {
        intDigits = 3;
      } else if (value < 10000.) {
        intDigits = 4;
      } else if (value < 100000.) {
        intDigits = 5;
      } else if (value < 1000000.) {
        intDigits = 6;
      }
    } else {
      // use one more digit here to preserve minus sign
      if (value > -1.) {
        intDigits = 1;
      } else if (value > -10.) {
        intDigits = 2;
      } else if (value > -100.) {
        intDigits = 3;
      } else if (value > -1000.) {
        intDigits = 4;
      } else if (value > -10000.) {
        intDigits = 5;
      } else if (value > -100000.) {
        intDigits = 6;
      }
    }

    // fallback, too many digits
    if (intDigits < 0) {
      return new BigDecimal(Double.toString((double) intPart / factor)).add(
          new BigDecimal(fracValue / factor)).doubleValue();
    }

    // use BigDecimals for division because of inaccuracies when using
    // double division directly
    final long fracFactor = getFactor(18 - intDigits - digits);
    final BigDecimal dividend = new BigDecimal((intPart * fracFactor)
        + (long) (fracValue * fracFactor));
    final BigDecimal divisor = new BigDecimal(fracFactor * factor);
    return dividend.divide(divisor).doubleValue();

    // return (double) ((intPart * fracFactor) + (long) (fracValue *
    // fracFactor)) / (fracFactor * factor);
  }

  private final static double _roundNormal(double value, int digits,
      RoundMode force_direction) {
    final long factor = getFactor(digits + 2);
    long tmp = (long) (value * factor);

    // The following term might cause future errors:
    // e.g. 65.02491 with digits=2 results in 650249 and last2Digits 49,
    // while 65.0349 results in 650348 and last2Digits 48

    final int last2Digits = (int) Math.abs(tmp % 100);

    // Modification : Simon Muras
    // since last2Digits is always <= 99 we can
    // eliminate then <=99

    switch (force_direction) {
    case NORMAL:
      // if (last2Digits >= 49 && last2Digits <= 99) {
      if (last2Digits >= 49) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (100 - last2Digits);
        } else {
          tmp -= (100 - last2Digits);
        }
      } else {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last2Digits;
        } else {
          tmp += last2Digits;
        }
      }
      break;
    case UP:
      // if (last2Digits >= 1 && last2Digits <= 99) {
      if (last2Digits >= 1) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (100 - last2Digits);
        } else {
          tmp -= (100 - last2Digits);
        }
      }

      break;
    case DOWN:
      if (last2Digits == 99) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (100 - last2Digits);
        } else {
          tmp -= (100 - last2Digits);
        }
      } else {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last2Digits;
        } else {
          tmp += last2Digits;
        }
      }
      break;
    }
    return (double) tmp / factor;
  }

  private final static double _roundHalf(double value, int digits,
      RoundMode force_direction) {
    final long factor = getFactor(digits + 3);
    long tmp = (long) (value * factor);
    final int last3Digits = (int) Math.abs(tmp % 1000);

    // since lastdigits always (by modulo definition)is >=0 we
    // can remove several comparisons
    switch (force_direction) {
    case NORMAL:
      // if (last3Digits >= 0 && last3Digits <= 248) {
      if (last3Digits <= 248) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last3Digits;
        } else {
          tmp += last3Digits;
        }
        // since we already know last3Digits is > 249 we can eliminate
        // this comparison:
        // } else if (last3Digits >= 249 && last3Digits <= 498) {
      } else if (last3Digits <= 498) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (500 - last3Digits);
        } else {
          tmp -= (500 - last3Digits);
        }
        // since we already know last3Digits is >= 499 we can eliminate
        // the comparison "last3Digits >= 499"
        // } else if (last3Digits >= 499 && last3Digits <= 748) {
      } else if (last3Digits <= 748) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last3Digits - 500;
        } else {
          tmp += last3Digits - 500;
        }
        // since we already know that that last3Digits is >= 749 and
        // by modulo definition is less than 999:
        // } else if (last3Digits >= 749 && last3Digits <= 999) {
      } else {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (1000 - last3Digits);
        } else {
          tmp -= (1000 - last3Digits);
        }
      }

      break;
    case UP: {
      if (last3Digits >= 1 && last3Digits <= 499) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (500 - last3Digits);
        } else {
          tmp -= (500 - last3Digits);
        }
        // By definition last3Digits % 1000 <= 999
        // } else if (last3Digits >= 501 && last3Digits <= 999) {
      } else if (last3Digits >= 501) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += (1000 - last3Digits);
        } else {
          tmp -= (1000 - last3Digits);
        }
      }
      break;
    }
    case DOWN: {
      if (last3Digits >= 1 && last3Digits <= 498) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last3Digits;
        } else {
          tmp += last3Digits;
        }
      } else if (last3Digits == 499 || last3Digits == 999) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += 1;
        } else {
          tmp -= 1;
        }
        // we already know lastDigit is not 999 and by modulo definition
        // is less than 1000
        // } else if (last3Digits >= 501 && last3Digits <= 998) {
      } else if (last3Digits >= 501) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last3Digits - 500;
        } else {
          tmp += last3Digits - 500;
        }
      }
      break;
    }

    }

    return (double) tmp / factor;
  }

  private final static double _roundQuarter(double value, int digits,
      RoundMode force_direction) {
    final long factor = getFactor(digits + 4);
    long tmp = (long) (value * factor);
    final int last4Digits = (int) Math.abs(tmp % 10000);

    switch (force_direction) {
    case NORMAL: {
      if (last4Digits == 0000 || last4Digits == 2500 || last4Digits == 5000
          || last4Digits == 7500) {
        break;
      }
      // Last4Digits always is > 0
      // if (last4Digits >= 0 && last4Digits <= 1248) {
      if (last4Digits <= 1248) {
        // Abrunden -> 0
        if (tmp >= 0) {
          tmp -= last4Digits;
        } else {
          tmp += last4Digits;
        }
        // Last4Digits already is >= 1249
        // } else if (last4Digits >= 1249 && last4Digits <= 2498) {
      } else if (last4Digits <= 2498) {
        // Aufrunden ->0.25
        if (tmp >= 0) {
          tmp += 2500 - last4Digits;
        } else {
          tmp -= 2500 - last4Digits;
        }
        // Last4Digits already is >= 2499
        // } else if (last4Digits >= 2499 && last4Digits <= 3748) {
      } else if (last4Digits <= 3748) {
        // Abrunden -> 0.25
        if (tmp >= 0) {
          tmp -= last4Digits - 2500;
        } else {
          tmp += last4Digits - 2500;
        }
        // Last4Digits already is >= 3749
        // } else if (last4Digits >= 3749 && last4Digits <= 4998) {
      } else if (last4Digits <= 4998) {
        // Aufrunden -> 0.5
        if (tmp >= 0) {
          tmp += 5000 - last4Digits;
        } else {
          tmp -= 5000 - last4Digits;
        }
        // Last4Digits already is >= 4999
        // } else if (last4Digits >= 4999 && last4Digits <= 6248) {
      } else if (last4Digits >= 4999 && last4Digits <= 6248) {
        // Abrunden -> 0.5
        if (tmp >= 0) {
          tmp -= last4Digits - 5000;
        } else {
          tmp += last4Digits - 5000;
        }
        // Last4Digits already is >= 6249
        // } else if (last4Digits >= 6249 && last4Digits <= 7498) {
      } else if (last4Digits <= 7498) {
        // Aufrunden -> 0.75
        if (tmp >= 0) {
          tmp += 7500 - last4Digits;
        } else {
          tmp -= 7500 - last4Digits;
        }
        // Last4Digits already is >= 7499
        // } else if (last4Digits <= 8748) {
      } else if (last4Digits <= 8748) {
        // Abrunden -> 0.75
        if (tmp >= 0) {
          tmp -= last4Digits - 7500;
        } else {
          tmp += last4Digits - 7500;
        }
        // Last4Digits already is >= 8749 and is by %10000 always <=
        // 99999
        // } else if (last4Digits >= 8749 && last4Digits <= 9999) {
      } else {
        // Aufrunden -> 1
        if (tmp >= 0) {
          tmp += 10000 - last4Digits;
        } else {
          tmp -= 10000 - last4Digits;
        }
      }
      break;
    }
    case UP: {
      if (last4Digits >= 1 && last4Digits <= 2499) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += 2500 - last4Digits;
        } else {
          tmp -= 2500 - last4Digits;
        }
      } else if (last4Digits >= 2501 && last4Digits <= 4999) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += 5000 - last4Digits;
        } else {
          tmp -= 5000 - last4Digits;
        }
      } else if (last4Digits >= 5001 && last4Digits <= 7499) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += 7500 - last4Digits;
        } else {
          tmp -= 7500 - last4Digits;
        }
        // % 10000 always is <= 99999
        // } else if (last4Digits >= 7501 && last4Digits <= 9999) {
      } else if (last4Digits >= 7501) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += 10000 - last4Digits;
        } else {
          tmp -= 10000 - last4Digits;
        }
      }

      break;
    }
    case DOWN: {
      if (last4Digits == 2499 || last4Digits == 4999 || last4Digits == 7499
          || last4Digits == 9999) {
        // Aufrunden
        if (tmp >= 0) {
          tmp += 1;
        } else {
          tmp -= 1;
        }
      } else if (last4Digits >= 1 && last4Digits <= 2498) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last4Digits;
        } else {
          tmp += last4Digits;
        }
      } else if (last4Digits >= 2501 && last4Digits <= 4998) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last4Digits - 2500;
        } else {
          tmp += last4Digits - 2500;
        }
      } else if (last4Digits >= 5001 && last4Digits <= 7498) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last4Digits - 5000;
        } else {
          tmp += last4Digits - 5000;
        }
      } else if (last4Digits >= 7501 && last4Digits <= 9998) {
        // Abrunden
        if (tmp >= 0) {
          tmp -= last4Digits - 7500;
        } else {
          tmp += last4Digits - 7500;
        }
      }

      break;
    }
    }
    return (double) tmp / factor;
  }

  public final static long getFactor(int i) {
    return (i < 18) ? FACTORS[i] : (long) Math.pow(10, i);
  }

  public static double getFraction(int i) {
    return (i < 16) ? FRACTIONS[i] : _roundNormal(Math.pow(10, -i), i,
        RoundMode.NORMAL);
  }

  /**
   * Errechnet den die kleinste Fraction f??r ein Item<br>
   * z.B.: 4 digits = 0.0001<br>
   * Nimmt ei Roundpips Normal den Denominator-Weg, sonst ??ber half/quarter etc.
   * 
   * @param digits
   * @param denominator
   * @return
   */
  public static double getSmallestTick(int digits, int denominator,
      RoundPips roundPips) {
    if (roundPips == RoundPips.NORMAL) {
      return getSmallestTick(digits, denominator);
    } else {
      return getSmallestTick(digits, roundPips);
    }
  }

  /**
   * Errechnet den die kleinste Fraction f??r ein Item<br>
   * z.B.: 4 digits = 0.0001
   * 
   * @param digits
   * @param denominator
   * @return
   */
  public static double getSmallestTick(int digits, int denominator) {
    if (denominator == 0 || denominator == 1)
      return FRACTIONS[digits];

    return Helper.round2(FRACTIONS[digits] / denominator, digits, denominator);
  }

  /**
   * Errechnet den die kleinste Fraction f??r ein Item<br>
   * z.B.: 4 digits = 0.0001 oder 4 digits, quarterpips = 0.000025
   * 
   * @param digits
   * @param roundPips
   * @return
   */
  public static double getSmallestTick(int digits, RoundPips roundPips) {
    if (roundPips == null || RoundPips.NORMAL == roundPips)
      return FRACTIONS[digits];

    if (RoundPips.QUARTER == roundPips) {
      return Helper.round(FRACTIONS[digits] / 4, 7);
    }

    if (RoundPips.HALF == roundPips) {
      return Helper.round(FRACTIONS[digits] / 2, 7);
    }

    if (RoundPips.TENTH == roundPips) {
      return Helper.round(FRACTIONS[digits + 1], 7);
    }

    return FRACTIONS[digits];
  }

  public static String getHostname() {
    if (hostname != null)
      return hostname;

    try {
      hostname = InetAddress.getLocalHost().getHostName();
      return hostname;
    } catch (final Exception e) {
      hostname = "";
      return hostname;
    }
  }

  /**
   * Creates a format pattern for usage with the <code>DecimalFormat</code>
   * class.
   * 
   * @param digits
   * @param fixDigits
   * @param withThousendSeperator
   * @return a format pattern for <code>DecimalFormat</code>
   */
  public final static String getDezimalFormatPattern(int digits,
      boolean fixDigits, boolean withThousendSeperator) {
    final StringBuilder format = new StringBuilder();
    char digitsChar;
    if (fixDigits)
      digitsChar = '0';
    else
      digitsChar = '#';

    if (withThousendSeperator)
      format.append("#,##0");
    else
      format.append("#");

    if (digits > 0) {
      format.append(".");
      for (int i = 1; i <= digits; i++)
        format.append(digitsChar);
    }

    return format.toString();
  }

  /**
   * Format a double to fixed digits and thousend seperator.
   * 
   * @param myDouble
   * @param digits
   * @return a String that represents the Double
   */
  public final static String formatToString(double myDouble, int digits) {
    final DecimalFormat df = new DecimalFormat(getDezimalFormatPattern(digits,
        true, true));
    return df.format(myDouble);
  }

  /**
   * @param myDouble
   * @param formatString
   *          like '##.## EUR'
   * @return a String that represents the Double.
   */
  public final static String formatToString(double myDouble, String formatString) {
    final DecimalFormat df = new DecimalFormat(formatString);
    return df.format(myDouble);
  }

  /**
   * 
   * @param myDouble
   * @param formatString
   * @param decimalFormatSymbols
   * @return a String that represents the Double.
   */
  public final static String formatToString(double myDouble,
      String formatString, DecimalFormatSymbols decimalFormatSymbols) {
    final DecimalFormat df = new DecimalFormat(formatString,
        decimalFormatSymbols);
    return df.format(myDouble);
  }

  @SuppressWarnings("restriction")
  public final static boolean SendEMail(String subject, String text,
      String from, String to, String smtpServerUrl) {
    try {
      subject = new String(Base64Coder.encode(subject.getBytes("UTF-8")));
      subject = "=?UTF-8?B?" + subject + "?=";

      final SmtpClient x = new SmtpClient(smtpServerUrl);
      x.setConnectTimeout(1000);
      x.setReadTimeout(1000);
      x.from(from);
      x.to(to);
      final PrintStream p = x.startMessage();
      p.println("From: " + from);
      p.println("To: " + to);
      p.println("Subject: " + subject);
      p.println("Content-Type: text/plain;format=flowed;charset=\"utf-8\"");
      p.println("Content-Transfer-Encoding: 8bit");
      p.println();
      p.write(text.getBytes("UTF-8"));

      x.closeServer();
    } catch (final Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Sendet eine E-Mail an ibas@baumann-technologie.de. Only da best stuff. Only
   * from Da Chuck!
   * 
   * @param subject
   *          Subject Header
   * @param text
   *          Mail-Inhalt
   */
  public final static boolean SendEMail(String subject, String text) {
    try {
      // RFC 2047 - MIME (Multipurpose Internet Mail Extensions) Part
      // Three: Message Header Extensions for Non-ASCII Text
      subject = new String(Base64Coder.encode(subject.getBytes("UTF-8")));
      subject = "=?UTF-8?B?" + subject + "?=";

      final SmtpClient x = new SmtpClient("mail-out.btnet.de");
      x.setConnectTimeout(1000);
      x.setReadTimeout(1000);
      x.from("ibas@baumann-technologie.de");
      x.to("ibas@baumann-technologie.de");
      final PrintStream p = x.startMessage();
      p.println("To: ibas@baumann-technologie.de");
      p.println("Subject: " + subject);
      p.println("Content-Type: text/plain;format=flowed;charset=\"utf-8\"");
      p.println("Content-Transfer-Encoding: 8bit");
      p.println();
      p.write(text.getBytes("UTF-8"));

      x.closeServer();
    } catch (final Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Sendet eine E-Mail.
   * 
   * @param smtphost
   *          SMTP-Server
   * @param from
   *          Absender-Adresse
   * @param to
   *          Empf??nger-Adresse
   * @param from_header
   *          Sender-Header
   * @param to_header
   *          Empf??nger-Header
   * @param subject
   *          Mail-Betreff
   * @param text
   *          Mail-Inhalt
   * 
   *          Bsp.: "Chuck Norris" &lt;chuck@norris.com&gt; Only da best stuff.
   *          Only from Da Chuck!
   */
  public final static boolean SendEMail(String smtphost, String from,
      String to, String from_header, String to_header, String subject,
      String text) {
    try {
      // RFC 2047 - MIME (Multipurpose Internet Mail Extensions) Part
      // Three: Message Header Extensions for Non-ASCII Text
      subject = new String(Base64Coder.encode(subject.getBytes("UTF-8")));
      subject = "=?UTF-8?B?" + subject + "?=";

      final SmtpClient x = new SmtpClient(smtphost);
      x.from(from);
      x.to(to);
      final PrintStream p = x.startMessage();
      p.println("From: " + from_header);
      p.println("To: " + to_header);
      p.println("Subject: " + subject);
      p.println("Content-Type: text/plain;format=flowed;charset=\"utf-8\"");
      p.println("Content-Transfer-Encoding: 8bit");
      p.println();
      p.write(text.getBytes("UTF-8"));

      x.closeServer();
    } catch (final Exception e) {
      return false;
    }
    return true;
  }

  public final static String getConfigValue(Connection conn, String key,
      String module) throws SQLException {
    String retval = null;

    final Statement stmt = conn.createStatement();
    final ResultSet rs = stmt
        .executeQuery("SELECT VALUE FROM CONFIG WHERE UPPER(MODULE) = '"
            + module.toUpperCase() + "' AND UPPER(\"KEY\") LIKE "
            + key.toUpperCase());
    retval = rs.getString("VALUE");

    return retval;
  }

  public final static boolean setConfigValue(Connection conn, String key,
      String value, String module) throws SQLException {
    boolean retval = false;
    final Statement stmt = conn.createStatement();
    final int rows = stmt
        .executeUpdate("INSERT INTO CONFIG (VALUE,KEY,MODULE) VALUES ('"
            + value.toUpperCase() + "','" + key.toUpperCase() + "','"
            + module.toUpperCase() + "')");
    retval = (rows > 0) ? true : false;
    return retval;
  }

  public final static String getStackTrace(Throwable t) {
    final StringBuilder sb = new StringBuilder();
    sb.append(t.getMessage());
    sb.append("\r\n");
    sb.append("Exception:");
    sb.append(t.getClass().getName());
    sb.append("\r\n");
    final StackTraceElement[] ste = t.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      sb.append(ste[i].toString());
      sb.append("\r\n");
    }
    return sb.toString();
  }

  public final static String WordWrap(String input, int len) {
    int lineno = 0;
    final StringBuilder sb = new StringBuilder();

    input = input.replace("\r\n", "\n"); // CRLF normalisieren
    final String[] lines = input.split("\n"); // IN ZEILEN ZERLEGEN

    final LinkedList<String> ll = new LinkedList<String>(Arrays.asList(lines));

    while (lineno < ll.size()) // ALLE ZEILEN DURCHGEHEN
    {
      String line = ll.get(lineno); // ZEILE AUSLESEN
      if (line.length() > len) // ZEILE IST ZU LANG
      {
        // K??RZEN!
        final int cutpos = line.lastIndexOf(" ", len);
        if (cutpos == -1) {
          final String neu = line.substring(len); // REST MERKEN
          ll.add(lineno + 1, neu);
          line = line.substring(0, len); // ZEILE K??RZEN
          ll.set(lineno, line);
        } else {
          final String neu = line.substring(cutpos + 1); // REST
          // MERKEN
          ll.add(lineno + 1, neu);
          line = line.substring(0, cutpos); // ZEILE K??RZEN
          ll.set(lineno, line);
        }

      }

      sb.append(line + "\r\n");
      lineno++;
    }

    return sb.toString();
  }

  // Wer ??ber diese Funktion lacht, kann daf??r in den Keller gehen
  public final static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (final InterruptedException ignored) {
      // ignored
    }
  }

  public final static int signum(long value) {
    if (value > 0)
      return 1;
    if (value < 0)
      return -1;
    return 0;
  }

  public static String stripHtmlTags(String text) {
    String retval = text.replaceAll("\\<[^>]*>", "");
    return retval;
  }

  public static int getRandomSaltLength(int max) {
    Random rand = new Random(System.currentTimeMillis());
    int tmp = rand.nextInt(max);
    if (tmp < 10)
      tmp = getRandomSaltLength(max);
    return tmp;
  }

  public static String getRandomToken(int length, long seed) {
    StringBuilder sb = new StringBuilder();
    Random rand = new Random(System.currentTimeMillis() * seed);
    for (int i = 0; i < length; i++) {
      int tmp = rand.nextInt(length);
      sb.append(tmp);
    }
    return sb.toString();
  }

}