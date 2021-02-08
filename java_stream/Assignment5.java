package assignment5;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import FormattedOutput.*;

public class Assignment5 {
    public static void main(String[] args) {
        FormattedOutput answer = new FormattedOutput();

        Flights flights = new Flights("flights_small.csv");

        String answer1 = question1(flights);
        System.out.println(answer1);
        answer.addAnswer(1, answer1);

        String answer2 = question2(flights);
        System.out.println(answer2);
        answer.addAnswer(2, answer2);

        String answer3 = question3(flights);
        System.out.println(answer3);
        answer.addAnswer(3, answer3);

        String answer4 = question4(flights);
        System.out.println(answer4);
        answer.addAnswer(4, answer4);

        String answer5 = question5(flights);
        System.out.println(answer5);
        answer.addAnswer(5, answer5);

        String answer6 = question6(flights);
        System.out.println(answer6);
        answer.addAnswer(6, answer6);

        String answer7 = question7(flights);
        System.out.println(answer7);
        answer.addAnswer(7, answer7);

        String answer8 = question8(flights);
        System.out.println(answer8);
        answer.addAnswer(8, answer8);

        answer.writeAnswers();
    }

    static String question1(Flights flights) {
        String carrier = "";
        double highestRate = 0.0;

        for (String c : flights.allCarriers) {
            List<Map<String, String>> all = flights.getFlightsFromCarrier(c);
            List<Map<String, String>> cancelled = flights.getCancelledFlights(all);
            double rate = (double) cancelled.size() / (double) all.size();

            if (rate > highestRate) {
                highestRate = rate;
                carrier = c;
            }
        }
        return carrier + "," + highestRate * 100 + "%";
    }

    static String question2(Flights flights) {
        List<Map<String, String>> cancelled = flights.getCancelledFlights(flights.allFlights);
        Map<String, Long> counts = cancelled.stream().map(x -> x.get("CancellationCode"))
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        // System.out.println(counts);
        return Collections.max(counts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    static String question3(Flights flights) {
        List<Map<String, String>> regular = flights.getRegularFlights(flights.allFlights);
        Map<String, Long> miles = regular.stream().map(x -> {
            Map<String, Long> mile = new HashMap<String, Long>();
            mile.put(x.get("TailNum"), Long.parseLong(x.get("Distance")));
            return mile;
        }).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        // System.out.println(miles);

        return Collections.max(miles.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    static String question4(Flights flights) {
        List<Map<String, String>> regular = flights.getRegularFlights(flights.allFlights);
        Map<String, Long> total = regular.stream().map(x -> {
            Map<String, Long> number = new HashMap<String, Long>();
            number.put(x.get("OriginAirportID"), 1L);
            number.put(x.get("DestAirportID"), 1L);
            return number;
        }).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        // System.out.println(total);

        return Collections.max(total.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    static String question5(Flights flights) {
        List<Map<String, String>> regular = flights.getRegularFlights(flights.allFlights);
        Map<String, Long> total = regular.stream().map(x -> {
            Map<String, Long> number = new HashMap<String, Long>();
            number.put(x.get("OriginAirportID"), 1L);
            number.put(x.get("DestAirportID"), -1L);
            return number;
        }).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        // System.out.println(total);

        return Collections.max(total.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    static String question6(Flights flights) {
        List<Map<String, String>> regular = flights.getRegularFlights(flights.allFlights);
        Map<String, Long> total = regular.stream().map(x -> {
            Map<String, Long> number = new HashMap<String, Long>();
            number.put(x.get("OriginAirportID"), 1L);
            number.put(x.get("DestAirportID"), -1L);
            return number;
        }).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        // System.out.println(total);

        return Collections.min(total.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    static String question7(Flights flights) {
        List<Map<String, String>> regular = flights.getRegularFlights(flights.allFlights);
        // List<Map<String, String>> regular =
        // flights.getRegularFlights(flights.getFlightsFromCarrier("AA"));
        Map<String, Long> total = regular.stream().map(x -> {
            Map<String, Long> number = new HashMap<String, Long>();
            if (Long.parseLong(x.get("ArrDelay")) >= 60 || Long.parseLong(x.get("DepDelay")) >= 60) {
                number.put(x.get("OriginAirportID"), 1L);
            }
            return number;
        }).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        // System.out.println(total);

        return Long.toString(total.values().stream().mapToLong(x -> x).sum());
    }

    static String question8(Flights flights) {
        List<Map<String, String>> regular = flights.getRegularFlights(flights.allFlights);
        Map<String, String> answer = regular.stream()
                .filter(x -> Long.parseLong(x.get("DepDelay")) > 0 && Long.parseLong(x.get("ArrDelay")) <= 0)
                .max(Comparator.comparing(x -> Long.parseLong(x.get("DepDelay")))).orElseThrow(NoSuchElementException::new);
        // System.out.println(answer);

        return answer.get("DayofMonth") + "," + answer.get("DepDelay") + "," + answer.get("TailNum");
    }
}

class Flights {
    private String csvFile;
    private Pattern pattern = Pattern.compile(",");

    public String[] header;
    public List<Map<String, String>> allFlights;
    public List<Map<String, String>> cancelledFlights;
    public Set<String> allCarriers;
    public Map<String, String> cancellationCodes;

    public Flights(String csvFile) {
        this.csvFile = csvFile;

        try {
            initFlights();
            initCancellationCodes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // this.cancelledFlights = this.getCancelledFlights();
        // System.out.println(this.allCarriers);
    }

    private void initFlights() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(csvFile))) {

            allCarriers = new HashSet<String>();
            header = pattern.split(in.readLine());

            allFlights = in.lines().map(x -> {
                String[] line = pattern.split(x);
                Map<String, String> row = new HashMap<String, String>();
                for (int i = 0; i < header.length; i++) {
                    row.put(header[i], line[i]);
                }
                allCarriers.add(line[Arrays.asList(header).indexOf("UniqueCarrier")]);
                return row;
            }).collect(Collectors.toList());
        }
    }

    private void initCancellationCodes() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader("cancellation_codes.csv"))) {
            cancellationCodes = in.lines().skip(1).map(x -> pattern.split(x)).collect(HashMap<String, String>::new,
                    (map, x) -> map.put(x[0], x[1]), HashMap::putAll);
            // System.out.println(cancellationCodes);
        }
    }

    public List<Map<String, String>> getFlightsFromCarrier(String carrier) {
        if (!this.allCarriers.contains(carrier))
            return null;
        return this.allFlights.stream().filter(x -> x.get("UniqueCarrier").equals(carrier))
                .collect(Collectors.toList());
    }

    public List<Map<String, String>> getCancelledFlights(List<Map<String, String>> flights) {
        return flights.stream().filter(x -> x.get("Cancelled").equals("1")).collect(Collectors.toList());
    }

    public List<Map<String, String>> getRegularFlights(List<Map<String, String>> flights) {
        return flights.stream().filter(x -> x.get("Cancelled").equals("0")).collect(Collectors.toList());
    }
}
