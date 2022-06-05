package ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.IdHolder;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.SecretGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeMap;

@Component
public class JwtSecretKeeper {
    private final TreeMap<Integer, String> secrets;
    private final TreeMap<Integer, LocalDateTime> generatedTime;
    private SecretGenerator generator;
    private Duration leaveTime;
    private Duration cleanSecretsInterval;
    private final Thread cleanSecretsThread;
    private boolean cleanThreadEnabled;

    public JwtSecretKeeper() {
        secrets = new TreeMap<>();
        generatedTime = new TreeMap<>();
        generator = new SecretGenerator();
        leaveTime = Duration.ofDays(1);
        cleanThreadEnabled = false;
        cleanSecretsThread = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(getCleanSecretsInterval().
                            toMillis());
                } catch (InterruptedException e) {
                    return;
                }
                if(!isCleanThreadEnabled()) {
                    return;
                }
                for(Integer key : generatedTime.keySet()) {
                    if(LocalDateTime.now().minus(getLeaveTime()).
                            isAfter(generatedTime.get(key))) {
                        generatedTime.remove(key);
                        secrets.remove(key);
                    }
                }
            }
        });
    }

    public SecretGenerator getGenerator() {
        return generator;
    }

    @Autowired
    public void setGenerator(SecretGenerator generator) {
        this.generator = generator;
    }

    @NonNull
    public Duration getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(@NonNull Duration leaveTime) {
        this.leaveTime = leaveTime;
    }

    public void enableCleanThread() {
        if(!cleanThreadEnabled) {
            cleanThreadEnabled = true;
            if(!cleanSecretsThread.isAlive()) {
                cleanSecretsThread.start();
            }
        }
    }

    public void disableCleanThread() {
        if(cleanThreadEnabled) {
            cleanThreadEnabled = false;
        }
    }

    public boolean isCleanThreadEnabled() {
        return cleanThreadEnabled;
    }

    public Duration getCleanSecretsInterval() {
        return cleanSecretsInterval;
    }

    public void setCleanSecretsInterval(Duration interval) {
        this.cleanSecretsInterval = interval;
    }

    public String getSecret(IdHolder entity) {
        String secret = null;
        LocalDateTime lastGenerated = null;
        if(generatedTime.containsKey(entity.getId())) {
            lastGenerated = generatedTime.get(entity.getId());
        }
        if(secrets.containsKey(entity.getId())) {
            secret = secrets.get(entity.getId());
        }
        if(lastGenerated == null || LocalDateTime.now().minus(
                leaveTime).isAfter(lastGenerated)) {
            secret = generator.generate();
            generatedTime.put(entity.getId(), LocalDateTime.now());
            secrets.put(entity.getId(), secret);
        }
        return secret;
    }

    public void resetSecret(IdHolder entity) {
        secrets.remove(entity.getId());
        generatedTime.remove(entity.getId());
    }
}
