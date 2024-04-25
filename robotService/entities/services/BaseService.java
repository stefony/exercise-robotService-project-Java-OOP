package robotService.entities.services;

import robotService.entities.robot.Robot;
import robotService.entities.supplements.Supplement;

import java.util.ArrayList;
import java.util.Collection;

import static robotService.common.ConstantMessages.NOT_ENOUGH_CAPACITY_FOR_ROBOT;
import static robotService.common.ExceptionMessages.SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY;

public abstract class BaseService implements Service{
    private String name;
    private int capacity;
    private Collection<Supplement> supplements;
    private Collection<Robot> robots;

    protected BaseService(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
        this.supplements = new ArrayList<>();
        this.robots = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException(SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public Collection<Robot> getRobots() {
        return robots;
    }

    @Override
    public Collection<Supplement> getSupplements() {
        return supplements;
    }

    @Override
    public void addRobot(Robot robot) {
        if (robots.size() >= capacity) {
            throw new IllegalStateException(NOT_ENOUGH_CAPACITY_FOR_ROBOT);
        }
        robots.add(robot);
    }

    @Override
    public void removeRobot(Robot robot) {
        robots.remove(robot);
    }

    @Override
    public void addSupplement(Supplement supplement) {
        supplements.add(supplement);
    }

    @Override
    public void feeding() {
        for (Robot robot : robots) {
            robot.eating();
        }
    }

    @Override
    public int sumHardness() {
        int totalHardness = 0;
        for (Supplement supplement : supplements) {
            totalHardness += supplement.getHardness();
        }
        return totalHardness;
    }

    @Override
    public String getStatistics() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getName()).append(" ").append(getClass().getSimpleName()).append(":\n");
        stringBuilder.append("Robots: ");

        if (robots.isEmpty()) {
            stringBuilder.append("none");
        } else {
            for (Robot robot : robots) {
                stringBuilder.append(robot.getName()).append(" ");
            }
        }

        stringBuilder.append("\nSupplements: ").append(supplements.size());
        stringBuilder.append(" Hardness: ").append(sumHardness());

        return stringBuilder.toString();
    }
}
