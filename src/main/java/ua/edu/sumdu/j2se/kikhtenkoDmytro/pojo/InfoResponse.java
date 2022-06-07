package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;

import java.util.Objects;

public class InfoResponse implements Replicable {
    private HttpStatus httpStatus;
    private String message;
    private Object content;

    public InfoResponse() {
        setHttpStatus(HttpStatus.OK);
    }

    public InfoResponse(@NonNull String message) {
        setHttpStatus(HttpStatus.OK);
        setMessage(message);
    }

    public InfoResponse(@Nullable Object content) {
        setContent(content);
        setHttpStatus(HttpStatus.OK);
    }

    public InfoResponse(@NonNull HttpStatus status,
                        @NonNull String message) {
        setHttpStatus(status);
        setMessage(message);
    }

    public InfoResponse(@NonNull HttpStatus status,
                        @Nullable Object content) {
        setContent(content);
        setHttpStatus(status);
    }

    public InfoResponse(@Nullable String message,
                        @Nullable Object content) {
        setContent(content);
        setHttpStatus(HttpStatus.OK);
        setMessage(message);
    }

    public InfoResponse(@Nullable Object content,
                        @NonNull HttpStatus status,
                        @Nullable String message) {
        setContent(content);
        setHttpStatus(status);
        setMessage(message);
    }

    public InfoResponse(@NonNull ServiceException e) {
        setContent(e.getContent());
        setHttpStatus(e.getLastStatus());
        setMessage(e.getMessage());
    }

    @JsonIgnore
    @NonNull
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public void setHttpStatus(@NonNull HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHttpStatusCode(int code) {
        this.httpStatus = HttpStatus.resolve(code);
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public Object getContent() {
        return content;
    }

    public void setContent(@Nullable Object content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoResponse)) {
            return false;
        }
        InfoResponse that = (InfoResponse) o;
        return httpStatus == that.httpStatus &&
                Objects.equals(message, that.message)
                && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus, message, content);
    }

    @Override
    public String toString() {
        return "InfoResponse{" +
                "httpStatus=" + httpStatus +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }

    @Override
    public Object replicate() {
        InfoResponse object = new InfoResponse();
        object.update(this);
        return object;
    }

    @Override
    public void update(Object object) {
        InfoResponse value = (InfoResponse) object;
        setMessage(value.getMessage());
        setHttpStatus(value.getHttpStatus());
        setContent(value.getContent());
    }
}
