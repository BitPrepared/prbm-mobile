package it.bitprepared.prbm.mobile.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.entities.EntityBuilding;
import it.bitprepared.prbm.mobile.model.entities.EntityCuriosity;
import it.bitprepared.prbm.mobile.model.entities.EntityFauna;
import it.bitprepared.prbm.mobile.model.entities.EntityFlower;
import it.bitprepared.prbm.mobile.model.entities.EntityInterview;
import it.bitprepared.prbm.mobile.model.entities.EntityMonument;
import it.bitprepared.prbm.mobile.model.entities.EntityNews;
import it.bitprepared.prbm.mobile.model.entities.EntityOther;
import it.bitprepared.prbm.mobile.model.entities.EntityPanorama;
import it.bitprepared.prbm.mobile.model.entities.EntityTree;
import it.bitprepared.prbm.mobile.model.entities.EntityWeather;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for visualizing list of available entities
 */
public class PrbmAddEntityActivity extends Activity  {

    /** Debug TAG */
    private final static String TAG = "PrbmAddEntityActivity";

    /** Reference to listview of Available entities */
    private ListView lstAvailableEntities = null;
    /** Reference to adapter of Available entities */
    private PrbmAvailableEntitiesAdapter adtEntities = null;
    /** List of availabe entities instances */
    private List<PrbmEntity> availableEntities = null;

    /** Reference to self, for activity delayed finalization */
    public static Activity self = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.self = this;
        super.onCreate(savedInstanceState);

        // Setting up Home back button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        ////////////////////////////////////////////
        //
        //  List of available entries is here
        //
        availableEntities = new ArrayList<>();
        availableEntities.add(new EntityFlower());
        availableEntities.add(new EntityTree());
        availableEntities.add(new EntityFauna());
        availableEntities.add(new EntityBuilding());
        availableEntities.add(new EntityPanorama());
        availableEntities.add(new EntityWeather());
        availableEntities.add(new EntityMonument());
        availableEntities.add(new EntityInterview());
        availableEntities.add(new EntityNews());
        availableEntities.add(new EntityCuriosity());
        availableEntities.add(new EntityOther());
        //
        ////////////////////////////////////////////


        // Building listview and adapter
        setContentView(R.layout.activity_add_entity);
        lstAvailableEntities = (ListView) findViewById(R.id.lstAvailableEntities);
        adtEntities = new PrbmAvailableEntitiesAdapter(PrbmAddEntityActivity.this,
                R.layout.list_available_entities, availableEntities);
        lstAvailableEntities.setAdapter(adtEntities);
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

    /**
     * Adapter used to display available entities
     */
    public class PrbmAvailableEntitiesAdapter extends ArrayAdapter<PrbmEntity> {

        /** Execution context */
        private Context c;

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
         * Method to obtain current view in an optimized manner
         * @param position    Position in list
         * @param convertView ConvertView returned at each invocation
         * @param parent      Parent viewgroup
         * @return Just created/rendered view
         */
        public View getViewOptimize(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            PrbmEntity unit = getItem(position);

            if (convertView == null) {

                // If convert view is null, I must inflate
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_available_entities, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.frmEntityList = (FrameLayout) convertView.findViewById(R.id.frmEntityList);
                viewHolder.imgEntityBackground = (ImageView) convertView.findViewById(R.id.imgBackEntity);
                viewHolder.txtEntityTitle = (TextView) convertView.findViewById(R.id.txtEntityTitle);
                viewHolder.txtEntityDescription = (TextView) convertView.findViewById(R.id.txtEntityDescription);

                // Set the tag
                convertView.setTag(viewHolder);
                convertView.setLongClickable(true);
            } else {
                // Restore tag
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.frmEntityList.setOnClickListener(v -> {
                UserData.getInstance().setEntity(availableEntities.get(position));
                Intent prbmEntityModify = new Intent(PrbmAddEntityActivity.this, EntityActivity.class);
                prbmEntityModify.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(prbmEntityModify);
                setResult(RESULT_OK);
            });
            viewHolder.imgEntityBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.imgEntityBackground.setImageBitmap(UserData.getInstance().getBackBitmap(unit.getIdListImage()));
            viewHolder.txtEntityTitle.setText(unit.getType());
            viewHolder.txtEntityDescription.setText(unit.getTypeDescription());

            return convertView;
        }

        /**
         * Viewholder class used to improve performance
         */
        private class ViewHolder {
            /** Reference to parent FrameLayout */
            private FrameLayout frmEntityList;
            /** Reference to entity title textview */
            private TextView txtEntityTitle;
            /** Reference to entity description textview */
            private TextView txtEntityDescription;
            /** Reference to image background view */
            private ImageView imgEntityBackground;
        }
    }
}
