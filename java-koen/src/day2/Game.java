package day2;

public interface Game {

    int id();

    boolean canBeTakenFrom(Bag bag);

    int minimumNumberOfCubesNeeded(Cube cube);

    int power();
}
