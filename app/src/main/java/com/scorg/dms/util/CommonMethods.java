package com.scorg.dms.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scorg.dms.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CommonMethods {

    private static final String TAG = "Dms/Common";
    private static boolean encryptionIsOn = true;
    private static String aBuffer = "";


    public static void showToast(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }


    public static void showSnack(View mViewById, String msg) {
        if (mViewById != null) {
            Snackbar.make(mViewById, msg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Log.d(TAG, "null snacbar view" + msg);
        }
    }


    /**
     * Email validator
     *
     * @param emailId
     * @return
     */
    public final static boolean isValidEmail(CharSequence emailId) {
        if (emailId == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
        }
    }

    /**
     * Returns Message for Error codes
     *
     * @param messageCode
     * @return
     */
    public static String getResponseCodeMessage(String messageCode) {
        String strMessage = "";
        try {
            if (messageCode.length() >= 3) {
                if (messageCode.equalsIgnoreCase("900")) {
                    strMessage = "Registration Error";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMessage;

    }

    public static void hideKeyboard(Activity cntx) {
        // Check if no view has focus:
        View view = cntx.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) cntx.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    //create the new folder in sd card & write the data in text file which is created in that folder.
    public static void DmsLogWriteFile(String title, String text, boolean textAppend) {
        try {
            byte[] keyBytes = getKey("password");
            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/", DmsConstants.DMS_LOG_FOLDER);
            if (!directory.exists()) {
                directory.mkdir();
            }
            //make a new text file in that created new directory/folder
            File file = new File(directory.getPath(), DmsConstants.DMS_LOG_FILE);

            if (!file.exists() && directory.exists()) {
                file.createNewFile();
            }
//            OutputStreamWriter osw;
//            osw = new FileWriter(file, textAppend);
//
//            BufferedWriter out = new BufferedWriter(osw);
//            out.write("************" + getCurrentDateTime() + "************" + title + ": " + text + "\n");
//            out.close();

            OutputStreamWriter osw;
            if (encryptionIsOn) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileOutputStream fos = new FileOutputStream(file, textAppend);
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                osw = new OutputStreamWriter(cos, "UTF-8");
            } else    // not encryptionIsOn
                osw = new FileWriter(file, textAppend);

            BufferedWriter out = new BufferedWriter(osw);
            out.write("************" + getCurrentDateTime() + "************" + title + ": " + text + "\n");
            out.close();


        } catch (Exception e) {
            System.out.println("Encryption Exception " + e);
        }
    }

    private static byte[] getKey(String password) {
        String key = "";
        while (key.length() < 16)
            key += password;
        return key.substring(0, 16).getBytes();
    }

    // read the whole file data with previous data also
    public static String DmsLogReadFile() {

        try {
            byte[] keyBytes = getKey("password");

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/", DmsConstants.DMS_LOG_FOLDER + "/" + DmsConstants.DMS_LOG_FILE);
            InputStreamReader isr;
            if (encryptionIsOn) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileInputStream fis = new FileInputStream(file);
                CipherInputStream cis = new CipherInputStream(fis, cipher);
                isr = new InputStreamReader(cis, "UTF-8");
            } else
                isr = new FileReader(file);

            BufferedReader in = new BufferedReader(isr);
            //	    		String line = in.readLine();
            StringBuffer s = new StringBuffer();
            int cr = 0;
            while ((cr = in.read()) != -1) {
                s.append((char) cr);
            }
            aBuffer = s.toString();
            CommonMethods.Log(TAG, "Text read: " + aBuffer);
            in.close();
            return aBuffer;
        } catch (Exception e) {
            System.out.println("Decryption Exception " + e);
        }
        return aBuffer;
    }

    public static String getCurrentDateTime() // for enrollmentId
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        String Year = String.valueOf(year);
        StringBuffer dString = new StringBuffer();
        dString.append("Date:");
        dString.append((date > 9) ? String.valueOf(date) : ("0" + date));
        dString.append("/");
        dString.append((month > 9) ? String.valueOf(month) : ("0" + month));
        dString.append("/");
        dString.append(Year.substring(2, 4));
        dString.append(" Time:");
        dString.append((hour > 9) ? String.valueOf(hour) : ("0" + hour));
        dString.append("_");
        dString.append((minute > 9) ? String.valueOf(minute) : ("0" + minute));
        dString.append("_");
        dString.append((sec > 9) ? String.valueOf(sec) : ("0" + sec));
        return dString.toString();
    }

    public static void showSnack(Context mContext, View mViewById, String msg) {
        Snackbar snack = Snackbar.make(mViewById, msg, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(mContext, R.color.errorColor));
        snack.show();
    }

    public static String splitToComponentTimes(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        String hrStr = (hr < 10 ? "0" : "") + hr;
        String mnStr = (mn < 10 ? "0" : "") + mn;
        String secStr = (sec < 10 ? "0" : "") + sec;
        return hrStr + ":" + mnStr + ":" + secStr;
    }

    public static String getHoursFromSeconds(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        String hrStr = (hr < 10 ? "0" : "") + hr;
        String mnStr = (mn < 10 ? "0" : "") + mn;
        String secStr = (sec < 10 ? "0" : "") + sec;
        float hour = (float) Integer.parseInt(hrStr);
        float minuite = (float) Integer.parseInt(mnStr);
        if (minuite > 0) {
            minuite = minuite / 60;
        }

        float finalvalue = hour + minuite;
        return "" + new DecimalFormat("##.##").format(finalvalue);
    }

    public static boolean comparedTwoTimezone(String serverdatetimezone) {
        String str1 = serverdatetimezone.substring(0, serverdatetimezone.indexOf(" "));
        String str2 = serverdatetimezone.substring(serverdatetimezone.indexOf(" ") + 1, serverdatetimezone.indexOf("+"));
        String finalserverdatestr = str1 + "T" + str2 + "Z";
        boolean flag = false;
        Date ds = new Date();
        DateTime d = new DateTime(ds);
        DateTime e = new DateTime(finalserverdatestr/*"2016-02-24T07:35:00Z"*/);  //2016-02-24 07:35:00+0000
        if (e.toLocalDateTime().isAfter(d.toLocalDateTime())) {
            flag = true;
//            System.out.println("After" + e.toLocalDateTime().isAfter(d.toLocalDateTime()));
        } else {
//            System.out.println("Before"+e.toLocalDateTime().isBefore(d.toLocalDateTime()));
        }
        return flag;
    }

    public static String dateFormatUtcToIst(String utcText, String utcPattern, String utcNewPattern, String finalPattern, String toTimeZone) {
        String finalDate = "";
        try {
            DateTimeFormatter utcFormatter = DateTimeFormat
                    .forPattern(utcPattern)
                    .withLocale(Locale.US)
                    .withZoneUTC();
            DateTimeZone indianZone = DateTimeZone.forID(toTimeZone);
            DateTimeFormatter indianZoneFormatter = utcFormatter.withZone(indianZone);


            DateTime parsed = utcFormatter.parseDateTime(utcText);
            String indianText = indianZoneFormatter.print(parsed);

            final SimpleDateFormat sdf = new SimpleDateFormat(utcNewPattern);
            final Date dateObj1;

            dateObj1 = sdf.parse(indianText);

            finalDate = new SimpleDateFormat(finalPattern).format(dateObj1);
            return finalDate;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return finalDate;
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static int getTimeStampDifference(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date secondParsedDate = null;
        Date firstParsedDate = null;
        long diff = 0;
        int mDiff = 0;
        try {
            firstParsedDate = dateFormat.parse(startTime);
            secondParsedDate = dateFormat.parse(endTime);
            diff = secondParsedDate.getTime() - firstParsedDate.getTime();
            mDiff = (int) (diff / 1000) % 60;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return mDiff;
    }

    public static void dateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("difference : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void Log(String tag, String message) {
        Log.e(tag, message);
    }


    public static String yyyy_dd__mm_To_Words(String date) {
        String output = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = simpleDateFormat.parse(date);
            simpleDateFormat.applyPattern("dd MMM yyyy");
            output = simpleDateFormat.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static View loadView(int resourceName, Context mActivity) {

        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // param.gravity = Gravity.CENTER;
        View child = inflater.inflate(resourceName, null);
        LinearLayout l1 = new LinearLayout(mActivity);
        child.setLayoutParams(param);

        l1.setLayoutParams(param);
        l1.addView(child);
        return l1;
    }
}

