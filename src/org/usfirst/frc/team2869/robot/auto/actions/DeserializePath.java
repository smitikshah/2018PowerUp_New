package org.usfirst.frc.team2869.robot.auto.actions;

import org.usfirst.frc.team2869.robot.auto.trajectory.Path;
import org.usfirst.frc.team2869.robot.auto.trajectory.io.TextFileDeserializer;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class DeserializePath {

    public static Path getPathFromFile(String name) throws IOException {
        TextFileDeserializer textFileDeserializer = new TextFileDeserializer();
        try {
            String filePath = "/home/lvuser/paths/" + name + ".txt";
            System.out.println(filePath);
            String contents = new String(Files.readAllBytes(Paths.get(filePath)));
            Path path = textFileDeserializer.deserialize(contents);
            path.goRight();
            return path;
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }
}
