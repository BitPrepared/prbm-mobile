package it.bitprepared.prbm.mobile.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;
import it.bitprepared.prbm.mobile.model.entities.EntityCuriosity;
import it.bitprepared.prbm.mobile.model.entities.EntityFauna;
import it.bitprepared.prbm.mobile.model.entities.EntityFlower;
import it.bitprepared.prbm.mobile.model.entities.EntityInterview;
import it.bitprepared.prbm.mobile.model.entities.EntityMonument;
import it.bitprepared.prbm.mobile.model.entities.EntityNews;
import it.bitprepared.prbm.mobile.model.entities.EntityPanorama;
import it.bitprepared.prbm.mobile.model.entities.EntityTree;
import it.bitprepared.prbm.mobile.model.entities.EntityWeather;

/**
 * Created by nicola on 19/08/15.
 */
public class PrbmAddEntityActivity extends Activity {

    /** Debug TAG */
    private final static String TAG = "PrbmAddEntityActivity";

    private ListView lstAvailableEntities = null;
    private PrbmAvailableEntitiesAdapter adtEntities = null;

    private List<PrbmEntity> availableEntities = null;

    public static Activity self = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.self = this;

        super.onCreate(savedInstanceState);

        // Setting up Home back button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        availableEntities = new ArrayList<>();
        availableEntities.add(new EntityFlower());
        availableEntities.add(new EntityTree());
        availableEntities.add(new EntityFauna());
        availableEntities.add(new EntityPanorama());
        availableEntities.add(new EntityWeather());
        availableEntities.add(new EntityMonument());
        availableEntities.add(new EntityInterview());
        availableEntities.add(new EntityNews());
        availableEntities.add(new EntityCuriosity());

        setContentView(R.layout.activity_add_entity);
        lstAvailableEntities = (ListView) findViewById(R.id.lstAvailableEntities);

        adtEntities = new PrbmAvailableEntitiesAdapter(PrbmAddEntityActivity.this,
                R.layout.list_available_entities, availableEntities);
        lstAvailableEntities.setAdapter(adtEntities);
        lstAvailableEntities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                UserData.getInstance().setEntity(availableEntities.get(position));
                Intent prbmEntityModify = new Intent(PrbmAddEntityActivity.this, EntityActivity.class);
                prbmEntityModify.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(prbmEntityModify);
                setResult(RESULT_OK);
//                finish();
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    public class PrbmAvailableEntitiesAdapter extends ArrayAdapter<PrbmEntity> implements View.OnClickListener {

        /** Execution context */
        private Context c;

        private List<Bitmap> backBitmaps;

        /**
         * Base constructor
         * @param context  Execution context
         * @param resource Related resource
         * @param objects  List of PrbmUnits
         */
        public PrbmAvailableEntitiesAdapter(Context context, int resource, List<PrbmEntity> objects) {
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

            PrbmEntity unit = getItem(position);

            if (convertView == null) {

                // Se la convertView e' nulla la devo caricare la prima volta
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_available_entities, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.frmEntityList = (FrameLayout) convertView.findViewById(R.id.frmEntityList);
                viewHolder.imgEntityBackground = (ImageView) convertView.findViewById(R.id.imgBackEntity);
                viewHolder.txtEntityTitle = (TextView) convertView.findViewById(R.id.txtEntityTitle);
                viewHolder.txtEntityDescription = (TextView) convertView.findViewById(R.id.txtEntityDescription);

                // Set the tag tag
                convertView.setTag(viewHolder);
                convertView.setLongClickable(true);
            } else {
                // Restore tag
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.frmEntityList.setOnClickListener(this);
            viewHolder.imgEntityBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.imgEntityBackground.setImageBitmap(UserData.getInstance().getBackBitmap(unit.getIdListImage()));
            viewHolder.txtEntityTitle.setText(unit.getType());
            viewHolder.txtEntityDescription.setText(unit.getTypeDescription());

            return convertView;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(PrbmAddEntityActivity.this, getString(R.string.long_press_to_choose), Toast.LENGTH_SHORT).show();
        }

        /**
         * Viewholder class used to improve performance
         * @author Nicola Corti
         */
        private class ViewHolder {
            private FrameLayout frmEntityList;
            private TextView txtEntityTitle;
            private TextView txtEntityDescription;
            private ImageView imgEntityBackground;
        }
    }
}
