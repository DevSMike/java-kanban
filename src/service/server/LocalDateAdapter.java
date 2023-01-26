package service.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm:ss");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, LocalDateTime localDate) throws IOException {
        try {
            jsonWriter.value(localDate.format(formatterWriter));
        } catch (NullPointerException e) {
            System.out.println("На вход Time не передавалось!");
            jsonWriter.value(LocalDateTime.MIN.format(formatterWriter));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);

    }
}
