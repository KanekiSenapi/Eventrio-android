package pl.aogiri.eventrio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewEvent extends AppCompatActivity {

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

    }






}
