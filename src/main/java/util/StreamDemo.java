package util;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @program: md
 * @description:
 * @author: Zhang
 * @create: 2019-05-09 10:10
 **/
public class StreamDemo {
    /**
     * 计算一系列数字的最大公约数(gcd) .
     * 使用 Arrays.stream().reduce() 和 GCD（使用递归公式）计算一组数字的最大公约数
     */
    public static OptionalInt gcd(int[] numbers) {
        return Arrays.stream(numbers).reduce(StreamDemo::gcd);
    }

    private static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    /**
     * 计算数字数组的最低公共倍数(LCM) .
     * 使用 Arrays.stream().reduce() 和 LCM公式(使用递归)来计算数字数组的最低公共倍数
     */
    public static OptionalInt lcm(int[] numbers) {
        IntBinaryOperator lcm = (x, y) -> (x * y) / gcd(x, y);
        return Arrays.stream(numbers).reduce(lcm::applyAsInt);
    }

    /**
     * 获取一组数字中的最大值
     */
    public static OptionalInt arrayMax(int[] numbers) {
        return Arrays.stream(numbers).max();
    }

    /**
     * 获取一组数字中的最小值
     */
    public static OptionalInt arrayMin(int[] numbers) {
        return Arrays.stream(numbers).min();
    }

    /**
     * 将数组分割成特定大小的小数组 .
     */
    public static int[][] chunk(int[] numbers, int size) {
        return IntStream.iterate(0, i -> i + size)
                        .limit((long) Math.ceil((double) numbers.length / size))
                        .mapToObj(cur -> Arrays.copyOfRange(numbers, cur, cur + size > numbers.length ? numbers.length : cur + size))
                        .toArray(int[][]::new);
    }

    /**
     * 计算数组中某个值出现的次数 .
     * 使用 Arrays.stream().filter().count() 计算等于指定值的值的总数
     */
    public static long countOccurrences(int[] numbers, int value) {
        return Arrays.stream(numbers)
                     .filter(number -> number == value)
                     .count();
    }

    /**
     * 数组扁平化 .
     * 使用递归实现 , Arrays.stream().flatMapToInt()
     */
    public static int[] deepFlatten(Object[] input) {
        return Arrays.stream(input)
                     .flatMapToInt(o -> o instanceof Object[] ? Arrays.stream(deepFlatten((Object[]) o)) : IntStream.of((Integer) o))
                     .toArray();
    }

    /**
     * 返回两个数组之间的差异 .
     * 从 b 中创建一个集合 , 然后在 a 上使用 Arrays.stream().filter() 只保留 b 中不包含的值 .
     */
    public static int[] difference(int[] first, int[] second) {
        Set<Integer> set = Arrays.stream(second).boxed().collect(Collectors.toSet());
        return Arrays.stream(first)
                     .filter(v -> !set.contains(v))
                     .toArray();
    }

    /**
     * 从比较器函数不返回true的数组中筛选出所有值 .
     * int的比较器是使用IntbinaryPerator函数来实现的 .
     * 使用 Arrays.stream().filter() 和 Arrays.stream().noneMatch() 查找相应的值
     */
    public static int[] differenceWith(int[] first, int[] second, IntBinaryOperator comparator) {
        return Arrays.stream(first)
                     .filter(a ->
                             Arrays.stream(second)
                                   .noneMatch(b -> comparator.applyAsInt(a, b) == 0)
                     ).toArray();
    }

    /**
     * 返回数组的所有不同值 .
     * 使用 Arrays.stream().distinct() 去除所有重复的值
     */
    public static int[] distinctValuesOfArray(int[] elements) {
        return Arrays.stream(elements).distinct().toArray();
    }

    /**
     * 移除数组中的元素 , 直到传递的函数返回true为止 .返回数组中的其余元素 .
     * 使用数组循环遍历数组 , 将数组的第一个元素删除 , 直到函数返回的值为真为止 .返回其余的元素
     */
    public static int[] dropElements(int[] elements, IntPredicate condition) {
        while (elements.length > 0 && !condition.test(elements[0])) {
            elements = Arrays.copyOfRange(elements, 1, elements.length);
        }
        return elements;
    }

