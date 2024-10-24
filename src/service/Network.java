package service;

import exceptions.EntityNotFoundException;
import models.Friendship;
import models.User;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Network {

    private final Service<Long, User> userService;
    private final Service<Long, Friendship> friendshipService;

    public Network(Service<Long, User> userService, Service<Long, Friendship> friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    public Optional<User> findUser(Long id) {
        return userService.findById(id);
    }

    public Optional<User> addUser(User user) {
        return userService.save(user);
    }

    public Optional<User> deleteUser(User user) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(user.getId()))
                .forEach(friendship -> friendshipService.deleteById(friendship.getId()));

        return userService.deleteById(user.getId());
    }

    public Optional<User> updateUser(User user) throws EntityNotFoundException {
        return userService.update(user);
    }

    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    public Iterable<User> getFriendsOfUser(User user) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(user.getId()) && !friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(user.getId()))
                .map(userService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

    public Iterable<User> getSentRequestsOfUser(User user) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> Objects.equals(friendship.getSenderId(), user.getId()) && friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(user.getId()))
                .map(userService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Iterable<User> getReceivedRequestsOfUser(User user) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> Objects.equals(friendship.getReceiverId(), user.getId()) && friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(user.getId()))
                .map(userService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void makeFriendship(User user1, User user2) {
        Friendship senderToReceiver = new Friendship(user1.getId(), user2.getId());
        senderToReceiver.setPending(false);
        friendshipService.save(senderToReceiver);

        Friendship receiverToSender = new Friendship(user2.getId(), user1.getId());
        receiverToSender.setPending(false);
        friendshipService.save(receiverToSender);
    }

    public void sendFriendRequest(User sender, User receiver) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(sender.getId()) &&
                        friendship.containsUser(receiver.getId()) &&
                        friendship.isPending())
                .findFirst()
                .ifPresentOrElse(
                        pendingFriendship -> {
                            friendshipService.deleteById(pendingFriendship.getId());
                            makeFriendship(sender, receiver);
                        },
                        () -> {
                            Friendship friendRequest = new Friendship(sender.getId(), receiver.getId());
                            friendshipService.save(friendRequest);
                        }
                );
    }
}
