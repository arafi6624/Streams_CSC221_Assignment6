import java.io.CharArrayReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Assignment_6 {

    /**
     * Use Java Streams only. Don't use any looping structures or conditional statements (IF, TERNARY, SWITCH)
     * Using stream operations only, convert the stack parameter and return the results as a Queue.
     * The original stack remains unchanged.
     * Hint, Java's LinkedList implements queue operations. e.g., Queue<Character> queue = new LinkedList<>();
     *
     * @param stackOriginal, a stack object
     * @return a queue objects with a copy of the stack's elements in the same order as the stack
     */
    public static Queue<Character> stackToQueue(final Stack<Character> stackOriginal) {

        Queue<Character> queue = new LinkedList<>();
        stackOriginal.stream()
                .forEach(i -> queue.add(i))
        ;

        return queue;
    }

    /**
     * Use Java Streams only. Don't use any looping structures or conditional statements (IF, TERNARY, SWITCH)
     * Using stream operations only, generate a list of random and unique numbers using java.security.SecureRandom as follows:
     * 1. Random numbers between min (inclusive) and max (exclusive)
     * 2. The number of randoms is <= to poolSize after removing repeated numbers
     * 3. The list is sorted and unique. For simplicity, we will remove repeated elements from the after generating the random
     * numbers. Therefore, the returned list size will be <= poolSize
     * <p>
     * Hint, during testing, use a seed to force SecureRandom to produce the same numbers. Once you're satisfied with the
     * results, remove the seed. For example, new SecureRandom("ABC".getBytes()) generates the same numbers over different runs while
     * new SecureRandom() generates different numbers over different runs
     *
     * @param min      the minimum random number range (inclusive)
     * @param max      the maximum random number range (exclusive)
     * @param poolSize the number of random numbers to generate. After removing unique elements, the final list size is <= poolSize
     * @return a sorted and unique list of random numbers between min (inclusive) and max (exclusive) and size <= poolSize
     */
    public static List<Integer> uniqueSortedRandoms(final int min, final int max, final int poolSize) {

        List<Integer> list = new SecureRandom("ABC".getBytes()).ints(poolSize,min,max)
                .boxed()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return list;
    }

    /**
     * Use Java Streams only. Don't use any looping structures or conditional statements (IF, TERNARY, SWITCH)
     * Using stream operations only, return the sum of values within the listInt parameter between fromIndex and
     * toIndex (inclusive).
     * The method must use array-like indexing (i.e., 0-base)
     * If the sum ranges are out of bounds, the method returns -1
     *
     * @param listInt   a list of integer values
     * @param fromIndex the starting sum position
     * @param toIndex   the end sum position
     * @return the sum of values between the parameters fromIndex and toIndex or -1 if the from and to parameters are invalid
     */
    public static int rangeSum(final List<Integer> listInt, final int fromIndex, final int toIndex) throws IllegalArgumentException {

        AtomicInteger sum = new AtomicInteger();
        try {
            listInt.stream()
                    .skip(fromIndex)
                    .limit(toIndex - fromIndex + 1)
                    .forEach(i -> sum.addAndGet(i))
            ;
        }
        catch (Exception e){
            sum.set(-1);
        }

        int result = sum.intValue();

        return result; // returned value if sum ranges are incorrect
    }

    /**
     * Use Java Streams only. Don't use any looping structures or conditional statements (IF, TERNARY, SWITCH)
     * Using stream operations only, return a count of each character within the provided text file.
     * Hint: This question is a miniature version of the example in Section 17.13 (slide 177)
     *
     * @param fileName the file to count the characters for
     * @return a map of each character and its count
     * @throws IOException thrown if the file read operation fails
     */
    public static Map<Character, Long> fileCharSummary(final String fileName) throws IOException {

        Pattern pattern = Pattern.compile("\\s+");

        Map<Character, Long> result = Files.lines(Paths.get(fileName))
                .filter(line -> !line.isEmpty())
                .flatMap(line -> pattern.splitAsStream(line))
                .flatMap(word -> word.toUpperCase().chars().mapToObj(i -> (char) i))
                .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()))
                ;

        return result;
    }

    /*
     * Please do not modify
     */
    public static void main(String[] args) throws IOException {

        // Test 1, converting a stack to queue
        Stack<Character> stackChar = new Stack<>();
        IntStream.rangeClosed(65, 90)
                .mapToObj(i -> (char) i)
                .forEach(stackChar::add);

        System.out.printf("%s%s%n", "Stack before queue: ", joinStream(stackChar));
        System.out.printf("%s%s%n", "stackToQueue returned: ", joinStream(stackToQueue(stackChar)));
        System.out.printf("%s%s%n%n", "Stack after queue: ", joinStream(stackChar));

        // Test 2, Random number generation. Your results will vary
        List<Integer> listOrderedRands = uniqueSortedRandoms(25, 200, 20);
        System.out.printf("%s%s%n%n", "Ordered list of random numbers without repetition: ", joinStream(listOrderedRands));

        // Test 3, summing a list's elements using different ranges
        List<Integer> listSumTest = Arrays.asList(13, 10, 20, 5, 1, 9, 16, 2, 4, 18, 19, 12, 16, 17, 8);
        System.out.printf("%s%,d%n", "Sum of listSumTest between 0 and 15: ", rangeSum(listSumTest, 0, 15));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 15 and 0: ", rangeSum(listSumTest, 15, 0));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 0 and 14: ", rangeSum(listSumTest, 0, 14));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 14 and 0: ", rangeSum(listSumTest, 14, 0));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 0 and 13: ", rangeSum(listSumTest, 0, 13));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 5 and 4: ", rangeSum(listSumTest, 5, 4));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 3 and 7: ", rangeSum(listSumTest, 3, 7));
        System.out.printf("%s%,d%n", "Sum of listSumTest between 14 and 14: ", rangeSum(listSumTest, 14, 14));

        // Test 4, Letter counter from file
        System.out.printf("%n%s%s", "Character counts: ", joinStream(fileCharSummary("TestFile.txt").entrySet()));
    }

    /**
     * Don't modify. This is a helper function for joining a collection's elements as a String object
     *
     * @param collection the collection with elements to join
     * @return a String object representing all elements in the list sparated by a ', '
     */
    private static <E> String joinStream(Collection<E> collection) {

        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
