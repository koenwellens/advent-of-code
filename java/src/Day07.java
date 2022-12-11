import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Day07 implements DaySolution<Long> {

    @Override
    public Long part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            FileSystemNavigator navigator = new FileSystemNavigator();
            lines.forEach(line -> executeCommand(line, navigator));

            long sum = listAllDirs(navigator.rootDir)
                    .filter(dir -> dir.getSize() < 100001)
                    .mapToLong(dir -> dir.size)
                    .sum();

            System.out.println("Sum of sizes of all dirs smaller than 100000 is " + sum);

            return sum;
        }
    }

    @Override
    public Long part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            FileSystemNavigator navigator = new FileSystemNavigator();
            lines.forEach(line -> executeCommand(line, navigator));

            long totalFileSystemSize = 70000000L;
            long requiredFreeSpace = 30000000L;
            long currentlyUsedSpace = navigator.rootDir.size;

            long diskSpaceToFreeUp = requiredFreeSpace - (totalFileSystemSize - currentlyUsedSpace);
            System.out.println("Disk space to free up: " + diskSpaceToFreeUp);

            Directory directoryToDelete = listAllDirs(navigator.rootDir)
                    .filter(dir -> dir.getSize() > diskSpaceToFreeUp)
                    .min(Comparator.comparing(Directory::getSize))
                    .orElseThrow();

            System.out.println("Deleting directory " + directoryToDelete.getName() + " would free up " + directoryToDelete.size);

            return directoryToDelete.size;
        }
    }

    Stream<Directory> listAllDirs(Directory directory) {
        return directory.items.values().stream()
                .filter(item -> item instanceof Directory)
                .map(item -> (Directory) item)
                .flatMap(i -> Stream.concat(Stream.of(i), listAllDirs(i)));
    }

    private void executeCommand(String line, FileSystemNavigator navigator) {
        if (line.equals("$ cd /")) {
            navigator.currentDir = navigator.rootDir;
        } else if (line.equals("$ cd ..")) {
            navigator.currentDir = navigator.currentDir.parent;
        } else if (line.startsWith("$ cd ")) {
            navigator.currentDir = navigator.currentDir.findDir(line.substring(5));
        } else if (line.startsWith("dir")) {
            navigator.currentDir.add(new Directory(line.substring(4), navigator.currentDir));
        } else if (!line.equals("$ ls")) {
            String[] fileData = line.split(" ");
            navigator.currentDir.add(new File(Long.parseLong(fileData[0]), fileData[1]));
        }
    }

    static class FileSystemNavigator {
        Directory rootDir = new Directory();
        Directory currentDir = rootDir;
    }

    private static class Directory implements FileSystemItem {
        private Map<String, FileSystemItem> items = new HashMap<>();
        private String name;
        private final Directory parent;
        private long size = 0;

        public Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
        }

        private Directory() {
            this.name = "/";
            this.parent = this;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return size;
        }

        void add(FileSystemItem fileSystemItem) {
            if (!items.containsKey(fileSystemItem.getName())) {
                items.put(fileSystemItem.getName(), fileSystemItem);
                updateDirSizes(fileSystemItem.getSize());
            }
        }

        private void updateDirSizes(long additionalSize) {
            this.size += additionalSize;
            Directory dir = this;
            while (dir.parent != dir) {
                dir = dir.parent;
                dir.size += additionalSize;
            }
        }

        public Directory findDir(String name) {
            FileSystemItem fileSystemItem = items.get(name);
            if (fileSystemItem == null)
                throw new IllegalArgumentException("not found: " + name);
            if (!(fileSystemItem instanceof Directory))
                throw new IllegalArgumentException("not a directory: " + name);
            return (Directory) fileSystemItem;
        }
    }

    private static class File implements FileSystemItem {
        long size;
        String name;

        public File(long size, String name) {
            this.size = size;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return size;
        }

    }

    private interface FileSystemItem {
        String getName();

        long getSize();
    }
}
