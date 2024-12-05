package Week1;

public class TextJustifier {
    public static String[] justifyText(String[] words, int maxWidth) {
        String[] result = new String[words.length];
        int resultIndex = 0;

        int i = 0;
        while (i < words.length) {
            int lineLength = words[i].length();
            int last = i + 1;

            while (last < words.length && lineLength + words[last].length() + (last - i) <= maxWidth) {
                lineLength += words[last].length();
                last++;
            }

            StringBuilder line = new StringBuilder();
            int wordCount = last - i;

            if (last == words.length || wordCount == 1) {
                for (int j = i; j < last; j++) {
                    line.append(words[j]);
                    if (j < last - 1) {
                        line.append(" ");
                    }
                }
                while (line.length() < maxWidth) {
                    line.append(" ");
                }
            } else {
                int totalSpaces = maxWidth - lineLength;
                int spacesBetweenWords = totalSpaces / (wordCount - 1);
                int extraSpaces = totalSpaces % (wordCount - 1);

                for (int j = i; j < last; j++) {
                    line.append(words[j]);
                    if (j < last - 1) {
                        for (int k = 0; k < spacesBetweenWords; k++) {
                            line.append(" ");
                        }
                        if (extraSpaces > 0) {
                            line.append(" ");
                            extraSpaces--;
                        }
                    }
                }
            }

            result[resultIndex] = line.toString();
            resultIndex++;

            i = last;
        }

        String[] finalResult = new String[resultIndex];
        System.arraycopy(result, 0, finalResult, 0, resultIndex);
        return finalResult;
    }

    public static void main(String[] args) {
        String[] words1 = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."};
        int maxWidth1 = 11;
        String[] justifiedText1 = justifyText(words1, maxWidth1);
        for (String line : justifiedText1) {
            System.out.println("\"" + line + "\"");
        }

        String[] words2 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        int maxWidth2 = 20;
        String[] justifiedText2 = justifyText(words2, maxWidth2);
        for (String line : justifiedText2) {
            System.out.println("\"" + line + "\"");
        }
    }
}
