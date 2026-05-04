package persistence;

import domain.launch.LaunchResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        sortHistoryChronologically();
    }

    public void replaceHistory(List<LaunchResult> launchResults) {
        history.clear();
        history.addAll(launchResults);
        sortHistoryChronologically();
    }

    public void loadHistory() throws IOException {
        if (!Files.exists(historyPath) || Files.size(historyPath) == 0) {
            replaceHistory(new ArrayList<>());
            return;
        }

        List<String> lines = Files.readAllLines(historyPath);
        if (lines.size() <= 1) {
            replaceHistory(new ArrayList<>());
            return;
        }

        List<LaunchResult> loadedHistory = new ArrayList<>();

        for (int index = 1; index < lines.size(); index++) {
            loadedHistory.add(csvMapper.fromCsvLine(lines.get(index)));
        }

        replaceHistory(loadedHistory);
    }

    public void saveHistory() throws IOException {
        if (historyPath.getParent() != null) {
            Files.createDirectories(historyPath.getParent());
        }

        List<String> lines = new ArrayList<>();
        lines.add(LaunchResultCsvMapper.HEADER);

        for (LaunchResult launchResult : history) {
            lines.add(csvMapper.toCsvLine(launchResult));
        }

        Files.write(historyPath, lines);
    }

    private void sortHistoryChronologically() {
        history.sort(Comparator.comparing(LaunchResult::getDate));
    }
}
