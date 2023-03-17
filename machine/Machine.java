package machine;


import static machine.Beverage.*;

public class Machine {
    private MachineState state;
    private int waterLevel;
    private int milkLevel;
    private int coffeeLevel;
    private int cupsLevel;
    private int moneyInRegistry;


    public Machine(int waterLevel, int milkLevel, int coffeeLevel, int cupsLevel, int moneyInRegistry) {
        this.waterLevel = waterLevel;
        this.milkLevel = milkLevel;
        this.coffeeLevel = coffeeLevel;
        this.cupsLevel = cupsLevel;
        this.moneyInRegistry = moneyInRegistry;
        setMainState();
    }

    public boolean isWorking() {
        return state != MachineState.OFF;
    }

    public void execute(String input) {
        switch (state) {
            case MAIN_MENU -> setState(input);
            case BUYING -> {
                handleTransaction(input);
                setMainState();
            }
            case FILLING_WATER -> {
                waterLevel += Integer.parseInt(input);
                System.out.println("Write how many ml of milk you want to add:");
                state = MachineState.FILLING_MILK;
            }
            case FILLING_MILK -> {
                milkLevel += Integer.parseInt(input);
                System.out.println("Write how many grams coffee beans you want to add:");
                state = MachineState.FILLING_COFFEE;
            }
            case FILLING_COFFEE ->{
                coffeeLevel += Integer.parseInt(input);
                System.out.println("Write how many disposable cups you want to add:");
                state = MachineState.FILLING_CUPS;
            }
            case FILLING_CUPS -> {
                cupsLevel += Integer.parseInt(input);
                state = MachineState.MAIN_MENU;
            }
        }
    }

    private void handleTransaction(String input) {
        Beverage typeOfCoffee ;
        switch (input) {
            case "1" -> typeOfCoffee = ESPRESSO;
            case "2" -> typeOfCoffee = LATTE;
            case "3" -> typeOfCoffee = CAPPUCCINO;
            case "back" -> {
                state = MachineState.MAIN_MENU;
                return;
            }
            default -> {
                System.out.println("Unknown input : " + input);
                return;
            }
        }
        makeCoffee(typeOfCoffee);
        takePayment(typeOfCoffee);
    }

    private void takePayment(Beverage typeOfCoffee) {
        moneyInRegistry += typeOfCoffee.getPrice();
    }

    private void makeCoffee(Beverage typeOfCoffee) {
        if (waterLevel < typeOfCoffee.getWater()) {
            System.out.println("Sorry, not enough water!");
            return;
        }
        if (milkLevel < typeOfCoffee.getMilk()) {
            System.out.println("Sorry, not enough milk!");
            return;
        }
        if (coffeeLevel < typeOfCoffee.getCoffee()) {
            System.out.println("Sorry, not enough coffee!");
            return;
        }
        if (cupsLevel < 1) {
            System.out.println("Sorry, nout enough cups");
            return;
        }
        waterLevel -= typeOfCoffee.getWater();
        milkLevel -= typeOfCoffee.getMilk();
        coffeeLevel -= typeOfCoffee.getCoffee();
        cupsLevel --;
        System.out.println("I have enough resources, making you a coffee!");

    }

    public void setMainState() {
        state = MachineState.MAIN_MENU;
        System.out.println("Write action (buy, fill, take, remaining, exit");
    }
    public void setState(String input) {
        switch (input) {
            case "buy" -> {
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                state = MachineState.BUYING;
            }
            case "fill" -> {
                System.out.println("Write how many ml of water do you want to add:");
                state = MachineState.FILLING_WATER;
            }
            case "take" -> {
                giveMoney();
                setMainState();
            }
            case "remaining" -> printState();
            case "exit" -> setExitState();
            default -> {
                System.out.println("Unknown command : " + input);
                setMainState();
            }

        }
    }

    private void setExitState() {
        state = MachineState.OFF;
    }

    private void printState() {
        System.out.printf("The coffee machine has:\n%d ml of water\n%d ml of milk\n", waterLevel, milkLevel);
        System.out.printf("%d g of coffee beans\n%d disposable cups\n$%d of money\n\n", coffeeLevel, cupsLevel, moneyInRegistry);
    }

    private void giveMoney() {
        System.out.printf("I give you $%d\n",moneyInRegistry);
        moneyInRegistry = 0;
    }
}
