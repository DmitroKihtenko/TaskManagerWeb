package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;

public class TaskIO {
    public static void write(AbstractTaskList tasks, OutputStream out) {
        try (ObjectOutputStream listStream = new ObjectOutputStream(out)) {
            listStream.writeInt(tasks.size());

            for (Task currentTask : tasks) {
                listStream.writeObject(currentTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(AbstractTaskList tasks, InputStream in) {
        try(ObjectInputStream listStream = new ObjectInputStream(in)) {
            int taskAmount = listStream.readInt();

            for(int counter = 0; counter < taskAmount; counter++) {
                tasks.add((Task)listStream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeBinary(AbstractTaskList tasks, File file) {
        try(ObjectOutputStream listStream = new ObjectOutputStream(
                Files.newOutputStream(file.toPath()))) {
            listStream.writeInt(tasks.size());

            for(Task currentTask : tasks) {
                listStream.writeObject(currentTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readBinary(AbstractTaskList tasks, File file) {
        try(ObjectInputStream listStream = new ObjectInputStream(
                Files.newInputStream(file.toPath()))) {
            int taskAmount = listStream.readInt();

            for(int counter = 0; counter < taskAmount; counter++) {
                tasks.add((Task)listStream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void write(AbstractTaskList tasks, Writer out) {
        try(BufferedWriter listStream = new BufferedWriter(out)) {
            Gson json = new Gson();

            Task[] tasksList = new Task[tasks.size()];
            int counter = 0;
            for(Task task : tasks) {
                tasksList[counter++] = task;
            }
            json.toJson(tasksList, listStream);
            listStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(AbstractTaskList tasks, Reader in) {
        try(LineNumberReader lnr = new LineNumberReader(in)) {
            Gson json = new Gson();

            StringBuilder jsonLine = new StringBuilder();
            String line = lnr.readLine();
            while(line != null) {
                jsonLine.append(line).append('\n');
                line = lnr.readLine();
            }

            Task[] tasksList = json.fromJson(jsonLine.toString(), Task[].class);
            for(Task task: tasksList) {
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeText(AbstractTaskList tasks, File file) {
        try(Writer writeStream = new OutputStreamWriter(
                Files.newOutputStream(file.toPath()))) {
            write(tasks, writeStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readText(AbstractTaskList tasks, File file) {
        try(Reader readStream = new InputStreamReader(
                Files.newInputStream(file.toPath()))) {
            read(tasks, readStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
