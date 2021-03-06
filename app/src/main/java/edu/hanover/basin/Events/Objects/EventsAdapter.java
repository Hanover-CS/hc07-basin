package edu.hanover.basin.Events.Objects;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.hanover.basin.Events.Activities.EventDetailsActivity;
import edu.hanover.basin.R;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

/**
 * A class for constructing a EventsAdapter that will be used for display of ListView items that are made from JSONObjects
 * @author Slaton Blickman
 * @see ArrayAdapter
 */
public class EventsAdapter extends ArrayAdapter<JSONObject> {

    /**
     * basic constructor for class that just calls the super method of construction
     * @param context the application context at the time of constructor call.
     * @param events the JSONObjects to be processed.
     */
    public EventsAdapter(Context context, ArrayList<JSONObject> events) {
        super(context, 0, events);
    }

    /**
     * Overrides the default getView to display EventMarker information.
     * List items show the title, time-date, coordinator, and coordinator picture
     * Sets the onClickListener to start EventDetailsActivity
     *
     * @param position the position of the item in the list
     * @param convertView the view to be inflated as an item_event
     * @param parent ViewGroup to use in inflater
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final JSONObject event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.title);
        ProfilePictureView picture = (ProfilePictureView) convertView.findViewById(R.id.picture);
        TextView coordinator = (TextView) convertView.findViewById(R.id.coordinator);
        TextView date_time = (TextView) convertView.findViewById(R.id.date_time);
        // Populate the data into the template view using the data object
        try {
            Log.i("Event list:", event.toString());
            title.setText(event.getString("title"));

            picture.setProfileId(event.getString("facebook_created_by"));
            picture.setPresetSize(ProfilePictureView.SMALL);

            String coordinator_name = event.getString("fname") + " " + event.getString("lname");
            coordinator.setText(coordinator_name);

            //date_time.setText(event.getString("time_start"));
            date_time.setText(event.getString("time_start") + "\n" + event.getString("date"));
            // Return the completed view to render on screen

        }
        catch(JSONException e){
            Log.e("EventsAdapter error", e.toString());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), EventDetailsActivity.class);
                try {
                    Log.e("event", event.toString());
                    intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, event.getString("event_id"));
                }
                catch(JSONException e){
                    Log.e("JSON EXCEPTION", e.toString());
                    try{
                        Log.e("event_id not found", "Using different ID field for event");
                        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, event.getString("_id"));
                    }
                    catch (JSONException e2) {
                        Log.e("JSON EXCEPTION", e2.toString());
                    }
                }
                intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}