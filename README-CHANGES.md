# Changes Made to Address Requirements

## Requirement 1: Handle spaces between commas in input parameters
The issue was that when sending commands with spaces between commas, like `CLI_MOD[1, 895688718, FERNANDO PINTO LINO, 736821455, CALLE ISRRAEL MENDIA#1, EVENTOS]`, the system wasn't accepting them.

### Solution:
Modified the `sacarParametros` method in the `Subject` class to trim spaces from parameters. The key change was adding a `trim()` call to remove spaces before and after each parameter:

```
part = part.trim();
```

This change ensures that spaces before and after commas are properly handled, allowing commands like the example to be processed correctly.

## Requirement 2: Validate IDs exist before executing MOD, DEL, VER commands

The requirement was to validate that IDs exist before executing MOD, DEL, VER commands, and if the ID doesn't exist, return a list of the table.

### Solution:
Modified the `execute` methods in the `ModCommand`, `DelCommand`, and `VerCommand` classes to check if the ID exists before executing the command.

For each command, we added code to:
1. Check if the ID exists by calling the `ver` method
2. If the ID doesn't exist (result is null), call the `listar` method to get all records
3. Return the list of all records with an appropriate message

The message returned when an ID doesn't exist is:
"No se encontró ningún registro con el ID: [id]. Aquí está la lista de todos los registros."

These changes ensure that when a user tries to modify, delete, or view a record with an ID that doesn't exist, they will receive a list of all records in the table instead of an error message.

## Additional Changes:
- Updated error messages in the `LisCommand` class to be consistent with the rest of the code
- Updated comments in the `LisCommand` class to be in English, consistent with the rest of the code

These changes improve the user experience by providing more helpful error messages and ensuring consistent language throughout the codebase.