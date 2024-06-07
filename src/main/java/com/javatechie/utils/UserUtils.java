package com.javatechie.utils;

import com.javatechie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserUtils {

    @Autowired
    private UserRepository userRepository;

    private static final String[] SUFFIXES = {"123", "official", "real", "the"};
    private static final int MAX_SUGGESTIONS = 10;
    private static final Random random = new Random();

    public List<String> suggestUsernames(String desiredUsername) {
        List<String> suggestions = new ArrayList<>();

        // Base suggestion by appending numbers
        suggestions.add(desiredUsername + random.nextInt(1000));

        // Adding common suffixes
        for (String suffix : SUFFIXES) {
            suggestions.add(desiredUsername + suffix);
        }

        // Adding some more variations
        suggestions.add(desiredUsername + "_" + random.nextInt(1000));
        suggestions.add("real_" + desiredUsername);
        suggestions.add("the_" + desiredUsername);

        // Check for availability and filter out taken usernames
        List<String> availableSuggestions = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (isUsernameAvailable(suggestion) && availableSuggestions.size() < MAX_SUGGESTIONS) {
                availableSuggestions.add(suggestion);
            }
        }

        return availableSuggestions;
    }

    private boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
