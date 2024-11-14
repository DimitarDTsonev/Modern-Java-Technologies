package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Comparator;

public class SortByFriendsCountComparator implements Comparator<UserProfile> {

    @Override
    public int compare(UserProfile user1, UserProfile user2) {
        return Integer.compare(user2.getFriends().size(), user1.getFriends().size());
    }
}