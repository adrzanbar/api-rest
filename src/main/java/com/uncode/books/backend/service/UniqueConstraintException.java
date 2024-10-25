package com.uncode.books.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;

public class UniqueConstraintException extends ServiceException {

    private final List<String> errors = new ArrayList<>();

    public UniqueConstraintException(DataIntegrityViolationException e) {
        super("Unique constraint violation");
        String causeMessage = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "";
        errors.addAll(extractErrors(causeMessage));
    }

    private List<String> extractErrors(String cause) {
        List<String> fieldNames = new ArrayList<>();

        // Clean the cause message and look for the part that contains the field names.
        String cleanedCause = cause.replaceAll("\\s+", " ").trim(); // Normalize spaces

        // Use a regex to find the pattern that contains the field names
        String regex = "\\((.*?)\\)"; // Match text inside parentheses
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanedCause);

        if (matcher.find()) {
            // If we found a match, split the contents by comma and extract field names
            String fieldsPart = matcher.group(1); // Get the matched group
            String[] fields = fieldsPart.split(","); // Split by comma

            for (String field : fields) {
                // Clean and trim the field names, and remove unwanted text
                String trimmedField = field.trim()
                        .replaceAll("NULLS FIRST", "")
                        .replaceAll("\\(.*?\\)", "")
                        .trim()
                        .toLowerCase(); // Convert to lowercase

                if (!trimmedField.isEmpty()) {
                    // If there's a valid field name, add it to the list
                    fieldNames.add(trimmedField);
                }
            }
        }

        return fieldNames;
    }

    public List<String> getErrors() {
        return errors;
    }
}
