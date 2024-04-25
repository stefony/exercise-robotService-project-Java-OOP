package robotService.core;

import robotService.entities.robot.FemaleRobot;
import robotService.entities.robot.MaleRobot;
import robotService.entities.robot.Robot;
import robotService.entities.services.MainService;
import robotService.entities.services.SecondaryService;
import robotService.entities.services.Service;
import robotService.entities.supplements.MetalArmor;
import robotService.entities.supplements.PlasticArmor;
import robotService.entities.supplements.Supplement;
import robotService.repositories.SupplementRepository;

import java.util.ArrayList;
import java.util.Collection;

import static robotService.common.ConstantMessages.*;
import static robotService.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {


    private SupplementRepository supplements;
    private Collection<Service> services;


    public ControllerImpl() {
        supplements = new SupplementRepository();
        services = new ArrayList<>();
    }

    @Override
    public String addService(String type, String name) {
        Service service;

        if ("MainService".equalsIgnoreCase(type)) {
            service = new MainService(name);
        } else if ("SecondaryService".equalsIgnoreCase(type)) {
            service = new SecondaryService(name);
        } else {
            throw new NullPointerException(INVALID_SERVICE_TYPE);
        }

        services.add(service);
        return String.format(SUCCESSFULLY_ADDED_SERVICE_TYPE, type);
    }

    @Override
    public String addSupplement(String type) {
        Supplement supplement;

        if ("PlasticArmor".equalsIgnoreCase(type)) {
            supplement = new PlasticArmor();
        } else if ("MetalArmor".equalsIgnoreCase(type)) {
            supplement = new MetalArmor();
        } else {
            throw new IllegalArgumentException(INVALID_SUPPLEMENT_TYPE);
        }

        supplements.addSupplement(supplement);
        return String.format(SUCCESSFULLY_ADDED_SUPPLEMENT_TYPE, type);
    }


    @Override
    public String supplementForService(String serviceName, String supplementType) {
        Service targetService = null;
        for (Service service : services) {
            if (serviceName.equalsIgnoreCase(service.getName())) {
                targetService = service;
                break;
            }
        }

//        if (targetService == null) {
//            throw new NullPointerException("Service not found.");
//        }

        Supplement supplement = supplements.findFirst(supplementType);
        if (supplement == null) {
            throw new IllegalArgumentException (String.format(NO_SUPPLEMENT_FOUND, supplementType) );
        }

        targetService.addSupplement(supplement);
        supplements.removeSupplement(supplement);
        return String.format(SUCCESSFULLY_ADDED_SUPPLEMENT_IN_SERVICE,supplementType,serviceName);
//
    }

    @Override
    public String addRobot(String serviceName, String robotType, String robotName, String robotKind, double price) {
        Service targetService = null;
        for (Service service : services) {
            if (serviceName.equalsIgnoreCase(service.getName())) {
                targetService = service;
                break;
            }
        }

//        if (targetService == null) {
//            throw new NullPointerException("Service not found.");
//        }

        Robot robot;
        if ("MaleRobot".equalsIgnoreCase(robotType)) {
            robot = new MaleRobot(robotName, robotKind, price);
        } else if ("FemaleRobot".equalsIgnoreCase(robotType)) {
            robot = new FemaleRobot(robotName, robotKind, price);
        } else {
            throw new IllegalArgumentException(INVALID_ROBOT_TYPE);
        }

        try {
            targetService.addRobot(robot);
        } catch (IllegalStateException e) {
            return UNSUITABLE_SERVICE;
        }
        return String.format(SUCCESSFULLY_ADDED_ROBOT_IN_SERVICE,robotType,serviceName);
//                Successfully added " + robotType + " to " + serviceName + ".";
  }

    @Override
    public String feedingRobot(String serviceName) {
        Service targetService = null;
        for (Service service : services) {
            if (serviceName.equalsIgnoreCase(service.getName())) {
                targetService = service;
                break;
            }
        }

        if (targetService == null) {
            throw new NullPointerException("Service not found.");
        }

        int fedCount = targetService.getRobots().size();
        targetService.feeding();

        return String.format(FEEDING_ROBOT,fedCount);
    }

    @Override
    public String sumOfAll(String serviceName) {
        Service targetService = null;
        for (Service service : services) {
            if (serviceName.equalsIgnoreCase(service.getName())) {
                targetService = service;
                break;
            }
        }

        if (targetService == null) {
            throw new NullPointerException("Service not found.");
        }

        double value = 0;
        for (Robot robot : targetService.getRobots()) {
            value += robot.getPrice();
        }

        for (Supplement supplement : targetService.getSupplements()) {
            value += supplement.getPrice();
        }

        return String.format(VALUE_SERVICE, serviceName, value);
    }

    @Override
    public String getStatistics() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Service service : services) {
            stringBuilder.append(service.getStatistics()).append("\n");
        }
        return stringBuilder.toString().trim();
    }
}
