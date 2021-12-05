#include <stdio.h>
#include <stdlib.h>
#define BOARD_SIZE 1000
#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a > b ? b : a)

/*
 * Compile: gcc -ansi -pedantic -Wall solution.c -o solution
 * Run: ./solution sample.txt
 */

void draw_vertical(int board[BOARD_SIZE][BOARD_SIZE], int x, int y1, int y2)
{
    int y;
    for (y = MIN(y1, y2); y <= MAX(y1, y2); y++)
    {
        board[x][y] += 1;
    }
}

void draw_horizontal(int board[BOARD_SIZE][BOARD_SIZE], int y, int x1, int x2)
{
    int x;
    for (x = MIN(x1, x2); x <= MAX(x1, x2); x++)
    {
        board[x][y] += 1;
    }
}

void draw_diagonal(int board[BOARD_SIZE][BOARD_SIZE], int x1, int y1, int x2, int y2)
{
    int x, y;
    int x_increasing = x1 < x2;
    int y_increasing = y1 < y2;
    for (
        x = x1, y = y1;
        x_increasing ? x <= x2 : x >= x2;
        x_increasing ? x++ : x--, y_increasing ? y++ : y--)
    {
        board[x][y] += 1;
    }
}

int get_number_of_dangerous_areas(int board[BOARD_SIZE][BOARD_SIZE])
{
    int x, y;
    int count = 0;
    for (x = 0; x < BOARD_SIZE; x++)
    {
        for (y = 0; y < BOARD_SIZE; y++)
        {
            if (board[x][y] >= 2)
            {
                count++;
            }
        }
    }
    return count;
}

int main(int argc, char **argv)
{
    FILE *file;
    char *filename;
    int x1, y1, x2, y2;
    int solution;
    int board[BOARD_SIZE][BOARD_SIZE] = {0};

    if (argc != 2)
    {
        /* Wrong number of args */
        fprintf(stderr, "Error: exactly 1 file as arg required\n");
        exit(EXIT_FAILURE);
    }

    /* Open file */
    filename = argv[1];
    file = fopen(filename, "r");
    if (file == NULL)
    {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }

    /* Parse file */
    while (!feof(file))
    {
        fscanf(file, "%i,%i -> %i,%i\n", &x1, &y1, &x2, &y2);
        if (x1 == x2)
        {
            draw_vertical(board, x1, y1, y2);
        }
        else if (y1 == y2)
        {
            draw_horizontal(board, y1, x1, x2);
        }
        else
        {
            /* Only for part 2 */
            draw_diagonal(board, x1, y1, x2, y2);
        }
    }

    /* Close file */
    fclose(file);

    /* Print solution */
    solution = get_number_of_dangerous_areas(board);
    printf("Solution: %i\n", solution);

    exit(EXIT_SUCCESS);
}
