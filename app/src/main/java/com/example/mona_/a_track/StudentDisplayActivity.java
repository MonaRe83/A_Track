package com.example.mona_.a_track;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class StudentDisplayActivity extends AppCompatActivity {
    Button Btn_update , Btn_delete;
    EditText Et_newText;
    private SoapPrimitive result;
    Button popin, save;
    EditText class_id;
    EditText subject;
    EditText assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_display);

        Et_newText= (EditText) findViewById(R.id.et_newText);
        Btn_update = (Button)findViewById(R.id.btn_update);
        Btn_delete = (Button)findViewById(R.id.btn_delete);

        Intent recievedIntent = getIntent();
        final String assignment = recievedIntent.getStringExtra("assignment");
        final Integer assignment_id = recievedIntent.getIntExtra("assignment_id", -1);

        Et_newText.setText(assignment);

        // Button delete on set listner
        Btn_delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              AssignmentInfo assignmentInfo = new AssignmentInfo();
              assignmentInfo.setAssignment(assignment);
              assignmentInfo.setAssignment_id(assignment_id);
              new deleteAssignmentTask(assignmentInfo).execute();
          }
      }

        );

        // Button update method when updating new text
        Btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AssignmentInfo assignmentInfo = new AssignmentInfo();
                assignmentInfo.setAssignment(Et_newText.getText().toString());

                 assignmentInfo.setAssignment_id(assignment_id);
                Log.wtf("Assignment", "wtf");// Testing
                new updateAssignmentTask(assignmentInfo).execute();

            }
        });


    }

    //  On click listner for button
    private View.OnClickListener myButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Simply execute the Async class
            AssignmentInfo assignmentInfo = new AssignmentInfo();
            assignmentInfo.setAssignment(assignment.getText().toString());
            assignmentInfo.setClass_id(Integer.parseInt(class_id.getText().toString()));
            // Using method from webservice
            new updateAssignmentTask(assignmentInfo).execute();
        }
    };
    private class updateAssignmentTask extends AsyncTask< Void, Void, Void > {

        private static final String SOAP_ACTION = "http://tempuri.org/IService1/updateAssignment";
        private static final String METHOD_NAME = "updateAssignment";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://10.0.0.10:8080/atrack/Service1.svc";

        AssignmentInfo assignment;

        public updateAssignmentTask(AssignmentInfo assignment) {

            this.assignment = assignment;
        }

        @Override
        protected Void doInBackground(Void...arg0) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                Log.wtf("Assignment", (assignment.getAssignment_id().toString())); // was not updating used method for debugging

                request.addProperty("class_id", assignment.getClass_id());
                request.addProperty("assignment", assignment.getAssignment());
                request.addProperty("assignment_id", assignment.getAssignment_id());


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
        // Excute method after Task is run
        @Override
        protected void onPostExecute(Void aVoid) {
            String assignments = Et_newText.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("new_assignment",assignments);
            intent.putExtra("assignmentResult", AssignmentActivity.ASSIGNMENT_UPDATE);
            setResult(RESULT_OK,intent);
            finish();
        }

    }

    public void OnUpdateClick(View view) {
    }

    public void OnDeleteClick(View view) {
    }

    //  On click listner for button
    private View.OnClickListener myButtonListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //simply execute the Async class
            AssignmentInfo assignmentInfo = new AssignmentInfo();
            assignmentInfo.setAssignment(assignment.getText().toString());
            assignmentInfo.setClass_id(Integer.parseInt(class_id.getText().toString()));

            new deleteAssignmentTask(assignmentInfo).execute();
        }
    };
    private class deleteAssignmentTask extends AsyncTask< Void, Void, Void > {

        private static final String SOAP_ACTION = "http://tempuri.org/IService1/deleteAssignment";
        private static final String METHOD_NAME = "deleteAssignment";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://10.0.0.10:8080/atrack/Service1.svc";

        AssignmentInfo assignment;

        public deleteAssignmentTask(AssignmentInfo assignment) {
            this.assignment = assignment;
        }

        @Override
        protected Void doInBackground(Void...arg0) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                Log.wtf("Assignment", (assignment.getAssignment_id().toString()));

                request.addProperty("class_id", assignment.getClass_id());
                request.addProperty("assignment", assignment.getAssignment());
                request.addProperty("assignment_id", assignment.getAssignment_id());

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
        // Excute method for Delete assignment
        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent();
            intent.putExtra("assignmentResult", AssignmentActivity.ASSIGNMENT_DELETE);
            setResult(RESULT_OK,intent);

            finish();
        }
    }
}
