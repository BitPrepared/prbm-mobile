package it.bitprepared.prbm.mobile.activity;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.EntityField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Support class used to delegate view creation and management for Entities objects
 */
public class EntityViewHelper {

    public static void addExtraField(Context c, LinearLayout lin, EntityField field) {
        switch (field.type){
            case LONG_TEXT:
                addLongEditText(c, lin, field.getID(), field.title, field.hint, 3);
                break;
            case SHORT_TEXT:
                addShortEditText(c, lin, field.getID(), field.title, field.hint);
                break;
            case NUMERIC:
                addNumericTextView(c, lin, field.getID(), field.title);
                break;
            case DATE:
                addDatePicker(c, lin, field.getID(), field.title);
                break;
        }
    }

    /**
     * Static method used to append a short editText to a linear layout
     * @param c     The execution context
     * @param lin   A linear layout that will receive new views
     * @param ID    View ID
     * @param title Descriptive title
     * @param hint  Text hint for textbox
     */
    private static void addShortEditText(Context c, LinearLayout lin, int ID, String title, String hint) {
        TextView descText = new TextView(c);
        EditText shortEdt = new EditText(c);
        shortEdt.setId(ID);

        descText.setTextColor(c.getResources().getColor(R.color.white));
        shortEdt.setTextColor(c.getResources().getColor(R.color.white));
        shortEdt.setHintTextColor(c.getResources().getColor(R.color.light_gray));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 0);
        descText.setLayoutParams(params);
        shortEdt.setLayoutParams(params);

        descText.setTextSize(18);
        shortEdt.setEms(10);

        descText.setText(title);
        shortEdt.setHint(hint);

        lin.addView(descText);
        lin.addView(shortEdt);
    }


    /**
     * Static method used to append a long editText to a linear layout
     * @param c     The execution context
     * @param lin   A linear layout that will receive new views
     * @param ID    View ID
     * @param title Descriptive title
     * @param hint  Text hint for textbox
     * @param lines Number of lines to be set in edit text
     */
    private static void addLongEditText(Context c, LinearLayout lin, int ID, String title, String hint, int lines) {
        TextView descText = new TextView(c);
        EditText longEdt = new EditText(c);
        longEdt.setId(ID);

        descText.setTextColor(c.getResources().getColor(R.color.white));
        longEdt.setTextColor(c.getResources().getColor(R.color.white));
        longEdt.setHintTextColor(c.getResources().getColor(R.color.light_gray));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 0);
        descText.setLayoutParams(params);
        longEdt.setLayoutParams(params);

        descText.setTextSize(18);
        longEdt.setLines(lines);
        longEdt.setMaxLines(lines);
        longEdt.setMinLines(lines);

        descText.setText(title);
        longEdt.setHint(hint);

        lin.addView(descText);
        lin.addView(longEdt);
    }


    /**
     * Static method used to append a numeric textbox to a linear layout
     * @param c     The execution context
     * @param lin   A linear layout that will receive new views
     * @param ID    View ID
     * @param title Descriptive title
     */
    private static void addNumericTextView(Context c, LinearLayout lin, int ID, String title) {

        // Views are organized with an horizontal linearlayout
        LinearLayout linNumeric = new LinearLayout(c);
        linNumeric.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 15, 15, 0);
        linNumeric.setLayoutParams(params);

        TextView numericTextView = new TextView(c);
        EditText numericEditText = new EditText(c);
        numericEditText.setId(ID);

        numericTextView.setTextColor(c.getResources().getColor(R.color.white));
        numericEditText.setTextColor(c.getResources().getColor(R.color.white));

        LinearLayout.LayoutParams paramsSon = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 0, 0);
        numericTextView.setLayoutParams(paramsSon);
        numericEditText.setLayoutParams(paramsSon);

        //Setting numeric input type
        numericEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        numericTextView.setTextSize(18);
        numericEditText.setEms(5);
        numericEditText.setText("0");
        numericTextView.setText(title);


        linNumeric.addView(numericTextView);
        linNumeric.addView(numericEditText);
        lin.addView(linNumeric);
    }


    /**
     * Static method used to append a Date picker to linear layout
     * @param c     The execution context
     * @param lin   A linear layout that will receive new views
     * @param ID    View ID
     * @param title Descriptive title
     */
    private static void addDatePicker(Context c, LinearLayout lin, int ID, String title) {

        // Date is set to actual
        TextView shortTextView = new TextView(c);
        DatePicker datePickerView = new DatePicker(c);
        datePickerView.setId(ID);

        shortTextView.setTextColor(c.getResources().getColor(R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 0);
        shortTextView.setLayoutParams(params);

        LinearLayout.LayoutParams paramsDat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsDat.setMargins(15, 15, 15, 0);
        datePickerView.setLayoutParams(paramsDat);

        shortTextView.setTextSize(18);
        shortTextView.setText(title);

        lin.addView(shortTextView);
        lin.addView(datePickerView);
    }

    /**
     * Static method used to save a set of Fields to a String array
     * @param extraFields Array of String. If string are present they will be override,
     *                    otherwise new strings will be create
     * @param linFree     Linear Layout containing fields
     */
    public static void saveLinearLayoutFields(EntityField[] extraFields, LinearLayout linFree) {
        boolean empty = (extraFields.length == 0);
        for (EntityField extraField : extraFields) {
            View v = linFree.findViewById(extraField.getID());
            String field = null;
            if (v instanceof EditText) {
                EditText edt = (EditText) v;
                field = edt.getText().toString();
            } else if (v instanceof DatePicker) {
                DatePicker dat = (DatePicker) v;
                Calendar c = Calendar.getInstance(Locale.ITALY);
                c.set(dat.getYear(), dat.getMonth(), dat.getDayOfMonth());
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                field = dateFormat.format(c.getTime());
            }
            extraField.setValue(field);
        }
    }

    /**
     * Static method used to populate a set of fields with values from String array
     * @param extraFields Array of string, values that will be displayed in views
     * @param linFree     Linear Layout containing fields
     */
    public static void restoreLinearLayoutFields(EntityField[] extraFields, LinearLayout linFree) {
        if (extraFields.length == 0) return;

        for (EntityField extraField : extraFields) {
            View v = linFree.findViewById(extraField.getID());
            if (v instanceof EditText) {
                EditText edt = (EditText) v;
                edt.setText(extraField.getValue());
            } else if (v instanceof DatePicker) {
                DatePicker dat = (DatePicker) v;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                Calendar c = Calendar.getInstance(Locale.ITALY);
                try {
                    c.setTime(dateFormat.parse(extraField.getValue()));
                } catch (ParseException e) {
                    c.setTime(new Date());
                }
                dat.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                               c.get(Calendar.DAY_OF_MONTH));
            }
        }
    }
}