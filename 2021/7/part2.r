# Try it here: https://www.jdoodle.com/execute-r-online/
file <- file("stdin")
data <- scan(file, sep=",")

gaus_triangle <- function(n) n*(n+1)/2
calculate_score <- function(n) {
    unweighted <- abs(n - data)
    weighted <- gaus_triangle(unweighted)
    return(sum(weighted))
}

positions = (0:max(data))
scores <- sapply(positions, calculate_score)
solution <- min(scores)
print(solution)
