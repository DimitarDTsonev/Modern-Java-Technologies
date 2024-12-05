package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    private Set<UserProfile> users;
    private List<Post> posts;

    public SocialNetworkImpl() {
        this.users = new HashSet<>();
        this.posts = new ArrayList<>();
    }

    /**
     * Registers a user in the social network.
     *
     * @param userProfile the user profile to register
     * @throws IllegalArgumentException  if the user profile is null
     * @throws UserRegistrationException if the user profile is already registered
     */
    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User is already registered");
        }

        users.add(userProfile);
    }

    /**
     * Returns all the registered users in the social network.
     *
     * @return unmodifiable set of all registered users (empty one if there are none).
     */
    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    /**
     * Posts a new post in the social network.
     *
     * @param userProfile the user profile that posts the content
     * @param content     the content of the post
     * @return the created post
     * @throws UserRegistrationException if the user profile is not registered
     * @throws IllegalArgumentException  if the user profile is null
     * @throws IllegalArgumentException  if the content is null or empty
     */
    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("User is not registered");
        }

        Post post = new SocialFeedPost(userProfile, content);
        posts.add(post);
        return post;
    }

    /**
     * Returns all posts in the social network.
     *
     * @return unmodifiable collection of all posts (empty one if there are none).
     */
    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableCollection(posts);
    }

    /**
     * Returns a collection of unique user profiles that can see the specified post in their feed. A
     * user can view a post if both of the following conditions are met:
     * <ol>
     *     <li>The user has at least one common interest with the author of the post.</li>
     *     <li>The user has the author of the post in their network of friends.</li>
     * </ol>
     * <p>
     * Two users are considered to be in the same network of friends if they are directly connected
     * (i.e., they are friends) or if there exists a chain of friends connecting them.
     * </p>
     *
     * @param post The post for which visibility is being determined
     * @return A set of user profiles that meet the visibility criteria (empty one if there are none).
     * @throws IllegalArgumentException if the post is <code>null</code>.
     */
    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        Set<UserProfile> reachedUsers = new HashSet<>();
        UserProfile author = post.getAuthor();

        Set<UserProfile> visited = new HashSet<>();
        visited.add(author);
        getReachedUsersRecursive(author, visited, reachedUsers, author);

        return reachedUsers;
    }

    /**
     * Recursive function that finds all the users that should see the post.
     *
     * @param currentUser
     * @param visited
     * @param reachedUsers
     * @param author
     */
    private void getReachedUsersRecursive(UserProfile currentUser, Set<UserProfile> visited,
                                          Set<UserProfile> reachedUsers, UserProfile author) {

        for (UserProfile friend : currentUser.getFriends()) {
            if (!visited.contains(friend)) {
                visited.add(friend);

                if (hasCommonInterests(author, friend)) {
                    reachedUsers.add(friend);
                }

                getReachedUsersRecursive(friend, visited, reachedUsers, author);
            }
        }

    }

    /**
     * Function that get two users and returns true if they have a common interests and false otherwise.
     *
     * @param user1
     * @param user2
     * @return if there is a common interest between two users
     */
    private boolean hasCommonInterests(UserProfile user1, UserProfile user2) {
        Set<Interest> user1Interests = EnumSet.copyOf(user1.getInterests());
        user1Interests.retainAll(user2.getInterests());
        return !user1Interests.isEmpty();
    }

    /**
     * Returns a set of all mutual friends between the two users.
     *
     * @param userProfile1 the first user profile
     * @param userProfile2 the second user profile
     * @return a set of all mutual friends between the two users or an empty set if there are no
     * mutual friends
     * @throws UserRegistrationException if any of the user profiles is not registered
     * @throws IllegalArgumentException  if any of the user profiles is null
     */
    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {

        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("User profiles cannot be null");
        }

        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException("User is not registered");
        }

        Set<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());
        return mutualFriends;

    }

    /**
     * Returns a sorted set of all user profiles ordered by the number of friends they have in
     * descending order.
     *
     * @return a sorted set of all user profiles ordered by the number of friends they have in
     * descending order
     */
    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedProfiles = new TreeSet<>(new SortByFriendsCountComparator());
        sortedProfiles.addAll(users);
        return Collections.unmodifiableSortedSet(sortedProfiles);
    }
}
