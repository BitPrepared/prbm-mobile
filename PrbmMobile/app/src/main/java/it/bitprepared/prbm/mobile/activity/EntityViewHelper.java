package it.bitprepared.prbm.mobile.activity;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.bitprepared.prbm.mobile.R;

/**
 * Created by nicola on 19/08/15.
 */
public class EntityViewHelper {

    public static void addShortTextView(Context c, LinearLayout lin, int ID, String title, String hint) {
        TextView shortTextView = new TextView(c);
        EditText shortEditText = new EditText(c);
        shortEditText.setId(ID);

        shortTextView.setTextColor(c.getResources().getColor(R.color.White));
        shortEditText.setTextColor(c.getResources().getColor(R.color.White));
        shortEditText.setHintTextColor(c.getResources().getColor(R.color.LightGray));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 0);
        shortTextView.setLayoutParams(params);
        shortEditText.setLayoutParams(params);

        shortTextView.setTextSize(18);
        shortEditText.setEms(10);

        shortTextView.setText(title);
        shortEditText.setHint(hint);

        lin.addView(shortTextView);
        lin.addView(shortEditText);
    }

    public static void addLongTextView(Context c, LinearLayout lin, int ID, String title, String hint, int lines) {
        TextView shortTextView = new TextView(c);
        EditText shortEditText = new EditText(c);
        shortEditText.setId(ID);

        shortTextView.setTextColor(c.getResources().getColor(R.color.White));
        shortEditText.setTextColor(c.getResources().getColor(R.color.White));
        shortEditText.setHintTextColor(c.getResources().getColor(R.color.LightGray));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 0);
        shortTextView.setLayoutParams(params);
        shortEditText.setLayoutParams(params);

        shortTextView.setTextSize(18);
        shortEditText.setLines(lines);
        shortEditText.setMaxLines(lines);
        shortEditText.setMinLines(lines);

        shortTextView.setText(title);
        shortEditText.setHint(hint);

        lin.addView(shortTextView);
        lin.addView(shortEditText);
    }

    public static void addNumericTextView(Context c, LinearLayout lin, int ID, String title) {

        LinearLayout linNumeric = new LinearLayout(c);
        linNumeric.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 15, 15, 0);
        linNumeric.setLayoutParams(params);

        TextView numericTextView = new TextView(c);
        EditText numericEditText = new EditText(c);
        numericEditText.setId(ID);

        numericTextView.setTextColor(c.getResources().getColor(R.color.White));
        numericEditText.setTextColor(c.getResources().getColor(R.color.White));

        LinearLayout.LayoutParams paramsSon = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 0, 0);
        numericTextView.setLayoutParams(paramsSon);
        numericEditText.setLayoutParams(paramsSon);
        numericEditText.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        numericTextView.setTextSize(18);
        numericEditText.setEms(5);
        numericEditText.setText("0");

        numericTextView.setText(title);

        linNumeric.addView(numericTextView);
        linNumeric.addView(numericEditText);
        lin.addView(linNumeric);
    }

    public static void addDatePicker(Context c, LinearLayout lin, int ID, String title) {
        TextView shortTextView = new TextView(c);
        DatePicker datePickerView = new DatePicker(c);
        datePickerView.setId(ID);

        shortTextView.setTextColor(c.getResources().getColor(R.color.White));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 0);
        shortTextView.setLayoutParams(params);

        LinearLayout.LayoutParams paramsDat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsDat.setMargins(15, 15, 15, 0);
        datePickerView.setLayoutParams(paramsDat);

        shortTextView.setTextSize(18);
        shortTextView.setText(title);
//        datePickerView.setCalendarViewShown(false);
//        datePickerView.setSpinnersShown(false);
//
//        Date date = new Date();
//        Calendar cal = Calendar.getInstance(Locale.ITALY);
//        cal.setTime(date);
//        datePickerView.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        lin.addView(shortTextView);
        lin.addView(datePickerView);
    }

    public static void saveLinearLayoutFields(List<String> extraFields, int[] id_fields, LinearLayout linFree) {
        boolean empty = (extraFields.size() == 0);
        for (int i = 0; i < id_fields.length; i++) {
            View v = linFree.findViewById(id_fields[i]);
            String field = null;
            if (v instanceof EditText){
                EditText edt = (EditText)v;
                field = edt.getText().toString();
            } else if (v instanceof DatePicker) {
                DatePicker dat = (DatePicker)v;
                Calendar c = Calendar.getInstance(Locale.ITALY);
                c.set(dat.getYear(), dat.getMonth(), dat.getDayOfMonth());
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                field = dateFormat.format(c.getTime());
            }
            if (empty)
                extraFields.add(field);
            else
                extraFields.set(i, field);
        }
    }

    public static void restoreLinearLayoutFields(List<String> extraFields, int[] id_fields, LinearLayout linFree) {
        boolean empty = (extraFields.size() == 0);
        if (empty) return;

        for (int i = 0; i < id_fields.length; i++) {
            View v = linFree.findViewById(id_fields[i]);
            String field = null;
            if (v instanceof EditText){
                EditText edt = (EditText)v;
                edt.setText(extraFields.get(i));
            } else if (v instanceof DatePicker) {
                DatePicker dat = (DatePicker)v;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                Calendar c = Calendar.getInstance(Locale.ITALY);
                try {
                    c.setTime(dateFormat.parse(extraFields.get(i)));
                } catch (ParseException e) {
                    c.setTime(new Date());
                }
                dat.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            }
        }
    }
}
