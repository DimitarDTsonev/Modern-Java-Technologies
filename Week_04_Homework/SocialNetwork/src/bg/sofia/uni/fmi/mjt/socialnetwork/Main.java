package bg.sofia.uni.fmi.mjt.socialnetwork;

//import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.ReactionType;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

public class Main {
    @SuppressWarnings("checkstyle:MethodLength")
    public static void main(String[] args) {

        UserProfile user1 = new DefaultUserProfile("Alice");
        UserProfile user2 = new DefaultUserProfile("Bob");
        UserProfile user3 = new DefaultUserProfile("Charlie");

        Post post = new SocialFeedPost(user1, "Hello, everyone!");

        System.out.println("Post created by: " + post.getAuthor().getUsername());
        System.out.println("Post content: " + post.getContent());
        System.out.println("Published on: " + post.getPublishedOn());
        System.out.println("Unique ID: " + post.getUniqueId());

        System.out.println("\nAdding reactions...");

        post.addReaction(user2, ReactionType.LIKE);
        post.addReaction(user3, ReactionType.LOVE);

        System.out.println("Reactions after adding:");
        System.out.println(post.getAllReactions());

        System.out.println("\nUpdating reaction...");
        post.addReaction(user2, ReactionType.LAUGH);

        System.out.println("Reactions after update:");
        System.out.println(post.getAllReactions());

        System.out.println("\nReaction counts:");
        for (ReactionType type : ReactionType.values()) {
            System.out.println(type + " reactions: " + post.getReactionCount(type));
        }

        System.out.println("\nTotal reactions count: " + post.totalReactionsCount());

        System.out.println("\nRemoving a reaction...");
        post.removeReaction(user3);

        System.out.println("Reactions after removal:");
        System.out.println(post.getAllReactions());

        System.out.println("\nFinal reaction counts:");
        for (ReactionType type : ReactionType.values()) {
            System.out.println(type + " reactions: " + post.getReactionCount(type));
        }
        System.out.println("Total reactions count: " + post.totalReactionsCount());
    }
}