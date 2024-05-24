package tpo.zad5_servlets.repo;

import tpo.zad5_servlets.model.Car;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarRepo {
    private static String CAR_FILE_NAME = "cars.txt";
    static List<Car> sampleCars  = Arrays.asList(
            new Car("Sedan", "Toyota", "2018", 6.5),
            new Car("SUV", "Honda", "2020", 7.8),
            new Car("Kombi", "Volkswagen", "2017", 5.9),
            new Car("Hatchback", "Ford", "2019", 6.3),
            new Car("Sedan", "BMW", "2021", 8.0),
            new Car("Crossover", "Nissan", "2016", 6.7),
            new Car("Pickup", "Chevrolet", "2015", 9.1),
            new Car("Sedan", "Mazda", "2022", 7.2),
            new Car("Minivan", "Chrysler", "2014", 10.0),
            new Car("Sedan", "Porsche", "2019", 9.5)
    );

    public CarRepo() {
        saveToFile(sampleCars);
    }

    public  static void saveToFile(List<Car> cars ) {
        new File(CAR_FILE_NAME);
        try (FileOutputStream fileOut = new FileOutputStream(CAR_FILE_NAME);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(cars);
            System.out.println("Serialized data is saved in " + CAR_FILE_NAME);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }


    public static List<Car> readFromFile() {
        List<Car> cars = null;
        try (FileInputStream fileIn = new FileInputStream("cars.txt");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            cars = (List<Car>) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Car class not found");
            c.printStackTrace();
        }
        return cars;
    }
    public List<Car> getCars(){
        return readFromFile();
    }
    public List<String> getTypes(){
        return getCars().stream()
                .map(car -> car.rodzaj)
                .collect(Collectors.toList());
    }
    public Set<Car> getCarsByType(String type){
       return getCars().stream()
                .filter(car -> car.rodzaj.equals(type))
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        System.out.println(readFromFile());
    }

}
