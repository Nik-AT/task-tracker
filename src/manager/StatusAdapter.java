package manager;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Status;

import java.io.IOException;

public class StatusAdapter extends TypeAdapter<Status> {
    @Override
    public void write(JsonWriter jsonWriter, Status status) throws IOException {
        if (status == null) {
            jsonWriter.value("null");
            return;
        }
        jsonWriter.value(Status.getStatusString(status));
    }

    @Override
    public Status read(JsonReader jsonReader) throws IOException {
        final String text = jsonReader.nextString();
        if (text.equals("null")) {
            return null;
        }
        return Status.getStatus(text);
    }
}
