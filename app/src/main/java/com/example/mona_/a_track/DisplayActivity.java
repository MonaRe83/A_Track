package com.example.mona_.a_track;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DisplayActivity extends AppCompatActivity {

    private SoapPrimitive result;
    Button popin, save;
    EditText class_id;
    EditText subject;
    EditText assignment;
    EditText username;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Display user name inserted into page when logged in
        String username = getIntent().getStringExtra("Username");
        TextView tv = (TextView) findViewById(R.id.TVusername);
        tv.setText(username);

        class_id = (EditText) findViewById(R.id.eT_class_id);
        subject = (EditText) findViewById(R.id.eT_class_subject);
        assignment = (EditText) findViewById(R.id.eT_assignment);

    }

    //  On click listner for button
    private View.OnClickListener myButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Execute the Async class
            AssignmentInfo assignmentInfo = new AssignmentInfo();
            assignmentInfo.setAssignment(assignment.getText().toString());
            assignmentInfo.setClass_id(Integer.parseInt(class_id.getText().toString()));
            assignmentInfo.setSubject(subject.getText().toString());

            new CreateAssignmentTask(assignmentInfo).execute();
        }
    };

    // Soap protocol to call method Create Assignment from web service and using the query
    private class CreateAssignmentTask extends AsyncTask < Void, Void, Void > {

        private static final String SOAP_ACTION = "http://tempuri.org/IService1/CreateAssignment";
        private static final String METHOD_NAME = "CreateAssignment";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://10.0.0.10:8080/atrack/Service1.svc";

        AssignmentInfo assignment;

        public CreateAssignmentTask(AssignmentInfo assignment) {

            this.assignment = assignment;
        }

        @Override
        protected Void doInBackground(Void...arg0) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("class_id", assignment.class_id);
                request.addProperty("subject", assignment.subject);
                request.addProperty("assignment", assignment.assignment);


                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    // Button for saving assignment information
    public void OnSaveClick(View v) {

        EditText class_id = (EditText) findViewById(R.id.eT_class_id);
        EditText subject = (EditText) findViewById(R.id.eT_class_subject);
        EditText assignment = (EditText) findViewById(R.id.eT_assignment);

        String subjectstr = subject.getText().toString();
        String assignmentstr = assignment.getText().toString();

        int c_id = 0;
        String class_idstr = class_id.getText().toString();

        // Show display message when created
        Toast pass2 = Toast.makeText(DisplayActivity.this, "Assignment Note Created ", Toast.LENGTH_SHORT);
        pass2.show();
        if (class_idstr.isEmpty()) {
            Toast pass = Toast.makeText(DisplayActivity.this, "Enter subject ID ", Toast.LENGTH_SHORT);
            pass.show();
        } else {
            c_id = Integer.parseInt(class_idstr);
        }

        if (subjectstr.isEmpty()) {
            Toast pass = Toast.makeText(DisplayActivity.this, "Enter Subject", Toast.LENGTH_SHORT);
            pass.show();
        }

        if (assignmentstr.isEmpty()) {
            Toast pass = Toast.makeText(DisplayActivity.this, "Enter Assignment ", Toast.LENGTH_SHORT);
            pass.show();
        } else {
            // Using class user info
            AssignmentInfo ai = new AssignmentInfo();
            ai.setClass_id(c_id);
            ai.setSubject(subjectstr);
            ai.setAssignment(assignmentstr);

            new CreateAssignmentTask(ai).execute();

            // Go to page when created
            Intent i = new Intent(DisplayActivity.this,AssignmentActivity.class);
            i.putExtra("class_id", c_id);
            startActivity(i);
        }
    }
    // Button for view notes , allows to only view notes with a valid id entered
    public void OnViewClick(View view) {
        int c_id = 0;
        String class_idstr = class_id.getText().toString();

        EditText class_id = (EditText) findViewById(R.id.eT_class_id);

        if (class_idstr.isEmpty()) {
            Toast pass = Toast.makeText(DisplayActivity.this, "error ", Toast.LENGTH_SHORT);
            pass.show();
        } else {
            c_id = Integer.parseInt(class_idstr);
        }

        Intent i = new Intent(DisplayActivity.this,AssignmentActivity.class);
        i.putExtra("class_id", c_id);
        startActivity(i);
    }

}