package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Status;
import model.TypeTask;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getInMemoryTask() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() throws IOException {
        return new HttpTaskManager("http://localhost:8077");
    }

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Status.class, new StatusAdapter())
                    .registerTypeAdapter(TypeTask.class, new TaskTypeAdapter());
            gson = gsonBuilder.create();
        }
        return gson;
    }
}


