/*   This file is part of PrbmMobile
 *
 *   PrbmMobile is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   PrbmMobile is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with PrbmMobile.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.bitprepared.prbm.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Adapter used to render list of Prbms
 * @author Nicola Corti
 */
public class PrbmAdapter extends ArrayAdapter<Prbm> {

    /** Execution context */
    private Context c;

    /**
     * Base constructor
     * @param context  Execution context
     * @param resource Related resource
     * @param objects  List of PrbmUnits
     */
    public PrbmAdapter(Context context, int resource, List<Prbm> objects) {
        super(context, resource, objects);
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewOptimize(position, convertView, parent);
    }

    /**
     * Metoto per ottenere la view in modo ottimizzato
     * @param position    Posizione nella lista
     * @param convertView Vista che viene ritornata ad ogni invocazione
     * @param parent      ViewGroup padre della vista
     * @return La nuova vista disegnata
     */
    public View getViewOptimize(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        final Prbm prbm = getItem(position);

        if (convertView == null) {

            // Se la convertView e' nulla la devo caricare la prima volta
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_prbm, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.txtListPrbmname);
            viewHolder.txtAuthors = (TextView)convertView.findViewById(R.id.txtListAuthors);
            viewHolder.txtPlaceDate = (TextView)convertView.findViewById(R.id.txtListPlaceDate);
            // Set the tag tag
            convertView.setTag(viewHolder);
        } else {
            // Restore tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTitle.setText(prbm.getTitle());
        String authors = prbm.getAuthors();
        String place = prbm.getPlace();
        String date = prbm.getDate();
        if (authors == null || authors.contentEquals("")){
            viewHolder.txtAuthors.setVisibility(View.GONE);
        } else {
            viewHolder.txtAuthors.setText("Autori: " + authors);
        }

        if (place == null && date == null ||
                (place != null && date != null && place.contentEquals("") && date.contentEquals(""))){
            viewHolder.txtPlaceDate.setVisibility(View.GONE);
        } else {
            String toView = "";
            if (place != null && !place.contentEquals("")){
                toView += "Luogo: " + place + " ";
            }
            if (date != null && !date.contentEquals("")){
                toView += "Data: " + date;
            }
            viewHolder.txtPlaceDate.setText(toView);
        }

        return convertView;
    }

    /**
     * Viewholder class used to improve performance
     * @author Nicola Corti
     */
    private class ViewHolder {
        /** Reference to Azimut label */
        public TextView txtTitle;
        /** Reference to Meters label */
        public TextView txtAuthors;
        /** Reference to Steps label */
        public TextView txtPlaceDate;
    }
}