    /**
     * 返回一个新数组 , 从右边移除n个元素 .
     * 检查n是否短于给定的数组 , 并使用 Array.copyOfRange() 以便对其进行相应的切片或返回一个空数组
     */
    public static int[] dropRight(int[] elements, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n is less than 0");
        }
        return n < elements.length
                ? Arrays.copyOfRange(elements, 0, elements.length - n)
                : new int[0];
    }

    /**
     * 返回数组中的每个第n个元素 .
     * 使用 IntStream.range().filter() 创建一个新数组 , 该数组包含给定数组的每个第n个元素
     */
    public static int[] everyNth(int[] elements, int nth) {
        return IntStream.range(0, elements.length)
                        .filter(i -> i % nth == nth - 1)
                        .map(i -> elements[i])
                        .toArray();
    }

    /**
     * 筛选出数组中的非唯一值 .
     * 对只包含唯一值的数组使用 Arrays.stream().filter()
     */
    public static int[] filterNonUnique(int[] elements) {
        return Arrays.stream(elements)
                     .filter(el -> indexOf(elements, el) == lastIndexOf(elements, el))
                     .toArray();
    }

    /**
     * 查找数组中元素的索引 , 在不存在元素的情况下返回-1 .
     * 使用 IntStream.range().filter() 查找数组中元素的索引
     */
    public static int indexOf(int[] elements, int el) {
        return IntStream.range(0, elements.length)
                        .filter(idx -> elements[idx] == el)
                        .findFirst()
                        .orElse(-1);
    }

    /**
     * 查找数组中元素的最后索引 , 在不存在元素的情况下返回-1 .
     * 使用 IntStream.iterate().limit().filter() 查找数组中元素的索引
     */
    public static int lastIndexOf(int[] elements, int el) {
        return IntStream.iterate(elements.length - 1, i -> i - 1)
                        .limit(elements.length)
                        .filter(idx -> elements[idx] == el)
                        .findFirst()
                        .orElse(-1);
    }

    /**
     * 使数组扁平 .
     * 使用 Arrays.stream().flatMapToInt().toArray() 创建一个新数组
     */
    public static int[] flatten(Object[] elements) {
        return Arrays.stream(elements)
                     .flatMapToInt(el -> el instanceof int[] ? Arrays.stream((int[]) el) : IntStream.of((int) el)
                     ).toArray();
    }

    /**
     * 将数组压平到指定的深度
     */
    public static Object[] flattenDepth(Object[] elements, int depth) {
        return depth == 0 ? elements : Arrays.stream(elements)
                                             .flatMap(el -> el instanceof Object[] ? Arrays.stream(flattenDepth((Object[]) el, depth - 1)) : Arrays.stream(new Object[]{el}))
                                             .toArray();
    }

    /**
     * 根据给定函数对数组元素进行分组 .
     * 使用 Arrays.stream().collect(Collectors.groupingBy()) 分组
     */
    public static <T, R> Map<R, List<T>> groupBy(T[] elements, Function<T, R> func) {
        return Arrays.stream(elements).collect(Collectors.groupingBy(func));
    }

    /**
     * 返回数组中除去最后一个的所有元素 .
     * 使用 Arrays.copyOfRange() 返回除最后一个之外的所有元素
     */
    public static <T> T[] initial(T[] elements) {
        return Arrays.copyOfRange(elements, 0, elements.length - 1);
    }

    /**
     * 初始化一个数组 , 该数组包含在指定范围内的数字 , 传入 start 和 end
     */
    public static int[] initializeArrayWithRange(int end, int start) {
        return IntStream.rangeClosed(start, end).toArray();
    }

    /**
     * 使用指定的值初始化并填充数组
     */
    public static int[] initializeArrayWithValues(int n, int value) {
        return IntStream.generate(() -> value).limit(n).toArray();
    }

    /**
     * 返回两个数组中存在的元素列表 .
     * 从第二步创建一个集合 , 然后在 a 上使用 Arrays.stream().filter() 来保存包含在 b 中的值
     */
    public static int[] intersection(int[] first, int[] second) {
        Set<Integer> set = Arrays.stream(second).boxed().collect(Collectors.toSet());
        return Arrays.stream(first)
                     .filter(set::contains)
                     .toArray();
    }

    /**
     * 如果数组按升序排序 , 则返回 1 , 如果数组按降序排序 , 返回 -1 , 如果没有排序 , 则返回 0 .
     * 计算前两个元素的排序 direction .使用for循环对数组进行迭代 , 并对它们进行成对比较 .如果 direction 发生变化 , 则返回 0 ,  如果到达最后一个元素 , 则返回 direction
     */
    public static <T extends Comparable<? super T>> int isSorted(T[] arr) {
        final int direction = arr[0].compareTo(arr[1]) < 0 ? 1 : -1;
        for (int i = 0; i < arr.length; i++) {
            T val = arr[i];
            if (i == arr.length - 1) return direction;
            else if ((val.compareTo(arr[i + 1]) * direction > 0)) return 0;
        }
        return direction;
    }

    /**
     * 将数组的所有元素连接到字符串中 , 并返回此字符串 .
     * 使用 IntStream.range 创建一个指定索引的数组 .然后 , 使用 Stream.reduce 将元素组合成字符串
     */
    public static <T> String join(T[] arr, String separator, String end) {
        return IntStream.range(0, arr.length)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, arr[i]))
                        .reduce("", (acc, val) -> val.getKey() == arr.length - 2
                                ? acc + val.getValue() + end
                                : val.getKey() == arr.length - 1 ? acc + val.getValue() : acc + val.getValue() + separator, (fst, snd) -> fst);
    }

    public static <T> String join(T[] arr, String separator) {
        return join(arr, separator, separator);
    }

    public static <T> String join(T[] arr) {
        return join(arr, ",");
    }

    /**
     * 返回数组的第n个元素 .
     * Use Arrays.copyOfRange() 优先得到包含第n个元素的数组
     */
    public static <T> T nthElement(T[] arr, int n) {
        if (n > 0) {
            return Arrays.copyOfRange(arr, n, arr.length)[0];
        }
        return Arrays.copyOfRange(arr, arr.length + n, arr.length)[0];
    }

    /**
     * 从对象中选择与给定键对应的键值对 .
     * 使用 Arrays.stream 过滤 arr 中存在的所有键 .然后 , 使用 Collectors.toMap 将所有的key转换为Map
     */
    public static <T, R> Map<T, R> pick(Map<T, R> obj, T[] arr) {
        return Arrays.stream(arr)
                     .filter(obj::containsKey)
                     .collect(Collectors.toMap(k -> k, obj::get));
    }

    /**
     * 根据条件筛选对象数组 , 同时筛选出未指定的键 .
     * 使用 Arrays.stream().filter() 根据谓词 fn 过滤数组 , 以便返回条件为真的对象 . 对于每个过滤的Map对象 , 创建一个新的Map , 其中包含 keys 中的键 .最后 , 将Map对象收集到一个数组中
     */
    public static Map<String, Object>[] reducedFilter(Map<String, Object>[] data, String[] keys, Predicate<Map<String, Object>> fn) {
        return Arrays.stream(data)
                     .filter(fn)
                     .map(el -> Arrays.stream(keys).filter(el::containsKey)
                                      .collect(Collectors.toMap(Function.identity(), el::get)))
                     .toArray((IntFunction<Map<String, Object>[]>) Map[]::new);
    }

    /**
     * 从数组中返回一个随机元素 .
     * 使用 Math.Randoman() 生成一个随机数 , 然后将它乘以数组的 length , 然后使用 Math.floor() 获得一个最近的整数 , 该方法也适用于字符串
     */
    public static <T> T sample(T[] arr) {
        return arr[(int) Math.floor(Math.random() * arr.length)];
    }

    /**
     * 从 array 到 array 大小的唯一键获取 n 个随机元素 .
     * 根据Fisher-Yates算法 , 使用 Array.copyOfRange() 获得优先的 n 个元素
     */
    public static <T> T[] sampleSize(T[] input, int n) {
        T[] arr = Arrays.copyOf(input, input.length);
        int length = arr.length;
        int m = length;
        while (m > 0) {
            int i = (int) Math.floor(Math.random() * m--);
            T tmp = arr[i];
            arr[i] = arr[m];
            arr[m] = tmp;
        }
        return Arrays.copyOfRange(arr, 0, n > length ? length : n);
    }

    /**
     * 将数组值的顺序随机化 , 返回一个新数组 .
     * 根据 Fisher-Yates 算法 重新排序数组的元素
     */
    public static <T> T[] shuffle(T[] input) {
        T[] arr = Arrays.copyOf(input, input.length);
        int length = arr.length;
        int m = length;
        while (m > 0) {
            int i = (int) Math.floor(Math.random() * m--);
            T tmp = arr[i];
            arr[i] = arr[m];
            arr[m] = tmp;
        }
        return arr;
    }

    /**
     * 返回出现在两个数组中的元素数组 .
     * 使用 Arrays.stream().filter() 移除 , 然后使用 Arrays.stream().anyMatch() 匹配 second 部分的值
     */
    public static <T> T[] similarity(T[] first, T[] second) {
        return Arrays.stream(first)
                     .filter(a -> Arrays.stream(second).anyMatch(b -> Objects.equals(a, b)))
                     // Make a new array of first's runtime type, but empty content:
                     .toArray(i -> (T[]) Arrays.copyOf(new Object[0], i, first.getClass()));
    }

    /**
     * 返回一个指定类型的空数组
     */
    public static <T> T[] emptyArray(Class<T> clz) {
        return (T[]) Array.newInstance(clz, 0);
    }


    /**
     * 返回值应该插入到数组中的最低索引 , 以保持其排序顺序 .
     * 检查数组是否按降序（松散地）排序 . 使用 IntStream.range().filter() 来找到元素应该被插入的合适的索引
     */
    public static <T extends Comparable<? super T>> int sortedIndex(T[] arr, T el) {
        boolean isDescending = arr[0].compareTo(arr[arr.length - 1]) > 0;
        return IntStream.range(0, arr.length)
                        .filter(i -> isDescending ? el.compareTo(arr[i]) >= 0 : el.compareTo(arr[i]) <= 0)
                        .findFirst()
                        .orElse(arr.length);
    }

    /**
     * 返回两个数组之间的对称差异 .
     * 从每个数组中创建一个 Set , 然后使用 Arrays.stream().filter() 来保持其他值不包含的值 .最后 , 连接两个数组并创建一个新数组并返回
     */
    public static <T> T[] symmetricDifference(T[] first, T[] second) {
        Set<T> sA = new HashSet<>(Arrays.asList(first));
        Set<T> sB = new HashSet<>(Arrays.asList(second));

        return Stream.concat(
                Arrays.stream(first).filter(a -> !sB.contains(a)),
                Arrays.stream(second).filter(b -> !sA.contains(b))
        ).toArray(i -> (T[]) Arrays.copyOf(new Object[0], i, first.getClass()));
    }

    /**
     * 返回数组中除第一个元素外的所有元素 .
     * 如果数组的长度大于1 , 则返回 Arrays.copyOfRange(1) , 否则返回整个数组
     */
    public static <T> T[] tail(T[] arr) {
        return arr.length > 1
                ? Arrays.copyOfRange(arr, 1, arr.length)
                : arr;
    }

    /**
     * 返回一个从开头删除n个元素的数组
     */
    public static <T> T[] take(T[] arr, int n) {
        return Arrays.copyOfRange(arr, 0, n);
    }

    /**
     * 返回从末尾移除n个元素的数组
     */
    public static <T> T[] takeRight(T[] arr, int n) {
        return Arrays.copyOfRange(arr, arr.length - n, arr.length);
    }

    /**
     * 返回两个数组中任何一个中存在的每个元素一次 .
     * 使用 a 和 b 的所有值创建一个 Set , 并将其转换为数组
     */
    public static <T> T[] union(T[] first, T[] second) {
        Set<T> set = new HashSet<>(Arrays.asList(first));
        set.addAll(Arrays.asList(second));
        return set.toArray((T[]) Arrays.copyOf(new Object[0], 0, first.getClass()));
    }

    /**
     * 筛选出具有指定值之一的数组的元素 .
     * 使用 Arrays.strean().filter() 创建一个数组 , 排除(使用 !Arrays.asList(elements).contains())所有命中的值
     */
    public static <T> T[] without(T[] arr, T... elements) {
        List<T> excludeElements = Arrays.asList(elements);
        return Arrays.stream(arr)
                     .filter(el -> !excludeElements.contains(el))
                     .toArray(i -> (T[]) Arrays.copyOf(new Object[0], i, arr.getClass()));
    }

    /**
     * 根据原始数组中的位置创建元素数组
     */
    public static List<Object[]> zip(Object[]... arrays) {
        OptionalInt max = Arrays.stream(arrays).mapToInt(arr -> arr.length).max();
        return IntStream.range(0, max.getAsInt())
                        .mapToObj(i -> Arrays.stream(arrays)
                                             .map(arr -> i < arr.length ? arr[i] : null)
                                             .toArray())
                        .collect(Collectors.toList());
    }

    /**
     * 给定有效的属性标识符数组和值数组 , 返回将属性与值关联的对象
     */
    public static Map<String, Object> zipObject(String[] props, Object[] values) {
        return IntStream.range(0, props.length)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(props[i], i < values.length ? values[i] : null))
                        .collect(
                                HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }

    /**
     * 返回两个或两个以上数字的平均值
     */
    public static double average(int[] arr) {
        return IntStream.of(arr)
                        .average()
                        .orElseThrow(() -> new IllegalArgumentException("Array is empty"));
    }

    /**
     * 生成一个字符串的所有字符（包含重复）
     */
    public static List<String> anagrams(String input) {
        if (input.length() <= 2) {
            return input.length() == 2
                    ? Arrays.asList(input, input.substring(1) + input.substring(0, 1))
                    : Collections.singletonList(input);
        }
        return IntStream.range(0, input.length())
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, input.substring(i, i + 1)))
                        .flatMap(entry ->
                                anagrams(input.substring(0, entry.getKey()) + input.substring(entry.getKey() + 1))
                                        .stream()
                                        .map(s -> entry.getValue() + s))
                        .collect(Collectors.toList());
    }

    /**
     * 以字节为单位返回字符串的长度
     */
    public static int byteSize(String input) {
        return input.getBytes().length;
    }

    /**
     * 将字符串首字母大写
     */
    public static String capitalize(String input, boolean lowerRest) {
        return input.substring(0, 1).toUpperCase() +
                (lowerRest
                        ? input.substring(1, input.length()).toLowerCase()
                        : input.substring(1, input.length()));
    }

    /**
     * 将字符串中每个单词的首字母大写
     */
    public static String capitalizeEveryWord(final String input) {
        return Pattern.compile("\\b(?=\\w)").splitAsStream(input)
                      .map(w -> capitalize(w, false))
                      .collect(Collectors.joining());
    }

    /**
     * 在提供的字符串中返回元音的个数
     */
    public static int countVowels(String input) {
        return input.replaceAll("[^aeiouAEIOU]", "").length();
    }

    /**
     * 转义要在正则表达式中使用的字符串
     */
    public static String escapeRegExp(String input) {
        return Pattern.quote(input);
    }

    /**
     * 从驼峰式转换字符串
     */
    public static String fromCamelCase(String input, String separator) {
        return input
                .replaceAll("([a-z\\d])([A-Z])", "$1" + separator + "$2")
                .toLowerCase();
    }

    /**
     * 如果给定的字符串是绝对URL , 则返回 true , 否则返回 false
     */
    public static boolean isAbsoluteUrl(String url) {
        return Pattern.compile("^[a-z][a-z0-9+.-]*:").matcher(url).find();
    }

    /**
     * 检查字符串是否为小写
     */
    public static boolean isLowerCase(String input) {
        return Objects.equals(input, input.toLowerCase());
    }

    /**
     * 检查字符串是否为大写
     */
    public static boolean isUpperCase(String input) {
        return Objects.equals(input, input.toUpperCase());
    }

    /**
     * 用指定的掩码字符替换除最后 num 个字符以外的所有字符
     */
    public static String mask(String input, int num, String mask) {
        int length = input.length();
        return num > 0
                ?
                input.substring(0, length - num).replaceAll(".", mask)
                        + input.substring(length - num)
                :
                input.substring(0, Math.negateExact(num))
                        + input.substring(Math.negateExact(num), length).replaceAll(".", mask);
    }

    /**
     * 判断一个字符串是否回文
     */
    public static boolean isPalindrome(String input) {
        String s = input.toLowerCase().replaceAll("[\\W_]", "");
        return Objects.equals(s, new StringBuilder(s).reverse().toString()
        );
    }

    /**
     * 反转字符串
     */
    public static String reverseString(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    /**
     * 按字母顺序排列字符串中的字符
     */
    public static String sortCharactersInString(String input) {
        return Arrays.stream(input.split("")).sorted().collect(Collectors.joining());
    }

    /**
     * 将多行字符串拆分为行数组
     */
    public static String[] splitLines(String input) {
        return input.split("\\r?\\n");
    }

    /**
     * 转换一个字符串为驼峰式
     */
    public static String toCamelCase(String input) {
        Matcher matcher = Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+").matcher(input);
        List<String> matchedParts = new ArrayList<>();
        while (matcher.find()) {
            matchedParts.add(matcher.group(0));
        }
        String s = matchedParts.stream()
                               .map(x -> x.substring(0, 1).toUpperCase() + x.substring(1).toLowerCase())
                               .collect(Collectors.joining());
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * 将字符串转换为kebab大小写
     */
    public static String toKebabCase(String input) {
        Matcher matcher = Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+").matcher(input);
        List<String> matchedParts = new ArrayList<>();
        while (matcher.find()) {
            matchedParts.add(matcher.group(0));
        }
        return matchedParts.stream()
                           .map(String::toLowerCase)
                           .collect(Collectors.joining("-"));
    }

    /**
     * 正则匹配
     */
    public static List<String> match(String input, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        List<String> matchedParts = new ArrayList<>();
        while (matcher.find()) {
            matchedParts.add(matcher.group(0));
        }
        return matchedParts;
    }

    /**
     * 将字符串转换为蛇形小写 , 如 Im_Biezhi
     */
    public static String toSnakeCase(String input) {
        Matcher matcher = Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+").matcher(input);
        List<String> matchedParts = new ArrayList<>();
        while (matcher.find()) {
            matchedParts.add(matcher.group(0));
        }
        return matchedParts.stream()
                           .map(String::toLowerCase)
                           .collect(Collectors.joining("_"));
    }

    /**
     * 将字符串截断到指定的长度
     */
    public static String truncateString(String input, int num) {
        return input.length() > num
                ? input.substring(0, num > 3 ? num - 3 : num) + "..."
                : input;
    }

    /**
     * 将给定的字符串转换为单词数组
     */
    public static String[] words(String input) {
        return Arrays.stream(input.split("[^a-zA-Z-]+"))
                     .filter(s -> !s.isEmpty())
                     .toArray(String[]::new);
    }

    /**
     * 将InputStream转换为字符串
     */
    public static String convertInputStreamToString(final InputStream in) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    /**
     *  从指定范围内获取一组随机数
     */
    public static int[] randomInts(int total, int start, int end) {
        return ThreadLocalRandom.current().ints(total, start, end).toArray();
    }

    /**
     * 将文件内容读入字符串
     */
    public String readFileAsString(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    /**
     * 将异常堆栈跟踪转换为字符串
     */
    public static String stackTraceAsString(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * 拼接数组
     */
    public static <T> T[] concat(T[] first, T[] second) {
        return Stream.concat(
                Stream.of(first),
                Stream.of(second)
        ).toArray(i -> (T[]) Arrays.copyOf(new Object[0], i, first.getClass()));
    }

    /**
     * 获取当前工作目录
     */
    public static String getCurrentWorkingDirectoryPath() {
        return FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    }

    /**
     * 检查字符串是否为数字
     */
    public static boolean isNumeric(final String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return IntStream.range(0, input.length())
                        .allMatch(i -> Character.isDigit(input.charAt(i)));
    }

    /**
     * 查找大于或等于该值的下一个幂 .
     * 该方法使用左移运算符将1与右侧的值位移 .右侧使用 Integer.numberOfLeadingZeros方法 . 001 << 2 would be 100. 100 in decimal is equal to 4.
     * Integer.numberOfLeadingZeros 给出了数值前导零的数目 .例如 , 调用 Integer.numberOfLeadingZeros(3) 将赋值为30 . 这是因为3在二进制中表示为 11 .由于整数有32位 , 所以有30位有0位 .左移运算符的右边变为 32-30 = 2 . 左移1 , 即 001 << 2 将是 100 , 十进制中的 100 等于 4
     */
    public static int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    /**
     * 检查数字是否是偶数 .
     * 这个方法使用按位运算符 , 0b1 是1的二进制表示 . 因为Java 7可以通过用 0b 或 0B 作为前缀来编写二进制文字 . 数字为偶数时 , ＆ 运算符将返回0 . 例如 , IsEven(4) 会导致 100 & 001 , ＆ 的结果将是 000
     */
    public static boolean isEven(final int value) {
        return (value & 0b1) == 0;
    }

    /**
     * 检查一个值是2的正幂 .
     * 为了理解它是如何工作的 , 让我们假设我们调用了 IsPowerOfTwo(4) .
     * 当值大于0时 , 将评估 && 运算符的右侧 .
     * (~value + 1) 的结果等于值本身 , ~100 + 001 => 011 + 001 => 100 .
     * (value & value) 的结果是value , 100 & 100 => 100. .
     * 当值等于值时 , 这将把值表达为真值
     */
    public static boolean isPowerOfTwo(final int value) {
        return value > 0 && ((value & (~value + 1)) == value);
    }

    /**
     * 生成一个介于 Integer.MIN_VALUE 和 Integer.MAX_VALUE 之间的随机数
     */
    public static int generateRandomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    /**
     * 返回 java.io.tmpdir 系统属性的值 .如果末尾没有分隔符 , 则追加分隔符
     */
    public static String tmpDirName() {
        String tmpDirName = System.getProperty("java.io.tmpdir");
        if (!tmpDirName.endsWith(File.separator)) {
            tmpDirName += File.separator;
        }

        return tmpDirName;
    }

    /**
     * 以小写字符串的形式获取操作系统的名称
     */
    public static String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * 检查JVM是否为debug模式
     */
    public static boolean isDebuggerAttached() {
        final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getInputArguments()
                            .stream()
                            .anyMatch(arg -> arg.contains("-agentlib:jdwp"));

    }

    /**
     * 将由空格分隔的数字字符串转换为 int 数组
     */

    public static int[] stringToIntegers(String numbers) {
        return Arrays.stream(numbers.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * 此方法返回由给定类及其超类实现的所有接口 .
     * 该方法通过连接两个Stream来工作 .第一个Stream是通过创建带有接口的流和接口实现的所有接口来递归构建的 . 第二个Stream对超类也是如此 .其结果是删除重复项后将两个Stream连接起来
     */
    public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
        return Stream.concat(
                Arrays.stream(cls.getInterfaces()).flatMap(intf ->
                        Stream.concat(Stream.of(intf), getAllInterfaces(intf).stream())),
                cls.getSuperclass() == null ? Stream.empty() : getAllInterfaces(cls.getSuperclass()).stream()
        ).distinct().collect(Collectors.toList());
    }

    /**
     * 此方法检查指定的类是内部类还是静态嵌套类
     */
    public static boolean isInnerClass(final Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
    }

    /**
     * 将枚举转换为 Map , 其中 key 是枚举名 , value 是枚举本身
     */
    public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                     .collect(Collectors.toMap(Enum::name, Function.identity()));
    }

}
