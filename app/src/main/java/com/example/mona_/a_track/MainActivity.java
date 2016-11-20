package com.example.mona_.a_track;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this, null, null, 1);
    DatabaseHelper db;

    private SoapPrimitive result;
    Button login, save;
    EditText class_id;
    EditText subject;
    EditText assignment;
    EditText username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this, null, null, 1);

        class_id = (EditText) findViewById(R.id.eT_class_id);
        subject = (EditText) findViewById(R.id.eT_class_subject);
        assignment = (EditText) findViewById(R.id.eT_assignment);
    }

    private View.OnClickListener myButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //simply execute the Async class
            AssignmentInfo assignmentInfo = new AssignmentInfo();
            assignmentInfo.setAssignment(assignment.getText().toString());
            assignmentInfo.setClass_id(Integer.parseInt(class_id.getText().toString()));
            assignmentInfo.setSubject(subject.getText().toString());
            new CreateAssignmentTask(assignmentInfo).execute();
        }
    };

    private class CreateAssignmentTask extends AsyncTask < Void, Void, String > {

        private static final String SOAP_ACTION = "http://tempuri.org/IService1/CreateAssignment";
        private static final String METHOD_NAME = "CreateAssignment";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://10.0.0.10:8080/atrack/Service1.svc";

        AssignmentInfo assignment;

        public CreateAssignmentTask(AssignmentInfo assignment) {
            this.assignment = assignment;
        }

        @Override
        protected String doInBackground(Void...arg0) {
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
                String response = envelope.getResponse().toString();

                SoapPrimitive resultsString = (SoapPrimitive) envelope.getResponse();
                return resultsString.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            username.setText(result.toString());
        }
    }


    private class StudentLoginTask extends AsyncTask < Void, Void, String > {
        AssignmentInfo assignment;
        String class_id;

        public StudentLoginTask(String class_id) {
            this.class_id = class_id;
        }

        @Override
        protected String doInBackground(Void...params) {
            return null;
        }
    }

    private class WebServiceTask extends AsyncTask < Void, Void, String > {
        AssignmentInfo assignment;

        private static final String SOAP_ACTION = "http://tempuri.org/IService1/CreateAssignment";
        private static final String METHOD_NAME = "CreateAssignment";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://10.0.0.10:8080/atrack/Service1.svc";


        public WebServiceTask(AssignmentInfo assignment) {
            this.assignment = assignment;
        }
        @Override
        protected String doInBackground(Void...arg0) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("class_id", assignment.class_id);
                request.addProperty("subject", assignment.subject);
                request.addProperty("assignment", assignment.assignment);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                result = (SoapPrimitive) envelope.getResponse();
                if (result != null) {
                    Log.e("NK_ws5", result.toString());
                    } else {
                        Log.e("NK_ws6", "Inside Else");
                        Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
                    }
            } //runnable

            catch (Exception e) {
                Log.e("NK_ws7", "Inside Catch");
                e.printStackTrace();
            } //catch
            return result.toString();
        }
        protected void onPostExecute(String result) {
            class_id.setText(result.toString());
        }
    }

    // Button to login , username and password from register in local database must be valid to enter
    public void OnButtonClick(View view) {

        if (view.getId() == R.id.button_login) {
            EditText a = (EditText) findViewById(R.id.et_username);
            String str = a.getText().toString();
            EditText b = (EditText) findViewById(R.id.eT_pasword);
            String pass = b.getText().toString();

            String password = helper.searchPass(str);
            if (pass.equals(password)) {
                Intent i = new Intent(MainActivity.this, DisplayActivity.class);
                i.putExtra("Username", str);
                startActivity(i);
            } else {
                Toast now = Toast.makeText(MainActivity.this, "Username and password dont match", Toast.LENGTH_SHORT);
                now.show();
            }

        }
    // Button to go onto Register page using an intent
        if (view.getId() == R.id.button_register) {
            Intent i = new Intent(MainActivity.this, Sign_UpActivity.class);
            startActivity(i);
        }
    }
}