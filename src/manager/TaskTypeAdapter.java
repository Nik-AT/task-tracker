package manager;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.TypeTask;

import java.io.IOException;

public class TaskTypeAdapter extends TypeAdapter<TypeTask> {
    @Override
    public void write(JsonWriter jsonWriter, TypeTask typeTask) throws IOException {
        if (typeTask == null) {
            jsonWriter.value("null");
            return;
        }
        jsonWriter.value(TypeTask.getTypeString(typeTask));
    }

    @Override
    public TypeTask read(JsonReader jsonReader) throws IOException {
        final String text = jsonReader.nextString();
        if (text.equals("null")) {
            return null;
        }
        return TypeTask.getType(text);
    }
}
