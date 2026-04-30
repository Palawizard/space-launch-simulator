package persistence;

import domain.launch.LaunchResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchHistoryService {
    public static final Path DEFAULT_HISTORY_PATH = Paths.get("data", "launch-history.csv");

    private final Path historyPath;
    private final List<LaunchResult> history;

    public LaunchHistoryService() {
        this(DEFAULT_HISTORY_PATH);
    }

    public LaunchHistoryService(Path historyPath) {
        this.historyPath = historyPath;
        this.history = new ArrayList<>();
    }

    public Path getHistoryPath() {
        return historyPath;
    }

    public List<LaunchResult> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void addResult(LaunchResult launchResult) {
        history.add(launchResult);
    }

    public void replaceHistory(List<LaunchResult> launchResults) {
        history.clear();
        history.addAll(launchResults);
    }
}
