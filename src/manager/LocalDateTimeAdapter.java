package manager;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {

        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(dateTimeFormatter));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        if (string.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(string, dateTimeFormatter);
    }
}