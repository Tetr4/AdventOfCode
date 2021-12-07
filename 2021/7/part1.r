# Try it here: https://www.jdoodle.com/execute-r-online/
file <- file("stdin")
data <- scan(file, sep=",")
optimum <- median(data)
solution <- sum(abs(optimum - data))
print(solution)
