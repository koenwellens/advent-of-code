package aoc2024.d09;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static aoc2024.common.Input.readString;
import static aoc2024.common.Runner.run;

public class D09 {
    public static void main(String[] args) {
        run("Compacted file system checksum", D09::compactedFileSystemCheckSum,
                "example.txt", 1928L
                , "input.txt", 6370402949053L
        );
        run("Compacted file system with contiguous files checksum", D09::compactedFileSystemWithContiguousFilesCheckSum,
                "example.txt", 2858L
                , "input.txt", 6398096697992L
        );
    }

    private static Long compactedFileSystemWithContiguousFilesCheckSum(Path path) {
        String compactLayoutMap = readString(path);
        int[] fullLayout = new int[compactLayoutMap.length() * 9];
        List<List<Integer>> gapIndexesBySize = new ArrayList<>();
        gapIndexesBySize.add(new ArrayList<>());
        for (int size = 1; size <= 9; size++) {
            gapIndexesBySize.add(new ArrayList<>());
        }
        List<Integer> fileIndexes = new ArrayList<>();
        int blockId = 0;
        for (int c = 0; c < compactLayoutMap.length(); c++) {
            int size = compactLayoutMap.charAt(c) - '0';
            if (c % 2 == 0) {
                int fileId = c / 2;
                fileIndexes.add(blockId);
                for (int i = 0; i < size; i++) {
                    fullLayout[blockId++] = fileId;
                }
            } else {
                if (size > 0) {
                    gapIndexesBySize
                            .get(compactLayoutMap.charAt(c) - '0')
                            .add(blockId);
                    for (int i = 0; i < size; i++) {
                        fullLayout[blockId++] = -1;
                    }
                }
            }
        }

        for (int fi = fileIndexes.size() - 1; fi >= 0; fi--) {
            int fileStartBlock = fileIndexes.get(fi);
            int fileId = fullLayout[fileStartBlock];
            int fileSize = 1;
            while (fileStartBlock + fileSize < fullLayout.length - 1
                    && fullLayout[fileStartBlock + fileSize] == fileId) {
                fileSize++;
            }

            boolean targetGapFound = false;
            int targetStartBlock = -1;
            int targetSize = -1;
            for (int possibleSize = 9; possibleSize >= fileSize; possibleSize--) {
                List<Integer> gapsOfSize = gapIndexesBySize.get(possibleSize);
                if (!gapsOfSize.isEmpty()) {
                    int gapIndex = gapsOfSize.getFirst();
                    if ((!targetGapFound || gapIndex < targetStartBlock) && gapIndex < fileStartBlock) {
                        targetStartBlock = gapIndex;
                        targetSize = possibleSize;
                        targetGapFound = true;
                    }
                }
            }

            if (targetGapFound) {
                for (int i = 0; i < fileSize; i++) {
                    fullLayout[targetStartBlock + i] = fileId;
                    fullLayout[fileStartBlock + i] = -1;
                }
                gapIndexesBySize.get(targetSize).removeFirst();
                int remainingGapSize = targetSize - fileSize;
                int remainingGapStartBlock = targetStartBlock + fileSize;
                if (remainingGapSize > 0) {
                    List<Integer> gapIndexesOfSize = gapIndexesBySize.get(remainingGapSize);
                    if (remainingGapStartBlock > gapIndexesOfSize.getLast()) {
                        gapIndexesOfSize.add(remainingGapStartBlock);
                    } else
                        for (int i = 0; i < gapIndexesOfSize.size(); i++) {
                            if (remainingGapStartBlock < gapIndexesOfSize.get(i)) {
                                gapIndexesOfSize.add(i, remainingGapStartBlock);
                                break;
                            }
                        }
                }
            }
        }
        long checksum = 0;
        for (int block = 0; block < fullLayout.length; block++) {
            if (fullLayout[block] > -1) {
                checksum += (long) fullLayout[block] * block;
            }
        }
        return checksum;
    }

    private static Long compactedFileSystemCheckSum(Path path) {
        String compactLayoutMap = readString(path);

        int compactLayoutCursor = 0;
        int remainingAtCursor = compactLayoutMap.charAt(compactLayoutCursor) - '0';

        int compactLayoutMoveableDataCursor = compactLayoutMap.length() - 1;
        int remainingAtMoveableDataCursor = compactLayoutMap.charAt(compactLayoutMoveableDataCursor) - '0';

        long checkSum = 0;
        for (long blockNr = 0; ; blockNr++) {
            if (compactLayoutCursor % 2 == 0) {
                // cursor pointing to file, use file to fill blocks
                int fileId = compactLayoutCursor / 2;
                checkSum += blockNr * fileId;
                remainingAtCursor--;
                while (remainingAtCursor == 0) {
                    compactLayoutCursor++;
                    remainingAtCursor = compactLayoutMap.charAt(compactLayoutCursor) - '0';
                }
            } else {
                // cursor pointing to gap, use file from end of filesystem to fill blocks
                int fileId = compactLayoutMoveableDataCursor / 2;
                checkSum += blockNr * fileId;
                remainingAtMoveableDataCursor--;
                while (remainingAtMoveableDataCursor == 0) {
                    compactLayoutMoveableDataCursor -= 2;
                    remainingAtMoveableDataCursor = compactLayoutMap.charAt(compactLayoutMoveableDataCursor) - '0';
                }
                remainingAtCursor--;
                while (remainingAtCursor == 0) {
                    compactLayoutCursor++;
                    remainingAtCursor = compactLayoutMap.charAt(compactLayoutCursor) - '0';
                }
            }
            if (compactLayoutCursor == compactLayoutMoveableDataCursor) {
                int fileId = compactLayoutMoveableDataCursor / 2;
                while (remainingAtMoveableDataCursor > 0) {
                    blockNr++;
                    remainingAtMoveableDataCursor--;
                    checkSum += blockNr * fileId;
                }
                break;
            }
            if (compactLayoutCursor > compactLayoutMoveableDataCursor) {
                break;
            }
        }

        return checkSum;
    }

}
