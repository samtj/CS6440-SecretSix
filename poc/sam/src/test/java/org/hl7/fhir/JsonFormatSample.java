package org.hl7.fhir;

import org.hl7.fhir.instance.formats.JsonComposer;
import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.model.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.hamcrest.core.Is.is;
import static org.hl7.fhir.instance.model.Encounter.EncounterLocationComponent;
import static org.hl7.fhir.instance.model.ResourceType.Encounter;
import static org.junit.Assert.assertThat;

public class JsonFormatSample {
    @Test
    public void shouldBeAbleToComposeJsonFromEncounterResource() throws Exception {
        final Encounter encounter = populateEncounter();
        new JsonComposer().compose(System.out, encounter, true);
    }

    @Test
    public void shouldBeAbleToParseJsonToEncounterResource() throws Exception {
        Encounter encounter = populateEncounter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new JsonComposer().compose(outputStream, encounter, true);
        String jsonRepresentation = new String(outputStream.toByteArray());

        JsonParser jsonParser = new JsonParser();
        Resource resource = jsonParser.parse(new ByteArrayInputStream(jsonRepresentation.getBytes()));
        assertThat(resource.getResourceType(), is(Encounter));

        Encounter parsedEncounter = (Encounter) resource;
        assertThat(parsedEncounter.getIdentifier().size(), is(1));
        assertThat(parsedEncounter.getIdentifier().get(0).getValueSimple(), is("id-12345"));
    }

    private Encounter populateEncounter() {
        final Encounter encounter = new Encounter();

        Identifier myId = new Identifier();
        myId.setValue(string_("id-12345"));
        encounter.getIdentifier().add(myId);

        ResourceReference location = new ResourceReference();
        location.setDisplay(string_("Soliz House"));
        Period period = new Period();
        period.setStart(new DateTime() {{
            setValue("2013-11-29");
        }});
        period.setEnd(new DateTime(){{
            setValue("2013-12-01");
        }});
        EncounterLocationComponent locationComponent = new EncounterLocationComponent(location, period);
        encounter.getLocation().add(locationComponent);
        return encounter;
    }

    private String_ string_(final String value) {
        return new String_() {{
            setValue(value);
        }};
    }
}
