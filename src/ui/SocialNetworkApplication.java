package ui;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import models.Friendship;
import models.User;
import service.Network;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SocialNetworkApplication {

    private final Network network;

    private final Map<String, Consumer<List<String>>> commands;

    public SocialNetworkApplication(Network network) {
        this.network = network;

        commands = new HashMap<>();
        commands.put("docs", this::displayDocumentation);
        commands.put("users", this::displayUsers);
        commands.put("add_user", this::addUser);
        commands.put("delete_user", this::deleteUser);
        commands.put("find_user", this::findUser);
        commands.put("friends", this::displayFriendsOfUser);
        commands.put("send_request", this::sendFriendRequest);
        commands.put("friendships", this::displayFriendships);
    }

    private void displayApplicationIntro() {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("|                                                                                                                                 |");
        System.out.println("|                                     ███████╗ ██████╗  ██████╗ ██╗ █████╗ ██╗     ███╗   ██╗                                     |");
        System.out.println("|                                     ██╔════╝██╔═══██╗██╔════╝ ██║██╔══██╗██║     ████╗  ██║                                     |");
        System.out.println("|                                     ███████╗██║   ██║██║      ██║███████║██║     ██╔██╗ ██║                                     |");
        System.out.println("|                                     ╚════██║██║   ██║██║      ██║██╔══██║██║     ██║╚██╗██║                                     |");
        System.out.println("|                                     ███████║╚██████╔╝╚██████╗ ██║██║  ██║███████╗██║ ╚████║                                     |");
        System.out.println("|                                     ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝                                     |");
        System.out.println("|                                                                                                                                 |");
        System.out.println("|                                            Welcome to your Social Network Platform!                                             |");
        System.out.println("|                                           © 2024 Moghioros Eric. All rights reserved.                                           |");
        System.out.println("|                                                                                                                                 |");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
    }

    private void displayDocumentation(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        System.out.println("+---------------------+----------------------------------------------------+------------------------------------------------------+");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "COMMAND", "PARAMETERS", "DESCRIPTION");
        System.out.println("+---------------------+----------------------------------------------------+------------------------------------------------------+");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "docs", "-", "Display information about application usage");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "users", "-", "Display available users in network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "add_user", "<FIRST_NAME> <LAST_NAME> <EMAIL> <PASSWORD>", "Add new user to network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "delete_user", "<ID>", "Delete user from network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "friends", "<UID>", "Display friends of specific user");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "send_request", "<SENDER_ID> <RECEIVER_ID>", "Send friend request to user");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "friendships", "-", "Display available friendships between users");
        System.out.println("+---------------------+----------------------------------------------------+------------------------------------------------------+");
    }

    private void sendFriendRequest(List<String> params) {
        if (params.size() != 2) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        String senderId = params.get(0);
        String receiverId = params.get(1);

        try {
            network.sendFriendRequest(senderId, receiverId);
            System.out.println("[INFO] Friend request sent successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR ] " + e.getMessage());
        }
    }

    private void displayFriendsOfUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        String uid = params.getFirst();

        try {
            String result = StreamSupport.stream(network.getFriendsOfUser(uid).spliterator(), false)
                    .map(User::toString)
                    .collect(Collectors.joining("\n\n"));
            if (result.isEmpty()) {
                System.out.println("[INFO] User does not have any friends.");
            } else {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void findUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        String uid = params.getFirst();

        try {
            Optional<User> user = network.findUser(uid);
            if (user.isPresent()) {
                System.out.println(user.get());
            } else {
                System.out.println("[INFO] User not found.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void addUser(List<String> params) {
        if (params.size() != 4) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

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
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        String uid = params.getFirst();

        try {
            network.deleteUser(uid);
            System.out.println("[INFO] User deleted successfully.");
        } catch (Exception | EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayFriendships(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        String result = StreamSupport.stream(network.getAllFriendships().spliterator(), false)
                .map(Friendship::toString)
                .collect(Collectors.joining("\n\n"));
        if (result.isEmpty()) {
            System.out.println("[INFO] There are no friendship relations between users.");
        } else {
            System.out.println(result);
        }
    }

    private void displayUsers(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        String result = StreamSupport.stream(network.getAllUsers().spliterator(), false)
                .map(User::toString)
                .collect(Collectors.joining("\n\n"));
        if (result.isEmpty()) {
            System.out.println("[INFO] No users found.");
        } else {
            System.out.println(result);
        }
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

            System.out.println("\n-----------------------------------------------------------------------------------------------------------------------------------\n");
        }
    }
}
