package ui;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidParametersException;
import models.User;
import service.Network;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class SocialNetworkApplication {

    private final Network network;

    private final Map<String, Consumer<List<String>>> commands;

    public SocialNetworkApplication(Network network) {
        this.network = network;

        commands = new HashMap<>();
        commands.put("users", this::displayUsers);
        commands.put("add_user", this::addUser);
        commands.put("delete_user", this::deleteUser);
    }

    private void displayApplicationIntro() {
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.println("|                                                                                                   |");
        System.out.println("|                      ███████╗ ██████╗  ██████╗ ██╗ █████╗ ██╗     ███╗   ██╗                      |");
        System.out.println("|                      ██╔════╝██╔═══██╗██╔════╝ ██║██╔══██╗██║     ████╗  ██║                      |");
        System.out.println("|                      ███████╗██║   ██║██║      ██║███████║██║     ██╔██╗ ██║                      |");
        System.out.println("|                      ╚════██║██║   ██║██║      ██║██╔══██║██║     ██║╚██╗██║                      |");
        System.out.println("|                      ███████║╚██████╔╝╚██████╗ ██║██║  ██║███████╗██║ ╚████║                      |");
        System.out.println("|                      ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝                      |");
        System.out.println("|                                                                                                   |");
        System.out.println("|                             Welcome to your Social Network Platform!                              |");
        System.out.println("|                            © 2024 Moghioros Eric. All rights reserved.                            |");
        System.out.println("|                                                                                                   |");
        System.out.println("-----------------------------------------------------------------------------------------------------");
    }

    private void addUser(List<String> params) {
        String firstName = params.get(0);
        String lastName = params.get(1);
        String email = params.get(2);
        String password = params.get(3);
        User user = new User(firstName, lastName, password, email);

        try {
            network.addUser(user);
            System.out.println("[INFO] User added successfully.");
        } catch (Exception | EntityAlreadyExistsException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void deleteUser(List<String> params) {
        String uid = params.getFirst();

        try {
            network.deleteUser(uid);
            System.out.println("[INFO] User deleted successfully.");
        } catch (Exception | EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayUsers(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        StreamSupport.stream(network.getAllUsers().spliterator(), false)
                .peek(user -> System.out.println(user.toString()))
                .findAny()
                .or(() -> {
                    System.out.println("[INFO] No users found in system.");
                    return Optional.empty();
                });
    }

    public void runApplication() {
        displayApplicationIntro();
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine();

            List<String> tokens = Arrays.asList(input.split(" "));
            String command = tokens.getFirst();
            List<String> params = tokens.subList(1, tokens.size());

            if (!commands.containsKey(command)) {
                System.out.println("[ERROR] Invalid command '" + command + "'");
            } else {
                try {
                    commands.get(command).accept(params);
                } catch (Exception e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }

            System.out.println("\n-----------------------------------------------------------------------------------------------------\n");
        }
    }
}
