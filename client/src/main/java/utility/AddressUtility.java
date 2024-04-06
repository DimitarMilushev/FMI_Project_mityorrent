package main.java.utility;

import kotlin.Pair;

import java.net.InetSocketAddress;

public class AddressUtility {
    /**
     * This function expects an input with both the hostname and port in place
     * Eg.
     * localhost:3000, etc...
     *
     * @param input stringified address
     * @return InetSocketAddress
     */
    public static InetSocketAddress fromString(String input) {
        Pair<String, Integer> pair = parseAddressPort(input);
        return new InetSocketAddress("localhost", pair.getSecond());
    }

    public static Pair<String, Integer> parseAddressPort(String input) {
        final String[] tokens = input.split(":");
        if (tokens.length != 2) throw new IllegalArgumentException("Expected a valid address but got " + input);
        final String hostname = tokens[0].replace("/", "");
        final int port = Integer.parseInt(tokens[1]);
        return new Pair<>(hostname, port);
    }

}
