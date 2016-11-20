package com.example.mona_.a_track;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class AssignmentActivity extends AppCompatActivity {

    // Variables
    private SoapPrimitive result;
    ListView lv;
    EditText class_id;
    EditText subject;
    EditText assignment;

    int selectedPosition = 0;

    ArrayAdapter<AssignmentInfo> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        // finds each of the data in fields by ET box assigns to the variable declared
        class_id = (EditText) findViewById(R.id.eT_class_id);
        subject = (EditText) findViewById(R.id.eT_class_subject);
        assignment = (EditText) findViewById(R.id.eT_assignment);


        Bundle extras = getIntent().getExtras();
         //assign the listview to a variable
        lv = (ListView) findViewById(R.id.listView);

        // finds the code from database into list view and passes it to display
        arrayAdapter = new ArrayAdapter<AssignmentInfo>(AssignmentActivity.this, android.R.layout.simple_list_item_1);

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedPosition = position;
                AssignmentInfo assinfo = arrayAdapter.getItem(position);
                String assignment = assinfo.assignment;

                Intent intent = new Intent(getBaseContext(), StudentDisplayActivity.class);
                intent.putExtra("assignment", assignment);
                intent.putExtra("assignment_id", assinfo.assignment_id);
                startActivityForResult(intent, 0);

                // Displays the assignment
                Toast.makeText(getBaseContext(), "assignment: " + assignment, Toast.LENGTH_SHORT).show();
            }
        });
        // Excutes the method from the webservice
        new GetAssignmentsTask().execute(extras.getInt("class_id"));


    }

    public final static int ASSIGNMENT_NONE = 0;
    public final static int ASSIGNMENT_DELETE = 1;
    public final static int ASSIGNMENT_UPDATE = 2;

    // When code is returned finds position ans replaces old code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Using  the java feature to switch the results of update and delete

        switch (resultCode) {
            case RESULT_OK:

                int assignmentResult = data.getIntExtra("assignmentResult", ASSIGNMENT_NONE);

                switch (assignmentResult) {
                    case ASSIGNMENT_UPDATE:
                        String newAssignment = data.getStringExtra("new_assignment");

                        arrayAdapter.getItem(selectedPosition).setAssignment(newAssignment);

                        arrayAdapter.notifyDataSetChanged();
                        // Displays new text entered into field
                        Toast.makeText(getBaseContext(), newAssignment, Toast.LENGTH_LONG).show();
                        break;
                    case ASSIGNMENT_DELETE:

                        arrayAdapter.remove(arrayAdapter.getItem(selectedPosition));
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Deleted assignment", Toast.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }
        //Soap protocol to retrieve method from webservice
    private class GetAssignmentsTask extends AsyncTask< Integer, Void, AssignmentInfo[] > {

        private static final String SOAP_ACTION = "http://tempuri.org/IService1/GetAssignments";
        private static final String METHOD_NAME = "GetAssignments";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://10.0.0.10:8080/Service1.svc";



        @Override
        protected AssignmentInfo[] doInBackground(Integer...args) {
            try {
                if(args.length < 1) {
                    return null;
                }
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("class_id", args[0]);
                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);

                SoapObject objContainer = (SoapObject)envelope.getResponse();
                int length = objContainer.getPropertyCount();

                AssignmentInfo[] assignments  = new AssignmentInfo[length];
                // Array sets each item from DB
                for(int i = 0; i < length; i++) {
                    SoapObject objItem = (SoapObject) objContainer.getProperty(i);
                    AssignmentInfo assignment = new AssignmentInfo();
                    assignment.setClass_id(args[0]);
                    assignment.setAssignment(objItem.getProperty("Assignment").toString());
                    assignment.setSubject(objItem.getProperty("Subject").toString());
                    assignment.setAssignment_id(Integer.parseInt(objItem.getProperty("AssignmentId").toString())); // upload to server and test, it shoudl work now. I gotta go eat dinner, been waiting for a lil bit, good luck!thanks

                    assignments[i] = assignment;
                }



                return assignments;

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;


        }
        // Executes after the Task
        protected void onPostExecute(AssignmentInfo[] assignments) {

            arrayAdapter.addAll(assignments);
            arrayAdapter.notifyDataSetChanged();
        }


    }






}
