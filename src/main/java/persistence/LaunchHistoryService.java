package persistence;

import domain.launch.LaunchResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchHistoryService {
    public static final Path DEFAULT_HISTORY_PATH = Paths.get("data", "launch-history.csv");

    private final Path historyPath;
    private final List<LaunchResult> history;
    private final LaunchResultCsvMapper csvMapper;

    public LaunchHistoryService() {
        this(DEFAULT_HISTORY_PATH);
    }

    public LaunchHistoryService(Path historyPath) {
        this.historyPath = historyPath;
        this.history = new ArrayList<>();
        this.csvMapper = new LaunchResultCsvMapper();
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

    public void loadHistory() throws IOException {
        List<String> lines = Files.readAllLines(historyPath);
        List<LaunchResult> loadedHistory = new ArrayList<>();

        for (int index = 1; index < lines.size(); index++) {
            loadedHistory.add(csvMapper.fromCsvLine(lines.get(index)));
        }

        replaceHistory(loadedHistory);
    }
}
