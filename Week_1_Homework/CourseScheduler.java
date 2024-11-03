package Week1;

public class CourseScheduler {

    public static int maxNonOverlappingCourses(int[][] courses) {
        for (int i = 0; i < courses.length - 1; i++) {
            for (int j = i + 1; j < courses.length; j++) {
                if (courses[i][1] > courses[j][1]) {
                    int[] temp = courses[i];
                    courses[i] = courses[j];
                    courses[j] = temp;
                }
            }
        }

        int count = 0;
        int lastEndTime = -1;

        for (int[] course : courses) {
            if (course[0] >= lastEndTime) {
                count++;
                lastEndTime = course[1];
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(maxNonOverlappingCourses(new int[][]{{9, 11}, {10, 12}, {11, 13}, {15, 16}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{13, 15}, {13, 17}, {11, 17}}));
    }
}
