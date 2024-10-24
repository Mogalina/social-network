package service;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import models.Friendship;
import models.User;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Network {

    private final Service<String, User> userService;
    private final Service<String, Friendship> friendshipService;

    public Network(Service<String, User> userService, Service<String, Friendship> friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    public Optional<User> findUser(String id) {
        return userService.findById(id);
    }

    public void addUser(User user) throws EntityAlreadyExistsException {
        userService.save(user);
    }

    public void deleteUser(String uid) throws EntityNotFoundException {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(uid))
                .forEach(friendship -> friendshipService.deleteById(friendship.getId()));

        userService.deleteById(uid);
    }

    public Optional<User> updateUser(User user) throws EntityNotFoundException {
        return userService.update(user);
    }

    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    public Iterable<Friendship> getAllFriendships() {
        return friendshipService.findAll();
    }

    public Iterable<User> getFriendsOfUser(String uid) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(uid) && !friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(uid))
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

    public void makeFriendship(String uid1, String uid2) throws EntityAlreadyExistsException {
        Friendship senderToReceiver = new Friendship(uid1, uid2);
        senderToReceiver.setPending(false);
        friendshipService.save(senderToReceiver);

        Friendship receiverToSender = new Friendship(uid2, uid1);
        receiverToSender.setPending(false);
        friendshipService.save(receiverToSender);
    }

    public void sendFriendRequest(String senderId, String receiverId) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(senderId) &&
                        friendship.containsUser(senderId) &&
                        friendship.isPending())
                .findFirst()
                .ifPresentOrElse(
                        pendingFriendship -> {
                            friendshipService.deleteById(pendingFriendship.getId());
                            try {
                                makeFriendship(senderId, receiverId);
                            } catch (EntityAlreadyExistsException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> {
                            Friendship friendRequest = new Friendship(senderId, receiverId);
                            try {
                                friendshipService.save(friendRequest);
                            } catch (EntityAlreadyExistsException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
