package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao;

import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class FileQueryLoader implements QueryLoader {
    private String prefix;
    private String suffix;
    private HashMap<String, String> cache;
    private String encoding;

    public FileQueryLoader() {
        prefix = "";
        suffix = ".sql";
    }

    public FileQueryLoader(String prefix, String suffix) {
        setPrefix(prefix);
        setSuffix(suffix);
        encoding = "utf-8";
        cache = new HashMap<>();
    }

    @NonNull
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(@NonNull String prefix) {
        this.prefix = prefix;
    }

    @NonNull
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(@NonNull String suffix) {
        this.suffix = suffix;
    }

    @NonNull
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(@NonNull String encoding) {
        this.encoding = encoding;
    }

    @NonNull
    public String load(@NonNull String key, String prefix, String suffix)
            throws IOException {
        String value = cache.get(key);
        if(value == null) {
            String file = prefix + key + suffix;
            try(InputStream is = getClass().
                    getResourceAsStream(file)) {
                if(is == null) {
                    throw new IOException("File '" + file + "' not found");
                }
                value = new String(is.readAllBytes(), encoding);
            }
            cache.put(key, value);
        }
        return value;
    }

    @NonNull
    public String load(@NonNull String key) throws IOException {
        return load(key, prefix, suffix);
    }
}
