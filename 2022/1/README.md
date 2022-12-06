# Question
Write a pythons script that solves the following puzzle. Input will be in input.txt.

As your boats approach land, the Elves begin taking inventory of their supplies. One important consideration is food - in particular, the number of Calories each Elf is carrying (your puzzle input).

The Elves take turns writing down the number of Calories contained by the various meals, snacks, rations, etc. that they've brought with them, one item per line. Each Elf separates their own inventory from the previous Elf's inventory (if any) by a blank line.

For example, suppose the Elves finish writing their items' Calories and end up with the following list:

1000
2000
3000

4000

5000
6000

7000
8000
9000

10000

This list represents the Calories of the food carried by five Elves:

The first Elf is carrying food with 1000, 2000, and 3000 Calories, a total of 6000 Calories.
The second Elf is carrying one food item with 4000 Calories.
The third Elf is carrying food with 5000 and 6000 Calories, a total of 11000 Calories.
The fourth Elf is carrying food with 7000, 8000, and 9000 Calories, a total of 24000 Calories.
The fifth Elf is carrying one food item with 10000 Calories.

In case the Elves get hungry and need extra snacks, they need to know which Elf to ask: they'd like to know how many Calories are being carried by the Elf carrying the most Calories. In the example above, this is 24000 (carried by the fourth Elf).

Find the Elf carrying the most Calories. How many total Calories is that Elf carrying?

# Answer
The following python script can be used to solve the puzzle:

```python
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
```