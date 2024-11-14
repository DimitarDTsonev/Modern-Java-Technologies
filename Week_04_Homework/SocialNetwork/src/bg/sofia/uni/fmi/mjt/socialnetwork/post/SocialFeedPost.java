package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SocialFeedPost implements Post {
    private UserProfile author;
    private String content;
    private UUID uniqueID;
    private LocalDateTime publishedDate;
    private Map<UserProfile, ReactionType> userProfileToReaction;
    private Map<ReactionType, Set<UserProfile>> reactionToUserProfiles;

    public SocialFeedPost(UserProfile author, String content) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }

        this.author = author;
        this.content = content;
        this.uniqueID = UUID.randomUUID();
        this.publishedDate = LocalDateTime.now();
        this.userProfileToReaction = new HashMap<>();
        this.reactionToUserProfiles = new EnumMap<>(ReactionType.class);

        for (ReactionType reaction : ReactionType.values()) {
            reactionToUserProfiles.put(reaction, new HashSet<>());
        }
    }

    /**
     * Returns the unique id of the post.
     * Each post is guaranteed to have a unique id.
     *
     * @return the unique id of the post
     */
    @Override
    public String getUniqueId() {
        return uniqueID.toString();
    }

    /**
     * Returns the author of the post.
     *
     * @return the author of the post
     */
    @Override
    public UserProfile getAuthor() {
        return author;
    }

    /**
     * Returns the date and time when the post was published.
     * A post is published once it is created.
     *
     * @return the date and time when the post was published
     */
    @Override
    public LocalDateTime getPublishedOn() {
        return publishedDate;
    }

    /**
     * Returns the content of the post.
     *
     * @return the content of the post
     */
    @Override
    public String getContent() {
        return content;
    }

    /**
     * Adds a reaction to the post.
     * If the profile has already reacted to the post, the reaction is updated to the latest one.
     * An author of a post can react to their own post.
     *
     * @param userProfile  the profile that adds the reaction
     * @param reactionType the type of the reaction
     * @return true if the reaction is added, false if the reaction is updated
     * @throws IllegalArgumentException if the profile is null
     * @throws IllegalArgumentException if the reactionType is null
     */
    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null");
        }

        if (reactionType == null) {
            throw new IllegalArgumentException("ReactionType cannot be null");
        }

        ReactionType hasPreviousReaction = userProfileToReaction.get(userProfile);

        if (hasPreviousReaction != null) {
            reactionToUserProfiles.get(hasPreviousReaction).remove(userProfile);
        }

        reactionToUserProfiles.get(reactionType).add(userProfile);
        userProfileToReaction.put(userProfile, reactionType);

        return hasPreviousReaction == null;
    }

    /**
     * Removes a reaction from the post.
     *
     * @param userProfile the profile that removes the reaction
     * @return true if the reaction is removed, false if the reaction is not present
     * @throws IllegalArgumentException if the profile is null
     */
    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null");
        }

        ReactionType hasPreviousReaction = userProfileToReaction.get(userProfile);

        if (hasPreviousReaction != null) {
            reactionToUserProfiles.get(hasPreviousReaction).remove(userProfile);
            userProfileToReaction.remove(userProfile);
            return true;
        }

        return false;
    }

    /**
     * Returns all reactions to the post.
     * The returned map is unmodifiable.
     *
     * @return an unmodifiable view of all reactions to the post
     */
    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        Map<ReactionType, Set<UserProfile>> nonEmptyReactions = new EnumMap<>(ReactionType.class);

        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactionToUserProfiles.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                nonEmptyReactions.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
            }
        }

        return Collections.unmodifiableMap(nonEmptyReactions);
    }

    /**
     * Returns the count of a specific reaction type to the post.
     *
     * @param reactionType the type of the reaction
     * @return the count of reactions of the specified type
     * @throws IllegalArgumentException if the reactionType is null
     */
    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("ReactionType cannot be null");
        }

        Set<UserProfile> reactionsSet = reactionToUserProfiles.get(reactionType);
        return reactionsSet != null ? reactionsSet.size() : 0;
    }

    /**
     * Returns the total count of all reactions to the post.
     *
     * @return the total count of all reactions to the post
     */
    @Override
    public int totalReactionsCount() {
        int totalCount = 0;

        for (Set<UserProfile> users : reactionToUserProfiles.values()) {
            totalCount += users.size();
        }

        return totalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialFeedPost that = (SocialFeedPost) o;
        return Objects.equals(uniqueID, that.uniqueID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueID);
    }
}