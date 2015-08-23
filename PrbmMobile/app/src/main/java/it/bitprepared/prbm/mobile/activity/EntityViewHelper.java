package it.bitprepared.prbm.mobile.activity;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import it.bitprepared.prbm.mobile.R;

/**
 * Created by nicola on 19/08/15.
 */
public class EntityViewHelper {

    public static void addShortTextView(Context c, LinearLayout lin, int ID, String title, String hint){
        TextView shortTextView = new TextView(c);
        EditText shortEditText = new EditText(c);
        shortEditText.setId(ID);

        shortTextView.setTextColor(c.getResources().getColor(R.color.White));
        shortEditText.setTextColor(c.getResources().getColor(R.color.White));
        shortEditText.setHintTextColor(c.getResources().getColor(R.color.LightGray));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,0);
        shortTextView.setLayoutParams(params);
        shortEditText.setLayoutParams(params);

        shortTextView.setTextSize(18);
        shortEditText.setEms(10);

        shortTextView.setText(title);
        shortEditText.setHint(hint);

        lin.addView(shortTextView);
        lin.addView(shortEditText);
    }

    public static void addLongTextView(Context c, LinearLayout lin, int ID, String title, String hint, int lines){
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
    public static void addNumericTextView(Context c, LinearLayout lin, int ID, String title){
        
        LinearLayout linNumeric = new LinearLayout(c);
        linNumeric.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,15,15,0);
        linNumeric.setLayoutParams(params);

        TextView numericTextView = new TextView(c);
        EditText numericEditText = new EditText(c);
        numericEditText.setId(ID);

        numericTextView.setTextColor(c.getResources().getColor(R.color.White));
        numericEditText.setTextColor(c.getResources().getColor(R.color.White));

        LinearLayout.LayoutParams paramsSon = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,0,0,0);
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
    public static View addDatePicker(){

        return null;
    }

    public static void saveLinearLayoutFields(List<String> extraFields, int[] id_fields, LinearLayout linFree){
        if (extraFields.size() == 0){
            for (int i = 0; i < id_fields.length; i++){
                EditText edt = (EditText)linFree.findViewById(id_fields[i]);
                extraFields.add(edt.getText().toString());
            }
        } else {
            for (int i = 0; i < id_fields.length; i++){
                EditText edt = (EditText)linFree.findViewById(id_fields[i]);
                extraFields.set(i, edt.getText().toString());
            }
        }
    }

    public static void restoreLinearLayoutFields(List<String> extraFields, int[] id_fields, LinearLayout linFree){
        if (extraFields.size() == 0){
            return;
        } else {
            for (int i = 0; i < id_fields.length; i++){
                EditText edt = (EditText)linFree.findViewById(id_fields[i]);
                edt.setText(extraFields.get(i));
            }
        }
    }
}
