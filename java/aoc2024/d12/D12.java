package aoc2024.d12;

import javax.swing.plaf.synth.Region;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.Run.r;
import static aoc2024.common.Runner.run;

public class D12 {
    public static void main(String[] args) {
        run("Total price of fencing", D12::totalPriceOfFencing,
                r("example1.txt", 140),
                r("example2.txt", 772),
                r("example3.txt", 1930)
                , r("input.txt", 1396562)
        );
        run("Total price of fencing with bulk discount", D12::totalPriceOfFencingWithBulkDiscount,
                r("example1.txt", 80),
                r("example2.txt", 436),
                r("example3.txt", 1206),
                r("example4.txt", 236),
                r("example5.txt", 368)
                , r("input.txt", 844132)
        );
    }

    private static int totalPriceOfFencing(Path path) {
        List<Region> regions = calculateRegions(path);

        int totalPrice = 0;
        for (Region region : regions) {
            int edges = 0;
            for (Plant plant : region.plants) {
                edges += plant.edges;
            }
            totalPrice += edges * region.plants.size();
        }

        return totalPrice;
    }

    private static int totalPriceOfFencingWithBulkDiscount(Path path) {
        List<Region> regions = calculateRegions(path);

        int totalPrice = 0;
        for (Region region : regions) {
            int sides = 0;

            region.plants.sort(Comparator
                    .comparing((Plant plant) -> plant.row)
                    .thenComparing((Plant plant) -> plant.col));

            int lastColWithEdgeAbove = -2;
            int lastColWithEdgeBelow = -2;
            int lastPlantRow = -1;
            for (Plant plant : region.plants) {
                if (plant.row != lastPlantRow) {
                    lastPlantRow = plant.row;
                    lastColWithEdgeAbove = -2;
                    lastColWithEdgeBelow = -2;
                }
                if ((plant.edgePattern & ABOVE) > 0) {
                    if (lastColWithEdgeAbove != plant.col - 1) {
                        sides++;
                    }
                    lastColWithEdgeAbove = plant.col;
                }
                if ((plant.edgePattern & BELOW) > 0) {
                    if (lastColWithEdgeBelow != plant.col - 1) {
                        sides++;
                    }
                    lastColWithEdgeBelow = plant.col;
                }
            }

            region.plants.sort(Comparator
                    .comparing((Plant plant) -> plant.col)
                    .thenComparing((Plant plant) -> plant.row));

            int lastRowWithEdgeLeft = -2;
            int lastRowWithEdgeRight = -2;
            int lastPlantCol = -1;
            for (Plant plant : region.plants) {
                if (plant.col != lastPlantCol) {
                    lastPlantCol = plant.col;
                    lastRowWithEdgeLeft = -2;
                    lastRowWithEdgeRight = -2;
                }
                if ((plant.edgePattern & LEFT) > 0) {
                    if (lastRowWithEdgeLeft != plant.row - 1) {
                        sides++;
                    }
                    lastRowWithEdgeLeft = plant.row;
                }
                if ((plant.edgePattern & RIGHT) > 0) {
                    if (lastRowWithEdgeRight != plant.row - 1) {
                        sides++;
                    }
                    lastRowWithEdgeRight = plant.row;
                }
            }

            totalPrice += sides * region.plants.size();
        }

        return totalPrice;
    }

    private static List<Region> calculateRegions(Path path) {
        AtomicInteger rowCounter = new AtomicInteger();
        List<Region> regions = new ArrayList<>();
        List<List<Plant>> plantsMap = new ArrayList<>();
        streamInputLines(path)
                .map(line -> line.split(""))
                .forEach(row -> {
                    int rowNr = rowCounter.getAndIncrement();
                    plantsMap.add(new ArrayList<>());

                    for (int col = 0; col < row.length; col++) {
                        Plant plant = new Plant(rowNr, col, row[col]);
                        plantsMap.get(rowNr).add(plant);

                        if (col == 0)
                            plant.addEdge(LEFT); // left edge of the map
                        else
                            tryJoinRegion(plant, plantsMap, rowNr, col - 1, LEFT, regions); //look left

                        if (rowNr == 0)
                            plant.addEdge(ABOVE); // top edge of the map
                        else
                            tryJoinRegion(plant, plantsMap, rowNr - 1, col, ABOVE, regions); // look above

                        if (plant.region == null) {
                            Region newRegion = new Region();
                            plant.region = newRegion;
                            newRegion.add(plant);
                            regions.add(newRegion);
                        }

                        if (col == row.length - 1)
                            plant.addEdge(RIGHT); // right edge of the map
                    }
                });
        // add bottom edge of the map
        plantsMap.getLast().forEach(plant -> plant.addEdge(BELOW));
        return regions;
    }

    private static void tryJoinRegion(Plant plant, List<List<Plant>> plantsMap, int neighbourRow, int neighbourCol, int edgeDirection, List<Region> regions) {
        if (neighbourRow >= 0 || neighbourCol >= 0) {
            Plant neighbour = plantsMap.get(neighbourRow).get(neighbourCol);
            if (neighbour.type.equals(plant.type)) {
                if (plant.region == null) {
                    plant.region = neighbour.region;
                    plant.region.add(plant);
                } else if (plant.region != neighbour.region) {
                    Region neighbourRegion = neighbour.region;
                    regions.remove(neighbourRegion);
                    plant.region.merge(neighbourRegion);
                    for (Plant p : neighbourRegion.plants) {
                        p.region = plant.region;
                    }
                }
            } else {
                neighbour.addEdge(inverseOf(edgeDirection));
                plant.addEdge(edgeDirection);
            }
        }
    }

    private static int inverseOf(int edgeDirection) {
        return switch (edgeDirection) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case ABOVE -> BELOW;
            case BELOW -> ABOVE;
            default -> throw new IllegalArgumentException();
        };
    }

    static class Plant {
        final int row;
        final int col;
        final String type;
        int edgePattern = 0;
        int edges = 0;
        Region region;

        void addEdge(int pattern) {
            if ((edgePattern & pattern) == 0) {
                edgePattern |= pattern;
                edges++;
            }
        }

        Plant(int row, int col, String type) {
            this.row = row;
            this.col = col;
            this.type = type;
        }
    }

    static class Region {
        List<Plant> plants = new ArrayList<>();

        public void add(Plant plant) {
            plants.add(plant);
        }

        public void merge(Region region) {
            region.plants.forEach(this::add);
        }
    }

    private final static int LEFT = 1;
    private final static int RIGHT = 1 << 1;
    private final static int ABOVE = 1 << 2;
    private final static int BELOW = 1 << 3;
}
