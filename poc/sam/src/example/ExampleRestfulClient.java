package example;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.commons.lang3.time.FastDateFormat;

@SuppressWarnings("unused")
public class ExampleRestfulClient {
	
//START SNIPPET: client
public static void main(String[] args) {
   FhirContext ctx = new FhirContext();
   //String serverBase = "http://fhirtest.uhn.ca/fhirServerBase";
   String serverBase = "http://fhirtest.uhn.ca/baseDstu1";
   //ctx.getRestfulClientFactory().setProxy("127.0.0.1", 8888);

   // Create the client
   IRestfulClient client = ctx.newRestfulClient(IRestfulClient.class, serverBase);
   
   // Try the client out! This method will invoke the server
   List<Patient> patients = client.getPatient(new StringDt("SMITH"));
   
   Patient patient = patients.get(0);
   System.out.println("Patient Last Name: " + patient.getName().get(0).getFamily().get(0).getValue());
}
//END SNIPPET: client

}
