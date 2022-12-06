# Open the input file
with open('input.txt', 'r') as input_file:
    # Initialize a list to store the total number of Calories for each Elf
    elf_calories = []

    # Initialize a variable to store the current Elf's total number of Calories
    current_elf_calories = 0

    # Iterate over the lines in the input file
    for line in input_file:
        # If the line is empty, it indicates that the next Elf's inventory is starting
        if not line.strip():
            # Add the current Elf's total number of Calories to the list
            elf_calories.append(current_elf_calories)
            # Reset the current Elf's total number of Calories to 0
            current_elf_calories = 0
        else:
            # If the line is not empty, it contains the number of Calories for a food item
            # Parse the number of Calories and add it to the current Elf's total
            current_elf_calories += int(line.strip())

    # Add the last Elf's total number of Calories to the list
    elf_calories.append(current_elf_calories)

# Find the Elf carrying the most Calories by getting the maximum value from the list
most_calories = max(elf_calories)

# Print the result
print(f"The Elf carrying the most Calories is carrying {most_calories} Calories.")