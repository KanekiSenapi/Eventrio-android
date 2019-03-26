package pl.aogiri.eventrio.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.aogiri.eventrio.R;

public class NewEvent extends AppCompatActivity {

    private Context CONTEXT;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG = "NewEventActivity";


    private EditText eventName;
    private EditText eventDateBeg;
    private EditText eventDateEnd;
    private EditText eventAddress;
    private EditText eventDescription;
    private EditText eventTags;
    private EditText eventImage;
    private CheckBox checkBoxPrivatePolicy;
    private CheckBox checkBox2;
    private Button createEventButton;

    private void assignViews() {
        CONTEXT = this;
        eventName = findViewById(R.id.eventName);
        eventDateBeg = findViewById(R.id.eventDateBeg);
        eventDateEnd = findViewById(R.id.eventDateEnd);
        eventAddress = findViewById(R.id.eventAddress);
        eventDescription = findViewById(R.id.eventDescription);
        eventTags = findViewById(R.id.eventTags);
        eventImage = findViewById(R.id.eventImage);
        checkBoxPrivatePolicy = findViewById(R.id.checkBoxPrivatePolicy);
        checkBox2 = findViewById(R.id.checkBox2);
        createEventButton = findViewById(R.id.createEventButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        assignViews();

        eventDateBeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateCalendar(eventDateBeg);
            }
        });

        eventDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateCalendar(eventDateEnd);
            }
        });

//TODO address but without costs
//        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
//        PlacesClient placesClient = Places.createClient(this);
//
//        // Set the fields to specify which types of place data to
//        // return after the user has made a selection.
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
//
//        // Start the autocomplete intent.
//        Intent intent = new Autocomplete.IntentBuilder(
//                AutocompleteActivityMode.OVERLAY, fields)
//                .build(this);
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    private void setDateCalendar(final EditText toR){
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR_OF_DAY);
        final int minute=calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CONTEXT, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(CONTEXT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            toR.setText(checkZero(dayOfMonth) + "-" + checkZero((month + 1)) + "-" + year + " " + checkZero(hourOfDay) + ":" + checkZero(minute));

                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private String checkZero(int number){
        if(number<10)
            return "0"+number;
        return Integer.toString(number);
    }




}
