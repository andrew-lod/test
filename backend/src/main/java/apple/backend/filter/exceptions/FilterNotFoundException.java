package apple.backend.filter.exceptions;

import java.util.function.Supplier;

public class FilterNotFoundException  extends RuntimeException {
     public FilterNotFoundException(Integer id) {
            super("Filter with name: " + id + " was not found");
        }

    }
